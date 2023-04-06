package com.xieqing.uidesign.project.inflater.widget;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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

import com.xieqing.uidesign.R;
import com.xieqing.uidesign.project.model.Attr;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import top.defaults.colorpicker.ColorPickerPopup;

public class AttrListAdapter extends RecyclerView.Adapter<AttrListAdapter.BaseHolder> {

    private Attr attr;
    private String[] keys;


    private Context context;



    public AttrListAdapter(Context context,Attr attr){
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
                    new ColorPickerPopup.Builder(context)
                            .initialColor(Color.BLACK) // Set initial color
                            .enableBrightness(true) // Enable brightness slider or not
                            .enableAlpha(true) // Enable alpha slider or not
                            .okTitle("选择")
                            .cancelTitle("取消")
                            .showIndicator(true)
                            .showValue(true)
                            .build()
                            .show(new ColorPickerPopup.ColorPickerObserver() {
                                @Override
                                public void onColorPicked(int color) {
                                    colorShow.setBackgroundColor(color);
                                    String c = com.xieqing.uidesign.project.Utils.Color.colorToHex(color);
                                    attr.put(keys[getAdapterPosition()],c);
                                    colorText.setText(c);
                                }
                            });
                }
            });
        }

        public ColorHolder(View itemView) {
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
