package esqeee.xieqing.com.eeeeee.adapter.holder_item;

import android.content.Context;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.util.Log;

import butterknife.BindView;
import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.adapter.MyAdapter;
import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.doAction.DoActionBean;
import esqeee.xieqing.com.eeeeee.fragment.PramaerFragment;
import esqeee.xieqing.com.eeeeee.widget.ValotionEdittext;
import esqeee.xieqing.com.eeeeee.widget.span.MyClickSpan;

public class FileHolder extends BaseHolder{

    public FileHolder(Context context, MyAdapter adapter) {
        super(context,-1,adapter);
    }

    @Override
    public void onBind(JSONBean jsonBean) {
        super.onBind(jsonBean);
        requestTime(false);
        JSONBean param = jsonBean.getJson("param");
        String arg = param.getString("arg");
        setHead(arg,R.drawable.icon_56);

        setTooltips(null);
        switch (arg){
            case "删除文件":
                setContentXML(R.xml.file_delete_file);
                break;
            case "复制文件":
                setContentXML(R.xml.file_copy_file);
                break;
            case "重命名文件":
                setContentXML(R.xml.file_rename_file);
                break;
            case "删除目录":
            case "创建目录":
                setContentXML(R.xml.file_delete_dir);
                break;
            case "写出字节集文件":
                setTooltips("写出一段字节数组并保存为指定文件名到SD卡上，文件名必须为完整路径，例如：\"/sdcard/123.png\"。");
                setContentXML(R.xml.file_write_byte);
                break;
            case "写出文本文件":
                setTooltips("写出一段文本并保存为指定文件名到SD卡上，文件名必须为完整路径，例如：\"/sdcard/123.png\"。");
                setContentXML(R.xml.file_write_str);
                break;
            case "读入字节集文件":
                setTooltips("从Sd卡上读取指定文件，返回字节数组，文件名必须为完整路径，例如：\"/sdcard/123.png\"。");
                setContentXML(R.xml.file_read_byte);
                break;
            case "读入文本文件":
                setTooltips("从Sd卡上读取指定文件，返回一段文本，文件名必须为完整路径，例如：\"/sdcard/123.png\"。");
                setContentXML(R.xml.file_read_str);
                break;
            case "遍历目录":
                setTooltips("获取目录下所有的子目录及文化");
                setContentXML(R.xml.file_read_dir);
                break;
            case "文件是否存在":
                setContentXML(R.xml.file_exits);
                break;
            case "获取网络文件":
                setTooltips("用GET方式获取指定网址的网络文件，返回字节数组(字节集)\n" +
                        "参数二 超时 单位为毫秒，建议设置为5000。");
                setContentXML(R.xml.file_read_net);
                break;
            case "解压zip":
                setContentXML(R.xml.file_u_zip);
                break;
            case "压缩zip":
                setContentXML(R.xml.file_n_zip);
                break;
            case "打开文件":
                setTooltips("调用系统中已安装的相关应用来打开SD卡上的指定文件。");
                setContentXML(R.xml.file_open_file);
                break;
            case "取文件修改时间":
                setTooltips("获取文件的最后修改时间。");
                setContentXML(R.xml.file_get_last_modify_time);
                break;
            case "取文件编码":
                setTooltips("获取SD卡上文本文件的文档编码类型。");
                setContentXML(R.xml.file_get_file_encoding);
                break;
            case "是否为隐藏文件":
                setTooltips("判断SD卡上的指定目录或文件是否为隐藏文件，需提供完整路径。");
                setContentXML(R.xml.file_is_hide_file);
                break;
            case "取子目录":
                setTooltips("取出指定目录中的子目录，各子目录用|分隔，没有子目录则返回空文本。");
                setContentXML(R.xml.file_get_child_folder);
                break;
            case "寻找文件关键词":
                setTooltips("在指定目录中寻找出所有文件名包含关键词的文件和子目录，返回结果以换行符\"\\n\"连接，结果中包含文件的完整路径。");
                setContentXML(R.xml.file_search_file_keyword);
                break;
            case "寻找文件后缀名":
                setTooltips("在指定目录中找出所有指定后缀名(例如：txt)的文件，返回结果以换行符\"\\n\"连接，结果中包含文件的完整路径。");
                setContentXML(R.xml.file_search_file_extension);
                break;
            case "调用播放器播放":
                //setTooltips("");
                setContentXML(R.xml.file_open_video_file);
                break;
        }

    }



    @Override
    public void initView() {
    }

    @Override
    public int getIcon() {
        return R.drawable.ic_zhushi;
    }

    @Override
    public String getName() {
        return "脚本注释";
    }

}