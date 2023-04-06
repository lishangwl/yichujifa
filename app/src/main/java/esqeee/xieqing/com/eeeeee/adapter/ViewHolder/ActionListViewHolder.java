package esqeee.xieqing.com.eeeeee.adapter.ViewHolder;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xieqing.codeutils.util.FileUtils;

import java.io.File;

import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.adapter.ActionListAdapter;
import esqeee.xieqing.com.eeeeee.listener.ActionClickEdit;
import esqeee.xieqing.com.eeeeee.listener.ActionClickMore;
import esqeee.xieqing.com.eeeeee.listener.ActionClickRun;

public class ActionListViewHolder extends ListViewHolder{
    private ActionListAdapter actionListAdapter;
    public ActionListViewHolder(View itemView, ActionListAdapter actionListAdapter) {
        super(itemView);
        this.actionListAdapter = actionListAdapter;
    }

    @Override
    public void bindView(File ycfProject) {
        Log.d("ListViewHolder",getAdapterPosition()+"->"+ycfProject.getName());
        View convertView = itemView;
        TextView tilte = convertView.findViewById(R.id.title);
        TextView creatTime = convertView.findViewById(R.id.creatTime);
        View run = convertView.findViewById(R.id.item_run);
        View edit = convertView.findViewById(R.id.item_edit);
        View more = convertView.findViewById(R.id.item_more);


        tilte.setText(FileUtils.getFileNameNoExtension(ycfProject));
        creatTime.setText(FileUtils.getFileSize(ycfProject));
        //image.setImageBitmap(ycfProject.getIcon());

        run.setOnClickListener(new ActionClickRun(ycfProject));
        edit.setOnClickListener(new ActionClickEdit(ycfProject));
        more.setOnClickListener(new ActionClickMore(ycfProject,actionListAdapter.getContext(),actionListAdapter));
        convertView.setOnLongClickListener(v->{
            more.performClick();
            return true;
        });
    }
}
