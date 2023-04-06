package esqeee.xieqing.com.eeeeee.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import com.xieqing.codeutils.util.DeviceUtils;
import com.xieqing.codeutils.util.FileUtils;
import com.xieqing.codeutils.util.SPUtils;
import com.xieqing.codeutils.util.SnackbarUtils;
import com.xieqing.codeutils.util.ToastUtils;
import com.yicu.yichujifa.ui.colorpicker.dialogs.ColorPickerDialog;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.io.File;
import java.net.URLEncoder;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.bbs.BBS;
import esqeee.xieqing.com.eeeeee.bbs.callback.CallBack;
import esqeee.xieqing.com.eeeeee.bbs.callback.CallResult;
import esqeee.xieqing.com.eeeeee.bean.JSONArrayBean;
import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.dialog.InputTextDialog;
import esqeee.xieqing.com.eeeeee.dialog.input.InputLine;
import esqeee.xieqing.com.eeeeee.dialog.input.InputTextLine;
import esqeee.xieqing.com.eeeeee.fragment.ActionsFragment;
import esqeee.xieqing.com.eeeeee.user.User;
import esqeee.xieqing.com.eeeeee.view.RichEditor;

public class EditorActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.richEditor)
    RichEditor richEditor;

    private String title = "";

    private int tid = 0;
    private String cate = "";
    @Override
    public int getContentLayout() {
        return R.layout.activity_editor;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tid = getIntent().getIntExtra("tid",0);
        cate = getIntent().getStringExtra("cate");

        toolbar.setTitle("发布 - "+cate);
        supportToolBarWithBack(toolbar);
        richEditor.setEditorFontSize(15);
        richEditor.setPlaceholder("总得写点什么吧...");
        richEditor.setCallBack(new RichEditor.OnTextChangeListener() {
            @Override
            public void onTextChange(String text) {
                title = text;
                SPUtils.getInstance("editor").put("title",text);
            }
        });
        richEditor.loadUrl("file:///android_asset/editor/editor.html");

        richEditor.setOnTextChangeListener(new RichEditor.OnTextChangeListener() {
            @Override
            public void onTextChange(String text) {
                SPUtils.getInstance("editor").put("content",richEditor.getHtml());
            }
        });

        richEditor.setOnInitialLoadListener(new RichEditor.AfterInitialLoadListener() {
            @Override
            public void onAfterInitialLoad(boolean isReady) {

                richEditor.setHtml( SPUtils.getInstance("editor").getString("content"));
                richEditor.exec("javascript:document.getElementById('editor2').value='"+SPUtils.getInstance("editor").getString("title")+"';");
                title = SPUtils.getInstance("editor").getString("title");
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_commit,menu);
        return true;
    }

    @OnClick(R.id.edit_redo)
    public void redo(){
        richEditor.redo();
    }

    @OnClick(R.id.edit_undo)
    public void undo(){
        richEditor.undo();
    }

    @OnClick(R.id.edit_align_left)
    public void align_left(){
        richEditor.setAlignLeft();
    }

    @OnClick(R.id.edit_align_center)
    public void align_center(){
        richEditor.setAlignCenter();
    }

    @OnClick(R.id.edit_align_right)
    public void align_right(){
        richEditor.setAlignRight();
    }

    private boolean isBlockquoted = false;
    @OnClick(R.id.edit_blockquote)
    public void blockquote(){
        if (isBlockquoted){
            richEditor.removeFormat();
            isBlockquoted = false;
            return;
        }
        richEditor.exec("javascript:document.execCommand('formatBlock', false, '<blockquote>');");
        isBlockquoted = true;
    }

    @OnClick(R.id.edit_bold)
    public void bold(){
        richEditor.setBold();
    }

    @OnClick(R.id.edit_bullets)
    public void bullets(){
        richEditor.setBullets();
    }

    @OnClick(R.id.edit_indent)
    public void indent(){
        richEditor.setIndent();
    }

    @OnClick(R.id.edit_outdent)
    public void outdent(){
        richEditor.setOutdent();
    }

    @OnClick(R.id.edit_italic)
    public void italic(){
        richEditor.setItalic();
    }

    @OnClick(R.id.edit_numbers)
    public void numbers(){
        richEditor.setNumbers();
    }

    @OnClick(R.id.edit_strikethrough)
    public void strikethrough(){
        richEditor.setStrikeThrough();
    }


    @OnClick(R.id.edit_h1)
    public void h1(){
        richEditor.setHeading(1);
    }

    @OnClick(R.id.edit_h2)
    public void h2(){
        richEditor.setHeading(2);
    }

    @OnClick(R.id.edit_h3)
    public void h3(){
        richEditor.setHeading(3);
    }

    @OnClick(R.id.edit_h4)
    public void h4(){
        richEditor.setHeading(4);
    }

    @OnClick(R.id.edit_h5)
    public void h5(){
        richEditor.setHeading(5);
    }

    @OnClick(R.id.edit_h6)
    public void h6(){
        richEditor.setHeading(6);
    }


    @OnClick(R.id.edit_txt_color)
    public void color(){
        new ColorPickerDialog()
                .withColor(Color.BLACK) // the default / initial color
                .withPresets(new int[]{0xFFF44336,0xFFE91E63,0xFF9C27B0,0xFF673AB7
                        ,0xFF3F51B5,0xFF2196F3,0xFF03A9F4,0xFF00BCD4,0xFF009688
                        ,0xFF4CAF50,0xFF8BC34A,0xFFCDDC39,
                        0xFFFFEB3B,0xFFFFC107,0xFFFF9800,0xFFFF5722,0xFF795548,0xFF9E9E9E,0xFF607D8B})
                .withListener((@Nullable ColorPickerDialog dialog, int color)->{
                    richEditor.setTextColor(color);
                })
                .show(getSupportFragmentManager(), "颜色选择器");
    }

    @OnClick(R.id.edit_backcolor)
    public void backcolor(){
        new ColorPickerDialog()
                .withColor(Color.BLACK) // the default / initial color
                .withPresets(new int[]{0xFFF44336,0xFFE91E63,0xFF9C27B0,0xFF673AB7
                        ,0xFF3F51B5,0xFF2196F3,0xFF03A9F4,0xFF00BCD4,0xFF009688
                        ,0xFF4CAF50,0xFF8BC34A,0xFFCDDC39,
                        0xFFFFEB3B,0xFFFFC107,0xFFFF9800,0xFFFF5722,0xFF795548,0xFF9E9E9E,0xFF607D8B})
                .withListener((@Nullable ColorPickerDialog dialog, int color)->{
                    richEditor.setTextBackgroundColor(color);
                })
                .show(getSupportFragmentManager(), "颜色选择器");
    }

    @OnClick(R.id.edit_image)
    public void image(){
        Matisse.from(this)
                .choose(MimeType.ofImage())
                .countable(true)
                .maxSelectable(1)
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(new GlideEngine())
                .forResult(5481);
    }

    @OnClick(R.id.edit_script)
    public void script(){
        ActionsFragment appsFragment = new ActionsFragment();
        appsFragment.setOnActionSelectedListener((File file)->{
            showProgress("上传中");
            String actionId = file.getAbsolutePath();
            String size = FileUtils.getFileSize(actionId);
            String name= (file == null ? "Null" : FileUtils.getFileName(file));
            appsFragment.dismiss();
            BBS.uploadScript(actionId,name,User.getUser(),new CallBack(){
                @Override
                public void callBack(CallResult result) {
                    disProgress();
                    if (result.getCode()!=0){
                        ToastUtils.showLong("上传失败："+result.getMessage());
                    }else{
                        String id = result.getString("id");
                        richEditor.exec("document.execCommand(\"insertHTML\", false, \"<div class='ycf-file' contenteditable='false'><button class='ycf-file-btn mdui-btn mdui-ripple mdui-color-teal-accent' onclick='window.app.down("+id+")'>下载</button><div class='ycf-file-content'><span>"+name+"</span></br><small>"+size+"</small></div></div>\");");
                    }
                }
            });
        });
        appsFragment.show(getSupportFragmentManager(),"chooseAction");
    }

    @OnClick(R.id.edit_link)
    public void link(){
        new InputTextDialog(this,false).setTitle("请输入")
                .addInputLine(new InputTextLine("链接",""))
                .addInputLine(new InputTextLine("链接文字(可选)","").setCanInputNull(true))
                .setInputTextListener((InputLine[] result)->{
                    String url = (String) result[0].getResult();
                    String title = (String) result[1].getResult();
                if (!(url.startsWith("http://") || url.startsWith("https://"))){
                    url = "http://"+url;
                }
                if (title.trim().length() == 0){
                    title = url;
                }
                richEditor.insertLink(url,title);
        }).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 5481 && resultCode==RESULT_OK){
            List<String> result = Matisse.obtainPathResult(data);
            if (result.size()>0){
                new InputTextDialog(EditorActivity.this,false).setTitle("请输入")
                        .addInputLine(new InputTextLine("图片介绍(可选)","").setCanInputNull(true))
                        .setInputTextListener((InputLine[] resultI)->{
                    String text = (String) resultI[0].getResult();
                    uploadImage(result.get(0),text);
                }).show();
            }
        }
    }

    private void uploadImage(String path,String content){
        showProgress("提示","正在上传图片，请稍等");
        User.getUser().uploadImage(path, new User.UploadListener() {
            @Override
            public void failed(String s) {
                disProgress();
                ToastUtils.showShort("上传失败："+s);
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
                    String html = "<div style='text-align: center;'><img src='"+image+"' onclick='window.app.openImage(this.src)'/>"+(TextUtils.isEmpty(content.trim())?"":"<div class='image-caption'>"+content+"</div>")+"</div>";
                    richEditor.exec("document.execCommand(\"insertHTML\", false, \""+html+"\")");
                }else{
                    ToastUtils.showLong(jsonBean.getString("msg"));
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_complete:
                commit();
                break;
            case R.id.action_cate:
                chooseCate();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void chooseCate() {
        showProgress("稍等");
        esqeee.xieqing.com.eeeeee.bbs.BBS.getCates(new CallBack(){
            @Override
            public void callBack(CallResult result) {
                disProgress();
                if (result.getCode() == 0){
                    JSONArrayBean cates = result.getArray("data");
                    String[] cateString = new String[cates.length()];
                    for (int i = 0;i<cates.length();i++){
                        cateString[i] = cates.getJson(i).getString("name");
                    }
                    new AlertDialog.Builder(EditorActivity.this)
                            .setItems(cateString, (DialogInterface dialogInterface, int i) ->{
                                tid = cates.getJson(i).getInt("id");
                                cate = cates.getJson(i).getString("name");
                                toolbar.setTitle("发布 - "+cate);
                            }).setTitle("选择版块")
                            .create()
                            .show();
                }else{
                    ToastUtils.showLong("error:"+result.getMessage());
                }
            }
        });
    }

    private void commit() {
        if (title.length() <4  || title.length() > 20){
            Snackbar.make(richEditor,"标题应该在 4 - 20 字之间",Snackbar.LENGTH_LONG).show();
            return;
        }
        if (richEditor.getHtml() == null || richEditor.getHtml().length() == 0){
            Snackbar.make(richEditor,"内容不能为空",Snackbar.LENGTH_LONG).show();
            return;
        }
        JSONBean content = new JSONBean();
        content.put("content",richEditor.getHtml());
        content.put("title",title);
        content.put("tid",tid);
        content.put("device", DeviceUtils.getMessage());
        content.put("uid",User.getUser().getUid());
        content.put("token",User.getUser().getToken());
        BBS.commit(URLEncoder.encode(content.toString()),new CallBack(){
            @Override
            public void callBack(CallResult result) {
                if (result.getCode()!=0){
                    SnackbarUtils.with(richEditor).setMessage(result.getMessage()).showError();
                }else{
                    SnackbarUtils.with(richEditor).setMessage(result.getMessage()).showSuccess();

                    SPUtils.getInstance("editor").put("content","");
                    SPUtils.getInstance("editor").put("title","");
                    startActivity(new Intent(EditorActivity.this,AcrtleActivity.class).putExtra("aid",result.getInt("data")));
                    finish();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        richEditor.removeAllViews();
        richEditor.clearView();
        richEditor.destroy();
    }
}
