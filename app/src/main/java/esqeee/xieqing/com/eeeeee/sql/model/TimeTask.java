package esqeee.xieqing.com.eeeeee.sql.model;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;

import esqeee.xieqing.com.eeeeee.sql.ActionsDatabase;

@Table(name = "TimeTask", database = ActionsDatabase.class)
public class TimeTask extends AutoDo {
    @PrimaryKey(autoincrement = true)
    @Column
    public int index;

    @Column
    public String path;

    @Column
    public int hour;

    @Column
    public int min;

    @Override
    public String toString() {
        return "[" + hour + ":" + min + "]" + path;
    }
}
