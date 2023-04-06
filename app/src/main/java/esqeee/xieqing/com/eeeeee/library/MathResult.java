package esqeee.xieqing.com.eeeeee.library;

public class MathResult {
    private Exception exception;
    private double result ;

    public void setResult(double result) {
        this.result = result;
    }

    public int getResult() {
        return (int)result;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public boolean isError(){
        return exception != null;
    }
}
