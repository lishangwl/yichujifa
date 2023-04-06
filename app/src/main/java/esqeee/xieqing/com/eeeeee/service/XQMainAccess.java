package esqeee.xieqing.com.eeeeee.service;

import android.accessibilityservice.AccessibilityService;

import androidx.annotation.RequiresApi;
import androidx.core.accessibilityservice.AccessibilityServiceInfoCompat;
import androidx.appcompat.app.AlertDialog;

import android.util.Log;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityWindowInfo;

import com.xieqing.codeutils.util.AppUtils;

import java.util.ArrayList;
import java.util.List;

import esqeee.xieqing.com.eeeeee.library.RuntimeLog;

import static androidx.core.accessibilityservice.AccessibilityServiceInfoCompat.CAPABILITY_CAN_REQUEST_ENHANCED_WEB_ACCESSIBILITY;
import static android.view.accessibility.AccessibilityWindowInfo.TYPE_APPLICATION;


public class XQMainAccess extends AccessibilityService{
    private String TAG = "XQMainAccess";
    private CharSequence currentWindowClassName = "";
    private CharSequence currentPackageName = "";
    private List<AccessbilityObserver> eventObservers = new ArrayList<>();


    public synchronized void addEventObserver(AccessbilityObserver eventAccessbilityObserver){
        eventObservers.add(eventAccessbilityObserver);
    }

    public synchronized void removeEventObserver(AccessbilityObserver eventAccessbilityObserver){
        eventObservers.remove(eventAccessbilityObserver);
    }

    public String getCurrentWindowClassName() {
        return currentWindowClassName == null?"空":currentWindowClassName.toString();
    }

    public CharSequence getCurrentPackageName() {
        return currentPackageName;
    }

    AccessibilityNodeInfo lastRootActiveNodeInfo = null;
    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent){

        if (accessibilityEvent.getClassName()!=null){
            if (!accessibilityEvent.getClassName().toString().startsWith("android.widget")
            &&!accessibilityEvent.getClassName().toString().startsWith("androidx")){
                currentWindowClassName = accessibilityEvent.getClassName();
            }
        }

        if (accessibilityEvent.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED && accessibilityEvent.getPackageName()!=null){
            currentPackageName = accessibilityEvent.getPackageName();
        }

        if (accessibilityEvent.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED ||
                accessibilityEvent.getEventType() == AccessibilityEvent.TYPE_VIEW_FOCUSED) {
            AccessibilityNodeInfo root = super.getRootInActiveWindow();
            if (root != null) {
                lastRootActiveNodeInfo = root;
            }
        }


        for (AccessbilityObserver eventAccessbilityObserver:eventObservers){
            try {
                eventAccessbilityObserver.onAccessibilityEvent(accessibilityEvent);
            } catch (Throwable throwable) {
                RuntimeLog.i(throwable);
            }
        }


    }



    public AccessibilityNodeInfo getRootNode() {
        AccessibilityNodeInfo root = getRootInActiveWindow();
        if (root == null)
            return lastRootActiveNodeInfo;
        return root;
    }



    @Override
    public AccessibilityNodeInfo getRootInActiveWindow() {
        AccessibilityNodeInfo nodeInfo = super.getRootInActiveWindow();
        if (nodeInfo == null){
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                try {
                    List<AccessibilityWindowInfo> windowInfos = getWindows();
                    Log.d("xxxxxxxx",windowInfos.toString());
                    for (AccessibilityWindowInfo info :windowInfos){
                        if (info.getType() == TYPE_APPLICATION){
                            return info.getRoot();
                        }
                    }
                }catch (Exception e){}
            }
        }
        return nodeInfo;
    }

    @Override
    public void onInterrupt() {
        RuntimeLog.e("无障碍服务被中断");
    }



    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        RuntimeLog.d("无障碍服务已经连接启动");
        AccessbilityUtils.init(this);
        AppUtils.resumeApp();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        RuntimeLog.d("无障碍服务已经创建");
    }


    @Override
    protected boolean onKeyEvent(KeyEvent event) {
        for (AccessbilityObserver eventAccessbilityObserver:eventObservers){
            try {
                boolean returnValue = eventAccessbilityObserver.onKeyEvent(event);
                if (returnValue) {
                    return true;
                }
            } catch (Throwable throwable) {
                RuntimeLog.i(throwable);
            }
        }
        return super.onKeyEvent(event);
    }

    @Override
    public void onDestroy() {
        RuntimeLog.d("无障碍服务销毁，断开连接");
        AccessbilityUtils.service = null;
        eventObservers.clear();
        super.onDestroy();
    }



}
