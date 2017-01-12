package com.project.kongdy.myelasticball.view

import android.annotation.TargetApi
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.os.Build
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import com.project.kongdy.myelasticball.R

/**
 * @author kongdy
 * on 2017/1/11
 * 弹性小球
 *  kotlin实现
 */
class MyElasticBall : View {

    /*
    圆的主要画笔
     */
    private object mainPaint : Paint()
    /*
    构建圆球的路径
     */
    private object mainPath : Path()
    /*
    圆球半径
     */
    private var radius = 10F

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initProperty(context as Context, attrs as AttributeSet)
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        initProperty(context as Context, attrs as AttributeSet)
    }

    /*
    初始化属性
     */
    fun initProperty(context: Context, attrs: AttributeSet) {
        val a: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.MyElasticBall)
        val mainColor: Int = a.getColor(R.styleable.MyElasticBall_m_ball_color, Color.RED)
        radius = a.getDimension(R.styleable.MyElasticBall_m_ball_radius, getRawSize(TypedValue.COMPLEX_UNIT_DIP, 10F))
        mainPaint.color = mainColor
        a.recycle()

        mainPaint.strokeWidth = getRawSize(TypedValue.COMPLEX_UNIT_DIP, 5F)
        highEffect(mainPaint)

        mainPaint.style = Paint.Style.STROKE
        mainPaint.strokeCap = Paint.Cap.ROUND
        mainPaint.strokeJoin = Paint.Join.ROUND
    }

    /*
          高画质处理
    */
    private fun highEffect(paint: Paint) {
        paint.isAntiAlias = true // 抗锯齿
        paint.isDither = true// 防抖
        paint.isFilterBitmap = true// 滤波
        paint.isSubpixelText = true// 像素自处理
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val mWidth = w-paddingLeft-paddingRight
        val mHeight = h-paddingTop-paddingBottom

        val centerX = mWidth/2F
        val centerY = mHeight/2F

        mainPath.reset()
        mainPath.moveTo(centerX,centerY+radius)
        mainPath.cubicTo(centerX-radius,centerY,centerX,centerY-radius,centerX+radius,centerY)
        mainPath.cubicTo(centerX,centerY-radius,centerX+radius,centerY,centerX,centerY+radius)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.saveLayer(0F,0F, measuredWidth.toFloat(), measuredHeight.toFloat(),mainPaint,Canvas.ALL_SAVE_FLAG)
        canvas?.drawColor(Color.GREEN)
        canvas?.drawPath(mainPath,mainPaint)

        canvas?.restore()
    }

    /**
     *  返回指定单位的像素
     */
    private fun getRawSize(unit: Int, value: Float): Float {
        val dm: DisplayMetrics = context.resources.displayMetrics;
        return TypedValue.applyDimension(unit, value, dm)
    }
}