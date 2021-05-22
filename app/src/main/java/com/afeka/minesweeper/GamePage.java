package com.afeka.minesweeper;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.afeka.minesweeper.services.GravitySensorService;
import com.afeka.minesweeper.services.GravitySensorServiceListener;
import com.afeka.minesweeper.util.BoardSize;
import com.afeka.minesweeper.util.GameStatus;

public class GamePage extends AppCompatActivity implements GravitySensorServiceListener {

    static final String TAG = "GamePage";
    static final String GAME_STATUS_KEY = "GAME_STATUS";
    static final String GAME_SCORE_KEY = "GAME_SCORE";

    GravitySensorService.SensorServiceBinder mBinder;
    boolean isBound = false;

    Handler handler;
    Runnable timerRunnable;
    BoardSize boardSize;

    int time = 0;
    int penaltyTime=0;

    TextView getStatus;
    TextView getDiff;
    TextView timerText;
    ConstraintLayout clo_main_game;
    TextView tv_gameStatus;
    Button btn_toSummary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        boardSize = getBoardSize();
        super.onCreate(savedInstanceState);
        GameEngine.getInstance(boardSize.value,true).createGrid(this);
        setContentView(R.layout.activity_game_page);
        initActivity();
        getSupportActionBar().hide();
    }

    public void initActivity(){
        getStatus = (TextView) findViewById(R.id.tvShowState);
        getDiff = (TextView) findViewById(R.id.tvShowDiff);
        timerText = (TextView) findViewById(R.id.timerText);
        clo_main_game = (ConstraintLayout) findViewById(R.id.clo_main_game);
        tv_gameStatus = (TextView) findViewById(R.id.tv_gameStatus);
        btn_toSummary = (Button) findViewById(R.id.btn_toSummary);
    }

    @Override
    protected void onResume() {
//        GameEngine.getInstance(boardSize.value,true).createGrid(this);
        super.onResume();
//        setContentView(R.layout.activity_game_page);
        startTimer();

        if (isBound) {
            mBinder.resetInitalLock();
            mBinder.startSensors();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        stopTimer();

        if (isBound) {
            mBinder.stopSensors();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, GravitySensorService.class);
        Log.d(TAG, "On start - binding to service...");
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (isBound) {
            unbindService(mConnection);
            isBound = false;
        }

    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d("Service Connection", "bound to service");
            mBinder = (GravitySensorService.SensorServiceBinder) service;
            mBinder.registerListener(GamePage.this);

            Log.d("Service Connection", "registered as listener");
            isBound = true;
            mBinder.startSensors();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

            mBinder.stopSensors();

            isBound = false;
        }
    };

    @Override
    public void alarmStateChanged(ALARM_STATE state) {

        Log.d(TAG, "STATE: " + state);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (state.equals(ALARM_STATE.ON))
                    startPenalty();
                else
                    stopPenalty();
                getStatus.setText("STATE: " + state);
            }
        });
    }

    @Override
    public void alarmSample(float xDiff, float yDiff, float zDiff) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getDiff.setText("Diff: " + xDiff + " " + yDiff + " " + zDiff + " " + ((xDiff + yDiff + zDiff)>2.5 ? "Yes" : "No"));
            }
        });
    }

    private BoardSize getBoardSize() {
        return BoardSize.PROFESSIONAL;
    }


    public void onNextButtonClicked(View view) {
        Intent intent = new Intent(GamePage.this, FinalPage.class);

        intent.putExtra(GamePage.GAME_STATUS_KEY,GameEngine.getInstance().getGameStatus().toString());
        intent.putExtra(GamePage.GAME_SCORE_KEY, Integer.toString(calculateScore(time)));

        startActivity(intent);
    }

    public void updateScore(){
        if (GameEngine.getInstance().getGameStatus().equals(GameStatus.PLAY) || GameEngine.getInstance().getGameStatus().equals(GameStatus.PENALTY)) {
            timerText.setText("Score: " + calculateScore(time));

            if (GameEngine.getInstance().getGameStatus().equals(GameStatus.PENALTY))
                clo_main_game.setBackgroundColor(Color.RED);
            else
                clo_main_game.setBackgroundColor(getResources().getColor(R.color.app_bg));
        }

        if (GameEngine.getInstance().getGameStatus().equals(GameStatus.WIN) || GameEngine.getInstance().getGameStatus().equals(GameStatus.LOSE)) {
            tv_gameStatus.setVisibility(View.VISIBLE);
            btn_toSummary.setVisibility(View.VISIBLE);

            if (GameEngine.getInstance().getGameStatus().equals(GameStatus.WIN))
                tv_gameStatus.setText(getString(R.string.you_won));
            else
            if (GameEngine.getInstance().getGameStatus().equals(GameStatus.LOSE))
                tv_gameStatus.setText(getString(R.string.you_lose));
            timerText.setText("Score: " + Integer.toString(calculateScore(time)));
        }
    }


    private void startTimer() {
        if(handler != null)
            handler.removeCallbacksAndMessages(null);

        handler = new Handler();

        time = 0;
        timerText.setText("0");

        timerRunnable = () -> {

            if (GameEngine.getInstance().getGameStatus().equals(GameStatus.PLAY) || GameEngine.getInstance().getGameStatus().equals(GameStatus.PENALTY)) {
                time++;
                handler.postDelayed(timerRunnable, 1000);
                if (GameEngine.getInstance().getGameStatus().equals(GameStatus.PENALTY)) {
                    penaltyTime++;
                    double penalty = (double) (penaltyTime*0.01);
                    GameEngine.getInstance().setPenalty(penalty);
                }
//                    penaltyTime++;
                else
                    penaltyTime=0;
                updateScore();
            }

            if (GameEngine.getInstance().getGameStatus().equals(GameStatus.WIN) || GameEngine.getInstance().getGameStatus().equals(GameStatus.LOSE)) {
                stopTimer();
                updateScore();
            }
        };

        handler.postDelayed(timerRunnable, 1000);
    }

//    private void saveScore(int calculateScore) {
//        SharedPreferences pref = getPreferences(Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = pref.edit();
//
//        int record = pref.getInt(boardSize.toString(), -1);
//
//        editor.putInt(BoardSize.BEGINNER.toString(), calculateScore);
//        editor.apply();
//    }

    private int calculateScore(int time){
        if(GameEngine.getInstance().getGameStatus().equals(GameStatus.LOSE))
            return 0;
        int score = (int) ((1/Math.log(time+1))*10000);

        // Remove
//        if (score > 4000 && score < 4100)
//            runOnUiThread(new Runnable() {
//                public void run() {
//                    GameEngine.getInstance().setGameStatus(GameStatus.PENALTY);
//                }
//            });
        return (int) ((1/Math.log(time+1))*10000);
    }

    private void startPenalty(){
        runOnUiThread(new Runnable() {
            public void run() {
                GameEngine.getInstance().setGameStatus(GameStatus.PENALTY);
            }
        });
    }

    private void stopPenalty(){
        runOnUiThread(new Runnable() {
            public void run() {
                GameEngine.getInstance().setGameStatus(GameStatus.PLAY);
            }
        });
    }

    private void stopTimer() {
        handler.removeCallbacks(timerRunnable);
    }
}
