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
import java.util.Arrays;
import java.util.List;

public class MainPage extends AppCompatActivity implements ScoreFragment.ScoreFragmentListener {

    static final String TAG = "MainPage";
    static final String BOARD_SIZE_KEY = "BOARD_SIZE";
    static final String GAME_RECORD_KEY = "RECORD";

    BoardSize boardSize = BoardSize.BEGINNER;
    ScoreFragment fragment;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        setBoardSize(boardSize);
        enableRButton();
        getSupportActionBar().hide();

        fragment = ScoreFragment.newInstance(getFilesDir().getPath(),this.boardSize.toString());

        //fragment.setListener(this);

        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment, TAG).commit();

//        getExtraRecord();
//        getExtraGameScore();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onResume() {
        super.onResume();
        setBoardSize(boardSize);
        enableRButton();
//        getExtraRecord();
//        getExtraGameScore();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void initRadioButton(RadioButton radioBtn){
        radioBtn.setChecked(true);
        updateBoardView();
        updateRecordView();
    }

    public void setBoardSize(BoardSize bSize){
        boardSize = bSize;
        if (fragment != null )
            fragment.setBoardSize(bSize.toString());
//        saveBoardSize();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
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

        recordView.setText(getString(R.string.record_is) +  loadGameRecordFromFile().toString());
    }

    public void updateBoardView(){
        TextView boardView = findViewById(R.id.tv_boardSize);
        boardView.setText(getString(R.string.board_size) + boardSize.value + "X" + boardSize.value);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
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
//        fragment.startTapped(fragment.button);
        fragment.updateScores();
    }

    public void onPlayButtonClicked(View view) {
        Intent intent = new Intent(MainPage.this, GamePage.class);
        intent.putExtra(MainPage.BOARD_SIZE_KEY,boardSize.toString());
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
    public Integer loadGameRecordFromFile() {
        com.afeka.minesweeper.FileHelper fh = new com.afeka.minesweeper.FileHelper();
        String filePath = getFilesDir().getPath() + FileHelper.SCORE_FILE_PREFIX + this.boardSize.toString()+ "/";
        String fileName = "scores.txt";
        ScoreHelper scores = fh.readGameFromFile(filePath, fileName);

        if (scores != null) {

            ArrayList<Integer> list = scores.getScores();

            Log.i(TAG,"Score "+list.toString());
            return list.get(0);
        }
        return -1;
    }

    @Override
    public void startTapped() {
        Log.d(TAG, "startTapped");
    }

    @Override
    public void stopTapped() {
        Log.d(TAG, "stopTapped");
    }
}

