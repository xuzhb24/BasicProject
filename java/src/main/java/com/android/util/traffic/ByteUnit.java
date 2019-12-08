package com.android.util.traffic;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by xuzhb on 2019/12/8
 * Desc:流量的单位
 */
public class ByteUnit {

    public static final String B = "byte";
    public static final String KB = "kb";
    public static final String MB = "mb";
    public static final String GB = "gb";

    @StringDef({B, KB, MB, GB})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ByteUnitDef {
    }

}
