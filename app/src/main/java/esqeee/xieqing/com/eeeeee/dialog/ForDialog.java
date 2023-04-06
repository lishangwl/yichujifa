package esqeee.xieqing.com.eeeeee.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.xieqing.codeutils.util.PhoneUtils;
import com.xieqing.codeutils.util.ReflectUtils;

import esqeee.xieqing.com.eeeeee.ConditionsActivity;
import esqeee.xieqing.com.eeeeee.MyApp;
import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.listener.OnActionAddListener;

public class ForDialog extends Dialog {
    private static ForDialog forDialog;
    private JSONBean jsonObject;
    private Context context;
    private boolean isAdd = true;
    private View rootView;
    private OnActionAddListener onActionAddListener;
    private EditText condition;
    private Button ok;
    private Button cannel;
    private Button editTrueCondition;
    private OnActionAddListener conditionListener = new OnActionAddListener() {
        @Override
        public void addAction(JSONBean jsonObject2) {
            jsonObject.put("condition",jsonObject2);
            initData();
        }
    };


    public static ForDialog getForDialog(Context context, JSONBean jsonObject) {
        forDialog = new ForDialog(context);
        forDialog.getWindow().setType(MyApp.getFloatWindowType());
        forDialog.setJsonObject(jsonObject);
        return forDialog;
    }
    public ForDialog setOnDismiss(@Nullable OnDismissListener listener) {
        setOnDismissListener(listener);
        return this;
    }

    public ForDialog(Context context){
        super(context);
        this.context = context;

        initDialog();
    }

    private void initView() {
        condition = rootView.findViewById(R.id.condition);
        editTrueCondition = rootView.findViewById(R.id.editTrueCondition);

        ok = rootView.findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hasCondition()){
                    addAction(jsonObject);
                    dismiss();
                }else{
                    Toast.makeText(context,"您还没添加条件", Toast.LENGTH_SHORT).show();
                    PhoneUtils.vibrateShort();
                }
            }
        });
        cannel = rootView.findViewById(R.id.cannel);
        cannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        condition.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (condition.getText().toString().length()>0){
                    jsonObject.put("condition",Integer.valueOf(condition.getText().toString()));
                }else{
                    jsonObject.remove("condition");
                }
            }
        });
        View.OnClickListener editclickListener =new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReflectUtils.reflect(context).method("closeWindow");
                String key = System.currentTimeMillis()+"";
                Intent intent = new Intent(context, ConditionsActivity.class);
                intent.putExtra("from",0);
                intent.putExtra("key",key);
                context.startActivity(intent);
                dismiss();
                if (isAdd){
                    addAction(jsonObject);
                }
            }
        };
        editTrueCondition.setOnClickListener(editclickListener);

    }

    private void initDialog() {
        rootView = View.inflate(context, R.layout.dialog_condition_for,null);
        setContentView(rootView);
        Window window = getWindow();
        window.setType(MyApp.getFloatWindowType());
        window.setBackgroundDrawable(new ColorDrawable(0));
        initView();
    }

    public ForDialog setOnActionAddListener(OnActionAddListener onActionAddListener) {
        this.onActionAddListener = onActionAddListener;
        return this;
    }

    public void addAction(JSONBean jsonObject){
        if (onActionAddListener == null){
            return;
        }
        onActionAddListener.addAction(jsonObject);
        this.jsonObject = null;
        onActionAddListener = null;
    }

    private boolean hasCondition(){
        return jsonObject.has("condition");
    }

    private void initData(){
        condition.setText(jsonObject.getInt("condition")+"");
    }

    public JSONBean getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONBean jsonObject) {
        this.jsonObject = jsonObject;
        if (this.jsonObject == null){
            this.jsonObject = new JSONBean();
                this.jsonObject.put("actionType", 53)
                        .put("witeTime",0)
                        .put("condition",1)
                        .put("param",new JSONBean())
                        .put("trueDo",new JSONBean());
            isAdd = true;
        }else{
            isAdd = false;
        }
        initData();
    }
}
