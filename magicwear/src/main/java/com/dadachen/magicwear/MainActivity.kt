package com.dadachen.magicwear

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.wearable.activity.WearableActivity
import com.dadachen.magicwear.sensors.Orientation
import com.dadachen.magicwear.utils.OrientationSensorInterface
import kotlinx.android.synthetic.main.activity_main.*

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

    private fun initView() {
        bt_collect.setOnClickListener {

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


    }
}
