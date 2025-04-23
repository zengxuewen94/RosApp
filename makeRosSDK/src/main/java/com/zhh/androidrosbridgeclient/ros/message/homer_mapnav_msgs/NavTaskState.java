package com.zhh.androidrosbridgeclient.ros.message.homer_mapnav_msgs;
import com.zhh.androidrosbridgeclient.ros.message.Message;
import com.zhh.androidrosbridgeclient.ros.message.MessageType;
@MessageType(string = "homer_mapnav_msgs/NavTaskState")
public class NavTaskState extends Message {
    static final int IDLE=0;
    static final int START=1;//执行任务
    static final int RUNNING=2;//中间状态
    static final int PAUSE=3;//暂停任务
    static final int STOPED=4;//停止任务
    int state;//可选1，3，4赋值
    public String name;//任务名
    public int task_seq;
    public int current_seq;
}