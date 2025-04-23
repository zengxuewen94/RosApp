package com.zhh.androidrosbridgeclient.ros.message.geometry_msgs;
import com.zhh.androidrosbridgeclient.ros.message.Message;
import com.zhh.androidrosbridgeclient.ros.message.MessageType;
@MessageType(string = "geometry_msgs/Vector3")
public class Vector3 extends Message {
  public double x; //小车x轴方向速度
  public double y; //小车y轴方向速度
  public double z; //小车z轴方向速度
}