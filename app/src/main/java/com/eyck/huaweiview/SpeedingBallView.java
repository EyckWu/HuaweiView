package com.eyck.huaweiview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Eyck on 2017/8/28.
 */

public class SpeedingBallView extends View {

    private static final String TAG = SpeedingBallView.class.getSimpleName();

    private int d;
    private RectF oval;
    private float startAngle = 120;
    private float sweepAngle = 300;
    private Paint mPaint;
    private float radius;
    private float lineLength;
    private int percent = 0;
    private int totalPercent = 30;
    private int lastTotalPercent = 30;
    private boolean isWiseColck = true;
    private boolean isRunning = false;

    private float count = 0;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 0) {
                count = (float) (count + 0.1);
                if(isWiseColck) {
                    if(percent < totalPercent) {
                        percent++;
                        postInvalidate();
                    }else {
                        isRunning = false;
                        postInvalidate();
                    }
                }else {
                    isRunning = true;
                    if(percent > 0) {
                        percent--;
                        postInvalidate();
                    }else{
                        isWiseColck = true;
                        postInvalidate();
                    }
                }
            }
        }
    };
    private Paint linePaint;
    private float[] wave1 ;
    private float[] wave2 ;
    private float prase = 0;
    private double h = 100;
    private float ballRadius;
    private Path path;

    public SpeedingBallView(Context context) {
        this(context,null);
    }

    public SpeedingBallView(Context context, AttributeSet attrs) {
        this(context, attrs , -1);
    }

    public SpeedingBallView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.STROKE);//设置不填充
        mPaint.setStrokeWidth(3);
        path = new Path();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
//        int width = Math.max(MeasureSpec.getSize(widthMeasureSpec),50);
//        int height = Math.max(MeasureSpec.getSize(heightMeasureSpec),50);
        d = Math.min(width,height);
        oval = new RectF(0,0,d,d);
        radius = d / 2;
        lineLength = radius/5;
        ballRadius = radius-lineLength-lineLength/6;
        wave1 = new float[d];
        wave2 = new float[d];

        setMeasuredDimension(d, d);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawArc(oval,startAngle,sweepAngle,false,mPaint);
//        if(isRunning) {
            drawLine(canvas);
            drawBall(canvas);
//        }
//        drawWaves(canvas);
    }

    private void drawBall(Canvas canvas) {
        canvas.save();

        Paint ballPaint =new Paint();
        ballPaint.setAntiAlias(true);
//        ballPaint.setStrokeWidth(3);
//        ballPaint.setStyle(Paint.Style.STROKE);
//        int red = 255 * (percent) / 100;
//        int green = 255 - red;
        ballPaint.setAlpha(53);
        ballPaint.setARGB(53,235,235,235);
//        ballPaint.setColor(Color.WHITE);
        canvas.drawCircle(radius,radius,ballRadius,ballPaint);

//        drawWaves(canvas);
        drawWaterView(canvas);

        Paint textPaint = new TextPaint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(radius/3);
        textPaint.setAntiAlias(true);
        canvas.drawText(""+percent+"%",radius,radius,textPaint);

        textPaint.setTextSize(radius/6);
        canvas.drawText("点击优化",radius,radius+radius/2,textPaint);

        canvas.restore();
    }

    private void drawWaves(Canvas canvas) {
        canvas.save();
        float w = (float) (Math.PI * 2 / d);

        h = ballRadius * percent / 100;
        prase = percent;

        Paint wavePaint =new Paint();
        wavePaint.setAntiAlias(true);
        wavePaint.setStrokeWidth(3);
        int red = 255 * (percent) / 100;
        int green = 255 - red;
        wavePaint.setARGB(255,red,green,0);
//        wavePaint.setColor(Color.BLUE);

        Path wavePath1 = new Path();
//        Path wavePath2 = new Path();
        wavePath1.reset();

        RectF rectf = new RectF(0,0,ballRadius,ballRadius);
        wavePath1.addArc(rectf,0,180);
//        wavePath2.reset();

//        canvas.clipPath(wavePath1);

//        for(int i=0;i<d;i++){
//            wave1[i] = (float) (10 * Math.sin(w*i+prase)-h);
//            wave2[i] = (float) (13 * Math.sin(w*i+prase+13)-h);
//            wavePath1.quadTo(i,wave1[i],i+1,wave1[i]);
////            wavePath2.quadTo(i,wave2[i],i+1,wave2[i]);
//        }

//        canvas.clipPath(mPath);


//        wavePath2.addCircle(radius,radius,radius-lineLength-lineLength/6, Path.Direction.CCW);

//        canvas.clipPath(wavePath1, Region.Op.REPLACE);

        canvas.translate(0,radius + ballRadius);

//        for (int i=0;i<d;i++){
//            canvas.drawLine(i,wave1[i],i,d,wavePaint);
//            canvas.drawLine(i,wave2[i],i,d,wavePaint);
//        }
        canvas.drawPath(wavePath1,wavePaint);
//        canvas.drawPath(wavePath2,wavePaint);

//        for (int i = 0; i < d; i++) {
//            canvas.drawLine(i, wave1[i], i, d, wavePaint);
//        }


        canvas.restore();
    }

    private void drawWaterView(Canvas canvas) {
        // y = Asin(wx+b)+h ，这个公式里：w影响周期，A影响振幅，h影响y位置，b为初相；
        // 将周期定为view总宽度
        float mCycleFactorW = (float) (2 * Math.PI / d);
        h = ballRadius * percent / 50;
        prase = count;
//        Math.random();

        // 得到第一条波的y值
        for (int i = 0; i < d; i++) {
            wave1[i] = (float) (10 * Math
                    .sin(mCycleFactorW * i + prase) - h);
        }
        // 得到第一条波的y值
        for (int i = 0; i < d; i++) {
            wave2[i] = (float) (15 * Math.sin(mCycleFactorW * i
                    + prase + 10) - h);

        }

        canvas.save();

        // 裁剪成圆形区域
        Paint wavePaint =new Paint();
        wavePaint.setAntiAlias(true);
        wavePaint.setStrokeWidth(3);
        int red = 255 * (percent) / 100;
        int green = 255 - red;
        wavePaint.setARGB(53,red,green,53);
        path.reset();
        canvas.clipPath(path);

        path.addCircle(d / 2, d / 2, ballRadius, Path.Direction.CCW);
        canvas.clipPath(path, android.graphics.Region.Op.REPLACE);
        // 将坐标系移到底部
        canvas.translate(0, d / 2 + ballRadius);

        for (int i = 0; i < d; i++) {
            canvas.drawLine(i, wave1[i], i, d, wavePaint);
        }
        for (int i = 0; i < d; i++) {
            canvas.drawLine(i, wave2[i], i, d, wavePaint);
        }
        canvas.restore();
    }

    private void drawLine(Canvas canvas) {
        canvas.save();
        canvas.translate(radius,radius);

        canvas.rotate(30);

        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setColor(Color.WHITE);
        linePaint.setStrokeWidth(3);

        int curPercent = 0;
        float dAngle = sweepAngle/100;
        for (int i=0;i<=100;i++){
            if(curPercent++ <= percent) {
//                linePaint.setColor(Color.BLUE);
                int red = 255 * (curPercent-1) / 100;
                int green = 255 - red;
                linePaint.setARGB(255,red,green,153);
            }else {
                linePaint.setColor(Color.WHITE);
            }
            canvas.drawLine(0,radius,0,radius-lineLength, linePaint);
            canvas.rotate(dAngle);
        }
        handler.sendEmptyMessageDelayed(0,30);
        canvas.restore();
    }

    public void refresh(int totalPercent){
        if(isRunning) {
            return;
        }
        totalPercent = Math.max(totalPercent,0);
        totalPercent = Math.min(totalPercent,100);
        this.totalPercent = totalPercent;
        isWiseColck = false;
        percent = lastTotalPercent;
        lastTotalPercent = totalPercent;
        handler.sendEmptyMessage(0);

    }

//    @Override
//    public void setOnClickListener(OnClickListener l) {
//        super.setOnClickListener(l);
//        refresh(totalPercent);
//    }
}
