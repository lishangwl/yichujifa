package esqeee.xieqing.com.eeeeee.library.tilefloat;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.xieqing.codeutils.util.AppUtils;
import com.xieqing.codeutils.util.ScreenUtils;
import com.xieqing.codeutils.util.SizeUtils;

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
import esqeee.xieqing.com.eeeeee.listener.TileMoveListener;
import esqeee.xieqing.com.eeeeee.widget.CircleImageView;

public class TileFloat{
    private static TileFloat tileFloat;
    private FloatWindow floatWindow;
    private Context context;
    public TileFloat(Context context) {
        this.context = context;
        init();
    }

    public static TileFloat getTileFloat(Context context) {
        if (tileFloat == null){
            tileFloat = new TileFloat(context);
        }
        return tileFloat;
    }
    public void updateSetting(){
        floatWindow.setAllowMove(SettingsPreference.canMoveFloatMenu());
    }
    private TileMoveListener tileMoveListener = new TileMoveListener();
    private void init(){
        View.OnClickListener addListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SettingsPreference.useSignleFloat()){
                    List<JSONBean> items = SettingsPreference.getFloatMenuItems();
                    if (items.size()==0){
                        return;
                    }
                    JSONBean bean = items.get(0);
                    JSONBean param = bean.getJson("param");
                    int type = bean.getInt("actionType");
                    if (type == DoActionBean.EXCE_ACTION.getActionType()){
                        Action action = ActionHelper.from(param.getString("actionId"));
                        if (action != null){
                            ActionRunHelper.startAction(context,action);
                        }
                    }else if (type == 12){
                        AppUtils.launchApp(param.getString("packName"));
                    }else{
                        DoActionBean.postAction(type,context,param.getString("text"));
                    }
                    return;
                }
                MenuFloat.getMenuFloat(context).show(floatWindow.getParams().x + floatWindow.getView().getWidth()/2,
                        floatWindow.getParams().y + floatWindow.getView().getHeight()/2);
            }
        };
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(-2,-2,MyApp.getFloatWindowType(),WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,-3);

        layoutParams.width = SizeUtils.dp2px(40);
        layoutParams.height = SizeUtils.dp2px(40);
        layoutParams.x = 0;
        layoutParams.gravity = Gravity.LEFT|Gravity.TOP;
        layoutParams.y = ScreenUtils.getScreenHeight()/3;

        CircleImageView imageView = new CircleImageView(context);
        imageView.setBorderColor(Color.WHITE);
        imageView.setBorderWidth(5);
        imageView.setImageResource(R.drawable.icon_56);
        imageView.setAlpha(0.5f);

        floatWindow = new FloatWindow.FloatWindowBuilder()
                .id("floatmenu")
                .move(SettingsPreference.canMoveFloatMenu())
                .with(imageView)
                .withClick(addListener)
                .move(tileMoveListener)
                .param(layoutParams)
                .build();
        //floatWindow.setMoveListener(null);
    }

    public void show(){
        floatWindow.add();
        if (SettingsPreference.canTileFloatMenu()){
            int viewWidth = floatWindow.getView().getWidth();
            floatWindow.getView().setAlpha(0.5f);
            floatWindow.getView().setX(-viewWidth/2);
        }
    }

    public void dismiss(){
        floatWindow.close();
    }



}
