package esqeee.xieqing.com.eeeeee.action;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;

import com.xieqing.codeutils.util.FileIOUtils;
import com.xieqing.codeutils.util.FileUtils;
import com.xieqing.codeutils.util.ToastUtils;
import com.xieqing.codeutils.util.Utils;

import java.io.File;

import esqeee.xieqing.com.eeeeee.R;

public class Action {
    private long time;
    private int count;
    private int log = 0;
    private int repeat;
    private int status;
    private int type;
    private String data = "";
    private String icon = "";
    private String path;
    private File file;
    private Drawable drawable;
    private boolean isEncrypt = false;

    public Drawable getDrawable(){
        return drawable;
    }

    public String getIcon() {
        return icon;
    }

    public Action(){}

    public void setEncrypt(boolean encrypt) {
        isEncrypt = encrypt;
    }

    public boolean isEncrypt() {
        return isEncrypt;
    }

    public static Action newAction(){
        Action action = new Action();
        action.setTime(System.currentTimeMillis());
        action.setRepeat(1);
        action.setCount(0);
        action.setStatus(0);
        action.setType(0);
        action.setLog(0);
        action.setEncrypt(false);
        return action;
    }

    public void setIcon(String icon) {
        this.icon = icon;
        if (icon == null){
            this.icon = "";
        }

        if (!getIcon().equals("")){
            drawable =  new BitmapDrawable(BitmapFactory.decodeFile(getIcon()));
            return;
        }
        switch (getType()){
            case 0:
                drawable = Utils.getApp().getResources().getDrawable(R.drawable.action_1);
                break;
            case 1:
                drawable = Utils.getApp().getResources().getDrawable(R.drawable.action_2);
                break;
            case 3:
            case 2:
                drawable = Utils.getApp().getResources().getDrawable(R.drawable.action_3);
                break;
        }
    }

    public void setData(String data) {
        this.data = data;
        if (this.data == null){
            this.data = "";
        }
    }

    public String title;
    public String getData() {
        return data;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setRepeat(int repeat) {
        this.repeat = repeat;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getType() {
        return type;
    }

    public String getTitle() {
        //return (TextUtils.isEmpty(title))?(file == null?"":FileUtils.getFileNameNoExtension(file)):title;
        return file == null?"":FileUtils.getFileNameNoExtension(file);
    }

    public int getCount() {
        return count;
    }

    public int getStatus() {
        return status;
    }

    public int getRepeat() {
        return repeat;
    }

    public long getTime() {
        return time;
    }

    public void setPath(String path) {
        this.path = path;
        this.file = new File(path);
    }

    public File getFile() {
        return file;
    }

    public String getPath() {
        return path;
    }

    public void delete() {
        FileUtils.delete(path);
    }

    public String save(){
        String ycfString = ActionHelper.actionToString(this,false,isEncrypt);
        FileIOUtils.writeFileFromString(path,ycfString);
        return path;
    }

    public void setLog(int log) {
        this.log = log;
    }

    public int getLog() {
        return log;
    }

    public boolean logAble() {
        return log == 0;
    }
}
