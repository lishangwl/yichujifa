package esqeee.xieqing.com.eeeeee.manager;

import android.content.Context;

import com.xieqing.codeutils.util.FileIOUtils;
import com.xieqing.codeutils.util.ResourceUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import esqeee.xieqing.com.eeeeee.action.Action;
import esqeee.xieqing.com.eeeeee.action.ActionHelper;
import esqeee.xieqing.com.eeeeee.bean.ActionGroup;

public class SimpleManager {
    private static SimpleManager manager;
    private Context context;
    public SimpleManager(Context context) {
        this.context = context;
    }

    public static SimpleManager getManager(Context context) {
        if (manager == null){
            manager = new SimpleManager(context);
        }
        return manager;
    }

    public List<ActionGroup> getGroups(){
        String string = ResourceUtils.readAssets2String("simple/Sample.json","UTF-8");
        List<ActionGroup> groupList =new ArrayList<>();
        try {
            JSONArray groups = new JSONArray(string);
            for (int i = 0 ;i < groups.length();i++){
                JSONObject jsonObject = groups.getJSONObject(i);
                ActionGroup group = new ActionGroup();
                group.setName(jsonObject.getString("name"));

                JSONArray actions = jsonObject.getJSONArray("samples");
                for (int j = 0;j<actions.length();j++){
                    String action = ResourceUtils.readAssets2String("simple/"+group.getName()+"/"+actions.getString(j),"UTF-8");

                    Action action1 = ActionHelper.stringToAction(action,true);
                    action1.setPath(ActionHelper.workSpaceDir + "/"+actions.getString(j));
                    group.addAction(action1);
                }
                groupList.add(group);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return groupList;
    }
}
