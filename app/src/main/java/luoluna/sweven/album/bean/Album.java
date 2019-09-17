package luoluna.sweven.album.bean;

import java.util.List;

/**
 * Created by Sweven on 2019/9/10--17:01.
 * Email: sweventears@foxmail.com
 */
public class Album {
    private int id;
    private String name;
    private String path;
    private long count;
    private List<String> desktops;
    private String remark;
    private boolean add;
    private String cover;

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
                '}';
    }
}
