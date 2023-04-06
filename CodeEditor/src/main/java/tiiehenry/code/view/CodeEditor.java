package tiiehenry.code.view;

import android.content.*;
import android.graphics.*;
import android.util.*;
import android.view.*;

import java.io.*;

import tiiehenry.code.*;
import tiiehenry.code.language.*;
import tiiehenry.code.language.lua.*;
import tiiehenry.code.language.xq.*;
import tiiehenry.code.language.xq.LuaLanguage;
import tiiehenry.code.view.listener.*;

public class CodeEditor extends CodeTextField {
    private Context mContext;

    private OnKeyShortcutListener _onKeyShortcutListener = new OnKeyShortcutListener() {
        @Override
        public boolean onKeyShortcut(int keyCode, KeyEvent event) {
            final int filteredMetaState = event.getMetaState() & ~KeyEvent.META_CTRL_MASK;
            if (KeyEvent.metaStateHasNoModifiers(filteredMetaState)) {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_A:
                        selectAll();
                        return true;
                    case KeyEvent.KEYCODE_X:
                        cut();
                        return true;
                    case KeyEvent.KEYCODE_C:
                        copy();
                        return true;
                    case KeyEvent.KEYCODE_V:
                        paste();
                        return true;
                }
            }
            return false;
        }
    };
    private boolean _isWordWrap;

    private File fontDir = new File("/sdcard/android/fonts/");

    private int _index;

    public CodeEditor(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public CodeEditor(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    public CodeEditor(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        initFont(fontDir);
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        float size = TypedValue.applyDimension(2, BASE_TEXT_SIZE_PIXELS, dm);
        setTextSize((int) size);
        setShowLineNumbers(true);
        setHighlightCurrentRow(true);
        //自动换行
        setWordWrap(false);
        setAutoIndentWidth(1);
        setLanguage(LuaLanguage.getInstance());
        setNavigationMethod(new YoyoNavigationMethod(this));
    }

    public void initFont(File fontDir) {
        setTypeface(Typeface.MONOSPACE);
        File df = new File(fontDir + "default.ttf");
        if (df.exists())
            setTypeface(Typeface.createFromFile(df));
        File bf = new File(fontDir + "bold.ttf");
        if (bf.exists())
            setBoldTypeface(Typeface.createFromFile(bf));
        File tf = new File(fontDir + "italic.ttf");
        if (tf.exists())
            setItalicTypeface(Typeface.createFromFile(tf));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (_index != 0 && right > 0) {
            moveCaret(_index);
            _index = 0;
        }
    }

    public void addNames(String[] names) {
        Language lang = Lexer.getLanguage();
        String[] old = lang.getNames();
        String[] news = new String[old.length + names.length];
        System.arraycopy(old, 0, news, 0, old.length);
        System.arraycopy(names, 0, news, old.length, names.length);
        lang.setNames(news);
        //Lexer.setLanguage(lang);
        //setTabSpaces(4);
        respan();
        invalidate();

    }

    @Override
    public boolean onKeyShortcut(int keyCode, KeyEvent event) {
        return _onKeyShortcutListener.onKeyShortcut(keyCode, event)
                || super.onKeyShortcut(keyCode, event);
    }

    public void setOnKeyShortcutListener(OnKeyShortcutListener l) {
        _onKeyShortcutListener = l;
    }

    //自动换行
    @Override
    public void setWordWrap(boolean enable) {
        _isWordWrap = enable;
        super.setWordWrap(enable);
    }

    public void insert(int idx, String text) {
        selectText(false);
        moveCaret(idx);
        paste(text);
        //_hDoc.insert(idx,text);
    }

    public DocumentProvider getText() {
        return createDocumentProvider();
    }

    public String getString() {
        return getText().toString();
    }

    public void setText(CharSequence c) {
        //TextBuffer text=new TextBuffer();
        Document doc = new Document(this);
        doc.setWordWrap(_isWordWrap);
        doc.setText(c);
        setDocumentProvider(new DocumentProvider(doc));
        //doc.analyzeWordWrap();
    }

    public void setText(CharSequence c, boolean isRep) {
        replaceText(0, getLength() - 1, c.toString());
    }

    public String getSelectedText() {
        return _hDoc.subSequence(getSelectionStart(), getSelectionEnd() - getSelectionStart()).toString();
    }

    public void setSelection(int index) {
        selectText(false);
        if (!hasLayout())
            moveCaret(index);
        else
            _index = index;
    }

    public void gotoLine(int line) {
        if (line > _hDoc.getRowCount()) {
            line = _hDoc.getRowCount();
        }
        int i = getText().getLineOffset(line - 1);
        setSelection(i);
    }

    public void undo() {
        DocumentProvider doc = createDocumentProvider();
        int newPosition = doc.undo();
        if (newPosition >= 0) {
            setEdited(true);
            respan();
            selectText(false);
            moveCaret(newPosition);
            invalidate();
        }
    }

    public void redo() {
        DocumentProvider doc = createDocumentProvider();
        int newPosition = doc.redo();
        if (newPosition >= 0) {
            setEdited(true);
            respan();
            selectText(false);
            moveCaret(newPosition);
            invalidate();
        }
    }


}
