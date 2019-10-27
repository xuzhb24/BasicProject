package com.android.frame.http.AATest.bean;

import java.io.Serializable;

/**
 * Created by xuzhb on 2019/10/27
 * Desc:
 */
public class NewsListBean implements Serializable {

    private String path;
    private String image;
    private String title;
    private String passtime;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPasstime() {
        return passtime;
    }

    public void setPasstime(String passtime) {
        this.passtime = passtime;
    }

}
