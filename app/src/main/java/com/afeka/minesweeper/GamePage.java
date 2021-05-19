package com.afeka.minesweeper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.afeka.minesweeper.util.BoardSize;
import com.afeka.minesweeper.util.GameStatus;

public class GamePage extends AppCompatActivity {

    static final String TAG = "GamePage";
    static final String GAME_STATUS_KEY = "GAME_STATUS";
    static final String GAME_SCORE_KEY = "GAME_SCORE";

    Handler handler;
    Runnable timerRunnable;
    BoardSize boardSize;
    int time = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        boardSize = getBoardSize();
        GameEngine.getInstance(boardSize.value,true).createGrid(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_page);
    }

    @Override
    protected void onResume() {
//        GameEngine.getInstance(boardSize.value,true).createGrid(this);
        super.onResume();
//        setContentView(R.layout.activity_game_page);
        startTimer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopTimer();
    }

    private BoardSize getBoardSize() {
        return BoardSize.PROFESSIONAL;
    }


    public void onNextButtonClicked(View view) {
        Intent intent = new Intent(GamePage.this, FinalPage.class);

        // TODO: Send Score and GameStatus.
//        intent.putExtra(GamePage.GAME_STATUS_KEY,GameEngine.getInstance().getGameStatus().toString());
//        intent.putExtra(GamePage.GAME_SCORE_KEY, Integer.toString(calculateScore(time)));

        startActivity(intent);
    }

    public void updateScore(){
        if (GameEngine.getInstance().getGameStatus().equals(GameStatus.PLAY) || GameEngine.getInstance().getGameStatus().equals(GameStatus.PENALTY)) {
            ((TextView) findViewById(R.id.timerText)).setText("Score: " + calculateScore(time));

            if (GameEngine.getInstance().getGameStatus().equals(GameStatus.PENALTY))
                ((ConstraintLayout) findViewById(R.id.clo_main_game)).setBackgroundColor(Color.RED);
            else
                ((ConstraintLayout) findViewById(R.id.clo_main_game)).setBackgroundColor(Color.WHITE);
        }

        if (GameEngine.getInstance().getGameStatus().equals(GameStatus.WIN) || GameEngine.getInstance().getGameStatus().equals(GameStatus.LOSE)) {
            TextView tv_gameStatus = findViewById(R.id.tv_gameStatus);
            tv_gameStatus.setVisibility(View.VISIBLE);
            Button btn_toSummary = findViewById(R.id.btn_toSummary);
            btn_toSummary.setVisibility(View.VISIBLE);

            if (GameEngine.getInstance().getGameStatus().equals(GameStatus.WIN))
                tv_gameStatus.setText(getString(R.string.you_won));
            else
            if (GameEngine.getInstance().getGameStatus().equals(GameStatus.LOSE))
                tv_gameStatus.setText(getString(R.string.you_lose));
            ((TextView) findViewById(R.id.timerText)).setText("Score: " + Integer.toString(calculateScore(time)));
        }
    }


    private void startTimer() {
        if(handler != null)
            handler.removeCallbacksAndMessages(null);

        handler = new Handler();

        time = 0;
        ((TextView)findViewById(R.id.timerText)).setText("0");

        timerRunnable = () -> {

            if (GameEngine.getInstance().getGameStatus().equals(GameStatus.PLAY) || GameEngine.getInstance().getGameStatus().equals(GameStatus.PENALTY)) {
                time++;
                handler.postDelayed(timerRunnable, 1000);
                updateScore();
//                ((TextView) findViewById(R.id.timerText)).setText("Score: " + calculateScore(time));
//
//                if (GameEngine.getInstance().getGameStatus().equals(GameStatus.PENALTY))
//                    ((ConstraintLayout) findViewById(R.id.clo_main_game)).setBackgroundColor(Color.RED);
//                else
//                    ((ConstraintLayout) findViewById(R.id.clo_main_game)).setBackgroundColor(Color.WHITE);
            }

            if (GameEngine.getInstance().getGameStatus().equals(GameStatus.WIN) || GameEngine.getInstance().getGameStatus().equals(GameStatus.LOSE)) {
                stopTimer();
                updateScore();
//                TextView tv_gameStatus = findViewById(R.id.tv_gameStatus);
//                tv_gameStatus.setVisibility(View.VISIBLE);
//                Button btn_toSummary = findViewById(R.id.btn_toSummary);
//                btn_toSummary.setVisibility(View.VISIBLE);
//
//                if (GameEngine.getInstance().getGameStatus().equals(GameStatus.WIN))
//                    tv_gameStatus.setText(getString(R.string.you_won));
//                else
//                    if (GameEngine.getInstance().getGameStatus().equals(GameStatus.LOSE))
//                        tv_gameStatus.setText(getString(R.string.you_lose));
//                ((TextView) findViewById(R.id.timerText)).setText("Score: " + Integer.toString(calculateScore(time)));
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
        if (score > 4000 && score < 4100)
            runOnUiThread(new Runnable() {
                public void run() {
                    GameEngine.getInstance().setGameStatus(GameStatus.PENALTY);
                }
            });
        return (int) ((1/Math.log(time+1))*10000);
    }

    private void stopTimer() {
        handler.removeCallbacks(timerRunnable);
    }
}
