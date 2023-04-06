package com.yicu.yichujifa.apk.scan;

import com.xieqing.codeutils.util.FileUtils;
import com.yicu.yichujifa.apk.bean.Action;
import com.yicu.yichujifa.apk.bean.ResFile;
import com.yicu.yichujifa.apk.bean.Xml;
import com.zhihu.matisse.compress.FileUtil;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileReader;
import java.util.List;

import esqeee.xieqing.com.eeeeee.action.ActionHelper;
import esqeee.xieqing.com.eeeeee.library.RuntimeLog;
import uidesign.project.model.Attr;

public class ActionScanner extends Thread{
    private Action.ActionItem item;
    private String path;

    public ActionScanner(Action.ActionItem item, String path){
        this.item = item;
        this.path = path;
        RuntimeLog.log("开始扫描自动化："+path);
    }

    @Override
    public void run() {
        List<String> images = ActionHelper.getAllImages(ActionHelper.from(path));
        for (String image : images){
            RuntimeLog.log("扫描到文件："+image);
            item.getResFile().add(new ResFile.ResItem(FileUtils.getFileNameNoExtension(image),image));
        }
        RuntimeLog.log("自动化扫描结束："+path);
    }

}
