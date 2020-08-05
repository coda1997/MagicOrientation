package com.dadachen.magicwear

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.wearable.activity.WearableActivity
import com.dadachen.magicwear.sensors.Orientation
import com.dadachen.magicwear.utils.OrientationSensorInterface
import com.dadachen.magicwear.utils.writeToLocalStorage
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.StringBuilder

class MainActivity : WearableActivity(), OrientationSensorInterface {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
        }
        setContentView(R.layout.activity_main)

        initView()
        // Enables Always-on
        setAmbientEnabled()
    }
    private var isWrite = false
    private val stringbuilder = StringBuilder()

    private fun initView() {
        bt_collect.setOnClickListener {
            if(isWrite){
                isWrite = false
                bt_collect.text = "记录到本地"
                writeToLocalStorage(stringbuilder.toString())
                stringbuilder.clear()
            }else{
                isWrite=true
                bt_collect.text = "停止记录"
            }
        }

    }
    override fun onResume() {
        super.onResume()
        val orientationSensor = Orientation(this.applicationContext, this)

        //------Turn Orientation sensor ON-------
        // set tolerance for any directions
        orientationSensor.init(1.0, 1.0, 1.0)

        // set output speed and turn initialized sensor on
        // 0 Normal
        // 1 UI
        // 2 GAME
        // 3 FASTEST
        orientationSensor.on(3)
        //---------------------------------------

        // turn orientation sensor off
//        orientationSensor.off()

        // return true or false
        orientationSensor.isSupport
    }

    override fun orientation(
        AZIMUTH: Double?,
        PITCH: Double?,
        ROLL: Double?,
        x: Double?,
        y: Double?,
        z: Double?,
        mx: Double?,
        my: Double?,
        mz: Double?
    ) {
        tv_rota.text = "Azi:$AZIMUTH, Pi:$PITCH, Ro:$ROLL"
        tv_acc.text = "Acc: x=$x, y=$y, z=$z"
        tv_ma.text = "M: mx=$mx, my=$my, mz=$mz"
        if(isWrite){
            stringbuilder.appendln("$AZIMUTH, $PITCH, $ROLL, $x, $y, $z, $mx, $my, $mz")
        }
    }
}
