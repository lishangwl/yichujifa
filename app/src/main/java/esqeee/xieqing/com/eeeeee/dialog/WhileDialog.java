package esqeee.xieqing.com.eeeeee.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xieqing.codeutils.util.PhoneUtils;
import com.xieqing.codeutils.util.ReflectUtils;

import esqeee.xieqing.com.eeeeee.ConditionsActivity;
import esqeee.xieqing.com.eeeeee.MyApp;
import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.listener.OnActionAddListener;

public class WhileDialog extends Dialog {
    private static WhileDialog whileDialog;
    private JSONBean jsonObject;
    private Context context;
    private boolean isAdd = true;
    private View rootView;
    private OnActionAddListener onActionAddListener;
    private Button addCondition;
    private View condition;
    private TextView condition_text;
    private ImageView condition_image;
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

    public WhileDialog setOnDismiss(@Nullable OnDismissListener listener) {
        setOnDismissListener(listener);
        return this;
    }
    public static WhileDialog getWhileDialog(Context context, JSONBean jsonObject) {
        whileDialog = new WhileDialog(context);
        whileDialog.getWindow().setType(MyApp.getFloatWindowType());
        whileDialog.setJsonObject(jsonObject);

        return whileDialog;
    }

    public WhileDialog(Context context){
        super(context);

        this.context = context;
        initDialog();
    }

    private void initView() {
        addCondition = rootView.findViewById(R.id.addCondition);
        condition = rootView.findViewById(R.id.condition);
        editTrueCondition = rootView.findViewById(R.id.editTrueCondition);
        condition_text = rootView.findViewById(R.id.condition_text);
        ok = rootView.findViewById(R.id.ok);

        rootView.findViewById(R.id.useNot).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jsonObject.put("useNot",!useNot());
                initData();
            }
        });

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
        View.OnClickListener clickListener =new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ConditionAddDialog.getConditionAddDialog(context,WhileDialog.this).show(conditionListener);
            }
        };
        View.OnClickListener editclickListener =new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!hasCondition()){
                    Toast.makeText(context,"您还没添加条件", Toast.LENGTH_SHORT).show();
                    PhoneUtils.vibrateShort();
                    return;
                }
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
        condition.setOnClickListener(clickListener);
        addCondition.setOnClickListener(clickListener);
        editTrueCondition.setOnClickListener(editclickListener);

        condition_image = rootView.findViewById(R.id.condition_image);
    }

    private void initDialog() {
        rootView = View.inflate(context, R.layout.dialog_condition_while,null);
        setContentView(rootView);
        Window window = getWindow();
        window.setType(MyApp.getFloatWindowType());
        window.setBackgroundDrawable(new ColorDrawable(0));
        initView();
    }

    public WhileDialog setOnActionAddListener(OnActionAddListener onActionAddListener) {
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

    private boolean useNot(){
        if (!jsonObject.has("useNot")){
            return false;
        }

        return jsonObject.getBoolean("useNot");
    }

    private void initData(){
        ((TextView)rootView.findViewById(R.id.useNot)).setText(useNot()?"使用正条件":"使用反条件");
        ((TextView)rootView.findViewById(R.id.condition_false)).setText(!useNot()?"条件未达成   (如果条件不符合，则跳出循环，执行下一个)":"条件达成   (如果条件符合，则跳出循环，执行下一个)");
        ((TextView)rootView.findViewById(R.id.condition_true)).setText(!useNot()?"条件达成   (如果条件符合，则循环进行指定的动作)":"条件未达成   (如果条件不符合，则循环进行指定的动作)");

        if (hasCondition()){
            condition.setVisibility(View.VISIBLE);
            addCondition.setVisibility(View.GONE);
            JSONBean condition = jsonObject.getJson("condition");
            JSONBean param = condition.getJson("param");
            int type = condition.getInt("actionType");
            switch (type){
                case 44:
                    condition_image.setImageDrawable(context.getResources().getDrawable(R.drawable.action_3));
                    condition_text.setText("屏幕中是否含有["+param.getString("text")+"]");
                    break;
                case 45:
                    condition_image.setImageDrawable(context.getResources().getDrawable(R.drawable.action_3));
                    condition_text.setText("区域["+param.getInt("top")+","+param.getInt("left")+"]["+ param.getInt("right")+","+param.getInt("bottom")+"]中是否含有["+param.getString("text")+"]");
                    break;
                case 47:
                    condition_image.setImageBitmap(BitmapFactory.decodeFile(param.getString("fileName")));
                    condition_text.setText("屏幕中是否含有该图片");
                    break;
                case 48:
                    condition_image.setImageBitmap(BitmapFactory.decodeFile(param.getString("fileName")));
                    condition_text.setText("区域["+param.getInt("top")+","+param.getInt("left")+"]["+ param.getInt("right")+","+param.getInt("bottom")+"]中是否含有该图片");
                    break;
                case 54:
                    condition_image.setImageDrawable(new ColorDrawable(Color.argb(255,param.getInt("red"),param.getInt("green"),param.getInt("blue"))));
                    condition_text.setText("屏幕中是否有该颜色");
                    break;
                case 63:
                    condition_image.setImageDrawable(new ColorDrawable(Color.argb(255,param.getInt("red"),param.getInt("green"),param.getInt("blue"))));
                    condition_text.setText("["+param.getInt("top")+","+param.getInt("left")+"]["+ param.getInt("right")+","+param.getInt("bottom")+"]是否有该颜色");
                    break;
            }
        }else{
            condition.setVisibility(View.GONE);
            addCondition.setVisibility(View.VISIBLE);
        }
    }

    public JSONBean getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONBean jsonObject) {
        this.jsonObject = jsonObject;
        if (this.jsonObject == null){
            this.jsonObject = new JSONBean();
            this.jsonObject.put("actionType", 52)
                        .put("witeTime",0)
                        .put("param",new JSONBean())
                        .put("trueDo",new JSONBean());
            isAdd = true;
        }else{
            isAdd = false;
        }
        initData();
    }
}
