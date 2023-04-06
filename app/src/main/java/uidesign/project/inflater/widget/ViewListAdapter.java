package uidesign.project.inflater.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

import esqeee.xieqing.com.eeeeee.R;
import uidesign.project.inflater.BaseLayoutInflater;
import uidesign.project.inflater.GboalViewHolder;
import uidesign.project.inflater.listener.UITouch;

public class ViewListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<GboalViewHolder.UIView> views;


    private Context context;



    public ViewListAdapter(Context context, ArrayList<GboalViewHolder.UIView> views){
        this.views = views;
        this.context = context;
    }





    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecyclerView.ViewHolder(LayoutInflater.from(context).inflate(R.layout.view_item,parent,false)){};
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        UITouch uiView = views.get(holder.getAdapterPosition()).getTouch();

        holder.itemView.setBackgroundDrawable(uiView.isSelected()?new ColorDrawable(Color.GRAY):context.getResources().getDrawable(R.drawable.click_item));
        ((TextView)holder.itemView.findViewById(R.id.value)).setText(uiView.getAttr().getString(BaseLayoutInflater.ATTR_NAME));

        ImageView icon = ((ImageView)holder.itemView.findViewById(R.id.icon));

        if (uiView.getView() instanceof ProgressBar){
            icon.setImageResource(R.drawable.icon_progressbar);
        }else if (uiView.getView() instanceof EditText){
            icon.setImageResource(R.drawable.icon_edit);
        }else if (uiView.getView() instanceof CheckBox){
            icon.setImageResource(R.drawable.icon_checkbox);
        }else if (uiView.getView() instanceof Switch){
            icon.setImageResource(R.drawable.icon_switch);
        }else if (uiView.getView() instanceof Spinner){
            icon.setImageResource(R.drawable.icon_spinner);
        }else if (uiView.getView() instanceof SeekBar){
            icon.setImageResource(R.drawable.icon_seekbar);
        }else if (uiView.getView() instanceof ImageView){
            icon.setImageResource(R.drawable.icon_image);
        }else if (uiView.getView() instanceof Button){
            icon.setImageResource(R.drawable.icon_button);
        }else if (uiView.getView() instanceof TextView){
            icon.setImageResource(R.drawable.icon_textview);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uiView.select();
                GboalViewHolder.getInstance().select(uiView);
                notifyDataSetChanged();
                GboalViewHolder.getInstance().closeDrawableLayout(Gravity.START);
            }
        });
    }




    @Override
    public int getItemCount() {
        return views.size();
    }

}
