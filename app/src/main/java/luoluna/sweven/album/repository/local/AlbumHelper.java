package luoluna.sweven.album.repository.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.greenrobot.greendao.database.Database;

import luoluna.sweven.album.app.App;
import luoluna.sweven.album.entity.local.DaoMaster;

/**
 * <p>Create by Sweven on 2020/7/15 -- 15:03</p>
 * Email: sweventears@163.com
 */
public class AlbumHelper extends DaoMaster.OpenHelper {
    public AlbumHelper() {
        super(App.application, App.database);
    }

    public AlbumHelper(Context context, String name) {
        super(context, name);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
    }
}
