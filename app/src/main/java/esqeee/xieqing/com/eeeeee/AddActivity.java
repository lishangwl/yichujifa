package esqeee.xieqing.com.eeeeee;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.liyi.flow.FlowView;
import com.xieqing.codeutils.util.PermissionUtils;
import com.xieqing.codeutils.util.SPUtils;
import com.xieqing.codeutils.util.ToastUtils;
import com.xieqing.codeutils.util.Utils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import esqeee.xieqing.com.eeeeee.action.Action;
import esqeee.xieqing.com.eeeeee.action.ActionCoverter;
import esqeee.xieqing.com.eeeeee.action.ActionHelper;
import esqeee.xieqing.com.eeeeee.adapter.holder_item.BaseHolder;
import esqeee.xieqing.com.eeeeee.annotation.NoReproground;
import esqeee.xieqing.com.eeeeee.bean.FloatWindow;
import esqeee.xieqing.com.eeeeee.bean.JSONArrayBean;
import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.bean.RefreshAction;
import esqeee.xieqing.com.eeeeee.bean.StandbyAction;
import esqeee.xieqing.com.eeeeee.dialog.SelectActionDialog;
import esqeee.xieqing.com.eeeeee.fragment.AutoConfigFragment;
import esqeee.xieqing.com.eeeeee.fragment.AutoFragment;
import esqeee.xieqing.com.eeeeee.fragment.BaseFragment;
import esqeee.xieqing.com.eeeeee.fragment.VariableTableFragment;
import esqeee.xieqing.com.eeeeee.helper.SettingsPreference;
import esqeee.xieqing.com.eeeeee.library.RuntimeLog;
import esqeee.xieqing.com.eeeeee.library.captrue.RecordAutoCaptruer;
import esqeee.xieqing.com.eeeeee.listener.KeyboardChangeListener;
import esqeee.xieqing.com.eeeeee.service.FloattingService;
import esqeee.xieqing.com.eeeeee.ui.BaseActivity;
import esqeee.xieqing.com.eeeeee.ui.CodeCovertActivity;
import esqeee.xieqing.com.eeeeee.view.ValotionView;
import esqeee.xieqing.com.eeeeee.widget.ValotionEdittext;


public class AddActivity extends BaseActivity {


    private Action currentAction;
    private ImageView showView;
    private JSONArrayBean jsonObjects = new JSONArrayBean();

    private JSONBean action;

    private ValotionView valotionView;
    private List<JSONBean> variables;


    @BindView(R.id.valotion)
    View valotion;

    @BindView(R.id.flowVi)
    FlowView flowView;

    @Override
    public int getContentLayout() {
        return R.layout.activity_add;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApp.isEdit = true;
        initView();
        new KeyboardChangeListener(this).setKeyBoardListener((boolean isShow, int keyboardHeight)-> {
            if (getWindow().getDecorView().findFocus() instanceof ValotionEdittext){
                valotion.setVisibility(isShow?View.VISIBLE:View.GONE);
                if (!isShow){
                    findViewById(R.id.flow).setVisibility(View.GONE);
                }
            }else{
                valotion.setVisibility(View.GONE);
                findViewById(R.id.flow).setVisibility(View.GONE);
            }
        });



    }

    private MenuItem complete = null;

    @Override
    public void addFragment(int id, BaseFragment fragment) {
        super.addFragment(id, fragment);
    }



    public static void open(Context context, String file){
        if (!PermissionUtils.getAppOps(Utils.getApp())){
            ToastUtils.showShort("请先开启悬浮窗权限");
            PermissionUtils.openAps(Utils.getApp());
            return;
        }
        Intent intent = new Intent(context, AddActivity.class);
        intent.putExtra("path",file);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add,menu);
        complete = menu.findItem(R.id.action_complete);
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if (getSupportFragmentManager().getBackStackEntryCount() > 1){
                    complete.setTitle("返回上一层");
                }else{
                    complete.setTitle("完毕");
                }
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_complete:
                if (getFragmentSize()>1){
                    back();
                    return true;
                }
                save();
                EventBus.getDefault().post(RefreshAction.INTANSCE);
                EventBus.getDefault().post(new StandbyAction(currentAction));
                finish();
                break;
            case R.id.action_fornt:
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                for (Fragment fragment : getSupportFragmentManager().getFragments()){
                    transaction.remove(fragment);
                }
                transaction.commit();
                addFragment(R.id.listView,AutoFragment.create(jsonObjects,variables,this));
                complete.setTitle("完毕");
                break;
            case R.id.action_var:
                if (!(getTopFragment() instanceof VariableTableFragment)){
                    addFragment(R.id.listView, VariableTableFragment.create(this,variables));
                }
                break;
            case R.id.action_save:
                save();
                break;
            case R.id.action_collose:
            case R.id.action_expland:
                Fragment baseFragment = getTopFragment();
                if (baseFragment instanceof AutoFragment){
                    RecyclerView recyclerView = ((AutoFragment)baseFragment).getRecyclerView();
                    RecyclerView.Adapter adapter = ((AutoFragment)baseFragment).getAdapter();
                    for (int i = 0 ;i < recyclerView.getChildCount(); i++){
                        BaseHolder holder = (BaseHolder) recyclerView.getChildViewHolder(recyclerView.getChildAt(i));
                        if (item.getItemId() == R.id.action_collose){
                            holder.collose();
                        }else{
                            holder.expland();
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
                break;
            case R.id.action_config:
                if (!(getTopFragment() instanceof AutoConfigFragment)){
                    addFragment(R.id.listView, AutoConfigFragment.create(action,currentAction));
                }
                break;
            case R.id.action_add:
                addAction();
                break;
            case R.id.action_covert:
                showProgress("正在转换");
                new Thread(()->{
                    try {
                        String code = new ActionCoverter(action).covert();
                        runOnUiThread(()->{
                            disProgress();
                            CodeCovertActivity.luanch(this,code);
                        });
                    }catch (Exception e){
                        ToastUtils.showLong("转换失败，详情请查看日志："+e.getMessage());
                        RuntimeLog.e(e);
                    }

                }).start();
                break;
            case R.id.action_moreChooose:
                baseFragment = getTopFragment();
                if (baseFragment instanceof AutoFragment){
                    ((AutoFragment)baseFragment).disptachMenuSelected(item.getItemId());
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (currentAction == null){
            return;
        }
        currentAction.setCount(jsonObjects.length());
        action.put("actions",jsonObjects);
        action.put("variables",new JSONArrayBean(variables));
        currentAction.setData(action.toString());
        currentAction.save();


        super.onSaveInstanceState(outState);
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener((view)->{
            back();
        });

        valotionView = new ValotionView(this,flowView);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            back();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    private void back() {
        if (getFragmentSize() > 1){
            getSupportFragmentManager().popBackStack();
            return;
        }
        new AlertDialog.Builder(this).setTitle("提示")
                    .setMessage("您还没有保存，如果现在退出将不会保存任何的修改")
                    .setNegativeButton("继续退出", (d,i)->{
                        finish();
                    }).setPositiveButton("取消",null).create().show();
    }

    public void showOrHideVolation(View v){
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        View fouc = getWindow().getDecorView().findFocus();

        if (findViewById(R.id.flow).getVisibility() == View.GONE){
            findViewById(R.id.flow).setVisibility(View.VISIBLE);
            valotionView.bind(variables);
            if (fouc instanceof ValotionEdittext){
                //inputMethodManager.hideSoftInputFromWindow(fouc.getWindowToken(),0);
            }
        }else{
            findViewById(R.id.flow).setVisibility(View.GONE);
            if (fouc instanceof ValotionEdittext){
                inputMethodManager.showSoftInput(fouc,0);
            }
        }
    }

    private void addAction() {
        if (!checkPermission("window")){
            return;
        }
        if (SPUtils.getInstance(getPackageName()).getBoolean("showImageScale",true)){
            ImageView imageView = new ImageView(this);
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.biubiubiu));
            new AlertDialog.Builder(this).setTitle("提示")
                    .setView(imageView)
                    .setMessage("拖动准心到需要操作的位置，然后点击准心选择动作即可添加")
                    .setPositiveButton("知道了",(d,i)->{
                        openWindow();
                    })
                    .setNeutralButton("不再提示", (d,i)->{
                        SPUtils.getInstance(getPackageName()).put("showImageScale",false);
                        openWindow();
                    }).create().show();
        }else{
            openWindow();
        }
    }
    private FloatWindow ZXView;
    private View.OnClickListener addListener;
    private int[] location = new int[2];
    private SelectActionDialog selectActionDialog;


    public AutoFragment getTopAutoFragment(){
        List<Fragment> fragments =getSupportFragmentManager().getFragments();
        for (int i = fragments.size();i>0;i--){
            if (fragments.get(i - 1) instanceof AutoFragment){
                return (AutoFragment)fragments.get(i - 1);
            }
        }
        return null;
    }

    private View.OnClickListener hookListener;

    public void setHookListener(View.OnClickListener hookListener) {
        this.hookListener = hookListener;
    }

    @NoReproground
    public void openWindow() {
        if (ZXView == null){
            selectActionDialog = new SelectActionDialog(AddActivity.this,null);
            addListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (hookListener!=null){
                        hookListener.onClick(view);
                        hookListener = null;
                        return;
                    }
                    if (!RecordAutoCaptruer.getIntance().isRequestPermission()) {
                        RecordAutoCaptruer.getIntance().request(AddActivity.this);
                        return;
                    }
                    showView.getLocationOnScreen(location);
                    final int x = (int) (location[0] + showView.getWidth()/2);
                    final int y = (int) (location[1] + showView.getHeight()/2);
                    Bitmap bitmap = RecordAutoCaptruer.getIntance().captrueScreen();
                    if (bitmap == null){
                        ToastUtils.showShort("截图失败！");
                    }

                    selectActionDialog.setActionAddListener(((AutoFragment)getTopAutoFragment()).getActionAddListener());
                    selectActionDialog.setBitmap(bitmap).setX(x).setY(y).show();
                }
            };
            showView = new ImageView(this);
            showView.setImageDrawable(getResources().getDrawable(R.drawable.biubiubiu));
            ZXView = new FloatWindow.FloatWindowBuilder()
                    .id("addAction")
                    .move(true)
                    .with(showView)
                    .withClick(addListener)
                    .param(
                            new FloatWindow.FloatWindowLayoutParamBuilder()
                            .flags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
                            .format(PixelFormat.TRANSLUCENT)
                            .height(SettingsPreference.getFloatSize())
                            .width(SettingsPreference.getFloatSize())
                            .type(MyApp.getFloatWindowType())
                            .build())
                    .build();
        }
        ZXView.add();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    private boolean init = false;
    @Override
    protected void onResume() {
        super.onResume();
        if (ZXView!=null){
            ZXView.setClickListener(addListener);
        }

        if (!init){
            showProgress("提示","正在打开自动化");
            new Thread(()->{
                init();
                init = true;
                disProgress();
            }).start();

            addAction();
        }
    }

    @NoReproground
    private void closeWindow() {
        if (ZXView!=null){
            ZXView.close();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApp.isEdit = false;
        closeWindow();
        FloattingService.getService().closeWindow("condition");
        FloattingService.getService().closeWindow("imagecap");
    }




    private void save() {
        currentAction.setCount(jsonObjects.length());
        action.put("actions",jsonObjects);
        action.put("variables",new JSONArrayBean(variables));
        currentAction.setData(action.toString());
        currentAction.save();


        ToastUtils.showShort("已保存");
        //finish();
    }


    private boolean init(){
        currentAction = ActionHelper.from(getIntent().getStringExtra("path"));
        if (currentAction == null){
            ToastUtils.showShort("自动化打开失败");
            finish();
            return false;
        }
        if (currentAction.isEncrypt()){
            ToastUtils.showShort("无法编辑，该脚本已加密");
            finish();
            return false;
        }
        if (currentAction.getData() == null || currentAction.getData().equals("")){
            action = new JSONBean();
        }else {
            action = new JSONBean(currentAction.getData());
            jsonObjects = action.getArray("actions");
        }

        if (jsonObjects == null){
            jsonObjects = new JSONArrayBean();
        }

        JSONArrayBean v = action.getArray("variables");
        if (v==null){
            variables = new ArrayList<>();
        }else{
            variables = v.toList();
        }






        runOnUiThread(()->{
            ((Toolbar) findViewById(R.id.toolbar)).setTitle(currentAction.getTitle());
            addFragment(R.id.listView,AutoFragment.create(jsonObjects,variables,this));
        });
        return true;
    }



}
