package esqeee.xieqing.com.eeeeee.ui;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.xq.settingsview.SettingView;
import com.xq.settingsview.bean.SwtichBean;

import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.bean.JSONBean;

public class ApkConfigActivity extends AppCompatActivity {

    SettingView settingView;
    JSONBean config;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apk_config);

        config = Apk.project.getConfig();
        settingView = findViewById(R.id.list);
        init();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    void init(){
        settingView.addItem(new SwtichBean("使用root模式", "将使用root进行点击，长按，滑动等手势",
                config.getBoolean("root_mode", false),
                swtichBean -> config.put("root_mode",swtichBean.isChecked())));


        settingView.addItem(new SwtichBean("自动申请权限", "首次打开app的时候，将会自动跳转到权限列表申请权限",
                config.getBoolean("auto_request_permission", true),
                swtichBean -> config.put("auto_request_permission",swtichBean.isChecked())));

        settingView.addItem(new SwtichBean("长按音量上键停止脚本", "",
                config.getBoolean("stop_by_voice_up", true),
                swtichBean -> config.put("stop_by_voice_up",swtichBean.isChecked())));

        settingView.addItem(new SwtichBean("长按音量下键停止脚本", "",
                config.getBoolean("stop_by_voice_down", true),
                swtichBean -> config.put("stop_by_voice_down",swtichBean.isChecked())));
        settingView.refresh();
    }
}
