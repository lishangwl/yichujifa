package esqeee.xieqing.com.eeeeee;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.xieqing.codeutils.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import esqeee.xieqing.com.eeeeee.adapter.MyAdapter;
import esqeee.xieqing.com.eeeeee.annotation.NoReproground;
import esqeee.xieqing.com.eeeeee.bean.FloatWindow;
import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.dialog.SelectActionDialog;
import esqeee.xieqing.com.eeeeee.helper.SettingsPreference;
import esqeee.xieqing.com.eeeeee.library.captrue.RecordAutoCaptruer;
import esqeee.xieqing.com.eeeeee.listener.OnActionAddListener;
import esqeee.xieqing.com.eeeeee.ui.BaseActivity;

public class ConditionsActivity extends BaseActivity {
    private JSONBean jsonObject;
    private List<JSONBean> jsonObjects = new ArrayList<>();
    private Toolbar toolbar;
    private FloatingActionButton floatingActionButton;
    private MyAdapter adapter;
    private RecyclerView recyclerView;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_condition;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
    private OnActionAddListener actionAddListener = (JSONBean jsonObject)->{
            if (adapter.getOnMenuSelectPosition() == -1 || adapter.getOnMenuSelectPosition() >= jsonObjects.size()){
                jsonObjects.add(jsonObject);
            }else{
                jsonObjects.add(adapter.getOnMenuSelectPosition()+1,jsonObject);
            }
            adapter.setOnMenuSelectPosition(-1);
            adapter.notifyDataSetChanged();
    };



    private FloatWindow ZXView;
    private int[] location = new int[2];
    private SelectActionDialog selectActionDialog;
    public void addAction(View view) {
        if (selectActionDialog == null){
            selectActionDialog = new SelectActionDialog(ConditionsActivity.this,actionAddListener);
        }
        if (ZXView == null){
            ImageView showView = new ImageView(this);
            showView.setImageDrawable(getResources().getDrawable(R.drawable.biubiubiu));
            ZXView = new FloatWindow.FloatWindowBuilder()
                    .id("addAction")
                    .move(true)
                    .with(showView)
                    .withClick(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showView.getLocationOnScreen(location);
                            final int x = (int) (location[0] + showView.getWidth()/2);
                            final int y = (int) (location[1] + showView.getHeight()/2);
                            Bitmap bitmap = RecordAutoCaptruer.getIntance().captrueScreen();
                            if (bitmap == null){
                                ToastUtils.showShort("截图失败！");
                            }
                            selectActionDialog.setBitmap(bitmap).setX(x).setY(y).show();
                        }
                    })
                    .param(new FloatWindow.FloatWindowLayoutParamBuilder()
                            .flags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
                            .format(PixelFormat.RGBA_8888)
                            .height(SettingsPreference.getFloatSize())
                            .width(SettingsPreference.getFloatSize())
                            .type(MyApp.getFloatWindowType())
                            .build())
                    .build();
        }
        ZXView.add();
    }

    @NoReproground
    private void closeWindow() {
        if (ZXView!=null){
            ZXView.close();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
