package esqeee.xieqing.com.eeeeee.ui;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import esqeee.xieqing.com.eeeeee.BroswerActivity;
import esqeee.xieqing.com.eeeeee.BuildConfig;
import esqeee.xieqing.com.eeeeee.R;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ((TextView)findViewById(R.id.version)).setText("版本号 V"+ BuildConfig.VERSION_NAME+" Release");
    }

    public void xieyi(View v){
        BroswerActivity.luanchResource(this,"app_use_agree.html");
    }
    public void zhengce(View v){
        BroswerActivity.luanchResource(this,"app_use_xy.html");
    }
}
