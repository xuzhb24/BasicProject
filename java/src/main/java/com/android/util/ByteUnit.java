package com.android.util;

import androidx.annotation.StringDef;

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

    private static final double UNIT = 1024;

    public static double convertByteUnit(double totalBytes, @ByteUnitDef String unit) {
        if (totalBytes > 0) {
            if (ByteUnit.KB.equals(unit)) {
                totalBytes /= Math.pow(UNIT, 1);
            } else if (ByteUnit.MB.equals(unit)) {
                totalBytes /= Math.pow(UNIT, 2);
            } else if (ByteUnit.GB.equals(unit)) {
                totalBytes /= Math.pow(UNIT, 3);
            }
        }
        return totalBytes;
    }

}
