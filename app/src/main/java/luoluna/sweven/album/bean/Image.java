package luoluna.sweven.album.bean;

/**
 * Created by Sweven on 2019/9/17--13:58.
 * Email: sweventears@foxmail.com
 */
public class Image {
    private int id;
    private String uri;

    public Image() {
    }

    public Image(String uri) {
        this.uri = uri;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
