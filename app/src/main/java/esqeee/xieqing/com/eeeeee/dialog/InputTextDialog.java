package esqeee.xieqing.com.eeeeee.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import androidx.annotation.NonNull;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.xieqing.codeutils.util.PhoneUtils;
import com.xieqing.codeutils.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import esqeee.xieqing.com.eeeeee.MyApp;
import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.dialog.input.InputHintLine;
import esqeee.xieqing.com.eeeeee.dialog.input.InputLine;
import esqeee.xieqing.com.eeeeee.dialog.input.InputLineView;
import esqeee.xieqing.com.eeeeee.dialog.input.InputNumberLine;
import esqeee.xieqing.com.eeeeee.dialog.input.InputPhoneLine;
import esqeee.xieqing.com.eeeeee.dialog.input.InputQQLine;
import esqeee.xieqing.com.eeeeee.dialog.input.InputTextListener;

public class InputTextDialog extends Dialog implements View.OnClickListener {
    public static InputTextDialog getDialog(Context context){
        return new InputTextDialog(context);
    }
    private Context context;
    private List<InputLineView> lines = new ArrayList<>();
    private String title = "提示";
    private String message = "";
    private InputTextListener inputTextListener;

    public InputTextDialog setInputTextListener(InputTextListener inputTextListener) {
        this.inputTextListener = inputTextListener;
        return this;
    }

    public InputTextDialog(@NonNull Context context) {
        this(context,true);
    }

    public InputTextDialog(@NonNull Context context,boolean isShowFloat) {
        super(context);
        this.context = context;
        Window window =this.getWindow();
        if (isShowFloat){
            window.setType(MyApp.getFloatWindowType());
        }
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }
    public InputTextDialog setMessage(String message){
        this.message = message;
        return this;
    }

    public InputTextDialog setTitle(String title){
        this.title = title;
        return this;
    }

    public InputTextDialog addInputLine(InputLine inputLine){
        lines.add(new InputLineView(context,inputLine));
        return this;
    }


    public void show(){
        View view = View.inflate(context,R.layout.dialog_inputtext,null);
        TextView title = view.findViewById(R.id.input_title);
        TextView message = view.findViewById(R.id.input_message);
        title.setText(this.title);
        message.setText(Html.fromHtml(this.message));

        ViewGroup line = view.findViewById(R.id.input_lines);
        Log.e("xieqing",lines.size()+"");
        for (int i = 0;i<lines.size();i++){
            line.addView(lines.get(i).getView());
        }

        View ok = view.findViewById(R.id.input_ok);
        View cannel = view.findViewById(R.id.input_cannel);

        ok.setOnClickListener(this);
        cannel.setOnClickListener(this);

        setContentView(view);
        super.show();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.input_ok:
                InputLine[] result = new InputLine[lines.size()];
                for (int i = 0; i < lines.size() ; i++){
                    InputLineView lineView = lines.get(i);
                    InputLine inputLine = lineView.getInputLine();
                    result[i] = inputLine;
                    if (inputLine instanceof InputHintLine){
                        continue;
                    }
                    if (lineView.isNull() && !lineView.getInputLine().canInputNull){
                        PhoneUtils.vibrateShort();
                        ToastUtils.showShort("请填写正确！");
                        return;
                    }

                    CharSequence charSequence = lineView.getInput();
                    if (inputLine instanceof InputNumberLine){
                        try {
                            if (Integer.parseInt(charSequence.toString())>=0){
                                ((InputNumberLine) inputLine).setResult(Integer.parseInt(charSequence.toString()));
                            }else{
                                PhoneUtils.vibrateShort();
                                ToastUtils.showShort("整数格式有误！");
                                return;
                            }
                        }catch (NumberFormatException e){
                            PhoneUtils.vibrateShort();
                            ToastUtils.showShort("整数格式有误！");
                            return;
                        }
                    }else if (inputLine instanceof InputPhoneLine){
                        if (charSequence.toString().length()!=11){
                            PhoneUtils.vibrateShort();
                            ToastUtils.showShort("手机格式有误！");
                            return;
                        }else{
                            try {
                                ((InputPhoneLine) inputLine).setResult(Long.parseLong(charSequence.toString()));
                            }catch (NumberFormatException e){
                                PhoneUtils.vibrateShort();
                                ToastUtils.showShort("手机格式有误！");
                                return;
                            }
                        }
                    }else if (inputLine instanceof InputQQLine){
                        if (charSequence.toString().length()<5 && charSequence.toString().length()>10){
                            PhoneUtils.vibrateShort();
                            ToastUtils.showShort("QQ格式有误！");
                            return;
                        }else{
                            try {
                                ((InputQQLine) inputLine).setResult(Long.parseLong(charSequence.toString()));
                            }catch (NumberFormatException e){
                                PhoneUtils.vibrateShort();
                                ToastUtils.showShort("手机格式有误！");
                                return;
                            }
                        }
                    }else{
                        inputLine.setResult(charSequence);
                    }
                }
                try {
                    inputTextListener.onConfirm(result);
                    dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.input_cannel:
                dismiss();
                break;
        }
    }
}
