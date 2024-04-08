package com.example.louxiaotian;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Base64;

public class EncryptionTest {


    public static void main(String[] args){
        try{
            EncryptionManager encryptionManager = new EncryptionManager();

            String dataToEncrypt = "Hello World";
            byte[] inputData = dataToEncrypt.getBytes();

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            encryptionManager.encrypt(inputData, outputStream);

            byte[] encryptedData = outputStream.toByteArray();

            String encryptedDataString = Base64.getEncoder().encodeToString(encryptedData);

            System.out.println("Encrypted Data: "+encryptedDataString);

            InputStream inputStream = new ByteArrayInputStream(encryptedData);

            byte[] decryptedData = encryptionManager.decrypt(inputStream);

            String decryptedDataString = new String(decryptedData);

            System.out.println("Decrypted data: "+decryptedDataString);

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
