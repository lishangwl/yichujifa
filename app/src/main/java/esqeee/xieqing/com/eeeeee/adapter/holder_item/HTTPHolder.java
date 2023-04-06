package esqeee.xieqing.com.eeeeee.adapter.holder_item;

import android.content.Context;
import android.content.DialogInterface;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.jhonjson.dialoglib.BottomListDialog;
import com.sevenheaven.segmentcontrol.SegmentControl;
import com.yicu.yichujifa.ui.theme.ThemeManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.adapter.MyAdapter;
import esqeee.xieqing.com.eeeeee.bean.JSONArrayBean;
import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.bean.VariableType;
import esqeee.xieqing.com.eeeeee.widget.ExpandableLayout;
import esqeee.xieqing.com.eeeeee.widget.ValotionEdittext;

public class HTTPHolder extends BaseHolder{

    @BindView(R.id.method)
    SegmentControl segmentControl;


    @BindView(R.id.type)
    SegmentControl type;

    @BindView(R.id.http_url)
    ValotionEdittext http_url;


    @BindView(R.id.post_text)
    ValotionEdittext post_text;

    @BindView(R.id.header_image)
    ImageView expland;

    @BindView(R.id.http_header)
    ExpandableLayout header;

    @BindView(R.id.add_header)
    View add_header;

    @BindView(R.id.http_headers)
    ViewGroup headers;

    @BindView(R.id.data_image)
    ImageView expland2;

    @BindView(R.id.http_data)
    ExpandableLayout data;

    @BindView(R.id.add_data)
    View add_data;

    @BindView(R.id.http_post_datas)
    ViewGroup datas;

    @BindView(R.id.novariable)
    View novariable;
    @BindView(R.id.variable)
    View variable;
    @BindView(R.id.http_choose_varibale)
    View choose;

    @BindView(R.id.novariable_header)
    View novariable_header;
    @BindView(R.id.variable_header)
    View variable_header;
    @BindView(R.id.http_choose_header)
    View http_choose_header;

    @BindView(R.id.variable_name)
    TextView variable_name;
    @BindView(R.id.variable_name_header)
    TextView variable_name_header;

    public HTTPHolder(Context context, MyAdapter adapter) {
        super(context,R.layout.holder_http,adapter);
    }

    private JSONArrayBean headerBean;
    private JSONArrayBean dataBean;

    @Override
    public void onBind(JSONBean jsonBean) {
        super.onBind(jsonBean);
        setTooltips("以指定方式向指定网址发送数据请求，返回得到的文本结果。");
        requestTime(false);
        JSONBean param = jsonBean.getJson("param");
        if (!param.has("header") || param.getArray("header")==null){
            param.put("header",new JSONArrayBean());
        }
        if (!param.has("datas")){
            param.put("datas",new JSONArrayBean());
        }
        headerBean = param.getArray("header");
        dataBean = param.getArray("datas");

        int requestMethod = param.getInt("requestMethod",0);

        http_url.bindChangeString(param,"url");
        http_url.setText(replace(param.getString("url")));


        post_text.bindChangeString(param,"post_text");
        post_text.setText(replace(param.getString("post_text")));

        add_header.setOnClickListener(v->{
            JSONBean json = new JSONBean();
            headerBean.put(json);
            headers.addView(new HeaderItem(headers.getContext(),json,headerBean.length() - 1));
        });

        add_data.setOnClickListener(v->{
            JSONBean json = new JSONBean();
            dataBean.put(json);
            datas.addView(new HeaderItem(datas.getContext(),json,dataBean.length() - 1));
        });
        data.setVisibility(requestMethod == 1 ?View.VISIBLE:View.GONE);

        segmentControl.setSelectedIndex(requestMethod);
        segmentControl.setOnSegmentControlClickListener(i->{
            param.put("requestMethod",i);
            data.setVisibility(i == 1 ?View.VISIBLE:View.GONE);
        });

        type.setSelectedIndex(param.getInt("dataType",0));
        type.setOnSegmentControlClickListener(i->{
            param.put("dataType",i);
            datas.setVisibility(i == 2 ? View.GONE : View.VISIBLE);
            add_data.setVisibility(i == 2 ? View.GONE : View.VISIBLE);
            post_text.setVisibility(i == 2 ? View.VISIBLE : View.GONE);
        });

        datas.setVisibility(param.getInt("dataType",0) == 2 ? View.GONE : View.VISIBLE);
        add_data.setVisibility(param.getInt("dataType",0) == 2 ? View.GONE : View.VISIBLE);
        post_text.setVisibility(param.getInt("dataType",0) == 2 ? View.VISIBLE : View.GONE);


        choose.setOnClickListener(v->{
            new BottomListDialog.Builder(getContext())
                    .addMenuListItem(new String[]{"清除","选取变量"},position -> {
                        if (position == 0){
                            param.remove("var");
                            bindVar(param.getString("var"));
                        }else{
                            List<JSONBean> strings = queryVariableByType(0);
                            String[] strings1 = new String[strings.size()];
                            for (int i = 0 ; i<strings.size() ;i++){
                                strings1[i] = strings.get(i).getString("name");
                            }
                            strings.clear();
                            strings = null;
                            new AlertDialog.Builder(getContext()).setTitle("选择变量").setItems(strings1,(DialogInterface dialogInterface, int i) ->{
                                param.put("var",strings1[i]);
                                bindVar(param.getString("var"));
                            }).create().show();
                        }
                    }).show();
        });
        http_choose_header.setOnClickListener(v->{
            new BottomListDialog.Builder(getContext())
                    .addMenuListItem(new String[]{"清除","选取变量"},position -> {
                        if (position == 0){
                            param.remove("respone_header");
                            bindheaderVar(param.getString("respone_header"));
                        }else{
                            List<JSONBean> strings = queryVariableByType(0);
                            String[] strings1 = new String[strings.size()];
                            for (int i = 0 ; i<strings.size() ;i++){
                                strings1[i] = strings.get(i).getString("name");
                            }
                            strings.clear();
                            strings = null;
                            new AlertDialog.Builder(getContext()).setTitle("选择变量").setItems(strings1,(DialogInterface dialogInterface, int i) ->{
                                param.put("respone_header",strings1[i]);
                                bindheaderVar(param.getString("respone_header"));
                            }).create().show();
                        }
                    }).show();
        });
        ThemeManager.attachTheme(segmentControl,type);

        bindHeader(headerBean);
        bindData(dataBean);
        bindVar(param.getString("var"));
        bindheaderVar(param.getString("respone_header"));
    }

    private void bindheaderVar(String varName) {
        JSONBean var = queryVariable(varName);
        if (var == null){
            variable_header.setVisibility(View.GONE);
            novariable_header.setVisibility(View.VISIBLE);
            return;
        }
        if (var.getInt("type") != VariableType.STRING.ordinal()){
            variable_header.setVisibility(View.GONE);
            novariable_header.setVisibility(View.VISIBLE);
            return;
        }
        novariable_header.setVisibility(View.GONE);
        variable_header.setVisibility(View.VISIBLE);
        variable_name_header.setText(varName);
    }
    private void bindVar(String varName) {
        JSONBean var = queryVariable(varName);
        if (var == null){
            noVariable();
            return;
        }
        if (var.getInt("type") != VariableType.STRING.ordinal()){
            noVariable();
            return;
        }
        novariable.setVisibility(View.GONE);
        variable.setVisibility(View.VISIBLE);
        variable_name.setText(varName);
    }

    private void noVariable() {
        variable.setVisibility(View.GONE);
        novariable.setVisibility(View.VISIBLE);
    }

    private void bindData(JSONArrayBean header) {
        datas.removeAllViews();
        for (int i = 0;i<header.length() ; i++){
            datas.addView(new HeaderItem(datas.getContext(),header.getJson(i),i));
        }
    }
    private void bindHeader(JSONArrayBean header) {
        headers.removeAllViews();
        for (int i = 0;i<header.length() ; i++){
            headers.addView(new HeaderItem(headers.getContext(),header.getJson(i),i));
        }
    }


    @Override
    public void initView() {
        header.setListener(isOpened -> {
            Animation animation = new RotateAnimation(isOpened?0:180,isOpened?180:0,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
            animation.setDuration(header.getDuration());
            animation.setFillAfter(true);
            expland.startAnimation(animation);
        });

        data.setListener(isOpened -> {
            Animation animation = new RotateAnimation(isOpened?0:180,isOpened?180:0,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
            animation.setDuration(header.getDuration());
            animation.setFillAfter(true);
            findViewById(R.id.data_image).startAnimation(animation);
        });
    }

    @Override
    public int getIcon() {
        return R.drawable.ic_dakaiwangzhi;
    }

    @Override
    public String getName() {
        return "访问网页";
    }

    private void deleteHeader(int pos){
        headerBean.remove(pos);
        bindHeader(headerBean);
    }
    private void deleteData(int pos){
        dataBean.remove(pos);
        bindData(dataBean);
    }

    class HeaderItem extends FrameLayout{
        private JSONBean item;
        private int position;

        @BindView(R.id.http_header_key)
        ValotionEdittext key;

        @BindView(R.id.http_header_value)
        ValotionEdittext value;

        @BindView(R.id.http_header_delete)
        View delete;

        public HeaderItem(@NonNull Context context,JSONBean item,int position) {
            super(context);
            addView(View.inflate(context,R.layout.holder_http_header_item,null));
            ButterKnife.bind(this,this);

            key.bindChangeString(item,"key");
            value.bindChangeString(item,"value");
            key.setText(replace(item.getString("key")));
            value.setText(replace(item.getString("value")));

            delete.setOnClickListener(v->{
                Log.d("xxxxxxxxxxxxx",((ViewGroup)getParent()).toString());
                if (((ViewGroup)getParent()) == headers){
                    deleteHeader(position);
                }else{
                    deleteData(position);
                }
            });
        }
    }
}
