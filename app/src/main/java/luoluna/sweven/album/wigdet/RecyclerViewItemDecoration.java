package luoluna.sweven.album.wigdet;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Sweven on 2019/9/17.
 * Email:sweventears@Foxmail.com
 */
public class RecyclerViewItemDecoration extends RecyclerView.ItemDecoration {
    private int space = 0;
    private int pos;
    private int decorate = 3;

    public RecyclerViewItemDecoration(int space, int decorate) {
        this.space = space;
        this.decorate = decorate;
    }

    public RecyclerViewItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        outRect.top = space;

        //该View在整个RecyclerView中位置。
        pos = parent.getChildAdapterPosition(view);

        //取模
        if (decorate == 3) {
            setSpace3(outRect);
        } else if (decorate == 2) {
            setSpace2(outRect);
        }


    }

    private void setSpace2(Rect outRect) {
        if (pos % decorate == 0) {
            outRect.left = 0;
            outRect.right = space;
        }
        if (pos % decorate == 1) {
            outRect.left = space;
            outRect.right = 0;
        }
    }

    private void setSpace3(Rect outRect) {
        //两列的左边一列
        if (pos % 3 == 0) {
            outRect.left = space;
            outRect.right = space / 3;
        }

        //两列的右边一列
        if (pos % 3 == 1) {
            outRect.left = space / 3;
            outRect.right = space;
        }
    }
}
