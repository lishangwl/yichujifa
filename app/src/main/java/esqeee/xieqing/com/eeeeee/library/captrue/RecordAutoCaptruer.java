package esqeee.xieqing.com.eeeeee.library.captrue;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;

import com.xieqing.codeutils.util.ScreenUtils;
import com.xieqing.codeutils.util.Utils;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import esqeee.xieqing.com.eeeeee.helper.SettingsPreference;
import esqeee.xieqing.com.eeeeee.library.RuntimeLog;
import esqeee.xieqing.com.eeeeee.library.image.CaptruerImage;
import esqeee.xieqing.com.eeeeee.library.image.ScreenCaptruer;
import esqeee.xieqing.com.eeeeee.library.root.Shell;
import esqeee.xieqing.com.eeeeee.listener.ActivityResultListener;
import esqeee.xieqing.com.eeeeee.ui.BaseActivity;
import esqeee.xieqing.com.eeeeee.ui.ScreenRequestActivity;

public class RecordAutoCaptruer {
    private static RecordAutoCaptruer intance;
    private ScreenCaptruer mScreenCapturer;
    private MediaProjection mMediaProjection;
    private MediaProjectionManager mediaProjectionManager;
    private RecordAutoCaptruer(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mediaProjectionManager = (MediaProjectionManager) Utils.getApp().getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        }else{
            lowVersion();
        }



        backgroundThread =  new HandlerThread("AutoCaptruer", android.os.Process.THREAD_PRIORITY_BACKGROUND);
        backgroundThread.start();
        backgroundHandler = new Handler(backgroundThread.getLooper());
    }

    private void lowVersion(){
        RuntimeLog.i("系统在5.0以下，无法使用截屏");
    }

    public MediaProjection getMediaProjection() {
        return mMediaProjection;
    }

    public static RecordAutoCaptruer getIntance() {
        if (intance == null){
            intance = new RecordAutoCaptruer();
        }
        return intance;
    }

    public ScreenCaptruer getScreenCapturer() {
        return mScreenCapturer;
    }

    public void request(BaseActivity activity){
        if (!isRequestPermission()){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Intent intent = new Intent(activity == null?Utils.getApp():activity, ScreenRequestActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                IntentExtras.newExtras()
                        .put("callback", new ActivityResultListener() {
                            @Override
                            public void onActivityResult(int requestCode, int resultCode, Intent data) {
                                if (requestCode == 4364){
                                    if (resultCode == Activity.RESULT_OK){
                                        ScreenCaptruer.mMediaProjection = mediaProjectionManager.getMediaProjection(resultCode,data);
                                        mScreenCapturer = new ScreenCaptruer("RecordAutoCaptruer",Utils.getApp(), ScreenCaptruer.ORIENTATION_AUTO, ScreenUtils.getScreenDensityDpi(), getBackgroundHandler());
                                        //mScreenCapturer =  new ScreenCaptruer("RecordAutoCaptruer",Utils.getApp(), data, ScreenCaptruer.ORIENTATION_AUTO, ScreenUtils.getScreenDensityDpi(), getBackgroundHandler());
                                    }else{
                                        RuntimeLog.i("获取权限失败，请允许截屏！");
                                        //throw new RuntimeException("获取权限失败，请允许截屏！");
                                    }
                                }
                            }
                        })
                        .putInIntent(intent);
                (activity == null?Utils.getApp():activity).startActivity(intent);
            }else{
                lowVersion();
            }
        }
    }

    private Image mPreCapture;
    private CaptruerImage mUnderUse;
    public synchronized CaptruerImage capture() {
        if (SettingsPreference.useRootCaptrue()){
            Shell.getShell().exce("screencap /sdcard/temp.png");
            java.io.File file = null;
            file = new java.io.File(Environment.getExternalStorageDirectory().getPath()+"/temp.png");
            Bitmap bitmap = null;
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                int len = 0;
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                while ((len = fileInputStream.read(buffer))!=-1){
                    byteArrayOutputStream.write(buffer,0,len);
                }
                byte[] r=byteArrayOutputStream.toByteArray();
                bitmap = BitmapFactory.decodeByteArray(r,0,r.length);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            file.delete();
            return new CaptruerImage(bitmap);
        }
        if (!isRequestPermission()) {
            return null;
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Image capture = mScreenCapturer.capture();
            if (capture == mPreCapture && mUnderUse != null) {
                return mUnderUse;
            }
            mPreCapture = capture;
            if (mUnderUse != null) {
                mUnderUse.close();
            }
            mUnderUse = new CaptruerImage(capture);
            return mUnderUse;
        }else {
            return null;
        }
    }

    public synchronized Bitmap captrueScreen() {
        CaptruerImage captruerImage = capture();
        Bitmap bitmap = captruerImage == null?null:captruerImage.getBitmap();
        return bitmap;
    }
    HandlerThread backgroundThread;
    Handler backgroundHandler;
    private Handler getBackgroundHandler() {
        return backgroundHandler;
    }

    public boolean isRequestPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (ScreenCaptruer.mMediaProjection != null){
                if (mScreenCapturer == null){
                    mScreenCapturer = new ScreenCaptruer("RecordAutoCaptruer",Utils.getApp(), ScreenCaptruer.ORIENTATION_AUTO, ScreenUtils.getScreenDensityDpi(), getBackgroundHandler());
                }
            }
        }
        return mScreenCapturer!=null;
    }

    public void release() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mScreenCapturer.release();
                mScreenCapturer = null;
                if (ScreenCaptruer.mMediaProjection != null){
                    ScreenCaptruer.mMediaProjection.stop();
                    ScreenCaptruer.mMediaProjection = null;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
