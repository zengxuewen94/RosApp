package com.zhh.androidrosbridgeclient.ros.message.os_msgs;

import com.zhh.androidrosbridgeclient.ros.message.Message;
import com.zhh.androidrosbridgeclient.ros.message.MessageType;

@MessageType(string = "os_msgs/OSResponseResult")
public class OSResponseResult extends Message {

  static final byte OS_NAV_STATUS_UNKNOWN = 0;//导航未知状态
  static final byte OS_NAV_STATUS_IDLE = 1;//导航空闲状态
  static final byte OS_NAV_STATUS_DASH = 2;//底盘状态
  static final byte OS_NAV_STATUS_MAPPING = 4;//建图
  static final byte OS_NAV_STATUS_NAV = 8;//导航
  static final byte OS_NAV_STATUS_DOCKING = 16;//回充
  static final byte OS_NAV_STATUS_GMCL = 32;//定位
  public int os_status;
  public long execute_state;//返回的执行状态信息，0:失败， 1:成功。
  public String process_result;
}