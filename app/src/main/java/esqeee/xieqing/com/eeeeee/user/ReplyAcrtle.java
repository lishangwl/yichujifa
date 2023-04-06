package esqeee.xieqing.com.eeeeee.user;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.xieqing.codeutils.util.DeviceUtils;
import com.xieqing.codeutils.util.FileUtils;
import com.xieqing.codeutils.util.ImageUtils;
import com.xieqing.codeutils.util.ScreenUtils;
import com.xieqing.codeutils.util.SnackbarUtils;
import com.xieqing.codeutils.util.ToastUtils;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.util.List;

import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.bbs.BBS;
import esqeee.xieqing.com.eeeeee.bbs.callback.CallBack;
import esqeee.xieqing.com.eeeeee.bbs.callback.CallResult;
import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.listener.ActivityResultListener;
import esqeee.xieqing.com.eeeeee.ui.BaseActivity;

import static android.app.Activity.RESULT_OK;

public class ReplyAcrtle extends PopupWindow {

    private View view;
    private String image = "";
    private EditText editText;
    private int aid = 0;
    BaseActivity context;
    public ReplyAcrtle(BaseActivity context,int aid){
        super(context);
        this.aid = aid;
        this.context = context;
        view = LayoutInflater.from(context).inflate(R.layout.acrtle_reply_dialog,null,false);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ScreenUtils.getScreenWidth(),ScreenUtils.getScreenHeight());
        view.setLayoutParams(layoutParams);

        setBackgroundDrawable(new ColorDrawable(Color.parseColor("#50000000")));
        setAnimationStyle(android.R.style.Animation_InputMethod);
        setContentView(view);
        setHeight(-1);
        setWidth(ScreenUtils.getScreenWidth());
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setFocusable(true);
        setOutsideTouchable(true);

        editText = view.findViewById(R.id.content);
        ImageView imageView = view.findViewById(R.id.image);
        ImageView close = view.findViewById(R.id.close);

        close.setOnClickListener(v->{
            imageView.setImageResource(R.mipmap.ic_add);
            image = "";
            close.setVisibility(View.GONE);
        });
        imageView.setOnClickListener(v->{
            context.addActivityResultListener(new ActivityResultListener() {
                @Override
                public void onActivityResult(int requestCode, int resultCode, Intent data) {
                    context.removeActivityResultListener(this);
                    if (requestCode == 5481 && resultCode == RESULT_OK) {
                        List<String> result = Matisse.obtainPathResult(data);
                        if (result.size()>0){
                            if (FileUtils.getFileLength(result.get(0)) > 2 * 1024 * 1024){
                                ToastUtils.showLong("大哥，图片有点大了。。。选个小于2M的吧");
                                return;
                            }
                            imageView.setImageBitmap(ImageUtils.getBitmap(result.get(0)));
                            image = result.get(0);
                            close.setVisibility(View.VISIBLE);
                        }
                    }
                }
            });
            Matisse.from(context)
                    .choose(MimeType.ofImage())
                    .countable(true)
                    .maxSelectable(1)
                    .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                    .thumbnailScale(0.85f)
                    .imageEngine(new GlideEngine())
                    .forResult(5481);
        });
        view.findViewById(R.id.commit).setOnClickListener(v->{
            if (editText.getText().toString().trim().length() == 0){
                SnackbarUtils.with(view).setMessage("至少说一点点啊....").showError();
                return;
            }
            context.showProgress("发表中");
            if (!TextUtils.isEmpty(image)){
                User.getUser().uploadImage(image, new User.UploadListener() {
                    @Override
                    public void failed(String s) {
                        context.disProgress();
                        ToastUtils.showShort("上传图片失败："+s);
                    }

                    @Override
                    public void progress(int progress) {

                    }

                    @Override
                    public void success(String data) {
                        JSONBean jsonBean = new JSONBean(data);
                        if (jsonBean.getInt("code",-1) == 0){
                            image = jsonBean.getString("msg");
                            commit(editText.getText().toString());
                        }else{
                            context.disProgress();
                            ToastUtils.showLong(jsonBean.getString("msg"));
                        }
                    }
                });
            }else{
                commit(editText.getText().toString());
            }
        });
    }

    public void setText(CharSequence text) {
        editText.setText(text);
        editText.setSelection(text.length());
    }

    private int uid = 0;
    public void setId(int uid) {
        this.uid = uid;
    }

    public int getUid() {
        return uid;
    }

    public interface CommitCallBack{
        void onCommited(JSONBean jsonBean);
    }

    CommitCallBack commitCallBack;

    public void setCommitCallBack(CommitCallBack commitCallBack) {
        this.commitCallBack = commitCallBack;
    }

    private void commit(String content) {
        BBS.reply(this.aid,this.uid,User.getUser(),content,image,new CallBack(){
            @Override
            public void callBack(CallResult result) {
                context.disProgress();
                ToastUtils.showLong(result.getMessage());
                if (result.getCode() == 0){
                    dismiss();
                    if (commitCallBack!=null){
                        JSONBean jsonBean = new JSONBean();
                        jsonBean.put("uid",User.getUser().getUid());
                        jsonBean.put("content",content);
                        jsonBean.put("time",System.currentTimeMillis()/1000);
                        jsonBean.put("device", DeviceUtils.getMessage());
                        jsonBean.put("username",User.getUser().getName());
                        jsonBean.put("atUid",uid);
                        jsonBean.put("image",image);
                        commitCallBack.onCommited(jsonBean);
                    }
                    ((EditText)view.findViewById(R.id.content)).setText("");
                    ((ImageView)view.findViewById(R.id.image)).setImageResource(R.mipmap.ic_add);
                    image = "";
                    ((ImageView)view.findViewById(R.id.close)).setVisibility(View.GONE);
                }


            }
        });
    }
}
