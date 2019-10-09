package luoluna.sweven.album.widget;

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
    private boolean hasSide = false;
    private boolean hasTop = false;

    public RecyclerViewItemDecoration(int space, int decorate) {
        this.space = space;
        this.decorate = decorate;
    }

    public RecyclerViewItemDecoration(int space) {
        this.space = space;
    }

    public void setHasSide(boolean hasSide) {
        this.hasSide = hasSide;
    }

    public void setHasTop(boolean hasTop) {
        this.hasTop = hasTop;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        outRect.top = hasTop ? space : 0;

        //该View在整个RecyclerView中位置。
        pos = parent.getChildAdapterPosition(view);

//        //取模
        if (decorate == 2) {
            setSpace2(outRect);
        } else if (decorate == 3) {
            setSpace3(outRect);
        }

//        setSpace(outRect);
    }

    private void setSpace(Rect rect) {
        int all = decorate;
        for (int i = 0; i < all; i++) {
            if (pos % decorate == i && i == 0) {
                rect.left = hasSide ? space : 0;
                rect.right = space;
            } else if (pos % decorate == i && i == all - 1) {
                rect.left = space;
                rect.right = hasSide ? space : 0;
            } else {
                rect.right = space;
                rect.left = space;
            }
        }
    }

    private void setSpace2(Rect outRect) {
        if (pos % decorate == 0) {
            outRect.left = space * 2;
            outRect.right = space;
        }
        if (pos % decorate == 1) {
            outRect.left = space;
            outRect.right = space * 2;
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
