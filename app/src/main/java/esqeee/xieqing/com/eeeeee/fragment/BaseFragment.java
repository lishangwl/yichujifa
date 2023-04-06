package esqeee.xieqing.com.eeeeee.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.xieqing.codeutils.util.SizeUtils;
import com.xieqing.codeutils.util.ThreadUtils;
import com.yicu.yichujifa.ui.theme.ThemeManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import esqeee.xieqing.com.eeeeee.ui.BaseActivity;
import esqeee.xieqing.com.eeeeee.widget.FDialog;

public abstract class BaseFragment extends DialogFragment {
    public View root;
    private Unbinder unbinder;
    public  void setArguments(@Nullable Bundle args){
        super.setArguments(args);
    }
    public Drawable getDrawable(int id){
        return getActivity().getResources().getDrawable(id);
    }

    private View view;
    private ProgressBar progressBar;

    private Map<String,Object> prepotey = new HashMap<>();

    public void setTag(String key,Object tag){
        if (tag == null){
            prepotey.remove(key);
        }else{
            prepotey.put(key,tag);
        }
        //System.gc();
    }

    private ProgressDialog progressDialog;
    public void showProgress(String message){
        showProgress("加载中",message);
    }
    public void showProgress(String title,String message){
        progressDialog = null;
        ThreadUtils.runOnUiThread(()->{
            progressDialog = ProgressDialog.show(getContext(),title,message);
        });
    }

    public void disProgress(){
        ThreadUtils.runOnUiThread(()->{
            progressDialog.dismiss();
        });
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        progressBar = new ProgressBar(inflater.getContext());
        progressBar.setVisibility(View.GONE);
        root = getContentView(inflater);
        unbinder =
                ButterKnife.bind(this,root);
        onFragmentInit();

        FrameLayout frameLayout = new FrameLayout(inflater.getContext());
        frameLayout.addView(progressBar);
        frameLayout.addView(root);
        view = frameLayout;

        onChangTheme();
        return view;
    }

    @Nullable
    @Override
    public View getView() {
        return view;
    }

    public View getView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        if (view == null){
            onCreateView(inflater,container,savedInstanceState);
        }
        return view;
    }

    public void showLoadding(){
        progressBar.setVisibility(View.VISIBLE);
        root.setVisibility(View.GONE);
    }
    public void disLoadding(){
        progressBar.setVisibility(View.GONE);
        root.setVisibility(View.VISIBLE);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("BaseFragment["+getClass().getSimpleName()+"]","onDestroyView ");
        if (unbinder!=null){
            unbinder.unbind();
        }
        if (((ViewGroup)root.getParent())!=null){
            ((ViewGroup)root.getParent()).removeView(root);
        }

    }

    private BaseActivity baseActivity;

    public BaseFragment setBaseActivity(BaseActivity baseActivity) {
        this.baseActivity = baseActivity;
        return this;
    }

    public BaseActivity getBaseActivity() {
        BaseActivity activity =(BaseActivity) getActivity();
        //Log.d("onCreateViewHolder["+getClass().getSimpleName()+"]","context "+activity);
        return activity!=null?activity : baseActivity;
    }

    private Context context;

    @Nullable
    @Override
    public Context getContext() {
        if (context == null){
            context = super.getContext();
        }
        return context!=null?context:getBaseActivity();
    }

    public void show(FDialog fDialog){
        //onCreate(null);
        context = fDialog.getContext();
        View view = getContentView(LayoutInflater.from(fDialog.getContext()));

        fDialog.addView(view)
                .setDismissListener(d->{
                    onDestroy();
                })
                .setMinimumHeight(SizeUtils.dp2px(350))
                .fillContent(true)
                .show();
        ButterKnife.bind(this,view);
        onFragmentInit();
        onChangTheme();
        onResume();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("BaseFragment["+getClass().getSimpleName()+"]","onResume ");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("BaseFragment["+getClass().getSimpleName()+"]","onCreate "+savedInstanceState);

        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("BaseFragment["+getClass().getSimpleName()+"]","onActivityCreated "+savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d("BaseFragment["+getClass().getSimpleName()+"]","onAttach ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void changedTheme(ThemeManager.Theme t){
        onChangTheme();
    }


    public void onChangTheme(){}
    @Override
    public void onPause() {
        super.onPause();
        Log.d("BaseFragment["+getClass().getSimpleName()+"]","onPause ");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("BaseFragment["+getClass().getSimpleName()+"]","onStop ");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("BaseFragment["+getClass().getSimpleName()+"]","onSaveInstanceState ");
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.d("BaseFragment["+getClass().getSimpleName()+"]","onViewStateRestored "+savedInstanceState);
    }

    /*
    *   获取View 视图
    * */
    public abstract View getContentView(LayoutInflater inflater);

    /*
     *   Fragment初始化
     * */
    protected abstract void onFragmentInit();

    public boolean disptachMenuSelected(int itemId) {
        return false;
    }
}
