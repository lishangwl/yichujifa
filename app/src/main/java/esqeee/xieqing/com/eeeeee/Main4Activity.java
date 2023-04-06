package esqeee.xieqing.com.eeeeee;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.xieqing.codeutils.util.AppUtils;
import com.xieqing.codeutils.util.PermissionUtils;
import com.xieqing.codeutils.util.SPUtils;
import com.xieqing.codeutils.util.ToastUtils;
import com.yicu.yichujifa.ui.theme.ThemeManager;

import butterknife.BindView;
import esqeee.xieqing.com.eeeeee.action.Action;
import esqeee.xieqing.com.eeeeee.action.ActionHelper;
import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.dialog.SelectActionDialog;
import esqeee.xieqing.com.eeeeee.doAction.DoActionBean;
import esqeee.xieqing.com.eeeeee.ui.BaseActivity;


public class Main4Activity extends BaseActivity implements View.OnClickListener{

    @BindView(R.id.item_1)
    View x;
    @BindView(R.id.item_2)
    View y;
    @BindView(R.id.item_3)
    View z;



    @BindView(R.id.seekBar)
    SeekBar seekBar;

    @Override
    public int getContentLayout() {
        return R.layout.activity_main4;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Switch switchId2 = findViewById(R.id.switchid2);
        switchId2.setChecked(SPUtils.getInstance().getBoolean("isOpenWDDong",true));
        switchId2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SPUtils.getInstance().put("isOpenWDDong",switchId2.isChecked());
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b){
                    SPUtils.getInstance().put("motion_thresh",i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        ThemeManager.attachTheme(findViewById(R.id.title1),seekBar,switchId2);
    }

    private SelectActionDialog selectActionDialog;
    String saveKey = "";
    @Override
    public void onClick(View view) {
        if (!PermissionUtils.getAppOps(this)){
            ToastUtils.showShort("先给软件悬浮窗权限");
            PermissionUtils.openAps(this);
            return;
        }
        switch (view.getId()){
            case R.id.item_1:
                saveKey = "x";
                break;
            case R.id.item_2:
                saveKey = "y";
                break;
            case R.id.item_3:
                saveKey = "z";
                break;
        }
        if (selectActionDialog == null){
            selectActionDialog = new SelectActionDialog(this, (JSONBean json)->{
                SPUtils editor = SPUtils.getInstance();
                editor.put(saveKey+"_wd",json.toString());
                resumeItem();
            }, false);
        }
        selectActionDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void onResume() {
        super.onResume();
        resumeItem();
        seekBar.setProgress(SPUtils.getInstance().getInt("motion_thresh",10));
    }

    private void resumeItem() {
        resumeItem("x",findViewById(R.id.item_1));
        resumeItem("y",findViewById(R.id.item_2));
        resumeItem("z",findViewById(R.id.item_3));
    }
    private void resumeItem(String key,View item) {
        JSONBean bean = new JSONBean(SPUtils.getInstance().getString(key+"_wd"));
        JSONBean param = bean.getJson("param");
        TextView textView = ((TextView)((ViewGroup)item).getChildAt(1));
        ImageView imageView = ((ImageView)((ViewGroup)item).getChildAt(0));

        textView.setTextColor(Color.BLACK);
        imageView.setPadding(0,0,0,0);
        imageView.setBackground(null);
        int type = bean.getInt("actionType",-1);

        if (type == -1){
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            imageView.setImageTintList(null);
        }
        if (type == DoActionBean.EXCE_ACTION.getActionType()){
            Action action = ActionHelper.from(param.getString("actionId"));
            if (action != null){
                imageView.setImageDrawable(action.getDrawable());
                textView.setText(action.getTitle());
            }
        }else if (type == 12){
            AppUtils.AppInfo appInfo = AppUtils.getAppInfo(param.getString("packName"));
            if(appInfo!=null){
                imageView.setImageDrawable(appInfo.getIcon());
                textView.setText("打开\t"+appInfo.getName());
            }
        }else{
            DoActionBean doActionBean = DoActionBean.getBeanFromType(type);
            imageView.setImageDrawable(doActionBean.getDrawable());
            textView.setText(doActionBean.getActionName());
        }
    }
}
