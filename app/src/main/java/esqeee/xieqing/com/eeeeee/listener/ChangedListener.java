package esqeee.xieqing.com.eeeeee.listener;

import android.widget.CompoundButton;

import esqeee.xieqing.com.eeeeee.action.Action;

public class ChangedListener implements CompoundButton.OnCheckedChangeListener{
            private Action action;
            public ChangedListener(Action action){
                this.action=action;
            }
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                //action.changeStatus((b?0:1));
            }
        }