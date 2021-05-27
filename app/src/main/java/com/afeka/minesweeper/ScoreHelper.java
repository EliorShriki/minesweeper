package com.afeka.minesweeper;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

public class ScoreHelper {
    @SerializedName("scores")
    private ArrayList<Integer> scores;

    public ScoreHelper(){
        scores = new ArrayList<Integer>();
    }

    public ScoreHelper(ArrayList scores){
        this.scores = scores;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public ArrayList<Integer> getScores() {
        return (ArrayList<Integer>) scores.stream().sorted((i1, i2) -> i1.compareTo(i2)).collect(Collectors.toList());
    }

    public void addScore(Integer score) {
        if (this.scores.size() < 10)
            this.scores.add(score);
        else
        {
            int minItem = this.scores.indexOf(Collections.min(this.scores));
            if (this.scores.get(minItem) < score) {
                this.scores.remove(minItem);
                this.scores.add(score);
            }
        }
    }
}
