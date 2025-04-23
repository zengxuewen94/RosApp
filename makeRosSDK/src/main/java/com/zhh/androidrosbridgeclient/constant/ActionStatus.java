package com.zhh.androidrosbridgeclient.constant;

/**
 * 导航状态
 *  /**
 *              *   static final byte PENDING=0;//任务尚未被处理
 *              *   static final byte ACTIVE=1 ;//任务正在被处理
 *              *   static final byte PREEMPTED=2 ;//任务执行后收到取消请求
 *              *   static final byte SUCCEEDED=3;//导航任务成功，目标点已到达
 *              *   static final byte ABORTED=4 ;//由于某些故障，任务执行过程中中止了目标
 *              *   static final byte REJECTED=5 ;//任务在未处理的情况下被服务器拒绝
 *              *   static final byte PREEMPTING=6 ;//目标在尚未完成执行动作时收到了取消请求
 *              *   static final byte RECALLING=7 ;//目标在开始执行前收到了取消请求，但是动作服务器还没有确认目标被取消
 *              *   static final byte RECALLED=8 ;//目标在开始执行前收到了取消请求，并成功取消
 *              *   static final byte LOST=9 ;//已经确定目标丢失
 *
 */
public enum ActionStatus {
    PENDING,
    ACTIVE,
    PREEMPTED,
    SUCCEEDED,
    ABORTED,
    REJECTED,
    PREEMPTING,
    RECALLING,
    RECALLED,
    LOST;
    private ActionStatus() {
    }
}
