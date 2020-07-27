package luoluna.sweven.album.app;

import android.content.Context;
import android.content.SharedPreferences;

import com.sweven.util.PreferenceUtil;

import org.greenrobot.greendao.query.WhereCondition;

import java.io.File;
import java.util.List;

import luoluna.sweven.album.entity.local.Album;
import luoluna.sweven.album.entity.local.AlbumDao;
import luoluna.sweven.album.entity.local.Image;
import luoluna.sweven.album.manager.FileManager;
import luoluna.sweven.album.repository.local.DaoManager;

/**
 * app项目的辅助类
 */
public class Helper {

    // 获取preference对象
    public static PreferenceUtil preference(Context context) {
        return new PreferenceUtil(context, App.preference);
    }

    // 获取editor对象
    public static SharedPreferences.Editor editor(Context context) {
        return preference(context).getEditor();
    }

    public static Album getAlbumByAid(long id) {
        List<Album> albums = DaoManager
                .getSession()
                .getAlbumDao()
                .queryBuilder()
                .where(new WhereCondition.PropertyCondition(AlbumDao.Properties.Id, "=" + id))
                .list();
        if (albums.size() == 0) return null;
        return albums.get(0);
    }

    public static long addAlbum(Album album) {
        return DaoManager.getSession().getAlbumDao().insertWithoutSettingPk(album) + 1;
    }

    public static boolean hasAlbum(Album album) {
        List<Album> albums = DaoManager
                .getSession()
                .getAlbumDao()
                .queryBuilder()
                .where(new WhereCondition.PropertyCondition(AlbumDao.Properties.Id, "=" + album.getId()))
                .list();
        return albums.size() > 0;
    }

    public static long updateAlbum(Album album) {
        DaoManager.getSession().getAlbumDao().update(album);
        return 1;
    }

    public static boolean delAlbum(Album album) {
        DaoManager.getSession().getAlbumDao().deleteByKey(album.getId());
        return !hasAlbum(album);
    }

    public static List<Album> queryAlbums() {
        List<Album> list = DaoManager.getSession().getAlbumDao().queryBuilder().list();
        for (Album album : list) {
            album.__setDaoSession(DaoManager.getSession());
            List<Image> desktops = album.getDesktops();
            List<Image> images = FileManager.sortImg(new File(album.getPath()));
            desktops.addAll(images);
            album.setDesktops(desktops);
        }
        return list;
    }
}
