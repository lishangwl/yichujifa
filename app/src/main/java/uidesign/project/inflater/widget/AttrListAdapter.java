package uidesign.project.inflater.widget;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.xieqing.codeutils.util.FileUtils;
import com.yicu.yichujifa.ui.colorpicker.dialogs.ColorPickerDialog;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.dialog.InputTextDialog;
import esqeee.xieqing.com.eeeeee.dialog.input.InputLine;
import esqeee.xieqing.com.eeeeee.dialog.input.InputNumberLine;
import esqeee.xieqing.com.eeeeee.dialog.input.InputTextLine;
import esqeee.xieqing.com.eeeeee.dialog.input.InputTextListener;
import esqeee.xieqing.com.eeeeee.fragment.ActionsFragment;
import esqeee.xieqing.com.eeeeee.listener.ActivityResultListener;
import esqeee.xieqing.com.eeeeee.ui.BaseActivity;
import uidesign.project.model.Attr;

import static android.app.Activity.RESULT_OK;

public class AttrListAdapter extends RecyclerView.Adapter<AttrListAdapter.BaseHolder> {

    private Attr attr;
    private String[] keys;


    private Context context;



    public AttrListAdapter(Context context, Attr attr){
        this.attr = attr;
        this.keys = attr.keySet().toArray(new String[]{});
        Arrays.sort(this.keys);
        this.context = context;
    }





    @NonNull
    @Override
    public BaseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 1){
            return new BoolHolder(LayoutInflater.from(context).inflate(R.layout.attr_item_bool,parent,false));
        }else if (viewType == 2){
            return new ColorHolder(LayoutInflater.from(context).inflate(R.layout.attr_item_color,parent,false));
        }else if (viewType == 3){
            return new ClickHolder(LayoutInflater.from(context).inflate(R.layout.attr_item_click,parent,false));
        }else if (viewType == 4){
            return new GravityHolder(LayoutInflater.from(context).inflate(R.layout.attr_item_gravity,parent,false));
        }else if (viewType == 5){
            return new ImageHolder(LayoutInflater.from(context).inflate(R.layout.attr_item_click,parent,false));
        }else if (viewType == 6){
            return new InputTypeHolder(LayoutInflater.from(context).inflate(R.layout.attr_item_input,parent,false));
        }else{
            return new StringHolder(LayoutInflater.from(context).inflate(R.layout.attr_item,parent,false));
        }
    }

    @Override
    public int getItemViewType(int position) {
        return Attr.getAttrType(keys[position]);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, int position) {
        try {
            holder.bind();
        }catch (Exception e){
            Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    private static Field mListener;

    {
        try {
            mListener = TextView.class.getDeclaredField("mListeners");
            mListener.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void clearListener(TextView editText){
        try {
            ArrayList<TextWatcher> list = (ArrayList<TextWatcher>) mListener.get(editText);
            if (list != null){
                list.clear();
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        return keys.length;
    }

    public void setAttr(Attr attrs) {
        this.attr = attrs;
        this.keys = attr.keySet().toArray(new String[]{});
        Arrays.sort(this.keys);
        notifyDataSetChanged();
    }

    class ImageHolder extends BaseHolder{

        public void bind(){
            final TextView name = itemView.findViewById(R.id.name);
            final TextView text = itemView.findViewById(R.id.value);

            name.setText(keys[getAdapterPosition()]);
            text.setText(attr.getString(keys[getAdapterPosition()]));

            text.setOnClickListener(v->{
                new AlertDialog.Builder(context)
                        .setItems(new String[]{"从相册中选取","手动输入","清空"},(d,i)->{
                            if (i == 0){
                                ((BaseActivity)context).addActivityResultListener(new ActivityResultListener() {
                                    @Override
                                    public void onActivityResult(int requestCode, int resultCode, Intent data) {
                                        ((BaseActivity)context).removeActivityResultListener(this);
                                        if (requestCode == 5481 && resultCode == RESULT_OK) {
                                            List<String> result = Matisse.obtainPathResult(data);
                                            if (result.size()>0){
                                                attr.put(keys[getAdapterPosition()],result.get(0));
                                                text.setText(attr.getString(keys[getAdapterPosition()]));
                                            }
                                        }
                                    }
                                });

                                Matisse.from(((BaseActivity)context))
                                        .choose(MimeType.ofImage())
                                        .countable(true)
                                        .maxSelectable(1)
                                        .isCrop(false)
                                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                                        .thumbnailScale(0.85f)
                                        .imageEngine(new GlideEngine())
                                        .forResult(5481);
                            }else if(i == 1){
                                InputTextDialog.getDialog((BaseActivity)context).setTitle("")
                                        .setMessage("请输入网络链接或者本地路径")
                                        .addInputLine(new InputTextLine())
                                        .addInputLine(new InputNumberLine("等待时间",1000)).setInputTextListener(new InputTextListener() {
                                    @Override
                                    public void onConfirm(InputLine[] result) throws Exception{
                                        attr.put(keys[getAdapterPosition()], (String) result[0].getResult());
                                        text.setText(attr.getString(keys[getAdapterPosition()]));
                                    }
                                }).show();
                            }else{
                                attr.put(keys[getAdapterPosition()], "");
                                text.setText("");
                            }
                        }).show();

            });
        }

        public ImageHolder(View itemView) {
            super(itemView);
        }
    }
    class StringHolder extends BaseHolder{

        public void bind(){
            final TextView name = itemView.findViewById(R.id.name);
            final EditText editText = itemView.findViewById(R.id.value);

            name.setText(keys[getAdapterPosition()]);
            editText.setText(attr.getString(keys[getAdapterPosition()]));

            editText.clearFocus();

            clearListener(editText);
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    attr.put(keys[getAdapterPosition()],editText.getText().toString());
                }
            });
        }

        public StringHolder(View itemView) {
            super(itemView);
        }
    }

    class BoolHolder extends BaseHolder{

        public void bind(){
            final TextView name = itemView.findViewById(R.id.name);
            final Spinner spinner = itemView.findViewById(R.id.value);

            name.setText(keys[getAdapterPosition()]);

            spinner.setSelection(attr.getBoolean(keys[getAdapterPosition()])?0:1);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    attr.put(keys[getAdapterPosition()],position == 0?"真":"假");
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

        public BoolHolder(View itemView) {
            super(itemView);
        }
    }

    class GravityHolder extends BaseHolder{

        public void bind(){
            final TextView name = itemView.findViewById(R.id.name);
            final Spinner spinner = itemView.findViewById(R.id.value);

            name.setText(keys[getAdapterPosition()]);

            spinner.setSelection(attr.getInt(keys[getAdapterPosition()]));
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    attr.put(keys[getAdapterPosition()],position+"");
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

        public GravityHolder(View itemView) {
            super(itemView);
        }
    }

    class InputTypeHolder extends BaseHolder{

        public void bind(){
            final TextView name = itemView.findViewById(R.id.name);
            final Spinner spinner = itemView.findViewById(R.id.value);

            name.setText(keys[getAdapterPosition()]);

            spinner.setSelection(attr.getInt(keys[getAdapterPosition()]));
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    attr.put(keys[getAdapterPosition()],position+"");
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

        public InputTypeHolder(View itemView) {
            super(itemView);
        }
    }

    class ColorHolder extends BaseHolder{

        public void bind(){
            final TextView name = itemView.findViewById(R.id.name);
            final TextView colorText = itemView.findViewById(R.id.value);
            final View colorShow = itemView.findViewById(R.id.colorView);
            final View colorView = itemView.findViewById(R.id.color);

            name.setText(keys[getAdapterPosition()]);

            String colorValue = attr.getString(keys[getAdapterPosition()]);
            if (TextUtils.isEmpty(colorValue)){
                colorText.setText("无");
                colorShow.setBackgroundColor(Color.TRANSPARENT);
            }else{
                colorText.setText(colorValue);
                colorShow.setBackgroundColor(Color.parseColor(colorValue));
            }

            colorView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new ColorPickerDialog()
                            .withColor(Color.BLACK) // the default / initial color
                            .withPresets(new int[]{0xFFF44336,0xFFE91E63,0xFF9C27B0,0xFF673AB7
                                    ,0xFF3F51B5,0xFF2196F3,0xFF03A9F4,0xFF00BCD4,0xFF009688
                                    ,0xFF4CAF50,0xFF8BC34A,0xFFCDDC39,
                                    0xFFFFEB3B,0xFFFFC107,0xFFFF9800,0xFFFF5722,0xFF795548,0xFF9E9E9E,0xFF607D8B})
                            .withListener((@Nullable ColorPickerDialog dialog, int color)->{
                                colorShow.setBackgroundColor(color);
                                String c = uidesign.project.Utils.Color.colorToHex(color);
                                attr.put(keys[getAdapterPosition()],c);
                                colorText.setText(c);
                            })
                            .show(((BaseActivity)context).getSupportFragmentManager(), "颜色选择器");
                }
            });
        }

        public ColorHolder(View itemView) {
            super(itemView);
        }
    }


    class ClickHolder extends BaseHolder{

        public void bind(){
            final TextView name = itemView.findViewById(R.id.name);
            final TextView text = itemView.findViewById(R.id.value);
            name.setText(keys[getAdapterPosition()]);
            String value = attr.getString(keys[getAdapterPosition()]);
            if (value.equals("") ||value.equals("无")){
                text.setText("无");
                text.setOnClickListener(v->{
                    ActionsFragment appsFragment = new ActionsFragment();
                    appsFragment.setOnActionSelectedListener((File file)->{
                        attr.put(keys[getAdapterPosition()],file.getAbsolutePath());
                        text.setText("执行["+FileUtils.getFileNameNoExtension(file)+"]");
                        appsFragment.dismiss();
                    });
                    appsFragment.show(((AppCompatActivity)context).getSupportFragmentManager(),"chooseAction");
                });
            }else{
                String actionName = FileUtils.getFileNameNoExtension(value);
                text.setText("执行["+actionName+"]");
                text.setOnClickListener(v->{
                    new AlertDialog.Builder(context)
                            .setItems(new String[]{"取消","选择脚本"},(d,i)->{
                                if (i == 1){
                                    ActionsFragment appsFragment = new ActionsFragment();
                                    appsFragment.setOnActionSelectedListener((File file)->{
                                        attr.put(keys[getAdapterPosition()],file.getAbsolutePath());
                                        text.setText("执行["+FileUtils.getFileNameNoExtension(file)+"]");
                                        appsFragment.dismiss();
                                    });
                                    appsFragment.show(((AppCompatActivity)context).getSupportFragmentManager(),"chooseAction");
                                }else {
                                    attr.put(keys[getAdapterPosition()],"");
                                    text.setText("无");
                                }
                            }).show();
                });
            }

        }

        public ClickHolder(View itemView) {
            super(itemView);
        }
    }


    abstract class BaseHolder extends RecyclerView.ViewHolder{

        public abstract void bind();

        public BaseHolder(View itemView) {
            super(itemView);
        }
    }
}
