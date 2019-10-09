package luoluna.sweven.album.bean;

import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import luoluna.sweven.album.activity.AlbumActivity;
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
    private long count;
    private boolean system;
    private List<String> desktops;
    private String remark;
    private boolean add;
    private String cover;

    public Album() {
    }

    /**
     * 创建相册
     *
     * @param album 默认一定有id和name，否则会无法同步数据库
     * @return 完整的album
     */
    public static Album create(Album album, String path) {
        album.setPath(path);
        String images = FileManager.getImgListByDir(path);
        String[] desktops = images.split(";");
        if (album.desktops == null) {
            album.desktops = new ArrayList<>();
        }
        if (desktops.length > 0) {
            album.setCover(desktops[0]);
            album.desktops.addAll(Arrays.asList(desktops));
        }
        album.setCount(album.desktops.get(0).equals("") ? 0 : desktops.length);
        return album;
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

    public static Album find(Context context, int aid) {
        return Helper.with().getAlbumByAid(context, aid);
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

    public boolean isSystem() {
        return system;
    }

    public void setSystem(boolean system) {
        this.system = system;
    }

    @Override
    public String toString() {
        return "Album{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", count=" + count +
                ", system=" + system +
                ", desktops=" + desktops +
                ", remark='" + remark + '\'' +
                ", cover='" + cover + '\'' +
                '}';
    }
}
