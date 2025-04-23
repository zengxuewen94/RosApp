package com.zhh.androidrosbridgeclient.ros.message.geometry_msgs;
import com.zhh.androidrosbridgeclient.ros.message.Message;
import com.zhh.androidrosbridgeclient.ros.message.MessageType;
@MessageType(string = "geometry_msgs/Quaternion")
public class Quaternion extends Message {
  public double x;//四元素x
  public double y;//四元素y
  public double z;//四元素z
  public double w;//四元素w
}
