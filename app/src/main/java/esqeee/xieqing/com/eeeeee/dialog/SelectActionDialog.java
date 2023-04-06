package esqeee.xieqing.com.eeeeee.dialog;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.xieqing.codeutils.util.AppUtils;
import com.xieqing.codeutils.util.FileUtils;
import com.yicu.yichujifa.ui.theme.ThemeManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.dialog.input.InputLine;
import esqeee.xieqing.com.eeeeee.dialog.input.InputNumberLine;
import esqeee.xieqing.com.eeeeee.doAction.DoActionBean;
import esqeee.xieqing.com.eeeeee.fragment.ActionsFragment;
import esqeee.xieqing.com.eeeeee.fragment.AppsFragment;
import esqeee.xieqing.com.eeeeee.fragment.BaseFragment;
import esqeee.xieqing.com.eeeeee.fragment.CommandFragment;
import esqeee.xieqing.com.eeeeee.fragment.FastIntentFragment;
import esqeee.xieqing.com.eeeeee.fragment.FastSwitchFragment;
import esqeee.xieqing.com.eeeeee.fragment.PramaerFragment;
import esqeee.xieqing.com.eeeeee.fragment.VirtualKeyFragment;
import esqeee.xieqing.com.eeeeee.listener.OnActionAddListener;
import esqeee.xieqing.com.eeeeee.ui.BaseActivity;

public class SelectActionDialog extends BaseDialog {
    private BaseActivity context;
    public int x,y;
    public Bitmap bitmap;
    public OnActionAddListener actionAddListener;

    public void setActionAddListener(OnActionAddListener actionAddListener) {
        this.actionAddListener = actionAddListener;
    }

    public static SelectActionDialog current;
    public SelectActionDialog(BaseActivity context,OnActionAddListener actionAddListener) {
        super(context);
        current = this;
        this.context = context;
        this.actionAddListener = actionAddListener;


        root = View.inflate(context,R.layout.dialog_select,null);
        setContentView(root);

        initView();
    }
    private boolean chooseAll = true;
    public SelectActionDialog(BaseActivity context,OnActionAddListener actionAddListener,boolean chooseAll) {
        super(context);
        current = this;
        this.context = context;
        this.actionAddListener = actionAddListener;
        this.chooseAll = chooseAll;

        root = View.inflate(context,R.layout.dialog_select,null);
        setContentView(root);

        initView();
    }
    public SelectActionDialog setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
        return this;
    }

    public SelectActionDialog setX(int x) {
        this.x = x;
        return this;
    }

    public SelectActionDialog setY(int y) {
        this.y = y;
        return this;
    }

    public SelectActionDialog(BaseActivity context, int x, int y, Bitmap bitmap, OnActionAddListener actionAddListener) {
        super(context);
        current = this;
        this.x=x;
        this.y=y;
        this.context = context;
        this.bitmap = bitmap;
        this.actionAddListener = actionAddListener;


        root = View.inflate(context,R.layout.dialog_select,null);
        setContentView(root);

        initView();
    }

    public BaseActivity getActivity() {
        return context;
    }

    private View root;
    private List<BaseFragment> fragments = new ArrayList<>();

    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.tablayout)
     TabLayout tabLayout;

    @BindView(R.id.toolbar)
     Toolbar toolbar;



    private void initView() {
        ButterKnife.bind(this,root);
        ThemeManager.attachTheme(tabLayout);
        ThemeManager.attachTheme(toolbar);
        initFragments();
        Log.e("fffffff",fragments.size()+"");
        toolbar.setNavigationOnClickListener(view -> {
            dismiss();
        });
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return fragments.size();
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return view == object;
            }

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                View view = fragments.get(position).getView(getActivity().getLayoutInflater(),container,null);
                container.addView(view);
                return view;
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                View view = fragments.get(position).getView(getActivity().getLayoutInflater(),container,null);
                container.removeView(view);
            }
        });
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setText("\t动作命令\t");
        tabLayout.getTabAt(1).setText("\t自动化\t");
        tabLayout.getTabAt(2).setText("\t程序命令\t");
        tabLayout.getTabAt(3).setText("\t虚拟按键\t");
        tabLayout.getTabAt(4).setText("\t应用程序\t");
        tabLayout.getTabAt(5).setText("\t快捷开关\t");
        tabLayout.getTabAt(6).setText("\t快捷方式\t");
    }

    @Override
    public void show() {
        current = this;
        super.show();
    }

    private AppsFragment appsFragment;
    private VirtualKeyFragment virtualKeyFragment;
    private FastSwitchFragment fastSwitchFragment;
    private ActionsFragment actionsFragment;
    private CommandFragment commandFragment;
    private FastIntentFragment fastIntentFragment;
    private PramaerFragment pramaerFragment;
    private void initFragments() {
        //动作命令
        commandFragment = (CommandFragment) new CommandFragment().setBaseActivity(context);
        Bundle bundle = new Bundle();
        bundle.putInt("x",x);
        bundle.putInt("y",y);
        commandFragment.setArguments(bundle);
        commandFragment.setSelectedListener(this::selectCommand);

        //虚拟按键
        virtualKeyFragment = (VirtualKeyFragment) new VirtualKeyFragment().setBaseActivity(context);
        virtualKeyFragment.setSelectedListener(this::selectVirtualKey);

        //应用程序
        appsFragment = (AppsFragment) new AppsFragment().setBaseActivity(context);
        appsFragment.setOnAppsSelectedListener(this::selectApps);


        //快捷开关
        fastSwitchFragment = (FastSwitchFragment) new FastSwitchFragment().setBaseActivity(context);
        fastSwitchFragment.setSelectedListener(this::selectVirtualKey);

        //自动化
        actionsFragment = (ActionsFragment) new ActionsFragment().setBaseActivity(context);
        actionsFragment.setOnActionSelectedListener(this::selectAction);

        //快捷方式
        fastIntentFragment = (FastIntentFragment) new FastIntentFragment().setBaseActivity(context);
        fastIntentFragment.setSelectedListener(this::selectFastIntent);


        pramaerFragment = (PramaerFragment) new PramaerFragment().setBaseActivity(context);
        pramaerFragment.setOnAppsSelectedListener(this::selectPramar);

        fragments.add(commandFragment);
        fragments.add(actionsFragment);
        fragments.add(pramaerFragment);
        fragments.add(virtualKeyFragment);
        fragments.add(appsFragment);
        fragments.add(fastSwitchFragment);
        fragments.add(fastIntentFragment);
    }

    private void selectFastIntent(DoActionBean bean,String param){
        dismiss();
        actionAddListener.addAction(JSONBean.optInt("actionType",bean.getActionType())
                .put("witeTime",1000)
                .put("param",JSONBean.optString("text",param)));
    }

    /*
    *   选中应用程序
    * */
    private void selectApps(AppUtils.AppInfo appInfo){
        if (!chooseAll){
            dismiss();
            actionAddListener.addAction(JSONBean.optInt("actionType",12)
                    .put("param",JSONBean.optString("packName",appInfo.getPackageName()))
                    .put("appName",appInfo.getName()));
            return;
        }
        String message = "单位毫秒,1000毫秒 = 1秒，时间太短(<50)很可能出现无法点击";
        InputTextDialog.getDialog(context).setTitle("输入等待时间").setMessage(message)
                .addInputLine(new InputNumberLine(1000)).setInputTextListener((InputLine[] result)->{
            dismiss();
            actionAddListener.addAction(JSONBean.optInt("actionType",12)
                    .put("witeTime",(int)result[0].getResult())
                    .put("param",JSONBean.optString("packName",appInfo.getPackageName()))
                    .put("appName",appInfo.getName()));
        }).show();

    }


    /*
     *   选中操作命令
     * */
    private void selectCommand(JSONBean jsonBean){
        dismiss();
        actionAddListener.addAction(jsonBean);
    }


    /*
    *   选中自动化
    * */
    private void selectAction(File action){
        dismiss();
        if (!chooseAll){
            actionAddListener.addAction(JSONBean.optInt("actionType",17)
                    .put("param",new JSONBean()
                            .put("actionName",FileUtils.getFileNameNoExtension(action))
                            .put("actionId",action.getAbsolutePath())
                    ));
            return;
        }
        final int[] choose = new int[]{0};
        new AlertDialog.Builder(context)
                .setTitle("选择执行自动化方式")
                .setSingleChoiceItems(new String[]{"等待自动化执行完毕再向下执行", "另外运行自动化然后继续向下执行"}, 0,(dialogInterface,i)->{
                        choose[0] = i;
                }).setNegativeButton("确定", (dialogInterface,i)->{
                    dialogInterface.dismiss();
                    actionAddListener.addAction(JSONBean.optInt("actionType",17)
                            .put("witeTime",1000)
                            .put("param",JSONBean.optBoolean("runInCurrentThread",choose[0]==0)
                                    .put("actionName",FileUtils.getFileNameNoExtension(action))
                                    .put("actionId",action.getAbsolutePath())
                            ));
                }).setPositiveButton("取消",null)
                .create()
                .show();
    }

    /*
     *   选中虚拟按键
     * */
    private void selectPramar(DoActionBean bean){
        dismiss();
        actionAddListener.addAction(JSONBean.optInt("actionType",bean.getActionType())
                .put("witeTime",0)
                .put("param",new JSONBean().put("arg",bean.getParam())));
        AppUtils.resumeApp();
    }

    /*
    *   选中虚拟按键
    * */
    private void selectVirtualKey(DoActionBean bean){
        String message = "单位毫秒,1000毫秒 = 1秒，时间太短(<50)很可能出现无法点击";
        InputTextDialog.getDialog(context).setTitle("输入等待时间").setMessage(message)
        .addInputLine(new InputNumberLine(1000)).setInputTextListener((InputLine[] result)->{
            dismiss();
            actionAddListener.addAction(JSONBean.optInt("actionType",bean.getActionType())
                    .put("witeTime",(int)result[0].getResult())
                    .put("param",JSONBean.optString("text",bean.getParam())));
        }).show();
    }

    /*
     *   选中快捷开关
     * */
    private void selectFastSwitch(DoActionBean bean){
        selectVirtualKey(bean);
    }

    public View getFragmentView(BaseFragment fragment){
        return fragment.onCreateView(getActivity().getLayoutInflater(),null,null);
    }
}
