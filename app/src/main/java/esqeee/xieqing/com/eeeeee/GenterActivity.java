package esqeee.xieqing.com.eeeeee;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import esqeee.xieqing.com.eeeeee.widget.CustomPath;
import esqeee.xieqing.com.eeeeee.widget.XQPathView;

public class GenterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_genter);

        XQPathView xqPathView = findViewById(R.id.xq);
        xqPathView.setOnGestureListener(new XQPathView.OnGestureListener() {
            @Override
            public void onStart() {
                findViewById(R.id.onok).setVisibility(View.GONE);
            }

            @Override
            public void onFinished(CustomPath path, float endX, float endY) {
                findViewById(R.id.onok).setVisibility(View.VISIBLE);
            }
        });
    }
    public void no(View view) {
        XQPathView xqPathView = findViewById(R.id.xq);
        xqPathView.reset();
        findViewById(R.id.onok).setVisibility(View.GONE);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            finish();
            overridePendingTransition(0,0);
        }
        return super.onKeyDown(keyCode, event);
    }

}
