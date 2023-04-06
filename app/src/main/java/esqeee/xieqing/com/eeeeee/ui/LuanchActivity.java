package esqeee.xieqing.com.eeeeee.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.xieqing.codeutils.util.SPUtils;

import esqeee.xieqing.com.eeeeee.HomeActivity;

public class LuanchActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isTaskRoot()) {
            finish();
            return;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (SPUtils.getInstance().getBoolean("isShowPosition",true)){
            startActivity(new Intent(this, MainActivity.class));
        }else{
            //startActivity(new Intent(this, SplashActivity.class));
            startActivity(new Intent(this, HomeActivity.class));
        }
        finish();
    }
}
