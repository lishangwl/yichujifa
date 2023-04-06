package com.yicu.yichujifa.inputborad;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.inputmethodservice.InputMethodService;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class InputBraod extends InputMethodService {
    public static final String SEND_TEXT = "com.yicu.yichujifa.inputborad.InputBraod.SEND_TEXT";
    public static final String SEND_ACTION = "com.yicu.yichujifa.inputborad.InputBraod.SEND_ACTION";
    public static final String EXTRA_CONTENT = "CONTENT";
    public static final String EXTRA_ACTION = "ACTION";
    private BroadcastReceiver broadcastReceiver;
    @Override
    public void onCreate() {
        super.onCreate();
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(SEND_TEXT)){
                    String text = intent.getStringExtra(EXTRA_CONTENT);
                    try{
                        getCurrentInputConnection().commitText(text,0);
                    }catch (Exception e){}
                }else{
                    int action = intent.getIntExtra(EXTRA_ACTION,0);
                    try{
                        getCurrentInputConnection().performEditorAction(EditorInfo.IME_ACTION_DONE);
                    }catch (Exception e){}
                }

            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SEND_TEXT);
        intentFilter.addAction(SEND_ACTION);
        registerReceiver(broadcastReceiver,intentFilter);
    }

    @Override
    public View onCreateInputView() {
        TextView textView = new TextView(this);
        textView.setGravity(Gravity.CENTER);
        textView.setPadding(50,50,50,50);
        textView.setText("一触即发专用输入法");

        Button button = new Button(this);
        button.setText("切换输入法");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputBraodUtils.showPicker(InputBraod.this);
            }
        });

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setBackgroundColor(Color.GRAY);
        linearLayout.addView(textView);
        linearLayout.addView(button);
        return linearLayout;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }
}
