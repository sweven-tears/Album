package luoluna.sweven.album.bean;

import android.content.Context;

import com.sweven.util.FileUtil;

import java.util.ArrayList;
import java.util.List;

import luoluna.sweven.album.app.App;
import luoluna.sweven.album.app.Helper;
import luoluna.sweven.album.manager.FileManager;

/**
 * Created by Sweven on 2019/9/10--17:01.
 * Email: sweventears@foxmail.com
 */
public class Album {
    private int id;
    private String name;
    private String path;
    private List<String> desktops;
    private long count;
    private String cover;
    private String remark;

    private boolean add;

    public Album() {
    }

    public Album(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Album(int id, String name, String cover) {
        this.id = id;
        this.name = name;
        this.cover = cover;
    }

    /**
     * 创建相册
     *
     * @param album 默认一定有id和name，否则会无法同步数据库
     * @return 完整的album
     */
    public static Album create(Album album, String path) {
        album.setPath(path);
        List<String> desktops = FileUtil.getFilesByEndName(path, App.supportFormat);
        if (desktops.size() > 0) {
            album.setCover(desktops.get(0));
            if (album.getDesktops() == null) {
                album.setDesktops(new ArrayList<>());
            }
            album.desktops.addAll(desktops);
        }
        album.setCount(desktops.size());
        return album;
    }

    public static Album find(Context context, int aid) {
        return Helper.with().getAlbumByAid(context, aid);
    }

    public static Album find(Context context, String uri) {
        return FileManager.getInstance().getAlbumByFolder(context, uri);
    }


    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public Album(boolean add) {
        this.add = add;
    }

    public boolean isAdd() {
        return add;
    }

    public void setAdd(boolean add) {
        this.add = add;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public List<String> getDesktops() {
        return desktops;
    }

    public void setDesktops(List<String> desktops) {
        this.desktops = desktops;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "Album{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", count=" + count +
                ", desktops=" + desktops +
                ", remark='" + remark + '\'' +
                ", cover='" + cover + '\'' +
                '}';
    }
}
