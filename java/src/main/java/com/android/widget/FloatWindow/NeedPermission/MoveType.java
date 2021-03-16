package com.android.widget.FloatWindow.NeedPermission;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by xuzhb on 2021/3/8
 * Desc:移动方式
 */
public class MoveType {

    public static final int inactive = 1;  //不可移动
    public static final int active = 2;    //可移动
    public static final int slide = 3;     //移动松手后贴边
    public static final int back = 4;      //移动松手后回到原来的位置

    @IntDef({inactive, active, slide, back})
    @Retention(RetentionPolicy.SOURCE)
    @interface moveType {
    }

}
