package esqeee.xieqing.com.eeeeee.dialog.input;

import android.text.InputType;

public class InputHintLine extends InputLine{
        private String result;

        public void setResult(Object result) {
            this.result = result.toString();
        }

        public String getResult() {
            return result;
        }

        public InputHintLine(String hint) {
            super(hint, "",InputType.TYPE_CLASS_TEXT);
        }
        public InputHintLine() {
            super("", "",InputType.TYPE_CLASS_TEXT);
        }

        public InputHintLine(String hint, String defaultText) {
            super(hint, defaultText,InputType.TYPE_CLASS_TEXT);
        }
    }