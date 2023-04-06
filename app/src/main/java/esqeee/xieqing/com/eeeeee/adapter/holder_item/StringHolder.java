package esqeee.xieqing.com.eeeeee.adapter.holder_item;

import android.content.Context;

import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.adapter.MyAdapter;
import esqeee.xieqing.com.eeeeee.bean.JSONBean;

public class StringHolder extends BaseHolder{

    public StringHolder(Context context, MyAdapter adapter) {
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
            case "到小写":
            case "到大写":
                setContentXML(R.xml.string_touper);
                break;
            case "取文本左边":
            case "取文本右边":
                setContentXML(R.xml.string_subleft);
                break;
            case "取文本长度":
            case "文本到整数":
                setContentXML(R.xml.string_strlen);
                break;
            case "取文本中间":
                setTooltips("取左边文本和右边文本的中间文本");
                setContentXML(R.xml.string_strsub);
                break;
            case "批量取文本中间":
                setTooltips("取出所有符合左边文本和右边文本的中间文本");
                setContentXML(R.xml.string_strsub_array);
                break;
            case "分割文本":
                setTooltips("将指定文本用指定分割符进行分割，返回分割后的文本数组。");
                setContentXML(R.xml.string_split);
                break;
            case "子文本替换":
                setTooltips("将文本中指定的子文本替换成另一子文本,返回转换后的文本");
                setContentXML(R.xml.string_replace);
                break;
            case "子文本替换2":
                setTooltips("将文本中指定位置的子文本替换成另一子文本,返回转换后的文本。");
                setContentXML(R.xml.string_replace_two);
                break;
            case "删首尾空":
                setContentXML(R.xml.encode);
                break;
            case "倒找文本":
            case "寻找文本":
                setTooltips("寻找指定文本，返回最先找到的文本在被搜寻文本中的位置，位置从0开始，0表示第一个字符，1表示第二个字符，如果未找到则返回-1");
                setContentXML(R.xml.string_find);
                break;
            case "文本比较":
                setTooltips("如果返回值小于0，表示文本一小于文本二；如果等于0，表示文本一等于文本二；如果大于0，表示文本一大于文本二。");
                setContentXML(R.xml.string_equals);
                break;
            case "翻转文本":
                setTooltips("翻转指定文本,返回转换后的文本。");
                setContentXML(R.xml.string_flip);
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