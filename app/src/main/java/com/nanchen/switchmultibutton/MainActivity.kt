package com.nanchen.switchmultibutton

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.nanchen.ncswitchmultibutton.NCSwitchMultiButton
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn2.onSwitchListener = object : NCSwitchMultiButton.OnSwitchListener {
            override fun onSwitch(position: Int, tabText: String) {
                Toast.makeText(applicationContext, tabText, Toast.LENGTH_SHORT).show()
            }

        }
    }
}
