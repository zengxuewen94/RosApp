package com.zhh.slam.robot;

import com.zhh.slam.bean.PointF;


public class RectangleArea {
    private PointF start_;
    private PointF end_;
    private float halfWidth_;
    private int id_;
    private int usage_;
    private MapMetaData metaData_;

    public RectangleArea() {
        this.start_ = new PointF();
        this.end_ = new PointF();
        this.halfWidth_ = 0.0F;
        this.id_ = 0;
        this.usage_ = 3;
    }

    public RectangleArea(PointF start, PointF end, float halfWidth, int id, int usage, MapMetaData metaData) {
        this.start_ = start;
        this.end_ = end;
        this.halfWidth_ = halfWidth;
        this.id_ = id;
        this.usage_ = usage;
        this.metaData_ = metaData;
    }

    public PointF getStart() {
        return this.start_;
    }

    public void setStart(PointF start) {
        this.start_ = start;
    }

    public PointF getEnd() {
        return this.end_;
    }

    public void setEnd(PointF end) {
        this.end_ = end;
    }

    public float getHalfWidth() {
        return this.halfWidth_;
    }

    public void setHalfWidth(float halfWidth) {
        this.halfWidth_ = halfWidth;
    }

    public int getId() {
        return this.id_;
    }

    public void setId(int id) {
        this.id_ = id;
    }

    public int getUsage() {
        return this.usage_;
    }

    public void setUsage(int usage) {
        this.usage_ = usage;
    }

    public MapMetaData getMetaData() {
        return this.metaData_;
    }

    public void setMetaData(MapMetaData metaData) {
        this.metaData_ = metaData;
    }
}

