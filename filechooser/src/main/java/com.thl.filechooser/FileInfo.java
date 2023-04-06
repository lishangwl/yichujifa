//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.thl.filechooser;

public class FileInfo{
    public static final String FILE_TYPE_FOLDER = "type_folder";
    public static final String FILE_TYPE_VIDEO = "type_video";
    public static final String FILE_TYPE_AUDIO = "type_audio";
    public static final String FILE_TYPE_FILE = "type_file";
    public static final String FILE_TYPE_APK = "type_apk";
    public static final String FILE_TYPE_ZIP = "type_zip";
    public static final String FILE_TYPE_RAR = "type_rar";
    public static final String FILE_TYPE_JPEG = "type_jpeg";
    public static final String FILE_TYPE_JPG = "type_jpg";
    public static final String FILE_TYPE_PNG = "type_png";
    public static final String FILE_TYPE_ALL = "type_all";
    public static final String FILE_TYPE_YCF = "type_ycf";
    public static final String FILE_TYPE_IMAGE = "type_image";
    public static final String FILE_TYPE_PACKAGE = "type_package";
    private String fileType = "type_folder";
    private boolean isFolder;
    private String fileName;
    private String filePath;
    private String createTime;
    private boolean isChoose = false;

    public void setChoose(boolean choose) {
        isChoose = choose;
    }

    public boolean isChoose() {
        return isChoose;
    }

    public FileInfo() {
    }

    @Override
    public String toString() {
        return fileName + (isFolder?"[]":"");
    }

    public String getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public boolean isFolder() {
        return this.isFolder;
    }

    public void setFolder(boolean folder) {
        this.isFolder = folder;
    }

    public String getFileType() {
        return this.fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
}
