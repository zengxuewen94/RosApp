package com.zhh.slam.robot;


public abstract class MapLayer {
    private MapMetaData metaData_;
    private String name_;
    private String usage_;

    public MapLayer() {
    }

    public MapMetaData getMetaData() {
        return this.metaData_;
    }

    public void setMetaData(MapMetaData metaData) {
        this.metaData_ = metaData;
    }

    public String getName() {
        return this.name_;
    }

    public void setName(String name) {
        this.name_ = name;
    }

    public String getUsage() {
        return this.usage_;
    }

    public void setUsage(String usage) {
        this.usage_ = usage;
    }

    public void clear() {
        this.metaData_.clear();
        this.name_ = "";
        this.usage_ = "";
    }
}
