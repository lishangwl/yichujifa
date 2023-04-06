//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.thl.filechooser;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.appcompat.widget.ListPopupWindow;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.LayoutManager;
import androidx.recyclerview.widget.RecyclerView.OnScrollListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.filechooser.R;
import com.thl.filechooser.CommonAdapter.OnItemClickListener;
import com.thl.filechooser.FileAdapter.ItemClickListener;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FileChooserActivity extends AppCompatActivity {
    private boolean showFile = true;
    private boolean showHideFile = true;
    public static FileChooser mFileChooser;
    private String mChoosenFilePath;
    private FileTourController tourController;
    private FileAdapter adapter;
    private CurrentFileAdapter currentFileAdapter;
    private RecyclerView fileRv;
    private int firstItemPosition = 0;
    private int lastItemPosition = 0;
    private HashMap<Integer, Integer> firstItemPositionMap;
    private HashMap<Integer, Integer> lastItemPositionMap;

    public FileChooserActivity() {
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_file_chooser);
        this.initListener();
    }

    private void initListener() {
        this.findViewById(R.id.back).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                FileChooserActivity.this.onBackPressed();
            }
        });
        this.findViewById(R.id.rightText).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                FileChooserActivity.this.clickRightText();
            }
        });
        this.showFile = this.getIntent().getBooleanExtra("showFile", true);
        this.showHideFile = this.getIntent().getBooleanExtra("showHideFile", true);
        this.mChoosenFilePath = this.getIntent().getStringExtra("currentPath");
        String title = this.getIntent().getStringExtra("title");
        String doneText = this.getIntent().getStringExtra("doneText");
        int backIconRes = this.getIntent().getIntExtra("backIconRes", -1);
        String chooseType = this.getIntent().getStringExtra("chooseType");
        int themeColorRes = this.getIntent().getIntExtra("themeColorRes", -1);
        this.tourController = new FileTourController(this, this.mChoosenFilePath);
        this.tourController.setShowFile(this.showFile);
        this.tourController.setShowHideFile(this.showHideFile);
        ImageView back = (ImageView)this.findViewById(R.id.back);
        TextView tvTitle = (TextView)this.findViewById(R.id.title);
        TextView tvRightText = (TextView)this.findViewById(R.id.rightText);
        View bgView = this.findViewById(R.id.bg_title);
        if (backIconRes != -1) {
            back.setImageResource(backIconRes);
        }

        tvTitle.setText(title);
        tvRightText.setText(doneText);
        if (themeColorRes != -1) {
            bgView.setBackgroundResource(themeColorRes);
        }

        this.adapter = new FileAdapter(this, (ArrayList)this.tourController.getCurrenFileInfoList(), R.layout.item_file, chooseType);
        this.fileRv = (RecyclerView)this.findViewById(R.id.fileRv);
        this.fileRv.setLayoutManager(new LinearLayoutManager(this, 1, false));
        this.fileRv.setAdapter(this.adapter);
        final RecyclerView currentPath = (RecyclerView)this.findViewById(R.id.currentPath);
        this.currentFileAdapter = new CurrentFileAdapter(this, (ArrayList)this.tourController.getCurrentFolderList(), R.layout.item_current_file);
        currentPath.setLayoutManager(new LinearLayoutManager(this, 0, false));
        currentPath.setAdapter(this.currentFileAdapter);
        currentPath.scrollToPosition(this.tourController.getCurrentFolderList().size() - 1);
        this.firstItemPositionMap = new HashMap();
        this.lastItemPositionMap = new HashMap();
        this.fileRv.setOnScrollListener(new OnScrollListener() {
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                LayoutManager layoutManager = recyclerView.getLayoutManager();
                if (recyclerView.getLayoutManager() != null && layoutManager instanceof LinearLayoutManager) {
                    LinearLayoutManager linearManager = (LinearLayoutManager)layoutManager;
                    FileChooserActivity.this.firstItemPosition = linearManager.findFirstVisibleItemPosition();
                    FileChooserActivity.this.lastItemPosition = linearManager.findLastVisibleItemPosition();
                }

            }
        });
        this.adapter.setItemClickListener(new ItemClickListener() {
            public void onItemClick(View view, int position, FileInfo data) {
                File selectFile = new File(((FileInfo)FileChooserActivity.this.tourController.getCurrenFileInfoList().get(position)).getFilePath());
                if (selectFile.isDirectory()) {
                    ArrayList<FileInfo> childFileInfoList = (ArrayList)FileChooserActivity.this.tourController.addCurrentFile(selectFile);
                    FileChooserActivity.this.adapter.setData(childFileInfoList);
                    FileChooserActivity.this.adapter.notifyData();
                    FileChooserActivity.this.currentFileAdapter.setData(FileChooserActivity.this.tourController.getCurrentFolderList());
                    FileChooserActivity.this.currentFileAdapter.notifyDataSetChanged();
                    int sign = FileChooserActivity.this.tourController.getCurrentFolderList().size() - 1;
                    currentPath.scrollToPosition(sign);
                    FileChooserActivity.this.firstItemPositionMap.put(sign, FileChooserActivity.this.firstItemPosition);
                    FileChooserActivity.this.lastItemPositionMap.put(sign, FileChooserActivity.this.lastItemPosition);
                } else {
                    FileChooserActivity.this.adapter.notifyClick(data, position);
                }

            }
        });
        this.currentFileAdapter.setItemClickListener(new OnItemClickListener() {
            public void onItemClick(View view, int position) {
                List<FileInfo> fileInfoList = FileChooserActivity.this.tourController.resetCurrentFile(position);
                FileChooserActivity.this.adapter.setData(fileInfoList);
                FileChooserActivity.this.adapter.notifyData();
                FileChooserActivity.this.currentFileAdapter.setData(FileChooserActivity.this.tourController.getCurrentFolderList());
                FileChooserActivity.this.currentFileAdapter.notifyDataSetChanged();
                currentPath.scrollToPosition(FileChooserActivity.this.tourController.getCurrentFolderList().size() - 1);
            }
        });
        this.findViewById(R.id.switchSdcard).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                final ListPopupWindow listPopupWindow = new ListPopupWindow(FileChooserActivity.this);
                listPopupWindow.setAnchorView(v);
                ArrayList<String> sdcardList = new ArrayList();
                sdcardList.add("手机存储");
                if (FileTourController.getStoragePath(FileChooserActivity.this, true) != null) {
                    sdcardList.add("SD卡");
                }

                SdCardAdapter sdCardAdapter = new SdCardAdapter(FileChooserActivity.this, sdcardList);
                listPopupWindow.setAdapter(sdCardAdapter);
                listPopupWindow.setWidth(sdCardAdapter.getItemViewWidth());
                listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        FileChooserActivity.this.firstItemPositionMap.clear();
                        FileChooserActivity.this.lastItemPositionMap.clear();
                        FileChooserActivity.this.firstItemPosition = 0;
                        FileChooserActivity.this.lastItemPosition = 0;
                        if (FileChooserActivity.this.tourController != null) {
                            FileChooserActivity.this.tourController.switchSdCard(position);
                        }

                        if (FileChooserActivity.this.adapter != null) {
                            FileChooserActivity.this.adapter.setData(FileChooserActivity.this.tourController.getCurrenFileInfoList());
                            FileChooserActivity.this.adapter.notifyDataSetChanged();
                        }

                        if (FileChooserActivity.this.currentFileAdapter != null) {
                            FileChooserActivity.this.currentFileAdapter.setData(FileChooserActivity.this.tourController.getCurrentFolderList());
                            FileChooserActivity.this.currentFileAdapter.notifyDataSetChanged();
                        }

                        listPopupWindow.dismiss();
                    }
                });
                listPopupWindow.show();
            }
        });
    }

    public void clickRightText() {
        if (this.adapter != null && this.adapter.getChooseFilePaths().length < 0) {
            Toast.makeText(this, "请选择文件路径", Toast.LENGTH_LONG).show();
        } else {
            if (this.tourController != null) {
                this.mChoosenFilePath = this.tourController.getCurrentFile().getAbsolutePath();
            }

            if (mFileChooser != null) {
                if (mFileChooser.isChooseSingle()){
                    mFileChooser.finish(this.adapter.getChooseFilePath());
                }else{
                    mFileChooser.finish(this.adapter.getChooseFilePaths());
                }
            }

            this.finish();
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        mFileChooser = null;
    }

    public void onBackPressed() {
        if (!this.tourController.isRootFile()) {
            List<FileInfo> currentList = this.tourController.backToParent();
            this.adapter.setData(currentList);
            this.adapter.notifyDataSetChanged();
            this.currentFileAdapter.setData(this.tourController.getCurrentFolderList());
            this.currentFileAdapter.notifyDataSetChanged();
            int sign = this.tourController.getCurrentFolderList().size();
            Integer firstposition = (Integer)this.firstItemPositionMap.get(sign);
            int first = firstposition == null ? 0 : firstposition;
            Integer lastItemPosition = (Integer)this.lastItemPositionMap.get(sign);
            int last = lastItemPosition == null ? 0 : lastItemPosition;
            int rectification = this.dp2px(15.0F);
            if (this.fileRv.getLayoutManager() != null) {
                ((LinearLayoutManager)this.fileRv.getLayoutManager()).scrollToPositionWithOffset(first, last);
            }
        } else {
            super.onBackPressed();
        }

    }

    public int dp2px(float dpValue) {
        float scale = this.getResources().getDisplayMetrics().density;
        return (int)(dpValue * scale + 0.5F);
    }
}
