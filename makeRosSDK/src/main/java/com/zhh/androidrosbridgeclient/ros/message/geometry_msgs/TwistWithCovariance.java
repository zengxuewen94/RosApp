package com.zhh.androidrosbridgeclient.ros.message.geometry_msgs;
import com.zhh.androidrosbridgeclient.ros.message.Message;
import com.zhh.androidrosbridgeclient.ros.message.MessageType;
@MessageType(string = "geometry_msgs/TwistWithCovariance")
public class TwistWithCovariance extends Message {
  public Twist twist;
  public double[] covariance;
}