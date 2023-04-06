package esqeee.xieqing.com.eeeeee.library.tilefloat;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import androidx.cardview.widget.CardView;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xieqing.codeutils.util.AppUtils;
import com.xieqing.codeutils.util.ScreenUtils;
import com.xieqing.codeutils.util.SizeUtils;

import java.util.ArrayList;
import java.util.List;

import esqeee.xieqing.com.eeeeee.MyApp;
import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.action.Action;
import esqeee.xieqing.com.eeeeee.action.ActionHelper;
import esqeee.xieqing.com.eeeeee.action.ActionRunHelper;
import esqeee.xieqing.com.eeeeee.bean.FloatWindow;
import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.doAction.DoActionBean;
import esqeee.xieqing.com.eeeeee.helper.SettingsPreference;
import esqeee.xieqing.com.eeeeee.ui.FloatMenuActivity;
import esqeee.xieqing.com.eeeeee.widget.CircleImageView;

public class MenuFloat {
    private static MenuFloat menuFloat;
    private FloatWindow floatWindow;
    private Context context;
    public MenuFloat(Context context) {
        this.context = context;
        init();
    }

    public static MenuFloat getMenuFloat(Context context) {
        if (menuFloat == null){
            menuFloat = new MenuFloat(context);
        }
        return menuFloat;
    }
    private Animator started = null;
    private Animator end = null;
    private List<JSONBean> menuItems = new ArrayList<>();
    private ImageView addImage;
    public void show(int x,int y){
        int centerX = frameLayout.getWidth()/2;
        int centerY = frameLayout.getWidth()/2;
        int padding = 30;
        int radius = frameLayout.getWidth();

        x = x - frameLayout.getWidth()/2;
        y = y - frameLayout.getHeight()/2;

        if (x < 30){
            x = 30;
            centerX = 0;
        }
        if (x > ScreenUtils.getScreenWidth() - 30 - frameLayout.getWidth()){
            x = ScreenUtils.getScreenWidth() - 30 - frameLayout.getWidth();
            centerX = frameLayout.getWidth();
        }
        if (y < 30){
            y = 30;
            centerY = 0;
        }
        if (y > ScreenUtils.getScreenHeight() - 30 - frameLayout.getWidth() - ScreenUtils.getVirtualBarHeight() - ScreenUtils.getStatusBarHeight() ){
            y = ScreenUtils.getScreenHeight() - 30 - frameLayout.getWidth() - ScreenUtils.getVirtualBarHeight() - ScreenUtils.getStatusBarHeight();
            centerY = frameLayout.getHeight();
        }
        floatWindow.getView().setVisibility(View.VISIBLE);
        floatWindow.add();
        floatWindow.x(x).y(y).update();
        int finalCenterX = centerX;
        int finalCenterY = centerY;
        gridView.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        if (started!=null && started.isRunning()){
                            started.end();
                        }
                        started = null;
                        started = ViewAnimationUtils.createCircularReveal(frameLayout, finalCenterX, finalCenterY, 0, radius);
                        started.setDuration(200);
                        started.addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animator) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animator) {
                                update();
                                started.removeAllListeners();
                            }

                            @Override
                            public void onAnimationCancel(Animator animator) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animator) {

                            }
                        });
                        started.start();


                        end = null;
                        end = ViewAnimationUtils.createCircularReveal(frameLayout, finalCenterX, finalCenterY, radius, 0);
                        end.setDuration(200);
                    }
                }catch (IllegalStateException e){
                    e.printStackTrace();
                    update();
                }
            }
        },2);
    }

    public void dismiss(){
        if (end == null){
            floatWindow.close();
        }else{
            end.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    floatWindow.getView().setVisibility(View.GONE);
                    floatWindow.close();
                    TileFloat.getTileFloat(context).show();
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            if (!end.isRunning() && !end.isStarted()){
                end.start();
            }
        }
    }
    public void update(){
        menuItems.clear();
        menuItems.addAll(SettingsPreference.getFloatMenuItems());
        ((BaseAdapter)gridView.getAdapter()).notifyDataSetChanged();
        if (menuItems.size() == 0){
            addImage.setVisibility(View.VISIBLE);
        }else{
            addImage.setVisibility(View.GONE);
        }
    }
    private CardView frameLayout;
    private GridView gridView;
    private void init(){
        addImage = new ImageView(context);
        addImage.setOnClickListener(v->{
            Intent intent2 = new Intent(context, FloatMenuActivity.class);
            intent2.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            context.startActivity(intent2);
            dismiss();
        });
        addImage.setImageDrawable(context.getResources().getDrawable(R.drawable.addfloat));
        addImage.setPadding(250,250,250,250);
        View.OnClickListener addListener = view->{
            dismiss();
        };
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(-2,-2,MyApp.getFloatWindowType(),WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,-3);
        layoutParams.gravity=Gravity.LEFT | Gravity.TOP;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;

        frameLayout = new CardView(context){
            @Override
            public boolean dispatchKeyEvent(KeyEvent event) {
                if(event.getKeyCode() == KeyEvent.KEYCODE_BACK
                        || event.getKeyCode() == KeyEvent.KEYCODE_SETTINGS) {
                    dismiss();
                    return true;
                }
                return false;
            }

            @Override
            public boolean dispatchTouchEvent(MotionEvent ev) {
                //RuntimeLog.log(ev.toString());
                if (ev.getAction() == MotionEvent.ACTION_OUTSIDE){
                    dismiss();
                    return true;
                }
                return super.dispatchTouchEvent(ev);
            }
        };
        frameLayout.setRadius(10);
        gridView = new GridView(context);
        gridView.setBackground(new ColorDrawable(Color.WHITE));
        gridView.setLayoutParams(new ViewGroup.LayoutParams(600,600));
        addImage.setLayoutParams(new ViewGroup.LayoutParams(600,600));
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent2 = new Intent(context, FloatMenuActivity.class);
                intent2.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                context.startActivity(intent2);
                dismiss();
                return true;
            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                dismiss();
                gridView.postDelayed(()->{
                    JSONBean bean = menuItems.get(position);
                    JSONBean param = bean.getJson("param");
                    int type = bean.getInt("actionType");
                    if (type == DoActionBean.EXCE_ACTION.getActionType()){
                        Action action =  ActionHelper.from(param.getString("actionId"));
                        if (action != null){
                            ActionRunHelper.startAction(context,action);
                        }
                    }else if (type == 12){
                        AppUtils.launchApp(param.getString("packName"));
                    }else{
                        DoActionBean.postAction(type,context,param.getString("text"));
                    }
                },300);
            }
        });
        gridView.setNumColumns(3);
        gridView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return menuItems.size();
            }

            @Override
            public Object getItem(int i) {
                return null;
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(int position, View view, ViewGroup viewGroup) {
                ViewHolder holder;
                if (view==null || view.getTag()==null){
                    holder = new ViewHolder();
                    LinearLayout linearLayout = new LinearLayout(context);
                    linearLayout.setOrientation(LinearLayout.VERTICAL);
                    linearLayout.setGravity(Gravity.CENTER);
                    TextView textView = new TextView(context);
                    textView.setGravity(Gravity.CENTER);
                    CircleImageView imageView = new CircleImageView(context);
                    view = linearLayout;
                    ViewGroup.LayoutParams layoutParams1 = new ViewGroup.LayoutParams(100,100);
                    imageView.setLayoutParams(layoutParams1);
                    imageView.setPadding(SizeUtils.dp2px(3),SizeUtils.dp2px(3),SizeUtils.dp2px(3),SizeUtils.dp2px(3));
                    layoutParams1 = new ViewGroup.LayoutParams(200,200);
                    linearLayout.setLayoutParams(layoutParams1);
                    linearLayout.addView(imageView);
                    linearLayout.addView(textView);

                    holder.icon = imageView;
                    holder.name = textView;
                }else{
                    holder = (ViewHolder) view.getTag();
                }
                JSONBean bean = menuItems.get(position);
                JSONBean param = bean.getJson("param");
                int type = bean.getInt("actionType");
                if (type == DoActionBean.EXCE_ACTION.getActionType()){
                    Action action =  ActionHelper.from(param.getString("actionId"));
                    if (action != null){
                        holder.icon.setImageDrawable(action.getDrawable());
                        holder.name.setText(action.getTitle());
                    }
                }else if (type == 12){
                    AppUtils.AppInfo appInfo = AppUtils.getAppInfo(param.getString("packName"));
                    if(appInfo!=null){
                        holder.icon.setImageDrawable(appInfo.getIcon());
                        holder.name.setText(appInfo.getName());
                    }
                }else{
                    DoActionBean doActionBean = DoActionBean.getBeanFromType(type);
                    holder.icon.setImageDrawable(doActionBean.getDrawable());
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    }
                    holder.name.setText(doActionBean.getActionName());
                }
                return view;
            }
        });
        frameLayout.addView(gridView);
        frameLayout.addView(addImage);




        floatWindow = new FloatWindow.FloatWindowBuilder()
                .id("menufloat")
                .move(false)
                .with(frameLayout)
                .withClick(addListener)
                .param(layoutParams)
                .build();
    }


    class ViewHolder{
        ImageView icon;
        TextView name;
    }
}
