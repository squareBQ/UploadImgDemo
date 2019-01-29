package com.zahi.one.uploadimgdemo.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtils {

    public static File create(String folder, String fileName) {
        File file = new File(
                address(folder, fileName));
        Log.d("--------------", "Create file address: " + address(folder, fileName));
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }

    public static void forceCreate(String folder, String fileName) {
        File dirs = new File(folder);
        dirs.mkdirs();

        File file = new File(
                address(folder, fileName));

        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void delete(String folder, String fileName) {
        File file = new File(
                address(folder, fileName));
        file.delete();
    }

    public static long size(String folder, String fileName) {
        File file = new File(
                address(folder, fileName));
        return file.length();
    }

    public static FileOutputStream getOutputStream(String folder, String fileName) {
        FileOutputStream fileOut = null;
        try {
            fileOut = new FileOutputStream(
                    address(folder, fileName));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return fileOut;
    }

    public static FileInputStream getInputStream(String folder, String fileName) {
        FileInputStream fileIn = null;
        try {
            fileIn = new FileInputStream(
                    address(folder, fileName));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return fileIn;
    }

    public static String address(String folder, String file) {
        return folder + "/" + file;
    }

    /**
     * bitmap转为base64
     *
     * @param bitmap
     * @return
     */
    public static String bitmapToBase64(Bitmap bitmap) {

        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * base64转为bitmap
     *
     * @param base64Data
     * @return
     */
    public static Bitmap base64ToBitmap(String base64Data) {
        byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
