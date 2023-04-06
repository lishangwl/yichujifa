package esqeee.xieqing.com.eeeeee.library.record;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import java.nio.file.Path;

import esqeee.xieqing.com.eeeeee.MyApp;
import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.bean.FloatWindow;
import esqeee.xieqing.com.eeeeee.bean.JSONArrayBean;
import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.service.AccessbilityUtils;
import esqeee.xieqing.com.eeeeee.view.PathAnimation;
import esqeee.xieqing.com.eeeeee.widget.CustomPath;
import esqeee.xieqing.com.eeeeee.widget.XQPathView;

public class AccessbilityRecorder implements XQPathView.OnGestureListener {

    public void close() {
        start.close();
        end.close();
        window.close();
    }

    public static interface RecorderListener{
        void onEnd(RecordFileBuilder builder);
    }


    private RecorderListener listener;

    public void setListener(RecorderListener listener) {
        this.listener = listener;
    }

    private long lastAcceptTime = System.currentTimeMillis();

    @Override
    public void onStart() {
        lastAcceptTime = System.currentTimeMillis() - lastAcceptTime;
    }

    @Override
    public void onFinished(CustomPath path, float endX, float endY) {
        builder.record(path,lastAcceptTime);
    }

    public class RecordFileBuilder{
        private SparseArray<RecordActionItem> recordActionItemSparseArray = new SparseArray<>();
        public void record(CustomPath path,long acceptTime){
            RecordActionItem item = new RecordActionItem(path,acceptTime);
            recordActionItemSparseArray.append(recordActionItemSparseArray.size(),item);

            animation(item);
        }



        public void reset(){
            recordActionItemSparseArray.clear();
        }

        public String buildString() {
            JSONBean jsonBean = new JSONBean();
            JSONArrayBean jsonArrayBean = new JSONArrayBean();
            for (int i = 0 ; i < recordActionItemSparseArray.size() ; i++){
                RecordActionItem item = recordActionItemSparseArray.valueAt(i);
                JSONBean param = new JSONBean()
                        .put("path",new JSONArrayBean().put(item.customPath.toBean()));
                JSONBean json = new JSONBean()
                        .put("actionType",42)
                        .put("witeTime",item.acceptTime)
                        .put("param",param);
                jsonArrayBean.put(json);
            }
            return jsonBean.put("actions",jsonArrayBean).toString();
        }
    }

    private void animation(RecordActionItem item) {
        showMessageView.setText("wait:"+item.acceptTime+"ms,continue:"+item.customPath.getDuration()+"ms");
        xqPathView.reset();
        showPathView.setVisibility(View.VISIBLE);
        xqPathView.setEnable(false);

        clearFoucas();

        xqPathView.postDelayed(()->{
            AccessbilityUtils.pressGestrue(item.customPath);
        },200);
        xqPathView.postDelayed(()->{

            showPathView.setVisibility(View.GONE);
            xqPathView.setEnable(true);
            requestFoucas();
            lastAcceptTime = System.currentTimeMillis();

        },item.customPath.getDuration()+200);
        new PathAnimation(showPathView, item.customPath, item.customPath.getDuration()).start();
    }

    private class RecordActionItem{
        private CustomPath customPath;
        private long acceptTime = 0;
        public RecordActionItem(CustomPath path,long acceptTime){
            this.acceptTime = acceptTime;
            this.customPath = path;
        }
    }


    private static AccessbilityRecorder recorder;
    private RecordFileBuilder builder;
    private Activity context;

    private AccessbilityRecorder(){
        builder = new RecordFileBuilder();
    }

    public static AccessbilityRecorder getRecorder() {
        if (recorder == null){
            recorder = new AccessbilityRecorder();
        }
        return recorder;
    }

    private boolean isRecording = false;
    public boolean isRecording(){
        return isRecording;
    }

    public void startRecord(Activity context){
        this.context = context;

        initWindowView(context);

        start.add();
    }

    public void requestFoucas(){
        WindowManager.LayoutParams layoutParams = window.getParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_FULLSCREEN| WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN|WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
        window.setParams(layoutParams);
        window.update();
    }
    public void clearFoucas(){
        WindowManager.LayoutParams layoutParams = window.getParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE| WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN|WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
        window.setParams(layoutParams);
        window.update();
    }

    private FloatWindow window;
    private FloatWindow start;
    private FloatWindow end;
    private View windowRecordView;
    private View showPathView;
    private TextView showMessageView;
    private XQPathView xqPathView;
    private void initWindowView(Context context) {
        windowRecordView = View.inflate(context, R.layout.accessbility_record,null);
        window = new FloatWindow.FloatWindowBuilder().id("AccessbilityRecorder")
                .move(false)
                .param(new WindowManager.LayoutParams(-1,-1,MyApp.getFloatWindowType(),WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE| WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN|WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                        ,-3))
                .with(windowRecordView).build();
        showPathView = windowRecordView.findViewById(R.id.showPath);
        xqPathView = windowRecordView.findViewById(R.id.xqPathView);
        showMessageView = windowRecordView.findViewById(R.id.showMessage);

        start = new FloatWindow.FloatWindowBuilder().id("AccessbilityRecorder_start")
                .move(true)
                .param(FloatWindow.DEFAULT)
                .with(View.inflate(context, R.layout.accessbility_record_start,null))
                .withClick(this::start)
                .build();

        end = new FloatWindow.FloatWindowBuilder().id("AccessbilityRecorder_end")
                .move(true)
                .param(FloatWindow.DEFAULT)
                .with(View.inflate(context, R.layout.accessbility_record_end,null))
                .withClick(this::end)
                .build();

        showPathView.setVisibility(View.GONE);
        xqPathView.setOnGestureListener(this);
    }

    private void start(View v){
        isRecording = true;
        start.close();


        window.add();
        end.add();
        builder.reset();
        lastAcceptTime = System.currentTimeMillis();
        showMessageView.setText("请在屏幕上滑动或点击以用于录制");
        requestFoucas();
    }

    private void end(View v){
        isRecording = false;
        end.close();
        start.add();
        window.close();

        if (listener!=null){
            listener.onEnd(builder);
        }
    }
}
