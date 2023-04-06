package esqeee.xieqing.com.eeeeee.fragment;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.xieqing.codeutils.util.ToastUtils;
import com.yicu.yichujifa.ui.theme.ThemeManager;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;
import com.zhihu.matisse.internal.ui.widget.CropImageView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.bbs.BBS;
import esqeee.xieqing.com.eeeeee.bbs.callback.CallBack;
import esqeee.xieqing.com.eeeeee.bbs.callback.CallResult;
import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.dialog.InputTextDialog;
import esqeee.xieqing.com.eeeeee.dialog.input.InputLine;
import esqeee.xieqing.com.eeeeee.dialog.input.InputTextLine;
import esqeee.xieqing.com.eeeeee.ui.BaseActivity;
import esqeee.xieqing.com.eeeeee.user.User;

public class DetailActivity extends BaseActivity {


    @Override
    public int getContentLayout() {
        return R.layout.activity_detail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userName.setText(User.getUser().getName());
        userEmail.setText(User.getUser().getEmail());
        userNick.setText(User.getUser().getNick());
        BBS.attachIconNoCashe(userHead,User.getUser().getUid());

        supportToolBarWithBack(toolbar);

        ThemeManager.attachTheme((Button)findViewById(R.id.login_out));
    }


    @BindView(R.id.user_name)
    TextView userName;

    @BindView(R.id.user_nick)
    TextView userNick;

    @BindView(R.id.user_head)
    ImageView userHead;

    @BindView(R.id.user_email)
    TextView userEmail;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @OnClick(R.id.item_nick)
    public void nick(){
        new InputTextDialog(this,false).setTitle("请输入昵称")
                .addInputLine(new InputTextLine("","").setCanInputNull(true))
                .setInputTextListener((InputLine[] resultI)->{
                    String text = (String) resultI[0].getResult();
                    BBS.editUserNick(User.getUser(),text,new CallBack(){
                        @Override
                        public void callBack(CallResult result) {
                            if (result.getCode() != 0){
                                ToastUtils.showLong(result.getMessage());
                            }else{
                                userNick.setText(text);
                                User.getUser().setNick(text);
                                EventBus.getDefault().post(User.getUser());
                            }
                        }
                    });
                }).show();
    }

    @OnClick(R.id.login_out)
    public void out(){
        User.getUser().loginOut();
        EventBus.getDefault().post(User.getUser());
        finish();
    }

    @OnClick(R.id.item_pass)
    public void pass(){
        new InputTextDialog(this,false).setTitle("请输入新密码")
                .addInputLine(new InputTextLine("","").setCanInputNull(true))
                .setInputTextListener((InputLine[] resultI)->{
                    String text = (String) resultI[0].getResult();
                    BBS.editUserPass(User.getUser(),text,new CallBack(){
                        @Override
                        public void callBack(CallResult result) {
                            if (result.getCode() != 0){
                                ToastUtils.showLong(result.getMessage());
                            }else{
                                EventBus.getDefault().post(User.getUser());
                            }
                        }
                    });
                }).show();
    }
    @OnClick(R.id.item_head)
    public void head(){
        Matisse.from(this)
                .choose(MimeType.ofAll(), false)      // 展示所有类型文件（图片 视频 gif）
                .capture(true)                        // 可拍照
                .countable(true)                      // 记录文件选择顺序
                .captureStrategy(new CaptureStrategy(true, "cache path"))
                .maxSelectable(1)                     // 最多选择一张
                .isCrop(true)                         // 开启裁剪
                .cropOutPutX(400)                     // 设置裁剪后保存图片的宽高
                .cropOutPutY(400)
                .cropFocusHeight(500)
                .cropFocusWidth(500)         // 设置裁剪后保存图片的宽高
                .cropStyle(CropImageView.Style.RECTANGLE)   // 方形裁剪CIRCLE为圆形裁剪
                .isCropSaveRectangle(true)                  // 裁剪后保存方形（只对圆形裁剪有效）
                .gridExpectedSize(400)
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                .thumbnailScale(0.8f)
                .imageEngine(new GlideEngine())
                .forResult(5481);
    }

    private void editHead(String url){
        BBS.editUserHead(User.getUser(),url,new CallBack(){
            @Override
            public void callBack(CallResult result) {
                if (result.getCode() != 0){
                    ToastUtils.showLong(result.getMessage());
                }else{
                    BBS.attachIconNoCashe(userHead,User.getUser().getUid());
                    EventBus.getDefault().post(User.getUser());
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 5481 && resultCode==RESULT_OK){
            List<String> result = Matisse.obtainPathResult(data);
            if (result.size()>0){
                showProgress("上传中");
                User.getUser().uploadImage(result.get(0), new User.UploadListener() {
                    @Override
                    public void failed(String s) {
                        disProgress();
                        ToastUtils.showLong(s);
                    }

                    @Override
                    public void progress(int progress) {

                    }

                    @Override
                    public void success(String data) {
                        disProgress();
                        JSONBean jsonBean = new JSONBean(data);
                        if (jsonBean.getInt("code",-1) == 0){
                            String image = jsonBean.getString("msg");
                            editHead(image);
                        }else{
                            ToastUtils.showLong(jsonBean.getString("msg"));
                        }
                    }
                });
            }
        }
    }
}
