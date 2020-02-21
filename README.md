<span id="nameNorm"><font face="微软雅黑" size=5 color=#db4b3c>命名规范</font></span>  
<span id="codeList"><font face="微软雅黑" size=5 color=#db4b3c>代码清单</font></span>

# [命名规范](#nameNorm)
* Activity  
第一种：标题_Activity，如标题名称为"我的"命名为"MineActivity"  
第二种：功能_Activity，如登录页命名为"LoginActivity"  
* Fragment (同Activity)
* 布局文件  
1、Activity对应布局文件命名为"activity_前缀"，如LoginActivity命名为"activity_login"  
2、Fragment对应布局文件命名为"fragment_前缀"，如MainFragment命名为"fragment_main"  
3、对话框Dialog和DialogFragment对应布局文件命名为"dialog_xxx"，如确定对话框命名为"dialog_confirm"    
4、列表RecyclerView和ListView对应Adapter的item布局命名为"item_xxx"，如DetailAdater的item布局命名为"item_detail"  
5、通过<include>标签引用的子布局或其他布局，如复合控件的布局命名为"layout_xxx"，如TitleBar的布局命名为"layout_title_bar"    
* 控件id  
名称_控件类型，如文本为"登录"的按钮Button命名为"login_btn"  
~~~
Button        xxx_btn
TextView      xxx_tv
EditText      xxx_et
LinearLayout  xxx_ll
FrameLayout   xxx_fl
以此类推...
~~~
* 图标图片  
ic_功能，如删除图片，如删除图标命名为"ic_delete"  
* anim动画文件  
动画类型_xxx，举个例子  
~~~xml
<!--这个动画是个平移动画，表示从底部滑动上去，命名为"translate_bottom_in"-->
<set xmlns:android="http://schemas.android.com/apk/res/android">
    <translate
        android:duration="200"
        android:fromYDelta="100%p"
        android:toYDelta="0%p" />
</set>
~~~
* ShapeDrawable  
shape_xxx，举个例子
~~~xml
<!--shape_corners_10_solid_ffffff_stroke_1_db4b3c-->
<shape xmlns:android="http://schemas.android.com/apk/res/android">
    <corners android:radius="10dp" />
    <solid android:color="#ffffff" />
    <stroke
        android:width="1dp"
        android:color="#db4b3c" />
</shape>
~~~
~~~xml
<!--shape_oval_size_6_solid_ff5400-->
<shape xmlns:android="http://schemas.android.com/apk/res/android"
    android:shape="oval">
    <size
        android:width="6dp"
        android:height="6dp" />
    <solid android:color="#FF5400" />
</shape>
~~~
~~~xml
<!--shape_corners_top_20_solid_f3f5fb-->
<shape xmlns:android="http://schemas.android.com/apk/res/android">
    <corners
        android:topLeftRadius="20dp"
        android:topRightRadius="20dp" />
    <solid android:color="#F3F5FB" />
</shape>
~~~
~~~xml
<!--shape_corners_top_left_20_solid_f3f5fb-->
<shape xmlns:android="http://schemas.android.com/apk/res/android">
    <corners
        android:topLeftRadius="20dp" />
    <solid android:color="#F3F5FB" />
</shape>
~~~
* SelectorDrawable  
selector_xxx，如按钮的Drawable命名为"selector_btn_bg"  

***

# [代码清单](#codeList)

## 框架篇 frame
* ### camera/zxing：扫码框架zxing
* ### http：Retrofit框架

## 控件篇 widget

## 工具篇 util


## 公共库 universal
