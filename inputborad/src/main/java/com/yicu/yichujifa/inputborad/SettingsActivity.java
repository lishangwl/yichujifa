package com.yicu.yichujifa.inputborad;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {

    private Button btn_active;
    private Button btn_choose;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        setContentView(linearLayout);

        btn_active = new Button(this);
        btn_choose = new Button(this);
        linearLayout.addView(btn_active);
        linearLayout.addView(btn_choose);
        TextView textView = new TextView(this);
        textView.setText("插件说明：本插件开启后软件占用原先输入法。好处是无障碍服务几乎不会被系统杀死，也不会掉，除此之外，软件也不会被轻易杀死，不会被系统回收。因此，在一些特殊场景，开启本插件后效果是非常好的。除此之外，开启本插件后使用【发送文本到焦点编辑框】是不需要ROOT的。");
        btn_choose.setText("选择输入法");
        linearLayout.addView(textView);

        btn_active.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputBraodUtils.toSettings(SettingsActivity.this);
            }
        });

        btn_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputBraodUtils.showPicker(SettingsActivity.this);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (InputBraodUtils.isVliad(this)){
            btn_active.setText("已激活");
            btn_active.setEnabled(false);
        }else {
            btn_active.setText("激活输入法");
            btn_active.setEnabled(true);
        }
    }
}
