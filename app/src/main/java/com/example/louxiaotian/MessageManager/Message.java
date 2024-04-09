package com.example.louxiaotian.MessageManager;

public class Message {
    public static String KEY_SENDER = "senderUsername";
    public static String reciever = "senderUsername";
    public static String text = "text";
    public static String timestamp = "senderUsername";

    public Message(String r, String t, String time){
        this.reciever = r;
        this.text = t;
        this.timestamp = time;
    }

    public String getText(){
        return this.text;
    }

}
