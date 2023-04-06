package com.xieqing.codeutils.util;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.core.content.FileProvider;

import java.io.File;

public class OpenFileUtils {

    public static File mFile;

    public static Context mContexnt;

    /**
     * 获取文件后缀名
     *
     * @param filename
     * @return
     */
    public static String getFileSuffix(String filename) {
        try{
            String[] strArray = filename.split("\\.");
            int suffixIndex = strArray.length - 1;
            return strArray[suffixIndex];
        }catch(Exception e){
            return "";
        }
    }


    public static void init(Context context){
        mContexnt = context;
    }
    public static void open(Context context,String path){
        init(context);
        mFile = new File(path);
        //if(null==file || !file.exists()){
        //	Toast.makeText(mainActivity.getContext(),"文件路径不存在",0).show();
        //	return;//文件不存在或者file是空对象
        //}
        //Toast.makeText(mainActivity.getContext(),type,0).show();
        String type = getFileSuffix(path);
        switch(type){
            case "txt":
                openText();
                break;
            case "htm":
                openText();
                break;
            case "html":
                openText();
                break;
            case "jpg":
                openImage();
                break;
            case "png":
                openImage();
                break;
            case "bmp":
                openImage();
                break;
            case "pdf":
                openPDF();
                break;
            case "mp3":
                openAudio();
                break;
            case "aac":
                openAudio();
                break;
            case "wma":
                openAudio();
                break;
            case "ogg":
                openAudio();
                break;
            case "m4a":
                openAudio();
                break;
            case "flac":
                openAudio();
                break;
            case "ape":
                openAudio();
                break;
            case "wav":
                openAudio();
                break;
            case "amr":
                openAudio();
                break;
            case "3gpp":
                openAudio();
                break;
            case "mp4":
                openVideo();
                break;
            case "avi":
                openVideo();
                break;
            case "wmv":
                openVideo();
                break;
            case "m3u8":
                openVideo();
                break;
            case "rm":
                openVideo();
                break;
            case "flv":
                openVideo();
                break;
            case "mkv":
                openVideo();
                break;
            case "mov":
                openVideo();
                break;
            case "mpeg":
                openVideo();
                break;
            case "mpg":
                openVideo();
                break;
            case "vob":
                openVideo();
                break;
            case "dat":
                openVideo();
                break;
            case "3gp":
                openVideo();
                break;
            case "chm":
                openCHM();
                break;
            case "apk":
                openAPK();
                break;
            case "ppt":
                openPPT();
                break;
            case "excel":
                openEXCEL();
                break;
            case "word":
                openWROD();
                break;
            case "zip":
                openCompress();
                break;
            case "rar":
                openCompress();
                break;
            default:
                openSystemFileManaget();
                break;
        }
    }

    //打开压缩文件
    public static void openCompress(){
        //好像有问题
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(mFile), "application/x-gzip");
    }

    //打开word文件
    public static void openWROD(){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(mFile);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(uri, "application/msword");
        startActivity(intent);
    }

    //打开excel文件
    public static void openEXCEL(){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(mFile);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(uri, "application/vnd.ms-excel");
        startActivity(intent);
    }

    //打开ppt文件
    public static void openPPT(){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(mFile);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        startActivity(intent);
    }

    //打开apk文件
    public static void openAPK(){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(mFile);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        startActivity(intent);
    }

    //打开chm文件
    public static void openCHM(){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(mFile);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(uri, "application/x-chm");
        startActivity(intent);
    }

    //打开视频文件
    public static void openVideo(){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(mFile);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        intent.setDataAndType(uri, "video/*");
        startActivity(intent);
    }

    //打开音频文件
    public static void openAudio(){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(mFile);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        intent.setDataAndType(uri, "audio/*");
        startActivity(intent);
    }

    //打开pdf文件
    public static void openPDF(){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(mFile);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(uri, "application/pdf");
        startActivity(intent);
    }

    //打开图片文件
    public static void openImage(){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(mFile);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(uri, "image/*");
        startActivity(intent);
    }

    //打开文本文件
    public static void openText(){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(mFile);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(mFile), "text/plain");
        startActivity(intent);
    }

    //打开系统文件管理器
    public static void openSystemFileManaget(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(mFile), "file/*");
        startActivity(intent);
    }

    //启动Activity
    public static void startActivity(Intent intent){
        try {
            //默认选择打开方式标题
            //mainActivity.getContext().startActivity(intent);
            mContexnt.startActivity(Intent.createChooser(intent,"选择浏览工具"));//自定义选择打开方式标题
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            //Toast.makeText(mainActivity.getContext(),e + "",0).show();
        }
    }




}

