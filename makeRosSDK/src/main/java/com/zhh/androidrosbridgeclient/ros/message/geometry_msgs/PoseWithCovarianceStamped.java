package com.zhh.androidrosbridgeclient.ros.message.geometry_msgs;
import com.zhh.androidrosbridgeclient.ros.message.Header;
import com.zhh.androidrosbridgeclient.ros.message.Message;
import com.zhh.androidrosbridgeclient.ros.message.MessageType;
@MessageType(string = "geometry_msgs/PoseWithCovarianceStamped ")
public class PoseWithCovarianceStamped extends Message {
  public Header header;
  public PoseWithCovariance pose; //充电桩位姿：pose:pose:position: {x:-0.98, y:7.6, z:5.5} orientation: {x:0.0, y:0.0, z:-0.7, w:0.7} covariance:[0.0]
}
