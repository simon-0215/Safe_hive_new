package com.example.louxiaotian.kdc;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

// The key auto updates itself
public class Key {
    private String key;

    // Observer pattern (notifies parent manager that the key value has changed)
    private KDCManagement manager;

    public Key(KDCManagement manager) {
        this.manager = manager;
        generateKey();
        System.out.println(key);
        Timer timer = new Timer();
        timer.schedule(new UpdateKey(), 10000, 10000); // Schedule the task to run every ___ MS
    }

    // Random Alphanumeric string
    private void generateKey() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        this.key = generatedString;
    }

    public String getKeyValue() {
        return key;
    }

    private class UpdateKey extends TimerTask {
        public void run() {
            generateKey();
            manager.notifyKeyUpdate(key);
        }
    }

}