package esqeee.xieqing.com.eeeeee.adapter.holder_item;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.jhonjson.dialoglib.BottomListDialog;
import com.xieqing.codeutils.util.EncryptUtils;
import com.xieqing.codeutils.util.ImageUtils;
import com.xieqing.codeutils.util.ToastUtils;

import java.io.File;
import java.text.SimpleDateFormat;

import butterknife.BindView;
import esqeee.xieqing.com.eeeeee.AddActivity;
import esqeee.xieqing.com.eeeeee.MyApp;
import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.action.ActionHelper;
import esqeee.xieqing.com.eeeeee.adapter.MyAdapter;
import esqeee.xieqing.com.eeeeee.bean.FloatWindow;
import esqeee.xieqing.com.eeeeee.bean.JSONArrayBean;
import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.dialog.GestruePathDialog;
import esqeee.xieqing.com.eeeeee.dialog.MakeSizeDialog;
import esqeee.xieqing.com.eeeeee.dialog.SwipePathDialog;
import esqeee.xieqing.com.eeeeee.helper.SettingsPreference;
import esqeee.xieqing.com.eeeeee.library.captrue.RecordAutoCaptruer;
import esqeee.xieqing.com.eeeeee.listener.OnMakeSizeRectListener;
import esqeee.xieqing.com.eeeeee.ui.BaseActivity;
import esqeee.xieqing.com.eeeeee.view.PathAnimation;
import esqeee.xieqing.com.eeeeee.widget.CustomPath;

public class GesnterHolder extends BaseHolder{
    @BindView(R.id.line)
    View view;
    public GesnterHolder(Context context, MyAdapter adapter) {
        super(context,R.layout.holder_swipe_line,adapter);
    }

    private FrameLayout frameLayout;
    private FloatWindow showViewWindow;

    @Override
    public void onBind(JSONBean jsonBean) {
        super.onBind(jsonBean);
        requestTime(true);
        JSONBean param = jsonBean.getJson("param");

        view.setOnClickListener(v->{
            new BottomListDialog.Builder(getContext())
                    .addMenuListItem(new String[]{"显示轨迹", "重新选取"},position -> {
                        if (position == 0){
                            showViewWindow.add();
                            JSONArrayBean paths = param.getArray("path");
                            if (paths == null){
                                ToastUtils.showShort("读取轨迹出错，请重新录制");
                                return;
                            }
                            for (int i = 0;i<paths.length();i++){
                                CustomPath customPath =CustomPath.from(paths.getJson(i));
                                if (customPath == null){
                                    continue;
                                }
                                ImageView showView = new ImageView(getContext());
                                showView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.biubiubiu));
                                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(SettingsPreference.getFloatSize(),SettingsPreference.getFloatSize());
                                showView.setLayoutParams(layoutParams);
                                frameLayout.addView(showView);
                                new PathAnimation(showView,customPath,customPath.getDuration()).start();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        frameLayout.removeView(showView);
                                        if (frameLayout.getChildCount() == 0){
                                            showViewWindow.close();
                                        }
                                    }
                                }, customPath.getDuration());
                            }
                            if (frameLayout.getChildCount() == 0){
                                showViewWindow.close();
                            }
                        }else{
                            ToastUtils.showLong("再次点击准心，即可重新选取");
                            ((AddActivity)getContext()).setHookListener(k->{
                                new GestruePathDialog((BaseActivity)getContext(), (json)->{
                                    jsonBean.put("param",json.getJson("param"));
                                    getAdapter().notifyItemChanged(getAdapterPosition());
                                }).show();
                            });
                        }
                    }).show();
        });
    }



    @Override
    public void initView() {
        frameLayout = new FrameLayout(getContext());
        frameLayout.setBackgroundColor(android.graphics.Color.parseColor("#50000000"));
        showViewWindow = new FloatWindow.FloatWindowBuilder()
                .id("addAction")
                .move(false)
                .with(frameLayout)
                .withClick(null)
                .param(new FloatWindow.FloatWindowLayoutParamBuilder()
                        .flags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
                        .format(PixelFormat.TRANSLUCENT)
                        .height(WindowManager.LayoutParams.MATCH_PARENT)
                        .width(WindowManager.LayoutParams.MATCH_PARENT)
                        .type(MyApp.getFloatWindowType())
                        .build())
                .build();
    }

    @Override
    public int getIcon() {
        return R.drawable.ic_shoushi;
    }

    @Override
    public String getName() {
        return "执行手势";
    }
}
