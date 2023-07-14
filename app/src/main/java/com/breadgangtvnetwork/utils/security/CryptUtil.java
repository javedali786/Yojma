package com.breadgangtvnetwork.utils.security;


import android.util.Base64;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class CryptUtil {

    private static CryptUtil cipherUtil;
    private static SecretKeySpec secretKey;
    IvParameterSpec ivSpec = new IvParameterSpec(new byte[]{1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16});
    private static final String ENCODING_METHOD = "AES/GCM/NoPadding";
    private static final String ALGORITHM = "AES";

    @NonNull
    public static CryptUtil getInstance() {
        if (cipherUtil == null) {
            cipherUtil = new CryptUtil();
        }
        return cipherUtil;
    }

    public static void setKey(@NonNull String myKey) {
        MessageDigest sha;
        try {
            byte[] key = myKey.getBytes(StandardCharsets.UTF_8);
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, ALGORITHM);
        } catch (Exception e) {
            //Logger.w(e);
        }
    }

    @Nullable
    public String encrypt(@NonNull String strToEncrypt, @NonNull String secret) {
        try {
            setKey(secret);
            Cipher cipher = Cipher.getInstance(ENCODING_METHOD);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
            return Base64.encodeToString(cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8)), 1);
        } catch (Exception e) {
            //Logger.e("Error while encrypt: " + e);
        }
        return null;
    }

    @Nullable
    public String decrypt(@NonNull String strToDecrypt, @NonNull String secret) {
        try {
            setKey(secret);
            Cipher cipher = Cipher.getInstance(ENCODING_METHOD);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
            return new String(cipher.doFinal(Base64.decode(strToDecrypt, 1)));
        } catch (Exception e) {
            //Logger.e("Error while decrypting: " + e);
        }
        return null;
    }
}
