package com.zhh.slam.mapview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;


import com.zhh.slam.bean.LocalPose;

import java.lang.ref.WeakReference;


/**
 * 充电桩（充点电）位姿信息
 */
public class HomeDockView extends SlamwareBaseView {
    private LocalPose mHomePose;
    private Paint mPaint;

    public HomeDockView(Context context, WeakReference<MapView> parent) {
        super(context, parent);
        mHomePose = null;

        mPaint = new Paint();
        mPaint.setColor(Color.GREEN);
        mPaint.setStrokeWidth(1f);
    }

    public void setHomePose(LocalPose pose) {
        mHomePose = pose;
        if (mHomePose != null) {
            postInvalidate();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mHomePose != null) {
            float x = mHomePose.getX();
            float y = mHomePose.getY();
            PointF center = mParent.get().mapCoordinateToWidthCoordinateF(x, y);
            canvas.drawCircle(center.x, center.y, getScale() * 3, mPaint);
        }
    }

}
