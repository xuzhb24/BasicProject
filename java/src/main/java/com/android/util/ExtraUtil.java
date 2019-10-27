package com.android.util;

import android.app.AlertDialog;
import android.content.Context;

import java.util.Random;

/**
 * Created by xuzhb on 2019/10/15
 * Desc:工具类扩展，存放单个方法
 */
public class ExtraUtil {

    public static void main(String[] args) {
        System.out.println("随机5位字符串：" + createRandomKey(5));
    }

    //生成指定长度由大小写字母、数字组成的随机字符串
    public static String createRandomKey(int length) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            char s = 0;
            int j = random.nextInt(3) % 4;
            switch (j) {
                case 0:
                    //随机生成数字
                    s = (char) (random.nextInt(57) % (57 - 48 + 1) + 48);
                    break;
                case 1:
                    //随机生成大写字母
                    s = (char) (random.nextInt(90) % (90 - 65 + 1) + 65);
                    break;
                case 2:
                    //随机生成小写字母
                    s = (char) (random.nextInt(122) % (122 - 97 + 1) + 97);
                    break;
            }
            sb.append(s);
        }
        return sb.toString();
    }

    public static void alert(Context context, String msg) {
        new AlertDialog.Builder(context)
                .setMessage(msg)
                .show();
    }

}
