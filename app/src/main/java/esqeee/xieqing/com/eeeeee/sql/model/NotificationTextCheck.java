package esqeee.xieqing.com.eeeeee.sql.model;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;

import esqeee.xieqing.com.eeeeee.sql.ActionsDatabase;

@Table(name = "NotificationTextCheck",database = ActionsDatabase.class)
public class NotificationTextCheck extends AutoDo {
    @PrimaryKey(autoincrement = true)
    @Column
    public int index;

    @Column
    public int notiydo;

    @Column
    public String path;

    @Column
    public String keys;
}
