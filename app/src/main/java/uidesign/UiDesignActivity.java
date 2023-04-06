package uidesign;

import android.os.Bundle;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.xieqing.codeutils.util.FileIOUtils;
import com.yicu.yichujifa.ui.theme.ThemeManager;

import java.io.File;

import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.ui.BaseActivity;
import uidesign.project.ViewLayoutInflater;
import uidesign.project.inflater.GboalViewHolder;

public class UiDesignActivity extends BaseActivity implements View.OnClickListener {

    FrameLayout absoluteLayout;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ui_design);

        absoluteLayout = findViewById(R.id.UILayout);
        absoluteLayout.setOnClickListener(this);

        toolbar = findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawablelayout);


        toolbar.setNavigationOnClickListener(this::onNavigationClick);
        stup();
        ThemeManager.attachTheme(toolbar,(Toolbar)findViewById(R.id.attr_toolbar),(Toolbar)findViewById(R.id.view_toolbar));
    }

    public void onNavigationClick(View v) {
        GboalViewHolder.getInstance().reFreshViewList();
        GboalViewHolder.getInstance().openDrawableLayout(Gravity.START);
    }

    DrawerLayout drawerLayout;
    void init() {
        File file = new File(getIntent().getStringExtra("layout"));
        GboalViewHolder.getInstance().setUIDesignViews((RecyclerView) findViewById(R.id.views),
                (RecyclerView) findViewById(R.id.attr_edit),drawerLayout
                ,toolbar
                    ,absoluteLayout,findViewById(R.id.selectedView),file);

        getMenuInflater().inflate(R.menu.activity_ui_design,toolbar.getMenu());
    }

    void stup(){
        try {
            init();
            File file = new File(getIntent().getStringExtra("layout"));
            ViewLayoutInflater.from(this)
                    .inflate(absoluteLayout, FileIOUtils.readFile2String(file),true);
        } catch (Exception e) {
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

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            if (drawerLayout.isDrawerOpen(Gravity.END)){
                drawerLayout.closeDrawer(Gravity.END);
                return true;
            }
            if (drawerLayout.isDrawerOpen(Gravity.START)){
                drawerLayout.closeDrawer(Gravity.START);
                return true;
            }
        }
        return super.onKeyUp(keyCode, event);
    }
}
