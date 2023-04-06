package esqeee.xieqing.com.eeeeee.widget.menu;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.xieqing.codeutils.util.AnimationUtils;
import com.xieqing.codeutils.util.AppUtils;
import com.xieqing.codeutils.util.FileIOUtils;
import com.xieqing.codeutils.util.PermissionUtils;
import com.xieqing.codeutils.util.ScreenUtils;
import com.xieqing.codeutils.util.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.action.Action;
import esqeee.xieqing.com.eeeeee.action.ActionHelper;
import esqeee.xieqing.com.eeeeee.dialog.InputTextDialog;
import esqeee.xieqing.com.eeeeee.dialog.input.InputLine;
import esqeee.xieqing.com.eeeeee.dialog.input.InputTextLine;
import esqeee.xieqing.com.eeeeee.dialog.input.InputTextListener;
import esqeee.xieqing.com.eeeeee.event.RefreshActionEvent;
import esqeee.xieqing.com.eeeeee.library.record.AccessbilityRecorder;
import esqeee.xieqing.com.eeeeee.service.AccessbilityUtils;
import esqeee.xieqing.com.eeeeee.widget.SimpleAnimationListener;

public class BottomMenu extends PopupWindow {

    private RelativeLayout showView;
    @BindView(R.id.close)
    View view;
    @BindView(R.id.viewGroup1)
    View viewGroup1;

    Activity mActivity;

    private File currentDir = ActionHelper.workSpaceDir;


    public BottomMenu(Activity activity) {
        super(activity);
        try {
            mActivity = activity;
            setAnimationStyle(0);
            showView = (RelativeLayout) View.inflate(activity, R.layout.activity_home_bottom_menu_dialog, null);
            ButterKnife.bind(this, showView);
            setWidth(-1);
            setHeight(-1);
            setFocusable(true);
            setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            showView.setOnClickListener(v -> {
                dismiss();
            });
            setContentView(showView);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        BottomMenu.super.showAtLocation(parent, gravity, x, y);
        showView.post(() -> {
            AnimationUtils.alpha(showView, 0f, 1f, new SimpleAnimationListener());
            AnimationUtils.rotation(view, 0, 45, new SimpleAnimationListener());
            AnimationUtils.translate(viewGroup1, ScreenUtils.getScreenHeight(), viewGroup1.getY(), new SimpleAnimationListener());
        });
    }

    @Override
    public void dismiss() {
        AnimationUtils.translate(viewGroup1, viewGroup1.getY(), ScreenUtils.getScreenHeight(), new SimpleAnimationListener());
        AnimationUtils.rotation(view, 45, 0, new SimpleAnimationListener());
        AnimationUtils.alpha(showView, 1f, 0f, new SimpleAnimationListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                BottomMenu.super.dismiss();
            }
        });
    }

    @OnClick(R.id.create_script)
    void create_script() {
        dismiss();
        new InputTextDialog(mActivity, false).setTitle("添加自动化脚本")
                .addInputLine(new InputTextLine("名称"))
                .setInputTextListener(result -> {
                    String name = result[0].getResult().toString() + ".ycf";
                    File file = new File(currentDir, name);
                    if (file.exists()) {
                        ToastUtils.showShort("该脚本已经存在");
                    } else {
                        if (file.createNewFile()) {
                            Action action = Action.newAction();
                            action.setData("");
                            if (FileIOUtils.writeFileFromString(file, ActionHelper.actionToString(action))) {
                                //onRefresh();
                                EventBus.getDefault().post(new RefreshActionEvent());
                            } else {
                                ToastUtils.showShort("创建失败，请查看是否给了软件存储权限");
                            }
                        } else {
                            ToastUtils.showShort("创建脚本失败！请检查权限");
                        }
                    }
                }).show();
    }

    @OnClick(R.id.create_ui)
    void create_ui() {
        dismiss();
        new InputTextDialog(mActivity, false).setTitle("添加界面")
                .addInputLine(new InputTextLine("名称"))
                .setInputTextListener(result -> {
                    String name = result[0].getResult().toString() + ".ycfml";
                    File file = new File(currentDir, name);
                    if (file.exists()) {
                        ToastUtils.showShort("该界面已经存在");
                    } else {
                        if (file.createNewFile()) {
                            if (FileIOUtils.writeFileFromString(file, "<界面></界面>")) {
                                //onRefresh();
                                EventBus.getDefault().post(new RefreshActionEvent());
                            } else {
                                ToastUtils.showShort("创建失败，请查看是否给了软件存储权限");
                            }
                        } else {
                            ToastUtils.showShort("创建界面失败！请检查权限");
                        }
                    }
                }).show();
    }

    @OnClick(R.id.transcribe)
    void create_file() {
        dismiss();
        if (!PermissionUtils.getAppOps(mActivity)) {
            ToastUtils.showShort("您需要开启悬浮窗权限，才能使用录制功能");
            PermissionUtils.openAps(mActivity);
            return;
        }
        if (!AccessbilityUtils.isAccessibilitySettingsOn(mActivity)) {
            AccessbilityUtils.toSetting();
            return;
        }
        mActivity.moveTaskToBack(false);
        AccessbilityRecorder.getRecorder().setListener(b -> {
            AccessbilityRecorder.getRecorder().close();
            AppUtils.resumeApp();

            new InputTextDialog(mActivity).setTitle("保存")
                    .addInputLine(new InputTextLine("名称", ""))
                    .setInputTextListener(result -> {
                        Action action = Action.newAction();
                        action.setData(b.buildString());
                        if (FileIOUtils.writeFileFromString(new File(currentDir, result[0].getResult().toString() + ".ycf"), ActionHelper.actionToString(action))) {
                            //onRefresh();
                            EventBus.getDefault().post(new RefreshActionEvent());
                        } else {
                            ToastUtils.showShort("创建失败，请查看是否给了软件存储权限");
                        }
                    }).show();
        });
        AccessbilityRecorder.getRecorder().startRecord(mActivity);
    }

    @OnClick(R.id.create_folder)
    void create_folder() {
        dismiss();
        new InputTextDialog(mActivity, false).setTitle("添加文件夹")
                .addInputLine(new InputTextLine("名称"))
                .setInputTextListener(new InputTextListener() {
                    @Override
                    public void onConfirm(InputLine[] result) throws Exception {
                        String name = result[0].getResult().toString();
                        File file = new File(currentDir, name);
                        if (file.exists() && file.isDirectory()) {
                            ToastUtils.showShort("该文件夹已经存在");
                        } else {
                            file.mkdir();
                            //onRefresh();
                            EventBus.getDefault().post(new RefreshActionEvent());
                        }
                    }
                }).show();
    }


}
