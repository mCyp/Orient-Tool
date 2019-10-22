package com.orient.base.utils;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * 文件工具类
 * <p>
 * 1. 规范文件必须存放在app目录下面，删除的时候数据一同删除
 */
@SuppressWarnings({"unused", "WeakerAccess", "ResultOfMethodCallIgnored"})
public class FileUtils {


    // 定义存放路径

    /**
     * 默认将路径设置在App下面
     *
     * @return 默认根目录
     */
    public static File getRootDirectory(Context context) {
        return context.getExternalCacheDir();
    }

    /**
     * 规定的照片的存放路径
     *
     * @return 照片的存放路径
     */
    public static File getPhotoDirectory(Context context) {
        File photoDir = new File(getRootDirectory(context), "photo");
        if (!photoDir.exists())
            photoDir.mkdirs();

        return photoDir;
    }

    /**
     * 规定的多媒体的存放路径
     *
     * @return 多媒体的存放路径
     */
    public static File getAudioDirectory(Context context) {
        File photoDir = new File(getRootDirectory(context), "media");
        if (!photoDir.exists())
            photoDir.mkdirs();

        return photoDir;
    }

    /**
     * 自定义路径
     *
     * @return 自定义路径
     */
    public static File getDefinePhotoDirectory(File parentFile, String name) {
        File dir = new File(parentFile, name);
        if (!dir.exists())
            dir.mkdirs();

        return dir;
    }

    // 兼容Android 7.0调用系统相机获取URI的方法

    /**
     * 创建调用系统相机需要的Uri
     * @param p 父目录
     */
    public static Uri getImageUri(String p, Context context, String authority) {
        Uri imageUri;
        File file = new File(p);
        if (!file.exists()) {
            file.mkdirs();
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        try {
            File image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    file      /* directory */
            );
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                imageUri = Uri.fromFile(image);
            } else {
                imageUri = FileProvider.getUriForFile(context, authority, image);
            }
            return imageUri;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 得到某个路径下某种文件的数量
     * @param path      路径
     * @param Extension 扩展名
     * @return 数量
     */
    public static int getFileCountByPath(String path, String Extension) {
        ArrayList<String> fileList = new ArrayList<>();
        File[] files = new File(path).listFiles();
        if (files == null)
            return 0;
        for (File f : files) {
            if (f.isFile()) {
                if (f.getPath().substring(f.getPath().length() - Extension.length()).equals(Extension)) {
                    fileList.add(f.getPath());
                }
            }
        }
        return fileList.size();
    }

    /**
     * 删除文件
     * @param path 路径
     */
    public static void deleteFile(String path) {
        if (TextUtils.isEmpty(path))
            return;

        File deleteFile = new File(path);
        if (!deleteFile.exists()) {
            return;
        }

        if (deleteFile.isDirectory()) {
            for (File file : deleteFile.listFiles()) {
                if (file.isDirectory())
                    deleteFile(file.getAbsolutePath());
                else
                    file.delete();
            }
        }

        deleteFile.delete();
    }

}
