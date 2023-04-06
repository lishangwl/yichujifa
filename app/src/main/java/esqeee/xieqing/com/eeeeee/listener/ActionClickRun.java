package esqeee.xieqing.com.eeeeee.listener;

import android.util.Log;
import android.view.View;

import com.xieqing.codeutils.util.ToastUtils;

import java.io.File;

import esqeee.xieqing.com.eeeeee.MyApp;
import esqeee.xieqing.com.eeeeee.action.Action;
import esqeee.xieqing.com.eeeeee.action.ActionHelper;
import esqeee.xieqing.com.eeeeee.action.ActionRunHelper;
import esqeee.xieqing.com.eeeeee.utils.ActivationUtils;

public class ActionClickRun implements View.OnClickListener {
    private File action;
    public ActionClickRun(File action){
        this.action = action;
    }
    @Override
    public void onClick(View view) {
        Log.d("Test","运行按钮被点击");
        //TODO 运行按钮被点击
        //((Activity)context).moveTaskToBack(true);
        if (ActivationUtils.isActivation()){
            new Thread(()->{
                ActionRunHelper.startAction(view.getContext(),action);
            }).start();
        }else{
            ActivationUtils.showActivationDialog(view.getContext());
        }
    }
}
