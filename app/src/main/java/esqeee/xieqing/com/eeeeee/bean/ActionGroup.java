package esqeee.xieqing.com.eeeeee.bean;

import java.util.ArrayList;
import java.util.List;

import esqeee.xieqing.com.eeeeee.action.Action;

public class ActionGroup {
    public static ActionGroup DEFUALT = new ActionGroup();
    private String groupId = "";
    private String name = "我的指令";

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getName() {
        return name;
    }

    private List<Action> actions = new ArrayList<>();
    public ActionGroup(){

    }
    public ActionGroup(String groupId,String name){
        this.groupId = groupId;
        this.name = name;
    }

    public void addAction(Action action){
        actions.add(action);
    }

    public void delete(Action action){
        actions.remove(action);
    }

    public int size(){
        return actions.size();
    }

    public Action get(int i){
        return actions.get(i);
    }
}
