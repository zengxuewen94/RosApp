package com.zhh.slam.robot;

import java.util.HashMap;


public final class MapMetaData {
    private HashMap<String, String> dict_;

    public MapMetaData() {
        this.dict_ = new HashMap();
    }

    public MapMetaData(MapMetaData another) {
        this.dict_ = new HashMap(another.getData());
    }

    public HashMap<String, String> getData() {
        return this.dict_;
    }

    public void setData(HashMap<String, String> dict_) {
        this.dict_ = dict_;
    }

    public String getValue(String key) {
        return (String)this.dict_.get(key);
    }

    public void setValue(String key, String value) {
        this.dict_.put(key, value);
    }

    public void swap(MapMetaData other) {
        HashMap<String, String> temp = new HashMap();
        temp.putAll(this.dict_);
        this.dict_.clear();
        this.dict_.putAll(other.getData());
        other.setData(temp);
    }

    public void clear() {
        this.dict_.clear();
    }
}
