package esqeee.xieqing.com.eeeeee.dialog.input;

import android.text.InputType;

public class InputNumberLine extends InputLine{
        private int result;

        public void setResult(Object result) {
            this.result = (int)result;
        }

        public Object getResult() {
            return result;
        }

        public InputNumberLine(String hint, int defaultText) {
            super(hint,String.valueOf(defaultText),InputType.TYPE_CLASS_NUMBER);
        }
        public InputNumberLine(int defaultText) {
            super("",String.valueOf(defaultText),InputType.TYPE_CLASS_NUMBER);
        }
        public InputNumberLine() {
            super("","",InputType.TYPE_CLASS_NUMBER);
        }
}