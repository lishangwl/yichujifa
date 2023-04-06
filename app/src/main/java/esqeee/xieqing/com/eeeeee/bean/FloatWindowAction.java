package esqeee.xieqing.com.eeeeee.bean;

public enum  FloatWindowAction {
    ADD,HIDE,SHOW,CLOSE,UPDATE;
    FloatWindowAction(){

    }
    private FloatWindow window;

    public FloatWindowAction setWindow(FloatWindow window) {
        this.window = window;
        return this;
    }

    public FloatWindow getWindow() {
        return window;
    }
}
