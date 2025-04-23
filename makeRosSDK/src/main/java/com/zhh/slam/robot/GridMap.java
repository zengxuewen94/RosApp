package com.zhh.slam.robot;

import com.zhh.slam.bean.Location;
import com.zhh.slam.bean.PointF;
import com.zhh.slam.bean.Size;


public final class GridMap extends MapLayer {
    private Location origin_;
    private Size dimension_;
    private PointF resolution_;
    private byte[] mapData_;

    public GridMap() {
    }

    public Location getOrigin() {
        return this.origin_;
    }

    public void setOrigin(Location origin) {
        this.origin_ = origin;
    }

    public Size getDimension() {
        return this.dimension_;
    }

    public void setDimension(Size dimension) {
        this.dimension_ = dimension;
    }

    public PointF getResolution() {
        return this.resolution_;
    }

    public void setResolution(PointF resolution) {
        this.resolution_ = resolution;
    }

    public byte[] getMapData() {
        return this.mapData_;
    }

    public void setMapData(byte[] mapData) {
        this.mapData_ = mapData;
    }

    @Override
    public void clear() {
        super.clear();
        this.origin_ = null;
        this.dimension_ = null;
        this.resolution_ = null;
        this.mapData_ = null;
    }
}
