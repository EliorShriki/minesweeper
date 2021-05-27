package com.afeka.minesweeper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.afeka.minesweeper.util.BoardSize;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainPage extends AppCompatActivity {

    static final String TAG = "MainPage";
    static final String BOARD_SIZE_KEY = "BOARD_SIZE";
    static final String GAME_RECORD_KEY = "RECORD";

    BoardSize boardSize = BoardSize.BEGINNER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        setBoardSize(boardSize);
        enableRButton();
        getSupportActionBar().hide();

//        getExtraRecord();
//        getExtraGameScore();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setBoardSize(boardSize);
        enableRButton();
//        getExtraRecord();
//        getExtraGameScore();
    }

    public void initRadioButton(RadioButton radioBtn){
        radioBtn.setChecked(true);
        updateBoardView();
        updateRecordView();
    }

    public void setBoardSize(BoardSize bSize){
        boardSize = bSize;
//        saveBoardSize();
    }

    public void enableRButton(){
        RadioButton radioBtn;
        if (boardSize.equals(BoardSize.BEGINNER))
            radioBtn = findViewById(R.id.rbtn_beginner);
        else
            if (boardSize.equals(BoardSize.INTERMEDIATE))
                radioBtn = findViewById(R.id.rbtn_intermediate);
            else
                radioBtn = findViewById(R.id.rbtn_professional);

        initRadioButton(radioBtn);
    }

//    private void getExtraGameScore() {
//        Intent intent = getIntent();
//        String score = intent.getStringExtra(GamePage.GAME_SCORE_KEY);
//
//        if(score != null) {
//            if (Integer.parseInt(score) > loadGameRecordNumeric())
//                saveScore(Integer.parseInt(score));
//        }
//    }

//    private void getExtraRecord() {
//        Intent intent = getIntent();
//        int record = intent.getIntExtra(MainPage.GAME_RECORD_KEY, -1);
//        String currentRecord = loadGameRecord();
//        if (currentRecord.equals(getString(R.string.no_record)))
//            saveScore(record);
//        else
//            if (Integer.parseInt(currentRecord) < record)
//                saveScore(record);
//    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void updateRecordView(){
        TextView recordView = findViewById(R.id.tv_record);
        // TODO: Load record
        int record = -1;

        recordView.setText(getString(R.string.record_is) +  loadGamestateFromFile().toString());
    }

    public void updateBoardView(){
        TextView boardView = findViewById(R.id.tv_boardSize);
        boardView.setText(getString(R.string.board_size) + boardSize.value + "X" + boardSize.value);
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.rbtn_beginner:
                if (checked)
                    setBoardSize(BoardSize.BEGINNER);
                break;
            case R.id.rbtn_intermediate:
                if (checked)
                    setBoardSize(BoardSize.INTERMEDIATE);
                break;
            case R.id.rbtn_professional:
                if (checked)
                    setBoardSize(BoardSize.PROFESSIONAL);
                break;
        }

        updateBoardView();
        updateRecordView();
    }

    public void onPlayButtonClicked(View view) {
        Intent intent = new Intent(MainPage.this, GamePage.class);
        intent.putExtra(MainPage.BOARD_SIZE_KEY,boardSize.toString());
        startActivity(intent);
    }

    public void onTopScoredButtonClicked(View view) {
        Intent intent = new Intent(MainPage.this, ScorePage.class);
        startActivity(intent);
    }

//    private String loadGameRecord() {
//        SharedPreferences pref = getPreferences(Context.MODE_PRIVATE);
//        int record = pref.getInt(boardSize.toString(), -1);
//
//        if (record==-1)
//            return getString(R.string.no_record);
//
//        return String.valueOf(record);
//    }

//    private int loadGameRecordNumeric() {
//        SharedPreferences pref = getPreferences(Context.MODE_PRIVATE);
//        return pref.getInt(boardSize.toString(), -1);
//    }

//    private void saveScore(int calculateScore) {
//        SharedPreferences pref = getPreferences(Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = pref.edit();
//
//        editor.putInt(boardSize.toString(), calculateScore);
//        editor.apply();
//    }

//    private void saveBoardSize() {
//        SharedPreferences pref = getPreferences(Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = pref.edit();
//        editor.putString(BOARD_SIZE_KEY, boardSize.toString());
//        editor.apply();
//    }

//    private String loadBoardSize() {
//        SharedPreferences pref = getPreferences(Context.MODE_PRIVATE);
//        String s= pref.getString(BOARD_SIZE_KEY, BoardSize.BEGINNER.toString());
//        return s;
//    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private ArrayList<Integer> loadGamestateFromFile() {
        com.afeka.minesweeper.FileHelper fh = new com.afeka.minesweeper.FileHelper();
        String filePath = getFilesDir().getPath() + FileHelper.SCORE_FILE_PREFIX + this.boardSize.toString()+ "/";
        String fileName = "scores.txt";
        ScoreHelper scores = fh.readGameFromFile(filePath, fileName);

        if (scores != null) {
//            IntStream.range(0, scores.getScores().size())
//                    .forEach(i -> Log.i(TAG,"Score "+i+" has a "+ scores.getScores().get(i)));
            ArrayList<Integer> list = scores.getScores();
//            ArrayList<Integer> sortedList = scores.getScores().stream().sorted((i1, i2) -> i1.compareTo(i2)).collect(Collectors.toList());
//                    .forEach(i -> Log.i(TAG,"Score "+i+" has a "+ list.get(i)));
            Log.i(TAG,"Score "+list.toString());
            return list;
        }
        return new ArrayList<Integer>();
    }
}

