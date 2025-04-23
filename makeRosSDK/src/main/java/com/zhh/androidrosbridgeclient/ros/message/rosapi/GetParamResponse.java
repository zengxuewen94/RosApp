package com.zhh.androidrosbridgeclient.ros.message.rosapi;

import com.zhh.androidrosbridgeclient.ros.message.Message;
import com.zhh.androidrosbridgeclient.ros.message.MessageType;

@MessageType(string = "rosapi/GetParam")
public class GetParamResponse extends Message {

  public String value;//返回值

}