package com.example.louxiaotian.Encryption;

import com.example.louxiaotian.EncryptionManager;

import java.io.InputStream;
import java.io.OutputStream;

public class EncryptionAdapter {
    static EncryptionManager em = new EncryptionManager();

    public static byte[] encrypt(byte[] ByteArray, OutputStream outputStream){
        return em.encrypt(ByteArray, outputStream);
    }

    public static byte[] decrypt(InputStream inputStream){
        return em.decrypt(inputStream);
    }
}
