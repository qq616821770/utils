package com.pingan.pinganwifiboss.util;

import android.content.Context;
import android.os.Environment;

import java.io.File;

import cn.core.log.Lg;

public class FileUtil {

    /**
     * 获取可用的sdcard路径
     *
     * @param context
     * @return
     */
    public static String getDownloadDir(Context context) {
        return getDownloadDir(context, true);
    }

    /**
     * 获取可用的sdcard路径
     *
     * @param context
     * @return
     */
    public static String getDownloadDir(Context context, boolean isAllowUseCache) {
        // 获取外部存储目录即 SDCard
        String storageDirectory = Environment.getExternalStorageDirectory().toString();
        File fDir = new File(storageDirectory);
        // 如果sdcard目录不可用
        if (!fDir.canWrite()) {
            // 获取可用
            storageDirectory = getSDCardDir();
            if (storageDirectory != null) {
                storageDirectory = storageDirectory + File.separator + "Download";
                Lg.i("项目存储路径采用自动找寻可用存储空间的方式   storageDirectory:" + storageDirectory);
                return storageDirectory;

            } else {
                if (isAllowUseCache) {
                    Lg.e(new Exception("没有找到可用的存储路径  采用cachedir"));
                    return context.getCacheDir().toString();
                } else {
                    return null;
                }
            }
        } else {
            storageDirectory = storageDirectory + File.separator + "Download";
            Lg.i("项目存储路径采用sdcard的地址   storageDirectory:" + storageDirectory);
            return storageDirectory;
        }
    }

    /**
     * 获取一个可用的存储路径（可能是内置的存储路径）
     *
     * @return 可用的存储路径
     */
    private static String getSDCardDir() {
        String pathDir = null;
        // 先获取内置sdcard路径
        File sdfile = Environment.getExternalStorageDirectory();
        // 获取内置sdcard的父路径
        File parentFile = sdfile.getParentFile();
        // 列出该父目录下的所有路径
        File[] listFiles = parentFile.listFiles();
        // 如果子路径可以写 就是拓展卡（包含内置的和外置的）

        long freeSizeMax = 0L;
        for (int i = 0; i < listFiles.length; i++) {
            if (listFiles[i].canWrite()) {
                // listFiles[i]就是SD卡路径
                String tempPathDir = listFiles[i].getAbsolutePath();
                long tempSize = getSDFreeSize(tempPathDir);
                if (tempSize > freeSizeMax) {
                    freeSizeMax = tempSize;
                    pathDir = tempPathDir;
                }
            }
        }
        return pathDir;
    }

    /**
     * 获取指定目录剩余空间
     *
     * @return
     * @author EX-LIJINHUA001
     * @date 2013-6-7
     */
    public static long getSDFreeSize(String filePath) {

        try {
            File file = new File(filePath);
            if (!file.exists()) {
                file.mkdirs();
            }

            android.os.StatFs statfs = new android.os.StatFs(filePath);

            long nBlocSize = statfs.getBlockSize(); // 获取SDCard上每个block的SIZE

            long nAvailaBlock = statfs.getAvailableBlocks(); // 获取可供程序使用的Block的数量

            long nSDFreeSize = nAvailaBlock * nBlocSize; // 计算 SDCard
            // 剩余大小B
            return nSDFreeSize;
        } catch (Exception ex) {
            Lg.w(ex);
            Lg.i("httpFrame threadName:" + Thread.currentThread().getName() + " getSDFreeSize  无法计算文件夹大小 folderPath:" + filePath);
        }

        return -1;
    }

    /**
     * 获取文件扩展名
     *
     * @param fileName
     * @return
     */
    public static String getFileFormat(String fileName) {
        if (StringUtil.isStringEmpty(fileName)) return "";

        int point = fileName.lastIndexOf('.');
        return fileName.substring(point + 1);
    }
}
