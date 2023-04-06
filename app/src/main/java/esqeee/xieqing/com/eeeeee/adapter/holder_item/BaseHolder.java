package esqeee.xieqing.com.eeeeee.adapter.holder_item;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.XmlResourceParser;
import android.graphics.Color;
import android.graphics.Rect;
import androidx.annotation.DrawableRes;
import androidx.core.widget.NestedScrollView;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.jhonjson.dialoglib.BottomListDialog;
import com.sevenheaven.segmentcontrol.SegmentControl;
//import com.tooltip.Tooltip;
import com.xieqing.codeutils.util.AppUtils;
import com.xieqing.codeutils.util.FileUtils;
import com.xieqing.codeutils.util.SizeUtils;
import com.xieqing.codeutils.util.StringUtils;
import com.xieqing.codeutils.util.ToastUtils;
import com.yicu.yichujifa.ui.theme.ThemeManager;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.ButterKnife;
import ch.ielse.view.SwitchView;
import esqeee.xieqing.com.eeeeee.AddActivity;
import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.adapter.MyAdapter;
import esqeee.xieqing.com.eeeeee.bean.JSONArrayBean;
import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.bean.VariableType;
import esqeee.xieqing.com.eeeeee.dialog.MakeSizeDialog;
import esqeee.xieqing.com.eeeeee.fragment.AppsFragment;
import esqeee.xieqing.com.eeeeee.fragment.VariableTableFragment;
import esqeee.xieqing.com.eeeeee.fragment.XmlsFragment;
import esqeee.xieqing.com.eeeeee.library.RuntimeLog;
import esqeee.xieqing.com.eeeeee.listener.ActionClickMoreEdit;
import esqeee.xieqing.com.eeeeee.ui.BaseActivity;
import esqeee.xieqing.com.eeeeee.widget.ValotionEdittext;
import esqeee.xieqing.com.eeeeee.widget.span.IconTextSpan;
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;
import parsii.eval.Expression;
import parsii.eval.Parser;
import parsii.eval.Scope;
import parsii.tokenizer.ParseException;

import static org.xmlpull.v1.XmlPullParser.END_DOCUMENT;
import static org.xmlpull.v1.XmlPullParser.START_DOCUMENT;
import static org.xmlpull.v1.XmlPullParser.START_TAG;

public abstract class BaseHolder extends ViewHolder{
    private MyAdapter adapter;
    private Context context;
    public View contentView;

    private List<JSONBean> variables = new ArrayList<>();

    public BaseHolder setVariables(List<JSONBean> variables) {
        this.variables = variables;
        return this;
    }

    public void setContentXML(int id){
        if (contentView!=null){
            ((ViewGroup)findViewById(R.id.holder_content)).removeAllViews();
        }
        contentView = null;
        //System.gc();

        if (id == -1){
            return;
        }
        XmlResourceParser xmlResourceParser = context.getResources().getXml(id);
        LinearLayout linearLayout = null;
        ss:while (true){
            try {
                switch (xmlResourceParser.getEventType()){
                    case START_DOCUMENT:
                        linearLayout = new LinearLayout(context);
                        linearLayout.setOrientation(LinearLayout.VERTICAL);
                        break;
                    case START_TAG:
                        if (!xmlResourceParser.getName().equals("root")){
                            linearLayout.addView(parseTag(xmlResourceParser));
                        }
                        break;
                    case END_DOCUMENT:
                        break ss;
                }
                xmlResourceParser.next();
            }catch (XmlPullParserException e){
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        contentView = linearLayout;
        ((ViewGroup)findViewById(R.id.holder_content)).addView(contentView);
        contentView.setAlpha((jsonBean.has("status") ? jsonBean.getBoolean("status"):true)?1f:0.1f);
    }

    private View parseTag(XmlPullParser pullParser) throws XmlPullParserException{
        String tagName = pullParser.getName();
        if (tagName.equals("input")){
            return parseInputItem(pullParser);
        }else if (tagName.equals("hr")){
            View view = new View(context);
            view.setBackgroundColor(Color.parseColor("#e4e4e4"));
            ViewGroup.MarginLayoutParams marginLayoutParams =new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,1);
            marginLayoutParams.leftMargin = 50;
            marginLayoutParams.rightMargin = 50;
            view.setLayoutParams(marginLayoutParams);
            return view;
        }else if (tagName.equals("var")){
            return parseVarItem(pullParser);
        }else if (tagName.equals("switch")){
            return parseSwitchItem(pullParser);
        }else if (tagName.equals("texteara")){
            return parseTextearaItem(pullParser);
        }else if (tagName.equals("rect")){
            return parseRectItem(pullParser);
        }else if (tagName.equals("segment")){
            return parseSegmentItem(pullParser);
        }else if (tagName.equals("list-xy")){
            return parseListXYItem(pullParser);
        }else if (tagName.equals("list-xyt")){
            return parseListXYTItem(pullParser);
        }else if (tagName.equals("seek")){
            return parseSeekView(pullParser);
        }else if (tagName.equals("app")){
            return parseAppView(pullParser);
        }else if (tagName.equals("ycfml")){
            return parseYcfmlView(pullParser);
        }
        return null;
    }
    private View parseYcfmlView(XmlPullParser pullParser){
        View view = View.inflate(context,R.layout.item_ycfml,null);
        TextView textView = view.findViewById(R.id.title);
        TextView app_name = view.findViewById(R.id.app_name);
        ImageView app_icon = view.findViewById(R.id.app_icon);

        String title = pullParser.getAttributeValue(null,"title");
        String save = pullParser.getAttributeValue(null,"save");

        textView.setText(title);
        app_name.setText(FileUtils.getFileNameNoExtension(param.getString(save)));

        view.setOnClickListener(v->{
            XmlsFragment xmlsFragment = new XmlsFragment();
            xmlsFragment.setOnActionSelectedListener(b->{
                param.put(save,b.getAbsolutePath());
                app_name.setText(FileUtils.getFileNameNoExtension(b));
                xmlsFragment.dismiss();
            });
            xmlsFragment.show(((BaseActivity)getContext()).getSupportFragmentManager(),"xmls");
        });
        return view;
    }
    private View parseAppView(XmlPullParser pullParser){
        View view = View.inflate(context,R.layout.item_app,null);
        TextView textView = view.findViewById(R.id.title);
        TextView app_name = view.findViewById(R.id.app_name);
        ImageView app_icon = view.findViewById(R.id.app_icon);

        String title = pullParser.getAttributeValue(null,"title");
        String save = pullParser.getAttributeValue(null,"save");
        AppUtils.AppInfo appInfo = AppUtils.getAppInfo(param.getString(save));

        textView.setText(title);
        app_name.setText(appInfo == null ? "" : appInfo.getName());
        app_icon.setImageDrawable(appInfo == null ? null : appInfo.getIcon());

        view.setOnClickListener(v->{
            AppsFragment appsFragment = new AppsFragment();
            appsFragment.setOnAppsSelectedListener((appInfo2)-> {
                param.put(save,appInfo2.getPackageName());
                app_name.setText(appInfo2 == null ? "" : appInfo2.getName());
                app_icon.setImageDrawable(appInfo2 == null ? null : appInfo2.getIcon());
                appsFragment.dismiss();
            });
            appsFragment.show(((BaseActivity)getContext()).getSupportFragmentManager(),"chooseApp");
        });
        return view;
    }
    private View parseSeekView(XmlPullParser pullParser){
        View view = View.inflate(context,R.layout.item_seek,null);
        SeekBar seekBar = view.findViewById(R.id.seekBar);
        TextView textView = view.findViewById(R.id.title);

        String title = pullParser.getAttributeValue(null,"title");
        String max = pullParser.getAttributeValue(null,"max");
        String defult = pullParser.getAttributeValue(null,"defult");
        String save = pullParser.getAttributeValue(null,"save");

        textView.setText(title);
        seekBar.setMax(Integer.parseInt(max));
        seekBar.setProgress(getParam().getInt(save,Integer.parseInt(defult)));
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b){
                    param.put(save,i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        ThemeManager.attachTheme(seekBar);
        return view;
    }

    private View parseListXYTItem(XmlPullParser pullParser) {

        View view = View.inflate(context,R.layout.item_list,null);
        LinearLayout linearLayout = view.findViewById(R.id.content);

        String save = pullParser.getAttributeValue(null,"save");
        JSONArrayBean lists = getParam().getArray(save);
        if (lists == null){
            lists = new JSONArrayBean();
            param.put(save,lists);
        }
        for (int i = 0;i<lists.length();i++){
            View view1 = getXYTItem(lists.getJson(i));
            int finalI = i;
            view1.findViewById(R.id.item_delete).setOnClickListener(v->{
                linearLayout.removeView(view1);
                JSONArrayBean list = getParam().getArray(save);
                list.remove(finalI);
                adapter.notifyItemChanged(getAdapterPosition());
            });
            linearLayout.addView(view1);
        }
        view.findViewById(R.id.add_item).setOnClickListener(v->{
            JSONBean jsonBean = new JSONBean("{\"x\":\"\",\"y\":\"\",\"time\":\"\"}");
            JSONArrayBean list = getParam().getArray(save);
            list.put(jsonBean);
            param.put(save,list);

            View view1 = getXYTItem(jsonBean);
            int finalI = list.length() - 1;
            view1.findViewById(R.id.item_delete).setOnClickListener(v1->{
                linearLayout.removeView(view1);
                list.remove(finalI);
                adapter.notifyItemChanged(getAdapterPosition());
            });
            linearLayout.addView(view1);
        });
        return view;
    }

    private View parseListXYItem(XmlPullParser pullParser) {

        View view = View.inflate(context,R.layout.item_list,null);
        LinearLayout linearLayout = view.findViewById(R.id.content);

        String save = pullParser.getAttributeValue(null,"save");
        JSONArrayBean lists = getParam().getArray(save);
        if (lists == null){
            lists = new JSONArrayBean();
            param.put(save,lists);
        }
        for (int i = 0;i<lists.length();i++){
            View view1 = getXYItem(lists.getJson(i));
            int finalI = i;
            view1.findViewById(R.id.item_delete).setOnClickListener(v->{
                linearLayout.removeView(view1);
                JSONArrayBean list = getParam().getArray(save);
                list.remove(finalI);
                adapter.notifyItemChanged(getAdapterPosition());
            });
            linearLayout.addView(view1);
        }
        view.findViewById(R.id.add_item).setOnClickListener(v->{
            JSONBean jsonBean = new JSONBean("{\"x\":\"\",\"y\":\"\"}");
            JSONArrayBean list = getParam().getArray(save);
            list.put(jsonBean);
            param.put(save,list);

            View view1 = getXYItem(jsonBean);
            int finalI = list.length() - 1;
            view1.findViewById(R.id.item_delete).setOnClickListener(v1->{
                linearLayout.removeView(view1);
                list.remove(finalI);
                adapter.notifyItemChanged(getAdapterPosition());
            });
            linearLayout.addView(view1);
        });
        return view;
    }

    private View getXYTItem(JSONBean jsonBean) {
        if (jsonBean == null){
            jsonBean = new JSONBean();
        }
        View view = View.inflate(context,R.layout.item_list_xyt,null);
        ValotionEdittext x = view.findViewById(R.id.xy_x);
        ValotionEdittext y = view.findViewById(R.id.xy_y);
        ValotionEdittext t = view.findViewById(R.id.xy_t);
        x.setText(replace(jsonBean.getString("x")));
        y.setText(replace(jsonBean.getString("y")));
        t.setText(replace(jsonBean.getString("time")));
        x.bindChangeString(jsonBean,"x");
        y.bindChangeString(jsonBean,"y");;
        t.bindChangeString(jsonBean,"time");
        checkInputMathExpression(x);
        checkInputMathExpression(y);
        checkInputMathExpression(t);

        return view;
    }

    private View getXYItem(JSONBean jsonBean) {
        if (jsonBean == null){
            jsonBean = new JSONBean();
        }
        View view = View.inflate(context,R.layout.item_list_xy,null);
        ValotionEdittext x = view.findViewById(R.id.xy_x);
        ValotionEdittext y = view.findViewById(R.id.xy_y);
        x.setText(replace(jsonBean.getString("x")));
        y.setText(replace(jsonBean.getString("y")));
        x.bindChangeString(jsonBean,"x");
        y.bindChangeString(jsonBean,"y");
        checkInputMathExpression(x);
        checkInputMathExpression(y);

        return view;
    }

    private View parseSegmentItem(XmlPullParser pullParser) {
        String title = pullParser.getAttributeValue(null,"title");
        String texts = pullParser.getAttributeValue(null,"texts");
        String defult = pullParser.getAttributeValue(null,"defult");
        String save = pullParser.getAttributeValue(null,"save");

        View view = View.inflate(context,R.layout.item_segment,null);
        SegmentControl segmentControl = view.findViewById(R.id.segment);
        segmentControl.setText(texts.split("\\|"));
        segmentControl.setSelectedIndex(param.getInt(save,Integer.parseInt(defult)));
        segmentControl.setOnSegmentControlClickListener(index -> {
            getParam().put(save,index);
        });
        ((TextView)view.findViewById(R.id.title)).setText(title);
        ThemeManager.attachTheme(segmentControl);
        return view;
    }
    private View parseTextearaItem(XmlPullParser pullParser) {
        String inputType = pullParser.getAttributeValue(null,"inputType");
        String defult = pullParser.getAttributeValue(null,"defult");
        String hint = pullParser.getAttributeValue(null,"hint");
        String save = pullParser.getAttributeValue(null,"save");

        NestedScrollView view = (NestedScrollView) View.inflate(context,R.layout.item_texteare,null);
        ValotionEdittext valotionEdittext = view.findViewById(R.id.input);
        valotionEdittext.setHint(hint);
        valotionEdittext.setText(defult);

        valotionEdittext.setText(replace(getParam().getString(save)));
        valotionEdittext.bindChangeString(getParam(),save);
        return view;
    }

    private View parseVarItem(XmlPullParser pullParser) throws XmlPullParserException{
        String title = pullParser.getAttributeValue(null,"title");
        int varType = Integer.parseInt(pullParser.getAttributeValue(null,"varType"));
        String save = pullParser.getAttributeValue(null,"save");
        return getVarView(title,varType,save);
    }

    public View getVarView(String title,int varType,String save){
        View v = View.inflate(context,R.layout.item_var,null);

        ViewGroup root = (ViewGroup) ((ViewGroup)v).getChildAt(0);

        TextView textView = (TextView) root.getChildAt(0);
        textView.setText(title);

        View novariable =  root.getChildAt(1);
        ViewGroup variable = (ViewGroup) root.getChildAt(2);
        TextView variable_name = (TextView) variable.getChildAt(0);

        String old = getParam().getString(save);
        JSONBean var = queryVariable(old);
        if (var!=null){
            novariable.setVisibility(View.GONE);
            variable.setVisibility(View.VISIBLE);
            variable_name.setText(old);
        }else{
            novariable.setVisibility(View.VISIBLE);
            variable.setVisibility(View.GONE);
        }
        root.setOnClickListener((view1)->{
            BottomListDialog more = new BottomListDialog.Builder(getContext())
                    .addMenuListItem(new String[]{"清除","选取变量"},position -> {
                        if (position == 0){
                            getParam().remove(save);
                            novariable.setVisibility(View.VISIBLE);
                            variable.setVisibility(View.GONE);
                        }else{
                            List<JSONBean> strings = queryVariableByType(varType);
                            String[] strings1 = new String[strings.size()];
                            for (int i = 0 ; i<strings.size() ;i++){
                                strings1[i] = strings.get(i).getString("name");
                            }
                            strings.clear();
                            strings = null;
                            String titleC = "选择变量";
                            try {
                                titleC = "选择["+VariableType.values()[varType].getTypeName()+"]变量";
                            }catch (Exception e){}

                            new AlertDialog.Builder(getContext()).setTitle(titleC).setItems(strings1,(DialogInterface dialogInterface, int i) ->{
                                getParam().put(save,strings1[i]);
                                novariable.setVisibility(View.GONE);
                                variable.setVisibility(View.VISIBLE);
                                variable_name.setText(strings1[i]);
                            }).create().show();
                        }
                    }).create();
            more.show();
        });
        return v;
    }

    private View parseSwitchItem(XmlPullParser pullParser) throws XmlPullParserException{
        String title = pullParser.getAttributeValue(null,"title");
        boolean checked = Boolean.getBoolean(pullParser.getAttributeValue(null,"checked"));
        String save = pullParser.getAttributeValue(null,"save");

        View view = View.inflate(context,R.layout.item_switch,null);
        TextView textView = view.findViewById(R.id.title);
        SwitchView switchView = view.findViewById(R.id.item_switch);
        switchView.setOpened(getParam().getBoolean(save,checked));
        switchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParam().put(save,switchView.isOpened());
            }
        });
        textView.setText(title);
        return view;
    }
    private View parseRectItem(XmlPullParser pullParser) throws XmlPullParserException{
        String title = pullParser.getAttributeValue(null,"title");
        String save = pullParser.getAttributeValue(null,"save");
        JSONBean RectParam = getParam().getJson(save);
        if (RectParam == null){
            RectParam = new JSONBean();
            getParam().put(save,RectParam);
        }

        View view = View.inflate(context,R.layout.holder_text_rect,null);

        TextView rect = view.findViewById(R.id.rect);
        View showView= view.findViewById(R.id.show_rect);
        TextView textView = view.findViewById(R.id.title);
        textView.setText(title);

        Rect rect1 = getRect(RectParam);
        showView.setTag(rect);
        rect.setText(getParam().getJson(save).has("rectVar")?getParam().getJson(save).getString("rectVar"):(rect1 == null ? "选取区域" :rect1.toShortString()));
        showView.setOnClickListener(new View.OnClickListener() {

            private void selectRect(JSONBean param) {
                ToastUtils.showLong("再次点击准心，即可重新选取");
                ((AddActivity)getContext()).setHookListener(v->{
                    new MakeSizeDialog(getContext())
                            .setTip("请选择区域")
                            .setOnMakeSizeRectListener((rect2)->{
                                showView.setTag(rect2);
                                setRect(rect2,getParam().getJson(save));
                                rect.setText(rect2.toShortString());
                            }).show();
                });
            }
            @Override
            public void onClick(View view) {
                if (showView.getTag() == null){
                    selectRect(getParam().getJson(save));
                    return;
                }
                new BottomListDialog.Builder(getContext())
                        .addMenuListItem(new String[]{"清除(即全屏)", "选取区域","选取矩阵变量"},position -> {
                            if (position == 1){
                                getParam().getJson(save).remove("rectVar");
                                selectRect(getParam().getJson(save));
                            }else if (position ==2){
                                List<JSONBean> strings = queryVariableByType(7);
                                String[] strings1 = new String[strings.size()];
                                for (int i = 0 ; i<strings.size() ;i++){
                                    strings1[i] = strings.get(i).getString("name");
                                }
                                strings.clear();
                                strings = null;
                                new AlertDialog.Builder(getContext()).setTitle("选择["+ VariableType.values()[7].getTypeName()+"]变量").setItems(strings1,(DialogInterface dialogInterface, int i) ->{
                                    clearRect(getParam().getJson(save));
                                    getParam().getJson(save).put("rectVar",strings1[i]);
                                    rect.setText(strings1[i]);
                                }).create().show();
                            }else{
                                clearRect(getParam().getJson(save));
                                rect.setText(getParam().getJson(save).has("rectVar")?getParam().getJson(save).getString("rectVar"):(rect1 == null ? "选取区域" :rect1.toShortString()));
                            }
                        }).show();
            }
        });
        return view;
    }
    private View parseInputItem(XmlPullParser pullParser) throws XmlPullParserException{
        String title = pullParser.getAttributeValue(null,"title");
        String inputType = pullParser.getAttributeValue(null,"inputType");
        String defult = pullParser.getAttributeValue(null,"defult");
        String hint = pullParser.getAttributeValue(null,"hint");
        String save = pullParser.getAttributeValue(null,"save");
        View view = View.inflate(context,R.layout.item_input,null);
        TextView textView = view.findViewById(R.id.title);
        ValotionEdittext valotionEdittext = view.findViewById(R.id.text);
        valotionEdittext.setHint(hint);
        textView.setText(title);

        valotionEdittext.setText(replace(getParam().getString(save,defult)));
        valotionEdittext.bindChangeString(getParam(),save);
        if (inputType.equals("int")){
            checkInputMathExpression(valotionEdittext);
        }
        return view;
    }

    public String[] findVariables(String find){
        return StringUtils.getSubStringArray(find,"{","}");
    }
    public String getString(String eval){
        String[] variables = findVariables(eval);
        for (String name : variables){
            JSONBean variable = VariableTableFragment.queryFinal(name);
            if (variable == null){
                variable = queryVariable(name);
                if (variable == null){
                    continue;
                }
            }
            eval = eval.replace("{"+name+"}",variable.getString("value"));
        }
        return eval;
    }
    public CharSequence replace(String old){
        SpannableString spannableString = new SpannableString(old);

        Pattern pattern = Pattern.compile("\\{(.*?)\\}");
        Matcher matcher = pattern.matcher(old);
        while (matcher.find()){
            int start = matcher.start();
            int end = matcher.end();
            String name = matcher.group();
            name = name.substring(1,name.length()-1);

            JSONBean variable = VariableTableFragment.queryFinal(name);
            int type = 0;
            if (variable == null){
                variable = queryVariable(name);
                if (variable == null){
                    continue;
                }
            }
            type = variable.getInt("type");

            IconTextSpan span = new IconTextSpan();
            spannableString.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spannableString;
    }

    public void checkInputMathExpression(ValotionEdittext edittext){
        edittext.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b){
                    //失去焦点了，输入完毕
                    if (!isCanEval(edittext.getText().toString())){
                        edittext.setError("计算表达式有误，脚本可能无法正常实现");
                    }
                }
            }
        });
    }


    public Rect getRect(JSONBean param){
        if (param == null){
            return null;
        }
        int left = param.getInt("left",-1);
        int top = param.getInt("top",-1);
        int right = param.getInt("right",-1);
        int bottom = param.getInt("bottom",-1);
        if (left == -1 || top == -1 || right == -1 || bottom == -1){
            return null;
        }
        return new Rect(left,top,right,bottom);
    }
    public void setRect(Rect rect,JSONBean param){
        if (param == null){
            return;
        }
        param.put("left",rect.left);
        param.put("top",rect.top);
        param.put("right",rect.right);
        param.put("bottom",rect.bottom);
    }
    public void clearRect(JSONBean param){
        param.remove("left");
        param.remove("top");
        param.remove("right");
        param.remove("bottom");
        param.remove("rectVar");
    }

    private boolean isCanEval(String eval){
        try {
            Scope scope = new Scope();

            Pattern pattern = Pattern.compile("\\{(.*?)\\}");
            Matcher matcher = pattern.matcher(eval);
            while (matcher.find()){
                int start = matcher.start();
                int end = matcher.end();
                String name = matcher.group();
                if (name.length() <= 2){
                    return false;
                }
                name = name.substring(1,name.length()-1);

                JSONBean variable = VariableTableFragment.queryFinal(name);
                int type = 0;
                if (variable == null){
                    variable = queryVariable(name);
                    if (variable == null){
                        continue;
                    }
                }
                type = variable.getInt("type");
                if (type == VariableType.STRING.ordinal() || type == VariableType.INT.ordinal()){
                    eval = eval.substring(0,start) + "(1)" + eval.substring(end,eval.length());
                }else{
                    continue;
                }

            }

            Expression expr = null;
            try {
                expr = Parser.parse(eval, scope);
            } catch (ParseException e) {
                //RuntimeLog.log("计算错误："+e.getErrors());
                return false;
            }
        }catch (Exception e){
            RuntimeLog.log("isCanEvalError:"+e.getMessage()+" : "+ eval);
        }
        return true;
    }

    public JSONBean queryVariable(String name) {
        for (JSONBean v : getVariables()){
            if (v.getString("name").equals(name)){
                return v;
            }
        }
        return null;
    }
    public List<JSONBean> queryVariableByType(int type) {
        List<JSONBean> types = new ArrayList<>();
        for (JSONBean v : getVariables()){
            if (v.getInt("type")== type || type == -1){
                types.add(v);
            }
        }
        return types;
    }
    public List<JSONBean> getVariables() {
        return variables;
    }

    public static View initView(Context context, int resId){
        View view = View.inflate(context,resId,null);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams==null){
            layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        layoutParams.width = -1;
        layoutParams.height = -2;
        view.setLayoutParams(layoutParams);
        return view;
    }

    public RecyclerView.Adapter getAdapter() {
        return adapter;
    }

    public BaseHolder(Context context, int resId, MyAdapter adapter) {
        super(initView(context,R.layout.baseholder));
        this.adapter = adapter;
        this.context = context;
        View view = itemView;
        if (resId != -1){
            contentView = View.inflate(context,resId,null);
            ButterKnife.bind(this,contentView);
            ((ViewGroup)view.findViewById(R.id.holder_content)).setVisibility(View.VISIBLE);
            ((ViewGroup)view.findViewById(R.id.holder_content)).addView(contentView);
        }
        (view.findViewById(R.id.header)).setOnClickListener(v->{
            if (contentView == null){
                return;
            }
            if (contentView.getVisibility() == View.GONE){
                contentView.setVisibility(View.VISIBLE);
                ((ImageView)view.findViewById(R.id.holder_expland)).setImageResource(R.mipmap.ic_ex_s);
            }else{
                contentView.setVisibility(View.GONE);
                ((ImageView)view.findViewById(R.id.holder_expland)).setImageResource(R.mipmap.ic_ex_m);
            }
        });
        (view.findViewById(R.id.holder_move)).setOnClickListener(v->{
            new ActionClickMoreEdit(getJsonBean(),context,adapter,BaseHolder.this).onClick(v);
        });
        initSelf();
        initView();
    }








    public Context getContext() {
        return context;
    }





    private TextView name;
    private ImageView icon;
    private CheckBox checkBox;
    public void setHead(String name,@DrawableRes int icon) {
        this.icon.setImageResource(icon);
        this.name.setText(name);
    }

    private JSONBean jsonBean;
    private JSONBean param;

    public BaseHolder setJsonBean(JSONBean jsonBean) {
        this.jsonBean = jsonBean;
        this.param =
                getJsonBean().getJson("param");
        return this;
    }

    public JSONBean getParam() {
        return param;
    }

    public JSONBean getJsonBean() {
        return jsonBean;
    }

    private void initSelf() {
        name = (TextView) findViewById(R.id.holder_title);
        icon = (ImageView) findViewById(R.id.holder_icon);
        checkBox = (CheckBox)findViewById(R.id.checkbox);
        name.setText(getName());
        icon.setImageResource(getIcon());
    }

    public void requestTime(boolean isShow){
        findViewById(R.id.time).setVisibility(isShow?View.VISIBLE:View.GONE);
        findViewById(R.id.time).clearFocus();
    }
    public View findViewById(int id) {
        return itemView.findViewById(id);
    }
    public void onBind(JSONBean jsonBean){
        ValotionEdittext writeTime = (ValotionEdittext) findViewById(R.id.witeTime);
        String desc = param.has("desc") && !param.getString("desc").equals("")?param.getString("desc"):"";
        boolean status = jsonBean.has("status") ? jsonBean.getBoolean("status"):true;
        ((TextView)findViewById(R.id.holder_desc)).setText(desc);
        name.setAlpha(status?1f:0.1f);
        icon.setAlpha(status?1f:0.1f);
        if (contentView!=null){
            contentView.setAlpha(status?1f:0.1f);
        }
        writeTime.bindChangeString(jsonBean,"witeTime");
        writeTime.setText(replace(jsonBean.getString("witeTime")));
        checkBox.setVisibility(adapter.isMoreSelection()?View.VISIBLE:View.GONE);
        checkBox.setChecked(jsonBean.getBoolean("selected",false));
        checkBox.setOnClickListener((v)->{
            adapter.selectedChanged(jsonBean,BaseHolder.this,checkBox.isChecked());
        });
        findViewById(R.id.holder_view).setPadding(adapter.isMoreSelection()?SizeUtils.dp2px(50):0,0,0,0);
    };
    public abstract void initView();
    public abstract int getIcon();
    public abstract String getName();


    public void setTooltips(String tooltips) {
        if (tooltips!=null){
            findViewById(R.id.holder_help).setVisibility(View.VISIBLE);
            findViewById(R.id.holder_help).setOnClickListener(v->{
                new SimpleTooltip.Builder(context)
                        .anchorView(v)
                        .text(tooltips)
                        .gravity(Gravity.BOTTOM)
                        .textColor(Color.WHITE)
                        .animated(true)
                        .transparentOverlay(false)
                        .build()
                        .show();
            });
        }else{
            findViewById(R.id.holder_help).setVisibility(View.GONE);
            findViewById(R.id.holder_help).setOnClickListener(null);
        }
    }

    public void expland() {
        if (contentView == null){
            return;
        }
        contentView.setVisibility(View.VISIBLE);
        ((ImageView)findViewById(R.id.holder_expland)).setImageResource(R.mipmap.ic_ex_s);
    }
    public void collose() {
        if (contentView == null){
            return;
        }
        contentView.setVisibility(View.GONE);
        ((ImageView)findViewById(R.id.holder_expland)).setImageResource(R.mipmap.ic_ex_m);
    }

    public void onDelete() {
    }

    public JSONBean copy() {
        return new JSONBean(getJsonBean());
    }
}
