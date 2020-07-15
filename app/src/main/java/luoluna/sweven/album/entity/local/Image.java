package luoluna.sweven.album.entity.local;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * <p>Create by Sweven on 2020/7/14 -- 17:10</p>
 * Email: sweventears@163.com
 */
@Entity
public class Image {
    @Id
    private long id;
    private long aid;
    private String uri;

    @Generated(hash = 1380170411)
    public Image(long id, long aid, String uri) {
        this.id = id;
        this.aid = aid;
        this.uri = uri;
    }

    @Generated(hash = 1590301345)
    public Image() {
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getAid() {
        return this.aid;
    }

    public void setAid(long aid) {
        this.aid = aid;
    }

    public String getUri() {
        return this.uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
