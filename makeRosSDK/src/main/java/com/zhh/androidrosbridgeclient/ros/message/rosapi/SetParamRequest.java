package com.zhh.androidrosbridgeclient.ros.message.rosapi;

import com.zhh.androidrosbridgeclient.ros.message.Message;
import com.zhh.androidrosbridgeclient.ros.message.MessageType;
@MessageType(string = "rosapi/SetParam")
public class SetParamRequest extends Message {

  public String name;//参数名：线速度：/move_base/DWAPlannerROS/max_vel_x  角速度/move_base/DWAPlannerROS/max_vel_theta //角速度
  public String value;//指定速度最大为0.6,单位为m/s

}