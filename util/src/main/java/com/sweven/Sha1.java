package com.sweven;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * <p>Create by Sweven on 2020/7/24 -- 12:02</p>
 * Email: sweventears@163.com
 */
public class Sha1 {
    public static void main(String[] args) {
        File file = new File("D://c.txt");
        String sha1 = getFileSha1(file);
        File file2 = new File("D://b.txt");
        String sha11 = getFileSha1(file2);
        try {
            System.out.println(sha1.equals(sha11));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getFileSha1(File file) {
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            byte[] buffer = new byte[1024 * 1024 * 10];

            int len = 0;
            while ((len = in.read(buffer)) > 0) {
                digest.update(buffer, 0, len);
            }
            String sha1 = new BigInteger(1, digest.digest()).toString(16);
            int length = 40 - sha1.length();
            if (length > 0) {
                for (int i = 0; i < length; i++) {
                    sha1 = "0" + sha1;
                }
            }
            return sha1;
        } catch (IOException | NoSuchAlgorithmException e) {
            System.out.println(e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                System.out.println(e);
            }
        }
        return null;
    }
}
