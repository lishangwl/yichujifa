package com.yicu.yichujifa.LayoutHierarchy;

import android.view.KeyEvent;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityNodeInfo;

import com.xieqing.codeutils.util.ToastUtils;
import com.yicu.yichujifa.GlobalContext;
import com.yicu.yichujifa.LayoutHierarchy.widget.LayoutBoundsView;
import com.yicu.yichujifa.LayoutHierarchy.widget.NodeInfo;
import com.yicu.yichujifa.LayoutHierarchy.widget.NodeInfoView;

import esqeee.xieqing.com.eeeeee.MyApp;
import esqeee.xieqing.com.eeeeee.bean.FloatWindow;
import esqeee.xieqing.com.eeeeee.service.AccessbilityUtils;
import esqeee.xieqing.com.eeeeee.widget.FDialog;

public class BoundsLayoutHierarchy extends FloatWindow {

    private LayoutBoundsView rootView;

    public BoundsLayoutHierarchy(){
        rootView = new LayoutBoundsView(GlobalContext.getContext()){
            @Override
            public boolean dispatchKeyEvent(KeyEvent event) {
                if(event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                    close();
                    return true;
                }
                return false;
            }
        };
        setId("ListLayoutHierarchy");
        setAllowMove(false);
        setParams(new WindowManager.LayoutParams(-1,-1, MyApp.getFloatWindowType(),WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,-3));
        setView(rootView);

        rootView.setOnNodeInfoSelectListener(info -> {
            NodeInfo mSelectedNode = info;
            showInfo(mSelectedNode,0);
        });
        rootView.getBoundsPaint().setStrokeWidth(2f);
    }


    FDialog nodeInfo;
    NodeInfoView nodeInfoView;

    @Override
    public void close() {
        super.close();
        NodeHelper.recyle(rootNode);
    }

    private AccessibilityNodeInfo rootNode;

    @Override
    public void add() {
        super.add();
        rootNode = AccessbilityUtils.getService().getRootNode();
        if (rootNode == null){
            close();
            ToastUtils.showLong("分析屏幕控件失败，请重新切换页面再尝试");
        }else{
            rootView.setRootNode(NodeInfo.capture(rootNode));
        }
    }


    private void showInfo(NodeInfo node, int index) {
        if (nodeInfo == null){
            nodeInfoView = new NodeInfoView(GlobalContext.getContext());
            nodeInfo = new FDialog(GlobalContext.getContext())
                    .setTitle("控件属性(点击复制)")
                    .setCanfirm("",null)
                    .addView(nodeInfoView);
        }
        nodeInfoView.setNodeInfo(node,index);
        nodeInfo.show();
    }

}
