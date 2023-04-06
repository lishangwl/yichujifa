//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.thl.filechooser;

import android.content.Context;
import android.os.Environment;
import android.os.storage.StorageManager;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.Collator;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class FileTourController {
    private File currentFile;
    private File rootFile;
    private List<FileInfo> currenFileInfoList;
    private List<File> currentFolderList = new ArrayList();
    private boolean isRootFile = true;
    private boolean showFile = true;
    private boolean showHideFile = false;
    private int sdcardIndex;
    private Context mContext;

    public FileTourController(Context context, String currentPath) {
        this.currentFile = new File(currentPath);
        this.mContext = context;
        this.rootFile = this.getRootFile();
        if (this.currentFile == null) {
            this.currentFile = this.rootFile;
        } else if (!this.currentFile.exists()) {
            this.currentFile = this.rootFile;
        } else {
            this.isRootFile = false;
        }

        try {
            if (!this.currentFile.getAbsolutePath().equals(this.getRootFile().getAbsolutePath())) {
                this.currentFolderList.add(this.rootFile);
                ArrayList<File> fileList = new ArrayList();

                for(File tempFile = this.currentFile; !tempFile.getParent().equals(this.rootFile.getAbsolutePath()); tempFile = tempFile.getParentFile()) {
                    fileList.add(tempFile.getParentFile());
                }

                for(int i = fileList.size() - 1; i >= 0; --i) {
                    this.currentFolderList.add(fileList.get(i));
                }
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }

        this.currenFileInfoList = this.searchFile(this.currentFile);
        this.currentFolderList.add(this.currentFile);
    }

    public FileTourController(Context context) {
        this.mContext = context;
        this.rootFile = this.getRootFile();
        this.currentFile = this.rootFile;
        this.currenFileInfoList = this.searchFile(this.currentFile);
        this.currentFolderList.add(this.currentFile);
    }

    public boolean isShowFile() {
        return this.showFile;
    }

    public void setShowFile(boolean showFile) {
        this.showFile = showFile;
    }

    public boolean isShowHideFile() {
        return this.showHideFile;
    }

    public void setShowHideFile(boolean showHideFile) {
        this.showHideFile = showHideFile;
    }

    public List<File> getCurrentFolderList() {
        return this.currentFolderList;
    }

    public List<FileInfo> getCurrenFileInfoList() {
        return this.currenFileInfoList;
    }
    private ArrayList<FileInfo> sortData(List<FileInfo> dataList) {
        ArrayList<FileInfo> folders = new ArrayList<>();
        ArrayList<FileInfo> files = new ArrayList<>();
        for (int i = 0; i < dataList.size() ; i++){
            FileInfo info = dataList.get(i);
            if (info.isFolder()){
                folders.add(info);
            }else{
                files.add(info);
            }
        }


        ArrayList<FileInfo> fileInfos = new ArrayList<>();
        Comparator name = new Comparator<FileInfo>() {
            @Override
            public int compare(FileInfo fileInfo, FileInfo t1) {
                return fileInfo.getFileName().toLowerCase().compareTo(t1.getFileName().toLowerCase());
            }
        };
        Collections.sort(files, name);
        Collections.sort(folders, name);

        fileInfos.addAll(folders);
        fileInfos.addAll(files);

        folders.clear();
        files.clear();
        folders = null;
        files = null;

        return fileInfos;
    }
    public File getRootFile() {
        return this.sdcardIndex == 1 ? this.getSDcard1() : this.getSDcard0();
    }

    public void switchSdCard(int sdcardIndex) {
        if (sdcardIndex == 0) {
            this.rootFile = this.getSDcard0();
        } else {
            this.rootFile = this.getSDcard1();
        }

        this.currentFile = this.rootFile;
        this.currenFileInfoList = new ArrayList();
        this.currentFolderList = new ArrayList();
        this.currenFileInfoList = this.searchFile(this.currentFile);
        this.currentFolderList.add(this.currentFile);
    }

    public File getSDcard0() {
        return new File(getStoragePath(this.mContext, false));
    }

    public File getSDcard1() {
        return getStoragePath(this.mContext, true) == null ? new File(getStoragePath(this.mContext, false)) : new File(getStoragePath(this.mContext, true));
    }

    public static String getStoragePath(Context mContext, boolean is_removale) {
        StorageManager mStorageManager = (StorageManager)mContext.getSystemService("storage");
        Class storageVolumeClazz = null;

        try {
            storageVolumeClazz = Class.forName("android.os.storage.StorageVolume");
            Method getVolumeList = mStorageManager.getClass().getMethod("getVolumeList");
            Method getPath = storageVolumeClazz.getMethod("getPath");
            Method isRemovable = storageVolumeClazz.getMethod("isRemovable");
            Object result = getVolumeList.invoke(mStorageManager);
            int length = Array.getLength(result);

            for(int i = 0; i < length; ++i) {
                Object storageVolumeElement = Array.get(result, i);
                String path = (String)getPath.invoke(storageVolumeElement);
                boolean removable = (Boolean)isRemovable.invoke(storageVolumeElement);
                if (is_removale == removable) {
                    return path;
                }
            }
        } catch (ClassNotFoundException var13) {
            var13.printStackTrace();
        } catch (InvocationTargetException var14) {
            var14.printStackTrace();
        } catch (NoSuchMethodException var15) {
            var15.printStackTrace();
        } catch (IllegalAccessException var16) {
            var16.printStackTrace();
        }

        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    public boolean isRootFile() {
        if (this.isRootFile(this.currentFile)) {
            this.isRootFile = true;
        } else {
            this.isRootFile = false;
        }

        return this.isRootFile;
    }

    public void setCurrentFile(File currentFile) {
        this.currentFile = currentFile;
    }

    public File getCurrentFile() {
        return this.currentFile;
    }

    public List<FileInfo> addCurrentFile(File file) {
        new ArrayList();
        this.currentFile = file;
        this.currentFolderList.add(file);
        List<FileInfo> fileInfoList = this.searchFile(file);
        this.currenFileInfoList = fileInfoList;
        return fileInfoList;
    }

    public List<FileInfo> resetCurrentFile(int position) {
        new ArrayList();

        while(this.currentFolderList.size() - 1 > position) {
            this.currentFolderList.remove(this.currentFolderList.size() - 1);
        }

        if (this.currentFolderList.size() != 0) {
            this.currentFile = new File(((File)this.currentFolderList.get(this.currentFolderList.size() - 1)).getAbsolutePath());
        } else {
            this.currentFile = this.rootFile;
        }

        List<FileInfo> fileInfoList = this.searchFile(this.currentFile);
        this.currenFileInfoList = fileInfoList;
        return fileInfoList;
    }

    public List<FileInfo> searchFile(File file) {
        this.currentFile = file;
        List<FileInfo> fileInfoList = new ArrayList();
        File[] childFiles = file.listFiles();
        if (childFiles != null) {
            for(int i = 0; i < childFiles.length; ++i) {
                FileInfo fileInfo = new FileInfo();
                File childFile = childFiles[i];
                String name = childFile.getName();
                if (name.length() <= 0 || !String.valueOf(name.charAt(0)).equals(".") || this.showHideFile) {
                    fileInfo.setFileName(name);
                    String time = (new SimpleDateFormat("yyyy年MM月dd日")).format(new Date(childFile.lastModified()));
                    fileInfo.setCreateTime(time);
                    fileInfo.setFilePath(childFile.getAbsolutePath());
                    if (childFile.isDirectory()) {
                        fileInfo.setFolder(true);
                        fileInfo.setFileType("type_folder");
                    } else {
                        fileInfo.setFolder(false);
                        if (!"mp4".equals(this.getFileTypeName(childFile.getAbsolutePath())) && !"mkv".equals(this.getFileTypeName(childFile.getAbsolutePath())) && !"avi".equals(this.getFileTypeName(childFile.getAbsolutePath())) && !"3gp".equals(this.getFileTypeName(childFile.getAbsolutePath())) && !"mov".equals(this.getFileTypeName(childFile.getAbsolutePath()))) {
                            if (!"mp3".equals(this.getFileTypeName(childFile.getAbsolutePath())) && !"aac".equals(this.getFileTypeName(childFile.getAbsolutePath())) && !"amr".equals(this.getFileTypeName(childFile.getAbsolutePath())) && !"ogg".equals(this.getFileTypeName(childFile.getAbsolutePath())) && !"wma".equals(this.getFileTypeName(childFile.getAbsolutePath())) && !"wav".equals(this.getFileTypeName(childFile.getAbsolutePath())) && !"flac".equals(this.getFileTypeName(childFile.getAbsolutePath())) && !"ape".equals(this.getFileTypeName(childFile.getAbsolutePath()))) {
                                if ("apk".equals(this.getFileTypeName(childFile.getAbsolutePath()))) {
                                    fileInfo.setFileType("type_apk");
                                } else if ("zip".equals(this.getFileTypeName(childFile.getAbsolutePath()))) {
                                    fileInfo.setFileType("type_zip");
                                } else if ("rar".equals(this.getFileTypeName(childFile.getAbsolutePath()))) {
                                    fileInfo.setFileType("type_rar");
                                } else if ("jpeg".equals(this.getFileTypeName(childFile.getAbsolutePath()))) {
                                    fileInfo.setFileType("type_jpeg");
                                } else if ("jpg".equals(this.getFileTypeName(childFile.getAbsolutePath()))) {
                                    fileInfo.setFileType("type_jpg");
                                } else if ("png".equals(this.getFileTypeName(childFile.getAbsolutePath()))) {
                                    fileInfo.setFileType("type_png");
                                }else if ("ycf".equals(this.getFileTypeName(childFile.getAbsolutePath()))) {
                                    fileInfo.setFileType("type_ycf");
                                } else {
                                    fileInfo.setFileType("type_file");
                                }
                            } else {
                                fileInfo.setFileType("type_audio");
                            }
                        } else {
                            fileInfo.setFileType("type_video");
                        }
                    }

                    if (this.showFile) {
                        fileInfoList.add(fileInfo);
                    } else if (fileInfo.isFolder()) {
                        fileInfoList.add(fileInfo);
                    }
                }
            }
        }

        return sortData(fileInfoList);
    }

    public List<FileInfo> backToParent() {
        this.currentFile = this.currentFile.getParentFile();
        if (this.isRootFile(this.currentFile)) {
            this.isRootFile = true;
        } else {
            this.isRootFile = false;
        }

        this.currentFolderList.remove(this.currentFolderList.size() - 1);
        return this.resetCurrentFile(this.currentFolderList.size());
    }

    public boolean isRootFile(File file) {
        return this.rootFile.getAbsolutePath().equals(file.getAbsolutePath());
    }

    private String getParentName(String path) {
        int end = path.lastIndexOf("/") + 1;
        return path.substring(0, end);
    }

    private String getFileTypeName(String path) {
        int start = path.lastIndexOf(".") + 1;
        return start == -1 ? "" : path.substring(start);
    }
}
