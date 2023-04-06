package esqeee.xieqing.com.eeeeee.adapter.holder_item;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sevenheaven.segmentcontrol.SegmentControl;
import com.xieqing.codeutils.util.AppUtils;
import com.xieqing.codeutils.util.PhoneUtils;
import com.yicu.yichujifa.ui.theme.ThemeManager;

import butterknife.BindView;
import ch.ielse.view.SwitchView;
import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.adapter.MyAdapter;
import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.fragment.AppsFragment;
import esqeee.xieqing.com.eeeeee.ui.BaseActivity;

public class AppHolder extends BaseHolder{
    @BindView(R.id.app)
    View app;
    @BindView(R.id.app_name)
    TextView app_name;
    @BindView(R.id.app_icon)
    ImageView app_icon;

    @BindView(R.id.action)
    SegmentControl segmentControl;
    public AppHolder(Context context, MyAdapter adapter) {
        super(context,R.layout.holder_app,adapter);
    }

    @Override
    public void onBind(JSONBean jsonBean) {
        super.onBind(jsonBean);
        requestTime(true);

        JSONBean param = jsonBean.getJson("param");
        String pack = param.getString("packName");
        AppUtils.AppInfo appInfo = AppUtils.getAppInfo(pack);

        app_name.setText(appInfo == null ? "Null" : appInfo.getName());
        app_icon.setImageDrawable(appInfo == null ? null : appInfo.getIcon());



        segmentControl.setSelectedIndex(param.getInt("action",0));
        segmentControl.setOnSegmentControlClickListener(index -> {
            param.put("action",index);
        });

        app.setOnClickListener(v->{
            AppsFragment appsFragment = new AppsFragment();
            appsFragment.setOnAppsSelectedListener((appInfo2)-> {
                param.put("appName",appInfo2.getName()).put("packName",appInfo2.getPackageName());
                app_name.setText(appInfo2 == null ? "Null" : appInfo2.getName());
                app_icon.setImageDrawable(appInfo2 == null ? null : appInfo2.getIcon());
                appsFragment.dismiss();
            });
            appsFragment.show(((BaseActivity)getContext()).getSupportFragmentManager(),"chooseApp");
        });


        ThemeManager.attachTheme(segmentControl);
    }



    @Override
    public void initView() {
    }

    @Override
    public int getIcon() {
        return R.drawable.ic_yingyongguanli;
    }

    @Override
    public String getName() {
        return "应用程序";
    }
}
