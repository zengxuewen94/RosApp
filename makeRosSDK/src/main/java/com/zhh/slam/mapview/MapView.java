package com.zhh.slam.mapview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;


import com.zhh.slam.bean.Line;
import com.zhh.slam.bean.LocalLaserScan;
import com.zhh.slam.bean.LocalPose;
import com.zhh.slam.bean.Location;
import com.zhh.slam.bean.Path;
import com.zhh.slam.mapview.mapdata.MapDataCache;
import com.zhh.slam.robot.Map;
import com.zhh.slam.utils.MathUtils;
import com.zhh.slam.utils.RadianUtil;
import com.zhh.slam.utils.SlamGestureDetector;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * 地图画布
 * 将在此画布中绘制slam地图、小车位姿、充电桩位姿、激光雷达数据、路径规划、位姿点信息等
 */
public class MapView extends FrameLayout implements SlamGestureDetector.OnRPGestureListener {
    private final static String TAG = MapView.class.getName();


    private Matrix mOuterMatrix = new Matrix();
    private int VIEW_WIDTH = 0;
    private int VIEW_HEIGHT = 0;
    private int defaultBackGroundColor = 0xC0C0C0;
    private float mMapScale = 1;
    private float mMaxMapScale = 400;
    private float mMinMapScale = 0.1f;
    private boolean isDrawRobotView = true;
    private boolean isDrawLaserScanView = true;
    private boolean isDrawHomeView = true;
    private ThreadPoolExecutor threadPoolExecutor;
    private MapDataCache mMapData;
    private WeakReference<MapView> mMapView;
    private List<SlamwareBaseView> mapLayers = new CopyOnWriteArrayList<>();
    private SlamMapView mSlamMapView;
    private VirtualLineView mWallView;
    private VirtualLineView mTrackView;
    private LaserScanView mLaserScanView;
    private RemainingMilestonesView mRemainingMilestonesView;
    private RemainingPathView mRemainingPathView;
    private DeviceView mDeviceView;
    private LocationLabelView mLocationLabelView;
    private HomeDockView mHomeDockView;


    private ISingleTapListener mSingleTapListener;
    private SlamGestureDetector mGestureDetector;

    public MapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MapView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        setDefaultBackground(defaultBackGroundColor);
        mOuterMatrix = new Matrix();
        mGestureDetector = new SlamGestureDetector(this, this);
        initView();
    }

    public void setDefaultBackground(int colorId) {
        setBackgroundColor(colorId);
    }

    public void setDrawRobotView(boolean drawRobotView) {
        isDrawRobotView = drawRobotView;
    }

    public void setDrawLaserScanView(boolean drawLaserScanView) {
        isDrawLaserScanView = drawLaserScanView;
    }

    public void setDrawHomeView(boolean drawHomeView) {
        isDrawHomeView = drawHomeView;
    }

    private void initView() {
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        mMapView = new WeakReference<>(this);
        mMapData = new MapDataCache();

        mSlamMapView = new SlamMapView(getContext());
        mWallView = new VirtualLineView(getContext(), mMapView, Color.RED);
        mTrackView = new VirtualLineView(getContext(), mMapView, Color.GREEN);
        mLaserScanView = new LaserScanView(getContext(), mMapView);
        mRemainingMilestonesView = new RemainingMilestonesView(getContext(), mMapView);
        mRemainingPathView = new RemainingPathView(getContext(), mMapView);
        mDeviceView = new DeviceView(getContext(), mMapView);
        mHomeDockView = new HomeDockView(getContext(), mMapView);

        mLocationLabelView = new LocationLabelView(getContext(), mMapView);

        addView(mSlamMapView, lp);
        addMapLayers(mWallView);
        addMapLayers(mTrackView);
        addMapLayers(mHomeDockView);
        addMapLayers(mLaserScanView);
        addMapLayers(mRemainingPathView);
        addMapLayers(mRemainingMilestonesView);
        addMapLayers(mDeviceView);
        addMapLayers(mLocationLabelView);
        setCentred();

    }


    private void onCreateThreadPoolExecutor() {
        if (threadPoolExecutor == null) {
            BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>(10);
            threadPoolExecutor = new ThreadPoolExecutor(
                    1,
                    10,
                    60L,
                    TimeUnit.SECONDS,
                    workQueue,
                    Executors.defaultThreadFactory(),
                    new ThreadPoolExecutor.AbortPolicy() // 拒绝策略
            );
        }
    }

    private int mCnt = 0;

    public void setSlamLocationBeans(List<Location> beanList) {
        mLocationLabelView.setLocationList(beanList);
    }

    public void setMap(Map map) {
        if (map != null) {
            onCreateThreadPoolExecutor();
            threadPoolExecutor.execute(() -> {
                mMapData = new MapDataCache(map);
                mSlamMapView.updateTiles(mMapData);
                if (mCnt++ == 0) {
                    setCentred();
                }
            });
        }
    }

    public void setViewWalls(List<Line> walls) {
        mWallView.setLines(walls);
    }

    public void setViewTracks(List<Line> tracks) {
        mTrackView.setLines(tracks);
    }

    public void setLaserScan(LocalLaserScan laserScan) {
        mLaserScanView.updateLaserScan(laserScan);
    }

    public void setRemainingMilestones(Path remainingMilestones) {
        mRemainingMilestonesView.updateRemainingMilestones(remainingMilestones);
    }

    public void setRemainingPath(Path remainingPath) {
        mRemainingPathView.updateRemainingPath(remainingPath);
    }


    /**
     * 设置机器人位姿
     *
     * @param pose
     */
    public void setRobotPose(LocalPose pose) {
        mDeviceView.setDevicePose(pose);
    }

    /**
     * 设置充电点/充电桩位姿
     *
     * @param homePose
     */
    public void setHomePose(LocalPose homePose) {
        mHomeDockView.setHomePose(homePose);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        return mGestureDetector.onTouchEvent(event);
    }

    @Override
    public void onMapTap(MotionEvent event) {
        singleTap(event);
    }

    @Override
    public void onMapPinch(float factor, PointF center) {
        setScale(factor, center.x, center.y);
    }

    @Override
    public void onMapMove(int distanceX, int distanceY) {
        setTransition(distanceX, distanceY);
    }

    @Override
    public void onMapRotate(float factor, PointF center) {
        setRotation(factor, (int) center.x, (int) center.y);
    }

    private void setRotation(float factor, int cx, int cy) {
        mOuterMatrix.postRotate(RadianUtil.toAngel(factor), cx, cy);
        setMatrixWithRotation(mOuterMatrix, factor);
    }

    private void setTransition(int dx, int dy) {
        mOuterMatrix.postTranslate(dx, dy);
        this.setMatrix(mOuterMatrix);
    }

    private void setScale(float factor, float cx, float cy) {
        float scale = mMapScale * factor;
        if (scale > mMaxMapScale || scale < mMinMapScale) {
            return;
        }
        mMapScale = scale;
        mOuterMatrix.postScale(factor, factor, cx, cy);
        setMatrixWithScale(mOuterMatrix, mMapScale);
    }


    private void singleTap(MotionEvent event) {
        if (mSingleTapListener != null) {
            mSingleTapListener.onSingleTapListener(event);
        }
    }

    private void setMatrix(Matrix matrix) {
        mSlamMapView.setMatrix(matrix);
        for (SlamwareBaseView mapLayer : mapLayers) {
            mapLayer.setMatrix(matrix);
        }

        postInvalidate();
    }

    private void setMatrixWithScale(Matrix matrix, float scale) {
        this.mOuterMatrix = matrix;
        this.mMapScale = scale;
        mSlamMapView.setMatrix(matrix);

        for (SlamwareBaseView mapLayer : mapLayers) {
            mapLayer.setMatrixWithScale(matrix, scale);
        }
    }

    private void setMatrixWithScaleAndRotation(Matrix matrix, float scale, float rotation) {
        this.mOuterMatrix = matrix;
        this.mMapScale = scale;
        mSlamMapView.setMatrix(matrix);

        for (SlamwareBaseView mapLayer : mapLayers) {
            mapLayer.setMatrixWithScale(matrix, scale);
            mapLayer.mRotation = rotation;
        }
    }

    private void setMatrixWithRotation(Matrix matrix, float rotation) {
        this.mOuterMatrix = matrix;
        mSlamMapView.setMatrix(matrix);

        for (SlamwareBaseView mapLayer : mapLayers) {
            mapLayer.setMatrixWithRotation(matrix, rotation);
        }
    }

    public void setCentred() {
        RectF scaledRect = new RectF();

        if (VIEW_WIDTH == 0 || VIEW_HEIGHT == 0) {
            this.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    VIEW_HEIGHT = getHeight();
                    VIEW_WIDTH = getWidth();
                    getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    setCentred();
                }
            });
            return;
        }

        if (mMapData != null) {
            int iWidth = mMapData.getDimension().getWidth();
            int iHeight = mMapData.getDimension().getHeight();

            MathUtils.calculateScaledRectInContainer(new RectF(0, 0, VIEW_WIDTH, VIEW_HEIGHT), iWidth, iHeight, ImageView.ScaleType.FIT_CENTER, scaledRect);
            float scale = scaledRect.width() / (float) iWidth;
            mMinMapScale = scale / 4;
            mMapScale = scale;

            mOuterMatrix = new Matrix();
            mOuterMatrix.postScale(mMapScale, mMapScale);
            mOuterMatrix.postTranslate((VIEW_WIDTH - mMapScale * iWidth) / 2, (VIEW_HEIGHT - mMapScale * iHeight) / 2);
            setMatrixWithScaleAndRotation(mOuterMatrix, mMapScale, 0f);
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        VIEW_WIDTH = MeasureSpec.getSize(widthMeasureSpec);
        VIEW_HEIGHT = MeasureSpec.getSize(heightMeasureSpec);
    }

    protected com.zhh.slam.bean.PointF widthCoordinateToMapCoordinate(float x, float y) {
        Matrix m = mOuterMatrix;
        float[] eventPoint = new float[]{x, y};
        float[] values = MathUtils.inverseMatrixPoint(m, eventPoint);
        return mMapData.mapPixelCoordinateToMapCoordinate(new PointF(values[0], values[1]));
    }

    public Point widgetCoordinateToMapPixelCoordinate(float x, float y) {
        Matrix m = mOuterMatrix;
        float[] eventPoint = new float[]{x, y};
        float[] values = MathUtils.inverseMatrixPoint(m, eventPoint);
        return new Point((int) values[0], (int) values[1]);
    }

    public PointF widgetCoordinateToMapPixelCoordinateF(float x, float y) {
        Matrix m = mOuterMatrix;
        float[] eventPoint = new float[]{x, y};
        float[] values = MathUtils.inverseMatrixPoint(m, eventPoint);
        return new PointF(values[0], values[1]);
    }

    public com.zhh.slam.bean.PointF widgetCoordinateToMapCoordinate(PointF widgetPointF) {
        Matrix m = mOuterMatrix;
        float[] points = new float[]{widgetPointF.x, widgetPointF.y};
        float[] values = MathUtils.inverseMatrixPoint(m, points);
        com.zhh.slam.bean.PointF point = mMapData.mapPixelCoordinateToMapCoordinate(new PointF(values[0], values[1]));
        return point;
    }

    public com.zhh.slam.bean.PointF widgetCoordinateToMapCoordinate(float x, float y) {
        Matrix m = mOuterMatrix;
        float[] points = new float[]{x, y};
        float[] values = MathUtils.inverseMatrixPoint(m, points);
        com.zhh.slam.bean.PointF point = mMapData.mapPixelCoordinateToMapCoordinate(new PointF(values[0], values[1]));
        return point;
    }

    public Point widgetCoordinateToMapPixelCoordinate(int x, int y) {
        Matrix m = mOuterMatrix;
        float[] points = new float[]{x, y};
        float[] values = MathUtils.inverseMatrixPoint(m, points);
        return new Point((int) values[0], (int) values[1]);
    }

    public float mapDistanceToWidthDistance(float distance) {
        synchronized (mMapData) {
            float mapPixelDistance = mMapData.mapDistanceToMapPixelDistance(distance);
            return mapPixelDistance * mMapScale;
        }
    }

    public PointF mapCoordinateToWidthCoordinateF(float x, float y) {
        synchronized (mMapData) {
            PointF mapPixelPointF = mMapData.mapCoordinateToMapPixelCoordinateF(x, y);
            return mapPixelCoordinateToMapWidthCoordinateF(mapPixelPointF);
        }
    }

    public PointF mapCoordinateToWidthCoordinateF(com.zhh.slam.bean.PointF mapPointF) {
        synchronized (mMapData) {
            PointF mapPixelPointF = mMapData.mapCoordinateToMapPixelCoordinateF(mapPointF.getX(), mapPointF.getY());
            return mapPixelCoordinateToMapWidthCoordinateF(mapPixelPointF);
        }
    }

    public Point mapCoordinateToWidthCoordinate(com.zhh.slam.bean.PointF mapPointF) {
        synchronized (mMapData) {
            PointF mapPixelPointF = mMapData.mapCoordinateToMapPixelCoordinateF(mapPointF.getX(), mapPointF.getY());
            PointF p = mapPixelCoordinateToMapWidthCoordinateF(mapPixelPointF);
            return new Point((int) p.x, (int) p.y);
        }

    }

    public PointF mapPixelCoordinateToMapWidthCoordinateF(PointF mapPixelPointF) {
        Matrix m = mOuterMatrix;
        float[] points = new float[]{mapPixelPointF.x, mapPixelPointF.y};
        m.mapPoints(points);
        return new PointF(points[0], points[1]);
    }

    public PointF mapCoordinateToMapPixelCoordinateF(com.zhh.slam.bean.PointF mapPointF) {
        PointF mapPixelPointF = mMapData.mapCoordinateToMapPixelCoordinateF(mapPointF.getX(), mapPointF.getY());
        return mapPixelPointF;
    }

    public PointF mapCoordinateToMapPixelCoordinateF(float x, float y) {
        if (mMapData == null) {
            return null;
        } else {
            return mMapData.mapCoordinateToMapPixelCoordinateF(x, y);
        }
    }


    @Override
    public void invalidate() {
        super.postInvalidate();
        mSlamMapView.postInvalidate();

        for (SlamwareBaseView mapLayer : mapLayers) {
            mapLayer.postInvalidate();
        }

    }

    public void addMapLayers(SlamwareBaseView mapLayer) {
        if (mapLayer != null && !mapLayers.contains(mapLayer)) {
            mapLayers.add(mapLayer);
            addView(mapLayer, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }

    }

    public void setSingleTapListener(ISingleTapListener singleTapListener) {
        mSingleTapListener = singleTapListener;
    }

    public interface ISingleTapListener {
        void onSingleTapListener(MotionEvent event);
    }

    @Override
    protected void onDetachedFromWindow() {
        if (null != mapLayers) {
            mapLayers.clear();
            mapLayers = null;
        }
        if (null != threadPoolExecutor) {
            threadPoolExecutor.shutdownNow();
        }

        super.onDetachedFromWindow();
    }

    public void setSlamMapViewShow(int visibility) {
        mSlamMapView.setVisibility(visibility);
    }

}
