package com.zhh.slam.mapview.mapdata;

import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;

import com.zhh.slam.bean.Size;
import com.zhh.slam.robot.Map;
import com.zhh.slam.utils.PointTranslateUtil;

public final class MapDataCache {
    private final static String TAG = "MapDataCache";

    private RectF mMapArea = null;
    private PointF mBaseCoordinate;
    private PointF mResolution;
    private Size mDimension;
    private MapDataStore mDataStore;


    public MapDataCache() {
        mResolution = new PointF(0.5f, 0.5f);
        mBaseCoordinate = new PointF();
        mDimension = new Size(160, 160);
        mDataStore = new MapDataStore(1, 1);
    }

    public MapDataCache(Map map) {
        if (map != null) {
            mResolution = new PointF(0.5f, 0.5f);
            mBaseCoordinate = new PointF();
            mDimension = new Size();
            mDataStore = new MapDataStore(map.getDimension().getWidth(), map.getDimension().getHeight());
            update(map);
        }

    }

    public PointF getResolution() {
        return mResolution;
    }

    public synchronized void update(Map map) {
        if (map == null) {
            return;
        }

        RectF updateArea = map.getMapArea();

        // new mMapArea within old mMapArea
        if (mMapArea != null && mMapArea.contains(updateArea)) {
            // copy new data
            mResolution = PointTranslateUtil.translate(map.getResolution());

            Rect mapPixelArea = mapAreaToMapPixelArea(updateArea);
            Point updateSize = PointTranslateUtil.translate(map.getDimension());

            byte[] newMapData = map.getData();
            byte[] dest = new byte[map.getDimension().getWidth()];

            Rect rect = new Rect();

            for (int y = 0; y < updateSize.y; y++) {
                int left = mapPixelArea.left;
                int top = mapPixelArea.bottom - y - 1;
                int right = mapPixelArea.left + updateSize.x;
                int bottom = top + 1;
                rect.set(left, top, right, bottom);
                System.arraycopy(newMapData, dest.length * y, dest, 0, dest.length);

                mDataStore.update(rect, dest);
            }

        } else {
            // expend area
            RectF oldMapArea = mMapArea;
            mMapArea = expendAreaPredicted(mMapArea, updateArea);

            mResolution = PointTranslateUtil.translate(map.getResolution());
            mBaseCoordinate = new PointF(mMapArea.left, mMapArea.top);
            mDimension = new Size(Math.round(mMapArea.width() / mResolution.x), Math.round(mMapArea.height() / mResolution.y));

            Rect mapPixelArea = mapAreaToMapPixelArea(mMapArea);

            MapDataStore newMapDataStore = new MapDataStore(mapPixelArea.width(), mapPixelArea.height());

            // copy old data
            if (oldMapArea != null) {
                Rect newCopyArea = mapAreaToMapPixelArea(oldMapArea);
                newMapDataStore.update(newCopyArea, mDataStore.getmMapData());
            }

            mDataStore = newMapDataStore;
            byte[] mapRawData = map.getData();
            Rect updateMapPixelArea;
            updateMapPixelArea = mapAreaToMapPixelArea(updateArea);
            byte[] dest = new byte[map.getDimension().getWidth()];

            Rect rect = new Rect();
            Point updateSize = PointTranslateUtil.translate(map.getDimension());

            // copy new data
            for (int y = 0; y < updateSize.y; y++) {
                int left = updateMapPixelArea.left;
                int top = updateMapPixelArea.bottom - y - 1;
                int right = updateMapPixelArea.left + updateSize.x;
                int bottom = top + 1;
                rect.set(left, top, right, bottom);
                System.arraycopy(mapRawData, map.getDimension().getWidth() * y, dest, 0, map.getDimension().getWidth());

                mDataStore.update(rect, dest);
            }
        }
    }


    private RectF expendArea(RectF mapArea, RectF area) {
        if (mapArea == null) {
            return area;
        }

        float left = Math.min(mapArea.left, area.left);
        float top = Math.min(mapArea.top, area.top);
        float right = Math.max(mapArea.right, area.right);
        float bottom = Math.max(mapArea.bottom, area.bottom);

        return new RectF(left, top, right, bottom);
    }

    private RectF expendAreaPredicted(RectF mapArea, RectF area) {
        if (mapArea == null) {
            return area;
        }

        float left = Math.min(mapArea.left, area.left);
        float top = Math.min(mapArea.top, area.top);
        float right = Math.max(mapArea.right, area.right);
        float bottom = Math.max(mapArea.bottom, area.bottom);

        if (area.left < mapArea.left) {
            left = area.left - 8;
        }
        if (area.top < mapArea.top) {
            top = area.top - 8;
        }
        if (area.right > mapArea.right) {
            right = area.right + 8;
        }
        if (area.bottom > mapArea.bottom) {
            bottom = area.bottom + 8;
        }

        return new RectF(left, top, right, bottom);
    }

    public synchronized void fetch(Rect area, byte[] buffer) {
        int width = mDimension.getWidth();
        int height = mDimension.getHeight();

        if (area.left < 0 || area.top < 0 || area.right > width || area.bottom > height) {
            return;
        }

        mDataStore.fetch(area, buffer);
    }

    public synchronized byte get(int x, int y) {
        return mDataStore.get(x, y);
    }

    public synchronized void set(int x, int y, byte color) {
        mDataStore.set(x, y, color);
    }

    public synchronized void clear() {
        mDataStore.clear();
        mMapArea = null;
    }

    public synchronized boolean isEmpty() {
        return mDataStore.isEmpty();
    }



    public com.zhh.slam.bean.PointF mapPixelCoordinateToMapCoordinate(Point pixel) {
        PointF offset = new PointF(pixel.x * mResolution.x, (mDimension.getHeight() - pixel.y) * mResolution.y);
        return new com.zhh.slam.bean.PointF(mBaseCoordinate.x + offset.x, mBaseCoordinate.y + offset.y);
    }

    final public com.zhh.slam.bean.PointF mapPixelCoordinateToMapCoordinate(PointF pixel) {
        PointF offset = new PointF(pixel.x * mResolution.x, (mDimension.getHeight() - pixel.y) * mResolution.y);
        return new com.zhh.slam.bean.PointF(mBaseCoordinate.x + offset.x, mBaseCoordinate.y + offset.y);
    }

    final public PointF mapCoordinateToMapPixelCoordinateF(float x, float y) {
        float px = (x - mBaseCoordinate.x) / mResolution.x;
        float py = mDimension.getHeight() - ((y - mBaseCoordinate.y) / mResolution.y);

        return new PointF(px, py);
    }

    final public Point mapCoordinateToMapPixelCoordinate(float x, float y) {
        int px = Math.round((x - mBaseCoordinate.x) / mResolution.x);
        int py = mDimension.getHeight() - Math.round((y - mBaseCoordinate.y) / mResolution.y);

        return new Point(px, py);
    }

    final public float mapDistanceToMapPixelDistance(float distence) {
        return distence / mResolution.x;
    }


    final public Rect mapAreaToMapPixelArea(RectF rect) {
        Point topLeft = mapCoordinateToMapPixelCoordinate(rect.left, rect.bottom);
        Point bottomRight = mapCoordinateToMapPixelCoordinate(rect.right, rect.top);

        return new Rect(topLeft.x, topLeft.y, bottomRight.x, bottomRight.y);
    }

    public Size getDimension() {
        return mDimension;
    }

}
