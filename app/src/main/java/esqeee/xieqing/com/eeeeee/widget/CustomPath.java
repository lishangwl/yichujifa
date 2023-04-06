package esqeee.xieqing.com.eeeeee.widget;

import android.graphics.Path;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;

import esqeee.xieqing.com.eeeeee.bean.JSONArrayBean;
import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.doAction.core.ScaleMatrics;

public class CustomPath extends Path {
    private ArrayList<PathAction> actions = new ArrayList<CustomPath.PathAction>();

    public static CustomPath readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        return (CustomPath) in.readObject();
    }

    private float downX, downY;
    private float tempX, tempY;

    public void setTempX(float tempX) {
        this.tempX = tempX;
    }

    public void setTempY(float tempY) {
        this.tempY = tempY;
    }

    public float getTempX() {
        return tempX;
    }

    public float getTempY() {
        return tempY;
    }

    public float getDownX() {
        return downX;
    }

    public float getDownY() {
        return downY;
    }

    public CustomPath(float downX, float downY) {
        this.downX = downX;
        this.downY = downY;
        this.tempX = downX;
        this.tempY = downY;
    }

    public CustomPath(int duration) {
        this.duration = duration;
    }

    public long getDuration() {
        return duration;
    }

    private long currtent = System.currentTimeMillis();
    private long duration = System.currentTimeMillis() - currtent;

    public void start() {
        currtent = System.currentTimeMillis();
    }

    public void end() {
        duration = System.currentTimeMillis() - currtent;
    }

    @Override
    public void moveTo(float x, float y) {
        actions.add(new ActionMove(x, y));
        super.moveTo(x, y);
    }

    @Override
    public void lineTo(float x, float y) {
        actions.add(new ActionLine(x, y));
        super.lineTo(x, y);
    }

    public static CustomPath from(JSONBean jsonBean) {
        if (jsonBean == null) {
            return null;
        }
        JSONArrayBean jsonArray = jsonBean.getArray("paths");
        if (jsonArray == null) {
            return null;
        }
        CustomPath path = new CustomPath(jsonBean.getInt("duration"));
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONBean jsonObject = jsonArray.getJson(i);
            if (jsonObject.getString("type").equals("moveTo")) {
                path.moveTo((float) jsonObject.getDouble("x"), (float) jsonObject.getDouble("y"));
            } else if (jsonObject.getString("type").equals("quadTo")) {
                path.quadTo((float) jsonObject.getDouble("x"), (float) jsonObject.getDouble("y"), (float) jsonObject.getDouble("x1"), (float) jsonObject.getDouble("y1"));
            } else if (jsonObject.getString("type").equals("lineTo")) {
                path.lineTo((float) jsonObject.getDouble("x"), (float) jsonObject.getDouble("y"));
            }
        }
        return path;
    }


    public static CustomPath from(JSONBean jsonBean, ScaleMatrics scaleMatrics) {
        if (jsonBean == null) {
            return null;
        }
        if (scaleMatrics == null) {
            return from(jsonBean);
        }
        JSONArrayBean jsonArray = jsonBean.getArray("paths");
        if (jsonArray == null) {
            return null;
        }
        CustomPath path = new CustomPath(jsonBean.getInt("duration"));
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONBean jsonObject = jsonArray.getJson(i);
            if (jsonObject.getString("type").equals("moveTo")) {
                path.moveTo(scaleMatrics.scaleX((float) jsonObject.getDouble("x")), scaleMatrics.scaleY((float) jsonObject.getDouble("y")));
            } else if (jsonObject.getString("type").equals("quadTo")) {
                path.quadTo(scaleMatrics.scaleX((float) jsonObject.getDouble("x"))
                        , scaleMatrics.scaleY((float) jsonObject.getDouble("y"))
                        , scaleMatrics.scaleX((float) jsonObject.getDouble("x1"))
                        , scaleMatrics.scaleY((float) jsonObject.getDouble("y1")));
            } else if (jsonObject.getString("type").equals("lineTo")) {
                path.lineTo(scaleMatrics.scaleX((float) jsonObject.getDouble("x")), scaleMatrics.scaleY((float) jsonObject.getDouble("y")));
            }
        }
        return path;
    }

    public JSONBean toBean() {
        JSONArrayBean jsonArray = new JSONArrayBean();
        for (int i = 0; i < actions.size(); i++) {
            PathAction pathAction = actions.get(i);
            JSONBean jsonObject = new JSONBean();
            if (pathAction instanceof ActionMove) {
                jsonObject.put("type", "moveTo");
                jsonObject.put("x", ((ActionMove) pathAction).getX());
                jsonObject.put("y", ((ActionMove) pathAction).getY());
                jsonArray.put(jsonObject);
            } else if (pathAction instanceof ActionQuad) {
                jsonObject.put("type", "quadTo");
                jsonObject.put("x", ((ActionQuad) pathAction).getX());
                jsonObject.put("y", ((ActionQuad) pathAction).getY());
                jsonObject.put("x1", ((ActionQuad) pathAction).getTox());
                jsonObject.put("y1", ((ActionQuad) pathAction).getToy());
                jsonArray.put(jsonObject);
            } else if (pathAction instanceof ActionLine) {
                jsonObject.put("type", "lineTo");
                jsonObject.put("x", ((ActionLine) pathAction).getX());
                jsonObject.put("y", ((ActionLine) pathAction).getY());
                jsonArray.put(jsonObject);
            }
        }
        JSONBean jsonObject = new JSONBean();
        jsonObject.put("paths", jsonArray).put("duration", duration);
        return jsonObject;
    }

    @Override
    public void quadTo(float dx1, float dy1, float dx2, float dy2) {
        actions.add(new ActionQuad(dx1, dy1, dx2, dy2));
        super.quadTo(dx1, dy1, dx2, dy2);
    }

    @Override
    public void reset() {
        super.reset();
        actions.clear();
    }

    public class PathAction {
    }

    private class ActionQuad extends PathAction {
        private float x, y, tox, toy;

        public ActionQuad(float x, float y, float tox, float toy) {
            this.x = x;
            this.y = y;
            this.tox = tox;
            this.toy = toy;
        }

        public float getY() {
            return y;
        }

        public float getTox() {
            return tox;
        }

        public float getToy() {
            return toy;
        }

        public float getX() {
            return x;
        }
    }

    private class ActionMove extends PathAction implements Serializable {
        private float x, y;

        public ActionMove(float x, float y) {
            this.x = x;
            this.y = y;
        }

        public float getY() {
            return y;
        }

        public float getX() {
            return x;
        }
    }

    private class ActionLine extends PathAction implements Serializable {
        private float x, y;

        public ActionLine(float x, float y) {
            this.x = x;
            this.y = y;
        }

        public float getY() {
            return y;
        }

        public float getX() {
            return x;
        }
    }
}