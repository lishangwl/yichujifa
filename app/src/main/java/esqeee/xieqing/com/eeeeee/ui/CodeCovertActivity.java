package esqeee.xieqing.com.eeeeee.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;


import com.xieqing.codeutils.util.SizeUtils;

import butterknife.BindView;
import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.widget.XQLanguage;
import tiiehenry.code.view.CodeEditor;


public class CodeCovertActivity extends BaseActivity {
    @BindView(R.id.code)
    CodeEditor code;
    @Override
    public int getContentLayout() {
        return R.layout.activity_code_covert;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        supportToolBarWithBack(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "复制成功", Snackbar.LENGTH_LONG).show();
            }
        });
        code.setTextSize(SizeUtils.dp2px(14));
        code.setLanguage(XQLanguage.getInstance());
        code.setText(getIntent().getStringExtra("code"));

    }

    public static void luanch(Context context, String code){
        context.startActivity(new Intent(context,CodeCovertActivity.class).putExtra("code",code));
    }

}
