package com.zhh.androidrosbridgeclient.interfaces;

import com.zhh.androidrosbridgeclient.ros.message.geometry_msgs.Pose;

public interface OnPoseListener {
    //动作成功完成。
    void finished(Pose pose);

}
