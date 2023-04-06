package com.yicu.yichujifa.apk;

import com.stardust.autojs.apkbuilder.ApkBuilder;
import com.stardust.autojs.apkbuilder.KeyStore;
import com.xieqing.codeutils.util.FileIOUtils;
import com.xieqing.codeutils.util.FileUtils;
import com.yicu.yichujifa.GlobalContext;
import com.yicu.yichujifa.apk.bean.Action;
import com.yicu.yichujifa.apk.bean.ResFile;
import com.yicu.yichujifa.apk.bean.Xml;
import com.yicu.yichujifa.apk.util.JsonFormatTool;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;

import esqeee.xieqing.com.eeeeee.action.ActionHelper;
import esqeee.xieqing.com.eeeeee.bean.JSONBean;

public class Project {
    String appName;
    String appPackage;
    int appVersionCode;
    String appVersionName;
    String icon;
    String projectid;
    JSONBean config;
    Xml xml;

    public Xml getXml() {
        return xml;
    }

    public void setXml(Xml xml) {
        this.xml = xml;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIcon() {
        return icon;
    }

    public void setProjectid(String projectid) {
        this.projectid = projectid;
    }

    public void setConfig(JSONBean config) {
        this.config = config;
        if (config == null){
            this.config = new JSONBean();
        }
    }

    public JSONBean getConfig() {
        return config;
    }

    public String getProjectid() {
        return projectid;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public void setAppPackage(String appPackage) {
        this.appPackage = appPackage;
    }

    public void setAppVersionCode(int appVersionCode) {
        this.appVersionCode = appVersionCode;
    }

    public void setAppVersionName(String appVersionName) {
        this.appVersionName = appVersionName;
    }

    public int getAppVersionCode() {
        return appVersionCode;
    }

    public String getAppName() {
        return appName;
    }

    public String getAppPackage() {
        return appPackage;
    }

    public String getAppVersionName() {
        return appVersionName;
    }

    public String getKey(){
        return getProjectid()+"_data";
    }

    @Override
    public String toString() {
        JSONBean jsonBean = new JSONBean();
        jsonBean.put("icon",icon);
        jsonBean.put("appName",appName);
        jsonBean.put("appPackage",appPackage);
        jsonBean.put("appVersionCode",appVersionCode);
        jsonBean.put("appVersionName",appVersionName);
        jsonBean.put("projectid",projectid);
        jsonBean.put("config",config);
        jsonBean.put("xml",xml.toJson());
        return jsonBean.toString();
    }

    private File buildDir;
    private File apkDir;
    private File tempDir;
    private File resourcesDir;

    //打包Apk
    public String build() throws Exception{
        prepreBuild();//准备打包
        buildIcon();//打包icon
        buildResources();//打包资源文件
        buildProjectJson();//打包json

        outApk();//导出apk

        return new File(apkDir,getApkFileName()).getAbsolutePath();
        //ZipUtils.zipFile(buildDir,new File(ActionHelper.buildDir,getAppPackage()+"_"+getAppVersionName()+"_"+getAppVersionCode()+".zip"));
        //FileUtils.deleteDir(buildDir);
    }

    private void outApk() throws Exception{
        ApkBuilder apkBuilder = new ApkBuilder(GlobalContext.getContext().getAssets().open("app-debug.apk"),
                new File(apkDir,getApkFileName()), tempDir.getPath()).prepare();
        FileUtils.copyDir(resourcesDir,new File(tempDir,"assets/resources"));
        FileUtils.copyFile(new File(buildDir,"project.json"),new File(tempDir,"assets/project.json"));
        FileUtils.copyFile(new File(buildDir,"resources.json"),new File(tempDir,"assets/resources.json"));


        FileUtils.copyFile(new File(buildDir,"icon.png"),new File(tempDir,"res/drawable/icon.png"));

        apkBuilder.editManifest()
                .setVersionCode(getAppVersionCode())
                .setVersionName(getAppVersionName())
                .setAppName(getAppName())
                .setPackageName(getAppPackage())
                .commit();
        apkBuilder
                .setArscPackageName(getAppPackage())
                .build()
                .buildApk()
                .sign();
    }

    private String getApkFileName() {
        return getAppPackage()+"_"+getAppVersionName()+"_"+getAppVersionCode()+".apk";
    }

    private void buildIcon() {
        FileUtils.copyFile(getIcon(),new File(buildDir,"icon.png").getAbsolutePath());
    }

    private void buildProjectJson() {
        JSONBean jsonBean = new JSONBean();
        JSONBean app = new JSONBean();
        app.put("name",getAppName());
        app.put("package",getAppPackage());
        app.put("version",getAppVersionCode());
        app.put("versionName",getAppVersionName());
        app.put("main",getProjectid());
        app.put("config",config);
        jsonBean.put("App",app);

        FileIOUtils.writeFileFromString(new File(buildDir,"project.json"), JsonFormatTool.formatJson(jsonBean.toString()));
    }

    private void prepreBuild() {
        buildDir = new File(ActionHelper.buildDir,getAppPackage()+"_"+getAppVersionName()+"_"+getAppVersionCode());
        apkDir = new File(buildDir,"apk");
        resourcesDir = new File(buildDir,"resources");
        tempDir = new File(buildDir,"temp");
        FileUtils.createOrExistsDir(buildDir,resourcesDir,apkDir,tempDir);
    }

    //打包所有资源文件
    private void buildResources(){
        HashMap<String,String> resources = new HashMap<>();
        for (int i = 0;i<xml.getItemCount();i++){
            Xml.XmlItem xmlItem = xml.getItem(i);
            buildResource(resources,xmlItem);
            resources.put(xmlItem.getPath(),buildResource(xmlItem.getPath()));
            for (int j = 0;j<xmlItem.getAction().size();j++){
                Action.ActionItem actionItem = xmlItem.getAction().getItem(j);
                resources.put(actionItem.getPath(),buildResource(actionItem.getPath()));
                for (int k = 0;k<actionItem.getResFile().size();k++){
                    ResFile.ResItem resItem = actionItem.getResFile().getItem(k);
                    resources.put(resItem.getPath(),buildResource(resItem.getPath()));
                }
            }
        }
        FileIOUtils.writeFileFromString(new File(buildDir,"resources.json"),JsonFormatTool.formatJson(new JSONBean(resources).toString()));
    }

    private void buildResource(HashMap<String,String> resources,Xml.XmlItem xmlItem) {
        for (int k = 0;k<xmlItem.getResFile().size();k++){
            ResFile.ResItem resItem = xmlItem.getResFile().getItem(k);
            resources.put(resItem.getPath(),buildResource(resItem.getPath()));
        }
    }

    private String buildResource(String path) {
        File file = new File(path);
        if (!file.exists() || file.isDirectory()){
            return "";
        }
        File newPath = new File(resourcesDir,FileUtils.getFileMD5ToString(file)+".bytes");
        FileUtils.copyFile(file,newPath);
        return newPath.getName();
    }
}
