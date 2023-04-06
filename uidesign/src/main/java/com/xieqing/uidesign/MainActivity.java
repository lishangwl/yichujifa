package com.xieqing.uidesign;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.xieqing.uidesign.project.ViewLayoutInflater;
import com.xieqing.uidesign.project.exception.LayoutInflaterException;

public class MainActivity extends AppCompatActivity {
    FrameLayout absoluteLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        absoluteLayout = new FrameLayout(this);
        setContentView(absoluteLayout);

        try {
            ViewLayoutInflater.from(this,false)
                    .inflate(absoluteLayout,getIntent().getStringExtra("layout"),true,false);


            new AlertDialog.Builder(this)
                    .setTitle("xml")
                    .setMessage(getIntent().getStringExtra("layout"))
                    .setPositiveButton("确定",null).show();
        } catch (LayoutInflaterException e) {
            e.printStackTrace();
            new AlertDialog.Builder(this)
                    .setTitle("错误")
                    .setMessage(e.getMessage())
                    .setPositiveButton("确定",null).show();
        }
    }


}
