package com.zhh.androidrosbridgeclient.ros.message.std_msgs;
import com.zhh.androidrosbridgeclient.ros.message.Message;
import com.zhh.androidrosbridgeclient.ros.message.MessageType;
/**
 * @author Administrator
 */
@MessageType(string = "std_msgs/String")
public class DataString extends Message {
    public String data;
}