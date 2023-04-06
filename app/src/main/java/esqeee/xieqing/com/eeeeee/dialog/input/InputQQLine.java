package esqeee.xieqing.com.eeeeee.dialog.input;

import android.text.InputType;

public class InputQQLine extends InputLine{
        private long result;

        public void setResult(Object result) {
            this.result = (long)result;
        }

        public Long getResult() {
            return result;
        }

        public InputQQLine(String hint, int defaultText) {
            super(hint,String.valueOf(defaultText),InputType.TYPE_CLASS_PHONE);
        }
        public InputQQLine(String hint) {
            super("","",InputType.TYPE_CLASS_PHONE);
        }
        public InputQQLine(int defaultText) {
            super("",String.valueOf(defaultText),InputType.TYPE_CLASS_PHONE);
        }
        public InputQQLine() {
            super("","",InputType.TYPE_CLASS_PHONE);
        }
}
