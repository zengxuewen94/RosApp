package com.zhh.androidrosbridgeclient.ros.message.os_msgs;

import com.zhh.androidrosbridgeclient.ros.message.Message;
import com.zhh.androidrosbridgeclient.ros.message.MessageType;

@MessageType(string = "os_msgs/NProcess")
public class NProcess extends Message {

    public String system_cmd;
    public String[] args;

}