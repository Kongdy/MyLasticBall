package com.project.kongdy.myelasticball.view

import android.annotation.TargetApi
import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.animation.Animation
import android.view.animation.Transformation
import com.project.kongdy.myelasticball.R

/**
 * @author kongdy
 * on 2017/1/11
 * 弹性小球
 *  kotlin实现
 */
class MyElasticBall : View {

    /**
     * 贝塞尔魔法数值，用于圆的曲率
     */
    private val BERZIER_MAGIC_VALUE = 0.551915024494F

    /*
    圆的主要画笔
     */
    private object mainPaint : Paint()

    /*
    构建圆球的路径
     */
    private object mainPath : Path()

    /*
    圆球基准值，表达成半径不太准确
     */
    private var radius = 10F

    private var leftElasticPoint: ElasticPoint = ElasticPoint()
    private var topElasticPoint: ElasticPoint = ElasticPoint()
    private var rightElasticPoint: ElasticPoint = ElasticPoint()
    private var bottomElasticPoint: ElasticPoint = ElasticPoint()


    private var elapseTime = 0F

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

        mainPaint.style = Paint.Style.FILL
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
    /**
     * 测试抖动效果
     */
    fun simpleShakeTest() {
        val reboundAnimator = ReboundAnimator()
        reboundAnimator.duration = 1000
        reboundAnimator.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationEnd(p0: Animation?) {
                resetSize()
            }

            override fun onAnimationRepeat(p0: Animation?) {
            }

            override fun onAnimationStart(p0: Animation?) {
            }
        })
        startAnimation(reboundAnimator)
    }

    fun resetSize() {
        val centerX = measuredWidth / 2F
        val centerY = measuredHeight / 2F

        val magicLine = radius * BERZIER_MAGIC_VALUE

        leftElasticPoint.setAllX(centerX-radius)
        rightElasticPoint.setAllX(centerX+radius)
        topElasticPoint.setAllY(centerY-radius)
        bottomElasticPoint.setAllY(centerY+radius)

        leftElasticPoint.y = centerY
        rightElasticPoint.y = centerY
        topElasticPoint.x = centerX
        bottomElasticPoint.x = centerX

        leftElasticPoint.leftOrTopPoint.y = leftElasticPoint.y - magicLine
        leftElasticPoint.rightOrBottomPoint.y = leftElasticPoint.y + magicLine
        topElasticPoint.leftOrTopPoint.x = topElasticPoint.x - magicLine
        topElasticPoint.rightOrBottomPoint.x = topElasticPoint.x + magicLine
        rightElasticPoint.leftOrTopPoint.y = rightElasticPoint.y - magicLine
        rightElasticPoint.rightOrBottomPoint.y = rightElasticPoint.y + magicLine
        bottomElasticPoint.leftOrTopPoint.x = bottomElasticPoint.x - magicLine
        bottomElasticPoint.rightOrBottomPoint.x = bottomElasticPoint.x + magicLine

        invalidate()
    }

    private fun calcOffset(time: Float): Float {
        return (((1 - Math.exp(-2 * (time + 0.054)) * Math.cos(20 * (time + 0.054))) - 1) * radius*BERZIER_MAGIC_VALUE).toFloat()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)


    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        resetSize()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        mainPath.reset()
        mainPath.moveTo(leftElasticPoint.x, leftElasticPoint.y)
        /*
         绘制圆的四个部分
         */
        mainPath.cubicTo(leftElasticPoint.leftOrTopPoint.x,leftElasticPoint.leftOrTopPoint.y,topElasticPoint.leftOrTopPoint.x,topElasticPoint.leftOrTopPoint.y,
                topElasticPoint.x,topElasticPoint.y)
        mainPath.cubicTo(topElasticPoint.rightOrBottomPoint.x,topElasticPoint.rightOrBottomPoint.y,rightElasticPoint.leftOrTopPoint.x,rightElasticPoint.leftOrTopPoint.y,
                rightElasticPoint.x,rightElasticPoint.y)
        mainPath.cubicTo(rightElasticPoint.rightOrBottomPoint.x,rightElasticPoint.rightOrBottomPoint.y,bottomElasticPoint.rightOrBottomPoint.x,bottomElasticPoint.rightOrBottomPoint.y,
                bottomElasticPoint.x,bottomElasticPoint.y)
        mainPath.cubicTo(bottomElasticPoint.leftOrTopPoint.x,bottomElasticPoint.leftOrTopPoint.y,leftElasticPoint.rightOrBottomPoint.x,leftElasticPoint.rightOrBottomPoint.y,
                leftElasticPoint.x,leftElasticPoint.y)
        canvas?.saveLayer(0F, 0F, measuredWidth.toFloat(), measuredHeight.toFloat(), mainPaint, Canvas.ALL_SAVE_FLAG)
        canvas?.drawColor(Color.GREEN)
        canvas?.drawPath(mainPath, mainPaint)

        canvas?.restore()
    }

    inner open class ReboundAnimator : Animation() {
        override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
            super.applyTransformation(interpolatedTime, t)
            elapseTime = interpolatedTime
            val offsetValue = calcOffset(elapseTime)

            rightElasticPoint.adjustAllX(offsetValue)
            Log.e("asdf", "elapseTime" +elapseTime.toString()+",offsetValue:"+offsetValue.toString())
            invalidate()
        }
    }

    /**
     *  贝塞尔基准轴
     */
    inner class ElasticPoint {
        var x = 0F
        var y = 0F
        val leftOrTopPoint: PointF = PointF()
        val rightOrBottomPoint: PointF = PointF()
        /**
         * 调整所有x变量
         */
        fun adjustAllY(offsetY: Float) {
            y += offsetY
            leftOrTopPoint.y += offsetY
            rightOrBottomPoint.y += offsetY
        }

        /**
         * 设置所有x变量
         */
        fun setAllY(yValue: Float) {
            y = yValue
            leftOrTopPoint.y = yValue
            rightOrBottomPoint.y = yValue
        }

        /**
         * 调整所有x变量
         */
        fun adjustAllX(offsetX: Float) {
            x += offsetX
            leftOrTopPoint.x += offsetX
            rightOrBottomPoint.x += offsetX
        }

        /**
         * 设置所有x变量
         */
        fun setAllX(xValue: Float) {
            x = xValue
            leftOrTopPoint.x = xValue
            rightOrBottomPoint.x = xValue
        }
    }


    /**
     *  返回指定单位的像素
     */
    private fun getRawSize(unit: Int, value: Float): Float {
        val dm: DisplayMetrics = context.resources.displayMetrics;
        return TypedValue.applyDimension(unit, value, dm)
    }
}