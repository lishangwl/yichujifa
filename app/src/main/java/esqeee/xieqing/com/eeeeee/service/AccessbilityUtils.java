package esqeee.xieqing.com.eeeeee.service;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;


import com.kongzue.dialog.v2.Notification;
import com.xieqing.codeutils.util.ScreenUtils;
import com.xieqing.codeutils.util.ThreadUtils;
import com.xieqing.codeutils.util.Utils;
import com.yicu.yichujifa.GlobalContext;

import java.util.ArrayList;
import java.util.List;

import esqeee.xieqing.com.eeeeee.bean.JSONArrayBean;
import esqeee.xieqing.com.eeeeee.doAction.core.ScaleMatrics;
import esqeee.xieqing.com.eeeeee.library.RuntimeLog;
import esqeee.xieqing.com.eeeeee.library.root.Shell;
import esqeee.xieqing.com.eeeeee.service.help.CheckerHelper;
import esqeee.xieqing.com.eeeeee.service.help.MediaProjectHelper;
import esqeee.xieqing.com.eeeeee.service.help.StopKeyHelper;
import esqeee.xieqing.com.eeeeee.widget.CustomPath;

public class AccessbilityUtils {
    public static XQMainAccess service;

    private static AccessibilityNodeInfo root;

    private static AccessibilityService.GestureResultCallback callback;

    public static XQMainAccess getService() {
        return service;
    }

    public static void init(XQMainAccess service2) {
        service = service2;

        service.addEventObserver(new MediaProjectHelper());
        service.addEventObserver(new StopKeyHelper());
        service.addEventObserver(new CheckerHelper());

        if (Build.VERSION.SDK_INT >= 24) {
            callback = new AccessibilityService.GestureResultCallback() {
                @Override
                public void onCompleted(GestureDescription gestureDescription) {
                    super.onCompleted(gestureDescription);
                }

                @Override
                public void onCancelled(GestureDescription gestureDescription) {
                    super.onCancelled(gestureDescription);
                }
            };
        } else {
            RuntimeLog.i("您的系统在7.0以下无法免root使用点击，长按等命令，应尽快升级系统或使设备root");
        }
    }

    public static List<AccessibilityNodeInfo> getAllNode() {
        List<AccessibilityNodeInfo> nodes = new ArrayList<>();
        if (root != null) {
            try {
                root.recycle();
                //有时候会报错 already in the pool 加上下面提示系统回收，并且加个容错万无一失
                root = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (service == null) {
            RuntimeLog.log("无障碍未开启！");
            return nodes;
        }
        AccessibilityNodeInfo nodeInfo = service.getRootInActiveWindow();
        if (nodeInfo != null) {
            root = nodeInfo;
            try {
                addNodeChilds(root, nodes);
            } catch (StackOverflowError error) {
                RuntimeLog.log(error);
            }
        }
        return nodes;
    }


    private static void addNodeChilds(AccessibilityNodeInfo nodeInfo, List<AccessibilityNodeInfo> nodes) {
        if (nodeInfo == null) {
            return;
        }
        if (nodeInfo != root && nodeInfo != null) {
            nodes.add(nodeInfo);
        }
        for (int i = 0; i < nodeInfo.getChildCount(); i++) {
            //浏览器会一直到栈区溢出
            //加个判断
            if (nodeInfo != nodeInfo.getChild(i)) {
                try {
                    addNodeChilds(nodeInfo.getChild(i), nodes);
                } catch (Throwable e) {
                    RuntimeLog.log(e);
                }
            }
        }
    }


    /*
     *   获取node 根据text
     * */
    public static AccessibilityNodeInfo findNodeByText(String string) {
        List<AccessibilityNodeInfo> nodes = AccessbilityUtils.getAllNode();
        for (int i = 0; i < nodes.size(); i++) {
            AccessibilityNodeInfo nodeInfo = nodes.get(i);
            if (nodeInfo.getText() == null || nodeInfo.getText().toString().equals("")) {
                recyleNodes(nodeInfo);
                continue;
            }
            if (nodeInfo.getText().toString().contains(string)) {
                return nodeInfo;
            }
        }
        return null;
    }

    /*
     *   获取所有的node 根据text
     * */
    public static List<AccessibilityNodeInfo> findNodesByText(String string) {
        List<AccessibilityNodeInfo> nodes = AccessbilityUtils.getAllNode();
        List<AccessibilityNodeInfo> find = new ArrayList<>();
        for (int i = 0; i < nodes.size(); i++) {
            AccessibilityNodeInfo nodeInfo = nodes.get(i);
            if (nodeInfo.getText() == null || nodeInfo.getText().toString().trim().length() == 0) {
                recyleNodes(nodeInfo);
                continue;
            }
            Log.d("AccessbilityUtils", nodeInfo.getText().toString());
            if (nodeInfo.getText().toString().contains(string)) {
                find.add(nodeInfo);
            }
        }
        return find;
    }

    /*
     *   模拟按键
     * */
    public static void performGlobalAction(int keyCode) {
        if (service == null) {
            RuntimeLog.log("无障碍未开启！");
            return;
        }
        service.performGlobalAction(keyCode);
    }


    /*
     * 指定区域中是否有关键词符合的节点
     * */
    public static AccessibilityNodeInfo findNodeByTextFromRect(String text2, Rect rect) {
        List<AccessibilityNodeInfo> nodeInfos = findNodesByText(text2);
        //LogUtils.aTag("AccessbilityUtils",rect.left,rect.bottom,rect.right,rect.top);
        for (int i = 0; i < nodeInfos.size(); i++) {
            AccessibilityNodeInfo node = nodeInfos.get(i);
            Rect nodeRect = new Rect();
            node.getBoundsInScreen(nodeRect);

            boolean isMatched = true;
            if (nodeRect.right < rect.left || rect.right < nodeRect.left) {
                isMatched = false;
            }
            if (nodeRect.top > rect.bottom || rect.top > nodeRect.bottom) {
                isMatched = false;
            }

            if (isMatched) {
                return node;
            } else {
                recyleNodes(node);
            }
        }
        return null;
    }


    public static String findNodesByRectToString(Rect rect) {
        List<AccessibilityNodeInfo> nodes = AccessbilityUtils.getAllNode();
        if (rect == null) {
            StringBuilder stringBuilder = new StringBuilder();
            for (AccessibilityNodeInfo nodeInfo : nodes) {
                if (nodeInfo.getText() == null) continue;
                stringBuilder.append(nodeInfo.getText()).append("\n");
            }
            return stringBuilder.toString();
        }
        List<AccessibilityNodeInfo> nodeInfos = new ArrayList<>();
        for (int i = 0; i < nodes.size(); i++) {
            AccessibilityNodeInfo node = nodes.get(i);
            Rect nodeRect = new Rect();
            node.getBoundsInScreen(nodeRect);

            boolean isMatched = true;
            if (nodeRect.right < rect.left || rect.right < nodeRect.left) {
                isMatched = false;
            }
            if (nodeRect.top > rect.bottom || rect.top > nodeRect.bottom) {
                isMatched = false;
            }

            if (isMatched) {
                nodeInfos.add(node);
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (AccessibilityNodeInfo nodeInfo : nodeInfos) {
            if (nodeInfo.getText() == null) continue;
            stringBuilder.append(nodeInfo.getText()).append("\n");
        }
        return stringBuilder.toString();
    }

    /*
     * 设置node内容
     * */
    public static void setNodeText(AccessibilityNodeInfo nodeInfo, String text) {
        if (nodeInfo == null) {
            return;
        }
        Bundle arguments = new Bundle();
        arguments.putCharSequence(AccessibilityNodeInfoCompat.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, text);
        nodeInfo.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
    }

    /*
     * 根据xy获取node
     * */
    public static List<AccessibilityNodeInfo> findNodesByXY(int x, int y) {
        List<AccessibilityNodeInfo> nodes = AccessbilityUtils.getAllNode();
        List<AccessibilityNodeInfo> find = new ArrayList<>();
        for (int i = 0; i < nodes.size(); i++) {
            AccessibilityNodeInfo nodeInfo = nodes.get(i);
            Rect outBounds = new Rect();
            nodeInfo.getBoundsInScreen(outBounds);
            int left = outBounds.left;
            int right = outBounds.right;
            int top = outBounds.top;
            int bottom = outBounds.bottom;
            if (x >= left && x <= right && y >= top && y <= bottom) {
                find.add(nodeInfo);
            } else {
                recyleNodes(nodeInfo);
            }
        }
        return find;
    }

    /*
     * 根据xy获取可以编辑的node
     * */
    public static AccessibilityNodeInfo findEditNodeByXY(int x, int y) {
        List<AccessibilityNodeInfo> nodeInfos = findNodesByXY(x, y);
        if (nodeInfos.size() > 0) {
            for (AccessibilityNodeInfo nodeInfo : nodeInfos) {
                if (nodeInfo.isEditable()) {
                    return nodeInfo;
                } else {
                    recyleNodes(nodeInfo);
                }
            }
        }
        return null;
    }

    /*
     *   点击坐标
     * */
    public static boolean touch(GestureDescription.StrokeDescription[] strokeDescriptions) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            if (service == null) {
                RuntimeLog.log("无障碍未开启！");
                return false;
            }

            GestureDescription.Builder builder = new GestureDescription.Builder();
            for (GestureDescription.StrokeDescription strokeDescription : strokeDescriptions) {
                builder.addStroke(strokeDescription);
            }
            GestureDescription gestureDescription = builder.build();
            return service.dispatchGesture(gestureDescription, callback, null);
        } else {
            RuntimeLog.log("无法使用点击，系统在7.0以下");
        }
        return false;
    }

    /*
     *   点击坐标
     * */
    public static boolean clickXY(int x, int y) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            if (service == null) {
                RuntimeLog.log("无障碍未开启！");
                return false;
            }
            if (x < 0) {
                x = 0;
            }
            if (x > ScreenUtils.getScreenWidth()) {
                x = ScreenUtils.getScreenWidth();
            }
            if (y > ScreenUtils.getScreenHeight()) {
                y = ScreenUtils.getScreenHeight();
            }
            if (y < 0) {
                y = 0;
            }
            Path path = new Path();
            path.moveTo(x, y);
            GestureDescription gestureDescription = new GestureDescription.Builder()
                    .addStroke(new GestureDescription.StrokeDescription(path, 0, 10))
                    .build();
            boolean b = service.dispatchGesture(gestureDescription, callback, null);
            ThreadUtils.sleep(10);
            return b;
        } else {
            RuntimeLog.log("无法使用点击，系统在7.0以下");
        }
        return false;
    }


    /*
     *   长按坐标处
     * */
    public static boolean touchXY(int x, int y, int time) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            if (service == null) {
                RuntimeLog.log("无障碍未开启！");
                return false;
            }
            if (x < 0) {
                x = 0;
            }
            if (x > ScreenUtils.getScreenWidth()) {
                x = ScreenUtils.getScreenWidth();
            }
            if (y > ScreenUtils.getScreenHeight()) {
                y = ScreenUtils.getScreenHeight();
            }
            if (y < 0) {
                y = 0;
            }
            GestureDescription.Builder builder = new GestureDescription.Builder();
            Path path = new Path();
            path.moveTo(x, y);
            GestureDescription gestureDescription = builder
                    .addStroke(new GestureDescription.StrokeDescription(path, 0, time))
                    .build();
            return service.dispatchGesture(gestureDescription, callback, null);
        } else {
            RuntimeLog.log("无法使用长按，系统在7.0以下");
        }
        return false;
    }

    /*
     *   长按坐标处
     * */
    public static boolean longClickXY(int x, int y) {
        touchXY(x, y, 2000);
        return false;
    }

    public static void pressSwipLocation(int x0, int y0, int x1, int y1, long time) {
        if (Build.VERSION.SDK_INT >= 24) {
            if (service == null) {
                RuntimeLog.log("无障碍未开启！");
                return;
            }
            GestureDescription.Builder builder = new GestureDescription.Builder();
            Path p = new Path();
            p.moveTo((float) x0, (float) y0);
            p.lineTo((float) x1, (float) y1);
            builder.addStroke(new GestureDescription.StrokeDescription(p, 0, time));
            service.dispatchGesture(builder.build(), callback, null);
        } else {
            RuntimeLog.log("无法使用滑动，系统在7.0以下");
        }
    }

    public static void pressSwipLocation(int x0, int y0, int x1, int y1) {
        pressSwipLocation(x0, y0, x1, y1, 500);
    }

    public static void pressLocation(Path path, long time) {
        if (Build.VERSION.SDK_INT >= 24) {
            if (service == null) {
                RuntimeLog.log("无障碍未开启！");
                return;
            }
            if (time < 0) {
                time = 500;
            }
            if (time < 50) {
                time = 50;
            }
            GestureDescription.Builder builder = new GestureDescription.Builder();
            builder.addStroke(new GestureDescription.StrokeDescription(path, 0, time));
            service.dispatchGesture(builder.build(), callback, null);
        } else {
            RuntimeLog.log("无法使用手势，系统在7.0以下");
        }
    }

    public static boolean pressGestrue(JSONArrayBean customPaths) {
        if (customPaths == null) {
            return false;
        }
        List<CustomPath> customPaths1 = new ArrayList<>();
        for (int i = 0; i < customPaths.length(); i++) {
            CustomPath customPath = CustomPath.from(customPaths.getJson(i));
            if (customPath == null) {
                return false;
            }
            customPaths1.add(customPath);
        }
        return pressGestrue(customPaths1);
    }

    public static boolean pressGestrue(JSONArrayBean customPaths, ScaleMatrics scaleMatrics) {
        if (customPaths == null) {
            return false;
        }
        List<CustomPath> customPaths1 = new ArrayList<>();
        for (int i = 0; i < customPaths.length(); i++) {
            CustomPath customPath = CustomPath.from(customPaths.getJson(i), scaleMatrics);
            if (customPath == null) {
                return false;
            }
            customPaths1.add(customPath);
        }
        return pressGestrue(customPaths1);
    }

    public static boolean pressGestrue(final List<CustomPath> customPaths) {
        if (Build.VERSION.SDK_INT >= 24) {
            if (service == null) {
                RuntimeLog.log("无障碍未开启！");
                return false;
            }

            GestureDescription.Builder builder = new GestureDescription.Builder();
            for (CustomPath path : customPaths) {
                long time = path.getDuration();
                if (time < 0) {
                    time = 500;
                }
                if (time < 50) {
                    time = 50;
                }
                try {
                    builder.addStroke(new GestureDescription.StrokeDescription(path, 0, time));
                } catch (IllegalStateException e) {
                    RuntimeLog.log("执行失败，手势执行触摸太多！");
                    return false;
                }
            }
            return service.dispatchGesture(builder.build(), new AccessibilityService.GestureResultCallback() {
                @Override
                public void onCompleted(GestureDescription gestureDescription) {
                    super.onCompleted(gestureDescription);
                    customPaths.clear();
                    //System.gc();
                }

                @Override
                public void onCancelled(GestureDescription gestureDescription) {
                    super.onCancelled(gestureDescription);
                    customPaths.clear();
                    //System.gc();
                }
            }, null);
        } else {
            RuntimeLog.log("无法使用手势，系统在7.0以下");
        }
        return false;
    }

    public static boolean pressGestrue(CustomPath customPath) {
        if (Build.VERSION.SDK_INT >= 24) {
            if (service == null) {
                RuntimeLog.log("无障碍未开启！");
                return false;
            }

            GestureDescription.Builder builder = new GestureDescription.Builder();
            long time = customPath.getDuration();
            if (time < 0) {
                time = 500;
            }
            if (time < 50) {
                time = 50;
            }
            try {
                builder.addStroke(new GestureDescription.StrokeDescription(customPath, 0, time));
            } catch (IllegalStateException e) {
                RuntimeLog.log("执行失败，手势执行触摸太多！");
                return false;
            }
            return service.dispatchGesture(builder.build(), callback, null);
        } else {
            RuntimeLog.log("无法使用手势，系统在7.0以下");
        }
        return false;
    }

    public static boolean clickNode(AccessibilityNodeInfo accessibilityNodeInfo) {
        if (accessibilityNodeInfo == null) {
            return false;
        }
        Rect rect = new Rect();
        accessibilityNodeInfo.getBoundsInScreen(rect);
        int x = rect.left + rect.width() / 2, y = rect.top + rect.height() / 2;
        return clickXY(x, y);
    }

    public static boolean longClickNode(AccessibilityNodeInfo accessibilityNodeInfo) {
        if (accessibilityNodeInfo == null) {
            return false;
        }
        Rect rect = new Rect();
        accessibilityNodeInfo.getBoundsInScreen(rect);
        int x = rect.left + rect.width() / 2, y = rect.top + rect.height() / 2;
        return longClickXY(x, y);
    }


    /*
     *   无障碍是否开启
     */
    public static boolean isAccessibilitySettingsOn(Context mContext) {
        if (service == null) {
            return false;
        }
        int accessibilityEnabled = 0;
        final String service = mContext.getPackageName() + "/" + XQMainAccess.class.getName();
        boolean accessibilityFound = false;
        try {
            accessibilityEnabled = Settings.Secure.getInt(mContext.getApplicationContext().getContentResolver(), android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException e) {
            return false;
        }
        TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');
        if (accessibilityEnabled == 1) {
            String settingValue = Settings.Secure.getString(mContext.getApplicationContext().getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                TextUtils.SimpleStringSplitter splitter = mStringColonSplitter;
                splitter.setString(settingValue);
                while (splitter.hasNext()) {
                    String accessabilityService = splitter.next();
                    if (accessabilityService.equalsIgnoreCase(service)) {
                        return true;
                    }
                }
            }
        }
        return accessibilityFound;
    }

    public static void open() {
        Shell.getShell().su();
        Shell.getShell().exce("settings put secure enabled_accessibility_services " + Utils.getApp().getPackageName() + "/" + XQMainAccess.class.getName());
        Shell.getShell().exce("settings put secure accessibility_enabled 1");
    }

    public static List<AccessibilityNodeInfo> findNodesByClass(String text) {
        List<AccessibilityNodeInfo> nodeInfos = getAllNode();
        List<AccessibilityNodeInfo> nodes = new ArrayList<>();
        if (nodeInfos.size() > 0) {
            for (AccessibilityNodeInfo nodeInfo : nodeInfos) {
                CharSequence className = nodeInfo.getClassName();
                if (className != null) {
                    if (className.toString().equals(text)) {
                        nodes.add(nodeInfo);
                        continue;
                    }
                }
                recyleNodes(nodeInfo);
            }
        }
        return nodes;
    }

    public static List<AccessibilityNodeInfo> findNodesByDescription(String text) {
        List<AccessibilityNodeInfo> nodeInfos = getAllNode();
        List<AccessibilityNodeInfo> nodes = new ArrayList<>();
        if (nodeInfos.size() > 0) {
            for (AccessibilityNodeInfo nodeInfo : nodeInfos) {
                CharSequence contentDescription = nodeInfo.getContentDescription();
                if (contentDescription != null) {
                    if (contentDescription.toString().equals(text)) {
                        nodes.add(nodeInfo);
                        continue;
                    }
                }
                recyleNodes(nodeInfo);
            }
        }
        return nodes;
    }


    public static void toSetting() {
        try {
            ThreadUtils.runOnUiThread(() -> {
                Notification.build(GlobalContext.getContext(), 0, "", "请先开启无障碍", 1000, Color.RED).showDialog();
            });
            Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            GlobalContext.getContext().startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void recyleNodes(List<AccessibilityNodeInfo> nodeInfos) {
        try {
            for (AccessibilityNodeInfo nodeInfo : nodeInfos) {
                recyleNodes(nodeInfo);
            }
        } catch (Throwable e) {
            RuntimeLog.log(e);
        }
    }

    private static void recyleNodes(AccessibilityNodeInfo nodeInfo) {
        try {
            nodeInfo.recycle();
        } catch (Throwable e) {
            RuntimeLog.log(e);
        }
    }

    public static String getCurrentWindowClassName() {
        return service.getCurrentWindowClassName();
    }
}
