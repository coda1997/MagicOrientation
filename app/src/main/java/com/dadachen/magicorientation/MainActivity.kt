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
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import java.lang.StringBuilder


class MainActivity : AppCompatActivity(), OrientationSensorInterface {
    //  使用原生安卓的坐标系，并没有调整
    override fun orientation(AZIMUTH: Double?, PITCH: Double?, ROLL: Double?, x: Double, y: Double, z: Double,mx:Double,my:Double,mz:Double) {
        setRotationValues(AZIMUTH!!, PITCH!!, ROLL!!, leadingAngle)
        setAcc(x, y, z, leadingAngle)
        setMag(mx,my,mz)
        stringbuilder.appendln()
    }

    private fun setMag(mx: Double, my: Double, mz: Double) {
        stringbuilder.append("$mx, $my, $mz")
        find<TextView>(R.id.mx).text=mx.toString()
        find<TextView>(R.id.my).text=my.toString()
        find<TextView>(R.id.mz).text=mz.toString()
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
    private val stringbuilder = StringBuilder()

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
    }

    private fun initView() {
        find<Button>(R.id.bt_correction).onClick {
            leadingAngle = find<TextView>(R.id.z1).text.toString().toDouble()
        }

        find<Button>(R.id.bt_timer).onClick {
            count = 0
            stringbuilder.clear()
        }
        find<Button>(R.id.bt_end).onClick {
            val content = stringbuilder.toString()
            writeToLocalStorage(content)
        }
    }

    private fun setAcc(x: Double, y: Double, z: Double, leanding: Double) {
        val acc = matrixMulti(x, y, z, leanding)
        find<TextView>(R.id.acc_x).text = String.format("%.2f", acc[0])
        find<TextView>(R.id.acc_y).text = String.format("%.2f", acc[1])
        find<TextView>(R.id.acc_z).text = String.format("%.2f", acc[2])
        stringbuilder.append("${acc[0]}, ${acc[1]}, ${acc[2]}, ")
    }

    private fun matrixMulti(x: Double, y: Double, z: Double, leanding: Double): DoubleArray {
        val xx = Math.cos(leanding) * (-x) + Math.sin(leanding) * y
        val yy = -Math.sin(leanding) * (-x) + Math.cos(leanding) * y
        return doubleArrayOf(xx, yy, -z)
    }

    private fun setRotationValues(z: Double, x: Double, y: Double, leanding: Double) {
        var z2 = z - leanding
        while (z2 < 0) {
            z2 += 360
        }
        z2 %= 360
        if (z2 > 180) {
            z2 -= 360
        }
        stringbuilder.append("${System.nanoTime()} ,$x, $y, $z2, ")
        find<TextView>(R.id.x).text = String.format("%.2f", x)
        find<TextView>(R.id.y).text = String.format("%.2f", y)
        find<TextView>(R.id.z1).text = String.format("%.2f", z)
        find<TextView>(R.id.z2).text = String.format("%.2f", z2)
    }
}
