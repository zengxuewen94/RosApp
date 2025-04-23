package com.zhh.androidrosbridgeclient.ros.message.os_msgs;

import com.zhh.androidrosbridgeclient.ros.message.Message;
import com.zhh.androidrosbridgeclient.ros.message.MessageType;
import com.zhh.androidrosbridgeclient.ros.message.geometry_msgs.PoseStamped;
import com.zhh.androidrosbridgeclient.ros.message.TimePrimitive;

@MessageType(string = "os_msgs/NavFeedBack")
public class NavFeedBack extends Message {

    public PoseStamped current_pose;//小车当前位置
    public TimePrimitive navigation_time;//导航已用时间
    public float distance_remaining;//目标点剩余距离

}