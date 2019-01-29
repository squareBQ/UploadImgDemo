package com.zahi.one.uploadimgdemo.utils;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by zahi on 2019/1/25.
 */
public class ImgHelper {

    /**
     * TODO:将byte数组以Base64方式编码为字符串
     *
     * @param bytes 待编码的byte数组
     * @return 编码后的字符串
     */
    @SuppressLint("NewApi")
    public static String encode(byte[] bytes) {
        return java.util.Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * TODO:将以Base64方式编码的字符串解码为byte数组
     *
     * @param encodeStr 待解码的字符串
     * @return 解码后的byte数组
     * @throws IOException
     */
    @SuppressLint("NewApi")
    public static byte[] decode(String encodeStr) throws IOException {
        byte[] bt = null;
        java.util.Base64.Decoder decoder = java.util.Base64.getDecoder();
        bt = decoder.decode(encodeStr);
        return bt;
    }

    /**
     * TODO:将两个byte数组连接起来后，返回连接后的Byte数组
     *
     * @param front 拼接后在前面的数组
     * @param after 拼接后在后面的数组
     * @return 拼接后的数组
     */
    public static byte[] connectBytes(byte[] front, byte[] after) {
        byte[] result = new byte[front.length + after.length];
        System.arraycopy(front, 0, result, 0, after.length);
        System.arraycopy(after, 0, result, front.length, after.length);
        return result;
    }

    /**
     * TODO:将图片以Base64方式编码为字符串
     *
     * @param imgUrl 图片的绝对路径（例如：D:\\jsontest\\abc.jpg）
     * @return 编码后的字符串
     * @throws IOException
     */
    public static String encodeImage(String imgUrl) throws IOException {
        FileInputStream fis = new FileInputStream(imgUrl);
        byte[] rs = new byte[fis.available()];
        fis.read(rs);
        fis.close();
        return encode(rs);
    }

    /**
     * 将Bitmap转换成字符串
     *
     * @param bitmap
     * @return
     */
    public static String bitmap2String(Bitmap bitmap) {
        String string;
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bStream);
        byte[] bytes = bStream.toByteArray();
        string = Base64.encodeToString(bytes, Base64.DEFAULT);
        return string;
    }

    /**
     * 把byte数组转化成 bitmap对象
     *
     * @param b
     * @return
     */
    public static Bitmap bytes2Bimap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }
}
