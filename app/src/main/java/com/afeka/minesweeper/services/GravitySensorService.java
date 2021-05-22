package com.afeka.minesweeper.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

interface GravitySensorServiceListener {

    enum ALARM_STATE {
        ON,OFF
    }

    void alarmStateChanged(ALARM_STATE state);

    void alarmSample(float xDiff,float yDiff,float zDiff);
}

public class GravitySensorService extends Service implements SensorEventListener {
    // This is a Tag for logging.
    private final static String TAG = "GravitySensorService";

    // This threshold is for the alarm state.
    private final double THRESHOLD = 2.5;

    // the current alarm state that the service is it.
    private GravitySensorServiceListener.ALARM_STATE currentAlarmState = GravitySensorServiceListener.ALARM_STATE.OFF;

    // This is the (Binded) Services
    public class SensorServiceBinder extends Binder {
        void registerListener(GravitySensorServiceListener listener) {
            mListeners.add(listener);
        }

        void unregisterListener(GravitySensorServiceListener listener) {
            mListeners.remove(listener);
        }

        void startSensors() {
            mSensorManager.registerListener(GravitySensorService.this,mAccel, SensorManager.SENSOR_DELAY_NORMAL);
        }

        void stopSensors() {
            mSensorManager.unregisterListener(GravitySensorService.this);
        }

        void resetInitalLock() {
            isLocked = false;
        }
    }

    // Binder given to clients
    private final IBinder mBinder = new SensorServiceBinder();

    // the listener to whom we send events regarding the alarm
    List<GravitySensorServiceListener> mListeners = new ArrayList<GravitySensorServiceListener>();

    // sensor manager to get sensor
    SensorManager mSensorManager;

    // the actual sensor we are using
    Sensor mAccel;

    // this is the first sensor event that LOCKS the initial "orientation"
    float firstX = 0;
    float firstY = 0;
    float firstZ = 0;
    boolean isLocked = false;

    public void GravitySensorServiceListener() { }

    @Override
    public void onCreate() {
        super.onCreate();

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        List<Sensor> deviceSensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);

        Log.d(TAG , "deviceSensors: " + deviceSensors.toString());

        mAccel = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        if(mAccel != null)
            Log.d(TAG , "Accelerometer available");
        else
            Log.d(TAG , "Accelerometer NOT available");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mSensorManager = null;
        mAccel = null;
        mListeners.clear();
        mListeners = null;

    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) { }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if(isLocked == false)
        {
            isLocked = true;
            firstX = event.values[0];
            firstY = event.values[1];
            firstZ = event.values[2];
        }
        else
        {

            float absDiffX = Math.abs(firstX - event.values[0]);
            float absDiffY = Math.abs(firstY - event.values[1]);
            float absDiffZ = Math.abs(firstZ - event.values[2]);

            Log.d(TAG,"Current: " + event.values[0] + " " + event.values[1] + " " + event.values[2]);
            Log.d(TAG, "Diffs: " + absDiffX + " " + absDiffY + " " + absDiffZ + " " + ((absDiffX + absDiffY + absDiffZ)>2.5 ? "Yes" : "No"));

            GravitySensorServiceListener.ALARM_STATE previousState = currentAlarmState;

            if (absDiffX + absDiffY + absDiffZ > THRESHOLD )
                this.currentAlarmState = GravitySensorServiceListener.ALARM_STATE.ON;
            else
                this.currentAlarmState = GravitySensorServiceListener.ALARM_STATE.OFF;


            for (GravitySensorServiceListener listener: mListeners) {
                if(previousState != currentAlarmState)
                    listener.alarmStateChanged(currentAlarmState);
                listener.alarmSample(absDiffX, absDiffY, absDiffZ);
            }
        }
    }
}
