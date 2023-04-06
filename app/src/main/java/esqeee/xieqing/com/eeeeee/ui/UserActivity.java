package esqeee.xieqing.com.eeeeee.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.xieqing.codeutils.util.SnackbarUtils;

import butterknife.BindView;
import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.bbs.BBS;
import esqeee.xieqing.com.eeeeee.bbs.callback.CallBack;
import esqeee.xieqing.com.eeeeee.bbs.callback.CallResult;
import esqeee.xieqing.com.eeeeee.bean.JSONArrayBean;
import esqeee.xieqing.com.eeeeee.fragment.AppbbsFragment;
import esqeee.xieqing.com.eeeeee.user.User;


public class UserActivity extends BaseActivity {

    @BindView(R.id.fragment)
    FrameLayout fragment;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    int uid = 0;
    String name = "";
    public int type = 0;

    private static final int TYPE_ACRTLE = 0;
    private static final int TYPE_COLLECT = 1;
    private static final int TYPE_SEARCH = 1;
    @Override
    public int getContentLayout() {
        return R.layout.activity_user;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        supportToolBarWithBack(toolbar);

        name = getIntent().getStringExtra("name");
        uid = getIntent().getIntExtra("uid",0);
        type = getIntent().getIntExtra("type",0);
        refresh();
    }

    public static void luanch(Context context,int uid,String name){
        Intent intent = new Intent(context,UserActivity.class);
        intent.putExtra("name",name);
        intent.putExtra("uid",uid);
        intent.putExtra("type",TYPE_ACRTLE);
        context.startActivity(intent);
    }

    public static void luanch2(Context context,int uid,String name){
        Intent intent = new Intent(context,UserActivity.class);
        intent.putExtra("name",name);
        intent.putExtra("uid",uid);
        intent.putExtra("type",TYPE_COLLECT);
        context.startActivity(intent);
    }

    public static void luanch3(Context context,String keyword){
        Intent intent = new Intent(context,UserActivity.class);
        intent.putExtra("name",keyword);
        intent.putExtra("type",TYPE_SEARCH);
        context.startActivity(intent);
    }


    void refresh(){
        toolbar.setTitle(name);
        getSupportFragmentManager().beginTransaction().add(R.id.fragment,new UserAcrtleFragment().setType(type).setTid(uid,name)).commit();
    }


    public static class UserAcrtleFragment extends AppbbsFragment{
        private int type = 0;

        public UserAcrtleFragment setType(int type) {
            this.type = type;
            return this;
        }

        @Override
        protected void load(int page) {
            CallBack callBack = (CallResult result)->{
                    swipeRefreshLayout.setRefreshing(false);
                    swipeRefreshLayout.setBottomRefreshing(false);
                    if (result.getCode() != 0){
                        SnackbarUtils.with(swipeRefreshLayout)
                                .setMessage(result.getMessage()).showError();
                    }else {
                        JSONArrayBean jsonArrayBean = result.getArray("data");
                        data.add(jsonArrayBean);
                        recyclerView.getAdapter().notifyItemRangeInserted(data.length() - jsonArrayBean.length(),jsonArrayBean.length());
                    }
            };
            if (type == TYPE_ACRTLE){
                BBS.getUserAcrtle(tid,page,User.getUser(),callBack);
            }else if(type == TYPE_SEARCH){
                BBS.searchAcrtle(cate,page,callBack);
            }else{
                BBS.getUserCollectAcrtle(tid,page,User.getUser(),callBack);
            }
        }

        @Override
        public View getContentView(LayoutInflater inflater) {
            ViewGroup view = (ViewGroup) super.getContentView(inflater);
            view.removeViewAt(1);
            return view;
        }
    }
}
