package com.zhh.androidrosbridgeclient;

import com.zhh.androidrosbridgeclient.action.MoveDirection;
import com.zhh.androidrosbridgeclient.constant.ActionStatus;
import com.zhh.androidrosbridgeclient.constant.CommonConstant;
import com.zhh.androidrosbridgeclient.interfaces.OnNavigateListener;
import com.zhh.androidrosbridgeclient.ros.MessageHandler;
import com.zhh.androidrosbridgeclient.ros.ROSClient;
import com.zhh.androidrosbridgeclient.ros.Service;
import com.zhh.androidrosbridgeclient.ros.Topic;
import com.zhh.androidrosbridgeclient.ros.message.Header;
import com.zhh.androidrosbridgeclient.ros.message.actionlib_msgs.GoalID;
import com.zhh.androidrosbridgeclient.ros.message.actionlib_msgs.GoalStatusArray;
import com.zhh.androidrosbridgeclient.ros.message.geometry_msgs.Point;
import com.zhh.androidrosbridgeclient.ros.message.geometry_msgs.Pose;
import com.zhh.androidrosbridgeclient.ros.message.geometry_msgs.PoseStamped;
import com.zhh.androidrosbridgeclient.ros.message.geometry_msgs.PoseWithCovariance;
import com.zhh.androidrosbridgeclient.ros.message.geometry_msgs.PoseWithCovarianceStamped;
import com.zhh.androidrosbridgeclient.ros.message.geometry_msgs.Quaternion;
import com.zhh.androidrosbridgeclient.ros.message.geometry_msgs.Twist;
import com.zhh.androidrosbridgeclient.ros.message.geometry_msgs.Vector3;
import com.zhh.androidrosbridgeclient.ros.message.homer_mapnav_msgs.GetMapListRequest;
import com.zhh.androidrosbridgeclient.ros.message.homer_mapnav_msgs.GetMapListResponse;
import com.zhh.androidrosbridgeclient.ros.message.homer_mapnav_msgs.GetTasksRequest;
import com.zhh.androidrosbridgeclient.ros.message.homer_mapnav_msgs.GetTasksResponse;
import com.zhh.androidrosbridgeclient.ros.message.homer_mapnav_msgs.LoadMapRequest;
import com.zhh.androidrosbridgeclient.ros.message.homer_mapnav_msgs.MapInfo;
import com.zhh.androidrosbridgeclient.ros.message.homer_mapnav_msgs.NavTask;
import com.zhh.androidrosbridgeclient.ros.message.homer_mapnav_msgs.NavTaskList;
import com.zhh.androidrosbridgeclient.ros.message.homer_mapnav_msgs.SaveMapRequest;
import com.zhh.androidrosbridgeclient.ros.message.nav_msgs.OccupancyGrid;
import com.zhh.androidrosbridgeclient.ros.message.nav_msgs.Path;
import com.zhh.androidrosbridgeclient.ros.message.os_msgs.OSRequestCmd;
import com.zhh.androidrosbridgeclient.ros.message.os_msgs.OSResponseResult;
import com.zhh.androidrosbridgeclient.ros.message.os_msgs.ResMsg;
import com.zhh.androidrosbridgeclient.ros.message.rosapi.SetParamRequest;
import com.zhh.androidrosbridgeclient.ros.message.sensor_msgs.BatteryState;
import com.zhh.androidrosbridgeclient.ros.message.sensor_msgs.LaserScan;
import com.zhh.androidrosbridgeclient.ros.message.std_msgs.DataString;
import com.zhh.androidrosbridgeclient.ros.message.std_msgs.Float32;
import com.zhh.androidrosbridgeclient.ros.message.std_msgs.UInt16;
import com.zhh.androidrosbridgeclient.ros.rosapi.message.Empty;
import com.zhh.androidrosbridgeclient.ros.rosbridge.ROSBridgeClient;

import java.util.ArrayList;
import java.util.List;

public enum RosBridgeClientManager {
    /**
     * 单例
     */
    INSTANCE;
    private final String uriString = "ws://192.168.31.195:9090";
    private ROSBridgeClient client;

    /**
     * 急停按下返回data：1
     * 刹车按下返回data：2
     * 刹车急停同时按下返回data：3
     * 松开状态：0
     */
    private int buttonStatus;
    /**
     * 电量百分比
     */
    private int batteryPercentage = 0;
    /**
     * 电充状态 0未知;1充电中;2放电中;3没有充电;4电池电量已满
     */
    private int powerSupplyStatus = 0;
    /**
     * 导航状态
     * status=1为导航中
     * status=3为目标点已到达
     */
    private int status;
    /**
     * 底盘线速度获取
     */
    private double speed;
    /**
     * 底盘角速度获取
     */
    private double rotateSpeed;

    /**
     * 导航状态
     * status=1为导航中
     * status=3为目标点已到达
     */
    boolean isActionRunning = false;

    /**
     * 底盘位置数据
     */
    private Pose robotPose;
    /**
     * 充电桩数据
     */
    private PoseStamped homePose;


    private ActionStatus actionStatus;
    /**
     * 获取地图原始数据
     */
    private Topic<OccupancyGrid> mapDateTopic;

    /**
     * 导航状态
     */
    private Topic<GoalStatusArray> statusTopic;

    /**
     * 获取雷达数据
     */
    private Topic<LaserScan> laserScanTopic;


    private OnNavigateListener onNavigateListener;
    /**
     * 地图数据
     */
    private OccupancyGrid occupancyGrid;

    /**
     * 雷达数据
     */
    private LaserScan laserScan;

    /**
     * 全局路径规划
     */
    private Path globalPlan;

    /**
     * 局部路径规划
     */
    private Path localPlan;



    private String currentMapName = "";

    public boolean connect(String uri, final ROSClient.ConnectionStatusListener listener, boolean isDebug) {
        boolean isConnect;
        if (uri.isEmpty()) {
            uri = uriString;
        }
        client = new ROSBridgeClient(uri);
        isConnect = client.connect(listener);
        if (isConnect) {
            if (isDebug) {
                client.setDebug(true);
            }
            initTopic();
        }
        return isConnect;

    }

    public boolean connect(String uri, final ROSClient.ConnectionStatusListener listener) {
        return connect(uri, listener, false);
    }

    public boolean connect(final ROSClient.ConnectionStatusListener listener) {
        return connect("", listener, false);
    }

    public void disConnect() {
        if (null != client) {
            client.disconnect();
        }
    }

    private void initTopic() {
        statusTopic();
        powerSupplyStatusTopic();
        robotPoseTopic();
        homePoseTopic();
        buttonStatusTopic();
    }

    /**
     * @return 电池充电状态，剩余电量
     */
    public void powerSupplyStatusTopic() {
        MessageHandler<BatteryState> handler = message -> {
            if (message == null) {
                robotPose = null;
            } else {
                powerSupplyStatus = message.power_supply_status;
                batteryPercentage = Math.round(message.percentage * 100);
            }
        };

        // 底盘电池数据
        Topic<BatteryState> batteryTopic = new Topic<>("/battery_state", BatteryState.class, client);
        batteryTopic.subscribe(handler);
    }


    /**
     * 获取机器人当前位姿信息
     */
    private void robotPoseTopic() {
        MessageHandler<Pose> handler = message -> {
            robotPose = message;
        };
        Topic<Pose> poseTopic = new Topic<>("/robot_pose", Pose.class, client);
        poseTopic.subscribe(handler);
    }

    /**
     * 获取充电桩数据
     */
    private void homePoseTopic() {

        MessageHandler<PoseStamped> handler = message -> {
            homePose = message;
        };

        Topic<PoseStamped> homePoseTopic = new Topic<>("/map_manager/home_pose", PoseStamped.class, client);
        homePoseTopic.subscribe(handler);
    }

    /**
     * 全局路径规划
     */
    public void pathLineTopic() {
        MessageHandler<Path> handler = new MessageHandler<Path>() {
            @Override
            public void onMessage(Path message) {
                if (null != message) {
                    globalPlan = message;
                }
            }
        };
        Topic<Path> pathTopic = new Topic<>("/move_base/DWAPlannerROS/global_plan", Path.class, client);
        pathTopic.subscribe(handler);
    }


    /**
     * 局部路径规划
     */
    public void localPlanTopic(){
        MessageHandler<Path> handler = new MessageHandler<Path>() {
            @Override
            public void onMessage(Path message) {
                if (null != message) {
                    localPlan = message;
                }
            }
        };
        Topic<Path> pathTopic = new Topic<>("/move_base/DWAPlannerROS/local_plan", Path.class, client);
        pathTopic.subscribe(handler);
    }




    private void statusTopic() {
        MessageHandler<GoalStatusArray> handler = message -> {
            if (message == null) {
                status = -1;
            } else {
                if (message.status_list.length > 0) {
                    status = message.status_list[0].status;
                } else {
                    status = -1;
                }
            }

            switch (status) {
                case 1:
                    actionStatus = ActionStatus.ACTIVE;
                    isActionRunning = true;
                    if (onNavigateListener != null) {
                        onNavigateListener.running();
                    }
                    break;
                case 2:
                    actionStatus = ActionStatus.PREEMPTED;
                    break;
                case 3:
                    actionStatus = ActionStatus.SUCCEEDED;
                    if (onNavigateListener != null) {
                        //确保回调只执行一次
                        if (isActionRunning) {
                            onNavigateListener.finished();
                            isActionRunning = false;
                        }
                    }
                    break;
                case 4:
                    actionStatus = ActionStatus.ABORTED;
                    if (onNavigateListener != null) {
                        if (isActionRunning) {
                            onNavigateListener.error();
                            isActionRunning = false;
                        }
                    }
                    break;
                case 5:
                    actionStatus = ActionStatus.REJECTED;
                    break;
                case 6:
                    actionStatus = ActionStatus.PREEMPTING;
                    break;
                case 7:
                    actionStatus = ActionStatus.RECALLING;
                    break;
                case 8:
                    actionStatus = ActionStatus.RECALLED;
                    break;
                case 9:
                    actionStatus = ActionStatus.LOST;
                    if (onNavigateListener != null) {
                        if (isActionRunning) {
                            onNavigateListener.error();
                            isActionRunning = false;
                        }
                    }
                    break;
                case 0:
                default:
                    actionStatus = ActionStatus.PENDING;
                    break;
            }
        };
        statusTopic = new Topic<>("/move_base/status", GoalStatusArray.class, client);
        statusTopic.subscribe(handler);
    }

    /**
     * @return 急停按下返回data：1
     * 刹车按下返回data：2
     * 刹车急停同时按下返回data：3
     * 松开状态：0
     */
    private void buttonStatusTopic() {
        MessageHandler<UInt16> handler = message -> {
            if (message == null) {
                buttonStatus = -1;
            } else {
                buttonStatus = message.data;

            }
        };
        /**
         * 急停，刹车按钮状态
         */
        Topic<UInt16> buttonStatusTopic = new Topic<>("/nuxboard_button_states", UInt16.class, client);
        buttonStatusTopic.subscribe(handler);
    }


    /**
     * 获取地图数据 的服务订阅
     */
    public void subscribeGetMapDataTopic() {
        MessageHandler<OccupancyGrid> handler = message -> {
            if (message != null) {
                occupancyGrid = message;
            }
        };
        Topic<OccupancyGrid> mapDateTopic = new Topic<>("/map", OccupancyGrid.class, client);
        mapDateTopic.subscribe(handler);
    }

    /**
     * 获取地图数据 取消地图数据获取的订阅
     */
    public void unsubscribeGetMapDataTopic() {
        if (mapDateTopic != null) {
            mapDateTopic.unsubscribe();
        }
    }

    /**
     * @return 获取当前加载的地图的名称
     */
    public String getLoadMapName() {

        MessageHandler<DataString> handler = message -> {
            if (message == null) {
                currentMapName = "";
            } else {
                currentMapName = message.data;
            }
        };
        /**
         * 获取加载地图名称
         */
        Topic<DataString> currentMapNameTopic = new Topic<>("/current_map_name", DataString.class, client);
        currentMapNameTopic.subscribe(handler);
        return currentMapName;
    }

    /**
     * @return 获取当前加载的地图的名称
     */
    public void subscribeLaserScanData() {

        MessageHandler<LaserScan> handler = message -> {
            if (message != null) {
                laserScan = message;
            }
        };
        laserScanTopic = new Topic<>("/scan", LaserScan.class, client);
        laserScanTopic.subscribe(handler);
    }

    /**
     * @return 获取当前加载的地图的名称
     */
    public void unsubscribeLaserScanData() {
        if (laserScanTopic != null) {
            laserScanTopic.unsubscribe();
        }

    }

    /**
     * @return 获取地图名字列表
     */
    public List<String> getMapList() {
        List<String> mapList = new ArrayList<>();
        try {
            Service<GetMapListRequest, GetMapListResponse> mapListResponseService =
                    new Service<>("/map_manager/get_map_lists", GetMapListRequest.class, GetMapListResponse.class, client);
            MapInfo[] mapArray = mapListResponseService.callBlocking(new GetMapListRequest()).map_ids;
            int length = mapArray.length;
            if (length > 0) {
                for (int i = 0; i < length; i++) {
                    mapList.add(mapArray[i].name);
                }
            }

        } catch (InterruptedException ex) {
            System.out.println("Process was interrupted.");
        }
        return mapList;
    }

    /**
     * @return 获取地图列表数据
     */
    public List<MapInfo> getMapMetaDataList() {
        List<MapInfo> mapMetaDataList = new ArrayList<>();
        try {
            Service<GetMapListRequest, GetMapListResponse> mapListResponseService =
                    new Service<>("/map_manager/get_map_lists", GetMapListRequest.class, GetMapListResponse.class, client);
            MapInfo[] mapArray = mapListResponseService.callBlocking(new GetMapListRequest()).map_ids;
            if (mapArray != null) {
                int length = mapArray.length;
                if (length > 0) {
                    for (int i = 0; i < length; i++) {
                        mapMetaDataList.add(mapArray[i]);
                    }
                }
            }

        } catch (InterruptedException ex) {
            System.out.println("Process was interrupted.");
        }
        return mapMetaDataList;
    }

    /**
     * @return 加载指定地图
     */
    public boolean LoadMap(String mapName) {
        boolean result = false;
        try {
            LoadMapRequest loadMapRequest = new LoadMapRequest();
            DataString dataString = new DataString();
            dataString.data = mapName;
            loadMapRequest.filename = dataString;
            Service<LoadMapRequest, ResMsg> loadMapRequestService =
                    new Service<>("/map_manager/load_map", LoadMapRequest.class, ResMsg.class, client);
            ResMsg response = loadMapRequestService.callBlocking(loadMapRequest);
            result = response.success;
        } catch (Exception ex) {
            System.out.println("Process was interrupted.");
        }
        return result;
    }


    /**
     * @return 获取当前加载的地图的名称, 通过services
     */
    public String getLoadMapNameByServices() {
        List<String> mapList = new ArrayList<>();
        try {
            Service<GetMapListRequest, GetMapListResponse> mapListResponseService =
                    new Service<>("/map_manager/get_map_lists", GetMapListRequest.class, GetMapListResponse.class, client);
            currentMapName = mapListResponseService.callBlocking(new GetMapListRequest()).current_map_name;
        } catch (InterruptedException ex) {
            System.out.println("Process was interrupted.");
        }
        return currentMapName;
    }

    /**
     * 保存地图
     *
     * @param mapName 地图名称
     * @return 结果
     */
    public boolean onSaveMap(String mapName) {
        boolean result = false;
        try {
            SaveMapRequest saveMapRequest = new SaveMapRequest();
            DataString dataString = new DataString();
            dataString.data = mapName;
            saveMapRequest.folder = dataString;
            Service<SaveMapRequest, ResMsg> saveMapRequestService =
                    new Service<>("/map_manager/save_map", SaveMapRequest.class, ResMsg.class, client);
            ResMsg response = saveMapRequestService.callBlocking(saveMapRequest);
            result = response.success;
        } catch (Exception ex) {
            System.out.println("Process was interrupted.");
        }
        return result;
    }


    /**
     * 保存地图
     *
     * @param mapName 地图名称
     * @return 结果
     */
    public boolean onSaveMapTopic(String mapName) {
        boolean result = false;
        try {

            DataString dataString = new DataString();
            dataString.data = mapName;
            Topic<DataString> saveMapTopic = new Topic<>("/map_manager/save_map", DataString.class, client);
            saveMapTopic.publish(dataString);
            result = true;
        } catch (Exception ex) {
            System.out.println("Process was interrupted.");
        }
        return result;
    }


    /**
     * @return 获取巡航任务列表, 通过services
     */
    public List<NavTask> getNavTasks() {
        List<NavTask> mNavTaskList = new ArrayList<>();
        try {
            Service<GetTasksRequest, GetTasksResponse> taskListResponseService =
                    new Service<>("/map_manager/get_tasks", GetTasksRequest.class, GetTasksResponse.class, client);
            NavTaskList navTaskList = taskListResponseService.callBlocking(new GetTasksRequest()).task_list;
            NavTask[] navTaskArray = navTaskList.tasks;
            if (navTaskArray != null) {
                int length = navTaskArray.length;
                if (length > 0) {
                    for (int i = 0; i < length; i++) {
                        mNavTaskList.add(navTaskArray[i]);
                    }
                }
            }
        } catch (InterruptedException ex) {
            System.out.println("Process was interrupted.");
        }
        return mNavTaskList;
    }


    /**
     * 底盘系统控制:开机重启
     * 开启导航，开启建图
     * static final byte SYSTEM_OS_INVALID=-1;//无效命令
     * <p>
     * static final byte SYSTEM_CMD_UNKNOWN=0;//指令未知状态
     * static final byte SYSTEM_CMD_SHUTDOWN=1;//关机
     * static final byte SYSTEM_CMD_REBOOT=2;//重启
     * public int system_cmd;//系统命令
     * <p>
     * static final byte OS_NAV_STATUS_UNKNOWN=0; //状态未知
     * static final byte OS_NAV_STATUS_IDLE=1;//导航空闲状态
     * static final byte OS_NAV_STATUS_DASH=2;//底盘状态
     * static final byte OS_NAV_STATUS_MAPPING=4;//建图状态
     * static final byte OS_NAV_STATUS_NAV=8;//导航状态
     * static final byte OS_NAV_STATUS_DOCKING=16;//回充状态
     * public int os_status;//导航功能状态切换
     */
    public boolean settingOs(OSRequestCmd requestCmd) {
        boolean result = false;
        try {
            Service<OSRequestCmd, OSResponseResult> service =
                    new Service<>("/os_system_cmd", OSRequestCmd.class, OSResponseResult.class, client);
            OSResponseResult response = service.callBlocking(requestCmd);
            result = response.execute_state == 0 ? false : true;
        } catch (Exception ex) {
            System.out.println("Process was interrupted.");
        }
        return result;
    }

    /**
     * @return 回充电桩
     */
    public void goHome(OnNavigateListener onNavigateListener) {
        this.onNavigateListener = onNavigateListener;
        //actionCancel();
        Topic topic = new Topic<>("/go_home", PoseStamped.class, client);
        topic.publish(new PoseStamped());
    }

    /**
     * @return 取消回充电桩
     */
    public void cancelGoHome() {
        Topic topic = new Topic<>("/cancel_home", Empty.class, client);
        topic.publish(new Empty());
    }

    /**
     * @return 取消移动
     */
    public void actionCancel() {
        Topic topic = new Topic<>("/move_base/cancel", GoalID.class, client);
        topic.publish(new GoalID());
    }

    /**
     * @return 移动到指定位置x:-0.98, y:7.6,
     */
    public void moveTo(PoseStamped poseStamped) {
        moveTo(poseStamped, null);
    }

    /**
     * @param poseStamped        位置信息
     * @param onNavigateListener 回调
     */
    public void moveTo(PoseStamped poseStamped, OnNavigateListener onNavigateListener) {
        this.onNavigateListener = onNavigateListener;
        //actionCancel();
        Topic topic = new Topic<>("/move_base_simple/goal", PoseStamped.class, client);
        topic.publish(poseStamped);
        this.onNavigateListener = onNavigateListener;
    }

    /**
     * @param x
     * @param y
     * @param yaw 弧度信息
     */
    public void moveTo(double x, double y, double yaw) {
        moveTo(x, y, yaw, null);
    }

    /**
     * @param x
     * @param y
     * @param yaw                弧度
     * @param onNavigateListener 回调
     */
    public void moveTo(double x, double y, double yaw, OnNavigateListener onNavigateListener) {
        this.onNavigateListener = onNavigateListener;
        //actionCancel();
        PoseStamped poseStamped = new PoseStamped();
        Header header = new Header();
        header.frame_id = CommonConstant.MAP;
        Pose pose = new Pose();
        Point point = new Point();
        point.x = x;
        point.y = y;
        point.z = 0.0;
        Quaternion quaternion = new Quaternion();
        quaternion.x = 0;
        quaternion.y = 0;
        quaternion.z = Math.sin(yaw / 2);
        quaternion.w = Math.cos(yaw / 2);
        pose.position = point;
        pose.orientation = quaternion;
        poseStamped.pose = pose;
        poseStamped.header = header;
        moveTo(poseStamped, onNavigateListener);
    }

    /**
     * linear.x 区分正负号 x值为正表示前进;x值为负表示后退
     * angular.z 区分正负号;z值为正表示左转;z值为负表示右转
     */
    public void moveBy(Twist twist) {
        moveBy(twist, null);
    }

    /**
     * linear.x 区分正负号 x值为正表示前进;x值为负表示后退
     * angular.z 区分正负号;z值为正表示左转;z值为负表示右转
     */
    public void moveBy(Twist twist, OnNavigateListener onNavigateListener) {
        this.onNavigateListener = onNavigateListener;
        //actionCancel();
        Topic topic = new Topic<>("/yocs_cmd_vel_mux/input/keyop", Twist.class, client);
        topic.publish(twist);

    }

    public void moveBy(MoveDirection moveDirection) {
        moveBy(moveDirection, null);
    }

    public void moveBy(MoveDirection moveDirection, OnNavigateListener onNavigateListener) {
        this.onNavigateListener = onNavigateListener;
        //actionCancel();
        Twist twist = new Twist();
        Vector3 vectorAngular = new Vector3();
        Vector3 vectorLinear = new Vector3();
        switch (moveDirection) {
            case FORWARD:
                vectorLinear.x = 0.3;
                vectorLinear.z = 0.0;
                vectorAngular.z = 0.0;
                twist.linear = vectorLinear;
                twist.angular = vectorAngular;
                break;
            case BACKWARD:
                vectorLinear.x = -0.3;
                vectorLinear.z = 0.0;
                vectorAngular.z = 0.0;
                twist.linear = vectorLinear;
                twist.angular = vectorAngular;
                break;
            case TURN_LEFT:
                vectorLinear.x = 0.0;
                vectorLinear.z = 0.0;
                vectorAngular.z = 0.3;
                twist.linear = vectorLinear;
                twist.angular = vectorAngular;
                break;
            case TURN_RIGHT:
                vectorLinear.x = 0.0;
                vectorLinear.z = 0.0;
                vectorAngular.z = -0.3;
                twist.linear = vectorLinear;
                twist.angular = vectorAngular;
                break;

            case STOP:
            default:
                vectorLinear.x = 0.0;
                vectorLinear.z = 0.0;
                vectorAngular.z = 0.0;
                vectorAngular.x = 0.0;
                twist.linear = vectorLinear;
                twist.angular = vectorAngular;
                break;

        }
        moveBy(twist);
    }


    public void commercialMoveBy(Twist twist, OnNavigateListener onNavigateListener) {
        this.onNavigateListener = onNavigateListener;
        Topic topic = new Topic<>("/yocs_cmd_vel_mux/input/joystick", Twist.class, client);
        topic.publish(twist);
    }


    /**
     * linear.x 区分正负号 x值为正表示前进;x值为负表示后退
     * angular.z 区分正负号;z值为正表示左转;z值为负表示右转
     */
    public void commercialMoveBy(Twist twist) {
        commercialMoveBy(twist, null);
    }

    public void commercialMoveBy(MoveDirection moveDirection) {
        commercialMoveBy(moveDirection, null);
    }


    public void commercialMoveBy(MoveDirection moveDirection, OnNavigateListener onNavigateListener) {
        this.onNavigateListener = onNavigateListener;
        //actionCancel();
        Twist twist = new Twist();
        Vector3 vectorAngular = new Vector3();
        Vector3 vectorLinear = new Vector3();
        switch (moveDirection) {
            case FORWARD:
                vectorLinear.x = 0.3;
                vectorLinear.z = 0.0;
                vectorAngular.z = 0.0;
                twist.linear = vectorLinear;
                twist.angular = vectorAngular;
                break;
            case BACKWARD:
                vectorLinear.x = -0.3;
                vectorLinear.z = 0.0;
                vectorAngular.z = 0.0;
                twist.linear = vectorLinear;
                twist.angular = vectorAngular;
                break;
            case TURN_LEFT:
                vectorLinear.x = 0.0;
                vectorLinear.z = 0.0;
                vectorAngular.z = 0.3;
                twist.linear = vectorLinear;
                twist.angular = vectorAngular;
                break;
            case TURN_RIGHT:
                vectorLinear.x = 0.0;
                vectorLinear.z = 0.0;
                vectorAngular.z = -0.3;
                twist.linear = vectorLinear;
                twist.angular = vectorAngular;
                break;
            case STOP:
                vectorLinear.x = 0.0;
                vectorLinear.z = 0.0;
                vectorAngular.z = 0.0;
                vectorAngular.x = 0.0;
                twist.linear = vectorLinear;
                twist.angular = vectorAngular;
                break;
        }
        commercialMoveBy(twist);
    }


    public void rotateTo(float angle) {
        rotateTo(angle);
    }

    /**
     * @param angle 传0°~360°范围的角度
     */
    public void rotateTo(float angle, OnNavigateListener onNavigateListener) {
        this.onNavigateListener = onNavigateListener;
        Float32 float32 = new Float32();
        float32.data = angle;
        Topic topic = new Topic<>("/correct_angle/fixed_angle", Float32.class, client);
        topic.publish(float32);

    }

    /**
     * 设置线速度
     * 参数名：
     * /move_base/DWAPlannerROS/max_vel_x //线速度
     * /move_base/DWAPlannerROS/max_vel_theta //角速度
     */
    public void setSpeed(String speedValue) {
        SetParamRequest setParamRequest = new SetParamRequest();
        setParamRequest.name = "/move_base/DWAPlannerROS/max_vel_x";
        setParamRequest.value = speedValue;
        Topic topic = new Topic<>("/rosapi/set_param", SetParamRequest.class, client);
        topic.publish(setParamRequest);

    }

    /**
     * 设置角速度
     * 参数名：
     * /move_base/DWAPlannerROS/max_vel_x //线速度
     * /move_base/DWAPlannerROS/max_vel_theta //角速度
     */
    public void setRotateSpeed(String rotateSpeedValue) {
        SetParamRequest setParamRequest = new SetParamRequest();
        setParamRequest.name = "/move_base/DWAPlannerROS/max_vel_theta";
        setParamRequest.value = rotateSpeedValue;
        Topic topic = new Topic<>("/rosapi/set_param", SetParamRequest.class, client);
        topic.publish(setParamRequest);

    }

    /**
     * @return 重定位
     */
    public void resetPosition(PoseWithCovarianceStamped poseWithCovarianceStamped) {

        Topic topic = new Topic<>("/initialpose", PoseWithCovarianceStamped.class, client);
        topic.publish(poseWithCovarianceStamped);
    }

    public void setPose(double x, double y, double yaw) {
        PoseWithCovarianceStamped poseWithCovarianceStamped = new PoseWithCovarianceStamped();
        PoseWithCovariance poseWithCovariance = new PoseWithCovariance();
        Header header = new Header();
        header.frame_id = CommonConstant.MAP;
        Pose pose = new Pose();
        Point point = new Point();
        point.x = x;
        point.y = y;
        point.z = 0.0;
        Quaternion quaternion = new Quaternion();
        quaternion.x = 0;
        quaternion.y = 0;
        quaternion.z = Math.sin(yaw / 2);
        quaternion.w = Math.cos(yaw / 2);
        pose.position = point;
        pose.orientation = quaternion;
        poseWithCovariance.pose = pose;
        poseWithCovarianceStamped.header = header;
        poseWithCovarianceStamped.pose = poseWithCovariance;
        resetPosition(poseWithCovarianceStamped);
    }


    public double getSpeed() {
        return speed;
    }

    public double getRotateSpeed() {
        return rotateSpeed;
    }

    public int getBatteryPercentage() {
        return batteryPercentage;
    }


    public int getButtonStatus() {
        return buttonStatus;
    }

    public Pose getPose() {
        return robotPose;
    }

    public PoseStamped getHomePose() {
        return homePose;
    }

    public int getPowerSupplyStatus() {
        return powerSupplyStatus;
    }

    public int getStatus() {
        return status;
    }

    public ActionStatus getActionStatus() {
        return actionStatus;
    }

    public OccupancyGrid getMapData() {
        return occupancyGrid;
    }

    public LaserScan getLaserScanData() {
        return laserScan;
    }

    public Path getGlobalPath() {
        return globalPlan;
    }

    public Path getLocalPlan() {
        return localPlan;
    }
}