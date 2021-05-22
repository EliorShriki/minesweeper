package com.afeka.minesweeper.services;

public interface GravitySensorServiceListener {

    enum ALARM_STATE {
        ON, OFF
    }

    void alarmStateChanged(ALARM_STATE state);

    void alarmSample(float xDiff, float yDiff, float zDiff);
}
