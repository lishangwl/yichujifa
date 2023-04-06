package esqeee.xieqing.com.eeeeee;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;

import com.google.android.material.tabs.TabLayout;
import androidx.appcompat.app.AlertDialog;
import android.view.View;

import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.xieqing.codeutils.util.ScreenUtils;
import com.yicu.yichujifa.ui.theme.ThemeManager;

import java.io.File;

import esqeee.xieqing.com.eeeeee.ui.CircularAnimUtil;

public class Main6Activity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {
    private int[] qrcodes = new int[]{R.drawable.ic_qrcode_wx,R.drawable.ic_qrcode_qq,R.drawable.ic_qrcode_alipay};
    private int[] colors = new int[]{Color.parseColor("#22AA39"),Color.parseColor("#019FE9"),Color.parseColor("#1CBBF5")};
    private String[] packages = new String[]{"com.tencent.mm","com.tencent.mobileqq","com.eg.android.AlipayGphone"};
    private String[] packagesName = new String[]{"微信","QQ","支付宝"};
    private View root;
    private TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        root = View.inflate(this,R.layout.activity_main6,null);
        setContentView(root);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tabLayout = findViewById(R.id.tablayout);
        tabLayout.addOnTabSelectedListener(this);

        onTabSelected(tabLayout.getTabAt(0));
    }

    private File save;
    private String getSaveShared() {
        if (save!=null){
            return save.getAbsolutePath();
        }
        File Share = new File(Environment.getExternalStorageDirectory().getPath()+"/YiChuJiFaShare");
        if (!Share.isDirectory()){
            Share.mkdir();
        }
        save = new File(Share,packages[tabLayout.getSelectedTabPosition()]+".png");
        //FileIOUtils.writeFileFromBytesByChannel(save.getAbsolutePath(), FormatCastUtils.getBytesByBitmap(ViewUtils.getCacheBitmapFromView(findViewById(R.id.qrcode))),true);
        return save.getAbsolutePath();
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        //((ImageView)findViewById(R.id.qrcode)).setImageDrawable(getResources().getDrawable(qrcodes[tab.getPosition()]));
        View circle = findViewById(R.id.animation);
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
            root.setBackgroundColor(colors[tab.getPosition()]);
            return;
        }

        circle.setVisibility(View.VISIBLE);
        circle.setBackgroundColor(colors[tab.getPosition()]);
        CircularAnimUtil.show(circle, 0, 200, new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                root.setBackgroundColor(colors[tab.getPosition()]);
                circle.setVisibility(View.GONE);
            }
        },ScreenUtils.getScreenWidth()/3 * tab.getPosition() + ScreenUtils.getScreenWidth()/6,(int)tabLayout.getY() + tabLayout.getHeight()/2);

        ThemeManager.setNavigationBarColor(this,colors[tab.getPosition()]);
        ThemeManager.setStatusBarColor(this,colors[tab.getPosition()]);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }


}
