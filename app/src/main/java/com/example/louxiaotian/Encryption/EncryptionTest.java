package com.example.louxiaotian.Encryption;

import java.nio.charset.StandardCharsets;

public class EncryptionTest {

    static EncryptionJava ej = new EncryptionJava();
    static String originalMessage = "Hello, World!";
    static byte[] encryptedMessage = ej.encrypt(originalMessage);
    // Decrypt a received message
    static String decryptedMessage = ej.decrypt(encryptedMessage);

    static String encrypted = "";


    public String get_encrypted(){
        if (encryptedMessage !=null){
            encrypted = new String(encryptedMessage, StandardCharsets.UTF_8);
        }
        return encrypted;
    }

    public String get_decrypted(){
        return decryptedMessage;
    }

}
