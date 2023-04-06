package esqeee.xieqing.com.eeeeee.doAction.api;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.PathMeasure;
import android.os.SystemClock;
import android.util.Log;

import com.xieqing.codeutils.util.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

import esqeee.xieqing.com.eeeeee.bean.JSONArrayBean;
import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.doAction.Base;
import esqeee.xieqing.com.eeeeee.doAction.DoActionBean;
import esqeee.xieqing.com.eeeeee.library.RuntimeLog;
import esqeee.xieqing.com.eeeeee.library.root.Shell;
import esqeee.xieqing.com.eeeeee.widget.CustomPath;

import static esqeee.xieqing.com.eeeeee.service.AccessbilityUtils.pressGestrue;
import static esqeee.xieqing.com.eeeeee.service.AccessbilityUtils.pressSwipLocation;

public class SwipLine extends Base {
    public static Base intansce;
    public static Base getIntansce(){
        if (intansce==null){
            intansce = new SwipLine();
        }
        return intansce;
    }
    @Override
    public boolean post(JSONBean param) {
        if (!param.has("path")){
            RuntimeLog.e("滑动手势读取失败！可能使用了旧版滑动，请删除该动作重新录制！");
            return false;
        }
        waitForAccessbility();
        log("执行:<"+getName()+">");
        return pressGestrue(param.getArray("path"));
    }

    @Override
    public int getType() {
        return 18;
    }

    @Override
    public String getName() {
        return "模拟滑动";
    }
}
