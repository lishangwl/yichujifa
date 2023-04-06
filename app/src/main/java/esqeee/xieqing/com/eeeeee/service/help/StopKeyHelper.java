package esqeee.xieqing.com.eeeeee.service.help;

import android.os.Handler;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;

import com.xieqing.codeutils.util.PhoneUtils;
import com.xieqing.codeutils.util.SPUtils;
import com.xieqing.codeutils.util.Utils;

import esqeee.xieqing.com.eeeeee.action.ActionRunHelper;
import esqeee.xieqing.com.eeeeee.service.AccessbilityObserver;

public class StopKeyHelper extends AccessbilityObserver {


    private boolean isVolumeKeyDowning = false;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) throws Throwable {

    }

    @Override
    public boolean onKeyEvent(KeyEvent event) throws Throwable {
        if ((event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP || event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN)
                && ActionRunHelper.hasActionRunning()){
            switch (event.getAction()){
                case KeyEvent.ACTION_UP:
                    isVolumeKeyDowning = false;
                    break;
                case KeyEvent.ACTION_DOWN:
                    isVolumeKeyDowning = true;
                    final KeyEvent event2 = event;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            if (SPUtils.getInstance(Utils.getApp().getPackageName()+"_preferences").getBoolean("isLongClickUpVloumStop",true) && isVolumeKeyDowning
                                    &&event2.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP){
                                PhoneUtils.vibrate();
                                ActionRunHelper.stopAllAction();
                                return;
                            }

                            if (SPUtils.getInstance(Utils.getApp().getPackageName()+"_preferences").getBoolean("isLongClickDownVloumStop",false) && isVolumeKeyDowning
                                    && event2.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN){
                                PhoneUtils.vibrate();
                                ActionRunHelper.stopAllAction();
                                return;
                            }
                        }
                    },500);
                    break;
            }
        }
        return false;
    }
}