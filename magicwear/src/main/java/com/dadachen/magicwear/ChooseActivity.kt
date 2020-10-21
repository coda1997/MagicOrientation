package com.dadachen.magicwear

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.dadachen.magicwear.utils.Utils
import com.dadachen.magicwear.utils.writeToLocalStorage
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.StringBuilder
import java.lang.Thread.sleep
import kotlin.concurrent.thread

class ChooseActivity : Activity() {
    private val gyro = FloatArray(3)
    private val acc = FloatArray(3)
    private val rotVector = FloatArray(4)
    private lateinit var sensorManager:SensorManager
    private var rotVSensor:Sensor? = null
    private var accVSensor:Sensor? = null
    private var gyroVSensor:Sensor? = null
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sharedPreferences = getPreferences(Context.MODE_PRIVATE)
        initSensor()
        initView()

    }
    private val rotl = object : SensorEventListener {
        override fun onSensorChanged(p0: SensorEvent?) {
            rotVector[0] = p0!!.values[0]
            rotVector[1] = p0.values[1]
            rotVector[2] = p0.values[2]
            rotVector[3] = p0.values[3]
        }

        override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
            Log.d("imu", "rot accuracy changed")
        }
    }
    private val gyrol = object : SensorEventListener {
        override fun onSensorChanged(p0: SensorEvent?) {
            gyro[0] = p0!!.values[0]
            gyro[1] = p0.values[1]
            gyro[2] = p0.values[2]
        }

        override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
            Log.d("imu", "gyro accuracy changed")
        }
    }
    private val accl = object : SensorEventListener {
        override fun onSensorChanged(p0: SensorEvent?) {
            acc[0] = p0!!.values[0]
            acc[1] = p0.values[1]
            acc[2] = p0.values[2]
        }

        override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
            Log.d("imu", "acc accuracy changed")
        }
    }
    private fun initSensor() {
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        rotVSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
        accVSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        gyroVSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE_UNCALIBRATED)

        sensorManager.registerListener(rotl, rotVSensor,SensorManager.SENSOR_DELAY_FASTEST)
        sensorManager.registerListener(accl, accVSensor, SensorManager.SENSOR_DELAY_FASTEST)
        sensorManager.registerListener(gyrol, gyroVSensor, SensorManager.SENSOR_DELAY_FASTEST)
    }

    override fun onResume() {
        super.onResume()
        initSensor()
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(accl)
        sensorManager.unregisterListener(gyrol)
        sensorManager.unregisterListener(rotl)
    }
    private val freq = "freq"
    private var isCollected = false
    private fun initView() {
        bt_collect.setOnClickListener {
            if(!isCollected){
                startRecord()
                bt_collect.text = "结束采集"
            }else{
                endRecord()
                bt_collect.text = "记录到本地"
            }
            isCollected = !isCollected
        }
    }
    private var recording = false
    private val stringBuilder = StringBuilder()
    private var frequency = 50
    private fun startRecord() {
        stringBuilder.clear()
        recording = true
        thread(start = true) {
            while (recording){
                val content = "${System.currentTimeMillis()},${acc[0]},${acc[1]},${acc[2]},${gyro[0]},${gyro[1]},${gyro[2]},${rotVector[0]},${rotVector[1]},${rotVector[2]},${rotVector[3]}"
                stringBuilder.appendln(content)
                sleep((1000/frequency).toLong())
            }
        }
    }
    private fun endRecord() {
        recording = false
        writeToLocalStorage("$externalCacheDir/IMU-acc-gyro-rotv-${Utils.getTime(System.currentTimeMillis())}.csv",stringBuilder.toString())
    }

}