package com.zhh.androidrosbridgeclient.ros.message.rosapi;

import com.zhh.androidrosbridgeclient.ros.message.Message;
import com.zhh.androidrosbridgeclient.ros.message.MessageType;

@MessageType(string = "rosapi/GetParam")
public class GetParamRequest extends Message {

  public String name;//参数名：/velocity_smoother/speed_lim_v（限定最大线速度）；/velocity_smoother/speed_lim_w（限定最大角速度）
  //public String default;//默认为空

}