package esqeee.xieqing.com.eeeeee.view;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.xieqing.codeutils.util.AnimationUtils;
import com.xieqing.codeutils.util.NoticUtil;
import com.xieqing.codeutils.util.PermissionUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import esqeee.xieqing.com.eeeeee.AddActivity;
import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.action.Action;
import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.dialog.InputTextDialog;
import esqeee.xieqing.com.eeeeee.dialog.input.InputLine;
import esqeee.xieqing.com.eeeeee.dialog.input.InputTextLine;
import esqeee.xieqing.com.eeeeee.dialog.input.InputTextListener;
import esqeee.xieqing.com.eeeeee.widget.ExpandableLayout;
import esqeee.xieqing.com.eeeeee.widget.FixedGroup;
import esqeee.xieqing.com.eeeeee.widget.UMExpandLayout;

import static com.umeng.analytics.a.b;
import static com.umeng.analytics.a.c;

public class AddChooseTypeView implements View.OnClickListener {
    private ExpandableLayout view;
    private TextView param;
    private Context context;
    private List<RadioButton> radioButtons = new ArrayList<>();
    private Action a;
    private TimePickerDialog timePicker;
    private JSONBean action;

    private View.OnClickListener onNotiyCheck = (v)->{
          ((CompoundButton)view.findViewById(R.id.radio_5)).setChecked(false);
          ((CompoundButton)view.findViewById(R.id.radio_6)).setChecked(false);
          ((CompoundButton)view.findViewById(R.id.radio_7)).setChecked(false);

          CompoundButton compoundButton = (CompoundButton) v;
          compoundButton.setChecked(true);
          switch (v.getId()){
              case R.id.radio_5:
                  action.put("notiy_do",0);
                  break;
              case R.id.radio_6:
                  action.put("notiy_do",1);
                  break;
              case R.id.radio_7:
                  action.put("notiy_do",2);
                  break;
          }
    };

    public AddChooseTypeView(Context context, View view, View param, Action a, JSONBean action){
        this.view = (ExpandableLayout) view;
        this.view.show();
        this.context = context;
        this.a = a;
        this.param = (TextView) param;
        this.action = action;
        initView();
    }

    private LinearLayout tag_screen;
    private List<TagView> screen_tags = new ArrayList<>();
    private LinearLayout tag_notiy;
    private List<TagView> notiy_tags = new ArrayList<>();
    private void initView(){

        tag_screen = view.findViewById(R.id.tag_screen);
        tag_notiy = view.findViewById(R.id.tag_notiy);

        radioButtons.add(view.findViewById(R.id.radio_1));
        radioButtons.add(view.findViewById(R.id.radio_2));
        radioButtons.add(view.findViewById(R.id.radio_4));
        radioButtons.add(view.findViewById(R.id.radio_3));
        for (RadioButton radioButton : radioButtons){
            radioButton.setOnClickListener(this);
        }

        resetChecked();
        radioButtons.get(a.getType()).setChecked(true);
        switch (a.getType()){
            case 1:
                param.setText(radioButtons.get(a.getType()).getText()+"["+action.getInt("hour")+":"+action.getInt("min")+"]");
                break;
            case 3:
                ((ExpandableLayout)this.view.findViewById(R.id.screen_expend)).show();
                param.setText(radioButtons.get(a.getType()).getText());
                String[] tags = action.has("text")?action.getString("text").split(",.split.,"):new String[0];
                for (String tag:tags){
                    if (tag.trim().length()!=0){
                        TagView tagView = new TagView(context,tag);
                        screen_tags.add(tagView);
                        tagView.setOnDeleteClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                screen_tags.remove(tagView);
                                tag_screen.removeView(tagView.getView());
                            }
                        });
                        tag_screen.addView(tagView.getView());
                    }
                }
                break;
            case 2:
                ((ExpandableLayout)this.view.findViewById(R.id.notiy_expend)).show();
                param.setText(radioButtons.get(a.getType()).getText());
                String[] tags2 = action.has("text")?action.getString("text").split(",.split.,"):new String[0];
                for (String tag:tags2){
                    if (tag.trim().length()!=0){
                        TagView tagView = new TagView(context,tag);
                        notiy_tags.add(tagView);
                        tagView.setOnDeleteClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                notiy_tags.remove(tagView);
                                tag_notiy.removeView(tagView.getView());
                            }
                        });
                        tag_notiy.addView(tagView.getView());
                    }
                }

                break;
            default:
                param.setText(radioButtons.get(a.getType()).getText());
                break;
        }
        view.findViewById(R.id.add_key_notiy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new InputTextDialog(context,false).setTitle("请输入触发自动化的文字").setMessage("当含有该文字出现，就触发自动化")
                        .addInputLine(new InputTextLine()).setInputTextListener(new InputTextListener() {
                    @Override
                    public void onConfirm(InputLine[] result) throws Exception{
                        TagView tagView = new TagView(context,result[0].getResult().toString());
                        notiy_tags.add(tagView);
                        tagView.setOnDeleteClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                notiy_tags.remove(tagView);
                                tag_notiy.removeView(tagView.getView());
                                freshtag(notiy_tags);
                            }
                        });
                        tag_notiy.addView(tagView.getView());
                        freshtag(notiy_tags);
                    }
                }).show();
            }
        });
        view.findViewById(R.id.add_key_screen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new InputTextDialog(context,false).setTitle("请输入触发自动化的文字").setMessage("当含有该文字出现，就触发自动化")
                        .addInputLine(new InputTextLine()).setInputTextListener(new InputTextListener() {
                    @Override
                    public void onConfirm(InputLine[] result) throws Exception{
                        TagView tagView = new TagView(context,result[0].getResult().toString());
                        screen_tags.add(tagView);
                        tagView.setOnDeleteClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                screen_tags.remove(tagView);
                                tag_screen.removeView(tagView.getView());
                                freshtag(screen_tags);
                            }
                        });
                        tag_screen.addView(tagView.getView());
                        freshtag(screen_tags);
                    }
                }).show();
            }
        });
        view.findViewById(R.id.radio_5).setOnClickListener(onNotiyCheck);
        view.findViewById(R.id.radio_6).setOnClickListener(onNotiyCheck);
        view.findViewById(R.id.radio_7).setOnClickListener(onNotiyCheck);

        int doNotiy = action.has("notiy_do")?action.getInt("notiy_do"):2;
        switch (doNotiy){
            case 0:
                ((CompoundButton)view.findViewById(R.id.radio_5)).setChecked(true);
                break;
            case 1:
                ((CompoundButton)view.findViewById(R.id.radio_6)).setChecked(true);
                break;
            case 2:
                ((CompoundButton)view.findViewById(R.id.radio_7)).setChecked(true);
                break;
        }
    }

    private void freshtag(List<TagView> tags) {
        StringBuilder stringBuilder = new StringBuilder();
        for (TagView tagView:tags){
            stringBuilder.append(tagView.getText()).append(",.split.,");
        }
        action.put("text",stringBuilder.toString());
    }

    public void showTimePicker(){
        if (timePicker == null){
            timePicker = new TimePickerDialog(context, android.R.style.Theme_DeviceDefault_Light_Dialog,new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    action.put("hour",hourOfDay);
                    action.put("min",minute);
                    a.setType(1);
                    param.setText(radioButtons.get(1).getText()+"["+hourOfDay+":"+minute+"]");
                    resetChecked();
                    radioButtons.get(1).setChecked(true);
                }
            },0,0,true);
        }
        timePicker.show();
    }

    private void resetChecked() {
        for (RadioButton radioButton : radioButtons){
            radioButton.setChecked(false);
        }
    }

    @Override
    public void onClick(View view) {
        RadioButton compoundButton = (RadioButton) view;
        resetChecked();
        a.setType(0);
        param.setText(radioButtons.get(0).getText());
        radioButtons.get(0).setChecked(true);
        switch (compoundButton.getId()){
            case R.id.radio_1:
                break;
            case R.id.radio_2:
                showTimePicker();
                break;
            case R.id.radio_3:
                if (!(PermissionUtils.isUsage()||!PermissionUtils.hasUsageOption())){
                    try {
                        Toast.makeText(context,"您需要开启“有权查看使用情况”这一权限，才能正常使用！",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                        context.startActivity(intent);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    return;
                }
                resetChecked();
                param.setText(compoundButton.getText());
                compoundButton.setChecked(true);
                a.setType(3);
                break;
            case R.id.radio_4:
                if (!NoticUtil.isNotificationEnabled2(context)){
                    Toast.makeText(context,"您需要开启“通知使用权”，才能正常使用！",Toast.LENGTH_LONG).show();
                    NoticUtil.toSetting2(context);
                    return;
                }
                resetChecked();
                param.setText(compoundButton.getText());
                compoundButton.setChecked(true);
                a.setType(2);
                break;
        }

        if (compoundButton.getId() == R.id.radio_4){
            ((ExpandableLayout)this.view.findViewById(R.id.notiy_expend)).show();
        }else{
            ((ExpandableLayout)this.view.findViewById(R.id.notiy_expend)).hide();
        }
        if (compoundButton.getId() == R.id.radio_3){
            ((ExpandableLayout)this.view.findViewById(R.id.screen_expend)).show();
        }else{
            ((ExpandableLayout)this.view.findViewById(R.id.screen_expend)).hide();
        }
    }



    class TagView{
        private String text;
        private View view;

        public String getText() {
            return text;
        }

        public View getView() {
            return view;
        }

        public TagView(Context context, String text, View.OnClickListener delete){
            this.text = text;
            view = View.inflate(context,R.layout.tag_view,null);
            ((TextView)view.findViewById(R.id.tag)).setText(text);
            if (delete!=null){
                view.findViewById(R.id.tag_delete).setOnClickListener(delete);
            }
        }

        public void setOnDeleteClickListener(View.OnClickListener delete) {
            if (delete!=null){
                view.findViewById(R.id.tag_delete).setOnClickListener(delete);
            }
        }

        public TagView(Context context, String text){
            this(context,text,null);
        }
    }
}
