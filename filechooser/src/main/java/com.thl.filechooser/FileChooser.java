//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.thl.filechooser;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.fragment.app.Fragment;

public class FileChooser {
    private Context mContext;
    private FileChoosenListener fileChoosenListener;
    private FileChoosensListener fileChoosensListener;
    private int themeColorRes = -1;
    private String mChoosenFilePath = "";
    private String title = "选择文件";
    private String doneText = "完成";
    private int backIconRes = -1;
    private boolean showFile = true;
    private boolean showHideFile = false;
    private boolean isChooseSingle = true;
    private String chooseType = "type_all";

    public boolean isShowHideFile() {
        return this.showHideFile;
    }

    public FileChooser setShowHideFile(boolean showHideFile) {
        this.showHideFile = showHideFile;
        return this;
    }

    public String getChooseType() {
        return this.chooseType;
    }

    public void setChooseType(String chooseType) {
        this.chooseType = chooseType;
    }

    public boolean isFileShow() {
        return this.showFile;
    }

    public FileChooser showFile(boolean showFile) {
        this.showFile = showFile;
        return this;
    }

    public FileChooser setCurrentPath(String currentPath) {
        this.mChoosenFilePath = currentPath;
        return this;
    }

    public FileChooser setTitle(String title) {
        this.title = title;
        return this;
    }

    public FileChooser setDoneText(String doneText) {
        this.doneText = doneText;
        return this;
    }

    public FileChooser setBackIconRes(int backIconRes) {
        this.backIconRes = backIconRes;
        return this;
    }

    public FileChooser(Fragment fragment, FileChoosenListener fileChoosenListener) {
        this.mContext = fragment.getContext();
        this.fileChoosenListener = fileChoosenListener;
    }

    public FileChooser(Activity activity, FileChoosenListener fileChoosenListener) {
        this.mContext = activity;
        this.fileChoosenListener = fileChoosenListener;
    }

    public FileChooser(Context activity, FileChoosensListener fileChoosensListener) {
        this.mContext = activity;
        this.fileChoosensListener = fileChoosensListener;
        isChooseSingle = false;
    }

    public boolean isChooseSingle() {
        return isChooseSingle;
    }

    public void open() {
        FileChooserActivity.mFileChooser = null;
        FileChooserActivity.mFileChooser = this;
        Intent intent = new Intent(this.mContext, FileChooserActivity.class);
        intent.putExtra("themeColorRes", this.themeColorRes);
        intent.putExtra("showHideFile", this.showHideFile);
        intent.putExtra("currentPath", this.mChoosenFilePath);
        intent.putExtra("title", this.title);
        intent.putExtra("doneText", this.doneText);
        intent.putExtra("backIconRes", this.backIconRes);
        intent.putExtra("chooseType", this.chooseType);
        intent.putExtra("showFile", this.showFile);
        this.mContext.startActivity(intent);
    }

    protected void finish(String filePath) {
        if (this.fileChoosenListener != null) {
            this.fileChoosenListener.onFileChoosen(filePath);
        }

    }
    protected void finish(String[] filePath) {
        if (this.fileChoosensListener != null) {
            this.fileChoosensListener.onFileChoosens(filePath);
        }

    }

    public FileChooser setThemeColor(int themeColorRes) {
        this.themeColorRes = themeColorRes;
        return this;
    }

    public FileChooser setFileChoosenListener(FileChoosenListener fileChoosenListener) {
        this.fileChoosenListener = fileChoosenListener;
        return this;
    }

    public interface FileChoosenListener {
        void onFileChoosen(String var1);
    }

    public interface FileChoosensListener {
        void onFileChoosens(String[] var1);
    }
}
