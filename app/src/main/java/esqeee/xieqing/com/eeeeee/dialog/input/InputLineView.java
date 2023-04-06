package esqeee.xieqing.com.eeeeee.dialog.input;

import android.content.Context;
import com.google.android.material.textfield.TextInputEditText;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xieqing.codeutils.util.SizeUtils;

import esqeee.xieqing.com.eeeeee.R;

public class InputLineView{
        private LinearLayout linearLayout;
        private TextView hint;
        private EditText input;
        private InputLine inputLine;

    public InputLine getInputLine() {
        return inputLine;
    }
    public void hide(){
        linearLayout.setVisibility(View.GONE);
    }
    private View.OnClickListener hintClick;
    public void setHintListener(View.OnClickListener clickListener){
        hintClick = clickListener;
        hint.setOnClickListener(hintClick);
    }
    public void setInput(String s){
        if (input!=null){
            input.setText(s);
        }
    }
    public InputLineView(Context context, InputLine inputLine){
            inputLine.setInputLineView(this);
            this.inputLine = inputLine;
            linearLayout = new LinearLayout(context);
            linearLayout.setGravity(Gravity.CENTER_VERTICAL);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,SizeUtils.dp2px(40));
            layoutParams.setMargins(16,16,16,16);
            hint = new TextView(context);
            hint.setText(inputLine.hint);

            input = new TextInputEditText(context);
            input.setLayoutParams(layoutParams);
            input.setBackgroundResource(R.drawable.edit_bg);

            if (inputLine instanceof InputHintLine){
                hint.setLayoutParams(layoutParams);
                hint.setText("\t\t\t\t"+inputLine.hint);
                hint.setTextColor(context.getResources().getColor(R.color.colorAccent));
                linearLayout.addView(hint);
                return;
            }

            if (inputLine instanceof InputLongTextLine){
                LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,SizeUtils.dp2px(150));
                layoutParams2.setMargins(16,16,16,16);
                input.setGravity(Gravity.LEFT);
                input.setTextSize(12);
                input.setMaxLines(1000);
                input.setSingleLine(false);
                input.setLayoutParams(layoutParams2);
                input.setHorizontallyScrolling(false);
                linearLayout.setGravity(Gravity.LEFT);
            }
            input.setText(inputLine.defaultText);
            input.setPadding(16,16,16,16);
            //input.setBackground(context.getResources().getDrawable(R.drawable.edit_bg));
            if (inputLine.inputType!=-1){
                input.setInputType(inputLine.inputType);
            }
            
            linearLayout.addView(hint);
            linearLayout.addView(input);
            linearLayout.setLayoutParams(layoutParams);
        }
        public View getView(){
            ViewParent viewParent =linearLayout.getParent();
            if (viewParent!=null){
                ((ViewGroup)viewParent).removeView(linearLayout);
            }
            return linearLayout;
        }
        public CharSequence getInput() {
            return input.getText();
        }

        
        public boolean isNull(){
            CharSequence charSequence = getInput();
            if (charSequence == null){
                return true;
            }
            if (charSequence.toString().equals("") || charSequence.length() == 0){
                return true;
            }
            return false;
        }
    }