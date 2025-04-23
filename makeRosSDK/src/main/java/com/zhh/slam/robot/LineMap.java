package com.zhh.slam.robot;

import java.util.HashMap;


public final class LineMap extends MapLayer {
    private HashMap<String, CompositeLine> lines_ = new HashMap();

    public LineMap() {
    }

    public HashMap<String, CompositeLine> getLines() {
        return this.lines_;
    }

    public void setLines(HashMap<String, CompositeLine> lines) {
        this.lines_ = lines;
    }

    @Override
    public void clear() {
        super.clear();
        this.lines_.clear();
    }
}

