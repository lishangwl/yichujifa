package com.yicu.yichujifa.inputborad;

import android.content.Context;
import android.content.Intent;
import android.hardware.input.InputManager;
import android.inputmethodservice.InputMethodService;
import android.provider.Settings;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;

import java.util.List;

public class InputBraodUtils {
    public static boolean isVliad(Context context){
        InputMethodManager inputMethodService = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        List<InputMethodInfo> list =  inputMethodService.getEnabledInputMethodList();
        for (InputMethodInfo inputMethodInfo : list){
            if (inputMethodInfo.getPackageName().equals(context.getPackageName())){
                return true;
            }
        }

        return false;
    }

    public static void toSettings(Context context){
        Intent intent = new Intent(Settings.ACTION_INPUT_METHOD_SETTINGS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void showPicker(Context context){
        InputMethodManager inputMethodService = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodService.showInputMethodPicker();
    }
}
