package esqeee.xieqing.com.eeeeee.sql.model;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import esqeee.xieqing.com.eeeeee.sql.ActionsDatabase;

@Table(name = "Message",database = ActionsDatabase.class)
public class Message extends BaseModel {
    @PrimaryKey(autoincrement = true)
    @Column
    public int index;

    @Column
    public long messageId;

    @Column
    public int fromUid;

    @Column
    public int toUid;


    @Column
    public String fromUserNick;


    @Column
    public String toUserNick;

    @Column
    public String title;

    @Column
    public String message;

    @Column
    public int target;

    @Column
    public long time;

}
