package com.xieqing.uidesign;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.xieqing.uidesign.project.ViewLayoutInflater;
import com.xieqing.uidesign.project.exception.LayoutInflaterException;
import com.xieqing.uidesign.project.inflater.GboalViewHolder;

import org.xmlpull.v1.XmlPullParserException;

public class UiDesignActivity extends AppCompatActivity implements View.OnClickListener {

    FrameLayout absoluteLayout;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ui_design);

        absoluteLayout = findViewById(R.id.UILayout);
        absoluteLayout.setOnClickListener(this);

        toolbar = findViewById(R.id.toolbar);

        init();
        stup();
    }

    void init() {
        GboalViewHolder.getInstance().setUIDesignViews((RecyclerView) findViewById(R.id.attr_edit),
                (DrawerLayout) findViewById(R.id.drawablelayout),toolbar
                    ,absoluteLayout,findViewById(R.id.selectedView));

        getMenuInflater().inflate(R.menu.activity_ui_design,toolbar.getMenu());
    }

    void stup(){
        try {
            ViewLayoutInflater.from(this)
                    .inflate(absoluteLayout,getIntent().getStringExtra("layout"),true);
        } catch (LayoutInflaterException e) {
            e.printStackTrace();
            Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show();
        }
    }

    void clearFoucs(){
        GboalViewHolder.getInstance().clearFoucs();
    }

    @Override
    public void onClick(View v) {
        if (v == absoluteLayout){
            clearFoucs();
        }
    }

}
