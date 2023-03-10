package com.yanhuanxy.multifunexport.tools.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * AES 加密 需要在配置文件中设置 key
 * 否则使用 默认key
 *
 * @author yym
 * @date 2020/08/27
 */
public class AESUtil {
    private static final Logger log = LoggerFactory.getLogger(AESUtil.class);
    private static String defaultkey = "AD42F6697B035B75";
    private static String DEFAULT_CIPHER_ALGORITHM = "SHA1PRNG";
    private static String KEY_ALGORITHM = "AES";
    public static final String DEFAULT_CODING = "utf-8";

    private static String dataSourceAESKey = "AD42F6697B035B75";

    public static String encrypt(String content, String key) throws Exception {
        byte[] input = content.getBytes(DEFAULT_CODING);
        SecretKeySpec skc = new SecretKeySpec(key.getBytes(DEFAULT_CODING), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, skc);

        byte[] cipherText = new byte[cipher.getOutputSize(input.length)];
        int ctLength = cipher.update(input, 0, input.length, cipherText, 0);
        ctLength += cipher.doFinal(cipherText, ctLength);
        return byte2HexStr(cipherText).toUpperCase();
    }
    /**
     * 字节转16进制数组
     */
    private static String byte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex);
        }
        return sb.toString();
    }
    /**
     * 解密
     */
    public static String decrypt(String encrypted, String key) throws Exception {
        byte[] keyb = key.getBytes(DEFAULT_CODING);
        SecretKeySpec skey = new SecretKeySpec(keyb, "AES");
        Cipher dcipher = Cipher.getInstance("AES");
        dcipher.init(Cipher.DECRYPT_MODE, skey);
        byte[] clearbyte = dcipher.doFinal(toBytes(encrypted));
        return new String(clearbyte, StandardCharsets.UTF_8);
    }

    public static String aesDecryptForFront(String encryptStr, String decryptKey) {
        if (StringUtils.isEmpty(encryptStr) || StringUtils.isEmpty(decryptKey)) {
            return null;
        }
        try {
            byte[] encryptByte = Base64.getDecoder().decode(encryptStr);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(decryptKey.getBytes(), "AES"));
            byte[] decryptBytes = cipher.doFinal(encryptByte);
            return new String(decryptBytes,StandardCharsets.UTF_8);

        } catch (Exception var3) {
            var3.printStackTrace();
            return null;
        }
    }
    /**
     * 字符串转字节数组
     */
    private static byte[] toBytes(String hexString) {
        int len = hexString.length() / 2;
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++) {
            result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2), 16).byteValue();
        }
        return result;
    }

    /**
     * 加密
     *
     * @param key
     * @param messBytes
     * @return
     */
    private static byte[] encrypt(Key key, byte[] messBytes) throws Exception {
        if (key != null) {

            Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return cipher.doFinal(messBytes);
        }
        return null;
    }

    /**
     * AES（256）解密
     *
     * @param key
     * @param cipherBytes
     * @return
     */
    public static byte[] decrypt(Key key, byte[] cipherBytes) throws Exception {
        if (key != null) {

            Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);
            return cipher.doFinal(cipherBytes);
        }
        return null;
    }


    /**
     * 生成加密秘钥
     *
     * @return
     * @throws NoSuchAlgorithmException
     */
    private static KeyGenerator getKeyGenerator() throws NoSuchAlgorithmException{

        String key = defaultkey;
        if(dataSourceAESKey != null){
            key = dataSourceAESKey;
        }

        KeyGenerator keygen = KeyGenerator.getInstance(KEY_ALGORITHM);
        SecureRandom secureRandom = SecureRandom.getInstance(DEFAULT_CIPHER_ALGORITHM);
        secureRandom.setSeed(key.getBytes());
        keygen.init(128, secureRandom);

        return keygen;
    }

    public static String encrypt(String message) {
        try {
            KeyGenerator keygen = getKeyGenerator();
            SecretKey secretKey = new SecretKeySpec(keygen.generateKey().getEncoded(), KEY_ALGORITHM);
            return Base64.getEncoder().encodeToString(encrypt(secretKey, message.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            log.warn("AES content encrypt error ");
        }
        return null;
    }

    public static String decrypt(String ciphertext) {
        try {
            KeyGenerator keygen = getKeyGenerator();
            SecretKey secretKey = new SecretKeySpec(keygen.generateKey().getEncoded(), KEY_ALGORITHM);
            return new String(decrypt(secretKey, Base64.getDecoder().decode(ciphertext)), StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.warn("AES content decrypt error ");
        }
        return null;
    }

    public static void main(String[] args) {
        String message = "root";
        String ciphertext = encrypt(message);

        System.out.println("加密后密文为: " + ciphertext);
        System.out.println("解密后明文为:" + decrypt("gJcRooRwqjVlGTdxqoqETLQZZH25k74zkOEIoyk9EVcjkJvnt4RX5Hm073E3dS8O"));
    }

}
