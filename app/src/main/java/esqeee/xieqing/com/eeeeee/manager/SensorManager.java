package esqeee.xieqing.com.eeeeee.manager;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import androidx.core.app.ActivityCompat;
import android.util.Log;
import android.view.WindowManager;

import com.xieqing.codeutils.util.AppUtils;
import com.xieqing.codeutils.util.PhoneUtils;
import com.xieqing.codeutils.util.SPUtils;
import com.xieqing.codeutils.util.Utils;

import esqeee.xieqing.com.eeeeee.action.Action;
import esqeee.xieqing.com.eeeeee.action.ActionHelper;
import esqeee.xieqing.com.eeeeee.action.ActionRunHelper;
import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.doAction.DoActionBean;
import esqeee.xieqing.com.eeeeee.library.RuntimeLog;

import static android.content.Context.SENSOR_SERVICE;
import static android.content.Context.WINDOW_SERVICE;

public class SensorManager  implements SensorEventListener {
    private static SensorManager manager;
    private Context context;


    private android.hardware.SensorManager sensorManager;
    private Sensor sensor;

    public SensorManager() {
        this.context = Utils.getApp();

        initSerso();
    }

    private boolean isRegister = false;

    public boolean isRegister() {
        return isRegister;
    }

    public boolean isCanUseSensor(){
        return sensorManager != null && sensor != null;
    }

    private void initSerso() {
        sensorManager = (android.hardware.SensorManager) context.getSystemService(SENSOR_SERVICE);
        if (sensorManager!=null){
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        }
    }
    public void register(){
        if (isRegister){
            return;
        }
        if (!isCanUseSensor()){
            RuntimeLog.log("SensorManager: 设备不支持摇动传感器");
            return;
        }
        isRegister = sensorManager.registerListener(this, sensor, android.hardware.SensorManager.SENSOR_DELAY_UI);
        RuntimeLog.log("SensorManager: Sensor is register :"+isRegister+"=>注册摇动传感器监听");
    }
    public void unregister(){
        if (!isRegister){
            return;
        }
        isRegister = false;
        sensorManager.unregisterListener(this);
        RuntimeLog.log("SensorManager:Sensor has unregistered!=>注销摇动传感器监听");
    }

    public static SensorManager getManager() {
        if (manager == null){
            manager = new SensorManager();
        }
        return manager;
    }


    private long gestureInterval = 700000000;
    private long lastGestureStamp = 0;

    private String getGesture(float x, float y, float z) {
        float ax = Math.abs(x);
        float ay = Math.abs(y);
        float az = Math.abs(z);
        if (((double) ax) >= 0.99d || ((double) ay) >= 0.99d || ((double) az) >= 0.99d) {
            try {
                double thresh = ((((double) SPUtils.getInstance().getInt("motion_thresh",10)) / 60.0d) * 8.0d) + 1.0d;
                if (((double) ay) > thresh && ay > (ax + az) * this.ratioThresh) {
                    return "y";
                }
                if (((double) ax) > thresh && ax > (ay + az) * this.ratioThresh) {
                    return "x";
                }
                if (((double) az) > thresh && az > (ax + ay) * this.ratioThresh) {
                    return "z";
                }
            } catch (Exception e) {
                Log.e("gesture", e.getMessage());
            }
        }
        return null;
    }




    public  boolean isPortrait() {
        int currentRatation = ((WindowManager) context.getSystemService(WINDOW_SERVICE)).getDefaultDisplay().getRotation();
        if (1 == currentRatation || 3 == currentRatation) {
            return false;
        }
        return true;
    }



    private float motionThresh =1f;
    private float ratioThresh = 1.0f;


    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == 4) {
            long stamp = System.nanoTime();
            String gesture = getGesture(event.values[0], event.values[1], event.values[2]);
            if (gesture != null && stamp - this.lastGestureStamp > this.gestureInterval) {
                this.lastGestureStamp = stamp;
                doSensorAction(gesture);
            }
        }
    }






    private void doSensorAction(String gesture) {
        if (SPUtils.getInstance().getBoolean("isOpenWDDong",true)){
            PhoneUtils.vibrateShort();
        }
        JSONBean bean = new JSONBean(SPUtils.getInstance().getString(gesture+"_wd"));
        JSONBean param = bean.getJson("param");
        int type = bean.getInt("actionType",-1);
        if (type == -1){
            return;
        }
        if (type == DoActionBean.EXCE_ACTION.getActionType()){
            Action action = ActionHelper.from(param.getString("actionId"));
            if (action != null){
                ActionRunHelper.startAction(context,action);
            }
        }else if (type == 12){
            AppUtils.launchApp(param.getString("packName"));
        }else{
            DoActionBean.postAction(type,context,param.getString("text"));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
