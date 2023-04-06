//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.thl.filechooser;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.filechooser.R;
import com.example.filechooser.R.id;
import com.example.filechooser.R.drawable;

import java.util.ArrayList;
import java.util.List;

public class FileAdapter extends CommonAdapter<FileInfo> {
    private String chooseType;
    private ItemClickListener mItemClickListener;

    public FileAdapter(Context context, ArrayList<FileInfo> dataList, int resId, String chooseType) {
        super(context,dataList , resId);
        this.chooseType = chooseType;
        Log.e("filechooser",dataList.toString());
    }


    public void reset(){
        for (int i = 0 ; i<dataList.size();i++){
            dataList.get(i).setChoose(false);
        }
    }


    public void bindView(ViewHolder holder, final FileInfo data, final int position) {
        TextView textView = (TextView)holder.itemView.findViewById(id.fileName);
        TextView textTime = (TextView)holder.itemView.findViewById(id.fileTime);
        textView.setText(data.getFileName());
        textTime.setText(data.getCreateTime());
        ImageView imageView = (ImageView)holder.itemView.findViewById(id.fileIcon);
        View divider = holder.itemView.findViewById(id.divider);
        if ("type_video".equals(data.getFileType())) {
            imageView.setImageResource(drawable.format_video);
        } else if ("type_audio".equals(data.getFileType())) {
            imageView.setImageResource(drawable.format_music);
        } else if ("type_apk".equals(data.getFileType())) {
            imageView.setImageResource(drawable.format_app);
        }else if ("type_ycf".equals(data.getFileType())) {
            imageView.setImageResource(R.drawable.icon);
        } else if (!"type_zip".equals(data.getFileType()) && !"type_rar".equals(data.getFileType())) {
            if (!"type_jpeg".equals(data.getFileType()) && !"type_jpg".equals(data.getFileType()) && !"type_png".equals(data.getFileType())) {
                if ("type_folder".equals(data.getFileType())) {
                    imageView.setImageResource(drawable.format_folder);
                } else {
                    imageView.setImageResource(drawable.format_other);
                }
            } else {
                imageView.setImageResource(drawable.format_picture);
            }
        } else {
            imageView.setImageResource(drawable.format_compress);
        }

        if (position != this.dataList.size() - 1) {
            divider.setVisibility(View.VISIBLE);
        } else {
            divider.setVisibility(View.GONE);
        }

        ImageView fileChoose = (ImageView)holder.itemView.findViewById(id.fileChoose);
        if (dataList.get(position).isChoose()) {
            fileChoose.setImageResource(drawable.log_choose_checkbox_on);
        } else {
            fileChoose.setImageResource(drawable.log_choose_checkbox_off);
        }

        holder.itemView.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (FileAdapter.this.mItemClickListener != null) {
                    FileAdapter.this.mItemClickListener.onItemClick(v, position, data);
                }

            }
        });
        if (this.chooseType.equals("type_all")) {
            fileChoose.setVisibility(View.VISIBLE);
            fileChoose.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    FileAdapter.this.notifyData(position);
                }
            });
        } else {
            boolean folder;
            if (this.chooseType.equals("type_folder")) {
                folder = data.isFolder();
                if (folder) {
                    fileChoose.setVisibility(View.VISIBLE);
                    fileChoose.setOnClickListener(new OnClickListener() {
                        public void onClick(View v) {
                            FileAdapter.this.notifyData(position);
                        }
                    });
                } else {
                    fileChoose.setOnClickListener(new OnClickListener() {
                        public void onClick(View v) {
                        }
                    });
                    fileChoose.setVisibility(View.GONE);
                }
            } else if (this.chooseType.equals("type_file")) {
                folder = data.isFolder();
                if (!folder) {
                    fileChoose.setVisibility(View.VISIBLE);
                    fileChoose.setOnClickListener(new OnClickListener() {
                        public void onClick(View v) {
                            FileAdapter.this.notifyData(position);
                        }
                    });
                } else {
                    fileChoose.setOnClickListener(new OnClickListener() {
                        public void onClick(View v) {
                        }
                    });
                    fileChoose.setVisibility(View.GONE);
                }
            } else if (this.chooseType.equals("type_image")) {
                if (!"type_jpeg".equals(data.getFileType()) && !"type_jpg".equals(data.getFileType()) && !"type_png".equals(data.getFileType())) {
                    fileChoose.setOnClickListener(new OnClickListener() {
                        public void onClick(View v) {
                        }
                    });
                    fileChoose.setVisibility(View.GONE);
                } else {
                    fileChoose.setVisibility(View.VISIBLE);
                    fileChoose.setOnClickListener(new OnClickListener() {
                        public void onClick(View v) {
                            FileAdapter.this.notifyData(position);
                        }
                    });
                }
            } else if (this.chooseType.equals("type_package")) {
                if (!"type_zip".equals(data.getFileType()) && !"type_rar".equals(data.getFileType())) {
                    fileChoose.setOnClickListener(new OnClickListener() {
                        public void onClick(View v) {
                        }
                    });
                    fileChoose.setVisibility(View.GONE);
                } else {
                    fileChoose.setVisibility(View.VISIBLE);
                    fileChoose.setOnClickListener(new OnClickListener() {
                        public void onClick(View v) {
                            FileAdapter.this.notifyData(position);
                        }
                    });
                }
            } else if (this.chooseType.equals(data.getFileType())) {
                fileChoose.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        FileAdapter.this.notifyData(position);
                    }
                });
                fileChoose.setVisibility(View.VISIBLE);
            } else {
                fileChoose.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                    }
                });
                fileChoose.setVisibility(View.GONE);
            }
        }

    }

    public void setItemClickListener(ItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public void notifyClick(FileInfo data, int position) {
        if (FileChooserActivity.mFileChooser.isChooseSingle()){
            reset();
        }

        if (this.chooseType.equals("type_all")) {
            this.notifyData(position);
        } else {
            boolean folder;
            if (this.chooseType.equals("type_folder")) {
                folder = data.isFolder();
                if (folder) {
                    this.notifyData(position);
                }
            } else if (this.chooseType.equals("type_file")) {
                folder = data.isFolder();
                if (!folder) {
                    this.notifyData(position);
                }
            } else if (this.chooseType.equals("type_image")) {
                if ("type_jpeg".equals(data.getFileType()) || "type_jpg".equals(data.getFileType()) || "type_png".equals(data.getFileType())) {
                    this.notifyData(position);
                }
            } else if (this.chooseType.equals("type_package")) {
                if ("type_zip".equals(data.getFileType()) || "type_rar".equals(data.getFileType())) {
                    this.notifyData(position);
                }
            } else if (this.chooseType.equals(data.getFileType())) {
                this.notifyData(position);
            }
        }

    }


    public String getChooseFilePath() {
        String[] choose = getChooseFilePaths();
        return choose.length>0 ? choose[0]:"";
    }
    public String[] getChooseFilePaths(){
        List<String> choose = new ArrayList<>();
        for (int i = 0 ; i < dataList.size() ; i++){
            if (dataList.get(i).isChoose()){
                choose.add(dataList.get(i).getFilePath());
            }
        }
        return choose.toArray(new String[]{});
    }
    public void notifyData() {
        this.notifyDataSetChanged();
    }

    public void notifyData(int position) {
        dataList.get(position).setChoose(!dataList.get(position).isChoose());
        notifyData();
    }

    public interface ItemClickListener {
        void onItemClick(View var1, int var2, FileInfo var3);
    }
}
