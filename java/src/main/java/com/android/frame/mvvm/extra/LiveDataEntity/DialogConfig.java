package com.android.frame.mvvm.extra.LiveDataEntity;

/**
 * Created by xuzhb on 2021/8/9
 * Desc:对话框配置信息
 */
public class DialogConfig {

    private String message;
    private boolean cancelable;

    public DialogConfig(String message, boolean cancelable) {
        this.message = message;
        this.cancelable = cancelable;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isCancelable() {
        return cancelable;
    }

    public void setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
    }

}
