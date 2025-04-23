package com.zhh.slam.mapview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;


import com.zhh.slam.mapview.mapdata.MapDataCache;
import com.zhh.slam.utils.ImageUtil;
import com.zhh.slam.utils.ListUtils;
import com.zhh.slam.utils.MatrixUtil;

import java.util.ArrayList;

/**
 * 绘制slam地图
 */
public final class SlamMapView extends View {
    public static final int TILE_PIXEL_SIZE = 200;
    private ArrayList<Tile> mTiles;
    private int mTileWidth;
    private int mTileHeight;
    private int mPixelWidth;
    private int mPixelHeight;

    private Paint mPaint;
    private Matrix mOuterMatrix = new Matrix();
    private MapDataCache mMapDataCache;


    public SlamMapView(Context context) {
        super(context);
        init();
    }

    public SlamMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mTiles = new ArrayList<>();
        setBackgroundColor(Color.TRANSPARENT);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mPaint = new Paint();
        mPaint.setDither(false);
        mPaint.setColor(Color.BLUE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        drawTiles(canvas, mPaint);
        canvas.restore();
    }


    private void drawTiles(Canvas canvas, Paint paint) {
        if (ListUtils.isNotEmpty(mTiles)) {
            Matrix innerMatrix = new Matrix();
            synchronized (mTiles) {
                for (int i = 0; i < mTiles.size(); i++) {
                    Tile tile = mTiles.get(i);
                    if (tile == null) {
                        continue;
                    }
                    innerMatrix.reset();

                    MatrixUtil.calculateRectTranslateMatrix(new RectF(0, 0, tile.bitmap.getWidth(), tile.bitmap.getHeight()), new RectF(tile.area), innerMatrix);
                    innerMatrix.postConcat(mOuterMatrix);
                    canvas.drawBitmap(tile.bitmap, innerMatrix, paint);

                }
            }
        }
    }


    /**
     * 将地图数据转成bitmap绘制在界面上
     * @param mapDataCache
     */
    public void updateTiles(MapDataCache mapDataCache) {
        mMapDataCache = mapDataCache;
        mPixelWidth = mapDataCache.getDimension().getWidth();
        mPixelHeight = mapDataCache.getDimension().getHeight();

        int numX = (int) Math.ceil(((double) mPixelWidth) / TILE_PIXEL_SIZE);
        int numY = (int) Math.ceil(((double) mPixelHeight) / TILE_PIXEL_SIZE);

        mTileWidth = numX;
        mTileHeight = numY;

        synchronized (mTiles) {

            mTiles = new ArrayList<>(mTileWidth * mTileHeight);

            for (int i = 0; i < mTileWidth; i++) {
                int left = TILE_PIXEL_SIZE * i;
                int right = left + TILE_PIXEL_SIZE;
                if (right > mPixelWidth) {
                    right = mPixelWidth;
                }

                for (int j = 0; j < mTileHeight; j++) {
                    int top = TILE_PIXEL_SIZE * j;
                    int bottom = top + TILE_PIXEL_SIZE;
                    if (bottom > mPixelHeight) {
                        bottom = mPixelHeight;
                    }

                    Tile tile = new Tile();
                    tile.area = new Rect(left, top, right, bottom);

                    byte[] buffer = new byte[tile.area.width() * tile.area.height()];
                    mMapDataCache.fetch(tile.area, buffer);
                    tile.bitmap = ImageUtil.createImage(buffer, tile.area.width(), tile.area.height());
                    mTiles.add(tile);
                }
            }
        }
        postInvalidate();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
    }

    public void setMatrix(Matrix matrix) {
        mOuterMatrix = matrix;
        postInvalidate();
    }

    private static class Tile {
        public Rect area;
        public Bitmap bitmap;
    }
}
