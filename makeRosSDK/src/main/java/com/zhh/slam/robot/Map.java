package com.zhh.slam.robot;

import android.graphics.RectF;

import com.zhh.slam.bean.PointF;
import com.zhh.slam.bean.Size;

import java.util.Arrays;


public class Map {
    private PointF origin;
    private Size dimension;
    private PointF resolution;
    private long timestamp;
    private byte[] data;

    public Map(PointF origin, Size dimension, PointF resolution, long timestamp, byte[] data) {
        this.origin = new PointF(origin);
        this.dimension = new Size(dimension.getWidth(), dimension.getHeight());
        this.resolution = new PointF(resolution);
        this.timestamp = timestamp;
        this.data = Arrays.copyOf(data, data.length);
    }

    public PointF getOrigin() {
        return this.origin;
    }

    public void setOrigin(PointF origin) {
        this.origin = new PointF(origin);
    }

    public Size getDimension() {
        return this.dimension;
    }

    public void setDimension(Size dimension) {
        this.dimension = new Size(dimension);
    }

    public PointF getResolution() {
        return this.resolution;
    }

    public void setResolution(PointF resolution) {
        this.resolution = new PointF(resolution);
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public RectF getMapArea() {
        return new RectF(this.origin.getX(), this.origin.getY(), (float)this.dimension.getWidth() * this.resolution.getX() + this.origin.getX(), (float)this.dimension.getHeight() * this.resolution.getY() + this.origin.getY());
    }

    public byte[] getData() {
        return this.data;
    }

    public void setData(byte[] data) {
        this.data = Arrays.copyOf(data, data.length);
    }
}

