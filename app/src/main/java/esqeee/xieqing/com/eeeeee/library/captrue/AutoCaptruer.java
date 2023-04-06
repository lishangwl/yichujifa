package esqeee.xieqing.com.eeeeee.library.captrue;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.media.Image;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AlertDialog;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.xieqing.codeutils.util.SPUtils;
import com.xieqing.codeutils.util.ScreenUtils;
import com.xieqing.codeutils.util.ThreadUtils;
import com.xieqing.codeutils.util.Utils;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import esqeee.xieqing.com.eeeeee.MyApp;
import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.action.ActionRun;
import esqeee.xieqing.com.eeeeee.bean.FloatWindow;
import esqeee.xieqing.com.eeeeee.helper.SettingsPreference;
import esqeee.xieqing.com.eeeeee.library.RuntimeLog;
import esqeee.xieqing.com.eeeeee.library.image.ImageWrapper;
import esqeee.xieqing.com.eeeeee.library.image.ScreenCaptruer;
import esqeee.xieqing.com.eeeeee.library.root.Shell;
import esqeee.xieqing.com.eeeeee.listener.ActivityResultListener;
import esqeee.xieqing.com.eeeeee.ui.ScreenRequestActivity;

public class AutoCaptruer {
    private ScreenCaptruer mScreenCapturer;
    private Context context;
    private ActionRun actionRun;
    private MediaProjectionManager mediaProjectionManager;

    public AutoCaptruer(ActionRun actionRun, Context context) {
        this.context = context;
        this.actionRun = actionRun;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mediaProjectionManager = (MediaProjectionManager) Utils.getApp().getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        } else {
            lowVersion();
        }
        backgroundHandler = new Handler(Looper.getMainLooper());
    }

    private void lowVersion() {
        RuntimeLog.i("系统在5.0以下，无法使用截屏");
    }

    public void request() {
        if (!isRequestPermission()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Intent intent = new Intent(context, ScreenRequestActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                IntentExtras.newExtras()
                        .put("callback", new ActivityResultListener() {
                            @Override
                            public void onActivityResult(int requestCode, int resultCode, Intent data) {
                                if (requestCode == 4364) {
                                    if (resultCode == Activity.RESULT_OK) {
                                        ScreenCaptruer.mMediaProjection = mediaProjectionManager.getMediaProjection(resultCode, data);
                                        mScreenCapturer = new ScreenCaptruer(actionRun.getAction().getTitle(), Utils.getApp(), ScreenCaptruer.ORIENTATION_AUTO, ScreenUtils.getScreenDensityDpi(), getBackgroundHandler());
                                    } else {
                                        RuntimeLog.i("获取权限失败，请允许截屏！");
                                        //throw new RuntimeException("获取权限失败，请允许截屏！");
                                    }
                                }
                            }
                        })
                        .putInIntent(intent);
                context.startActivity(intent);
            } else {
                lowVersion();
            }
        }
    }

    private void waitForPermission() {
        long current = System.currentTimeMillis();
        while (!isRequestPermission()) {
            if (System.currentTimeMillis() - current > 5000) {
                showCaptrueRequestErrorDialog();
                throw new RuntimeException("未给截屏权限");
            }
        }
    }

    public void showCaptrueRequestErrorDialog() {
        ThreadUtils.runOnUiThread(() -> {
            if (!SPUtils.getInstance().getBoolean("showBackWardPermission", true)) {
                return;
            }
            RuntimeLog.i("未给截屏权限");
            View view = View.inflate(context, R.layout.screencaptrue_request_error, null);
            Button know = view.findViewById(R.id.know);
            Button noshow = view.findViewById(R.id.noshow);
            view.setBackgroundColor(Color.parseColor("#80000000"));
            FloatWindow floatWindow = new FloatWindow.FloatWindowBuilder()
                    .id("showCaptrueRequestErrorDialog")
                    .move(false)
                    .with(view)
                    .param(new FloatWindow.FloatWindowLayoutParamBuilder()
                            .flags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
                            .format(PixelFormat.TRANSLUCENT)
                            .height(-1)
                            .width(-1)
                            .type(MyApp.getFloatWindowType())
                            .build())
                    .build();
            floatWindow.add();

            know.setOnClickListener(v -> {
                floatWindow.close();
            });
            noshow.setOnClickListener(v -> {
                floatWindow.close();
                SPUtils.getInstance().put("showBackWardPermission", false);
            });
        });
    }

    private Image mPreCapture;
    private ImageWrapper mUnderUse;

    public synchronized ImageWrapper capture() {
        if (SettingsPreference.useRootCaptrue()) {
            Shell.getShell().exce("screencap /sdcard/temp.png");
            java.io.File file = null;
            file = new java.io.File(Environment.getExternalStorageDirectory().getPath() + "/temp.png");
            Bitmap bitmap = null;
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                int len = 0;
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                while ((len = fileInputStream.read(buffer)) != -1) {
                    byteArrayOutputStream.write(buffer, 0, len);
                }
                byte[] r = byteArrayOutputStream.toByteArray();
                bitmap = BitmapFactory.decodeByteArray(r, 0, r.length);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //file.delete();
            return new ImageWrapper(bitmap);
        }
        if (!isRequestPermission()) {
            request();
            waitForPermission();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Image capture = mScreenCapturer.capture();
            if (capture == mPreCapture && mUnderUse != null) {
                return mUnderUse;
            }
            mPreCapture = capture;
            if (mUnderUse != null) {
                mUnderUse.recycle();
            }
            mUnderUse = ImageWrapper.ofImage(capture);
            return mUnderUse;
        } else {
            return null;
        }
    }

    public synchronized Bitmap captrueScreen() {
        ImageWrapper captruerImage = capture();
        Bitmap bitmap = captruerImage == null ? null : captruerImage.getBitmap();
        return bitmap;
    }

    private Handler backgroundHandler;

    private Handler getBackgroundHandler() {
        return backgroundHandler;
    }

    public boolean isRequestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (ScreenCaptruer.mMediaProjection != null) {
                if (mScreenCapturer == null) {
                    mScreenCapturer = new ScreenCaptruer("AutoCaptruer", Utils.getApp(), ScreenCaptruer.ORIENTATION_AUTO, ScreenUtils.getScreenDensityDpi(), getBackgroundHandler());
                }
            }
        }
        return mScreenCapturer != null;
    }

    public void release() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (mScreenCapturer != null) {
                    mScreenCapturer.release();
                    mScreenCapturer = null;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
