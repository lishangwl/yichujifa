package esqeee.xieqing.com.eeeeee.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import esqeee.xieqing.com.eeeeee.library.captrue.IntentExtras;
import esqeee.xieqing.com.eeeeee.listener.ActivityResultListener;

public class ScreenRequestActivity extends BaseActivity {

    private ActivityResultListener mCallback;
    @Override
    public int getContentLayout() {
        return 0;
    }

    @Override
    public View getContentView() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IntentExtras extras = IntentExtras.fromIntentAndRelease(getIntent());
        if (extras == null) {
            finish();
            return;
        }
        mCallback = extras.get("callback");
        if (mCallback == null) {
            finish();
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startActivityForResult(((MediaProjectionManager)getSystemService(Context.MEDIA_PROJECTION_SERVICE)).createScreenCaptureIntent(),4364);
        }else {
            finish();
            return;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mCallback!=null){
            mCallback.onActivityResult(requestCode,resultCode,data);
        }
        finish();
    }
}
