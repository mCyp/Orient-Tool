package com.orient.common.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

/**
 * 照片的工具类
 *
 * 1. 获取某个路径下面的Bitmap
 */
public class PhotoUtils {

    /**
     * 得到某个路径下面的Bitmaps
     * @param path      路径
     * @param Extension 扩展名
     * @return List<Bitmap>
     */
    public static List<Bitmap> getAlbumByPath(String path, String Extension) {
        List<Bitmap> bitmaps = new LinkedList<>();                //结果 List
        File[] files = new File(path).listFiles();
        if (files == null)
            return null;
        // 发生OOM异常的简单处理
        InputStream inputStream = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inSampleSize = 2;

        try {
            for (File f : files) {
                if (f.isFile()) {
                    if (f.getPath().substring(f.getPath().length() - Extension.length()).equals(Extension)) {
                        inputStream = new FileInputStream(f);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);
                        if (bitmap != null) {
                            bitmaps.add(bitmap);
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                    options = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return bitmaps;
    }

    /**
     * 得到某个路径下面的Bitmaps，指定图片的宽和高
     * @param path      路径
     * @param Extension 扩展名
     * @return List<Bitmap>
     */
    public static List<Bitmap> getAlbumByPath(String path, String Extension, int reqWidth, int reqHeight) {
        List<Bitmap> bitmaps = new LinkedList<>();                //结果 List
        File[] files = new File(path).listFiles();
        if (files == null)
            return null;
        // 发生OOM异常的简单处理
        InputStream inputStream = null;

        try {
            for (File f : files) {
                if (f.isFile()) {
                    if (f.getPath().substring(f.getPath().length() - Extension.length()).equals(Extension)) {
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inJustDecodeBounds = true;
                        options.inPreferredConfig = Bitmap.Config.RGB_565;
                        inputStream = new FileInputStream(f);
                        BitmapFactory.decodeStream(inputStream, null, options);
                        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
                        options.inJustDecodeBounds = false;
                        inputStream = new FileInputStream(f);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);
                        if (bitmap != null) {
                            bitmaps.add(bitmap);
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return bitmaps;
    }


    /**
     * 计算压缩比例
     * @return 压缩比列
     */
    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // 原图片的宽高
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // 计算inSampleSize值
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }


}
