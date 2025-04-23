package com.zhh.slam.mapview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;


import com.zhh.slam.bean.LocalPose;
import com.zhh.slam.utils.RadianUtil;

import java.lang.ref.WeakReference;

import static android.graphics.Paint.Cap.ROUND;

/**
 * 绘制小车(设备)在地图中所在位姿
 */
public class DeviceView extends SlamwareBaseView {
    private static final String TAG = DeviceView.class.getSimpleName();
    /**
     * 小车标出的圆形范围颜色,默认为#09898FE
     */
    public static final int RADIUS_COLOR = 0xC09898FE;
    /**
     * 小车标出的圆形半径大小
     */
    private float mDeviceRadius = 0.18f;
    /**
     * 小车的位姿信息
     */
    private LocalPose mDevicePose;
    private Paint mPaintRadius;
    /**
     * 小车画笔
     */
    private Paint mPaintOrientation;
    private Paint mPaintWhite;

    public DeviceView(Context context, WeakReference<MapView> parent) {
        super(context, parent);
        mDevicePose = new LocalPose();

        mPaintOrientation = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintOrientation.setColor(Color.RED);

        mPaintRadius = new Paint();
        mPaintRadius.setStrokeCap(ROUND);
        mPaintRadius.setAntiAlias(true);
        mPaintRadius.setColor(RADIUS_COLOR);

        mPaintWhite = new Paint();
        mPaintWhite.setColor(RADIUS_COLOR);
        mPaintWhite.setAntiAlias(true);
        mPaintWhite.setStrokeWidth(1f);
    }

    public void setDevicePose(LocalPose pose) {
        if (pose != null) {
            mDevicePose = pose;
            postInvalidate();
        }

    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float x = mDevicePose.getX();
        float y = mDevicePose.getY();
        float yaw = mDevicePose.getYaw();

        //获取小车在地图中显示的实际位姿
        PointF center = mParent.get().mapCoordinateToWidthCoordinateF(x, y);
        // 小车标注圆形半径
        float widthRadius = mParent.get().mapDistanceToWidthDistance(mDeviceRadius);

        mPaintRadius.setStrokeWidth(widthRadius * 5f);
        canvas.drawPoint(center.x, center.y, mPaintRadius);

//        PointF originalLeft = new PointF(center.x - widthRadius * 0.25f, center.y - widthRadius * 0.6f);
//        PointF originalRight = new PointF(center.x - widthRadius * 0.25f, center.y + widthRadius * 0.6f);
//        PointF originalTop = new PointF(center.x + widthRadius, center.y);
//        PointF originalBottom = new PointF(center.x - widthRadius * 0.5f, center.y);


        PointF originalLeft = new PointF(center.x - widthRadius * 1f, center.y - widthRadius * 1f);
        PointF originalRight = new PointF(center.x - widthRadius * 1f, center.y + widthRadius * 1f);
        PointF originalTop = new PointF(center.x + widthRadius * 1.5f, center.y);
        PointF originalBottom = new PointF(center.x - widthRadius * 1.25f, center.y);


        Matrix matrix = new Matrix();
        matrix.postRotate(RadianUtil.toAngel(-yaw) + mRotation, center.x, center.y);

        float[] points = new float[]{originalTop.x, originalTop.y, originalLeft.x, originalLeft.y, originalLeft.x * 2 - originalBottom.x, originalBottom.y, originalRight.x, originalRight.y};

        matrix.mapPoints(points);

        Path path = new Path();
        path.moveTo(points[0], points[1]);

        path.lineTo(points[2], points[3]);
        path.lineTo(points[4], points[5]);
        path.lineTo(points[6], points[7]);
        path.lineTo(points[0], points[1]);
        canvas.drawPath(path, mPaintOrientation);


        canvas.drawLine(points[0], points[1], points[4], points[5], mPaintWhite);
//        canvas.drawLine(points[2], points[3], center.x, center.y, mPaintWhite);
//        canvas.drawLine(points[6], points[7], center.x, center.y, mPaintWhite);

    }

}
