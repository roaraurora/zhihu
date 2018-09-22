package com.zhihu.demo.util;

import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEByteEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;

public class JasyptUtil {

    /**
     * Jasypt生成加密结果
     *
     * @param password 配置文件中的加密密钥 jasypt.encryptor.password
     * @param value 待加密值
     */
    public static String encryptPwd(String password, String value) {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        encryptor.setConfig(cryptOr(password));
        return encryptor.encrypt(value);
    }

    /**
     * 解密
     * @param password 密钥
     * @param value 待解密密文
     */
    public static String decryptPwd(String password, String value) {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        encryptor.setConfig(cryptOr(password));
        return encryptor.decrypt(value);
    }

    private static SimpleStringPBEConfig cryptOr(String password) {
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword(password);
        config.setAlgorithm(StandardPBEByteEncryptor.DEFAULT_ALGORITHM);
        config.setKeyObtentionIterations("1000");
        config.setPoolSize("1");
        config.setProviderName("SunJCE");
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        config.setStringOutputType("base64");
        return config;
    }

    public static void main(String[] args) {
//        System.out.println(encryptPwd("","3."));
//        System.out.println(decryptPwd("",""));
//        System.out.println(System.getenv("SECRET_KEY")); //
    }
}
