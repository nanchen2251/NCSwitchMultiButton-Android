# NCSwitchMultiButton-Android
🔥 A SwitchButton support Multiple https://github.com/nanchen2251/NCSwitchMultiButton-Android
## feature
1. switchbutton
2. multitype

## Screenshots

## How to use it
#### Step 1. Add it in your root build.gradle at the end of repositories:
```groovy
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}       
```
#### Step 2. Add the dependency
```groovy
dependencies {
    implementation 'com.github.nanchen2251:NCSwitchMultiButton-Android:Tag:1.0.1'
}
```

#### Step 3. Just use it in your project
```xml
<com.nanchen.ncswitchmultibutton.NCSwitchMultiButton
        android:id="@+id/btn2"
        android:layout_width="match_parent"
        android:layout_height="35dp"
        android:layout_marginTop="20dp"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/btn1"
        app:smb_style="all_circle"
        app:smb_tabs="@array/test" />
```
#### Step 3. Support for custom attributes
```xml
<declare-styleable name="NCSwitchMultiButton">
        <attr name="smb_style" format="enum">
            <enum name="normal" value="0" />
            <enum name="all_circle" value="1" />
        </attr>
        <attr name="smb_tv_size" format="dimension" />
        <attr name="smb_tabs" format="reference" />
        <attr name="smb_selected_color" format="color|reference" />
        <attr name="smb_unselected_color" format="color|reference" />
        <attr name="smb_selected_tv_color" format="color|reference" />
        <attr name="smb_unselected_tv_color" format="color|reference" />
        <attr name="smb_select_pos" format="integer" />
        <attr name="smb_stroke_width" format="dimension" />
        <attr name="smb_radius" format="dimension" />
    </declare-styleable>
```

#### Step 5. If you still don't understand, please refer to the demo

### About the author
    nanchen<br>
    Chengdu,China<br>
    [其它开源](https://github.com/nanchen2251/)<br>
    [个人博客](https://nanchen2251.github.io/)<br>
    [简书](http://www.jianshu.com/u/f690947ed5a6)<br>
    [博客园](http://www.cnblogs.com/liushilin/)<br>
    交流群：118116509<br>
    欢迎投稿(关注)我的唯一公众号，公众号搜索 nanchen 或者扫描下方二维码：<br>
    ![](https://github.com/nanchen2251/Blogs/blob/master/images/nanchen12.jpg)
    
## Licenses
```
 Copyright 2019 nanchen(刘世麟)

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
```

