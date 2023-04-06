package esqeee.xieqing.com.eeeeee.sql.model;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;

import esqeee.xieqing.com.eeeeee.sql.ActionsDatabase;

@Table(name = "AppActivity",database = ActionsDatabase.class)
public class App extends AutoDo {
    @PrimaryKey(autoincrement = true)
    @Column
    public int index;

    @Column
    public String path;

    @Column
    public String packageName;

    @Column
    public String activityName;
}
