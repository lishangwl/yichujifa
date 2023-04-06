package esqeee.xieqing.com.eeeeee.task;

import com.xieqing.codeutils.util.ThreadUtils;

public abstract class BackgroundTask<Param,Result> implements Runnable{
    private Param param;
    private Result result;
    public BackgroundTask(Param param){
        this.param = param;
    }
    public abstract void doInBackground(Param param) throws Exception;
    public abstract void doOnMain(Result result) throws Exception;

    public Param getParam() {
        return param;
    }

    private Runnable handler = new Runnable() {
        @Override
        public void run() {
            try {
                doOnMain(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    @Override
    public void run() {
        try {
            doInBackground(param);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void handle(Result result){
        this.result = result;
        ThreadUtils.runOnUiThread(handler);
    }
    public void start(){
        new Thread(this).start();
    }
}
