package esqeee.xieqing.com.eeeeee.service;

import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;

public abstract class AccessbilityObserver{

    public abstract void onAccessibilityEvent(AccessibilityEvent event) throws Throwable;

    public abstract boolean onKeyEvent(KeyEvent event) throws Throwable;

}
