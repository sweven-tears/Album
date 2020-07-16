package luoluna.sweven.album.repository.local;

import android.database.sqlite.SQLiteDatabase;

import org.greenrobot.greendao.database.Database;

import luoluna.sweven.album.entity.local.DaoMaster;
import luoluna.sweven.album.entity.local.DaoSession;

/**
 * <p>Create by Sweven on 2020/7/14 -- 16:43</p>
 * Email: sweventears@163.com
 */
public class DaoManager {
    private static DaoManager instance;
    private static DaoSession session;

    public static DaoManager getInstance() {
        if (instance == null) {
            synchronized (DaoManager.class) {
                if (instance == null) {
                    instance = new DaoManager();
                }
            }
        }
        return instance;
    }

    public static DaoSession getSession() {
        if (session == null) {
            synchronized (DaoMaster.class) {
                Database database = new AlbumHelper().getWritableDb();
                DaoMaster daoMaster = new DaoMaster(database);
                session = daoMaster.newSession();
            }
        }
        return session;
    }
}
