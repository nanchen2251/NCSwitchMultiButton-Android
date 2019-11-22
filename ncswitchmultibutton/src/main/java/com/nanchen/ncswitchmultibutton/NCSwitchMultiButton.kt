package com.nanchen.ncswitchmultibutton

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import kotlin.math.max
import kotlin.math.min

/**
 * Author: nanchen
 * Email: liushilin2251@gmail.com
 * Date: 2019-11-22 14:24
 */
class NCSwitchMultiButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val DEFAULT_SELECT_COLOR = Color.parseColor("#02cf9c")
    private val DEFAULT_UNSELECT_COLOR = Color.parseColor("#f3f4f6")
    private val DEFAULT_SELECT_TV_COLOR = Color.parseColor("#ffffff")
    private val DEFAULT_UNSELECT_TV_COLOR = Color.parseColor("#4c5059")

    private var selectColor = DEFAULT_SELECT_COLOR
    private var unSelectColor = DEFAULT_UNSELECT_COLOR
    private var selectTvColor = DEFAULT_SELECT_TV_COLOR
    private var unSelectTvColor = DEFAULT_UNSELECT_TV_COLOR
    private var tvSize = 0f
    var styleAppearance: Style = Style.NORMAL

    private val strokePaint by lazy(LazyThreadSafetyMode.NONE) { Paint(Paint.ANTI_ALIAS_FLAG) }
    private val fillPaint by lazy(LazyThreadSafetyMode.NONE) { Paint(Paint.ANTI_ALIAS_FLAG) }
    private val selectedTextPaint by lazy(LazyThreadSafetyMode.NONE) { TextPaint(Paint.ANTI_ALIAS_FLAG) }
    private val unSelectedTextPaint by lazy(LazyThreadSafetyMode.NONE) { TextPaint(Paint.ANTI_ALIAS_FLAG) }

    private var perWidth: Float = 0f
    private var strokeRadius = -1f
    private var strokeWidth = 0f
    private var selectTab = 0
    var onSwitchListener: OnSwitchListener? = null
    private var tabTexts = arrayOf("L", "M", "R")
    private val tvHeightOffset: Float
    private val defaultWidth: Int
        get() {
            var tabTextWidth = 0f
            val tabs = tabTexts.size
            for (i in 0 until tabs) {
                tabTextWidth = max(tabTextWidth, selectedTextPaint.measureText(tabTexts[i]))
            }
            val totalTextWidth = tabTextWidth * tabs
            val totalStrokeWidth = strokeWidth * tabs
            val totalPadding = (paddingLeft + paddingRight) * tabs
            return (totalTextWidth + totalStrokeWidth + totalPadding).toInt()
        }
    private val defaultHeight: Int
        get() = (selectedTextPaint.fontMetrics.bottom - selectedTextPaint.fontMetrics.top).toInt() + paddingTop + paddingBottom


    init {
        context.obtainStyledAttributes(attrs, R.styleable.NCSwitchMultiButton).run {
            styleAppearance = Style.values()[getInt(
                R.styleable.NCSwitchMultiButton_smb_style,
                Style.NORMAL.ordinal
            )]
            tvSize = getDimensionPixelSize(
                R.styleable.NCSwitchMultiButton_smb_tv_size,
                dp2px(14f).toInt()
            ).toFloat()
            selectColor =
                getColor(R.styleable.NCSwitchMultiButton_smb_selected_color, DEFAULT_SELECT_COLOR)
            unSelectColor = getColor(
                R.styleable.NCSwitchMultiButton_smb_unselected_color,
                DEFAULT_UNSELECT_COLOR
            )
            selectTvColor = getColor(
                R.styleable.NCSwitchMultiButton_smb_selected_tv_color,
                DEFAULT_SELECT_TV_COLOR
            )
            unSelectTvColor = getColor(
                R.styleable.NCSwitchMultiButton_smb_unselected_tv_color,
                DEFAULT_UNSELECT_TV_COLOR
            )
            selectTab = getInt(R.styleable.NCSwitchMultiButton_smb_select_pos, 0)
            strokeWidth =
                getDimensionPixelSize(
                    R.styleable.NCSwitchMultiButton_smb_stroke_width,
                    dp2px(1f).toInt()
                ).toFloat()
            strokeRadius =
                getDimensionPixelSize(R.styleable.NCSwitchMultiButton_smb_radius, -1).toFloat()
            val strResId = getResourceId(R.styleable.NCSwitchMultiButton_smb_tabs, 0)
            if (strResId != 0) {
                tabTexts = resources.getStringArray(strResId)
            }
            recycle()
        }

        selectedTextPaint.apply {
            color = selectTvColor
            textSize = tvSize
        }

        unSelectedTextPaint.apply {
            color = unSelectTvColor
            textSize = tvSize
        }

        fillPaint.apply {
            style = Paint.Style.FILL_AND_STROKE
            color = selectColor
        }

        strokePaint.apply {
            style = Paint.Style.STROKE
            color = unSelectColor
        }

        tvHeightOffset = -(selectedTextPaint.ascent() + selectedTextPaint.descent()) * 0.5f
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        Log.e("nanchen2251", "$defaultHeight")
        setMeasuredDimension(
            getExpectSize(defaultWidth, widthMeasureSpec), getExpectSize(
                defaultHeight,
                heightMeasureSpec
            )
        )
    }

    private fun getExpectSize(size: Int, measureSpec: Int): Int {
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)
        return when (specMode) {
            MeasureSpec.EXACTLY -> specSize
            MeasureSpec.UNSPECIFIED -> size
            MeasureSpec.AT_MOST -> min(size, specSize)
            else -> {
                size
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        perWidth = measuredWidth.toFloat() / tabTexts.size
        if (strokeRadius == -1F) {
            strokeRadius = measuredHeight.toFloat() / 2
        }
        checkAttrs()
    }

    private fun checkAttrs() {
        if (strokeRadius > 0.5f * measuredHeight) {
            strokeRadius = 0.5f * measuredHeight
        }
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas == null) return
        val left = strokeWidth * 0.5f
        val top = strokeWidth * 0.5f
        val right = measuredWidth - strokeWidth * 0.5f
        val bottom = measuredHeight - strokeWidth * 0.5f



        strokePaint.run {
            if (styleAppearance == Style.ALL_CIRCLE) {
                color = unSelectColor
                style = Paint.Style.FILL_AND_STROKE
            } else {
                color = selectColor
                style = Paint.Style.STROKE
            }
        }

        canvas.drawRoundRect(
            RectF(left, top, right, bottom),
            strokeRadius,
            strokeRadius,
            strokePaint
        )

        if (styleAppearance == Style.NORMAL) {
            for (i in 1 until tabTexts.size) {
                canvas.drawLine(perWidth * i, top, perWidth * i, bottom, strokePaint)
            }
        }
        //draw tab and line
        for (i in tabTexts.indices) {
            val tabText = tabTexts[i]
            val tabTextWidth = selectedTextPaint.measureText(tabText)
            if (i == selectTab) {
                //draw selected tab
                if (styleAppearance == Style.NORMAL) {
                    when (i) {
                        0 -> drawLeftPath(canvas, left, top, bottom)
                        tabTexts.size - 1 -> drawRightPath(canvas, top, right, bottom)
                        else -> canvas.drawRect(
                            RectF(perWidth * i, top, perWidth * (i + 1), bottom),
                            fillPaint
                        )
                    }
                } else {
                    drawPath(canvas, perWidth * i, top, perWidth * (i + 1), bottom)
                }
                // draw selected text
                canvas.drawText(
                    tabText,
                    0.5f * perWidth * (2 * i + 1).toFloat() - 0.5f * tabTextWidth,
                    measuredHeight * 0.5f + tvHeightOffset,
                    selectedTextPaint
                )
            } else {
                //draw unselected text
                canvas.drawText(
                    tabText,
                    0.5f * perWidth * (2 * i + 1).toFloat() - 0.5f * tabTextWidth,
                    measuredHeight * 0.5f + tvHeightOffset,
                    unSelectedTextPaint
                )
            }
        }
    }

    private fun drawPath(canvas: Canvas, left: Float, top: Float, right: Float, bottom: Float) {
        val path = Path()
        path.moveTo(left + strokeRadius, top)
        path.lineTo(right - strokeRadius, top)
        path.arcTo(
            RectF(right - 2 * strokeRadius, top, right, top + 2 * strokeRadius),
            -90f,
            90f
        )

        path.lineTo(right, top + strokeRadius)
        path.arcTo(
            RectF(right - 2 * strokeRadius, bottom - 2 * strokeRadius, right, bottom),
            0f,
            90f
        )
        path.lineTo(left + strokeRadius, bottom)
        path.arcTo(
            RectF(left, bottom - 2 * strokeRadius, left + 2 * strokeRadius, bottom),
            90f,
            90f
        )
        path.lineTo(left, top + strokeRadius)
        path.arcTo(RectF(left, top, left + 2 * strokeRadius, top + 2 * strokeRadius), 180f, 90f)
        canvas.drawPath(path, fillPaint)
    }

    private fun drawLeftPath(canvas: Canvas, left: Float, top: Float, bottom: Float) {
        val leftPath = Path()
        leftPath.moveTo(left + strokeRadius, top)
        leftPath.lineTo(perWidth, top)
        leftPath.lineTo(perWidth, bottom)
        leftPath.lineTo(left + strokeRadius, bottom)
        leftPath.arcTo(
            RectF(left, bottom - 2 * strokeRadius, left + 2 * strokeRadius, bottom),
            90f,
            90f
        )
        leftPath.lineTo(left, top + strokeRadius)
        leftPath.arcTo(RectF(left, top, left + 2 * strokeRadius, top + 2 * strokeRadius), 180f, 90f)
        canvas.drawPath(leftPath, fillPaint)
    }

    private fun drawRightPath(canvas: Canvas, top: Float, right: Float, bottom: Float) {
        val rightPath = Path()
        rightPath.moveTo(right - strokeRadius, top)
        rightPath.lineTo(right - perWidth, top)
        rightPath.lineTo(right - perWidth, bottom)
        rightPath.lineTo(right - strokeRadius, bottom)
        rightPath.arcTo(
            RectF(right - 2 * strokeRadius, bottom - 2 * strokeRadius, right, bottom),
            90f,
            -90f
        )
        rightPath.lineTo(right, top + strokeRadius)
        rightPath.arcTo(
            RectF(right - 2 * strokeRadius, top, right, top + 2 * strokeRadius),
            0f,
            -90f
        )
        canvas.drawPath(rightPath, fillPaint)
    }

    fun getSelectedTab(): Int {
        return selectTab
    }

    /**
     * set selected tab
     *
     * @param mSelectedTab
     * @return
     */
    fun setSelectedTab(mSelectedTab: Int): NCSwitchMultiButton {
        this.selectTab = mSelectedTab
        invalidate()
        onSwitchListener?.onSwitch(mSelectedTab, tabTexts[mSelectedTab])
        return this
    }

    fun setText(tagTexts: Array<String>): NCSwitchMultiButton {
        if (tagTexts.size > 1) {
            this.tabTexts = tagTexts
            requestLayout()
            return this
        } else {
            throw IllegalArgumentException("the size of tagTexts should greater then 1")
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.run {
            if (action == MotionEvent.ACTION_UP) {
                for (i in tabTexts.indices) {
                    if (x > perWidth * i && x < perWidth * (i + 1)) {
                        if (selectTab != i) {
                            selectTab = i
                            onSwitchListener?.onSwitch(i, tabTexts[i])
                            invalidate()
                        }
                    }
                }
            }
        }
        return true
    }

    private fun dp2px(dp: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.applicationContext.resources.displayMetrics
        )
    }


    interface OnSwitchListener {
        fun onSwitch(position: Int, tabText: String)
    }

    enum class Style {
        NORMAL,
        ALL_CIRCLE
    }
}

