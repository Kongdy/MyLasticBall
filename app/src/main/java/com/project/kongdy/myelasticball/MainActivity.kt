package com.project.kongdy.myelasticball

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.project.kongdy.myelasticball.view.MyElasticBall

/**
 * @author Kongdy
 *  kotlin尝试
 *  主页面
 *
 *  kotlin使用的过程中，继承的时候，父类后面带括号可以省略构造函数，
 *  如果遇到特殊情况下必须重写父类的构造方法，那么请移除括号，并
 *  添加constructor
 */
class MainActivity : AppCompatActivity() {

    private lateinit var meb_my_ball:MyElasticBall

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        meb_my_ball = findViewById(R.id.meb_my_ball) as MyElasticBall

        meb_my_ball.setOnClickListener { view -> meb_my_ball.simpleShakeTest()}

    }
}
