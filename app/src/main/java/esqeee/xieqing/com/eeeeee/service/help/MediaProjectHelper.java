package esqeee.xieqing.com.eeeeee.service.help;

import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

import esqeee.xieqing.com.eeeeee.service.AccessbilityObserver;
import esqeee.xieqing.com.eeeeee.service.AccessbilityUtils;
import esqeee.xieqing.com.eeeeee.service.XQMainAccess;

public class MediaProjectHelper extends AccessbilityObserver{

    @Override
    public void onAccessibilityEvent(AccessibilityEvent arg) throws Throwable {
        if (AccessbilityUtils.getCurrentWindowClassName().equals("com.android.systemui.media.MediaProjectionPermissionActivity")){
            List<AccessibilityNodeInfo> nodes = AccessbilityUtils.findNodesByText("不再显示");
            if (nodes.size() > 0){
                nodes.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
            nodes = AccessbilityUtils.findNodesByText("立即开始");
            if (nodes.size() > 0){
                nodes.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }else{
                nodes = AccessbilityUtils.findNodesByText("允许");
                if (nodes.size() > 0){
                    nodes.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                }
            }
            nodes.clear();
        }
    }

    @Override
    public boolean onKeyEvent(KeyEvent event) throws Throwable {
        return false;
    }
}