package com.afeka.minesweeper;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.afeka.minesweeper.util.GameStatus;

public class FinalPage extends AppCompatActivity {

    static final String TAG = "FinalPage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_page);

        GameStatus gameStatus = GameStatus.valueOf(getBoardSize());

        TextView gameResult = findViewById(R.id.tv_gameResults);
        TextView gameScore = findViewById(R.id.tv_gameScore);
        ImageView imageView = findViewById(R.id.img_gameStatus);
        if (gameStatus.equals(GameStatus.WIN)) {
            gameResult.setText(getString(R.string.you_won));
            imageView.setImageResource(R.drawable.smile);
        }
        else
            if(gameStatus.equals(GameStatus.LOSE)) {
                gameResult.setText(getString(R.string.you_lose));
                imageView.setImageResource(R.drawable.sad);
            }
        gameScore.setText(getString(R.string.your_score) + getExtraGameScore());
    }

    private String getBoardSize() {
//        Intent intent = getIntent();
//        String gameStatus = intent.getStringExtra(GamePage.GAME_STATUS_KEY);
//        return GameStatus.valueOf(gameStatus).toString();
        return GameStatus.LOSE.name();
    }

    private String getExtraGameScore() {
//        Intent intent = getIntent();
//        String gameScore = intent.getStringExtra(GamePage.GAME_SCORE_KEY);
//        return gameScore;
        return "-1";
    }

    public void onPlayAgainButtonClicked(View view) {
        Intent intent = new Intent(FinalPage.this, MainPage.class);
//        intent.putExtra(GamePage.GAME_SCORE_KEY, getExtraGameScore());
        startActivity(intent);
    }
}