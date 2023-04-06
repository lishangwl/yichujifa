package esqeee.xieqing.com.eeeeee.fragment;

import android.app.Dialog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import esqeee.xieqing.com.eeeeee.BroswerActivity;
import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.adapter.MyViewHolder;
import esqeee.xieqing.com.eeeeee.widget.MyLinearLayoutManager;

public class JCFragment extends BaseFragment {

    class MyAdapter extends RecyclerView.Adapter{

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = null;
            switch (viewType){
                case 0:
                    view = inflate(view,R.layout.jc_1);
                    break;
                case 1:
                    view = inflate(view,R.layout.jc_2);
                    break;
                case 2:
                    view = inflate(view,R.layout.jc_3);
                    break;
                case 3:
                    view = inflate(view,R.layout.jc_4);
                    break;
                case 4:
                    view = inflate(view,R.layout.jc_6);
                    break;
                case 5:
                    view = inflate(view,R.layout.jc_7);
                    break;
                case 6:
                    view = inflate(view,R.layout.jc_8);
                    break;
                case 7:
                    view = inflate(view,R.layout.jc_9);
                    break;
                case 8:
                    view = inflate(view,R.layout.jc_10);
                    break;
            }
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            if (layoutParams==null){
                layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
            layoutParams.width = -1;
            layoutParams.height = -2;
            view.setLayoutParams(layoutParams);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            View view = holder.itemView;
            switch (position){
                case 0:
                    toggle(view.findViewById(R.id.item),((TextView)view.findViewById(R.id.text_2)).getText().toString(),((TextView)view.findViewById(R.id.text_1)).getText().toString());
                    break;
                case 1:
                    toggle(view.findViewById(R.id.item),((TextView)view.findViewById(R.id.text_2)).getText().toString(),((TextView)view.findViewById(R.id.text_1)).getText().toString());
                    break;
                case 2:
                    toggle(view.findViewById(R.id.item),((TextView)view.findViewById(R.id.text_2)).getText().toString(),((TextView)view.findViewById(R.id.text_1)).getText().toString());
                    break;
                case 3:
                    toggle(view.findViewById(R.id.item),((TextView)view.findViewById(R.id.text_2)).getText().toString(),((TextView)view.findViewById(R.id.text_1)).getText().toString());
                    break;
                case 4:
                    view.findViewById(R.id.item_left).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            BroswerActivity.luanch(getActivity(),"http://www.baidu.com/teach/Texttutorial.html");
                        }
                    });
                    view.findViewById(R.id.item_right).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            BroswerActivity.luanch(getActivity(),"http://www.baidu.com/teach/videotutorial.html");
                        }
                    });
                    break;
                case 5:
                    toggle(view.findViewById(R.id.item),((TextView)view.findViewById(R.id.text_2)).getText().toString(),((TextView)view.findViewById(R.id.text_1)).getText().toString());
                    break;
                case 6:
                    toggle(view.findViewById(R.id.item),((TextView)view.findViewById(R.id.text_2)).getText().toString(),((TextView)view.findViewById(R.id.text_1)).getText().toString());
                    break;
                case 7:
                    toggle(view.findViewById(R.id.item),((TextView)view.findViewById(R.id.text_2)).getText().toString(),((TextView)view.findViewById(R.id.text_1)).getText().toString());
                    break;
                case 8:
                    View.OnClickListener clickListener = (v)->{
                        BroswerActivity.luanch(getActivity(),v.getTag().toString());
                    };
                    view.findViewById(R.id.item_1).setOnClickListener(clickListener);
                    view.findViewById(R.id.item_2).setOnClickListener(clickListener);
                    view.findViewById(R.id.item_3).setOnClickListener(clickListener);
                    view.findViewById(R.id.item_4).setOnClickListener(clickListener);
                    break;
            }
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public int getItemCount() {
            return 9;
        }

        public View inflate(View view,int id){
            return View.inflate(getActivity(),id,null);
            /*if (view == null){
                return View.inflate(getActivity(),id,null);
            }
            return view;*/
        }
        Dialog dialog;
        View view;
        public void toggle(View itemView, final String title, final String detailText){
            if (dialog==null){
                dialog = new Dialog(getActivity(), R.style.common_dialog);
                view = View.inflate(getActivity(),R.layout.detial,null);

                Window win = dialog.getWindow();
                win.getDecorView().setPadding(0, 0, 0, 0);
                WindowManager.LayoutParams lp = win.getAttributes();
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.MATCH_PARENT;

                win.setWindowAnimations(R.style.bottomMenuAnimStyle);

                win.setAttributes(lp);

                dialog.setContentView(view);
                dialog.setCanceledOnTouchOutside(true);
                dialog.setCancelable(true);
            }

            (view.findViewById(R.id.close)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view2) {
                    //dialog.dismiss();
                    ((TextView)view.findViewById(R.id.detail)).setText(detailText);
                    ((TextView)view.findViewById(R.id.title)).setText(title);
                    dialog.show();
                }
            });
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }
    }
    private RecyclerView listView;
    @Override
    public View getContentView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.frgament_jc,null);
    }

    @Override
    protected void onFragmentInit() {
        listView = (RecyclerView) root;
        listView.setAdapter(new MyAdapter());
        listView.setLayoutManager(new MyLinearLayoutManager(getActivity()));
    }
}
