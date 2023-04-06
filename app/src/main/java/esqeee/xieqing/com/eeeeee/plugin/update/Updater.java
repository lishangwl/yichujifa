package esqeee.xieqing.com.eeeeee.plugin.update;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Build;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import com.xieqing.codeutils.util.AppUtils;
import com.xieqing.codeutils.util.FileIOUtils;
import com.xieqing.codeutils.util.FileUtils;
import com.xieqing.codeutils.util.SPUtils;
import com.xieqing.codeutils.util.SQLUtils;
import com.xieqing.codeutils.util.StringUtils;
import com.xieqing.codeutils.util.ToastUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import esqeee.xieqing.com.eeeeee.BuildConfig;
import esqeee.xieqing.com.eeeeee.action.Action;
import esqeee.xieqing.com.eeeeee.action.ActionHelper;
import esqeee.xieqing.com.eeeeee.bean.ActionGroup;
import esqeee.xieqing.com.eeeeee.bean.JSONArrayBean;
import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.doAction.DoActionBean;
import esqeee.xieqing.com.eeeeee.helper.SettingsPreference;
import esqeee.xieqing.com.eeeeee.library.RuntimeLog;
import esqeee.xieqing.com.eeeeee.ui.BaseActivity;

import static android.content.Context.MODE_PRIVATE;

public class Updater {



    //检查目录

    public static SQLiteDatabase sqLiteDatabase;
    public static File dbPath;
    public static void update(BaseActivity context){
        SPUtils.getInstance().put("isUpdate2.2.8",false);
        RuntimeLog.log("update");
        FileUtils.createOrExistsDir(ActionHelper.projectDir
                ,ActionHelper.workSpaceDir
                ,ActionHelper.workSpaceImageDir
                ,ActionHelper.saveDir
                ,ActionHelper.shareDir);


        FileUtils.copyDir(new File(Environment.getExternalStorageDirectory().getPath()+"/YiChuJiFa"),ActionHelper.workSpaceImageDir);


        dbPath = new File(context.getDir("databases", MODE_PRIVATE), "actions.db");

        if (dbPath.exists()){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    context.showProgress("正在从旧版升级新版","正在更新，请勿退出!");
                    sqLiteDatabase = context.openOrCreateDatabase(dbPath.getAbsolutePath(), 0, null);

                    try {
                        //导出所有的action
                        outPutAllAction();
                    }catch (SQLiteException e){
                        RuntimeLog.log(e);
                    }

                    //把目录都移动过去
                    FileUtils.delete(Environment.getExternalStorageDirectory().getPath()+"/YiChuJiFa");
                    FileUtils.delete(Environment.getExternalStorageDirectory().getPath()+"/YiChuJiFaLog");
                    FileUtils.moveDir(Environment.getExternalStorageDirectory().getPath()+"/YiChuJiFaSave",ActionHelper.saveDir.getAbsolutePath());
                    FileUtils.moveDir(Environment.getExternalStorageDirectory().getPath()+"/YiChuJiFaShare",ActionHelper.shareDir.getAbsolutePath());


                    dbPath.delete();

                    //把摇动触发里的自动化都改好
                    setSensor();

                    //把悬浮窗的自动化改好
                    setFloat();

                    SPUtils.getInstance().put("isUpdate2.2.8",false);
                    context.disProgress();
                    ToastUtils.showShort("已经更新完毕");
                    RuntimeLog.log("已经更新完毕");
                }
            }).start();
        }
    }
    private  static void setFloat() {
        List<JSONBean> list = SettingsPreference.getFloatMenuItems();
        for (JSONBean bean : list){
            JSONBean param = bean.getJson("param");
            int type = bean.getInt("actionType");
            if (type == DoActionBean.EXCE_ACTION.getActionType()){
                Action action =  ActionHelper.from(param.getString("actionId"));
                if (action != null){
                    if (hasOut.containsKey(param.getString("actionId"))){
                        param.put("actionId",hasOut.get(param.getString("actionId")));
                    }
                }
            }
        }
        SettingsPreference.saveFloatMenuItems(list);
    }
    private static void setSensor() {
        setSensor("x");
        setSensor("y");
        setSensor("z");
    }
    private static void setSensor(String key) {
        JSONBean bean = new JSONBean(SPUtils.getInstance().getString(key+"_wd"));
        int type = bean.getInt("actionType",-1);
        JSONBean param = bean.getJson("param");
        if (type == DoActionBean.EXCE_ACTION.getActionType()){
            String id = param.getString("actionId");
            if (hasOut.containsKey(id)){
                param.put("actionId",hasOut.get(id));
            }
            SPUtils.getInstance().put(key+"_wd",bean.toString());
        }
    }
    private static void outPutAllAction() {
        //全部导出
        //获取所有的指令组,然后创建目录
        List<ActionGroup> groups = new ArrayList<>();
        Cursor cursor2 = sqLiteDatabase.rawQuery("select * from ActionGroups", null);
        while (cursor2.moveToNext()) {
            String groupId = cursor2.getString(cursor2.getColumnIndex("groupId"));
            String groupName = cursor2.getString(cursor2.getColumnIndex("title"));

            File directory = new File(ActionHelper.workSpaceDir,groupName);
            if (!directory.isDirectory()){
                directory.mkdir();
            }

            Log.d("Updater","out ->"+groupName);
            Cursor cursor = sqLiteDatabase.rawQuery("select * from Actions where groupId = '"+groupId+"' order by time desc",null);
            Log.d("Updater","out ->"+cursor2.getCount());
            while (cursor.moveToNext()) {
                int count = Integer.parseInt(cursor.getString(cursor.getColumnIndex("count")));
                int repeat = Integer.parseInt(cursor.getString(cursor.getColumnIndex("repeat")));
                int status=Integer.parseInt(cursor.getString(cursor.getColumnIndex("status")));
                int type = Integer.parseInt(cursor.getString(cursor.getColumnIndex("type")));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String icon = cursor.getString(cursor.getColumnIndex("icon"));
                String data=URLDecoder.decode(cursor.getString(cursor.getColumnIndex("data")));
                long time=Long.parseLong(cursor.getString(cursor.getColumnIndex("time")));
                String id = cursor.getString(cursor.getColumnIndex("actionId"));
                String filePath = outPut(id,groupName,count,repeat,status,type,time,title,icon,data);
            }
            cursor.close();
        }
        cursor2.close();
    }

    private static String getGroupName(String id){
        String name = "我的指令";
        Cursor cursor = sqLiteDatabase.rawQuery("select * from ActionGroups where groupId = '"+id+"'", null);
        if (cursor.moveToNext()){
            name = cursor.getString(cursor.getColumnIndex("title"));
        }
        cursor.close();
        return name;
    }
    private static HashMap<String,String> hasOut = new HashMap<>();
    private static String outPut(String actionId) {
        if (hasOut.containsKey(actionId)){
            return hasOut.get(actionId);
        }
        Log.d("Updater","out ->"+actionId);
        String path = "";
        Cursor cursor = sqLiteDatabase.rawQuery("select * from Actions where actionId = '"+actionId+"'",null);
        if (cursor.moveToNext()){
            int count = Integer.parseInt(cursor.getString(cursor.getColumnIndex("count")));
            int repeat = Integer.parseInt(cursor.getString(cursor.getColumnIndex("repeat")));
            int status=Integer.parseInt(cursor.getString(cursor.getColumnIndex("status")));
            int type = Integer.parseInt(cursor.getString(cursor.getColumnIndex("type")));
            String title = cursor.getString(cursor.getColumnIndex("title"));
            String icon = cursor.getString(cursor.getColumnIndex("icon"));
            String data=URLDecoder.decode(cursor.getString(cursor.getColumnIndex("data")));
            String groupId = cursor.getString(cursor.getColumnIndex("groupId"));
            long time=Long.parseLong(cursor.getString(cursor.getColumnIndex("time")));
            path = outPut(actionId,getGroupName(groupId),count,repeat,status,type,time,title,icon,data);
        }
        cursor.close();
        return path;
    }
    private static String outPut(String id,String groupName,int count, int repeat, int status, int type, long time, String title, String icon, String data) {
        if (hasOut.containsKey(id)){
            return hasOut.get(id);
        }
        File filePath = new File(new File(ActionHelper.workSpaceDir,groupName),title+".ycf");
        hasOut.put(id,filePath.getAbsolutePath());
        Log.d("Updater","out ->"+groupName+"->"+title);
        File directory = new File(ActionHelper.workSpaceDir,groupName);
        if (!directory.isDirectory()){
            directory.mkdir();
        }

        String[] useActions = StringUtils.getSubStringArray(data,"\"actionId\":\"","\"");
        Log.d("Updater","use"+data);
        Log.d("Updater","use"+Arrays.toString(useActions));
        for (String actionId : useActions){
            String outPath = outPut(actionId);
            data = data.replace(actionId,outPath);
        }


        JSONBean dataBean = new JSONBean(data);

        List<String> images = new ArrayList<>();
        outputAllImages(dataBean.getArray("actions"));
        data = dataBean.toString();

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<version>").append(BuildConfig.VERSION_NAME).append("</version>\n");
        stringBuilder.append("<title>").append(title).append("</title>\n");
        stringBuilder.append("<repeat>").append(repeat).append("</repeat>\n");
        stringBuilder.append("<count>").append(count).append("</count>\n");
        stringBuilder.append("<data>").append(URLEncoder.encode(data)).append("</data>\n");
        stringBuilder.append("<status>").append(status).append("</status>\n");
        stringBuilder.append("<type>").append(type).append("</type>\n");
        stringBuilder.append("<icon>").append(icon).append("</icon>\n");



        FileIOUtils.writeFileFromString(filePath,stringBuilder.toString());
        return filePath.getAbsolutePath();
    }

    public static void outputAllImages(JSONArrayBean array){
        for (int i = 0;i<array.length();i++){
            JSONBean jsonObject = array.getJson(i);
            int type = jsonObject.getInt("actionType");
            switch (type){
                case 47:
                case 48:
                case 60:
                case 61:
                case 50:
                case 30:
                    String image = jsonObject.getJson("param").getString("fileName");
                    File file = new File(image);
                    if (!file.exists()){
                        continue;
                    }
                    File move = new File(ActionHelper.workSpaceImageDir,file.getName());
                    FileUtils.copyFile(file,move);
                    jsonObject.getJson("param").put("fileName",move.getAbsolutePath());
                    break;
                case 43:
                    switch (jsonObject.getJson("condition").getInt("actionType")){
                        case 47:
                        case 48:
                            String image2 = jsonObject.getJson("condition").getJson("param").getString("fileName");
                            File file2 = new File(image2);
                            if (!file2.exists()){
                                continue;
                            }
                            File move2 = new File(ActionHelper.workSpaceImageDir,file2.getName());
                            FileUtils.copyFile(file2,move2);
                            jsonObject.getJson("condition").getJson("param").put("fileName",move2.getAbsolutePath());
                            break;
                    }
                    if (jsonObject.getJson("trueDo").has("actions")){
                        outputAllImages(jsonObject.getJson("trueDo").getArray("actions"));
                    }
                    if (jsonObject.getJson("falseDo").has("actions")){
                        outputAllImages(jsonObject.getJson("falseDo").getArray("actions"));
                    }
                    break;
                case 52:
                    if (jsonObject.getJson("condition").getInt("actionType") == 47 || jsonObject.getJson("condition").getInt("actionType") == 48){
                        String image2 = jsonObject.getJson("condition").getJson("param").getString("fileName");
                        File file2 = new File(image2);
                        if (!file2.exists()){
                            continue;
                        }
                        File move2 = new File(ActionHelper.workSpaceImageDir,file2.getName());
                        FileUtils.copyFile(file2,move2);
                        jsonObject.getJson("condition").getJson("param").put("fileName",move2.getAbsolutePath());
                    }
                    if (jsonObject.getJson("trueDo").has("actions")){
                        outputAllImages(jsonObject.getJson("trueDo").getArray("actions"));
                    }
                    break;
                case 53:
                    if (jsonObject.getJson("trueDo").has("actions")){
                        outputAllImages(jsonObject.getJson("trueDo").getArray("actions"));
                    }
                    break;
            }
        }
    }
}
