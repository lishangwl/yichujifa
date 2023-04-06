package esqeee.xieqing.com.eeeeee.dialog.input;

import android.text.InputType;

public class InputPhoneLine extends InputLine{
        private long result;

        public void setResult(Object result) {
            this.result = (long)result;
        }

        public Long getResult() {
            return result;
        }

        public InputPhoneLine(String hint, int defaultText) {
            super(hint,String.valueOf(defaultText),InputType.TYPE_CLASS_PHONE);
        }
        public InputPhoneLine(String hint) {
            super("","",InputType.TYPE_CLASS_PHONE);
        }
        public InputPhoneLine(int defaultText) {
            super("",String.valueOf(defaultText),InputType.TYPE_CLASS_PHONE);
        }
        public InputPhoneLine() {
            super("","",InputType.TYPE_CLASS_PHONE);
        }
}
