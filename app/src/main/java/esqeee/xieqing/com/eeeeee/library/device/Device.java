package esqeee.xieqing.com.eeeeee.library.device;

import android.view.InputDevice;

public class Device {
    public static InputDevice getTouchDevices() {
        int[] ids = InputDevice.getDeviceIds();
        for (int id : ids) {
            InputDevice device = InputDevice.getDevice(id);
            if ((device.getSources() & InputDevice.SOURCE_TOUCHSCREEN) == InputDevice.SOURCE_TOUCHSCREEN
                    || (device.getSources() & InputDevice.SOURCE_TOUCHPAD) == InputDevice.SOURCE_TOUCHPAD) {
                return device;
            }
        }
        return null;
    }
}
