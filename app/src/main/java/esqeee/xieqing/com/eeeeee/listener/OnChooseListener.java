package esqeee.xieqing.com.eeeeee.listener;

import android.graphics.Rect;

import com.xieqing.codeutils.util.AppUtils;

import esqeee.xieqing.com.eeeeee.action.Action;
import esqeee.xieqing.com.eeeeee.doAction.DoActionBean;

public interface OnChooseListener {
    public void onChoose(AppUtils.AppInfo appInfo, DoActionBean doActionBean,Action action,String param);
}
