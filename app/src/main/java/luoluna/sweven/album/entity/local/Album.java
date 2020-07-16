package luoluna.sweven.album.entity.local;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.Transient;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import luoluna.sweven.album.manager.FileManager;

/**
 * Created by Sweven on 2019/9/10--17:01.
 * Email: sweventears@foxmail.com
 */
@Entity
public class Album {

    @Id
    private long id;
    private String name;
    private String path;
    @ToMany(referencedJoinProperty = "aid")
    private List<Image> desktops;
    private long count;
    private String cover;
    private String remark;

    @Transient
    private boolean selected;
    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /**
     * Used for active entity operations.
     */
    @Generated(hash = 172302968)
    private transient AlbumDao myDao;

    public Album() {
    }

    public Album(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Album(int id, String name, String cover) {
        this.id = id;
        this.name = name;
        this.cover = cover;
    }

    @Generated(hash = 948938020)
    public Album(long id, String name, String path, long count, String cover,
                 String remark) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.count = count;
        this.cover = cover;
        this.remark = remark;
    }

    /**
     * 创建相册
     *
     * @param album 默认一定有id和name，否则会无法同步数据库
     * @return 完整的album
     */
    public static Album create(Album album, String path) {
        album.setPath(path);
        List<Image> desktops = FileManager.sort(new File(path));
        if (desktops.size() > 0) {
            album.setCover(desktops.get(0).getUri());
            if (album.getDesktops() == null) {
                album.setDesktops(new ArrayList<>());
            }
            album.desktops.addAll(desktops);
        }
        album.setCount(desktops.size());
        return album;
    }

    public String[] getDesktopArray() {
        if (getDesktops() == null || getDesktops().size() == 0) return new String[0];
        String[] array = new String[getDesktops().size()];
        for (int i = 0; i < getDesktops().size(); i++) {
            array[i] = getDesktops().get(i).getUri();
        }
        return array;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getCount() {
        return this.count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public String getCover() {
        return this.cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setDesktops(List<Image> desktops) {
        this.desktops = desktops;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 518686895)
    public List<Image> getDesktops() {
        if (desktops == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ImageDao targetDao = daoSession.getImageDao();
            List<Image> desktopsNew = targetDao._queryAlbum_Desktops(id);
            synchronized (this) {
                if (desktops == null) {
                    desktops = desktopsNew;
                }
            }
        }
        return desktops;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 1890363222)
    public synchronized void resetDesktops() {
        desktops = null;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1023911229)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getAlbumDao() : null;
    }

}
