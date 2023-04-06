package esqeee.xieqing.com.eeeeee.doAction.api;

import androidx.appcompat.app.AlertDialog;

import android.view.ViewGroup;
import android.widget.EditText;

import com.xieqing.codeutils.util.SizeUtils;
import com.yicu.yichujifa.inputborad.InputBraodUtils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.doAction.Base;
import esqeee.xieqing.com.eeeeee.widget.FDialog;

public class Dialog extends Base {
    public static final int ACTION_DELETE_FILE = 0;
    public static Base intansce;
    public static Base getIntansce(){
        if (intansce==null){
            intansce = new Dialog();
        }
        return intansce;
    }

    @Override
    public boolean post(JSONBean param) {
        String arg = param.getString("arg");
        JSONBean var;
        switch (arg){
            case "弹出对话框":
                JSONBean variable = queryVariable(param.getString("var"));
                String title = getString(param.getString("title"));
                String confirm = getString(param.getString("confirm"));
                String cannel = getString(param.getString("cannel"));
                String text = getString(param.getString("text"));
                setValue(variable,"1");
                lock();
                new FDialog(getActionRun().getContext())
                        .setTitle(title)
                        .setMessage(text)
                        .setCanfirm(confirm,view -> {
                            setValue(variable,"0");
                            unlock();
                        }).setCannel(cannel,v->{
                            unlock();
                        })
                        .setDismissListener(d->{
                            unlock();
                        }).show();
                waitForUnlock();
                break;
            case "弹出选择框":
                variable = checkVarExiets(param,"var");
                title = getString(param.getString("title"));
                text = getString(param.getString("text"));
                setValue(variable,"-1");
                lock();
                new FDialog(getActionRun().getContext())
                        .setTitle(title)
                        .setCanfirm("",null)
                        .setDismissListener(d->{
                            unlock();
                        }).setItems(text.split("\n"),i->{
                    unlock();
                    setValue(variable, i +"");
                }).show();
                waitForUnlock();
                break;
            case "弹出输入框":
                variable = checkVarExiets(param,"var");
                title = getString(param.getString("title"));
                String hint = getString(param.getString("hint"));
                text = getString(param.getString("default"));
                setValue(variable,"");
                lock();
                EditText editText = new EditText(getActionRun().getContext());
                editText.setHint(hint);
                editText.setText(text);
                ViewGroup.MarginLayoutParams marginLayoutParams = new ViewGroup.MarginLayoutParams(-1,-2);
                marginLayoutParams.setMargins(SizeUtils.dp2px(16),0,SizeUtils.dp2px(16),0);
                editText.setLayoutParams(marginLayoutParams);
                new FDialog(getActionRun().getContext())
                        .setTitle(title)
                        .addView(editText)
                        .setCanfirm("确定",view -> {
                            setValue(variable,editText.getText().toString());
                            unlock();
                        }).setCannel("取消",v->{
                            unlock();
                        })
                        .setDismissListener(d->{
                            unlock();
                        }).show();
                waitForUnlock();
                break;
            case "弹出输入法选择框":
                InputBraodUtils.showPicker(getActionRun().getContext());
                break;
        }
        return true;
    }


    private boolean writeByte(String file,byte[] bytes){
        try {
            java.io.File file1 = new java.io.File(file);
            FileOutputStream fileOutputStream = new FileOutputStream(file1);
            fileOutputStream.write(bytes);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public int getType() {
        return 2;
    }

    @Override
    public String getName() {
        return "文件操作";
    }
}
