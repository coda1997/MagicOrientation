package com.dadachen.magicorientation;

import android.app.Activity;
import android.util.Log;
import com.dadachen.magicorientation.sensors.Orientation;
import com.dadachen.magicorientation.utils.OrientationSensorInterface;

/**
 * Created by majidGolshadi on 9/22/2014.
 */
public class TestActivity extends Activity implements OrientationSensorInterface {

    @Override
    protected void onResume() {
        super.onResume();

        Orientation orientationSensor = new Orientation(this.getApplicationContext(), this);

        //------Turn Orientation sensor ON-------
        // set tolerance for any directions
        orientationSensor.init(1.0, 1.0, 1.0);

        // set output speed and turn initialized sensor on
        // 0 Normal
        // 1 UI
        // 2 GAME
        // 3 FASTEST
        orientationSensor.on(0);
        //---------------------------------------


        // turn orientation sensor off
//        orientationSensor.off();

        // return true or false
        orientationSensor.isSupport();

    }


    @Override
    public void orientation(Double AZIMUTH, Double PITCH, Double ROLL, Double x, Double y, Double z, Double mx, Double my, Double mz) {
        //test
    }
}
