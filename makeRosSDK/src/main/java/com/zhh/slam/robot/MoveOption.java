package com.zhh.slam.robot;


public class MoveOption {
    private boolean appending = false;
    private boolean milestone = false;
    private boolean noSmooth = false;
    private boolean keyPoints = false;
    private boolean precise = false;
    private boolean withYaw = false;
    private boolean returnUnreachableDirectly = false;
    private boolean trackWithOA = false;
    private boolean onlyUseGlobalCostMap = false;
    private Double speedRatio = null;
    private Integer failRetryCount = null;

    public MoveOption() {
    }

    public MoveOption(Double speedRatio) {
        this.speedRatio = speedRatio;
    }

    public boolean isAppending() {
        return this.appending;
    }

    public void setAppending(boolean appending) {
        this.appending = appending;
    }

    public boolean isMilestone() {
        return this.milestone;
    }

    public void setMilestone(boolean milestone) {
        this.milestone = milestone;
    }

    public boolean isNoSmooth() {
        return this.noSmooth;
    }

    public void setNoSmooth(boolean noSmooth) {
        this.noSmooth = noSmooth;
    }

    public boolean isKeyPoints() {
        return this.keyPoints;
    }

    public void setKeyPoints(boolean keyPoints) {
        this.keyPoints = keyPoints;
    }

    public boolean isPrecise() {
        return this.precise;
    }

    public void setPrecise(boolean precise) {
        this.precise = precise;
    }

    public boolean isWithYaw() {
        return this.withYaw;
    }

    public void setWithYaw(boolean withYaw) {
        this.withYaw = withYaw;
    }

    public boolean isReturnUnreachableDirectly() {
        return this.returnUnreachableDirectly;
    }

    public void setReturnUnreachableDirectly(boolean returnUnreachableDirectly) {
        this.returnUnreachableDirectly = returnUnreachableDirectly;
    }

    public boolean isTrackWithOA() {
        return this.trackWithOA;
    }

    public void setTrackWithOA(boolean trackWithOA) {
        this.trackWithOA = trackWithOA;
    }

    public boolean isOnlyUseGlobalCostMap() {
        return this.onlyUseGlobalCostMap;
    }

    public void setOnlyUseGlobalCostMap(boolean v) {
        this.onlyUseGlobalCostMap = v;
    }

    public Double getSpeedRatio() {
        return this.speedRatio;
    }

    public void setSpeedRatio(Double speedRatio) {
        this.speedRatio = speedRatio;
    }

    public Integer getFailRetryCount() {
        return this.failRetryCount;
    }

    public void setFailRetryCount(Integer failRetryCount) {
        this.failRetryCount = failRetryCount;
    }
}

