package com.orient.base.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

/**
 * 照片的工具类
 * <p>
 * 1. 获取某个路径下面的Bitmap
 */
@SuppressWarnings({"unused", "ResultOfMethodCallIgnored"})
public class PhotoUtils {

    /**
     * 通过路径获取单个Bitmap
     * @param path 路径
     * @return Bitmap
     */
    public static Bitmap getSingleBitmapByPath(String path) {
        File file = new File(path);
        if (!file.exists())
            return null;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inSampleSize = 1;
        FileInputStream is = null;
        Bitmap bitmap;
        try {
            is = new FileInputStream(file);
            bitmap = BitmapFactory.decodeStream(is, null, options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bitmap;
    }

    /**
     * 通过指定路径或者指定宽高的图片
     * @param path      路径
     * @param reqWidth  需要的宽度
     * @param reqHeight 需要的高度
     * @return Bitmap
     */
    public static Bitmap getSingleBitmapByPath(String path, int reqWidth, int reqHeight) {
        File f = new File(path);
        if (!f.exists())
            return null;

        FileInputStream is = null;
        Bitmap bitmap = null;
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            is = new FileInputStream(f);
            BitmapFactory.decodeStream(is, null, options);
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
            options.inJustDecodeBounds = false;
            is = new FileInputStream(f);
            bitmap = BitmapFactory.decodeStream(is, null, options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null)
                    is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

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

    /**
     * 将一个View转化成一个Bitmap
     * @param v 视图
     * @return Bitmap
     */
    public static Bitmap createViewBitmap(View v, int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        float scaleWidth = (float) width / v.getWidth();
        float scaleHeight = (float) height / v.getHeight();
        canvas.scale(scaleWidth, scaleHeight);
        v.draw(canvas);
        return bitmap;
    }

    /**
     * 储存签名路径
     * @param bitmap     Bitmap
     * @param dir 存放的父路径
     * @param name       文件名
     * @return 文件字符串
     */
    public static String saveSign(Bitmap bitmap, String dir, String name) {
        ByteArrayOutputStream bao = null;
        FileOutputStream fos = null;
        File file = new File(dir);
        if (!file.exists()) {
            file.mkdirs();
        }
        String fileName = name + ".jpg";
        File cacheFile = new File(file, fileName);

        // 检查该文件是否存在，如果不存在就直接创建一个
        try {
            cacheFile.createNewFile();
            fos = new FileOutputStream(cacheFile);
            bao = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bao);
            byte[] b = bao.toByteArray();
            if (b != null) {
                fos.write(b);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null)
                    fos.close();
                if (bao != null)
                    bao.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return cacheFile.getPath();
    }


    /**
     * 储存签名路径
     * @param bitmap     Bitmap
     * @return 文件字符串
     */
    public static String saveSign(Bitmap bitmap, String path) {
        ByteArrayOutputStream bao = null;
        FileOutputStream fos = null;
        File cacheFile = new File(path);

        try {
            cacheFile.createNewFile();
            fos = new FileOutputStream(cacheFile);
            bao = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bao);
            byte[] b = bao.toByteArray();
            if (b != null) {
                fos.write(b);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null)
                    fos.close();
                if (bao != null)
                    bao.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return cacheFile.getPath();
    }


}
