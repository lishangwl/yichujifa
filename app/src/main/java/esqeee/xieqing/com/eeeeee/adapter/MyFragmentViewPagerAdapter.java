package esqeee.xieqing.com.eeeeee.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import android.view.ViewGroup;

import esqeee.xieqing.com.eeeeee.fragment.BaseFragment;

public class MyFragmentViewPagerAdapter extends FragmentPagerAdapter {
    public MyFragmentViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }
    private BaseFragment[] fragments;
    public MyFragmentViewPagerAdapter(FragmentManager fm,BaseFragment[] fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }
}
