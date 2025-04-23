package com.zhh.androidrosbridgeclient.ros.message.nav_msgs;
import com.zhh.androidrosbridgeclient.ros.message.Header;
import com.zhh.androidrosbridgeclient.ros.message.Message;
import com.zhh.androidrosbridgeclient.ros.message.MessageType;
import com.zhh.androidrosbridgeclient.ros.message.geometry_msgs.PoseWithCovariance;
import com.zhh.androidrosbridgeclient.ros.message.geometry_msgs.TwistWithCovariance;

@MessageType(string = "nav_msgs/Odometry")
public class Odometry extends Message {
public Header header;//时间戳信息
public String child_frame_id;//对应组件id
public PoseWithCovariance pose;//空间位姿
public TwistWithCovariance twist;
}