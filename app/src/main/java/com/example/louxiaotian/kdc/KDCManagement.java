package com.example.louxiaotian.kdc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KDCManagement {
    // maps userId: Key
    Map<Integer, Key> distributedKeys = new HashMap();

    public Key generateKey(int userId) {
        Key key = new Key(this);
        distributedKeys.put(userId, key);
        return key;
    }

    public void notifyKeyUpdate(String updatedKey) {
        for (Map.Entry<Integer, Key> userKey : distributedKeys.entrySet()) {
            if (userKey.getValue().getKeyValue().equals(updatedKey)) {
                System.out.println("User " + userKey.getKey() + ": Key updated to " + userKey.getValue().getKeyValue());
            }
        }

    }

    // main FUNCTION FOR TESTING PURPOSES
//    public static void main(String[] args) {
//        KDCManagement kdcManagement = new KDCManagement();
//        kdcManagement.generateKey(1);
//        kdcManagement.generateKey(2);
//
//    }
}
