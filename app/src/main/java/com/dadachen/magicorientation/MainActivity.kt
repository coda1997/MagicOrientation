package com.dadachen.magicorientation

import android.Manifest
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat

import android.widget.Button
import android.widget.TextView
import com.dadachen.magicorientation.sensors.Orientation
import com.dadachen.magicorientation.utils.OrientationSensorInterface
import com.dadachen.magicorientation.utils.writeToLocalStorage
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import java.lang.StringBuilder


class MainActivity : AppCompatActivity(), OrientationSensorInterface {
    override fun orientation(
        AZIMUTH: Double?,
        PITCH: Double?,
        ROLL: Double?,
        x: Double,
        y: Double,
        z: Double,
        mx: Double,
        my: Double,
        mz: Double
    ) {
        count++
        setRotationValues(AZIMUTH!!, PITCH!!, ROLL!!, leadingAngle)
        setAcc(x, y, z)
        setMag(mx, my, mz)
        stringBuilder.appendln()
    }

    private fun setMag(mx: Double, my: Double, mz: Double) {
        stringBuilder.append("$mx,$my,$mz")
        find<TextView>(R.id.mx).text = mx.toString()
        find<TextView>(R.id.my).text = my.toString()
        find<TextView>(R.id.mz).text = mz.toString()
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


    private var leadingAngle: Double = 0.0
    private var count = 0
    private val stringBuilder = StringBuilder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                1
            )
        }
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView() {
        bt_correction.onClick {
            leadingAngle = find<TextView>(R.id.z1).text.toString().toDouble()
        }

        bt_timer.onClick {
            count = 0
            stringBuilder.clear()
            bt_timer.isEnabled = false
            bt_end.isEnabled = true
            bt_timer.text = "采集中"
            toast("开始采集")
        }
        bt_end.onClick {
            val content = stringBuilder.toString()
            val filePath = "$externalCacheDir/IMU_data_${System.currentTimeMillis()}.csv"
            withContext(Dispatchers.IO) {
                writeToLocalStorage(filePath, content)
            }
            bt_timer.isEnabled = true
            bt_end.isEnabled = false
            bt_timer.text = "开始采集"
            toast("结束采集，数据已保存")
        }
    }


    private fun setAcc(x: Double, y: Double, z: Double) {
        acc_x.text = String.format("%.2f", x)
        acc_y.text = String.format("%.2f", y)
        acc_z.text = String.format("%.2f", z)
        stringBuilder.append("${y},${x},${z}, ")
    }


    private fun setRotationValues(z: Double, x: Double, y: Double, leading: Double) {
        var z2 = z - leading
        while (z2 < 0) {
            z2 += 360
        }
        z2 %= 360
        if (z2 > 180) {
            z2 -= 360
        }
        stringBuilder.append("${System.currentTimeMillis()},$x,$y,$z2,")
        find<TextView>(R.id.x).text = String.format("%.2f", x)
        find<TextView>(R.id.y).text = String.format("%.2f", y)
        find<TextView>(R.id.z1).text = String.format("%.2f", z)
        find<TextView>(R.id.z2).text = String.format("%.2f", z2)
    }
}
