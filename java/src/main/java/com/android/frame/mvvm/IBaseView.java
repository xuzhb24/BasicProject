package com.android.frame.mvvm;

/**
 * Created by xuzhb on 2021/8/9
 */
public interface IBaseView {

    /**
     * 显示加载弹窗，表示正在加载数据
     */
    void showLoadingDialog();

    /**
     * 显示加载弹窗，表示正在加载数据
     *
     * @param message    加载中的文本提示
     * @param cancelable 用户是否可取消加载弹窗，默认可以
     */
    void showLoadingDialog(String message, boolean cancelable);

    /**
     * 显示加载状态布局，表示正在加载数据，一般和showLoadingDialog只有一个会出现
     */
    void showLoadingLayout();

    /**
     * 数据加载完成，此时可以做一些通用的处理，如取消加载弹窗、完成下拉刷新动作等
     *
     * @param isError 数据是否加载成功，为true时表示数据加载失败
     */
    void loadFinish(boolean isError);

    /**
     * 显示Toast
     *
     * @param text 提示的文本内容
     */
    void showToast(CharSequence text);

    /**
     * 显示Toast
     *
     * @param text      提示的文本内容
     * @param isCenter  是否居中显示，true表示居中显示，默认为false
     * @param longToast 显示长Toast还是短Toast，默认为Toast.LENGTH_SHORT，即短Toast
     */
    void showToast(CharSequence text, boolean isCenter, boolean longToast);

    /**
     * 跳转到登录界面
     */
    void gotoLogin();

}
