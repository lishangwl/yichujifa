package esqeee.xieqing.com.eeeeee.ui;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.xieqing.codeutils.util.SPUtils;

import java.util.ArrayList;
import java.util.List;

import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.SplashActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (isShowPosition()){
            List<Integer> integers = new ArrayList<>();
            integers.add(R.drawable.p0);
            integers.add(R.drawable.p1);
            integers.add(R.drawable.p2);
            integers.add(R.drawable.p3);
            integers.add(R.drawable.p4);
            integers.add(R.drawable.p5);
            integers.add(R.drawable.p6);
            integers.add(R.drawable.p7);
            integers.add(R.drawable.p8);
            ImageView[] views =new ImageView[integers.size()];


           ViewPager banner = findViewById(R.id.rx_banner);
            banner.setAdapter(new PagerAdapter() {
                @Override
                public int getCount() {
                    return integers.size();
                }

                @Override
                public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {

                    return view == object;
                }

                @Override
                public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                    //((BitmapDrawable)views[position].getDrawable()).getBitmap().recycle();
                    container.removeView(views[position]);
                }

                @NonNull
                @Override
                public Object instantiateItem(@NonNull ViewGroup container, int position) {
                    if (views[position] == null){
                        views[position] = new ImageView(self());
                    }
                    container.addView(views[position]);
                    views[position].setImageResource(integers.get(position));
                    return views[position];
                }
            });
            SPUtils.getInstance().put("isShowPosition",false);
        }else{
            findViewById(R.id.rx_banner).setVisibility(View.GONE);
            into();
        }
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_main;
    }


    boolean isShowPosition(){
        return SPUtils.getInstance().getBoolean("isShowPosition",true);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void into() {
        //startActivity(new Intent(MainActivity.this, UiDesignActivity.class));
        startActivity(new Intent(MainActivity.this, SplashActivity.class));
        finish();
    }

    public void into(View view) {
        into();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
