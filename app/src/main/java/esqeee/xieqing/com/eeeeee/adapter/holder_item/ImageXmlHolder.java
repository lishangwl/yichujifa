package esqeee.xieqing.com.eeeeee.adapter.holder_item;

import android.content.Context;

import androidx.appcompat.app.AlertDialog;

import com.xieqing.codeutils.util.FileUtils;

import java.io.File;

import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.adapter.MyAdapter;
import esqeee.xieqing.com.eeeeee.bean.JSONBean;

public class ImageXmlHolder extends BaseHolder{
    public ImageXmlHolder(Context context, MyAdapter adapter) {
        super(context,-1,adapter);
    }

    @Override
    public void onDelete() {
        super.onDelete();
    }

    @Override
    public void onBind(JSONBean jsonBean) {
        super.onBind(jsonBean);
        requestTime(true);
        int type = jsonBean.getInt("actionType");
        JSONBean param = jsonBean.getJson("param");

        setContentXML(R.xml.image);
    }

    @Override
    public JSONBean copy() {
        JSONBean jsonBean = new JSONBean(getJsonBean());
        String file = jsonBean.getJson("param").getString("fileName");

        File y = new File(file);
        File t = new File(y.getParent(),FileUtils.getFileNameNoExtension(y)+"_copy."+FileUtils.getFileExtension(y));
        FileUtils.copyFile(y,t);
        jsonBean.getJson("param").put("fileName",t.getAbsoluteFile());
        return jsonBean;
    }



    @Override
    public void initView() {

    }

    @Override
    public int getIcon() {
        return R.drawable.ic_shitu;
    }

    @Override
    public String getName() {
        return "识别图片";
    }

}
