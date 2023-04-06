package esqeee.xieqing.com.eeeeee.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.textfield.TextInputEditText;
import androidx.appcompat.app.AlertDialog;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.xieqing.codeutils.util.AppUtils;
import com.xieqing.codeutils.util.FileUtils;
import com.xieqing.codeutils.util.ToastUtils;
import com.yicu.yichujifa.apk.Project;
import com.yicu.yichujifa.apk.ProjectManager;
import com.yicu.yichujifa.apk.bean.Xml;
import com.yicu.yichujifa.apk.bean.XmlViewHolder;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;
import com.zhihu.matisse.internal.ui.widget.CropImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import esqeee.xieqing.com.eeeeee.BroswerActivity;
import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.fragment.XmlsFragment;
import esqeee.xieqing.com.eeeeee.library.RuntimeLog;
import esqeee.xieqing.com.eeeeee.library.captrue.IntentExtras;

public class Apk extends BaseActivity{
    @Override
    public int getContentLayout() {
        return R.layout.activity_apk;
    }

    public static Project project;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportToolBarWithBack(R.id.toolbar);


        project = ProjectManager.getInstance().getProject(getIntent().getStringExtra("id"));



        stupAppInfo();

        stupXmlItems();
    }




    private void stupAppInfo() {
        version.setText(String.valueOf(project.getAppVersionCode()));
        versionName.setText(project.getAppVersionName());
        apkName.setText(project.getAppName());
        packageName.setText(project.getAppPackage());
        Glide.with(self()).load(project.getIcon())
                .error(R.mipmap.ic_add).into(icon);
    }




    private void stupXmlItems() {
        file_content.removeAllViews();
        for(int i = 0;i<project.getXml().getItemCount();i++){
            Xml.XmlItem item = project.getXml().getItem(i);
            stupXmlItem(item);
        }
    }




    private void stupXmlItem(Xml.XmlItem item){
        XmlViewHolder holder = new XmlViewHolder(self());
        holder.getView().setOnLongClickListener(v->{

            new AlertDialog.Builder(self())
                    .setTitle("选择更多操作")
                    .setItems(new String[]{"重新载入文件", "删除文件"}, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == 1){
                                new AlertDialog.Builder(self()).setTitle("确定删除吗？")
                                        .setNegativeButton("确定",(d,in)->{
                                            file_content.removeView(holder.getView());
                                            project.getXml().remove(item);
                                        }).create().show();
                            }else {
                                file_content.removeView(holder.getView());
                                project.getXml().remove(item);
                                Xml.XmlItem i = new Xml.XmlItem(FileUtils.getFileNameNoExtension(item.getPath()),item.getPath());
                                if (project.getXml().addItem(i)){
                                    stupXmlItem(i);
                                }
                            }
                        }
                    }).show();


            return true;
        });
        holder.getView().setOnClickListener(v->{
            Intent intent = new Intent(self(), ApkDetailActivity.class);
            IntentExtras.newExtras()
                    .put("file", item.getAction())
                    .putInIntent(intent);
            startActivity(intent);
        });
        holder.name.setText(item.getName());
        holder.path.setText(item.getPath());
        file_content.addView(holder.getView());
    }






    @OnClick(R.id.file_add)
    public void addFile(){
        XmlsFragment xmlsFragment = new XmlsFragment();
        xmlsFragment.setOnActionSelectedListener(b->{
            Xml.XmlItem item = new Xml.XmlItem(FileUtils.getFileNameNoExtension(b),b.getAbsolutePath());
            if (project.getXml().addItem(item)){
                stupXmlItem(item);
            }
            xmlsFragment.dismiss();
        });
        xmlsFragment.show(getSupportFragmentManager(),"xmls");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ProjectManager.getInstance().save(project);
    }

    @OnTextChanged(R.id.edit_apkName)
    void setVersion(){
        project.setAppVersionCode(Integer.parseInt(version.getText().toString()));
    }

    @OnTextChanged(R.id.edit_packageName)
    void setPackageName(){
        project.setAppPackage(packageName.getText().toString());
    }

    @OnTextChanged(R.id.edit_appVersionName)
    void setVersionName(){
        project.setAppVersionName(versionName.getText().toString());
    }

    @OnTextChanged(R.id.edit_apkName)
    void setApkName(){
        project.setAppName(apkName.getText().toString());
    }

    @BindView(R.id.edit_appVersion)
    TextInputEditText version;

    @BindView(R.id.edit_appVersionName)
    TextInputEditText versionName;

    @BindView(R.id.edit_packageName)
    TextInputEditText packageName;

    @BindView(R.id.edit_apkName)
    TextInputEditText apkName;

    @BindView(R.id.edit_icon)
    ImageView icon;

    @BindView(R.id.file_content)
    LinearLayout file_content;


    @OnClick(R.id.edit_icon)
    void chooseIcon(){
        Matisse.from(this)
                .choose(MimeType.ofAll(), false)      // 展示所有类型文件（图片 视频 gif）
                .capture(true)                        // 可拍照
                .countable(true)                      // 记录文件选择顺序
                .isCrop(true)                         // 开启裁剪
                .captureStrategy(new CaptureStrategy(true, "cache path"))
                .maxSelectable(1)                     // 最多选择一张
                .cropStyle(CropImageView.Style.RECTANGLE)   // 方形裁剪CIRCLE为圆形裁剪
                .isCropSaveRectangle(true)                  // 裁剪后保存方形（只对圆形裁剪有效）
                .gridExpectedSize(400)
                .cropFocusHeight(500)
                .cropFocusWidth(500)
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                .thumbnailScale(0.8f)
                .imageEngine(new GlideEngine())
                .forResult(5481);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 5481 && resultCode == RESULT_OK) {
            List<String> result = Matisse.obtainPathResult(data);
            if (result.size()>0){
                project.setIcon(result.get(0));
                Glide.with(self()).load(result.get(0)).into(icon);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.config:
                startActivity(new Intent(self(),ApkConfigActivity.class));
                break;
            case R.id.help:

                break;
            case R.id.bao:
                showProgress("打包中");
                new Thread(()->{
                    try {
                        final String path = project.build();
                        runOnUiThread(()->{
                            AppUtils.installApp(path);
                        });
                        Thread.sleep(1000);
                    }catch (Exception e){
                        RuntimeLog.e(e);
                        runOnUiThread(()->{
                            ToastUtils.showLong("打包失败，请查看日志");
                        });
                    }
                    disProgress();
                }).start();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_ui_apk,menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void question(View v){
        new AlertDialog.Builder(self())
                .setTitle("帮助")
                .setMessage("由于窗口被创建时被销毁时，基于无障碍的权限没有打开，则绑定于创建时或销毁时的脚本无法启动，因此无法打包该事件下的脚本文件。\n请在其他地方进行操作，比如设置一个初始化按钮。\n请悉知!")
                .setPositiveButton("确定",null).show();
    }
}
