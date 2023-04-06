package tiiehenry.code.view;

import android.content.*;
import android.os.*;
import android.util.*;

import tiiehenry.code.*;
import tiiehenry.code.language.*;

public abstract class CodeTextField extends FreeScrollingTextField {
    public CodeTextField(Context context) {
        super(context);
    }

    public CodeTextField(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CodeTextField(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setLanguage(Language lan) {
        Lexer.setLanguage(lan);
        AutoCompletePanel.setLanguage(lan);
    }

    public void format() {
        selectText(false);
        CharSequence text = Lexer.getFormatter().format(new DocumentProvider(_hDoc), _autoIndentWidth);
        _hDoc.beginBatchEdit();
        _hDoc.deleteAt(0, _hDoc.docLength() - 1, System.nanoTime());
        _hDoc.insertBefore(text.toString().toCharArray(), 0, System.nanoTime());
        _hDoc.endBatchEdit();
        _hDoc.clearSpans();
        respan();
        invalidate();
    }

    public DocumentProvider getDocumentProvider() {
        return _hDoc;
    }//to do

    public TextFieldUiState getUiState() {
        return new TextFieldUiState(this);
    }

    public void restoreUiState(Parcelable state) {
        TextFieldUiState uiState = (TextFieldUiState) state;
        if (uiState.doc != null)
            setDocumentProvider(uiState.doc);
        final int caretPosition = uiState.caretPosition;
        setScrollX(uiState.scrollX);
        setScrollY(uiState.scrollY);
        // If the text field is in the process of being created, it may not
        // have its width and height set yet.
        // Therefore, post UI restoration tasks to run later.
        if (uiState.selectMode) {
            final int selStart = uiState.selectBegin;
            final int selEnd = uiState.selectEnd;

            post(new Runnable() {
                @Override
                public void run() {
                    setSelectionRange(selStart, selEnd - selStart);
                    if (caretPosition < selEnd) {
                        focusSelectionStart(); //caret at the end by default
                    }
                }
            });
        } else {
            post(new Runnable() {
                @Override
                public void run() {
                    moveCaret(caretPosition);
                }
            });
        }
    }

    //*********************************************************************
    //**************** UI State for saving and restoring ******************
    //*********************************************************************
    public static class TextFieldUiState implements Parcelable {
        public static final Parcelable.Creator<TextFieldUiState>
                CREATOR = new Parcelable.Creator<TextFieldUiState>() {
            @Override
            public TextFieldUiState createFromParcel(Parcel in) {
                return new TextFieldUiState(in);
            }

            @Override
            public TextFieldUiState[] newArray(int size) {
                return new TextFieldUiState[size];
            }
        };
        final int caretPosition;
        final int scrollX;
        final int scrollY;
        final boolean selectMode;
        final int selectBegin;
        final int selectEnd;
        DocumentProvider doc;

        public TextFieldUiState(CodeTextField textField) {
            caretPosition = textField.getCaretPosition();
            scrollX = textField.getScrollX();
            scrollY = textField.getScrollY();
            selectMode = textField.isSelectText();
            selectBegin = textField.getSelectionStart();
            selectEnd = textField.getSelectionEnd();
            doc = textField.getDocumentProvider();
        }

        private TextFieldUiState(Parcel in) {
            caretPosition = in.readInt();
            scrollX = in.readInt();
            scrollY = in.readInt();
            selectMode = in.readInt() != 0;
            selectBegin = in.readInt();
            selectEnd = in.readInt();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            out.writeInt(caretPosition);
            out.writeInt(scrollX);
            out.writeInt(scrollY);
            out.writeInt(selectMode ? 1 : 0);
            out.writeInt(selectBegin);
            out.writeInt(selectEnd);
        }

    }


}
