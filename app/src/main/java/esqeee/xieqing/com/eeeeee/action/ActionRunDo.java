package esqeee.xieqing.com.eeeeee.action;

import com.yicu.yichujifa.GlobalContext;

import java.util.HashMap;

import esqeee.xieqing.com.eeeeee.bean.JSONBean;
import esqeee.xieqing.com.eeeeee.doAction.Base;
import esqeee.xieqing.com.eeeeee.doAction.api.App;
import esqeee.xieqing.com.eeeeee.doAction.api.Array;
import esqeee.xieqing.com.eeeeee.doAction.api.Assgin;
import esqeee.xieqing.com.eeeeee.doAction.api.Auto;
import esqeee.xieqing.com.eeeeee.doAction.api.Bule;
import esqeee.xieqing.com.eeeeee.doAction.api.Click;
import esqeee.xieqing.com.eeeeee.doAction.api.ClickText;
import esqeee.xieqing.com.eeeeee.doAction.api.Color;
import esqeee.xieqing.com.eeeeee.doAction.api.Condition;
import esqeee.xieqing.com.eeeeee.doAction.api.Dialog;
import esqeee.xieqing.com.eeeeee.doAction.api.Encrypt;
import esqeee.xieqing.com.eeeeee.doAction.api.Fast;
import esqeee.xieqing.com.eeeeee.doAction.api.File;
import esqeee.xieqing.com.eeeeee.doAction.api.FlashClose;
import esqeee.xieqing.com.eeeeee.doAction.api.FlashOpen;
import esqeee.xieqing.com.eeeeee.doAction.api.For;
import esqeee.xieqing.com.eeeeee.doAction.api.Genster;
import esqeee.xieqing.com.eeeeee.doAction.api.Http;
import esqeee.xieqing.com.eeeeee.doAction.api.If;
import esqeee.xieqing.com.eeeeee.doAction.api.Image;
import esqeee.xieqing.com.eeeeee.doAction.api.InputText;
import esqeee.xieqing.com.eeeeee.doAction.api.Key;
import esqeee.xieqing.com.eeeeee.doAction.api.LongClick;
import esqeee.xieqing.com.eeeeee.doAction.api.Node;
import esqeee.xieqing.com.eeeeee.doAction.api.Paste;
import esqeee.xieqing.com.eeeeee.doAction.api.RandomSleep;
import esqeee.xieqing.com.eeeeee.doAction.api.SL;
import esqeee.xieqing.com.eeeeee.doAction.api.STRING;
import esqeee.xieqing.com.eeeeee.doAction.api.Setting;
import esqeee.xieqing.com.eeeeee.doAction.api.Sleep;
import esqeee.xieqing.com.eeeeee.doAction.api.Swip;
import esqeee.xieqing.com.eeeeee.doAction.api.SwipLine;
import esqeee.xieqing.com.eeeeee.doAction.api.System;
import esqeee.xieqing.com.eeeeee.doAction.api.Toast;
import esqeee.xieqing.com.eeeeee.doAction.api.UI;
import esqeee.xieqing.com.eeeeee.doAction.api.While;
import esqeee.xieqing.com.eeeeee.doAction.api.Wifi;
import esqeee.xieqing.com.eeeeee.doAction.core.Media;
import esqeee.xieqing.com.eeeeee.doAction.core.ScaleMatrics;
import esqeee.xieqing.com.eeeeee.doAction.core.Text2Speech;
import esqeee.xieqing.com.eeeeee.library.RuntimeLog;

public class ActionRunDo {
    private HashMap<Object, Base> actionTable = new HashMap<>();{
        actionTable.put(-1,new Sleep());
        actionTable.put(0,new FlashOpen());
        actionTable.put(1,new FlashClose());
        actionTable.put(2,new Click());
        actionTable.put(3,new ClickText());
        actionTable.put(41,new ClickText());
        actionTable.put(49,new ClickText());
        actionTable.put(62,new ClickText());

        actionTable.put(30,new Image());
        actionTable.put(50,new Image());
        actionTable.put(60,new Image());
        actionTable.put(61,new Image());

        actionTable.put(58,new Color());
        actionTable.put(59,new Color());
        actionTable.put(67,new Color());
        actionTable.put(68,new Color());


        actionTable.put(4,new Key());
        actionTable.put(5,new Key());
        actionTable.put(6,new Key());
        actionTable.put(11,new Key());
        actionTable.put(14,new Key());
        actionTable.put(15,new InputText());

        actionTable.put(12,new App());
        actionTable.put(13,new SwipLine());
        actionTable.put(42,new Genster());

        actionTable.put(7,new Swip());
        actionTable.put(8,new Swip());
        actionTable.put(9,new Swip());
        actionTable.put(10,new Swip());


        actionTable.put(22,new Setting());
        actionTable.put(23,new Setting());
        actionTable.put(24,new Setting());
        actionTable.put(25,new Setting());
        actionTable.put(26,new Setting());
        actionTable.put(27,new Setting());
        actionTable.put(28,new Setting());
        actionTable.put(29,new Setting());
        actionTable.put(31,new Setting());
        actionTable.put(34,new Setting());
        actionTable.put(35,new Setting());
        actionTable.put(36,new Setting());
        actionTable.put(37,new Setting());
        actionTable.put(57,new Setting());
        actionTable.put(64,new Setting());
        actionTable.put(65,new Setting());
        actionTable.put(66,new Setting());
        actionTable.put(69,new Setting());

        actionTable.put(38,new Fast());
        actionTable.put(39,new Fast());
        actionTable.put(40,new Fast());

        actionTable.put(46,new Paste());
        actionTable.put(53,new For());
        actionTable.put(43,new If());

        actionTable.put(51,new RandomSleep());
        actionTable.put(52,new While());
        actionTable.put(55,new Toast());

        actionTable.put(16,new LongClick());
        actionTable.put(17,new Auto());

        actionTable.put(18,new Wifi());
        actionTable.put(19,new Wifi());

        actionTable.put(20,new Bule());
        actionTable.put(21,new Bule());

        actionTable.put(54,new Condition());
        actionTable.put(63,new Condition());
        actionTable.put(44,new Condition());
        actionTable.put(45,new Condition());
        actionTable.put(47,new Condition());
        actionTable.put(48,new Condition());
        actionTable.put(72,new Condition());

        actionTable.put(70,new Http());
        actionTable.put(71,new Assgin());
        actionTable.put(73,new STRING());
        actionTable.put(74,new File());
        actionTable.put(75,new Encrypt());
        actionTable.put(77,new SL());
        actionTable.put(78,new Array());
        actionTable.put(79,new Node());
        actionTable.put(80,new Dialog());
        actionTable.put(81,new UI());
        actionTable.put(83,new System());
    }

    public boolean post(int type,JSONBean jsonBean, ActionRun self, ActionRun.Block block){
        if (type == 56){
            return true;
        }
        //RuntimeLog.log("[block:"+block.blockName+"]"+block.getVariables().length());
        if (!actionTable.containsKey(type)){
            RuntimeLog.e("error, action = "+type+" not define.");
            return false;
        }
        JSONBean param = jsonBean.getJson("param");
        String desc = param.getString("desc");
        if (!desc.equals("")){
            RuntimeLog.i("ps:"+desc);
        }
        return actionTable.get(type).setJsonBean(jsonBean).setRun(self).setBlock(block).post(param.put("actionType",type));
    }



    private Media media;
    private Text2Speech speech = new Text2Speech(GlobalContext.getContext());

    public Text2Speech getSpeech() {
        return speech;
    }

    public Media getMedia() {
        if (media == null){
            media = new Media();
        }
        return media;
    }

    public ScaleMatrics getScaleMatrics(int width,int height) {
        return new ScaleMatrics(width,height);
    }

    public void reslese(){
        if (media != null){
            media.reslese();
        }

        if (speech != null){
            speech.reslese();
        }
    }
}
