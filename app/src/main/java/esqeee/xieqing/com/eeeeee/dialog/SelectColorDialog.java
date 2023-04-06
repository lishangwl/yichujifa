package esqeee.xieqing.com.eeeeee.dialog;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.fragment.BaseFragment;
import esqeee.xieqing.com.eeeeee.fragment.FindColorImageFragment;
import esqeee.xieqing.com.eeeeee.listener.OnActionAddListener;
import esqeee.xieqing.com.eeeeee.ui.BaseActivity;
import esqeee.xieqing.com.eeeeee.widget.viewPager.LazyViewPager;
import esqeee.xieqing.com.eeeeee.widget.viewPager.LazyViewPagerAdapter;

public class SelectColorDialog extends BaseDialog {
    private BaseActivity context;
    public Bitmap bitmap;
    private OnActionAddListener onActionAddListener;
    public boolean isLongClick=false;
    public SelectColorDialog setLongClick(boolean b) {
        isLongClick = b;
        return this;
    }

    public interface SelectColor{
        void select(int color,String colorHex);
    }

    public SelectColorDialog(BaseActivity context, @Nullable Bitmap bitmap,OnActionAddListener onActionAddListener) {
        super(context);
        this.bitmap = bitmap;
        this.context = context;
        this.onActionAddListener = onActionAddListener;
        root = View.inflate(context,R.layout.dialog_findcolor,null);
        setContentView(root);

        initView();
    }
    private SelectColor selectColor;
    public SelectColorDialog(BaseActivity context, Bitmap bitmap,SelectColor selectColor) {
        super(context);
        this.bitmap = bitmap;
        this.context = context;
        this.selectColor = selectColor;
        root = View.inflate(context,R.layout.dialog_findcolor,null);
        setContentView(root);

        initView();
    }
    private boolean chooseAll = true;

    public SelectColorDialog setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
        return this;
    }


    public BaseActivity getActivity() {
        return context;
    }

    private View root;
    private List<BaseFragment> fragments = new ArrayList<>();

    @BindView(R.id.viewPager)
    LazyViewPager viewPager;

    @BindView(R.id.tablayout)
    TabLayout tabLayout;

    @BindView(R.id.toolbar)
    Toolbar toolbar;



    private void initView() {
        ButterKnife.bind(this,root);
        initFragments();
        toolbar.setNavigationOnClickListener(view -> {
            dismiss();
        });
        viewPager.setScroll(false);
        viewPager.setAdapter(new LazyViewPagerAdapter() {
            @Override
            protected View getItem(ViewGroup container, int position) {
                return getFragmentView(fragments.get(position));
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        });
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setText("图片取色");
    }
    private FindColorImageFragment findColorImageFragment;
    private void initFragments() {
        findColorImageFragment = (FindColorImageFragment) FindColorImageFragment.create(bitmap).setSelectColorDialog(this).setBaseActivity(context);
        findColorImageFragment.setSelectedListener(this::selectColor);


        fragments.add(findColorImageFragment);

    }


    public void selectColor(String color){
        dismiss();
        if (onActionAddListener == null){
            Log.d("selectColor",color);
            selectColor.select(Color.parseColor(color),color);
        }else{
            JSONBean param = new JSONBean()
                    .put("color",color);
            if (rect!=null){
                param.put("x",rect.left);
                param.put("y",rect.top);
                param.put("width",rect.width());
                param.put("height",rect.height());
            }
            JSONBean jsonBean = new JSONBean()
                    .put("actionType",rect == null?(isLongClick?67:58):(isLongClick?68:59))
                    .put("witeTime",1000)
                    .put("param",param);
            onActionAddListener.addAction(jsonBean);
        }
    }

    public View getFragmentView(BaseFragment fragment){
        return fragment.onCreateView(getActivity().getLayoutInflater(),null,null);
    }
    private Rect rect;
    public SelectColorDialog setRect(Rect rect) {
        this.rect = rect;
        return this;
    }
}
