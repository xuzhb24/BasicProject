package com.android.util.regex;

/**
 * Created by xuzhb on 2020/4/11
 * Desc:正则相关常量
 * 特殊字符：
 * ^：匹配输入字符串的开始位置
 * $：匹配输入字符串的结尾位置，举个例子："^-?\\d+$"匹配整数"1"，如果不带$，那么"^-?\\d+"不仅匹配整数"1"，也匹配"1asdd"这种格式；
 * *：匹配前面的子表达式零次或多次
 * +：匹配前面的子表达式一次或多次，也就是说至少匹配一次
 * ?：匹配前面的子表达式零次或一次
 * |：即竖线，指明两项之间的一个选择
 * \:将下一个字符标记为或特殊字符、或原义字符、或向后引用、或八进制转义符，例如'n'匹配字符'n'、'\n'匹配换行符、'\\'匹配'\'、'\.'匹配'.'
 * ()：标记一个子表达式的开始和结束位置
 * []：标记一个中括号表达式的开始和结束位置
 * {}：标记限定符表达式的开始和结束位置
 * 限定符（除了*、+和?还有）：
 * {n}：n是一个非负整数，匹配确定的n次
 * {n,}：n是一个非负整数，至少匹配n次
 * {n,m}：m和n均为非负整数，其中n<=m，最少匹配n次且最多匹配m次，如{1,3}表示匹配1到3个
 * 更多语法参考：https://www.runoob.com/regexp/regexp-syntax.html
 * 正则表达式在线测试：http://tool.oschina.net/regex
 * 正则表达式更多例子：http://toutiao.com/i6231678548520731137
 */
public class RegexConstant {

    /*---------------------------------校验数字的正则表达式 start---------------------------------*/

    //数字，包括整数和小数、正数和负数，不匹配"+"开头的数字，如需匹配使用"^[-\\+]?xxx"，下同
    public static final String DIGIT = "^-?(\\d+(\\.\\d+)?)$";

    //整数
    public static final String INTEGER = "^-?\\d+$";

    //非0的正整数
    public static final String POSITIVE_INTEGER = "^[1-9]\\d*$";

    //非0的负整数
    public static final String NEGATIVE_INTEGER = "^-[1-9]\\d*$";

    //非正整数，即负整数和0
    public static final String NOT_POSITIVE_INTEGER = "^-[1-9]\\d*$|0$";

    //非负整数，即正整数和0
    public static final String NOT_NEGATIVE_INTEGER = "^[1-9]\\d*$|0$";

    //浮点数，不包括整数
    public static final String FLOAT = "^-?[1-9]\\d*\\.\\d+$|^-?0\\.\\d+$";

    //正浮点数，不包括整数
    public static final String POSITIVE_FLOAT = "^[1-9]\\d*\\.\\d+$|^0\\.\\d+$";

    //负浮点数，不包括整数
    public static final String NEGATIVE_FLOAT = "^-[1-9]\\d*\\.\\d+$|-0\\.\\d+$";

    //非正浮点数，包括负浮点数和0或0.00
    public static final String NOT_POSITIVE_FLOAT = "^-[1-9]\\d*\\.\\d+$|-0\\.\\d+$|0(\\.\\0*)?$";

    //非负浮点数，包括正浮点数和0或0.00
    public static final String NOT_NEGATIVE_FLOAT = "^[1-9]\\d*\\.\\d+$|^0\\.\\d+$|0(\\.\\0*)?$";

    //带2位小数的浮点数
    public static final String FLOAT_WITH_TWO_DECIMAL = "^-?[1-9]\\d*\\.\\d{2}$|^-?0\\.\\d{2}$";

    /*---------------------------------校验数字的正则表达式 end---------------------------------*/


    /*---------------------------------校验字符的正则表达式 start---------------------------------*/

    //汉字
    public static final String CHINESE = "^[\\u4e00-\\u9fa5]+$";

    //双字节字符(包括汉字在内)
    public static final String DOUBLE_BYTE_CHAR = "[^\\x00-\\xff]";

    //英文字母或数字，至少包含一个字符
    public static final String LETTER_OR_DIGIT = "^[A-Za-z0-9]+$";

    //英文字母和数字，至少包含一个英文字母和一个数字，"(?!xxx)"标识预测该位置后不是xxx字符
    public static final String LETTER_AND_DIGIT = "^(?=.*[0-9].*)(?=.*[A-Za-z].*).{2,}$";

    //大写字母和小写字母和数字，至少包含一个大写字母、一个小写字母和一个数字
    public static final String UPPER_AND_LOWER_CASE_LETTER_AND_DIGIT = "^(?=.*[0-9].*)(?=.*[A-Z].*)(?=.*[a-z].*).{3,}$";

    /*---------------------------------校验字符的正则表达式 end---------------------------------*/


    /*---------------------------------特殊需求的正则表达式 start---------------------------------*/

    //手机号（简单）
    public static final String MOBILE_SIMPLE = "^[1]\\d{10}$";

    /**
     * 手机号（精确）
     * 移动：134(0-8)、135、136、137、138、139、147、150、151、152、157、158、159、178、182、183、184、187、188
     * 联通：130、131、132、145、155、156、175、176、185、186
     * 电信：133、153、173、177、180、181、189
     * 全球星：1349
     * 虚拟运营商：170
     */
    public static final String MOBILE_EXACT = "^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0,3,5-8])|(18[0-9])|(147))\\d{8}$";

    //固定电话号码
    public static final String TEL_PHONE = "^0\\d{2,3}[- ]?\\d{7,8}";

    //15位身份证号码
    public static final String ID_CARD_15 = "^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$";

    //18位身份证号码
    public static final String ID_CARD_18 = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9Xx])$";

    //邮箱
    public static final String EMAIL = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";

    //URL
    public static final String URL = "[a-zA-z]+://[^\\s]*";

    //yyyy-MM-dd格式的日期校验，已考虑平闰年
    public static final String DATE = "^(?:(?!0000)[0-9]{4}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1[0-9]|2[0-8])|(?:0[13-9]|1[0-2])-(?:29|30)|(?:0[13578]|1[02])-31)|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)-02-29)$";

    //IP地址
    public static final String IP = "((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)";

    //QQ号
    public static final String QQ = "[1-9][0-9]{4,}";

    //中国邮政编码
    public static final String POSTAL_CODE = "[1-9]\\d{5}(?!\\d)";

    /*---------------------------------特殊需求的正则表达式 end---------------------------------*/

}
