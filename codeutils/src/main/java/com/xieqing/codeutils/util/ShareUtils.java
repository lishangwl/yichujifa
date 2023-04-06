package com.xieqing.codeutils.util;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import androidx.core.content.FileProvider;

import java.io.File;
import java.util.ArrayList;

public class ShareUtils {
    public static void 分享文件到QQ好友(String str) {
        File file = new File(str);
        Intent intent = new Intent();
        intent.setAction("android.intent.action.SEND");
        intent.setComponent(new ComponentName("com.tencent.mobileqq", "com.tencent.mobileqq.activity.JumpActivity"));
        intent.setType("*/*");
        intent.putExtra("android.intent.extra.STREAM", getUriForFile(file));
        try {
            Utils.getApp().startActivity(intent);
        }catch (Exception e){
            e.toString();
            ToastUtils.showLong("分享失败，可能应用未安装");
        }
    }
    public static Uri getUriForFile(File file){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            return FileProvider.getUriForFile(Utils.getApp(),Utils.getApp().getPackageName(),file);
        }
        return Uri.fromFile(file);
    }
    public static void 分享文件到QQ好友_多个(String... strArr) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.tencent.mobileqq", "com.tencent.mobileqq.activity.JumpActivity"));
        intent.setAction("android.intent.action.SEND_MULTIPLE");
        intent.setType("*/*");
        ArrayList arrayList = new ArrayList();
        for (String file : strArr) {
            arrayList.add(getUriForFile(new File(file)));
        }
        intent.putParcelableArrayListExtra("android.intent.extra.STREAM", arrayList);
        try {
            Utils.getApp().startActivity(intent);
        }catch (Exception e){
            e.toString();
            ToastUtils.showLong("分享失败，可能应用未安装");
        }
    }

    public static void 分享文件到微信好友(String str) {
        File file = new File(str);
        Intent intent = new Intent();
        intent.setAction("android.intent.action.SEND");
        intent.setComponent(new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI"));
        intent.setType("*/*");
        intent.putExtra("android.intent.extra.STREAM", getUriForFile(file));
        try {
            Utils.getApp().startActivity(intent);
        }catch (Exception e){
            e.toString();
            ToastUtils.showLong("分享失败，可能应用未安装");
        }
    }

    public static void 分享文件到微信好友_多张(String... strArr) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI"));
        intent.setAction("android.intent.action.SEND_MULTIPLE");
        intent.setType("*/*");
        ArrayList arrayList = new ArrayList();
        for (String file : strArr) {
            arrayList.add(getUriForFile(new File(file)));
        }
        intent.putParcelableArrayListExtra("android.intent.extra.STREAM", arrayList);
        try {
            Utils.getApp().startActivity(intent);
        }catch (Exception e){
            e.toString();
            ToastUtils.showLong("分享失败，可能应用未安装");
        }
    }

    public static void 分享文字到QQ好友(String str) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.SEND");
        intent.setComponent(new ComponentName("com.tencent.mobileqq", "com.tencent.mobileqq.activity.JumpActivity"));
        intent.setType("text/plain");
        intent.putExtra("android.intent.extra.TEXT", str);
        try {
            Utils.getApp().startActivity(intent);
        }catch (Exception e){
            e.toString();
            ToastUtils.showLong("分享失败，可能应用未安装");
        }
    }

    public static void 分享文字到微信好友(String str) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.SEND");
        intent.setComponent(new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI"));
        intent.setType("text/plain");
        intent.putExtra("android.intent.extra.TEXT", str);
        try {
            Utils.getApp().startActivity(intent);
        }catch (Exception e){
            e.toString();
            ToastUtils.showLong("分享失败，可能应用未安装");
        }
    }

    public static void 分享图片到QQ好友(String str) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.tencent.mobileqq", "com.tencent.mobileqq.activity.JumpActivity"));
        intent.setAction("android.intent.action.SEND");
        intent.setType("image/*");
        intent.putExtra("android.intent.extra.STREAM", getUriForFile(new File(str)));
        try {
            Utils.getApp().startActivity(intent);
        }catch (Exception e){
            e.toString();
            ToastUtils.showLong("分享失败，可能应用未安装");
        }
    }

    public static void 分享图片到QQ好友_多张(String... strArr) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.tencent.mobileqq", "com.tencent.mobileqq.activity.JumpActivity"));
        intent.setAction("android.intent.action.SEND_MULTIPLE");
        intent.setType("image/*");
        ArrayList arrayList = new ArrayList();
        for (String file : strArr) {
            arrayList.add(getUriForFile(new File(file)));
        }
        intent.putParcelableArrayListExtra("android.intent.extra.STREAM", arrayList);
        try {
            Utils.getApp().startActivity(intent);
        }catch (Exception e){
            e.toString();
            ToastUtils.showLong("分享失败，可能应用未安装");
        }
    }

    public static void 分享图片到QQ空间(String str) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.qzone", "com.qzonex.module.operation.ui.QZonePublishMoodActivity"));
        intent.setAction("android.intent.action.SEND");
        intent.setType("image/*");
        intent.putExtra("android.intent.extra.STREAM", getUriForFile(new File(str)));
        try {
            Utils.getApp().startActivity(intent);
        }catch (Exception e){
            e.toString();
            ToastUtils.showLong("分享失败，可能应用未安装");
        }
    }

    public static void 分享图片到QQ空间_多张(String... strArr) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.qzone", "com.qzonex.module.operation.ui.QZonePublishMoodActivity"));
        intent.setAction("android.intent.action.SEND_MULTIPLE");
        intent.setType("image/*");
        ArrayList arrayList = new ArrayList();
        for (String file : strArr) {
            arrayList.add(getUriForFile(new File(file)));
        }
        intent.putParcelableArrayListExtra("android.intent.extra.STREAM", arrayList);
        try {
            Utils.getApp().startActivity(intent);
        }catch (Exception e){
            e.toString();
            ToastUtils.showLong("分享失败，可能应用未安装");
        }
    }

    public static void 分享图片到微信好友(String str) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI"));
        intent.setAction("android.intent.action.SEND");
        intent.setType("image/*");
        intent.putExtra("android.intent.extra.STREAM", getUriForFile(new File(str)));
        try {
            Utils.getApp().startActivity(intent);
        }catch (Exception e){
            e.toString();
            ToastUtils.showLong("分享失败，可能应用未安装");
        }
    }

    public static void 分享图片到微信好友_多张(String... strArr) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI"));
        intent.setAction("android.intent.action.SEND_MULTIPLE");
        intent.setType("image/*");
        ArrayList arrayList = new ArrayList();
        for (String file : strArr) {
            arrayList.add(getUriForFile(new File(file)));
        }
        intent.putParcelableArrayListExtra("android.intent.extra.STREAM", arrayList);
        try {
            Utils.getApp().startActivity(intent);
        }catch (Exception e){
            e.toString();
            ToastUtils.showLong("分享失败，可能应用未安装");
        }
    }

    public static void 分享图片到朋友圈(String str) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareToTimeLineUI"));
        intent.setAction("android.intent.action.SEND");
        intent.setType("image/*");
        intent.putExtra("android.intent.extra.STREAM", getUriForFile(new File(str)));
        try {
            Utils.getApp().startActivity(intent);
        }catch (Exception e){
            e.toString();
            ToastUtils.showLong("分享失败，可能应用未安装");
        }
    }

    public static void 分享图片到朋友圈_多张(String... strArr) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareToTimeLineUI"));
        intent.setAction("android.intent.action.SEND_MULTIPLE");
        intent.setType("image/*");
        ArrayList arrayList = new ArrayList();
        for (String file : strArr) {
            arrayList.add(getUriForFile(new File(file)));
        }
        intent.putParcelableArrayListExtra("android.intent.extra.STREAM", arrayList);
        try {
            Utils.getApp().startActivity(intent);
        }catch (Exception e){
            e.toString();
            ToastUtils.showLong("分享失败，可能应用未安装");
        }
    }

}
