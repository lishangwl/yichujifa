package esqeee.xieqing.com.eeeeee.action;

import android.content.Context;
import android.view.OrientationEventListener;

import com.xieqing.codeutils.util.FileUtils;
import com.xieqing.codeutils.util.ScreenUtils;
import com.zhihu.matisse.compress.FileUtil;

import esqeee.xieqing.com.eeeeee.doAction.Base;
import esqeee.xieqing.com.eeeeee.bean.JSONArrayBean;
import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.doAction.core.ScaleMatrics;
import esqeee.xieqing.com.eeeeee.library.MathResult;
import esqeee.xieqing.com.eeeeee.library.RuntimeLog;
import esqeee.xieqing.com.eeeeee.library.captrue.AutoCaptruer;

public class ActionRun extends Thread{
    private Context context;
    private Action action;
    private boolean isStop = true;
    private long startTime ;
    private AutoCaptruer captruer;
    private ActionRunDo runDo;
    private OrientationEventListener mOrientationEventListener;
    private int orientation;

    public Action getAction() {
        return action;
    }

    public long getRunTime() {
        return System.currentTimeMillis() - startTime;
    }

    public Context getContext() {
        return context;
    }

    public boolean isStop() {
        return isStop;
    }

    public ActionRun(Action action, Context context){
        this.context = context;
        this.action = action;
        this.captruer = new AutoCaptruer(this,context);
        runDo = new ActionRunDo();
        mOrientationEventListener = new OrientationEventListener(context) {
            @Override
            public void onOrientationChanged(int orientation) {
                ActionRun.this.orientation = orientation;
            }
        };
    }

    public int getOrientation() {
        return orientation;
    }

    public AutoCaptruer getCaptruer() {
        return captruer;
    }

    public static class Block{
        public String blockName = "main";
        public Block parent;
        private boolean breaked = false;
        private JSONArrayBean jsonArray;
        private ActionRun actionRun;
        private JSONArrayBean variables = new JSONArrayBean();

        public JSONArrayBean getVariables() {
            return variables;
        }

        public Block(String blockName, JSONArrayBean jsonArray, ActionRun actionRun, Block parent){
            this.blockName = blockName;
            this.jsonArray = jsonArray;
            this.actionRun = actionRun;
            this.parent = parent;
            this.variables = parent == null?null:parent.variables;
        }
        public Block(String blockName,JSONArrayBean jsonArray,ActionRun actionRun,JSONArrayBean variables){
            this.blockName = blockName;
            this.jsonArray = jsonArray;
            this.actionRun = actionRun;
            this.variables = variables;
        }
        public Block(String blockName,JSONArrayBean jsonArray,ActionRun actionRun,JSONArrayBean variables,Block parent){
            this.blockName = blockName;
            this.jsonArray = jsonArray;
            this.parent = parent;
            this.actionRun = actionRun;
            this.variables = variables;
        }

        public boolean isBreaked() {
            return breaked;
        }

        public void breaked(){
            breaked = true;
            if (blockName.equals("while") || blockName.equals("for")){
            }else{
                if (parent!=null){
                    parent.breaked();
                }
            }
        }

        public void returned(){
            breaked = true;
            if (parent!=null){
                parent.returned();
            }
        }

        public void exceute(){
            if (jsonArray == null){
                return;
            }
            for (int j = 0; j < jsonArray.length() && !actionRun.isStop() && !breaked; j++) {
                JSONBean a = jsonArray.getJson(j);
                if (a.getInt("actionType") == 76 && a.getBoolean("status",true)){
                    breaked();
                    return;
                }
                if (a.getInt("actionType") == 82 && a.getBoolean("status",true)){
                    returned();
                    return;
                }
                actionRun.post(a,this);
            }
        }
    }



    /*
    *   doAction
    *   开始执行所有动作
    * */

    ScaleMatrics scaleMatrics;

    public ScaleMatrics getScaleMatrics() {
        return scaleMatrics;
    }

    public void doAction(Action action) {
        try {
            //执行次数如果为0 就一直循环执行，除非暂停
            if (action == null || action.getData() == null){
                return;
            }
            JSONBean data = new JSONBean(action.getData());

            Block main;
            if(data.getBoolean("scale",false)){
                scaleMatrics = new ScaleMatrics(data.getInt("scale_width"),data.getInt("scale_height"));
                ScaleMatrics.initIfNeeded(context);
            }else {
                scaleMatrics = null;
            }

            main = new Block("main",data.getArray("actions"),this,data.getArray("variables"),null);
            for (int i = 0; (i < action.getRepeat() || action.getRepeat() == 0) && !isStop && !main.isBreaked(); i++) {
                main.exceute();
            }
        }catch (Throwable e){
            RuntimeLog.e(e);
            if(e.getMessage().contains("opencv")){
                RuntimeLog.e("建议重启手机解决该问题");
            }
        }
    }

    public ActionRunDo getRunDo() {
        return runDo;
    }

    public int scaleMatriacsX(int value){
        if (scaleMatrics == null){
            //RuntimeLog.log("不缩放");
            return value;
        }else {
            return scaleMatrics.scaleX(value,orientation);
        }
    }

    public int scaleMatriacsY(int value){
        if (scaleMatrics == null){
            return value;
        }else {
            return scaleMatrics.scaleY(value,orientation);
        }
    }

    /*
    *   执行一个动作
    * */
    private void post(JSONBean a,Block block) {
        if (!a.getBoolean("status",true)){
            return;
        }
        int type = a.getInt("actionType");
        try {
            if (!getRunDo().post(type,a,this,block)){
                stopDo();
                return;
            }

            if (!getRunDo().post(-1,a,this,block)){
                stopDo();
                return;
            }
        }catch (Exception e) {
            e.printStackTrace();
            RuntimeLog.e("action["+type+"] error at:");
            RuntimeLog.e(e);
            stopDo();
        }

    }

    public void start(){
        new Thread(()->{
            run();
        }).start();
    }

    public void log(Object object){
        if (action.logAble()){
            RuntimeLog.log(object);
        }
    }

    public void run() {
        RuntimeLog.log("<"+ FileUtils.getFileNameNoExtension(action.getPath()) +">开始执行");
        startTime = System.currentTimeMillis();
        isStop = false;
        doAction(action);
        isStop = true;
        ActionRunHelper.stopAction(this);
        captruer.release();
        getRunDo().reslese();
        mOrientationEventListener.disable();
        RuntimeLog.log("<"+ FileUtils.getFileNameNoExtension(action.getPath()) +">执行完毕，耗时："+(System.currentTimeMillis() - startTime)+"ms");
    }


    public void stopDo() {
        isStop = true;
    }
}
