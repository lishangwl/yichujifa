package esqeee.xieqing.com.eeeeee.ui.floatmenu;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewGroup;

import com.xieqing.codeutils.util.AppUtils;
import com.xieqing.codeutils.util.SizeUtils;
import com.yicu.yichujifa.GlobalContext;
import com.yicu.yichujifa.LayoutHierarchy.BoundsLayoutHierarchy;
import com.yicu.yichujifa.LayoutHierarchy.LayoutHierarchy;
import com.yicu.yichujifa.LayoutHierarchy.ListLayoutHierarchy;

import java.io.File;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.action.ActionRunHelper;
import esqeee.xieqing.com.eeeeee.fragment.ActionsFragment;
import esqeee.xieqing.com.eeeeee.helper.SettingsPreference;
import esqeee.xieqing.com.eeeeee.widget.CircleImageView;
import esqeee.xieqing.com.eeeeee.widget.FDialog;

/**
 * Created by Stardust on 2017/10/18.
 */

public class CircularMenu{

    public static CircularMenu circularMenu;

    public static CircularMenu getCircularMenu(Context context) {
        if (circularMenu == null){
            circularMenu = new CircularMenu(context);
        }
        return circularMenu;
    }

    public void close() {
        if (mWindow!=null){
            mWindow.close();
            isShowing = false;
        }
    }

    boolean isShowing = false;

    public void show() {
        if (!isShowing){
            initFloaty();
            setupListeners();
            isShowing = true;
        }
    }

    public static class StateChangeEvent {
        private int currentState;
        private int previousState;

        public StateChangeEvent(int currentState, int previousState) {
            this.currentState = currentState;
            this.previousState = previousState;
        }

        public int getCurrentState() {
            return currentState;
        }

        public int getPreviousState() {
            return previousState;
        }
    }

    public static final int STATE_CLOSED = -1;
    public static final int STATE_NORMAL = 0;
    public static final int STATE_RECORDING = 1;

    private static final int IC_ACTION_VIEW = R.drawable.icon_56;

    CircularMenuWindow mWindow;
    private int mState;
    private CircleImageView mActionViewIcon;
    private Context mContext;

    private BoundsLayoutHierarchy boundsLayoutHierarchy;
    private ListLayoutHierarchy listLayoutHierarchy;

    public CircularMenu(Context context) {
        mContext = new ContextThemeWrapper(context, R.style.AppTheme);
        //initFloaty();
        //setupListeners();
    }

    private void setupListeners() {
        mWindow.setOnActionViewClickListener(v -> {
            if (mWindow.isExpanded()) {
                mWindow.collapse();
            } else {
                mWindow.expand();
            }
        });
    }

    private void initFloaty() {
        mWindow = new CircularMenuWindow(mContext, new CircularMenuFloaty() {

            @Override
            public View inflateActionView(CircularMenuWindow window) {
                mActionViewIcon = new CircleImageView(mContext);
                mActionViewIcon.setBorderColor(Color.WHITE);
                mActionViewIcon.setBorderWidth(5);
                mActionViewIcon.setImageResource(R.drawable.icon_56);
                mActionViewIcon.setAlpha(0.5f);
                mActionViewIcon.setLayoutParams(new ViewGroup.LayoutParams(SizeUtils.dp2px(40),SizeUtils.dp2px(40)));
                return mActionViewIcon;
            }

            @Override
            public CircularActionMenu inflateMenuItems(CircularMenuWindow window) {
                CircularActionMenu menu = (CircularActionMenu) View.inflate(mContext, R.layout.circular_menu, null);
                ButterKnife.bind(CircularMenu.this, menu);
                return menu;
            }
        });
        mWindow.setKeepToSideHiddenWidthRadio(0.25f);
    }


    @Optional
    @OnClick(R.id.script_list)
    void showScriptList() {
        mWindow.collapse();

        FDialog dialog = new FDialog(mContext).setTitle("执行脚本").setCanfirm("",null);
        ActionsFragment appsFragment = new ActionsFragment();
        appsFragment.setOnActionSelectedListener((File file)->{
            dialog.dissmis();
            ActionRunHelper.startAction(GlobalContext.getContext(),file);
        });
        appsFragment.show(dialog);
    }



    private void setState(int state) {
        mState = state;
        mActionViewIcon.setImageResource(IC_ACTION_VIEW);
        mActionViewIcon.setBackgroundResource(R.drawable.circle_white);
        int padding = (int) mContext.getResources().getDimension(R.dimen.padding_5);
        mActionViewIcon.setPadding(padding, padding, padding, padding);
    }


    @Optional
    @OnClick(R.id.layout_inspect)
    void inspectLayout() {

        mWindow.collapse();
        LayoutHierarchy.getInstance().showDialog();
    }

    @Optional
    @OnClick(R.id.stopAll)
    void stopAll() {
        mWindow.collapse();
        ActionRunHelper.stopAllAction();
    }

    FDialog setting;

    @Optional
    @OnClick(R.id.settings)
    void settings() {
        mWindow.collapse();
        setting = new FDialog(GlobalContext.getContext())
                .setTitle("")
                .setCanfirm("",null)
                .setDismissListener(d->{

                }).setItems(new String[]{"回到一触即发","关闭悬浮窗"},i->{
            new Handler().postDelayed(()->{
                onSelectAction(i);
            },500);
        });
        setting.show();
    }

    private void onSelectAction(int i) {
        switch (i){
            case 0:
                AppUtils.resumeApp();
                break;
            case 1:
                close();
                SettingsPreference.setShowFloatWindow(false);
                break;
        }
    }

}
