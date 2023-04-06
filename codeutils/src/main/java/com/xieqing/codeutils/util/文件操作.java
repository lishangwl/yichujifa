package com.xieqing.codeutils.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class 文件操作 {

    private static String TAG = "文件操作";

    static BufferedReader br;
    static BufferedWriter bw;
    static FileInputStream fin;
    static FileOutputStream fout;
    static InputStreamReader isr;
    static String line = "";
    static OutputStreamWriter osw;

    private 文件操作() {
    }

    public static boolean 修改文件名(String oldname, String newname) {
        if (newname.equals(oldname)) {
            return true;
        }
        File oldfile = new File(oldname);
        if (!oldfile.exists()) {
            return false;
        }
        File newfile = new File(newname);
        if (newfile.exists()) {
            return false;
        }
        if (!oldfile.renameTo(newfile)) {
            return false;
        }
        return true;
    }

    public static boolean 删除文件(String name) {
        File file = new File(name);
        if (file.exists() && !file.isDirectory() && file.delete()) {
            return true;
        }
        return false;
    }

    public static boolean 创建目录(String path) {
        String[] dir = path.split("/");
        String dist = dir[0];
        boolean result = true;
        if (dir.length <= 0) {
            return false;
        }
        for (int i = 1; i < dir.length; i++) {
            dist = dist + "/" + dir[i];
            File mkdir = new File(dist);
            if (!mkdir.exists()) {
                result = mkdir.mkdir();
            }
        }
        return result;
    }

    public static boolean 删除目录(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            return false;
        }
        return deleteDir(dir);
    }

    private static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (String file : children) {
                if (!deleteDir(new File(dir, file))) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    public static boolean 是否为目录(String name) {
        File file = new File(name);
        if (file.exists() && file.isDirectory()) {
            return true;
        }
        return false;
    }

    public static boolean 是否为隐藏文件(String name) {
        File file = new File(name);
        if (file.exists()) {
            return file.isHidden();
        }
        return false;
    }

    public static boolean 文件是否存在(String name) {
        return new File(name).exists();
    }

    public static String 取文件编码(String filename) {
        try {
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(new File(filename)));
            in.mark(4);
            byte[] first3bytes = new byte[3];
            in.read(first3bytes);
            in.reset();
            if (first3bytes[0] == -17 && first3bytes[1] == -69 && first3bytes[2] == -65) {
                return "utf-8";
            }
            if (first3bytes[0] == -1 && first3bytes[1] == -2) {
                return "unicode";
            }
            if (first3bytes[0] == -2 && first3bytes[1] == -1) {
                return "utf-16be";
            }
            if (first3bytes[0] == -1 && first3bytes[1] == -1) {
                return "utf-16le";
            }
            return "GBK";
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "";
        } catch (IOException e2) {
            e2.printStackTrace();
            return "";
        }
    }

    public static String 读入文本文件(String filename, String charset) {
        String res = "";
        if (!new File(filename).exists()) {
            return res;
        }
        try {
            FileInputStream fin2 = new FileInputStream(filename);
            int length = fin2.available();
            byte[] buffer = new byte[length];
            fin2.read(buffer);
            String res2 = new String(buffer, 0, length, charset);
            try {
                fin2.close();
                return res2;
            } catch (Exception e) {
                e = e;
                res = res2;
                e.printStackTrace();
                return res;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return res;
        }
    }

    public static boolean 写出文本文件(String filename, String message, String charset) {
        try {
            FileOutputStream fout2 = new FileOutputStream(filename);
            fout2.write(message.getBytes(charset));
            fout2.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static byte[] 读入字节文件(String filename) {
        byte[] buffer = null;
        if (!new File(filename).exists()) {
            return null;
        }
        try {
            FileInputStream fin2 = new FileInputStream(filename);
            buffer = new byte[fin2.available()];
            fin2.read(buffer);
            fin2.close();
            return buffer;
        } catch (Exception e) {
            e.printStackTrace();
            return buffer;
        }
    }


    public static boolean 写出字节文件(String filename, byte[] bytes) {
        try {
            FileOutputStream fout2 = new FileOutputStream(filename);
            fout2.write(bytes);
            fout2.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static long 取文件尺寸(String filePath) {
        File file = new File(filePath);
        try {
            if (file.isDirectory()) {
                return getFileSizes(file);
            }
            return getFileSize(file);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private static long getFileSize(File file) throws Exception {
        if (file.exists()) {
            return (long) new FileInputStream(file).available();
        }
        file.createNewFile();
        return 0;
    }

    private static long getFileSizes(File f) throws Exception {
        long fileSize;
        long size = 0;
        File[] flist = f.listFiles();
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                fileSize = getFileSizes(flist[i]);
            } else {
                fileSize = getFileSize(flist[i]);
            }
            size += fileSize;
        }
        return size;
    }

    public static String 寻找文件关键词(String Path, String keyword) {
        String result = "";
        for (File f : new File(Path).listFiles()) {
            if (f.getName().indexOf(keyword) >= 0) {
                result = f.getPath() + "\n" + result;
            }
        }
        return result;
    }

    public static String 寻找文件后缀名(String Path, String Extension) {
        String result = "";
        for (File f : new File(Path).listFiles()) {
            if (f.getPath().substring(f.getPath().length() - Extension.length()).equals(Extension) && !f.isDirectory()) {
                result = f.getPath() + "\n" + result;
            }
        }
        return result;
    }

    public static boolean 复制文件(String sourcePath, String targetPath) {
        if (!new File(sourcePath).exists()) {
            return false;
        }
        int bytesum = 0;
        try {
            InputStream inStream = new FileInputStream(sourcePath);
            FileOutputStream fs = new FileOutputStream(targetPath);
            byte[] buffer = new byte[1444];
            while (true) {
                int byteread = inStream.read(buffer);
                if (byteread != -1) {
                    bytesum += byteread;
                    fs.write(buffer, 0, byteread);
                } else {
                    inStream.close();
                    fs.close();
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean 创建文件(String path) {
        File f = new File(path);
        if (f.exists()) {
            return true;
        }
        try {
            return f.createNewFile();
        } catch (IOException e) {
            return false;
        }
    }

    public static boolean 打开文本文件_读(String filename, String charset) {
        if (!new File(filename).exists()) {
            return false;
        }
        try {
            fin = new FileInputStream(filename);
            isr = new InputStreamReader(fin, charset);
            br = new BufferedReader(isr);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean 关闭读() {
        try {
            br.close();
            fin.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String 读一行() {
        try {
            String readLine = br.readLine();
            line = readLine;
            if (readLine != null) {
                return line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static boolean 打开文本文件_写(String filename, String charset) {
        if (!new File(filename).exists()) {
            return false;
        }
        try {
            fout = new FileOutputStream(filename);
            osw = new OutputStreamWriter(fout, charset);
            bw = new BufferedWriter(osw);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean 关闭写() {
        try {
            bw.close();
            fout.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean 写一行(String message) {
        try {
            bw.newLine();
            bw.write(message);
            bw.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String 取子目录(String dir) {
        String pa = "";
        File[] ff = new File(dir).listFiles();
        for (int i = 0; i < ff.length; i++) {
            if (ff[i].isDirectory()) {
                pa = pa + ff[i].getAbsolutePath() + "|";
            }
        }
        return pa;
    }

    /**
     * 取文件修改时间
     * @param path
     * @return
     */
    public static String getFileLastModifyTime(String path) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(new File(path).lastModified()));
    }

    /**
     * 打开文件
     * @param context
     * @param Path
     */
    public static void openFile(Context context,String Path) {
        Log.d(TAG,"Path -> " + Path);
        context.startActivity(openFile(Path));
    }

    private static Intent openFile(String filePath) {
        File file = new File(filePath);
        if (file == null || !file.exists() || file.isDirectory()) {
            return null;
        }
        String end = file.getName().substring(file.getName().lastIndexOf(".") + 1, file.getName().length()).toLowerCase();
        if (end.equals("m4a") || end.equals("mp3") || end.equals("mid") || end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
            return getAudioFileIntent(filePath);
        }
        if (end.equals("3gp") || end.equals("mp4")) {
            return getVideoFileIntent(filePath);
        }
        if (end.equals("jpg") || end.equals("gif") || end.equals("png") || end.equals("jpeg") || end.equals("bmp")) {
            return getImageFileIntent(filePath);
        }
        if (end.equals("apk")) {
            return getApkFileIntent(filePath);
        }
        if (end.equals("ppt")) {
            return getPptFileIntent(filePath);
        }
        if (end.equals("xls")) {
            return getExcelFileIntent(filePath);
        }
        if (end.equals("doc")) {
            return getWordFileIntent(filePath);
        }
        if (end.equals("pdf")) {
            return getPdfFileIntent(filePath);
        }
        if (end.equals("chm")) {
            return getChmFileIntent(filePath);
        }
        if (end.equals("txt")) {
            return getTextFileIntent(filePath, false);
        }
        return getAllIntent(filePath);
    }

    private static Intent getAllIntent(String param) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction("android.intent.action.VIEW");
        intent.setDataAndType(Uri.fromFile(new File(param)), "*/*");
        return intent;
    }

    private static Intent getApkFileIntent(String param) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction("android.intent.action.VIEW");
        intent.setDataAndType(Uri.fromFile(new File(param)), "application/vnd.android.package-archive");
        return intent;
    }

    private static Intent getVideoFileIntent(String param) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        intent.setDataAndType(Uri.fromFile(new File(param)), "video/*");
        return intent;
    }

    private static Intent getAudioFileIntent(String param) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        intent.setDataAndType(Uri.fromFile(new File(param)), "audio/*");
        return intent;
    }

    private static Intent getHtmlFileIntent(String param) {
        Uri uri = Uri.parse(param).buildUpon().encodedAuthority("com.android.htmlfileprovider").scheme("content").encodedPath(param).build();
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setDataAndType(uri, "text/html");
        return intent;
    }

    private static Intent getImageFileIntent(String param) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(new File(param)), "image/*");
        return intent;
    }

    private static Intent getPptFileIntent(String param) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(new File(param)), "application/vnd.ms-powerpoint");
        return intent;
    }

    private static Intent getExcelFileIntent(String param) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(new File(param)), "application/vnd.ms-excel");
        return intent;
    }

    private static Intent getWordFileIntent(String param) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(new File(param)), "application/msword");
        return intent;
    }

    private static Intent getChmFileIntent(String param) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(new File(param)), "application/x-chm");
        return intent;
    }

    private static Intent getTextFileIntent(String param, boolean paramBoolean) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (paramBoolean) {
            intent.setDataAndType(Uri.parse(param), "text/plain");
        } else {
            intent.setDataAndType(Uri.fromFile(new File(param)), "text/plain");
        }
        return intent;
    }

    private static Intent getPdfFileIntent(String param) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(new File(param)), "application/pdf");
        return intent;
    }

    public static void 调用本地播放器(Context context,String str){
        try {
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setDataAndType(Uri.parse(str), "*/*");
            context.startActivity(intent);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}