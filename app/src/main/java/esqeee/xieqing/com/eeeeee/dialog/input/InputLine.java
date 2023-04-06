package esqeee.xieqing.com.eeeeee.dialog.input;

public abstract class InputLine{
    public String hint;
    public String defaultText;
    public int inputType;
    public abstract Object getResult();
    public abstract void setResult(Object o);
    public boolean canInputNull = false;
    private InputLineView inputLineView;

    public InputLineView getInputLineView() {
        return inputLineView;
    }



    public void setInputLineView(InputLineView inputLineView) {
        this.inputLineView = inputLineView;
    }

    public InputLine setCanInputNull(boolean canInputNull) {
        this.canInputNull = canInputNull;
        return this;
    }

    public InputLine(String hint, String defaultText, int inputType){
            this.hint = hint;
            this.defaultText = defaultText;
            this.inputType = inputType;
    }

}