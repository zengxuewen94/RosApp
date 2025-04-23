package com.zhh.androidrosbridgeclient.ros.message.os_msgs;

import com.zhh.androidrosbridgeclient.ros.message.Message;
import com.zhh.androidrosbridgeclient.ros.message.MessageType;

@MessageType(string = "os_msgs/OSConfig")
public class OSConfig extends Message {
//系统版本信息
    public String version; //版本号
    public String code_name;
    public String section;
    public String arch_name;//系统架构
    public String priority;
    public String compile_time;//系统发布时间
    public String commit_id;
    public String license_id;
    public String ascan;

}