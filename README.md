# 让这个库完成50%的通用开发，剩下的50%跟随业务推进

[<font face="微软雅黑" size=5>命名规范</font>](#nameNorm)   
[<font face="微软雅黑" size=5>代码清单</font>](#codeList)

# <span id="nameNorm">命名规范</span>
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

# <span id="codeList">代码清单</span>

## 框架篇 frame
* ### camera.zxing：扫码框架zxing
~~~
CaptureActivity：扫码的Activity类
~~~
* ### guide：引导页
* ### http：Retrofit框架封装
~~~
RetrofitFactory：配置Retrofit
ExceptionUtil：异常处理
SchedulerUtil：简化RxJava的subscribeOn和observeOn调用
model.BaseResponse：bean基类
model.BaseListResponse：列表类型的bean基类
interceptor.HttpCacheInterceptor：网络缓存拦截器，只支持GET请求
interceptor.HttpLogInterceptor：日志拦截器，会将请求报文和响应报文Json格式化
interceptor.MaxRetryInterceptor：限制最多重试次数的拦截器
~~~
* ### mvc：MVC框架封装
~~~
BaseActivity：基类Activity，和CustomObserver配合使用
BaseFragment：基类Fragment，和CustomObserver配合使用
BaseListActivity：列表数据对应的基类Activity，和CustomObserver配合使用
BaseListFragment：列表数据对应的基类Fragment，和CustomObserver配合使用
IBaseView：Activity和Fragment共同调用的方法集合
WebviewActivity：H5 Activity基类
WebviewFragment：H5 Fragment基类
extra.http.CustomObserver：Rxjava自定义Observer，加入加载状态布局、加载弹窗、网络错误的处理
extra.RecyclerView.CustomLoadMoreView：BaseQuickAdapter中自定义加载更多的脚布局
extra.RecyclerView.LoadMoreAdapter：实现上拉加载更多的Adapter，继承至BaseQuickAdapter
~~~
* ### mvp：MVP框架封装
~~~
BaseActivity：基类Activity
BaseFragment：基类Fragment
IBaseView：MVP中的基类View
BasePresenter：MVP中的基类Presenter
BaseListActivity：列表数据对应的基类Activity
BaseListFragment：列表数据对应的基类Fragment
IBaseListView：列表数据对应的基类View
BaseListPresenter：列表数据对应的基类Presenter
CommonBaseActivity：不需要额外声明View和Presenter的Activity的父类
CommonBaseFragment：不需要额外声明View和Presenter的Fragment的父类
extra.http.CustomObserver：Rxjava自定义Observer，加入加载状态布局、加载弹窗、网络错误的处理
extra.RecyclerView.CustomLoadMoreView：BaseQuickAdapter中自定义加载更多的脚布局
extra.RecyclerView.LoadMoreAdapter：实现上拉加载更多的Adapter，继承至BaseQuickAdapter
~~~
* ### permission：权限动态申请示例
~~~
PermissionFrameActivity：使用EasyPermission实现权限动态申请
PermissionRequestActivity：原生API处理权限动态申请和回调
~~~
* ### TestLeakActivity：测试leakcanary检测内存泄漏

## 控件篇 widget
* ### dialog：Dialog对话框
~~~
BaseDialog：基类的Dialog
CommonDialog：通用的Dialog，可以实现不同布局的Dialog
ConfirmDialog：常见单/双按钮的Dialog
~~~
* ### FloatWindow：悬浮窗
~~~
NeedPermission：需要申请悬浮窗权限才能创建的悬浮窗
NoPermission：无需申请权限的应用内全局悬浮窗
~~~
* ### LetterIndexBar：通讯录字母索引
* ### LineChart：图表相关
~~~
type1.LineChart：曲线图
type2.LineChart：双折线图
~~~
* ### LoadingDialog：加载框
* ### LoadingLayout：加载状态布局
* ### PhotoViewer：图片查看器
* ### PicGetterDialog：拍照或从相册选取选择框
~~~
BasePicGetterDialog：拍照或从相册选取选择框基类，提供一些基础的方法，可以继承该类实现自定义的UI布局
PicGetterDialog：默认实现的拍照或从相册选取选择框
~~~
* ### PieChart：饼状图
~~~
type1.PieChart：带延长线的饼状图
type2.PieChart：饼状图
~~~
* ### PopupWindow：PopupWindow封装
~~~
CommonPopupWindow：通用的PopupWindow，可以实现不同的UI布局
WindowHelper：Window辅助类，实现背景灰色效果
~~~
* ### ProgressBar：进度条
~~~
CircleProgressBar：圆形进度条
~~~
* ### RecyclerView：RecyclerView.Adapter封装
~~~
BaseAdapter：通用的RecyclerView适配器基类
LoadMoreAdapter：实现上拉加载更多的Adapter
LoadMoreWrapper：使用装饰着模式实现上拉加载更多，目前支持LinearLayoutManager和GridLayoutManager
~~~
* ### ArcView：自定义圆弧
* ### CustomViewPager：自定义ViewPager，控制ViewPager是否可以左右滑动
* ### ExpandTextView：点击展开/收起的TextView
* ### IndicatorLayout：指示器
* ### InputLayout：带删除按钮的输入框
* ### MaxHeightRecyclerView：支持maxHeight属性的RecyclerView
* ### PasswordEditText：密码输入框
* ### ScrollTextView：TextView文字滚动显示，即跑马灯效果（不需要获取焦点）
* ### SearchLayout：搜索框
* ### SuffixTextView：带有后缀的TextView
* ### TitleBar：标题栏
* ### ViewHolder：缓存view，减少findViewById以及优化控件方法的调用

## 工具篇 util
* ### ActivityUtil：Activity相关工具类
* ### AppUtil：应用相关工具类
* ### BitmapUtil：图片工具
* ### BarCodeUtil：条形码工具类
* ### QRCodeUtil：二维码工具类
* ### encrypt：加解密工具
~~~
AESCBCUtil：AES加解密工具，CBC模式
AESUtil：AES加解密工具，ECB模式
Base64：Base64编码解码
DESCBCUtil：DES加解密工具，CBC模式
DESUtil：DES加解密工具，ECB模式
MessageDigestUtil：信息摘要加密
RSAUtil：非对称加密
SingtureUtil：数字签名
~~~
* ### GlideUtil：Glide工具类
* ### LocationUtil：位置工具类
* ### PermissionUtil：权限管理工具
* ### RegexUtil：正则表达式工具类
* ### ServiceUtil：服务相关工具类
* ### StatusBarUtil：实现沉浸式状态栏
* ### threadPool：线程池工具
~~~
StepThread：封装Callable接口
ThreadPoolManager：线程池管理器
ThreadPoolUtil：线程池工具类
~~~
* ### AlertDialogUtil：AlertDialog工具类
* ### ApkDownloadUtil：APK下载工具类
* ### CacheUtil：磁盘缓存工具类
* ### CheckFastClickUtil：快速点击检测工具
* ### CleanUtil：应用文件清除工具类
* ### CPUUtil：CPU工具类
* ### CrashHandler：崩溃异常监听
* ### DataDueUtil：数据缓存是否过期
* ### DateUtil：时间相关工具类
* ### DeviceUtil：设备相关工具类
* ### DigitalUtil：一些浮点数格式化的工具类
* ### DrawableUtil：代码创建Drawable
* ### EncodeUtil：编码解码相关工具类
* ### FileUtil：文件管理类
* ### IntentUtil：Intent相关工具类
* ### IOUtil：IO流相关工具类
* ### JsonUtil：Json工具类
* ### KeyboardUtil：软键盘管理工具类
* ### LayoutParamsUtil：布局参数工具类
* ### LogUtil：日志工具
* ### NetReceiver：监听网络变化
* ### NetworkUtil：网络工具
* ### NotificationUtil：通知管理
* ### OnAppBarStateChangeListener：监听AppBarLayout的展开与折叠
* ### OnMultiClickListener：连续点击事件监听器
* ### PhoneUtil：手机相关工具类
* ### PickerViewUtil：底部选择器Android-PickerView工具类
* ### PinyinUtil：拼音工具类
* ### ScreenUtil：屏幕相关工具类
* ### SDCardUtil：SD卡工具类
* ### ShellUtil：Shell相关工具类，执行shell或root命令
* ### SizeUtil：尺寸信息单位转换
* ### SpannableStringUtil：SpannableString工具类
* ### SPUtil：SharedPreferences工具类
* ### StringUtil：字符串工具类
* ### ToastUtil：自定义Toast
* ### TransformUtil：转换工具
* ### VibrationUtil：震动相关工具类
* ### ZipUtil：压缩工具类

## 公共库 universal
* ### TestSystemWidgetActivity、TestSystemWidgetFragment：记录一些系统自带控件的使用

## 一些好用的开源库
* ### [轮播图 youth.banner](https://github.com/youth5201314/banner)
* ### [RecyclerView适配器 BaseRecyclerViewAdapterHelper](https://github.com/CymChad/BaseRecyclerViewAdapterHelper)
* ### [下拉刷新框架 SmartRefreshLayout](https://github.com/scwang90/SmartRefreshLayout)
* ### [导航栏切换 MagicIndicator](https://github.com/hackware1993/MagicIndicator)
* ### [换肤框架 Android-skin-support](https://github.com/ximsfei/Android-skin-support)
* ### [仿iOS时间选择器 Android-PickerView](https://github.com/Bigkoo/Android-PickerView)

# 本库也参考和收集了一些优秀开源库的代码，特此感谢！！！
