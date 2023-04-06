package esqeee.xieqing.com.eeeeee.ui;

import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;

import com.xieqing.codeutils.util.FileIOUtils;
import com.xieqing.codeutils.util.ToastUtils;

import java.io.IOException;

import esqeee.xieqing.com.eeeeee.AddActivity;
import esqeee.xieqing.com.eeeeee.action.ActionHelper;

public class ImportActivity extends BaseActivity {
    @Override
    public int getContentLayout() {
        return 0;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getData()!=null){
            Uri uri = getIntent().getData();
            String name = uri.getPath();
            name = ActionHelper.workSpaceDir.getPath()+"/"+name.substring(name.lastIndexOf("/"));
            String finalName = name;
            new Thread(()->{
                try {
                    FileIOUtils.writeFileFromIS(finalName, getContentResolver().openInputStream(uri));
                    ToastUtils.showLong("已导入:"+finalName);
                    AddActivity.open(getApplicationContext(),finalName);
                    finish();
                } catch (IOException e) {
                    e.printStackTrace();
                    ToastUtils.showLong("已导入："+e);
                }catch (SecurityException e){
                    ToastUtils.showLong("导入失败："+e);
                }
            }).start();
        }
    }
}
