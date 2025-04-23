package com.zhh.androidrosbridgeclient.interfaces;

public interface OnNavigateListener {
    //动作已创建但未开始。
    void waitingForStart();

    //动作正在进行。
    void running();

    //动作成功完成。
    void finished();

    //动作已暂停
    void paused();

    //动作已停止
    void stopped();

    //动作遇到错误
    void error();

}
