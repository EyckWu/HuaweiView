package com.eyck.huaweiview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Eyck on 2017/8/28.
 */

public class PathView extends View {
    private Paint mPaint;
    private Path mPath;

    public PathView(Context context) {
        this(context,null);
    }

    public PathView(Context context, AttributeSet attrs) {
        this(context, attrs,-1);
    }

    public PathView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.STROKE);//设置不填充
        mPaint.setStrokeWidth(3);
        mPath = new Path();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(Color.parseColor("#ff0000"));
        //为了增强效果对比，我们绘制出三个控制点之间对应的直线
        RectF rectF = new RectF(100,100,300,300);
        mPath.addRect(rectF, Path.Direction.CW);
        mPath.addArc(rectF,0,360);
        canvas.drawPath(mPath,mPaint);
    }
}
