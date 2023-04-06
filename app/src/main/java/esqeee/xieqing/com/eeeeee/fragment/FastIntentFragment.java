package esqeee.xieqing.com.eeeeee.fragment;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.adapter.MyViewHolder;
import esqeee.xieqing.com.eeeeee.dialog.InputTextDialog;
import esqeee.xieqing.com.eeeeee.dialog.input.InputLine;
import esqeee.xieqing.com.eeeeee.dialog.input.InputPhoneLine;
import esqeee.xieqing.com.eeeeee.dialog.input.InputQQLine;
import esqeee.xieqing.com.eeeeee.dialog.input.InputTextLine;
import esqeee.xieqing.com.eeeeee.dialog.input.InputTextListener;
import esqeee.xieqing.com.eeeeee.doAction.DoActionBean;
import esqeee.xieqing.com.eeeeee.widget.MyLinearLayoutManager;

public class FastIntentFragment extends BaseFragment{
    private RecyclerView listView;
    private RecyclerView.Adapter adapter;
    private List<DoActionBean> keys = new ArrayList<>();
    @Override
    public View getContentView(LayoutInflater inflater) {
        listView = new RecyclerView(getBaseActivity());
        return listView;
    }

    @Override
    protected void onFragmentInit() {
        addActionBean();
        adapter = new RecyclerView.Adapter() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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
                    switch (keys.get(holder.getAdapterPosition())){
                        case LNK_QQ:
                            InputTextDialog.getDialog(getBaseActivity()).setTitle("请输入QQ")
                                    .addInputLine(new InputQQLine("QQ"))
                                    .setInputTextListener(new InputTextListener() {
                                        @Override
                                        public void onConfirm(InputLine[] result) throws Exception{
                                            if (selectedListener!=null){
                                                selectedListener.select(keys.get(position),result[0].getResult().toString());
                                            }
                                        }
                                    }).show();
                            break;
                        case LNK_TELEPHONE:
                            InputTextDialog.getDialog(getBaseActivity()).setTitle("请输入手机号")
                                    .addInputLine(new InputPhoneLine("手机号"))
                                    .setInputTextListener(new InputTextListener() {
                                        @Override
                                        public void onConfirm(InputLine[] result) throws Exception{
                                            if (selectedListener!=null){
                                                selectedListener.select(keys.get(position),result[0].getResult().toString());
                                            }
                                        }
                                    }).show();
                            break;
                        case LNK_LINK:
                            InputTextDialog.getDialog(getBaseActivity()).setTitle("请输入网址")
                                    .addInputLine(new InputTextLine())
                                    .setInputTextListener(new InputTextListener() {
                                        @Override
                                        public void onConfirm(InputLine[] result) throws Exception{
                                            if (selectedListener!=null){
                                                selectedListener.select(keys.get(position),result[0].getResult().toString());
                                            }
                                        }
                                    }).show();
                            break;
                        default:
                            if (selectedListener!=null){
                                selectedListener.select(keys.get(position),null);
                            }
                            break;
                    }

                });
            }

            @Override
            public int getItemCount() {
                return keys.size();
            }
        };

        listView.setAdapter(adapter);
        listView.setLayoutManager(new MyLinearLayoutManager(getBaseActivity()));
    }

    private void addActionBean() {
        keys.clear();
        keys.add(DoActionBean.LNK_WECHAT_SCAN);
        keys.add(DoActionBean.LNK_ALIPAY_SCAN);
        keys.add(DoActionBean.LNK_ALIPAY_GETMONEY);
        keys.add(DoActionBean.LNK_ALIPAY_SENDMONEY);
        keys.add(DoActionBean.LNK_LINK);
        keys.add(DoActionBean.LNK_TELEPHONE);
        keys.add(DoActionBean.LNK_QQ);
    }

    private OnSelectedListener selectedListener;

    public void setSelectedListener(OnSelectedListener selectedListener) {
        this.selectedListener = selectedListener;
    }

    public interface OnSelectedListener{
        void select(DoActionBean actionBean,String param);
    }
}
