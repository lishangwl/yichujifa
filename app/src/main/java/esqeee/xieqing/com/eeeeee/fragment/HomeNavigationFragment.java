package esqeee.xieqing.com.eeeeee.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import esqeee.xieqing.com.eeeeee.BroswerActivity;
import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.event.AddScriptEvent;
import esqeee.xieqing.com.eeeeee.ui.BaseActivity;
import esqeee.xieqing.com.eeeeee.ui.EditorActivity;


import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;


import com.raizlabs.android.dbflow.sql.language.Select;
import com.xieqing.codeutils.util.FileUtils;


import com.xieqing.codeutils.util.ToastUtils;
import com.xq.settingsview.helper.ViewHelper;
import com.yicu.yichujifa.ui.colorpicker.ColorPicker;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.xieqing.codeutils.util.ActivityUtils.startActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeNavigationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeNavigationFragment extends BaseFragment{

    private List<String> sentences;
    private View mView;


    public HomeNavigationFragment(){

    }
    public static HomeNavigationFragment newInstance() {
        HomeNavigationFragment fragment = new HomeNavigationFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public View getContentView(LayoutInflater inflater) {
        mView = inflater.inflate(R.layout.fragment_home_navigation, null, false);

        return mView;
    }

    @Override
    protected void onFragmentInit() {
        onHiddenChanged(false);
        initView(mView);
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    private void initView(View view) {
        view.findViewById(R.id.about).setOnClickListener(v -> {

        });
        view.findViewById(R.id.item_left).setOnClickListener(v -> {
            BroswerActivity.luanch(getActivity(),"http://www.baidu.com/teach/Texttutorial.html");
        });
        view.findViewById(R.id.item_right).setOnClickListener(v -> {
            BroswerActivity.luanch(getActivity(),"http://www.baidu.com/teach/videotutorial.html");
        });
        view.findViewById(R.id.add).setOnClickListener(v -> {
            EventBus.getDefault().post(new AddScriptEvent());
        });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

}
