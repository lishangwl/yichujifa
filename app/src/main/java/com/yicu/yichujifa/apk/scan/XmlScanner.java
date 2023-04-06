package com.yicu.yichujifa.apk.scan;

import android.util.Log;

import com.xieqing.codeutils.util.FileIOUtils;
import com.xieqing.codeutils.util.FileUtils;
import com.xieqing.codeutils.util.StringUtils;
import com.yicu.yichujifa.apk.bean.Action;
import com.yicu.yichujifa.apk.bean.ResFile;
import com.yicu.yichujifa.apk.bean.Xml;
import com.zhihu.matisse.compress.FileUtil;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileReader;
import java.io.StringReader;

import esqeee.xieqing.com.eeeeee.action.ActionHelper;
import esqeee.xieqing.com.eeeeee.library.RuntimeLog;
import uidesign.project.model.Attr;

public class XmlScanner extends Thread{
    private Xml.XmlItem item;
    private String path;

    public XmlScanner(Xml.XmlItem item,String path){
        this.item = item;
        this.path = path;
        RuntimeLog.log("开始扫描xml："+path);
    }

    @Override
    public void run() {
        try {
            XmlPullParser xmlPullParser = XmlPullParserFactory.newInstance().newPullParser();
            xmlPullParser.setInput(new FileReader(path));
            inflate:while (true){
                switch (xmlPullParser.getEventType()){
                    case XmlPullParser.START_DOCUMENT:
                        //开始解析
                        break;
                    case XmlPullParser.END_DOCUMENT:
                        //解析完毕
                        RuntimeLog.log("xml扫描结束："+path);
                        break inflate;
                    case XmlPullParser.START_TAG:
                        //开始解析节点
                        int attributeCount = xmlPullParser.getAttributeCount();
                        for (int i = 0;i<attributeCount;i++){
                            String name = xmlPullParser.getAttributeName(i);
                            String value = xmlPullParser.getAttributeValue(i);
                            if (Attr.getAttrType(name) == 3 && value!=null && !value.isEmpty() && ActionHelper.isActionFile(new File(value))){
                                item.getAction().add(new Action.ActionItem(FileUtils.getFileNameNoExtension(value),value));
                                RuntimeLog.log("扫描到自动化："+value);
                            }

                            if (Attr.getAttrType(name) == 5 && value!=null && !value.isEmpty()){
                                item.getResFile().add(new ResFile.ResItem(FileUtils.getFileNameNoExtension(value),value));
                            }
                        }
                        break;
                }
                xmlPullParser.next();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
