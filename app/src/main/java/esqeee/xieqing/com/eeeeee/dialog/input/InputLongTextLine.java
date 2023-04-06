package esqeee.xieqing.com.eeeeee.dialog.input;

import android.text.InputType;

public class InputLongTextLine extends InputTextLine {
    public InputLongTextLine(String hint) {
        super(hint, "");
    }

    public InputLongTextLine() {
        super("", "");
    }

    public InputLongTextLine(String hint, String defaultText) {
        super(hint, defaultText);
    }

}