package com.zhh.slam.mapview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.view.View;

import com.zhh.slam.bean.LocalLaserScan;
import com.zhh.slam.bean.LocalPose;
import com.zhh.slam.robot.LaserPoint;


import java.lang.ref.WeakReference;
import java.util.Vector;

import static android.graphics.Paint.Style.FILL;
import static android.graphics.Paint.Style.STROKE;

/**
 * 激光雷达层
 */
public class LaserScanView extends SlamwareBaseView {

    private LocalLaserScan mLaserScan;
    private Paint mPaintLaserPoint;
    private Paint mPaintLaserArea;
    private Paint mPaintLaserAreaEdge;
    public LaserScanView(Context context, WeakReference<MapView> parent) {
        super(context, parent);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mParent = parent;

        mPaintLaserPoint = new Paint();
        mPaintLaserPoint.setColor(Color.RED);
        mPaintLaserPoint.setStrokeWidth(4);
        mPaintLaserPoint.setStyle(STROKE);

        mPaintLaserArea = new Paint();
        mPaintLaserArea.setARGB(50, 150, 0, 0);
        mPaintLaserArea.setStrokeWidth(1);
        mPaintLaserArea.setStyle(FILL);

        mPaintLaserAreaEdge = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintLaserAreaEdge.setARGB(190, 255, 0, 0);
        mPaintLaserAreaEdge.setStrokeWidth(0.3f);
        mPaintLaserAreaEdge.setStyle(STROKE);
    }

    public void updateLaserScan(LocalLaserScan laserScan) {

        if (laserScan != null) {

            mLaserScan = laserScan;
            postInvalidate();
        }
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mLaserScan == null) {
            return;
        }

        PointF uiCenter;
        PointF centerPosition = new PointF();
        Path pathLaserArea = new Path();

        LocalPose robotPose = mLaserScan.getPose();
        PointF uiRobotPoint = mParent.get().mapCoordinateToWidthCoordinateF(robotPose.getX(), robotPose.getY());

        pathLaserArea.moveTo(uiRobotPoint.x, uiRobotPoint.y);

        Vector<LaserPoint> scanPoints = mLaserScan.getLaserPoints();

        for (LaserPoint scanPoint : scanPoints) {
            if (scanPoint.getAngle() < 0.001) {
                continue;
            }

            double r = scanPoint.getAngle();
            double physicalX = robotPose.getX() + r * Math.cos(scanPoint.getDistance());
            double physicalY = robotPose.getY() + r * Math.sin(scanPoint.getDistance());

            centerPosition.x = ((float) physicalX);
            centerPosition.y = ((float) physicalY);

            uiCenter = mParent.get().mapCoordinateToWidthCoordinateF(centerPosition.x, centerPosition.y);

            canvas.drawPoint(uiCenter.x, uiCenter.y, mPaintLaserPoint);

            pathLaserArea.lineTo(uiCenter.x, uiCenter.y);
        }

        pathLaserArea.lineTo(uiRobotPoint.x, uiRobotPoint.y);
        pathLaserArea.close();

        canvas.drawPath(pathLaserArea, mPaintLaserArea);
        canvas.drawPath(pathLaserArea, mPaintLaserAreaEdge);
    }
}
