package luoluna.sweven.album.entity;

import com.sweven.console.ToastUtil;

import java.io.File;

import luoluna.sweven.album.app.App;
import luoluna.sweven.album.entity.local.Image;

/**
 * <p>Create by Sweven on 2020/7/16 -- 14:55</p>
 * Email: sweventears@163.com
 */
public class PictureMenu extends AbsMenu {

    private Image image;
    private String path;
    private String fileName;
    private long size;

    public PictureMenu(String menu, String method) {
        super(menu, method);
    }

    public void setImage(Image image) {
        this.image = image;
        this.path = image.getUri();
        int index = this.path.lastIndexOf("/");
        this.fileName = this.path.substring(index + 1);
        this.size = new File(path).length();
    }

    public void rename() {
        ToastUtil.showShort(App.application, "rename");
    }
}
