package esqeee.xieqing.com.eeeeee.ui;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.thl.filechooser.FileChooser;
import com.thl.filechooser.FileInfo;
import com.xieqing.codeutils.util.FileUtils;
import com.yicu.yichujifa.apk.bean.Action;
import com.yicu.yichujifa.apk.bean.FileBean;
import com.yicu.yichujifa.apk.bean.ResFile;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import esqeee.xieqing.com.eeeeee.R;
import esqeee.xieqing.com.eeeeee.fragment.ActionsFragment;
import esqeee.xieqing.com.eeeeee.library.captrue.IntentExtras;
import esqeee.xieqing.com.eeeeee.widget.MyLinearLayoutManager;

public class ApkDetailActivity extends BaseActivity {

    FileBean bean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportToolBarWithBack(R.id.toolbar);
        IntentExtras extras = IntentExtras.fromIntentAndRelease(getIntent());
        if (extras == null){
            finish();
            return;
        }
        bean = extras.get("file");


        recyclerView.setLayoutManager(new MyLinearLayoutManager(self()));
        recyclerView.setAdapter(new RecyclerView.Adapter() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new RecyclerView.ViewHolder(getLayoutInflater().inflate(R.layout.apk_file_item,parent,false)) {};
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                ImageView icon = holder.itemView.findViewById(R.id.icon);
                TextView path = holder.itemView.findViewById(R.id.path);
                TextView name = holder.itemView.findViewById(R.id.name);
                path.setText(bean.path(position));
                name.setText(bean.name(position));
                icon.setImageDrawable(bean.icon(position));

                holder.itemView.setOnLongClickListener(v->{
                    new AlertDialog.Builder(self()).setTitle("确定删除吗？")
                            .setNegativeButton("确定",(d,in)->{
                                bean.delete(holder.getAdapterPosition());
                                recyclerView.getAdapter().notifyItemRemoved(holder.getAdapterPosition());
                            }).create().show();
                    return true;
                });
                holder.itemView.setOnClickListener(v->{
                    if (bean instanceof Action){
                        Intent intent = new Intent(self(), ApkDetailActivity.class);
                        IntentExtras.newExtras()
                                .put("file", ((Action)bean).getItem(holder.getAdapterPosition()).getResFile())
                                .putInIntent(intent);
                        startActivity(intent);
                    }

                });
            }

            @Override
            public int getItemCount() {
                return bean.size();
            }
        });
    }


    @Override
    public int getContentLayout() {
        return R.layout.activity_apk_detail;
    }

    @BindView(R.id.recylerView)
    RecyclerView recyclerView;



    @OnClick(R.id.file_add)
    public void addFile(){
        if (bean instanceof Action){
            addAction();
        }else{
            addTFile();
        }
    }

    private void addTFile() {
        FileChooser fileChooser = new FileChooser(self(), new FileChooser.FileChoosensListener() {
            @Override
            public void onFileChoosens(String[] filePaths) {
                for (String file :filePaths){
                    if (file!=null && !file.isEmpty()){
                        File file1 = new File(file);
                        if (file1!=null && file1.exists() && !file1.isDirectory()){
                            ResFile.ResItem item = new ResFile.ResItem(FileUtils.getFileNameNoExtension(file1),file);
                            bean.add(item);
                        }
                    }
                    recyclerView.getAdapter().notifyDataSetChanged();
                }
            }
        });
        fileChooser.setBackIconRes(R.drawable.ic_back);
        fileChooser.setTitle("选择打包资源文件");
        fileChooser.setDoneText("确定");
        fileChooser.setThemeColor(R.color.colorAccent);
        fileChooser.setChooseType(FileInfo.FILE_TYPE_ALL);
        fileChooser.showFile(true);  //是否显示文件
        fileChooser.open();
    }

    private void addAction() {
        ActionsFragment fragment = new ActionsFragment();
        fragment.setOnActionSelectedListener(file->{
            if (bean.exits(file.getAbsolutePath())){
                return;
            }
            Action.ActionItem item = new Action.ActionItem(FileUtils.getFileNameNoExtension(file),file.getAbsolutePath());
            bean.add(item);
            recyclerView.getAdapter().notifyItemInserted(bean.size());
            fragment.dismiss();
        });
        fragment.show(getSupportFragmentManager(),"actions");
    }
}
