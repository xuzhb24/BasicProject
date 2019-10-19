package com.android.util.encrypt;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * Created by xuzhb on 2019/10/17
 * Desc:数字签名
 */
public class SingtureUtil {

    //生成数字签名
    public static String sign(String input, PrivateKey privateKey) throws Exception {
        //获取数字签名实例对象
        Signature signature = Signature.getInstance("SHA256withRSA");
        //初始化签名
        signature.initSign(privateKey);
        //设置数据源
        signature.update(input.getBytes("UTF-8"));
        //签名
        byte[] sign = signature.sign();
        return Base64.encode(sign);
    }

    //数字签名验证
    public static Boolean verify(String input, PublicKey publicKey, String sign) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA");
        //初始化签名
        signature.initVerify(publicKey);
        //传入数据源
        signature.update(input.getBytes("UTF-8"));
        //校验签名信息
        return signature.verify(Base64.decode(sign));
    }

    public static void main(String[] args) throws Exception {
        System.out.println("===============数字签名===============");
        String data = "加密工具aBc123";
        //RSA非对称加密 生成密钥对
        String publicKeyStr2 =
                "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDymOuE7z9SfGv3Czq1ZAwzukNknR1iVTBZurJ6BTh9cBiQUczZ9U4HgBtgIWMRaLNeFjkFMszQz/uDmWQXMJbcsYuSAEJNxUn5KuHAp2YEWfV2CXTj/3I/Q9rhGVq+aLs0rgfCvVXoxFjJV+uGPtzXxhVZTahCcwUo20MSPqI0WwIDAQAB";
        String privateKeyStr2 =
                "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAPKY64TvP1J8a/cLOrVkDDO6Q2SdHWJVMFm6snoFOH1wGJBRzNn1TgeAG2AhYxFos14WOQUyzNDP+4OZZBcwltyxi5IAQk3FSfkq4cCnZgRZ9XYJdOP/cj9D2uEZWr5ouzSuB8K9VejEWMlX64Y+3NfGFVlNqEJzBSjbQxI+ojRbAgMBAAECgYAT4bpzk5Px86Z5gZ8XHJLvblV1mna9B1RGFknoPCNMDHLG6R1Lw5HYhYQ41aOj2pvQmyujJG2qs1DekSSlzeKfHMgoiHiKO/LPLCe4QgbkCsxtTy+SloVM/EirMBCwoPjMwYPqoxVm1a38kCqNgiitMpGooTXflCR0jgGvR9ej+QJBAPwTrCkk6SupWERxQG7yBdCIO9lyWzer9DPa2+JmMcoNydov4bq2ZUCgpum8JkAkmhonM1Oz8x6OFOwcbzHnyZ0CQQD2X3qpvF1Iyc3M/gqZcOSTORQTRkZVSt5ctPnJw6lIGax+HMr9YNccuiwlfi2UtSA0NOtC5yozDS8s9Xt2yHBXAkAzfKkrdjiSDHLU9/TbNF/vqgPfdDYhduPYO5mx8oG07YAPKryGcH7Z5nZxQ1bkvxUixmL7c8Pyt76aQ2yK2vcZAkBLoDFR6t0jm7aNhymPwiSXwHyWEgtC4TFyeab3NRVAaYkWSRZSQqilS8yDUcECFbsl61yP889zTke94Die1JYPAkEAjy3tzsbUeVCMF5CTfAklXSoJbJMZiWvW0S5lolAV9TOVmXFsW+itONAivg7k6HYkxGQF78u6JdcCf5y5p1DqSQ==";

        KeyFactory kf2 = KeyFactory.getInstance("RSA");
        PrivateKey privateKey2 = kf2.generatePrivate(new PKCS8EncodedKeySpec(Base64.decode(privateKeyStr2)));
        PublicKey publicKey2 = kf2.generatePublic(new X509EncodedKeySpec(Base64.decode(publicKeyStr2)));

        String sige = sign(data, privateKey2);
        System.out.println(sige);
        boolean s = verify(data, publicKey2, sige);
        System.out.println(s);
    }

}
