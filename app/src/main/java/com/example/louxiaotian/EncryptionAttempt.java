package com.example.louxiaotian;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

//https://www.youtube.com/watch?v=bVXT_AkHIEQ
public class EncryptionAttempt extends AppCompatActivity {
    private EditText editText;
    private ListView listView;
    private DatabaseReference databaseReference;
    private String stringMessage;
    private byte encryptionKey[]={1,2,3,4,5,6,7,8,9,0,1,2,3,4,5,6};
    private Cipher cipher,decipher;
    private SecretKeySpec secretKeySpec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //shouldn't be commented out but for some reason its not working.
        //setContentView(R.layout.activity_chat_screen);
        //editText = findViewById(R.id.editText);
        //listView = findViewById(R.id.listView);
        try {
            databaseReference = FirebaseDatabase.getInstance().getReference("Message");

            try {
                cipher = Cipher.getInstance("AES");
                decipher = Cipher.getInstance("AES");
            } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
                e.printStackTrace();
            }

            secretKeySpec = new SecretKeySpec(encryptionKey, "AES");

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    stringMessage = (String) dataSnapshot.getValue();
                    if (stringMessage != null) {
                        stringMessage = stringMessage.substring(1, stringMessage.length() - 1);
                    }

                    assert stringMessage != null;
                    String[] stringMessageArray;
                    stringMessageArray  = ", ".split(stringMessage);
                    Arrays.sort(stringMessageArray);
                    String[] stringFinal = new String[stringMessageArray.length * 2];

                    try {
                        for (int i = 0; i < stringMessageArray.length; i++) {
                            String[] stringKeyValue = stringMessageArray[i].split("=", 2);
                            stringFinal[2 * i] = (String) android.text.format.DateFormat.format("dd-MM-yyyy hh:mm:ss", Long.parseLong(stringKeyValue[0]));
                            stringFinal[2 * i + 1] = AESDecryptionMethod(stringKeyValue[1]);
                        }


                        listView.setAdapter(new ArrayAdapter<String>(EncryptionAttempt.this, android.R.layout.simple_list_item_1, stringFinal));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }


    }

    private String AESEncryptionMethod(String string){
        byte[] stringByte = string.getBytes();
        byte[] encrypteByte = new byte[stringByte.length];

        try {
            cipher.init(Cipher.ENCRYPT_MODE,secretKeySpec);
            encrypteByte=cipher.doFinal(stringByte);
        } catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        String returnString=null;
        returnString = new String(encrypteByte, StandardCharsets.ISO_8859_1);
        return returnString;
    }
    private String AESDecryptionMethod(String string) throws UnsupportedEncodingException {
        byte[] EncryptedByte = string.getBytes(StandardCharsets.ISO_8859_1);
        String decryptedString = string;

        byte[] decryption;

        try {
            decipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            decryption = decipher.doFinal(EncryptedByte);
            decryptedString = new String(decryption);
        } catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return decryptedString;
    }

    public void sendButton(View view) {
        Date date=new Date();
        databaseReference.child(Long.toString(date.getTime())).setValue(AESEncryptionMethod(editText.getText().toString()));
        editText.setText("");
    }
}
