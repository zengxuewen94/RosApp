package com.zhh.androidrosbridgeclient.ros.message.geometry_msgs;
import com.zhh.androidrosbridgeclient.ros.message.Message;
import com.zhh.androidrosbridgeclient.ros.message.MessageType;
@MessageType(string = "geometry_msgs/Twist")
public class Twist extends Message {
  public Vector3 linear; //线速度 linear.x 控制移动
  public Vector3 angular; //角速度 angular.z 控制旋转
}