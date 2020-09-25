package com.dadachen.magicorientation.sensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.util.Observable;

public class RotationVector extends Observable implements SensorEventListener, Isensor {
    private SensorManager sensorManager;
    private Sensor rotationVectorSensor;
    private SensorEvent sensorevent;

    public RotationVector(Context context) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        rotationVectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

    }
    public SensorEvent getEvent(){
        return sensorevent;
    }
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        this.sensorevent = sensorEvent;
        setChanged();
        notifyObservers();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        //nope
    }

    @Override
    public boolean isSupport() {
        return rotationVectorSensor != null;
    }

    @Override
    public void on(int speed) {
        switch (speed) {

            case 1:
                sensorManager.registerListener(this, rotationVectorSensor, SensorManager.SENSOR_DELAY_UI);
                break;

            case 2:
                sensorManager.registerListener(this, rotationVectorSensor, SensorManager.SENSOR_DELAY_GAME);
                break;

            case 3:
                sensorManager.registerListener(this, rotationVectorSensor, SensorManager.SENSOR_DELAY_FASTEST);
                break;

            default:
                sensorManager.registerListener(this, rotationVectorSensor, SensorManager.SENSOR_DELAY_NORMAL);
                break;
        }
    }

    @Override
    public void off() {
        sensorManager.unregisterListener(this, rotationVectorSensor);
    }

    @Override
    public float getMaximumRange() {
        return rotationVectorSensor.getMaximumRange();
    }
}
