package esqeee.xieqing.com.eeeeee.fragment;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xieqing.codeutils.util.AppUtils;
import com.xieqing.codeutils.util.EncryptUtils;
import com.xieqing.codeutils.util.ImageUtils;
import com.xieqing.codeutils.util.ToastUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.action.ActionHelper;
import esqeee.xieqing.com.eeeeee.adapter.MyViewHolder;
import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.dialog.GestruePathDialog;
import esqeee.xieqing.com.eeeeee.dialog.InputTextDialog;
import esqeee.xieqing.com.eeeeee.dialog.MakeSizeDialog;
import esqeee.xieqing.com.eeeeee.dialog.SelectActionDialog;
import esqeee.xieqing.com.eeeeee.dialog.SelectColorDialog;
import esqeee.xieqing.com.eeeeee.dialog.SwipePathDialog;
import esqeee.xieqing.com.eeeeee.dialog.input.InputLine;
import esqeee.xieqing.com.eeeeee.dialog.input.InputNumberLine;
import esqeee.xieqing.com.eeeeee.dialog.input.InputTextLine;
import esqeee.xieqing.com.eeeeee.dialog.input.InputTextListener;
import esqeee.xieqing.com.eeeeee.doAction.DoActionBean;
import esqeee.xieqing.com.eeeeee.library.captrue.RecordAutoCaptruer;
import esqeee.xieqing.com.eeeeee.listener.OnMakeSizeRectListener;
import esqeee.xieqing.com.eeeeee.widget.MyLinearLayoutManager;

public class CommandFragment extends BaseFragment{
    private RecyclerView listView;
    private RecyclerView.Adapter adapter;
    private List<DoActionBean> keys = new ArrayList<>();
    @Override
    public View getContentView(LayoutInflater inflater) {
        listView = new RecyclerView(inflater.getContext());
        return listView;
    }
    private void addActionBean() {

        keys.clear();
        keys.add(DoActionBean.CLICK_SCREEN);
        keys.add(DoActionBean.LONG_CIICK);

        keys.add(DoActionBean.CLICK_SCREEN_TEXT);
        keys.add(DoActionBean.CLICK_TEXT_RECT);
        keys.add(DoActionBean.LONGCLICK_SCREEN_TEXT);
        keys.add(DoActionBean.LONGCLICK_SCREEN_TEXT_RECT);

        keys.add(DoActionBean.CLICK_IMAGE);
        keys.add(DoActionBean.CLICK_IMAGE_RECT);
        keys.add(DoActionBean.LONG_CLICK_IMAGE);
        keys.add(DoActionBean.LONG_CLICK_IMAGE_RECT);


        keys.add(DoActionBean.CLICK_COLOR);
        keys.add(DoActionBean.CLICK_RECT_COLOR);
        keys.add(DoActionBean.LONGCLICK_COLOR);
        keys.add(DoActionBean.LONG_RECT_COLOR);

        keys.add(DoActionBean.SWIP);
        keys.add(DoActionBean.SWIP_LINE);
        keys.add(DoActionBean.GESTRUE);
        keys.add(DoActionBean.INPUT_TEXT);
        keys.add(DoActionBean.PASTE_TEXT);

        keys.add(DoActionBean.IF);
        keys.add(DoActionBean.FOR);
        keys.add(DoActionBean.WHILE);

        keys.add(DoActionBean.RANDOM_SLEEP);
        keys.add(DoActionBean.TOAST);
        keys.add(DoActionBean.DESC);
        keys.add(DoActionBean.BREAK);
        keys.add(DoActionBean.RETURN);
        keys.add(DoActionBean.STOP);
        keys.add(DoActionBean.STOP_ALL);


    }
    @Override
    protected void onFragmentInit() {
        addActionBean();
        adapter = new RecyclerView.Adapter() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                Log.d("onCreateViewHolder","context "+getBaseActivity());
                return new MyViewHolder(View.inflate(getBaseActivity(),R.layout.list_item,null),true);
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                View view=holder.itemView;
                ((TextView)view.findViewById(R.id.title)).setText(keys.get(position).getActionName());
                ((ImageView)view.findViewById(R.id.image)).setImageDrawable(keys.get(position).getDrawable());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ((ImageView)view.findViewById(R.id.image)).setImageTintList(null);
                }
                ((TextView)view.findViewById(R.id.creatTime)).setVisibility(View.GONE);

                view.setOnClickListener(view1 -> {
                    view.postDelayed(()->{
                        switch (keys.get(position)){
                            case CLICK_SCREEN:
                                doClickScreen();
                                break;
                            case LONG_CIICK:
                                doLongClickScreen();
                                break;
                            case CLICK_SCREEN_TEXT:
                                doClickText();
                                break;
                            case CLICK_TEXT_RECT:
                                SelectActionDialog.current.dismiss();
                                new MakeSizeDialog(getBaseActivity()).setOnMakeSizeRectListener((Rect rect) -> {
                                    SelectActionDialog.current.show();
                                    doClickText(rect);
                                }).setTip("请先选择区域").show();
                                break;
                            case SWIP:
                                SelectActionDialog.current.dismiss();
                                new SwipePathDialog(getBaseActivity(), (json)->{
                                    addAction(json);
                                }).show();
                                break;
                            case GESTRUE:
                                SelectActionDialog.current.dismiss();
                                new GestruePathDialog(getBaseActivity(), (json)->{
                                    addAction(json);
                                }).show();
                                break;
                            case INPUT_TEXT:
                                doInputText();
                                break;
                            case CLICK_IMAGE:
                                doClickImage();
                                break;
                            case CLICK_IMAGE_RECT:
                                doClickImageRect();
                                break;
                            case LONG_CLICK_IMAGE:
                                doLongClickImage();
                                break;
                            case LONG_CLICK_IMAGE_RECT:
                                doLongClickImageRect();
                                break;
                            case LONGCLICK_SCREEN_TEXT:
                                doLongClickText();
                                break;
                            case LONGCLICK_SCREEN_TEXT_RECT:
                                doLongClickScreenRect();
                                break;
                            case BREAK:
                                addAction(new JSONBean().put("actionType", DoActionBean.BREAK.getActionType())
                                        .put("witeTime", 0)
                                        .put("param", new JSONBean()));
                                break;
                            case RETURN:
                                addAction(new JSONBean().put("actionType", DoActionBean.RETURN.getActionType())
                                        .put("witeTime", 0)
                                        .put("param", new JSONBean()));
                                break;
                            case STOP:
                                addAction(new JSONBean().put("actionType", DoActionBean.STOP.getActionType())
                                        .put("witeTime", 0)
                                        .put("param", new JSONBean()));
                                break;
                            case STOP_ALL:
                                addAction(new JSONBean().put("actionType", DoActionBean.STOP_ALL.getActionType())
                                        .put("witeTime", 0)
                                        .put("param", new JSONBean()));
                                break;
                            case SWIP_LINE:
                                addAction(new JSONBean().put("actionType", DoActionBean.SWIP_LINE.getActionType())
                                        .put("witeTime", 1000)
                                        .put("param", new JSONBean()));
                                break;
                            case CLICK_COLOR:
                                SelectActionDialog.current.dismiss();
                                //RuntimeLog.log(SelectActionDialog.current.bitmap);
                                new SelectColorDialog(getBaseActivity(),SelectActionDialog.current.bitmap,SelectActionDialog.current.actionAddListener).show();
                                break;
                            case LONGCLICK_COLOR:
                                SelectActionDialog.current.dismiss();
                                new SelectColorDialog(getBaseActivity(),SelectActionDialog.current.bitmap,SelectActionDialog.current.actionAddListener)
                                        .setLongClick(true).show();
                                break;
                            case CLICK_RECT_COLOR:
                                SelectActionDialog.current.dismiss();
                                new MakeSizeDialog(getBaseActivity()).setOnMakeSizeRectListener((Rect rect)->{
                                    new SelectColorDialog(getBaseActivity(),SelectActionDialog.current.bitmap,SelectActionDialog.current.actionAddListener)
                                            .setRect(rect).show();
                                }).setTip("请选择区域").show();
                                break;
                            case LONG_RECT_COLOR:
                                SelectActionDialog.current.dismiss();
                                new MakeSizeDialog(getBaseActivity()).setOnMakeSizeRectListener((Rect rect)->{
                                    new SelectColorDialog(getBaseActivity(),SelectActionDialog.current.bitmap,SelectActionDialog.current.actionAddListener)
                                            .setRect(rect)
                                            .setLongClick(true).show();
                                }).setTip("请选择区域").show();
                                break;
                            case PASTE_TEXT:
                                doPasteText();
                                break;
                            case RANDOM_SLEEP:
                                doRandomSleep();
                                break;
                            case TOAST:
                                doToast();
                                break;
                            case DESC:
                                doDescription();
                                break;
                            case IF:
                                SelectActionDialog.current.dismiss();
                                AppUtils.resumeApp();
                                SelectActionDialog.current.actionAddListener.addAction(new JSONBean()
                                        .put("actionType", 43)
                                        .put("witeTime",0)
                                        .put("param",new JSONBean())
                                        .put("trueDo",new JSONBean())
                                        .put("falseDo",new JSONBean()));
                                //IfDialog.getIfDialog(getBaseActivity(),null).setOnActionAddListener(SelectActionDialog.current.actionAddListener).show();
                                break;
                            case WHILE:
                                SelectActionDialog.current.dismiss();
                                AppUtils.resumeApp();
                                SelectActionDialog.current.actionAddListener.addAction(new JSONBean()
                                        .put("actionType", 52)
                                        .put("witeTime",0)
                                        .put("param",new JSONBean())
                                        .put("trueDo",new JSONBean()));
                                //WhileDialog.getWhileDialog(getBaseActivity(),null).setOnActionAddListener(SelectActionDialog.current.actionAddListener).show();
                                break;
                            case FOR:
                                SelectActionDialog.current.dismiss();
                                AppUtils.resumeApp();
                                SelectActionDialog.current.actionAddListener.addAction(new JSONBean()
                                        .put("actionType", 53)
                                        .put("witeTime",0)
                                        .put("condition","1")
                                        .put("param",new JSONBean())
                                        .put("trueDo",new JSONBean()));

                                //ForDialog.getForDialog(getBaseActivity(),null).setOnActionAddListener(SelectActionDialog.current.actionAddListener).show();
                                break;
                        }
                    },200);
                });
            }

            @Override
            public int getItemCount() {
                return keys.size();
            }
        };

        listView.setAdapter(adapter);
        listView.setLayoutManager(new MyLinearLayoutManager(getActivity()));
    }

    /*
    * 注释
    * */
    private void doDescription() {
        addAction(new JSONBean().put("actionType", 56)
                .put("witeTime", 0)
                .put("param", new JSONBean().put("text","")));
        AppUtils.resumeApp();
    }

    /*
    * 弹出提示
    * */
    private void doToast() {
        addAction(new JSONBean().put("actionType", 55)
                .put("witeTime", 1000)
                .put("param", new JSONBean().put("text","")));
        AppUtils.resumeApp();
    }

    /*
    *   随机延迟
    * */
    private void doRandomSleep() {
        addAction(new JSONBean().put("actionType", 51)
                .put("witeTime", 0)
                .put("param", new JSONBean()
                        .put("min",0)
                        .put("max",1000)));
        AppUtils.resumeApp();
    }

    /*
    *   在此输入文字
    * */
    private void doPasteText() {
        JSONBean param = new JSONBean()
                .put("x",SelectActionDialog.current.x)
                .put("y",SelectActionDialog.current.y);
        JSONBean jsonObject = new JSONBean()
                .put("actionType",46)
                .put("witeTime",1000)
                .put("param",param);
        addAction(jsonObject);
    }

    /*
    *
    *   长按屏幕文字
    * */
    private void doLongClickText() {
        InputTextDialog.getDialog(getBaseActivity()).setTitle("请输入点击文字")
                .addInputLine(new InputTextLine())
                .addInputLine(new InputNumberLine("等待时间",1000)).setInputTextListener(new InputTextListener() {
            @Override
            public void onConfirm(InputLine[] result) throws Exception{
                addAction(new JSONBean().put("actionType", 41)
                        .put("witeTime", ((InputNumberLine)result[1]).getResult())
                        .put("param", new JSONBean().put("text",result[0].getResult())));
            }
        }).show();
    }

    /*
     *
     *   点击区域图片
     * */
    private void doClickImageRect() {
        SelectActionDialog.current.dismiss();
        new MakeSizeDialog(getBaseActivity()).setOnMakeSizeRectListener(new OnMakeSizeRectListener() {
            @Override
            public void onConfirnm(Rect rect) {
                new MakeSizeDialog(getBaseActivity()).setOnMakeSizeRectListener(new OnMakeSizeRectListener() {
                    @Override
                    public void onConfirnm(Rect rect2) {
                        Bitmap bitmap  = ImageUtils.cutBitmap(RecordAutoCaptruer.getIntance().captrueScreen(),rect2);
                        if (bitmap == null){
                            ToastUtils.showLong("截取失败");
                        }else {
                            String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Long.valueOf(System.currentTimeMillis()));
                            File localFile = new File(ActionHelper.workSpaceImageDir, "img_" + EncryptUtils.encryptMD5ToString(time) + ".dddbbb");
                            ImageUtils.save(bitmap, localFile, Bitmap.CompressFormat.PNG);
                            JSONBean param = new JSONBean()
                                    .put("fileName", localFile.getAbsolutePath())
                                    .put("left", rect.left)
                                    .put("top", rect.top)
                                    .put("right", rect.right)
                                    .put("bottom", rect.bottom);
                            JSONBean jsonObject = new JSONBean()
                                    .put("actionType", 50)
                                    .put("witeTime", 1000)
                                    .put("param", param);
                            addAction(jsonObject);
                        }
                    }
                }).setTip("请先选择需要识别的图片").show();
            }
        }).setTip("请先选择区域").show();

    }
    /*
     *
     *   点击区域图片
     * */
    private void doLongClickImageRect() {
        SelectActionDialog.current.dismiss();
        new MakeSizeDialog(getBaseActivity()).setOnMakeSizeRectListener(new OnMakeSizeRectListener() {
            @Override
            public void onConfirnm(Rect rect) {
                new MakeSizeDialog(getBaseActivity()).setOnMakeSizeRectListener(new OnMakeSizeRectListener() {
                    @Override
                    public void onConfirnm(Rect rect2) {
                        Bitmap bitmap  = ImageUtils.cutBitmap(RecordAutoCaptruer.getIntance().captrueScreen(),rect2);
                        if (bitmap == null){
                            ToastUtils.showLong("截取失败");
                        }else {
                            String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Long.valueOf(System.currentTimeMillis()));
                            File localFile = new File(ActionHelper.workSpaceImageDir, "img_" + EncryptUtils.encryptMD5ToString(time) + ".dddbbb");
                            ImageUtils.save(bitmap, localFile, Bitmap.CompressFormat.PNG);
                            JSONBean param = new JSONBean()
                                    .put("fileName", localFile.getAbsolutePath())
                                    .put("left", rect.left)
                                    .put("top", rect.top)
                                    .put("right", rect.right)
                                    .put("bottom", rect.bottom);
                            JSONBean jsonObject = new JSONBean()
                                    .put("actionType", 61)
                                    .put("witeTime", 1000)
                                    .put("param", param);
                            addAction(jsonObject);
                        }
                    }
                }).setTip("请先选择需要识别的图片").show();
            }
        }).setTip("请先选择区域").show();

    }
    /*
     *
     *   点击图片
     * */
    private void doLongClickImage() {
        SelectActionDialog.current.dismiss();
        new MakeSizeDialog(getBaseActivity()).setOnMakeSizeRectListener(new OnMakeSizeRectListener() {
            @Override
            public void onConfirnm(Rect rect) {
                Bitmap bitmap  = ImageUtils.cutBitmap(RecordAutoCaptruer.getIntance().captrueScreen(),rect);
                if (bitmap == null){
                    ToastUtils.showLong("截取失败");
                }else{
                    String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Long.valueOf(System.currentTimeMillis()));
                    File localFile = new File(ActionHelper.workSpaceImageDir, "img_" + EncryptUtils.encryptMD5ToString(time) + ".dddbbb");
                    ImageUtils.save(bitmap,localFile, Bitmap.CompressFormat.PNG);
                    addAction(new JSONBean().put("actionType",60)
                            .put("witeTime",1000)
                            .put("param",new JSONBean().put("fileName",localFile.getAbsolutePath())));
                }
            }
        }).show();
    }
    /*
    *
    *   点击图片
    * */
    private void doClickImage() {
        SelectActionDialog.current.dismiss();
        new MakeSizeDialog(getBaseActivity()).setOnMakeSizeRectListener(new OnMakeSizeRectListener() {
            @Override
            public void onConfirnm(Rect rect) {
                Bitmap bitmap  = ImageUtils.cutBitmap(RecordAutoCaptruer.getIntance().captrueScreen(),rect);
                if (bitmap == null){
                    ToastUtils.showLong("截取失败");
                }else{
                    String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Long.valueOf(System.currentTimeMillis()));
                    File localFile = new File(ActionHelper.workSpaceImageDir, "img_" + EncryptUtils.encryptMD5ToString(time) + ".dddbbb");
                    ImageUtils.save(bitmap,localFile, Bitmap.CompressFormat.PNG);
                    addAction(new JSONBean().put("actionType",30)
                            .put("witeTime",1000)
                            .put("param",new JSONBean().put("fileName",localFile.getAbsolutePath())));
                }
            }
        }).show();
    }

    /*
     *   点击区域屏幕文字
     * */
    private void doClickText(Rect rect) {
        InputTextDialog.getDialog(getBaseActivity()).setTitle("当区域内含有该文字时点击")
                .addInputLine(new InputTextLine())
                .addInputLine(new InputNumberLine("等待时间",1000))
                .setInputTextListener((InputLine[] result)->{
                    JSONBean json = new JSONBean();
                    JSONBean param = new JSONBean();
                    json.put("actionType",49)
                            .put("witeTime",(int)result[1].getResult());
                    param.put("text",result[0].getResult().toString())
                            .put("left",rect.left)
                            .put("top",rect.top)
                            .put("right",rect.right)
                            .put("bottom",rect.bottom);
                    json.put("param",param);
                    addAction(json);
                }).show();
    }

    /*
     *   在此输入文字
     * */
    private void doInputText() {
        InputTextDialog.getDialog(getBaseActivity()).setTitle("请输入文字")
                .addInputLine(new InputTextLine())
                .addInputLine(new InputNumberLine("等待时间",1000)).setInputTextListener(new InputTextListener() {
            @Override
            public void onConfirm(InputLine[] result) throws Exception{
                SelectActionDialog.current.dismiss();
                addAction(new JSONBean().put("actionType", 15)
                        .put("witeTime", ((InputNumberLine)result[1]).getResult())
                        .put("param", new JSONBean().put("x",SelectActionDialog.current.x)
                                .put("y",SelectActionDialog.current.y)
                                .put("text",result[0].getResult())));
            }
        }).show();
    }
    /*
    *   点击屏幕文字
    * */
    private void doClickText() {
        InputTextDialog.getDialog(getBaseActivity()).setTitle("请输入点击文字")
                .addInputLine(new InputTextLine())
                .addInputLine(new InputNumberLine("等待时间",1000))
                .setInputTextListener((InputLine[] result)->{
                    JSONBean json = new JSONBean();
                    JSONBean param = new JSONBean();
                    json.put("actionType",3)
                            .put("witeTime",(int)result[1].getResult());
                    param.put("text",result[0].getResult().toString());
                    json.put("param",param);
                    addAction(json);
                }).show();
    }

    /*
    *   点击屏幕位置
    * */
    private void doClickScreen() {
        InputTextDialog.getDialog(getBaseActivity()).setTitle("请输入等待时间")
                .addInputLine(new InputNumberLine("点击次数",1))
                .addInputLine(new InputTextLine("备注信息","").setCanInputNull(true))
                .addInputLine(new InputNumberLine("等待时间",1000)).setInputTextListener((InputLine[] result)->{
                    JSONBean jsonBean = JSONBean.optInt("actionType", 2)
                            .put("witeTime",(int)result[2].getResult())
                            .put("param", new JSONBean()
                                                .put("count",(int)result[0].getResult())
                                                .put("desc",result[1].getResult())
                                                .put("x",SelectActionDialog.current.x)
                                                .put("y",SelectActionDialog.current.y)
                            );
                    addAction(jsonBean);
        }).show();
    }

    /*
     *   长按屏幕位置
     * */
    private void doLongClickScreenRect() {
        SelectActionDialog.current.dismiss();
        new MakeSizeDialog(getBaseActivity()).setOnMakeSizeRectListener(new OnMakeSizeRectListener() {
            @Override
            public void onConfirnm(Rect rect) {
                InputTextDialog.getDialog(getBaseActivity()).setTitle("请输入点击文字")
                        .addInputLine(new InputTextLine())
                        .addInputLine(new InputNumberLine("等待时间",1000))
                        .setInputTextListener((InputLine[] result)->{
                            JSONBean json = new JSONBean();
                            JSONBean param = new JSONBean();
                            json.put("actionType",62)
                                    .put("witeTime",(int)result[1].getResult());
                            param.put("text",result[0].getResult().toString())
                                    .put("left", rect.left)
                                    .put("top", rect.top)
                                    .put("right", rect.right)
                                    .put("bottom", rect.bottom);
                            json.put("param",param);
                            addAction(json);
                        }).show();
            }
        }).setTip("请先选择区域").show();
    }
    /*
     *   长按屏幕位置
     * */
    private void doLongClickScreen() {
        InputTextDialog.getDialog(getBaseActivity()).setTitle("请输入等待时间").setMessage("0表示无限循环")
                .addInputLine(new InputNumberLine("等待时间",1000)).setInputTextListener((InputLine[] result)->{
            JSONBean jsonBean = JSONBean.optInt("actionType", 16)
                    .put("witeTime",(int)result[0].getResult())
                    .put("param", new JSONBean()
                            .put("x",SelectActionDialog.current.x)
                            .put("y",SelectActionDialog.current.y)
                    );
            addAction(jsonBean);
        }).show();
    }

    private void addAction(JSONBean jsonBean) {
        if (selectedListener != null){
            SelectActionDialog.current.dismiss();
            selectedListener.select(jsonBean);
        }
    }





    private OnSelectedListener selectedListener;

    public void setSelectedListener(OnSelectedListener selectedListener) {
        this.selectedListener = selectedListener;
    }

    public interface OnSelectedListener{
        void select(JSONBean actionBean);
    }
}
