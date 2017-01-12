package com.project.kongdy.myelasticball.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

import com.project.kongdy.myelasticball.R;

/**
 * @author kongdy
 *         on 2017/1/9
 *
 *         具有弹性势能的小球
 *         带有定点吸附
 */

public class ElasticBall extends View{

    /*
     主要画笔，圆形的主色
     */
    private Paint mainPaint = new Paint();
    /*
     构建圆形的path
     */
    private Path mainPath = new Path();
    /*
    圆形半径
     */
    private float radius = 10;

    public ElasticBall(Context context) {
        this(context,null);
    }
    public ElasticBall(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }
    public ElasticBall(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initProperty(context, attrs);
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ElasticBall(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initProperty(context, attrs);
    }

    /*
    初始化属性
     */
    private void initProperty(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ElasticBall);
        int mainColor = a.getColor(R.styleable.ElasticBall_ball_color, Color.RED);
        radius = a.getDimension(R.styleable.ElasticBall_ball_radius,getRawSize(TypedValue.COMPLEX_UNIT_DIP,10));
        mainPaint.setColor(mainColor);
        a.recycle();

        mainPaint.setStrokeWidth(getRawSize(TypedValue.COMPLEX_UNIT_DIP,5));
        highAffect(mainPaint);

        mainPaint.setStyle(Paint.Style.STROKE);
        mainPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    /*
      高画质处理
     */
    private void highAffect(Paint paint) {
        paint.setAntiAlias(true); // 抗锯齿
        paint.setDither(true); // 防抖
        paint.setFilterBitmap(true); // 滤波
        paint.setSubpixelText(true); // 像素自处理
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int mWidth = w-getPaddingLeft()-getPaddingRight();
        int mHeight = h-getPaddingBottom()-getPaddingTop();

        mainPath.reset();
        mainPath.moveTo(50,mHeight/2);
        mainPath.quadTo(mWidth/3,mHeight/3,mWidth*2/3,mHeight/2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.saveLayer(0,0,getMeasuredWidth(),getMeasuredHeight(),mainPaint,Canvas.ALL_SAVE_FLAG);
        canvas.drawColor(Color.GREEN);
        canvas.drawPath(mainPath,mainPaint);

        canvas.restore();
    }

    /*
    根据指定单位和指定数值返回像素值
     */
    private float getRawSize(int unit,float value) {
        DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
        return TypedValue.applyDimension(unit,value,dm);
    }
}
