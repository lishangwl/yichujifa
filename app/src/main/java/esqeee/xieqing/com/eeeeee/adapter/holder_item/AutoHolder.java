package esqeee.xieqing.com.eeeeee.adapter.holder_item;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sevenheaven.segmentcontrol.SegmentControl;
import com.xieqing.codeutils.util.AppUtils;
import com.xieqing.codeutils.util.FileUtils;
import com.yicu.yichujifa.ui.theme.ThemeManager;

import java.io.File;

import butterknife.BindView;
import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.adapter.MyAdapter;
import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.fragment.ActionsFragment;
import esqeee.xieqing.com.eeeeee.fragment.AppsFragment;
import esqeee.xieqing.com.eeeeee.helper.PathHelper;
import esqeee.xieqing.com.eeeeee.ui.BaseActivity;

public class AutoHolder extends BaseHolder{
    @BindView(R.id.app)
    View app;
    @BindView(R.id.app_name)
    TextView app_name;

    @BindView(R.id.run)
    SegmentControl run;

    public AutoHolder(Context context, MyAdapter adapter) {
        super(context,R.layout.holder_auto,adapter);
    }

    @Override
    public void onBind(JSONBean jsonBean) {
        super.onBind(jsonBean);
        requestTime(true);


        JSONBean param = jsonBean.getJson("param");

        String pack = PathHelper.covert(param.getString("actionId"));
        File action = new File(pack);

        app_name.setText(action == null ? "Null" : FileUtils.getFileNameNoExtension(action));

        app.setOnClickListener(v->{
            ActionsFragment appsFragment = new ActionsFragment();
            appsFragment.setOnActionSelectedListener((File file)->{
                param.put("actionId",file.getAbsolutePath());
                app_name.setText(file == null ? "Null" : FileUtils.getFileNameNoExtension(file));
                appsFragment.dismiss();
            });
            appsFragment.show(((BaseActivity)getContext()).getSupportFragmentManager(),"chooseAction");
        });

        int type = param.getInt("type",-1);
        if (type == -1){
            type = param.getBoolean("runInCurrentThread",true)?0:1;
            param.put("type",type);
        }

        run.setSelectedIndex(type);
        run.setOnSegmentControlClickListener(i->{
            param.put("type", i);
        });
        ThemeManager.attachTheme(run);
    }



    @Override
    public void initView() {
    }

    @Override
    public int getIcon() {
        return R.drawable.holder_0_1;
    }

    @Override
    public String getName() {
        return "操作自动化";
    }
}
