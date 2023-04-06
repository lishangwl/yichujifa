package esqeee.xieqing.com.eeeeee.dialog.input;

import android.text.InputType;

public class InputTextLine extends InputLine{
        private String result;

        public void setResult(Object result) {
            this.result = result.toString();
        }

        public String getResult() {
            return result;
        }

        public InputTextLine(String hint) {
            super(hint, "",-1);
        }
        public InputTextLine() {
            super("", "",-1);
        }

        public InputTextLine(String hint, String defaultText) {
            super(hint, defaultText,-1);
        }
    }