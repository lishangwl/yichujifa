package esqeee.xieqing.com.eeeeee.library.root;

import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;


import esqeee.xieqing.com.eeeeee.library.RuntimeLog;
import esqeee.xieqing.com.eeeeee.library.device.Device;


public class Shell {
    private static Shell shell;

    private Process process;
    private DataOutputStream dataOutputStream;
    private int touchDevice = 0;

    public static Shell getShell() {
        if (shell == null) {
            shell = new Shell();
        }
        return shell;
    }

    private Shell() {
        try {
            touchDevice = Device.getTouchDevices().getId();
        } catch (Exception e) {
            touchDevice = 0;
        }
    }

    public boolean su() {
        if (process == null) {
            try {
                process = Runtime.getRuntime().exec("su");
                dataOutputStream = new DataOutputStream(process.getOutputStream());
            } catch (IOException e) {
                RuntimeLog.i("获取ROOT失败！请检查设备是否已经root，如果已经root请给一触即发权限，如果未root，在设置处关闭所有ROOT选项");
                return false;
            }
        }
        return true;
    }

    public void exce(String string) {
        Log.d("Shell", "exec=" + string);
        if (dataOutputStream == null) {
            if (!su()) {
                return;
            }
        }
        try {
            dataOutputStream.write((string + "\n").getBytes());
            dataOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
            RuntimeLog.log("shell:" + e.getMessage());
        }
    }


    public boolean click(int x, int y) {
        if (su()) {
            exce(("input tap " + x + " " + y));
            return true;
        }
        return false;
    }

    public boolean swipXY(int x, int y, int toX, int toY, int touchTime) {
        if (su()) {
            exce(("input swipe " + x + " " + y + " " + toX + " " + toY + " " + touchTime));
            return true;
        }
        return false;
    }

    public boolean touchXY(int x, int y, int touchTime) {
        if (su()) {
            exce(("input swipe " + x + " " + y + " " + x + " " + y + " " + touchTime));
            return true;
        }
        return false;
    }


    private void sleep(long duration) {
        try {
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            RuntimeLog.log(e);
        }
    }
}
