package esqeee.xieqing.com.eeeeee.view;

import android.app.Activity;
import android.text.Editable;
import android.text.Spannable;
import android.util.Log;
import android.view.View;

import com.liyi.flow.FlowView;
import com.liyi.flow.adapter.BaseFlowHolder;
import com.liyi.flow.adapter.QuickFlowAdapter;

import java.util.ArrayList;
import java.util.List;

import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.fragment.VariableTableFragment;
import esqeee.xieqing.com.eeeeee.widget.ValotionEdittext;
import esqeee.xieqing.com.eeeeee.widget.span.IconTextSpan;

public class ValotionView implements FlowView.OnItemClickListener{
    private FlowAdapter flowAdapter;
    private Activity activity;
    private FlowView flowView;
    public ValotionView(Activity context, FlowView flowView){
        flowAdapter = new FlowAdapter();
        flowView.setAdapter(flowAdapter);
        this.activity = context;
        this.flowView = flowView;

        flowView.setOnItemClickListener(this);
    }


    public void bind(List<JSONBean> variables){
        flowView.removeAllViews();
        List<JSONBean> jsonBeans = new ArrayList<>();
        jsonBeans.addAll(VariableTableFragment.finalList);
        jsonBeans.addAll(variables);
        flowAdapter.updateData(jsonBeans);
        Log.d("Valotion",flowAdapter.getData()+"=>"+flowAdapter.getItemCount());
        //flowAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(int position, View view) {
        View fouc = activity.getWindow().getDecorView().findFocus();
        String valotionName = flowAdapter.getData().get(position).getString("name");
        int valotionType = flowAdapter.getData().get(position).getInt("type");
        if (fouc instanceof ValotionEdittext){
            ValotionEdittext edittext = (ValotionEdittext) fouc;
            Editable editable = edittext.getText();


            int start = edittext.getSelectionStart();
            editable.insert(edittext.getSelectionStart(),"{"+valotionName+"}");
            IconTextSpan span = new IconTextSpan();
            editable.setSpan(span,start,valotionName.length()+2+start,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    private class FlowAdapter extends QuickFlowAdapter<JSONBean, BaseFlowHolder> {

        public FlowAdapter() {
            super();
            addItemType(0, R.layout.variable_item_view);
        }

        @Override
        public int onHandleViewType(int position) {
            return 0;
        }

        @Override
        public void onHandleViewHolder(BaseFlowHolder holder, int position, JSONBean item) {
            holder.getTextView(R.id.variable_name).setText(item.getString("name"));
            holder.getConvertView().setBackground(activity.getResources().getDrawable(position<VariableTableFragment.finalList.size() ? R.drawable.let:R.drawable.var));
        }
    }
}
