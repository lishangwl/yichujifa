package com.yicu.yichujifa.apk;

import com.xieqing.codeutils.util.FileUtils;
import com.xieqing.codeutils.util.SPUtils;
import com.yicu.yichujifa.apk.bean.Xml;

import java.util.HashMap;

import esqeee.xieqing.com.eeeeee.bean.JSONBean;

public class ProjectManager {
    private static ProjectManager instance = new ProjectManager();
    private static SPUtils pref = SPUtils.getInstance("project_manager");
    private static HashMap<String,Project> projectCashe = new HashMap<>();
    public static ProjectManager getInstance() {
        return instance;
    }



    public Project getProject(String projectid){
        if (hasProject(projectid)){
            return fromProject(projectid);
        }else{
            return newProject(projectid);
        }
    }

    private Project fromProject(String projectid){
        if (projectCashe.containsKey(projectid)){
            return projectCashe.get(projectid);
        }
        pref.put(projectid,true);
        JSONBean jsonBean = new JSONBean(pref.getString(projectid+"_data"));
        Project project = new Project();
        project.setIcon(jsonBean.getString("icon"));
        project.setAppName(jsonBean.getString("appName"));
        project.setAppPackage(jsonBean.getString("appPackage"));
        project.setAppVersionCode(jsonBean.getInt("appVersionCode"));
        project.setAppVersionName(jsonBean.getString("appVersionName"));
        project.setProjectid(projectid);
        project.setConfig(jsonBean.getJson("config"));
        project.setXml(new Xml(jsonBean.getArray("xml")));
        projectCashe.put(projectid,project);
        return project;
    }

    private Project newProject(String projectid){
        pref.put(projectid,true);
        Project project = new Project();
        project.setAppName("我的应用");
        project.setAppPackage("com.newapp");
        project.setAppVersionCode(1);
        project.setAppVersionName("1.0");
        project.setConfig(new JSONBean());
        project.setProjectid(projectid);
        Xml xml = new Xml();
        xml.addItem(new Xml.XmlItem(FileUtils.getFileNameNoExtension(projectid),projectid));
        project.setXml(xml);
        projectCashe.put(projectid,project);
        save(project);
        return project;
    }

    public void save(Project project){
        pref.put(project.getKey(),project.toString());
    }

    public boolean hasProject(String projectid){
        return pref.getBoolean(projectid,false);
    }
}
