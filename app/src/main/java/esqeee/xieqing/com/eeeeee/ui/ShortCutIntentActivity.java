package esqeee.xieqing.com.eeeeee.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.kongzue.dialog.v2.DialogSettings;
import com.kongzue.dialog.v2.Notification;

import esqeee.xieqing.com.eeeeee.action.Action;
import esqeee.xieqing.com.eeeeee.action.ActionHelper;
import esqeee.xieqing.com.eeeeee.action.ActionRunHelper;



public class ShortCutIntentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Action action = ActionHelper.from(getIntent().getStringExtra("actionID"));
        if (action!=null){
            ActionRunHelper.startAction(getApplicationContext(),action);
        }else{
            Notification.show(this, 1, "未找到该自动化").setDialogStyle(DialogSettings.STYLE_IOS);
        }
        finish();
    }
}
