package uidesign.project;

import android.app.Activity;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import uidesign.MainActivity;
import uidesign.project.exception.LayoutInflaterException;
import uidesign.project.inflater.ActivityInflater;
import uidesign.project.inflater.BaseLayoutInflater;
import uidesign.project.inflater.ButtonInflater;
import uidesign.project.inflater.CheckBoxInflater;
import uidesign.project.inflater.EditTextInflater;
import uidesign.project.inflater.GboalViewHolder;
import uidesign.project.inflater.ImageInflater;
import uidesign.project.inflater.LayoutInflater;
import uidesign.project.inflater.ProgressInflater;
import uidesign.project.inflater.SeekBarInflater;
import uidesign.project.inflater.SpinnerInflater;
import uidesign.project.inflater.SwitchInflater;
import uidesign.project.inflater.TextViewInflater;
import uidesign.project.inflater.ToolbarInflater;
import uidesign.project.inflater.WebViewInflater;
import uidesign.project.model.Attr;

public class ViewLayoutInflater {

    public static Map<String, LayoutInflater> inflaterMap = new HashMap<>();
    public static ActivityInflater activityInflater = new ActivityInflater();
    static {
        inflaterMap.put("按钮",new ButtonInflater());
        inflaterMap.put("图片",new ImageInflater());
        inflaterMap.put("标签",new TextViewInflater());
        inflaterMap.put("编辑框",new EditTextInflater());
        inflaterMap.put("选择框",new CheckBoxInflater());
        inflaterMap.put("开关",new SwitchInflater());
        inflaterMap.put("加载圈",new ProgressInflater());
        inflaterMap.put("进度条",new SeekBarInflater());
        inflaterMap.put("下拉框",new SpinnerInflater());
        inflaterMap.put("标题栏",new ToolbarInflater());
        inflaterMap.put("浏览器",new WebViewInflater());
    }



    private static final String TAG = "ViewLayoutInflater";
    private Activity activity;
    private XmlPullParser xmlPullParser;

    private ViewLayoutInflater(Activity activity){
        this.activity = activity;
        try {
            xmlPullParser = XmlPullParserFactory.newInstance().newPullParser();
        } catch (XmlPullParserException e) {
            throw new LayoutInflaterException(e,xmlPullParser);
        }
    }


    public static ViewLayoutInflater from(Activity context){
        GboalViewHolder.getInstance().reset();
        return new ViewLayoutInflater(context);
    }

    public static ViewLayoutInflater from(Activity context, boolean isReset){
        if (isReset){
            GboalViewHolder.getInstance().reset();
        }
        return new ViewLayoutInflater(context);
    }

    private ViewGroup viewGroup;
    private String xml;
    private boolean attachToParent;
    public void inflate(@Nullable ViewGroup viewGroup,String xmlBuffer,boolean attachToParent) throws LayoutInflaterException {
        inflate(viewGroup,xmlBuffer,attachToParent,true);
    }

    public void inflate(@Nullable ViewGroup viewGroup,String xmlBuffer,boolean attachToParent,boolean isDesgin) throws LayoutInflaterException {
        if (xmlBuffer == null){
            return;
        }
        this.xml = xmlBuffer;
        this.viewGroup = viewGroup;
        this.attachToParent = attachToParent;

        try {
            xmlPullParser.setInput(new StringReader(xml));
            inflate(isDesgin);
        } catch (XmlPullParserException e) {
            throw new LayoutInflaterException(e,xmlPullParser);
        }
    }


    private View inflate(boolean isDesgin) throws LayoutInflaterException {
        try {
            FrameLayout frameLayout = new FrameLayout(activity);
            inflate:while (true){
                switch (xmlPullParser.getEventType()){
                    case XmlPullParser.START_DOCUMENT:
                        Log.v(TAG,"开始解析");
                        break;
                    case XmlPullParser.END_DOCUMENT:
                        Log.v(TAG,"解析完毕");
                        break inflate;
                    case XmlPullParser.START_TAG:
                        Log.v(TAG,"开始解析节点");
                        inflateNode(frameLayout,isDesgin);
                        break;
                }
                    xmlPullParser.next();

            }
            if (attachToParent){
                viewGroup.addView(frameLayout);
            }
            return frameLayout;
        } catch (Exception e) {
            throw new LayoutInflaterException(e.getMessage());
        }
    }

    private void inflateNode(ViewGroup viewGroup,boolean isDesgin) {
        String tagName = getTagName();
        Attr attrs = getAttrs();
        if (tagName.equals("界面")){
            ViewLayoutInflater.activityInflater.inflate(activity,viewGroup,attrs);
            if (!isDesgin){
                if (MainActivity.currentActivity != null){
                    MainActivity.currentActivity.setAttr(attrs);
                }
            }else {
                GboalViewHolder.getInstance().setActivityAttr(attrs);
            }
            return;
        }
        if (!inflaterMap.containsKey(tagName)){
            throw new LayoutInflaterException("unknown tag ："+tagName);
        }

        BaseLayoutInflater layoutInflater = (BaseLayoutInflater) inflaterMap.get(tagName);
        Set<String> base = layoutInflater.baseAttr.keySet();
        Set<String> view = attrs.keySet();
        for (String string : base){
            if (!view.contains(string)){
                attrs.put(string,layoutInflater.baseAttr.getString(string));
            }
        }


        View inflateView = layoutInflater.getView(activity,attrs,isDesgin);
        viewGroup.addView(inflateView,inflateView.getLayoutParams());
    }

    private Attr getAttrs() {
        Attr attr = new Attr();
        int attributeCount = xmlPullParser.getAttributeCount();
        for (int i = 0;i<attributeCount;i++){
            attr.put(xmlPullParser.getAttributeName(i),xmlPullParser.getAttributeValue(i));
        }
        return attr;
    }

    private String getTagName() {
        return xmlPullParser.getName();
    }
}
