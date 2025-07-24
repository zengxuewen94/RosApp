package com.zhh.rosApp.viewmodel;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.OnLifecycleEvent;


import com.zhh.androidrosbridgeclient.action.MoveDirection;
import com.zhh.androidrosbridgeclient.interfaces.OnNavigateListener;
import com.zhh.androidrosbridgeclient.ros.message.nav_msgs.OccupancyGrid;
import com.zhh.rosApp.R;
import com.zhh.rosApp.manger.RosDeviceManager;

import java.awt.font.TextAttribute;
import java.io.FileInputStream;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class MainViewModel extends AndroidViewModel implements LifecycleObserver {

    private String uriString = "ws://192.168.0.200:9090";
    public MutableLiveData<OccupancyGrid> obOccupancyGrid = new MutableLiveData<>();


    public MutableLiveData<OccupancyGrid> getObOccupancyGrid() {
        return obOccupancyGrid;
    }

    public MutableLiveData<String> connectIp = new MutableLiveData<>();

    // 底盘连接成功
    private boolean connectSuccess;
    //是否需要连接机器人
    private boolean needConnectRos = true;
    //连接失败的次数
    private int connectRosFailedTimes = 0;
    private boolean live = true;

    private Context context;

    public MainViewModel(@NonNull Application application) {
        super(application);
        context = application.getBaseContext();
    }


    @OnLifecycleEvent(value = Lifecycle.Event.ON_CREATE)
    public void onCreate() {
        connectROS();
    }

    public void onClick(View view) {
        if (R.id.bt_m_connect == view.getId()) {
            if (TextUtils.isEmpty(connectIp.getValue())) {
                showToast("请填写ip地址");
                return;
            }
            uriString = "ws://" + connectIp.getValue();
            connectROS();
        }

        if (!connectSuccess) {
            return;
        }
        if (R.id.bt_m_left == view.getId()) {
            RosDeviceManager.INSTANCE.moveBy(MoveDirection.TURN_LEFT, onNavigateListener);
        } else if (R.id.bt_m_right == view.getId()) {
            RosDeviceManager.INSTANCE.moveBy(MoveDirection.TURN_RIGHT, onNavigateListener);
        } else if (R.id.bt_m_forward == view.getId()) {
            RosDeviceManager.INSTANCE.moveBy(MoveDirection.FORWARD, onNavigateListener);
        } else if (R.id.bt_m_backward == view.getId()) {
            RosDeviceManager.INSTANCE.moveBy(MoveDirection.BACKWARD, onNavigateListener);
        }
    }

    /**
     * 连接ROS底盘
     */
    private void connectROS() {
        Observable.create(emitter -> {
            //这里失败后连接两次不成功放弃连接
            while (needConnectRos && connectRosFailedTimes < 2) {
                connectSuccess = RosDeviceManager.INSTANCE.connect(uriString);
                if (connectSuccess) {
                    needConnectRos = false;
                    //订阅地图数据
                    RosDeviceManager.INSTANCE.subscribeGetMapDataTopic();
                    RosDeviceManager.INSTANCE.subscribeLaserScanData();
                    RosDeviceManager.INSTANCE.subscribeLocalLine();
                }
                emitter.onNext(connectSuccess);
                Thread.sleep(2000);
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Observer<Object>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull Object o) {
                if (connectSuccess) {
                    connectRosSuccess();
                } else {
                    connectRosFailedTimes++;
                    if (connectRosFailedTimes >= 2) {
                        needConnectRos = true;
                    }
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

    }


    private void connectRosSuccess() {
        connectSuccess = true;
        //直接new 了一个线程，开发中不建议这么写
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (live) {
                    try {
                        Thread.sleep(300);
                        OccupancyGrid occupancyGrid = RosDeviceManager.INSTANCE.getMapData();
                        if (null != occupancyGrid) {
                            obOccupancyGrid.postValue(occupancyGrid);
                            live = false;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        }).start();
    }


    @Override
    protected void onCleared() {
        super.onCleared();
    }


    private void showToast(CharSequence charSequence) {
        Toast.makeText(context, charSequence, Toast.LENGTH_SHORT).show();
    }

    OnNavigateListener onNavigateListener = new OnNavigateListener() {
        @Override
        public void waitingForStart() {

        }

        @Override
        public void running() {

        }

        @Override
        public void finished() {

        }

        @Override
        public void paused() {

        }

        @Override
        public void stopped() {

        }

        @Override
        public void error() {

        }
    };
}
