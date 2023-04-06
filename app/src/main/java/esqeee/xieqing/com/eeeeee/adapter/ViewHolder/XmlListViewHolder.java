package esqeee.xieqing.com.eeeeee.adapter.ViewHolder;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.xieqing.codeutils.util.FileIOUtils;
import com.xieqing.codeutils.util.FileUtils;
import com.yicu.yichujifa.GlobalContext;

import java.io.File;

import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.adapter.ActionListAdapter;
import esqeee.xieqing.com.eeeeee.listener.XmlClickMore;
import esqeee.xieqing.com.eeeeee.utils.ActivationUtils;
import uidesign.MainActivity;
import uidesign.UiDesignActivity;

public class XmlListViewHolder extends ListViewHolder{
    private ActionListAdapter actionListAdapter;
    public XmlListViewHolder(View itemView, ActionListAdapter actionListAdapter) {
        super(itemView);
        this.actionListAdapter = actionListAdapter;
    }

    @Override
    public void bindView(File ycfProject) {
        View convertView = itemView;
        TextView tilte = convertView.findViewById(R.id.title);
        View run = convertView.findViewById(R.id.item_run);
        View edit = convertView.findViewById(R.id.item_edit);
        View more = convertView.findViewById(R.id.item_more);


        tilte.setText(FileUtils.getFileNameNoExtension(ycfProject));
        //TODO 运行按钮被点击
        Log.d("Test","运行按钮被点击");
        run.setOnClickListener(v->{
            if (ActivationUtils.isActivation()){
                GlobalContext.getContext().startActivity(new Intent(GlobalContext.getContext(),
                        MainActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .putExtra("layout", FileIOUtils.readFile2String(ycfProject))
                );
            }else{
                ActivationUtils.showActivationDialog(convertView.getContext());
            }

        });
        edit.setOnClickListener(v -> {
            GlobalContext.getContext().startActivity(new Intent(GlobalContext.getContext(),
                    UiDesignActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .putExtra("layout", ycfProject.getPath())
            );
        });
        more.setOnClickListener(new XmlClickMore(ycfProject,actionListAdapter.getContext(),actionListAdapter));
    }
}
