package esqeee.xieqing.com.eeeeee.fragment;

import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.xieqing.codeutils.util.ToastUtils;
import com.yicu.yichujifa.ui.theme.ThemeManager;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import esqeee.xieqing.com.eeeeee.bbs.callback.CallBack;
import esqeee.xieqing.com.eeeeee.bbs.callback.CallResult;
import esqeee.xieqing.com.eeeeee.bean.CategorySelect;
import esqeee.xieqing.com.eeeeee.bean.JSONArrayBean;
import esqeee.xieqing.com.eeeeee.manager.MessageManager;
import esqeee.xieqing.com.eeeeee.ui.UserActivity;
import esqeee.xieqing.com.eeeeee.widget.viewPager.LazyFragmentPagerAdapter;
import esqeee.xieqing.com.eeeeee.widget.viewPager.LazyViewPager;

public class BBS extends BaseFragment implements SearchView.OnQueryTextListener {

    LazyViewPager viewPager;
    TabLayout tabLayout;
    JSONArrayBean cates;
    BaseFragment[] fragments;
    @Override
    public View getContentView(LayoutInflater inflater) {
        tabLayout = new TabLayout(inflater.getContext());
        viewPager = new LazyViewPager(inflater.getContext());

        viewPager.setId(100);
        viewPager.setInitLazyItemOffset(0.5f);
        tabLayout.setBackgroundColor(Color.WHITE);
        LinearLayout linearLayout = new LinearLayout(inflater.getContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.addView(tabLayout);
        linearLayout.addView(viewPager);
        return linearLayout;
    }

    @Override
    public void onChangTheme() {
        super.onChangTheme();

        ThemeManager.attachTheme(tabLayout);
    }
    @Override
    protected void onFragmentInit() {
        showLoadding();
        esqeee.xieqing.com.eeeeee.bbs.BBS.getCates(new CallBack(){
            @Override
            public void callBack(CallResult result) {
                if (result.getCode() == 0){
                    cates = result.getArray("data");
                    fragments = new BaseFragment[cates.length()];
                    for (int i = 0;i<fragments.length;i++){
                        fragments[i] = ((AppbbsFragment) Fragment.instantiate(getActivity(),AppbbsFragment.class.getName()))
                                .setTid(cates.getJson(i).getInt("id"),cates.getJson(i).getString("name"))
                                .setBaseActivity(getBaseActivity());
                    }
                    stupViewPager();
                }else{
                    ToastUtils.showLong("error:"+result.getMessage());
                }
                disLoadding();
            }
        });
    }

    private void stupViewPager() {
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                MessageManager.getManager().readNew();
            }
        });
        viewPager.setAdapter(new LazyFragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            protected Fragment getItem(ViewGroup container, int position) {
                return fragments[position];
            }

            @Override
            public int getCount() {
                return fragments.length;
            }
        });
        stupTab();
    }

    private void stupTab() {
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(0);
        for (int i = 0;i<cates.length();i++){
            tabLayout.getTabAt(i).setText(cates.getJson(i).getString("name"));
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void cagtory(CategorySelect select){
        for (int i = 0;i<cates.length();i++){
            if (select.getTid() == cates.getJson(i).getInt("id")){
                viewPager.setCurrentItem(i);
                break;
            }
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (!TextUtils.isEmpty(query)){
            UserActivity.luanch3(getBaseActivity(),query);
        }
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
