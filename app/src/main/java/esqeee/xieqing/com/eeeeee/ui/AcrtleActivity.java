package esqeee.xieqing.com.eeeeee.ui;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.graphics.Bitmap;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.widget.ImageView;
import android.widget.TextView;

import com.SuperKotlin.pictureviewer.ImagePagerActivity;
import com.SuperKotlin.pictureviewer.PictureConfig;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.xieqing.codeutils.util.DeviceUtils;
import com.xieqing.codeutils.util.FileIOUtils;
import com.xieqing.codeutils.util.SnackbarUtils;
import com.xieqing.codeutils.util.TimeUtils;
import com.xieqing.codeutils.util.ToastUtils;
import com.xieqing.codeutils.util.Utils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.action.ActionHelper;
import esqeee.xieqing.com.eeeeee.bbs.BBS;
import esqeee.xieqing.com.eeeeee.bbs.callback.CallBack;
import esqeee.xieqing.com.eeeeee.bbs.callback.CallResult;
import esqeee.xieqing.com.eeeeee.bean.JSONArrayBean;
import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.user.ReplyAcrtle;
import esqeee.xieqing.com.eeeeee.user.User;
import esqeee.xieqing.com.eeeeee.view.RichEditor;
import esqeee.xieqing.com.eeeeee.widget.MyLinearLayoutManager;

public class AcrtleActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;


    @BindView(R.id.swipe)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.user_header)
    ImageView header;


    @BindView(R.id.user_name)
    TextView user_name;

    @BindView(R.id.user_description)
    TextView description;

    @BindView(R.id.show)
    RichEditor webView;

    @BindView(R.id.a_zan)
    ImageView a_zan;

    @BindView(R.id.a_collect)
    ImageView a_collect;

    @BindView(R.id.scrollView)
    NestedScrollView scrollView;

    @BindView(R.id.recylerView)
    RecyclerView recyclerView;

    @BindView(R.id.user_device)
    TextView user_device;

    boolean isZan = false;
    boolean isCollect = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        toolbar.setTitle("");
        supportToolBarWithBack(toolbar);
        swipeRefreshLayout.setOnRefreshListener(this);
        webView.loadUrl("file:///android_asset/editor/show.html");
        webView.setInputEnabled(false);
        webView.fill_parent(true);
        webView.addJavascriptInterface(this, "app");
        webView.setOnInitialLoadListener(b->{
            onRefresh();
        });
        webView.setBackgroundColor(0);


        recyclerView.setLayoutManager(new MyLinearLayoutManager(this){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        recyclerView.setAdapter(new ReplyListAdapter());

        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                    loadReply();
                }
            }
        });

        replyAcrtle = new ReplyAcrtle(this,getIntent().getIntExtra("aid", 0));
        replyAcrtle.setCommitCallBack(jsonBean -> {
            data.add(0,jsonBean);
            recyclerView.getAdapter().notifyItemInserted(0);
        });
    }

    @OnClick(R.id.clickbg)
    public void longClick(){

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.removeAllViews();
        webView.clearView();
        webView.destroy();
    }

    private Boolean isLoadReply = false;
    public void loadReply(){
        if (isLoadReply){
            return;
        }
        isLoadReply = true;
        page++;
        BBS.getAcrtleReply(getIntent().getIntExtra("aid", 0),page,User.getUser(), new CallBack() {
            @Override
            public void callBack(CallResult result) {
                isLoadReply = false;
                if (result == null){
                    SnackbarUtils.with(recyclerView)
                            .setMessage("加载评论失败！").showError();
                }else {
                    JSONArrayBean jsonArrayBean = result.getArray("data");
                    data.add(jsonArrayBean);
                    //Log.d("xxxxxxxx",data.toString());
                    recyclerView.getAdapter().notifyItemRangeInserted(data.length() - jsonArrayBean.length(),jsonArrayBean.length());
                    //recyclerView.getAdapter().notifyDataSetChanged();
                    if (jsonArrayBean.length() == 0){
                        findViewById(R.id.noReplyShow).setVisibility(View.VISIBLE);
                    }else{
                        findViewById(R.id.noReplyShow).setVisibility(View.GONE);
                    }
                }
            }
        });
    }


    JSONArrayBean data = new JSONArrayBean();
    int page = 1;

    private class ReplyListAdapter extends RecyclerView.Adapter{

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new RecyclerView.ViewHolder(LayoutInflater.from(AcrtleActivity.this).inflate(R.layout.reply_list,parent,false)) {};
        }



        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            TextView name = holder.itemView.findViewById(R.id.user_name);
            TextView content = holder.itemView.findViewById(R.id.user_content);
            ImageView icon = holder.itemView.findViewById(R.id.user_head);
            ImageView image = holder.itemView.findViewById(R.id.user_image);
            TextView atvr_time = holder.itemView.findViewById(R.id.user_time);
            JSONBean bean = data.getJson(position);

            if (bean  == null){
                return;
            }
            holder.itemView.setOnLongClickListener(v->{
                String[] items = new String[]{"复制内容"};
                if (User.getUser().isAdmin()){
                    items = new String[]{"复制内容","进黑屋","出黑屋","删除"};
                }
                new AlertDialog.Builder(self()).setItems(items,(d,i)->{
                    if (i == 0){
                        ((ClipboardManager) Utils.getApp().getSystemService(CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText("一触即发--剪贴板",content.getText()));
                        ToastUtils.showShort("已复制");
                    }if (i == 1 || i == 2){
                        BBS.status(bean.getInt("uid"),i == 2,User.getUser(),null);
                    }else{
                        BBS.deleteReply(getIntent().getIntExtra("aid", 0),bean.getInt("id"),User.getUser(),new CallBack(){
                            @Override
                            public void callBack(CallResult result) {
                                if (result.getCode() == 0){
                                    data.remove(holder.getAdapterPosition());
                                    notifyItemRemoved(holder.getAdapterPosition());
                                }
                            }
                        });
                    }
                }).show();
                return true;
            });


            View.OnClickListener onClickListener = v->{
                UserActivity.luanch(AcrtleActivity.this,bean.getInt("uid"),bean.getString("username")+"的动态");
            };
            name.setOnClickListener(onClickListener);
            icon.setOnClickListener(onClickListener);

            content.setText(bean.getString("content"));
            name.setText(bean.getString("username"));
            atvr_time.setText(TimeUtils.getFriendlyTimeSpanByNow(bean.getInt("time")));
            BBS.attachIconNoCashe(icon,bean.getInt("uid"));


            if (!TextUtils.isEmpty(bean.getString("image"))){
                holder.itemView.findViewById(R.id.image_content).setVisibility(View.VISIBLE);
                image.setVisibility(View.VISIBLE);
                image.setOnClickListener(view -> {
                    List<String> list = new ArrayList<>();
                    //网络图片
                    list.add(bean.getString("image"));
                    //使用方式
                    PictureConfig config = new PictureConfig.Builder()
                            .setListData((ArrayList<String>) list)	//图片数据List<String> list
                            .setPosition(0)	//图片下标（从第position张图片开始浏览）
                            .setIsShowNumber(true)//是否显示数字下标
                            .needDownload(true)	//是否支持图片下载
                            .build();
                    ImagePagerActivity.startActivity(AcrtleActivity.this, config);
                });
                Glide.with(AcrtleActivity.this)
                        .load(bean.getString("image"))
                        .asBitmap()
                        .into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                int imageWidth = resource.getWidth();
                                int imageHeight = resource.getHeight();
                                int height = image.getWidth() * imageHeight / imageWidth;
                                if (imageHeight / imageWidth < 1 ){
                                    height = image.getWidth();
                                }
                                if (imageHeight / imageWidth > 2){
                                    holder.itemView.findViewById(R.id.image_long).setVisibility(View.VISIBLE);
                                    height = image.getWidth() * 2;
                                }else{
                                    holder.itemView.findViewById(R.id.image_long).setVisibility(View.GONE);
                                }
                                ViewGroup.LayoutParams para = image.getLayoutParams();
                                if (para!=null){
                                    para.height = height;
                                    image.setLayoutParams(para);
                                }
                                image.setImageBitmap(resource);
                            }
                });
            }else{
                image.setVisibility(View.GONE);
                holder.itemView.findViewById(R.id.image_content).setVisibility(View.GONE);
            }

            holder.itemView.findViewById(R.id.user_reply).setOnClickListener(v->{
                replyAcrtle.setId(bean.getInt("uid"));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    replyAcrtle.setText(Html.fromHtml("<font color='#008B8B'>@"+name.getText()+":&nbsp;</font>",Html.FROM_HTML_MODE_COMPACT));
                }else{
                    replyAcrtle.setText(Html.fromHtml("<font color='#008B8B'>@"+name.getText()+":&nbsp;</font>"));
                }
                reply();
            });
        }

        @Override
        public int getItemCount() {
            return data.length();
        }
    }


    @Override
    public int getContentLayout() {
        return R.layout.activity_acrtle;
    }


    @OnClick(R.id.a_zan)
    public void zan(){
        BBS.zan(User.getUser(),getIntent().getIntExtra("aid", 0),null);
        isZan = !isZan;
        a_zan.setImageResource(isZan?R.mipmap.ic_zan_ed:R.mipmap.ic_zan);
    }

    @OnClick(R.id.a_collect)
    public void collect(){
        BBS.collect(User.getUser(),getIntent().getIntExtra("aid", 0),null);
        isCollect = !isCollect;
        a_collect.setImageResource(isCollect?R.mipmap.ic_collect_ed:R.mipmap.ic_collect);
    }

    ReplyAcrtle replyAcrtle;

    @OnClick(R.id.a_reply)
    public void reply(){
        replyAcrtle.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM,0,0);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_acrtle,menu);
        if (User.getUser().isAdmin()){
            menu.findItem(R.id.action_verify).setVisible(true);
            menu.findItem(R.id.action_delete).setVisible(true);
            menu.findItem(R.id.action_choice).setVisible(true);
            menu.findItem(R.id.action_user).setVisible(true);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        CallBack actionCallBack = (CallResult result)->{
            if (result.getCode() != 0){
                SnackbarUtils.with(webView).setMessage(result.getMessage()).showError();
            }else{
                onRefresh();
            }
        };
        switch (item.getItemId()){
            case R.id.action_copy_title:
                ((ClipboardManager) Utils.getApp().getSystemService(CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText("一触即发--剪贴板",title.getText()));
                ToastUtils.showShort("已复制");
                break;
            case R.id.action_copy_content:
                ((ClipboardManager) Utils.getApp().getSystemService(CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText("一触即发--剪贴板",Html.fromHtml(content)));
                ToastUtils.showShort("已复制");
                break;
            case R.id.action_copy_acrtorName:
                ((ClipboardManager) Utils.getApp().getSystemService(CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText("一触即发--剪贴板",user_name.getText()));
                ToastUtils.showShort("已复制");
                break;
            case R.id.action_verify_success:
            case R.id.action_verify_error:
                BBS.verify(getIntent().getIntExtra("aid", 0),item.getItemId() != R.id.action_verify_success,User.getUser(),actionCallBack);
                break;
            case R.id.action_choice_success:
            case R.id.action_choice_error:
                BBS.choice(getIntent().getIntExtra("aid", 0),item.getItemId() == R.id.action_choice_success,User.getUser(),actionCallBack);
                break;
            case R.id.black:
            case R.id.white:
                BBS.status(uid,item.getItemId() == R.id.white,User.getUser(),actionCallBack);
                break;
            case R.id.action_delete:
                new AlertDialog.Builder(AcrtleActivity.this).setTitle("尊敬的管理员")
                        .setMessage("确定删除这篇文章吗？")
                        .setPositiveButton("确定",(i,b)->{
                            BBS.deleteAcrtle(getIntent().getIntExtra("aid", 0),User.getUser(),actionCallBack);
                        }).setNegativeButton("取消",null).create().show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    String content = "";
    int uid = 0;
    @Override
    public void onRefresh() {
        if (!swipeRefreshLayout.isRefreshing()){
            swipeRefreshLayout.setRefreshing(true);
        }
        BBS.getAcrtleDetail(User.getUser(),getIntent().getIntExtra("aid", 0), new CallBack() {
            @Override
            public void callBack(CallResult result) {
                if (swipeRefreshLayout.isRefreshing()){
                    swipeRefreshLayout.setRefreshing(false);
                }
                if (isDestroyed() || isFinishing()){
                    return;
                }
                if (result.getCode() == 0){
                    JSONBean data = result.getData();
                    toolbar.setTitle(data.getString("title"));
                    title.setText(data.getString("title"));
                    View.OnClickListener onClickListener = v->{
                        UserActivity.luanch(AcrtleActivity.this,data.getInt("uid"),data.getString("username")+"的动态");
                    };
                    user_name.setText(data.getString("username"));
                    user_name.setOnClickListener(onClickListener);
                    uid = data.getInt("uid");
                    description.setText("发表于 "+ TimeUtils.getFriendlyTimeSpanByNow(data.getInt("time")));
                    BBS.attachIconNoCashe(header,data.getInt("uid"));
                    header.setOnClickListener(onClickListener);
                    content = data.getString("content");
                    webView.exec("javascript:document.getElementById('editor').innerHTML='"+content+"';");
                    isZan = data.getBoolean("isZan");isCollect = data.getBoolean("isCollect");
                    a_collect.setImageResource(isCollect?R.mipmap.ic_collect_ed:R.mipmap.ic_collect);
                    a_zan.setImageResource(isZan?R.mipmap.ic_zan_ed:R.mipmap.ic_zan);
                    JSONBean device = new JSONBean(data.getString("device"));
                    user_device.setText("来自 "+device.getString("model")+" 安卓"+
                            DeviceUtils.getBuildVersionName(device.getInt("sdk"))+" 屏幕"+ device.getString("pixel"));
                    AcrtleActivity.this.data = data.getArray("replys");
                    if (AcrtleActivity.this.data == null){
                        AcrtleActivity.this.data = new JSONArrayBean();
                    }
                    recyclerView.getAdapter().notifyDataSetChanged();
                    if (AcrtleActivity.this.data.length() == 0){
                        findViewById(R.id.noReplyShow).setVisibility(View.VISIBLE);
                    }else{
                        findViewById(R.id.noReplyShow).setVisibility(View.GONE);
                    }
                }else{
                    SnackbarUtils.with(swipeRefreshLayout).setMessage(result.getMessage()).showError();
                }
            }
        });
    }

    @JavascriptInterface
    public void down(int id){
        showProgress("下载中");
        BBS.getUserFile(User.getUser(),id,new CallBack(){
            @Override
            public void callBack(CallResult result) {
                if (result.getCode() != 0){
                    disProgress();
                    ToastUtils.showLong("获取下载链接失败："+result.getMessage());
                }else{
                    String url = result.getString("url");
                    String name = result.getString("name");
                    new Thread(()->{
                        java.io.File file = new java.io.File(ActionHelper.downDir,name);
                        try {
                            FileIOUtils.writeFileFromIS(file,new URL(url).openStream());
                            ActionHelper.importTo(file.getAbsolutePath(),file.getAbsolutePath());
                            disProgress();
                            ToastUtils.showLong("已下载保存到："+file.toString());
                        } catch (IOException e) {
                            disProgress();
                            ToastUtils.showLong("下载失败："+e.getMessage());
                            e.printStackTrace();
                        }
                    }).start();
                }
            }
        });
    }

    @android.webkit.JavascriptInterface
    public void openImage(String img) {
        List<String> list = new ArrayList<>();
        //网络图片
        list.add(img);
        //使用方式
        PictureConfig config = new PictureConfig.Builder()
                .setListData((ArrayList<String>) list)	//图片数据List<String> list
                .setPosition(0)	//图片下标（从第position张图片开始浏览）
                .setIsShowNumber(true)//是否显示数字下标
                .needDownload(true)	//是否支持图片下载
                .build();
        ImagePagerActivity.startActivity(AcrtleActivity.this, config);
    }

    private void lookImages() {
        webView.exec("javascript:var objs = document.getElementsByTagName('img');for(var i=0;i<objs.length;i++){objs[i].onclick=function(){window.app.openImage(this.src);}}");
    }
}
