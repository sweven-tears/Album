package luoluna.sweven.album.widget;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Sweven on 2019/9/17.
 * Email:sweventears@Foxmail.com
 */
public class RecyclerViewItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public RecyclerViewItemDecoration() {
        this.space = 10;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        if (parent.indexOfChild(view) == 0) {
            //控件第一排时，让它低于正常高度
            outRect.top = space / 2;
        } else if (parent.indexOfChild(view) == 2) {
            //控件第三排时，让它低于正常高度，这样达到瀑布流效果
            outRect.top = space / 2;
        } else {
            outRect.top = space / 3;
        }
        if (parent.indexOfChild(view) == parent.getChildCount()) {
            outRect.bottom = space;
        }
    }
}
