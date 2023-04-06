package esqeee.xieqing.com.eeeeee.service.help;

import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.yicu.yichujifa.GlobalContext;

import java.util.ArrayList;
import java.util.List;

import esqeee.xieqing.com.eeeeee.action.ActionRunHelper;
import esqeee.xieqing.com.eeeeee.library.RuntimeLog;
import esqeee.xieqing.com.eeeeee.manager.AppActivityCheckManager;
import esqeee.xieqing.com.eeeeee.manager.CheckTextManager;
import esqeee.xieqing.com.eeeeee.service.AccessbilityObserver;
import esqeee.xieqing.com.eeeeee.service.AccessbilityUtils;
import esqeee.xieqing.com.eeeeee.sql.model.App;
import esqeee.xieqing.com.eeeeee.sql.model.TextCheck;

import static esqeee.xieqing.com.eeeeee.service.AccessbilityUtils.getCurrentWindowClassName;

public class CheckerHelper extends AccessbilityObserver {

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) throws Throwable {
        switch (accessibilityEvent.getEventType()) {
            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
                new Thread(()->{
                    try {

                        checkScreenText();
                    }catch (Exception e){

                    }
                }).start();
                break;
            case AccessibilityEvent.TYPE_WINDOWS_CHANGED:
                new Thread(()->{
                    try {

                        checkAppActivity();
                    }catch (Exception e){

                    }
                }).start();
                break;
        }
    }


    private boolean checkIsCanDo(TextCheck check, String message) {
        String[] keys = check.keys.split(",.split.,");
        for (String key:keys){
            if (message.contains(key)){
                return true;
            }
        }
        return false;
    }
    /*

     *   扫描窗口，加进程锁，防止溢出
     * */
    boolean isCheckAppActivity = false;
    public  void checkAppActivity() {
        if (isCheckAppActivity){
            return;
        }
        isCheckAppActivity = true;
        List<App> appList = AppActivityCheckManager.getManager().queryList();
        for (App check : appList){
            if (check.activityName.equals(getCurrentWindowClassName())){
                ActionRunHelper.startAction(GlobalContext.getContext(),check.path);
            }
        }
        appList.clear();
        isCheckAppActivity = false;
    }

    /*
     *   扫描屏幕文字，加进程锁，防止溢出
     * */
    boolean isChecked = false;
    public void checkScreenText() {
        if (isChecked){
            return;
        }
        isChecked = true;
        List<AccessibilityNodeInfo> nodes = getAllNode();
        for (int i = 0 ; i< nodes.size() ; i++){
            AccessibilityNodeInfo nodeInfo = nodes.get(i);
            if (nodeInfo != null && nodeInfo.getText()!=null){
                String nMessage = nodeInfo.getText().toString();

                List<TextCheck> checkList = CheckTextManager.getManager().queryList();
                for (TextCheck check : checkList){
                    if (checkIsCanDo(check,nMessage)){
                        ActionRunHelper.startAction(GlobalContext.getContext(),check.path);
                    }
                }
                checkList.clear();
            }
        }
        isChecked = false;
    }

    @Override
    public boolean onKeyEvent(KeyEvent event) throws Throwable {
        return false;
    }

    public List<AccessibilityNodeInfo> getAllNode(){
        List<AccessibilityNodeInfo> nodes = new ArrayList<>();
        if (AccessbilityUtils.service == null){
            return nodes;
        }
        AccessibilityNodeInfo nodeInfo = AccessbilityUtils.service.getRootInActiveWindow();
        if (nodeInfo!=null){
            try {
                addNodeChilds(nodeInfo,nodes);
            }catch (StackOverflowError error){
                RuntimeLog.log(error);
            }
        }
        return nodes;
    }


    private void addNodeChilds(AccessibilityNodeInfo nodeInfo,List<AccessibilityNodeInfo> nodes) {
        if (nodeInfo == null){
            return;
        }
        nodes.add(nodeInfo);
        for (int i = 0; i < nodeInfo.getChildCount(); i++){
            //浏览器会一直到栈区溢出
            //加个判断
            if (nodeInfo != nodeInfo.getChild(i)){
                try {
                    addNodeChilds(nodeInfo.getChild(i),nodes);
                }catch (Throwable e){
                    RuntimeLog.log(e);
                }
            }
        }
    }
}