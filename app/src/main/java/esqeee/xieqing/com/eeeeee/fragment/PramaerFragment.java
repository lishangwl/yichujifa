package esqeee.xieqing.com.eeeeee.fragment;

import androidx.annotation.NonNull;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.adapter.MyViewHolder;
import esqeee.xieqing.com.eeeeee.doAction.DoActionBean;
import esqeee.xieqing.com.eeeeee.widget.MyGridLayoutManager;

public class PramaerFragment extends BaseFragment implements SearchView.OnQueryTextListener {
    @BindView(R.id.searchView) SearchView searchView;
    @BindView(R.id.recylerView) RecyclerView recyclerView;
    @BindView( R.id.swipe) SwipeRefreshLayout swipeRefreshLayout;

    private List<Object> appInfos = new ArrayList<>();
    private List<Object> appInfos_search = new ArrayList<>();
    private RecyclerView.Adapter adapter;



    @Override
    public View getContentView(LayoutInflater inflater) {
        View in = inflater.inflate(R.layout.fragment_apps,null);
        return in;
    }



    @Override
    protected void onFragmentInit() {
        //refresh();
        swipeRefreshLayout.setOnRefreshListener(this::refresh);
        searchView.setOnQueryTextListener(this);
        MyGridLayoutManager gridLayoutManager = new MyGridLayoutManager(getBaseActivity(),3);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (searchString.equals("") && position < appInfos.size() && position>=0){
                    if (appInfos.get(position) instanceof String){
                        return 3;
                    }
                }
                return 1;
            }
        });
        recyclerView.setLayoutManager(gridLayoutManager);

        adapter = new RecyclerView.Adapter() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new MyViewHolder(View.inflate(getBaseActivity(),R.layout.param,null),true);
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                TextView view= (TextView) holder.itemView;
                Object o = searchString.equals("")?appInfos.get(position):appInfos_search.get(position);
                if (o instanceof DoActionBean){
                    view.setGravity(Gravity.CENTER);
                    view.setBackgroundResource(R.drawable.param_item);
                    view.setText(((DoActionBean)o).getActionName());
                    view.setOnClickListener(view1 -> {
                        if (onAppsSelectedListener!=null){
                            onAppsSelectedListener.select((DoActionBean)o);
                        }
                    });
                }else if (o instanceof PItem){
                    view.setGravity(Gravity.CENTER);
                    view.setBackgroundResource(R.drawable.param_item);
                    view.setText((((PItem)o).name));
                    view.setOnClickListener(view1 -> {
                        if (onAppsSelectedListener!=null){
                            ((DoActionBean)((PItem)o).o).setParam((((PItem)o).name));
                            onAppsSelectedListener.select((DoActionBean)((PItem)o).o);
                        }
                    });
                }else{
                    view.setText(o.toString());
                    view.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
                    view.setBackground(null);
                    view.setOnClickListener(null);
                }

            }

            @Override
            public int getItemCount() {
                return searchString.equals("")?appInfos.size():appInfos_search.size();
            }
        };
        recyclerView.setAdapter(adapter);
        refresh();
    }

    public static class PItem{
        String name;Object o;
        public PItem(String name,Object o){
            this.name = name;
            this.o=o;
        }
    }


    private void addActionBean() {
        appInfos.clear();
        appInfos_search.clear();
        searchString = "";

        appInfos.add("常用操作");
        appInfos.add(new PItem("主机是否可连接",DoActionBean.ENCRPTY_ACTION));
        appInfos.add(new PItem("调试输出日志",DoActionBean.ENCRPTY_ACTION));
        appInfos.add(new PItem("取随机数",DoActionBean.ENCRPTY_ACTION));
        appInfos.add(new PItem("数学运算",DoActionBean.ENCRPTY_ACTION));
        appInfos.add(new PItem("获取截图",DoActionBean.ENCRPTY_ACTION));
        appInfos.add(new PItem("获取屏幕颜色",DoActionBean.ENCRPTY_ACTION));
        appInfos.add(new PItem("获取文字",DoActionBean.ENCRPTY_ACTION));
        appInfos.add(new PItem("获取文字2",DoActionBean.ENCRPTY_ACTION));
        appInfos.add(new PItem("打开应用",DoActionBean.ENCRPTY_ACTION));
        appInfos.add(new PItem("变整数",DoActionBean.ENCRPTY_ACTION));
        appInfos.add(new PItem("JSON解析",DoActionBean.ENCRPTY_ACTION));
        appInfos.add(new PItem("取星期几",DoActionBean.ENCRPTY_ACTION));
        appInfos.add("变量操作");
        appInfos.add(DoActionBean.PRO_HTTP);
        appInfos.add(DoActionBean.PRO_ASSGIN);
        appInfos.add(new PItem("创建矩型",DoActionBean.ENCRPTY_ACTION));
        appInfos.add(new PItem("显示网页",DoActionBean.ENCRPTY_ACTION));
        appInfos.add(new PItem("保存变量",DoActionBean.ENCRPTY_ACTION));
        appInfos.add(new PItem("读取变量",DoActionBean.ENCRPTY_ACTION));
        appInfos.add("加密解密操作");
        appInfos.add(new PItem("DES加密",DoActionBean.ENCRPTY_ACTION));
        appInfos.add(new PItem("DES解密",DoActionBean.ENCRPTY_ACTION));
        appInfos.add(new PItem("RC4加密",DoActionBean.ENCRPTY_ACTION));
        appInfos.add(new PItem("RC4解密",DoActionBean.ENCRPTY_ACTION));
        appInfos.add(new PItem("Authcode加密",DoActionBean.ENCRPTY_ACTION));
        appInfos.add(new PItem("Authcode解密",DoActionBean.ENCRPTY_ACTION));
        appInfos.add("QQ相关操作");
        appInfos.add(new PItem("临时QQ会话",DoActionBean.ENCRPTY_ACTION));
        appInfos.add(new PItem("打开QQ加群",DoActionBean.ENCRPTY_ACTION));
        appInfos.add(new PItem("打开QQ资料卡",DoActionBean.ENCRPTY_ACTION));
        appInfos.add(new PItem("取QQ昵称",DoActionBean.ENCRPTY_ACTION));
        appInfos.add(new PItem("取QQ头像",DoActionBean.ENCRPTY_ACTION));
        appInfos.add("系统操作");
        appInfos.add(new PItem("打开权限设置",DoActionBean.SYSTEM_ACTION));
        appInfos.add(new PItem("打开网络设置",DoActionBean.SYSTEM_ACTION));
        appInfos.add(new PItem("取手机信息",DoActionBean.SYSTEM_ACTION));
        appInfos.add(new PItem("置屏幕亮度",DoActionBean.SYSTEM_ACTION));
        appInfos.add(new PItem("置屏幕亮度模式",DoActionBean.SYSTEM_ACTION));
        appInfos.add(new PItem("调用系统分享",DoActionBean.SYSTEM_ACTION));
        appInfos.add(new PItem("调用系统打开文件",DoActionBean.SYSTEM_ACTION));
        appInfos.add("应用操作");
        appInfos.add(new PItem("卸载应用",DoActionBean.SYSTEM_ACTION));
        appInfos.add(new PItem("取应用信息",DoActionBean.SYSTEM_ACTION));
        appInfos.add("设备操作");
        appInfos.add(new PItem("取通知栏信息",DoActionBean.ENCRPTY_ACTION));
        appInfos.add(new PItem("取WIFI状态",DoActionBean.ENCRPTY_ACTION));
        //appInfos.add(new PItem("语音识别",DoActionBean.ENCRPTY_ACTION));
        appInfos.add(new PItem("语音播放",DoActionBean.ENCRPTY_ACTION));
        appInfos.add(new PItem("取蓝牙状态",DoActionBean.ENCRPTY_ACTION));
        appInfos.add(new PItem("取时间戳",DoActionBean.ENCRPTY_ACTION));
        appInfos.add(new PItem("格式化时间戳",DoActionBean.ENCRPTY_ACTION));
        appInfos.add(new PItem("设置剪贴板文本",DoActionBean.ENCRPTY_ACTION));
        appInfos.add(new PItem("获取剪贴板文本",DoActionBean.ENCRPTY_ACTION));
        appInfos.add(new PItem("发送短信",DoActionBean.ENCRPTY_ACTION));
        appInfos.add(new PItem("获取最新短信",DoActionBean.ENCRPTY_ACTION));
        appInfos.add("媒体操作");
        appInfos.add(new PItem("播放音乐",DoActionBean.ENCRPTY_ACTION));
        appInfos.add(new PItem("停止播放音乐",DoActionBean.ENCRPTY_ACTION));
        appInfos.add(new PItem("更新图片到相册",DoActionBean.ENCRPTY_ACTION));
        appInfos.add("屏幕触摸操作");
        appInfos.add(new PItem("多指点击",DoActionBean.ENCRPTY_ACTION));
        appInfos.add(new PItem("多指长按",DoActionBean.ENCRPTY_ACTION));
        appInfos.add("编码操作");
        appInfos.add(new PItem("Unicode转Ascll",DoActionBean.ENCRPTY_ACTION));
        appInfos.add(new PItem("Ascll转Unicode",DoActionBean.ENCRPTY_ACTION));
        appInfos.add(new PItem("URL编码",DoActionBean.ENCRPTY_ACTION));
        appInfos.add(new PItem("URL解码",DoActionBean.ENCRPTY_ACTION));
        appInfos.add(new PItem("Base64编码",DoActionBean.ENCRPTY_ACTION));
        appInfos.add(new PItem("Base64解码",DoActionBean.ENCRPTY_ACTION));
        appInfos.add(new PItem("MD5",DoActionBean.ENCRPTY_ACTION));
        appInfos.add("高级操作");
        appInfos.add(new PItem("执行Shell",DoActionBean.ENCRPTY_ACTION));
        appInfos.add(new PItem("发送文本到焦点编辑框",DoActionBean.ENCRPTY_ACTION));
        appInfos.add(new PItem("执行输入法动作",DoActionBean.ENCRPTY_ACTION));
        appInfos.add("对话框操作");
        appInfos.add(new PItem("弹出对话框",DoActionBean.DIALOG_ACTION));
        appInfos.add(new PItem("弹出输入框",DoActionBean.DIALOG_ACTION));
        appInfos.add(new PItem("弹出选择框",DoActionBean.DIALOG_ACTION));
        appInfos.add(new PItem("弹出输入法选择框",DoActionBean.DIALOG_ACTION));
        appInfos.add("文本操作");
        appInfos.add(new PItem("到大写",DoActionBean.STRING_ACTION));
        appInfos.add(new PItem("到小写",DoActionBean.STRING_ACTION));
        appInfos.add(new PItem("子文本替换",DoActionBean.STRING_ACTION));
        appInfos.add(new PItem("子文本替换2",DoActionBean.STRING_ACTION));
        appInfos.add(new PItem("删首尾空",DoActionBean.STRING_ACTION));
        appInfos.add(new PItem("分割文本",DoActionBean.STRING_ACTION));
        appInfos.add(new PItem("寻找文本",DoActionBean.STRING_ACTION));
        appInfos.add(new PItem("倒找文本",DoActionBean.STRING_ACTION));
        appInfos.add(new PItem("取文本中间",DoActionBean.STRING_ACTION));
        appInfos.add(new PItem("批量取文本中间",DoActionBean.STRING_ACTION));
        appInfos.add(new PItem("取文本左边",DoActionBean.STRING_ACTION));
        appInfos.add(new PItem("取文本右边",DoActionBean.STRING_ACTION));
        appInfos.add(new PItem("取文本长度",DoActionBean.STRING_ACTION));
        appInfos.add(new PItem("文本到整数",DoActionBean.STRING_ACTION));
        appInfos.add(new PItem("文本比较",DoActionBean.STRING_ACTION));
        appInfos.add(new PItem("翻转文本",DoActionBean.STRING_ACTION));
        appInfos.add("数组操作");
        appInfos.add(new PItem("取数组成员数",DoActionBean.ARRAY_ACTION));
        appInfos.add(new PItem("取成员",DoActionBean.ARRAY_ACTION));
        appInfos.add(new PItem("加入成员",DoActionBean.ARRAY_ACTION));
        appInfos.add("文件操作");
        appInfos.add(new PItem("写出字节集文件",DoActionBean.FILE_ACTION));
        appInfos.add(new PItem("读入字节集文件",DoActionBean.FILE_ACTION));
        appInfos.add(new PItem("写出文本文件",DoActionBean.FILE_ACTION));
        appInfos.add(new PItem("读入文本文件",DoActionBean.FILE_ACTION));
        appInfos.add(new PItem("删除文件",DoActionBean.FILE_ACTION));
        appInfos.add(new PItem("复制文件",DoActionBean.FILE_ACTION));
        appInfos.add(new PItem("重命名文件",DoActionBean.FILE_ACTION));
        appInfos.add(new PItem("删除目录",DoActionBean.FILE_ACTION));
        appInfos.add(new PItem("创建目录",DoActionBean.FILE_ACTION));
        appInfos.add(new PItem("遍历目录",DoActionBean.FILE_ACTION));
        appInfos.add(new PItem("文件是否存在",DoActionBean.FILE_ACTION));
        appInfos.add(new PItem("获取网络文件",DoActionBean.FILE_ACTION));
        appInfos.add(new PItem("压缩zip",DoActionBean.FILE_ACTION));
        appInfos.add(new PItem("解压zip",DoActionBean.FILE_ACTION));
        appInfos.add(new PItem("打开文件",DoActionBean.FILE_ACTION));
        appInfos.add(new PItem("取文件修改时间",DoActionBean.FILE_ACTION));
        appInfos.add(new PItem("取文件编码",DoActionBean.FILE_ACTION));
        appInfos.add(new PItem("是否为隐藏文件",DoActionBean.FILE_ACTION));
        appInfos.add(new PItem("取子目录",DoActionBean.FILE_ACTION));
        appInfos.add(new PItem("寻找文件关键词",DoActionBean.FILE_ACTION));
        appInfos.add(new PItem("寻找文件后缀名",DoActionBean.FILE_ACTION));
        appInfos.add(new PItem("调用播放器播放",DoActionBean.FILE_ACTION));
        appInfos.add("控件操作");
        appInfos.add(new PItem("取当前窗口类名",DoActionBean.NODE_ACTION));
        appInfos.add(new PItem("查询所有控件",DoActionBean.NODE_ACTION));
        appInfos.add(new PItem("取查询控件总数",DoActionBean.NODE_ACTION));
        appInfos.add(new PItem("取控件",DoActionBean.NODE_ACTION));
        appInfos.add(new PItem("取控件类名",DoActionBean.NODE_ACTION));
        appInfos.add(new PItem("取控件内容",DoActionBean.NODE_ACTION));
        appInfos.add(new PItem("取控件矩阵",DoActionBean.NODE_ACTION));
        appInfos.add(new PItem("取子控件数",DoActionBean.NODE_ACTION));
        appInfos.add(new PItem("取子控件",DoActionBean.NODE_ACTION));
        appInfos.add(new PItem("取父控件",DoActionBean.NODE_ACTION));
        appInfos.add(new PItem("设置控件内容",DoActionBean.NODE_ACTION));
        appInfos.add(new PItem("正向滑动控件",DoActionBean.NODE_ACTION));
        appInfos.add(new PItem("反向滑动控件",DoActionBean.NODE_ACTION));
        appInfos.add(new PItem("点击控件",DoActionBean.NODE_ACTION));
        appInfos.add(new PItem("长按控件",DoActionBean.NODE_ACTION));
        appInfos.add(new PItem("获取焦点",DoActionBean.NODE_ACTION));
        appInfos.add("界面操作");
        appInfos.add(new PItem("打开窗口",DoActionBean.UI_ACTION));
        appInfos.add(new PItem("关闭当前窗口",DoActionBean.UI_ACTION));
        appInfos.add(new PItem("创建组件",DoActionBean.UI_ACTION));
        appInfos.add(new PItem("删除组件",DoActionBean.UI_ACTION));
        appInfos.add(new PItem("取组件内容",DoActionBean.UI_ACTION));
        appInfos.add(new PItem("置组件内容",DoActionBean.UI_ACTION));
        appInfos.add(new PItem("取组件属性",DoActionBean.UI_ACTION));
        appInfos.add(new PItem("置组件属性",DoActionBean.UI_ACTION));
        appInfos.add(new PItem("取组件选中状态",DoActionBean.UI_ACTION));
        appInfos.add(new PItem("取组件可见状态",DoActionBean.UI_ACTION));
        appInfos.add(new PItem("置组件可见状态",DoActionBean.UI_ACTION));
        appInfos.add(new PItem("取进度条进度值",DoActionBean.UI_ACTION));
        appInfos.add(new PItem("取下拉框选中项",DoActionBean.UI_ACTION));
        appInfos.add(new PItem("置浏览器链接",DoActionBean.UI_ACTION));
        appInfos.add(new PItem("置浏览器html代码",DoActionBean.UI_ACTION));
        appInfos.add(new PItem("取浏览器接口参数",DoActionBean.UI_ACTION));
    }
    private void refresh() {
        swipeRefreshLayout.setRefreshing(true);
        addActionBean();
        swipeRefreshLayout.setRefreshing(false);
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    private String searchString = "";
    private void search(String string) {
        searchString = string;
        appInfos_search.clear();
        for (int i=0;i<appInfos.size();i++){
            if (appInfos.get(i) instanceof DoActionBean){
                if (((DoActionBean)appInfos.get(i)).getActionName().contains(string)||((DoActionBean)appInfos.get(i)).getActionName().toUpperCase().contains(string.toUpperCase())){
                    appInfos_search.add(appInfos.get(i));
                }
            }
            if (appInfos.get(i) instanceof PItem){
                if (((PItem)appInfos.get(i)).name.contains(string)||((PItem)appInfos.get(i)).name.toUpperCase().contains(string.toUpperCase())){
                    appInfos_search.add(appInfos.get(i));
                }
            }
        }
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        search(newText);
        return false;
    }

    private OnSelectedListener onAppsSelectedListener;

    public void setOnAppsSelectedListener(OnSelectedListener onAppsSelectedListener) {
        this.onAppsSelectedListener = onAppsSelectedListener;
    }

    public interface OnSelectedListener{
        void select(DoActionBean appInfo);
    }
}
