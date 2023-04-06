package esqeee.xieqing.com.eeeeee.sql.model;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.Database;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import esqeee.xieqing.com.eeeeee.sql.ActionsDatabase;

@Table(database = ActionsDatabase.class,name = "Notification")
public class NotificationItem extends BaseModel {
    @PrimaryKey(autoincrement = true)
    @Column
    public int index;

    @Column
    public String actionPath;

    @Override
    public String toString() {
        return "[id="+index+",path="+actionPath+"]";
    }
}
