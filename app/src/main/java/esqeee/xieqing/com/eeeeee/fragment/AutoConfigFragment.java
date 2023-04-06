package esqeee.xieqing.com.eeeeee.fragment;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.provider.Settings;
import com.google.android.material.textfield.TextInputEditText;
import androidx.appcompat.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.xieqing.codeutils.util.FileUtils;
import com.xieqing.codeutils.util.ImageUtils;
import com.xieqing.codeutils.util.NoticUtil;
import com.xieqing.codeutils.util.PermissionUtils;
import com.xieqing.codeutils.util.ScreenUtils;
import com.xieqing.codeutils.util.ToastUtils;
import com.xieqing.codeutils.util.Utils;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.action.Action;
import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.dialog.InputTextDialog;
import esqeee.xieqing.com.eeeeee.dialog.input.InputLine;
import esqeee.xieqing.com.eeeeee.dialog.input.InputLongTextLine;
import esqeee.xieqing.com.eeeeee.helper.ViewModel;
import esqeee.xieqing.com.eeeeee.listener.ActivityResultListener;
import esqeee.xieqing.com.eeeeee.manager.AppActivityCheckManager;
import esqeee.xieqing.com.eeeeee.manager.CheckTextManager;
import esqeee.xieqing.com.eeeeee.manager.SystemActionManager;
import esqeee.xieqing.com.eeeeee.manager.TimerTaskManager;
import esqeee.xieqing.com.eeeeee.service.AccessbilityUtils;
import esqeee.xieqing.com.eeeeee.sql.model.App;
import esqeee.xieqing.com.eeeeee.sql.model.App_Table;
import esqeee.xieqing.com.eeeeee.sql.model.AutoDo;
import esqeee.xieqing.com.eeeeee.sql.model.NotificationTextCheck;
import esqeee.xieqing.com.eeeeee.sql.model.NotificationTextCheck_Table;
import esqeee.xieqing.com.eeeeee.sql.model.System;
import esqeee.xieqing.com.eeeeee.sql.model.System_Table;
import esqeee.xieqing.com.eeeeee.sql.model.TextCheck;
import esqeee.xieqing.com.eeeeee.sql.model.TextCheck_Table;
import esqeee.xieqing.com.eeeeee.sql.model.TimeTask;
import esqeee.xieqing.com.eeeeee.ui.BaseActivity;

import static android.app.Activity.RESULT_OK;

public class AutoConfigFragment extends BaseFragment {

    @BindView(R.id.icon)
    ImageView icon;

    @BindView(R.id.item_1)
    View item_1;

    @BindView(R.id.repeat)
    TextInputEditText repeat;

    @BindView(R.id.scale_width)
    TextInputEditText scale_width;

    @BindView(R.id.scale_height)
    TextInputEditText scale_height;

    @BindView(R.id.logable)
    Switch logable;

    @BindView(R.id.scaleable)
    Switch scaleable;

    @BindView(R.id.item_2)
    View item_2;

    JSONBean jsonBean;
    Action action;

    @Override
    public View getContentView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_config,null);
    }

    @OnClick(R.id.logable)
    public void cccc(){
        action.setLog(logable.isChecked()?1:0);
    }



    @OnClick(R.id.scaleable)
    public void scaleable(){
        jsonBean.put("scale",scaleable.isChecked());
    }

    @Override
    protected void onFragmentInit() {
        logable.setChecked(!action.logAble());
        scaleable.setChecked(jsonBean.getBoolean("scale",false));
        if (!action.getIcon().equals("")){
            icon.setImageBitmap(BitmapFactory.decodeFile(action.getIcon()));
        }
        repeat.setText(String.valueOf(action.getRepeat()));
        if (jsonBean.getInt("scale_width",-1) == -1){
            jsonBean.put("scale_width", ScreenUtils.getScreenWidth());
        }
        if (jsonBean.getInt("scale_height",-1) == -1){
            jsonBean.put("scale_height", ScreenUtils.getScreenHeight());
        }
        scale_width.setText(String.valueOf(jsonBean.getInt("scale_width")));
        scale_height.setText(String.valueOf(jsonBean.getInt("scale_height")));
        scale_width.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    jsonBean.put("scale_width", Integer.parseInt(s.toString()));
                }catch (NumberFormatException e){
                    repeat.setError(e.getMessage());
                }
            }
        });
        scale_height.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    jsonBean.put("scale_height", Integer.parseInt(s.toString()));
                }catch (NumberFormatException e){
                    repeat.setError(e.getMessage());
                }
            }
        });
        repeat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    action.setRepeat(Integer.parseInt(editable.toString()));
                }catch (NumberFormatException e){
                    repeat.setError(e.getMessage());
                }
            }
        });
        item_1.setOnClickListener((v)->{
            ((BaseActivity)getContext()).addActivityResultListener(new ActivityResultListener() {
                @Override
                public void onActivityResult(int requestCode, int resultCode, Intent data) {
                    ((BaseActivity)getContext()).removeActivityResultListener(this);
                    if (requestCode == 5481 && resultCode == RESULT_OK) {
                        List<String> result = Matisse.obtainPathResult(data);
                        if (result.size()>0){
                            if (FileUtils.getFileLength(result.get(0)) > 2 * 1024 * 1024){
                                ToastUtils.showLong("大哥，图片有点大了。。。选个小于2M的吧");
                                return;
                            }
                            action.setIcon(result.get(0));
                            if (icon.getDrawable() !=null){
                                ImageUtils.recycle(((BitmapDrawable)icon.getDrawable()).getBitmap());
                            }
                            icon.setImageBitmap(BitmapFactory.decodeFile(result.get(0)));
                        }
                    }
                }
            });
            Matisse.from(((BaseActivity)getContext()))
                    .choose(MimeType.ofImage())
                    .countable(true)
                    .maxSelectable(1)
                    .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                    .thumbnailScale(0.85f)
                    .imageEngine(new GlideEngine())
                    .forResult(5481);
        });

        item_2.setOnClickListener((v)->{
            if (conditionDialog == null){
                initCondition();
            }
            conditionDialog.show();
        });
    }
    ViewModel conditionViewModel;
    List<AutoDo> actions = new ArrayList<>();
    AlertDialog conditionDialog;
    private void initCondition() {
        initAddCondition();
        actions.addAll(TimerTaskManager.getManager().queryList(action.getPath()));
        actions.addAll(CheckTextManager.getManager().queryList(action.getPath()));
        actions.addAll(CheckTextManager.getManager().queryNotificationList(action.getPath()));
        actions.addAll(AppActivityCheckManager.getManager().queryList(action.getPath()));
        actions.addAll(SystemActionManager.getManager().queryList(action.getPath()));
        conditionViewModel = ViewModel.from(getBaseActivity(),R.layout.dialog_action_config_condition,null)
            .onClick(R.id.add,v->{
                addDialog.show();
            });
        for (AutoDo autoDo : actions){
            conditionViewModel.addView(R.id.conditions,getView(autoDo));
        }
        conditionDialog = new AlertDialog.Builder(getBaseActivity())
                .setTitle("触发条件")
                .setView(conditionViewModel.getView()).create();


    }

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public long getNextDayRealTime(int hour,int min){
        Calendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.set(Calendar.HOUR_OF_DAY,hour);
        gregorianCalendar.set(Calendar.MINUTE,min);
        gregorianCalendar.set(Calendar.SECOND,0);
        gregorianCalendar.set(Calendar.MILLISECOND,0);
        long nextTime = gregorianCalendar.getTimeInMillis();
        if (nextTime < java.lang.System.currentTimeMillis()) {
            //如果时间已经过  选第二天吧
            gregorianCalendar.add(Calendar.DATE, 1);
            nextTime = gregorianCalendar.getTimeInMillis();
        }
        return nextTime;
    }


    private View getView(AutoDo task) {
        ViewModel viewModel = null;
        if (task instanceof TimeTask){
            viewModel = ViewModel.from(getBaseActivity(),R.layout.dialog_action_time,null)
                    .onClick(R.id.condition_close,v->{
                        TimerTaskManager.getManager().deleteTask(((TimeTask)task));
                        ((ViewGroup)v.getParent().getParent().getParent()).removeView((ViewGroup)v.getParent().getParent());
                    })
                    .setText(R.id.condition_text,((TimeTask)task).hour+":"+((TimeTask)task).min);
        }else if (task instanceof NotificationTextCheck){
            viewModel = ViewModel.from(getBaseActivity(),R.layout.dialog_action_notifaction,null)
                    .onClick(R.id.condition_close,v->{
                        SQLite.select().from(NotificationTextCheck.class)
                                .where(NotificationTextCheck_Table.index.eq(((NotificationTextCheck) task).index)).querySingle()
                                .delete();
                        ((ViewGroup)v.getParent().getParent().getParent()).removeView((ViewGroup)v.getParent().getParent());
                    })
                    .setText(R.id.condition_text,Arrays.toString(((NotificationTextCheck)task).keys.split(",.split.,")));
        }else if (task instanceof TextCheck){
            viewModel = ViewModel.from(getBaseActivity(),R.layout.dialog_action_text,null)
                    .onClick(R.id.condition_close,v->{
                        SQLite.select().from(TextCheck.class)
                                .where(TextCheck_Table.index.eq(((TextCheck) task).index)).querySingle()
                                .delete();
                        ((ViewGroup)v.getParent().getParent().getParent()).removeView((ViewGroup)v.getParent().getParent());
                    })
                    .setText(R.id.condition_text,Arrays.toString(((TextCheck)task).keys.split(",.split.,")));
        }else if (task instanceof App){
            viewModel = ViewModel.from(getBaseActivity(),R.layout.dialog_action_app,null)
                    .onClick(R.id.condition_close,v->{
                        SQLite.select().from(App.class)
                                .where(App_Table.index.eq(((App) task).index)).querySingle()
                                .delete();
                        ((ViewGroup)v.getParent().getParent().getParent()).removeView((ViewGroup)v.getParent().getParent());
                    })
                    .setText(R.id.condition_text,((App)task).activityName);
        }else if (task instanceof System){
            viewModel = ViewModel.from(getBaseActivity(),R.layout.dialog_action_system,null)
                    .onClick(R.id.condition_close,v->{
                        SQLite.select().from(System.class)
                                .where(System_Table.index.eq(((System) task).index))
                                .querySingle()
                                .delete();
                        ((ViewGroup)v.getParent().getParent().getParent()).removeView((ViewGroup)v.getParent().getParent());
                    })
                    .setText(R.id.condition_text,((System)task).actionName);
        }
        return viewModel.getView();
    }

    ViewModel addViewModel;
    AlertDialog addDialog;

    private void initAddCondition() {
        addViewModel = ViewModel.from(getBaseActivity(),R.layout.dialog_add_condition,null)
            .onClicks(v -> {
                switch (v.getId()){
                    case R.id.item_time:
                        processorTime();
                        break;
                    case R.id.item_notifaction:
                        processorNotification();
                        break;
                    case R.id.item_text:
                        processorScreen();
                        break;
                    case R.id.item_system:
                        processorSysytem();
                        break;
                    case R.id.item_activity:
                        processorApp();
                        break;
                }
            },R.id.item_time,R.id.item_activity,R.id.item_text,R.id.item_notifaction,R.id.item_system);
        addDialog = new AlertDialog.Builder(getBaseActivity())
                .setTitle("添加触发条件")
                .setView(addViewModel.getView()).create();
    }



    public static final String[] SYSYTEM_ACTIONS = new String[]{
            "android.intent.action.BOOT_COMPLETED"
            ,"android.intent.action.ACTION_SHUTDOWN"
            ,"android.net.wifi.WIFI_STATE_CHANGED"
            ,"android.bluetooth.adapter.action.STATE_CHANGED"
            ,"android.bluetooth.device.action.ACL_CONNECTED"
            ,"android.bluetooth.device.action.ACL_DISCONNECTED"
            ,"android.intent.action.USER_PRESENT"
            ,"android.intent.action.SCREEN_OFF"
            ,"android.intent.action.SCREEN_ON"
            ,"android.intent.action.PHONE_STATE"
            ,"android.provider.Telephony.SMS_RECEIVED"
    };
    public static final String[] SYSYTEM_ACTIONS_NAMES = new String[]{
            "开机",
            "关机",
            "WIFI状态被改变",
            "蓝牙状态改变",
            "蓝牙已连接",
            "蓝牙已断开",
            "屏幕解锁",
            "屏幕锁定",
            "屏幕亮起",
            "拨出或收到电话",
            "收到短信"
    };
    private void processorSysytem() {
        showMenuDialog("选择系统消息",SYSYTEM_ACTIONS_NAMES,(d,index)->{
            System task = new System();
            task.actionIntent = SYSYTEM_ACTIONS[index];
            task.path = action.getPath();
            task.actionName = SYSYTEM_ACTIONS_NAMES[index];
            task.insert();
            conditionViewModel.addView(R.id.conditions,getView(task));
        });
    }

    private void processorApp() {
        if (!AccessbilityUtils.isAccessibilitySettingsOn(getBaseActivity())){
            AccessbilityUtils.toSetting();
            return;
        }

        AppsFragment appsFragment = new AppsFragment();
        appsFragment.setOnAppsSelectedListener((appInfo2)-> {
            appsFragment.dismiss();
            String packName = appInfo2.getPackageName();
            String pack = appInfo2.getName();
            PackageManager pm = Utils.getApp().getPackageManager();
            try {
                ActivityInfo[] ai = pm.getPackageInfo(packName, PackageManager.GET_ACTIVITIES).activities;
                if (ai == null){
                    showDialog("提示","读取应用窗口列表失败，请检查是否有安全软件禁止或者权限未给予！");
                    return;
                }
                String[] activitys = new String[ai.length];
                for (int i = 0;i<ai.length;i++){
                    activitys[i] = ai[i].name;
                }

                showMenuDialog("选择界面",activitys,(i,index)->{
                    App task = new App();
                    task.packageName = packName;
                    task.path = action.getPath();
                    task.activityName = activitys[index];
                    task.insert();
                    conditionViewModel.addView(R.id.conditions,getView(task));
                });
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                ToastUtils.showShort(e.getMessage());
            }

        });
        appsFragment.show(((BaseActivity)getContext()).getSupportFragmentManager(),"chooseApp");
    }

    private void showDialog(String t, String s) {
        new AlertDialog.Builder(getBaseActivity())
                .setTitle(t)
                .setMessage(s)
                .setPositiveButton("确定",null)
                .create()
                .show();
    }

    private void processorScreen() {
        if (!(PermissionUtils.isUsage()||!PermissionUtils.hasUsageOption())){
            try {
                Toast.makeText(getBaseActivity(),"您需要开启“有权查看使用情况”这一权限，才能正常使用！",Toast.LENGTH_LONG).show();
                getBaseActivity().startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
            }catch (Exception e){
                e.printStackTrace();
            }
            return;
        }
        new InputTextDialog(getBaseActivity(),false).setTitle("请输入触发自动化的关键词").setMessage("当含有关键词出现，就触发自动化")
                .addInputLine(new InputLongTextLine("多个关键词\n以换行分割")).setInputTextListener((InputLine[] result)->{
            String[] screen_tags = result[0].getResult().toString().split("\n");
            if (screen_tags.length == 0){
                Toast.makeText(getBaseActivity(),"至少添加一个关键词！",Toast.LENGTH_LONG).show();
                return;
            }
            TextCheck task =  new TextCheck();
            task.keys = result[0].getResult().toString().replace("\n",",.split.,");
            task.path = action.getPath();
            task.insert();
            conditionViewModel.addView(R.id.conditions,getView(task));
        }).show();
    }

    private void processorNotification() {
        if (!NoticUtil.isNotificationEnabled2(getBaseActivity())){
            Toast.makeText(getBaseActivity(),"您需要开启“通知使用权”，才能正常使用！",Toast.LENGTH_LONG).show();
            NoticUtil.toSetting2(getBaseActivity());
            return;
        }
        new InputTextDialog(getBaseActivity(),false).setTitle("请输入触发自动化的关键词").setMessage("当含有关键词出现，就触发自动化")
                .addInputLine(new InputLongTextLine("多个关键词\n以换行分割")).setInputTextListener((InputLine[] result)->{
                    String[] screen_tags = result[0].getResult().toString().split("\n");
                    if (screen_tags.length == 0){
                        Toast.makeText(getBaseActivity(),"至少添加一个关键词！",Toast.LENGTH_LONG).show();
                        return;
                    }

                    showMenuDialog("触发时对该通知栏做什么操作",new String[]{"删除","打开","不处理"},(k,index)->{
                        NotificationTextCheck task =  new NotificationTextCheck();
                        task.keys = result[0].getResult().toString().replace("\n",",.split.,");
                        task.notiydo = index;
                        task.path = action.getPath();
                        task.insert();
                        conditionViewModel.addView(R.id.conditions,getView(task));
                    });

        }).show();
    }

    private void processorTime() {
        new TimePickerDialog(getBaseActivity(), android.R.style.Theme_DeviceDefault_Light_Dialog,new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                TimeTask task = new TimeTask();
                task.hour = hourOfDay;
                task.min = minute;
                task.path = action.getPath();
                task.insert();
                TimerTaskManager.getManager().scheduleAlarm(task);
                conditionViewModel.addView(R.id.conditions,getView(task));
            }
        },0,0,true).show();
    }

    public static AutoConfigFragment create(JSONBean jsonBean, Action action){
        AutoConfigFragment autoFragment = new AutoConfigFragment();
        autoFragment.action = action;
        autoFragment.jsonBean = jsonBean;
        return autoFragment;
    }



    private void showMenuDialog(String title,String[] items, DialogInterface.OnClickListener clickListener){
        new AlertDialog.Builder(getBaseActivity())
                .setTitle(title).setItems(items,clickListener).create().show();
    }

}
