package luoluna.sweven.album.interf;

import android.view.View;

public interface AtlasOperate {

    /**
     * 选中所有
     */
    void selectAll();

    /**
     * 全部不选
     */
    void selectNone();

    /**
     * @return 获取选中数量
     */
    int getSelectedCount();

    /**
     * 删除
     */
    void delete(View view);

    /**
     * 分享
     */
    void share(View view);

    /**
     * 修改相册名
     */
    void  rename(View view);

    /**
     * 退出编辑状态
     */
    void closeEdit();

    /**
     * 至少选择判断
     */
    boolean less();

    /**
     * 图集合并
     */
    void merge(View view);

    /**
     * 添加图集
     */
    void addAtlas();
}
