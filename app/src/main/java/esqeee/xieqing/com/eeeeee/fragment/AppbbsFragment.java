package esqeee.xieqing.com.eeeeee.fragment;

import android.content.Intent;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.xieqing.codeutils.util.MathUtils;
import com.xieqing.codeutils.util.SizeUtils;
import com.xieqing.codeutils.util.SnackbarUtils;
import com.xieqing.codeutils.util.TimeUtils;
import com.yicu.yichujifa.ui.theme.ThemeManager;

import org.greenrobot.eventbus.EventBus;

import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.bbs.BBS;
import esqeee.xieqing.com.eeeeee.bbs.callback.CallBack;
import esqeee.xieqing.com.eeeeee.bbs.callback.CallResult;
import esqeee.xieqing.com.eeeeee.bean.CategorySelect;
import esqeee.xieqing.com.eeeeee.bean.JSONArrayBean;
import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.ui.AcrtleActivity;
import esqeee.xieqing.com.eeeeee.ui.EditorActivity;
import esqeee.xieqing.com.eeeeee.ui.UserActivity;
import esqeee.xieqing.com.eeeeee.user.User;
import esqeee.xieqing.com.eeeeee.widget.MyLinearLayoutManager;
import esqeee.xieqing.com.eeeeee.widget.gz.GZoomSwifrefresh;

public class AppbbsFragment extends BaseFragment implements GZoomSwifrefresh.OnRefreshListener, GZoomSwifrefresh.OnBottomRefreshListener {

    public GZoomSwifrefresh swipeRefreshLayout;
    public RecyclerView recyclerView;

    public JSONArrayBean data = new JSONArrayBean();
    FloatingActionButton floatingActionButton;

    @Override
    public View getContentView(LayoutInflater inflater) {
        swipeRefreshLayout = new GZoomSwifrefresh(inflater.getContext());
        recyclerView = new RecyclerView(inflater.getContext());
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setOnBottomRefreshListenrer(this);
        swipeRefreshLayout.addView(recyclerView);
        recyclerView.setBackgroundColor(Color.parseColor("#aae4e4e4"));

        FrameLayout frameLayout = new FrameLayout(inflater.getContext());
        frameLayout.addView(swipeRefreshLayout);

        if (!cate.equals("推荐")) {
            floatingActionButton = new FloatingActionButton(inflater.getContext());
            floatingActionButton.setImageResource(R.mipmap.ic_add);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(SizeUtils.dp2px(55), SizeUtils.dp2px(55));
            layoutParams.gravity = Gravity.BOTTOM | Gravity.RIGHT;
            layoutParams.setMargins(SizeUtils.dp2px(16), SizeUtils.dp2px(16), SizeUtils.dp2px(16), SizeUtils.dp2px(66));
            floatingActionButton.setLayoutParams(layoutParams);
            frameLayout.addView(floatingActionButton);
            floatingActionButton.setOnClickListener(v -> {

                startActivity(new Intent(getBaseActivity(), EditorActivity.class)
                        .putExtra("tid", tid)
                        .putExtra("cate", cate));

            });
        }


        return frameLayout;
    }

    @Override
    protected void onFragmentInit() {
        recyclerView.setLayoutManager(new MyLinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new AcrtleListAdapter());
    }

    public int tid = 0;
    public String cate;

    public AppbbsFragment setTid(int tid, String cate) {
        this.tid = tid;
        this.cate = cate;
        return this;
    }

    int page = 1;

    @Override
    public void onRefresh() {
        data.clear();
        swipeRefreshLayout.setRefreshing(true);
        page = 1;
        load(page);
    }

    protected void load(int page) {
        BBS.getAcrtle(tid, page, new CallBack() {
            @Override
            public void callBack(CallResult result) {
                swipeRefreshLayout.setRefreshing(false);
                swipeRefreshLayout.setBottomRefreshing(false);
                if (result == null) {
                    SnackbarUtils.with(swipeRefreshLayout)
                            .setMessage("刷新失败，请重试！").showError();
                } else {
                    JSONArrayBean jsonArrayBean = result.getArray("data");
                    if (jsonArrayBean == null) {
                        SnackbarUtils.with(swipeRefreshLayout)
                                .setMessage("刷新失败，请重试！").showError();
                        return;
                    }
                    data.add(jsonArrayBean);
                    //Log.d("xxxxxxxx",data.toString());
                    recyclerView.getAdapter().notifyItemRangeInserted(data.length() - jsonArrayBean.length(), jsonArrayBean.length());
                    //recyclerView.getAdapter().notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onBottomRefresh() {
        swipeRefreshLayout.setBottomRefreshing(true);
        page++;
        load(page);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (data.length() == 0) {
            onRefresh();
        }
    }

    @Override
    public void onChangTheme() {
        super.onChangTheme();
        ThemeManager.attachTheme(floatingActionButton);
    }

    private class AcrtleListAdapter extends RecyclerView.Adapter {

        private final int[] colors = new int[]{
                R.drawable.card_header_1,
                R.drawable.card_header_2,
                R.drawable.card_header_3,
                R.drawable.card_header_5,
                R.drawable.card_header_4,
                R.drawable.card_header_6,
                R.drawable.card_header_7,
        };

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new RecyclerView.ViewHolder(LayoutInflater.from(getBaseActivity()).inflate(R.layout.acrtle_list, parent, false)) {
            };
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            attachTheme(holder.itemView);
            TextView title = holder.itemView.findViewById(R.id.title);
            TextView content = holder.itemView.findViewById(R.id.content);
            ImageView icon = holder.itemView.findViewById(R.id.atvr_icon);
            TextView atvr_name = holder.itemView.findViewById(R.id.atvr_name);
            TextView atvr_time = holder.itemView.findViewById(R.id.atvr_time);
            TextView cat = holder.itemView.findViewById(R.id.cat);
            TextView zan = holder.itemView.findViewById(R.id.zans);
            TextView collects = holder.itemView.findViewById(R.id.collects);
            TextView reply = holder.itemView.findViewById(R.id.replys);

            JSONBean bean = data.getJson(position);

            if (bean == null) {
                return;
            }
            title.setText(bean.getString("title"));
            content.setText(bean.getString("content"));
            atvr_name.setText(bean.getString("username"));
            cat.setText(bean.getString("tid_name"));
            collects.setText(String.valueOf(bean.getInt("collects")));
            zan.setText(String.valueOf(bean.getInt("zans")));
            reply.setText(String.valueOf(bean.getInt("replys")));
            atvr_time.setText("更新于 " + TimeUtils.getFriendlyTimeSpanByNow(bean.getInt("updatetime")));
            BBS.attachIconNoCashe(icon, bean.getInt("uid"));

            icon.setOnClickListener(V -> {
                UserActivity.luanch(getBaseActivity(), bean.getInt("uid"), bean.getString("username") + "的动态");
            });

            holder.itemView.findViewById(R.id.cagtory).setOnClickListener(v -> {
                EventBus.getDefault().post(new CategorySelect(bean.getInt("tid")));
            });

            holder.itemView.findViewById(R.id.item_zan).setOnClickListener(v -> {
                startActivity(new Intent(getBaseActivity(), AcrtleActivity.class).putExtra("aid", bean.getInt("id")));
            });

            holder.itemView.findViewById(R.id.item_reply).setOnClickListener(v -> {
                startActivity(new Intent(getBaseActivity(), AcrtleActivity.class).putExtra("aid", bean.getInt("id")));
            });

            holder.itemView.findViewById(R.id.item_down).setOnClickListener(v -> {
                startActivity(new Intent(getBaseActivity(), AcrtleActivity.class).putExtra("aid", bean.getInt("id")));
            });

            holder.itemView.setOnClickListener(v -> {
                startActivity(new Intent(getBaseActivity(), AcrtleActivity.class).putExtra("aid", bean.getInt("id")));
            });
        }

        private void attachTheme(View itemView) {
            TextView title = itemView.findViewById(R.id.title);
            TextView content = itemView.findViewById(R.id.content);
            if (cate.equals("推荐")) {
                itemView.findViewById(R.id.theme).setBackgroundResource(colors[MathUtils.random(0, colors.length - 1)]);
                itemView.findViewById(R.id.cagtory).setVisibility(View.VISIBLE);
                title.setTextColor(Color.WHITE);
                content.setTextColor(Color.parseColor("#80ffffff"));
            } else {
                itemView.findViewById(R.id.theme).setBackgroundResource(R.drawable.card_header_0);
                itemView.findViewById(R.id.cagtory).setVisibility(View.GONE);
                title.setTextColor(Color.BLACK);
                content.setTextColor(Color.GRAY);
            }
        }

        @Override
        public int getItemCount() {
            return data.length();
        }
    }
}
