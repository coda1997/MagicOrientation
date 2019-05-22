package com.dadachen.magicorientation

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.opengl.GLSurfaceView
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class MainActivity : AppCompatActivity(), SensorEventListener {
    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }

    private var leadingAngle :Float= 0f
    override fun onSensorChanged(p0: SensorEvent?) {
        if (p0?.sensor?.type== Sensor.TYPE_ORIENTATION){
            values=p0.values
            if (values.size == 3) {
                setRotationValues(-values[0],-values[1],-values[2],leadingAngle)

            } else {
                toast("vector: ${values.joinToString { "$it," }}")
            }
        }
    }

    private lateinit var sensorManager: SensorManager
    private var sensor:Sensor?=null
    private var values = floatArrayOf(0f,0f,0f)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initSensors()
        initView()
    }

    private fun initView() {
        find<Button>(R.id.bt_correction).onClick {
            leadingAngle= find<TextView>(R.id.z1).text.toString().toFloat()
        }
        setRotationValues(-values[0],-values[1],-values[2],leadingAngle)
    }
    private fun setRotationValues(z:Float,x:Float,y:Float, leanding:Float){
        var z2 = z-leanding
        while (z2<0){
            z2+=360
        }
        z2%=360
        if (z2>180){
            z2-=360
        }
        find<TextView>(R.id.x).text=String.format("%.2f",x)
        find<TextView>(R.id.y).text=String.format("%.2f",y)
        find<TextView>(R.id.z1).text=String.format("%.2f",z)
        find<TextView>(R.id.z2).text=String.format("%.2f",z2)
    }

    private fun initSensors() {
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensor=sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION)
        sensorManager.registerListener(this,sensor, SensorManager.SENSOR_DELAY_NORMAL)

    }
}
