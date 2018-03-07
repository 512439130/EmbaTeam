package com.fala.emba.team.util;

import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * 文件辅助类
 * Created by SoMustYY on 2017/7/4.
 */
public class FileUtil {

    public static final String ROOT = Environment.getExternalStorageDirectory().getPath() + "/nicehair/";
    public static final String CAMERA = Environment.getExternalStorageDirectory().getPath() + "/DCIM/Camera/";
    public static final String CACHE_IMG = "/cache/images/";
    /**
     * 应用日志目录文件
     */
    public static String APP_LOG_PATH = ROOT + "log/";

    /**
     * 日志文件路径
     */
    public static String LOGFILE = APP_LOG_PATH + "log.txt";

    /**
     * 读取输入流数据
     *
     * @param inStream
     * @return
     */
    public static byte[] read(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        inStream.close();
        return outStream.toByteArray();
    }

    /**
     * 判断SD是否可以
     *
     * @return
     */
    public static boolean isSdcardExist() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }

    /**
     * 创建根目录
     *
     * @param path 目录路径
     */
    public static void createDirFile(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    /**
     * 创建文件
     *
     * @param path 文件路径
     * @return 创建的文件
     */
    public static File createNewFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                return null;
            }
        }
        return file;
    }


    /**
     * 取得指定SDCard文件下载目录
     * 内置sdCard
     * APP公用目录
     */
    public static String getCommonPath() {
        //有sd卡
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            // 创建一个文件夹对象，赋值为外部存储器的目录
            File sdcardDir = Environment.getExternalStorageDirectory();
            // 得到一个路径，内容是sdcard的文件夹路径和名字
            String path = "/" + "TeachaerApp";
            File path1 = new File(path);
            if (!path1.exists())
                // 若不存在，创建目录，可以在应用启动的时候创建
                path1.mkdirs();

            return path;
        } else{
            //无SD卡
            return "";
        }

    }


}
