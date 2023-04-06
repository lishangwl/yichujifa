package com.yicu.yichujifa.LayoutHierarchy;

import android.graphics.Rect;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.ArrayList;
import java.util.List;

public class NodeHelper {
    public static void recyle(AccessibilityNodeInfo nodeInfo){
        if (nodeInfo == null){
            return;
        }
        if (nodeInfo.getChildCount()>0){
            for (int i=0;i<nodeInfo.getChildCount();i++){
                recyle(nodeInfo.getChild(i));
            }
        }
        try {
            nodeInfo.recycle();
        }catch (Exception e){}
    }

    public static Rect getBounds(AccessibilityNodeInfo nodeInfo){
        if (nodeInfo == null){
            return null;
        }
        Rect rect = new Rect();
        nodeInfo.getBoundsInScreen(rect);
        return rect;
    }

    public static Rect getBounds(AccessibilityNodeInfoCompat nodeInfo){
        if (nodeInfo == null){
            return null;
        }
        Rect rect = new Rect();
        nodeInfo.getBoundsInScreen(rect);
        return rect;
    }


    public static List<AccessibilityNodeInfo> getChildrens(AccessibilityNodeInfo nodeInfo){
        if (nodeInfo == null){
            return null;
        }
        List<AccessibilityNodeInfo> childrens = new ArrayList<>();
        for (int i = 0;i<nodeInfo.getChildCount();i++){
            childrens.add(nodeInfo.getChild(i));
        }
        return childrens;
    }

    public static void captrues(List<AccessibilityNodeInfo> nodeInfos,AccessibilityNodeInfo nodeInfo){
        if (nodeInfo == null){
            return;
        }
        for (int i = 0;i<nodeInfo.getChildCount();i++){
            captrues(nodeInfos,nodeInfo.getChild(i));
        }
        nodeInfos.add(nodeInfo);
    }

    public static int getIndexInParent(AccessibilityNodeInfo nodeInfo){
        if (nodeInfo == null){
            return 0;
        }
        AccessibilityNodeInfo parent = nodeInfo.getParent();
        if (parent == null){
            return 0;
        }
        for (int i = 0; i<parent.getChildCount();i++){
            if (parent.getChild(i) == nodeInfo){
                return i;
            }
        }
        return 0;
    }

    public static Rect getBoundsInParent(AccessibilityNodeInfoCompat nodeInfo) {
        if (nodeInfo == null){
            return null;
        }
        Rect rect = new Rect();
        nodeInfo.getBoundsInParent(rect);
        return rect;
    }
}
