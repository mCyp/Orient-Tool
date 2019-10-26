package com.orient.base.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

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
     * @return 默认根目录
     */
    public static File getRootDirectory(Context context) {
        return context.getExternalCacheDir();
    }

    /**
     * 规定的照片的存放路径
     * @return 照片的存放路径
     */
    public static File getPhotoDirectory(Context context) {
        File photoDir = new File(getRootDirectory(context), "photo");
        if (!photoDir.exists())
            photoDir.mkdirs();

        return photoDir;
    }

    /**
     * 规定的录音的存放路径
     * @return 录音的存放路径
     */
    public static File getAudioDirectory(Context context) {
        File audioDir = new File(getRootDirectory(context), "audio");
        if (!audioDir.exists())
            audioDir.mkdirs();

        return audioDir;
    }

    /**
     * 签名的存放路径
     * @param context 上下文
     * @return 签名的存放路径
     */
    public static File getSignDirectory(Context context) {
        File signDir = new File(getRootDirectory(context), "sign");
        if (!signDir.exists())
            signDir.mkdirs();

        return signDir;
    }

    /**
     * 多媒体文件的存放路径
     */
    public static File getMediaDirectory(Context context) {
        File mediaDir = new File(getRootDirectory(context), "media");
        if (!mediaDir.exists())
            mediaDir.mkdirs();

        return mediaDir;
    }

    /**
     * 自定义路径
     * @return 自定义路径
     */
    public static File getDefineDirectory(File parentFile, String name) {
        File dir = new File(parentFile, name);
        if (!dir.exists())
            dir.mkdirs();

        return dir;
    }

    /**
     * 自定义三级路径
     * @param parentFile 一级目录
     * @param name       二级文件名
     * @param subName    子文件名
     * @return 自定义路径
     */
    public static File getDefineDirectory(File parentFile, String name, String subName) {
        String p = parentFile.getAbsolutePath() + File.separator + name + File.separator + subName;
        File dir = new File(p);
        if(!dir.exists())
            dir.mkdirs();

        return dir;
    }

    // 兼容Android 7.0调用系统相机获取URI的方法

    /**
     * 创建调用系统相机需要的Uri
     * @param p 父目录
     */
    @SuppressLint("SimpleDateFormat")
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
     * 调用系统相机需要的Uri
     * 面向Android7.0以下版本
     * @param path 路径
     * @return Uri
     */
    @SuppressLint("SimpleDateFormat")
    public static Uri getImageUri(String path) {
        File mediaStorageDir = new File(path);
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = new File(path, "IMG_" + timeStamp + ".jpg");
        return Uri.fromFile(mediaFile);
    }

    /**
     * 得到某个路径下某种文件的数量
     * @param path      路径
     * @param Extension 扩展名
     * @return 数量
     */
    public static int getSubFileCount(String path, String Extension) {
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
     * 得到某个路径下面某个类型文件的所有路径
     * @param path      父路径
     * @param Extension 文件扩展名
     * @return 子路径集合
     */
    public static List<String> getSubFilePaths(String path, String Extension) {
        List<String> paths = new LinkedList<>();
        File[] files = new File(path).listFiles();
        if (files == null)
            return null;
        for (File f : files) {
            if (f.isFile()) {
                if (f.getPath().substring(f.getPath().length() - Extension.length()).equals(Extension)) {
                    paths.add(f.getPath());
                }
            }
        }
        return paths;
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

    /**
     * 解压文件
     * @param inputZip             解压的文件地址
     * @param destinationDirectory 目录存放路径
     */
    public static void unzipFile(String inputZip, String destinationDirectory) throws IOException {
        int buffer = 2048;
        List<String> zipFiles = new ArrayList<>();
        File sourceZipFile = new File(inputZip);
        File unzipDirectory = new File(destinationDirectory);
        createDir(unzipDirectory.getAbsolutePath());
        ZipFile zipFile;
        zipFile = new ZipFile(sourceZipFile, ZipFile.OPEN_READ);
        Enumeration zipFileEntries = zipFile.entries();

        while (zipFileEntries.hasMoreElements()) {
            ZipEntry entry = (ZipEntry) zipFileEntries.nextElement();
            String currentEntry = entry.getName();
            File destFile = new File(unzipDirectory, currentEntry);

            if (currentEntry.endsWith(".zip")) {
                zipFiles.add(destFile.getAbsolutePath());
            }

            File destinationParent = destFile.getParentFile();
            createDir(destinationParent.getAbsolutePath());

            if (!entry.isDirectory()) {

                if (destFile.exists()) {
                    // Log.i(TAG,destFile + "已经存在！");
                    continue;
                }

                BufferedInputStream is = new BufferedInputStream(zipFile.getInputStream(entry));
                int currentByte;
                // buffer for writing file
                byte[] data = new byte[buffer];

                FileOutputStream fos = new FileOutputStream(destFile);
                BufferedOutputStream dest = new BufferedOutputStream(fos, buffer);

                while ((currentByte = is.read(data, 0, buffer)) != -1) {
                    dest.write(data, 0, currentByte);
                }
                dest.flush();
                dest.close();
                is.close();
            }
        }
        zipFile.close();

        for (String zipName : zipFiles) {
            unzipFile(zipName, destinationDirectory + File.separatorChar
                    + zipName.substring(0, zipName.lastIndexOf(".zip")));
        }
    }

    /**
     * 递归创建文件夹
     *
     * @param dirPath 创建的路径
     */
    public static void createDir(String dirPath) {
        try {
            File file = new File(dirPath);
            if (file.getParentFile().exists()) {
                file.mkdir();
                file.getAbsolutePath();
            } else {
                createDir(file.getParentFile().getAbsolutePath());
                file.mkdir();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
