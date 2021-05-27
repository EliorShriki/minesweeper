package com.afeka.minesweeper;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class FileHelper {
    public static final String SCORE_FILE_PREFIX = "/Score";

    public void saveBoardJSONToFile(int game, String filePath, String fileName) throws IOException {
        ScoreHelper scores = readGameFromFile(filePath,fileName);
        if ( scores == null )
            scores = new ScoreHelper();
        scores.addScore(game);
        Gson gson = new Gson();
        createPathIfNeeded(filePath);
        FileWriter fw = new FileWriter(filePath + "\\" + fileName);
        gson.toJson(scores, fw);
        fw.flush(); //flush data to file   <---
        fw.close(); //close write

    }

    private void createPathIfNeeded(String filePath) {

        File direct = new File(filePath);
        if(!direct.exists()) {
            direct.mkdirs();
        }

    }

    public ScoreHelper readGameFromFile(String filePath, String fileName) {

        try {
            // create Gson instance
            Gson gson = new Gson();

            // create a reader
            Reader jsonReader = new FileReader(filePath + "\\" + fileName);

            // convert JSON string to Game object
            ScoreHelper game = gson.fromJson(jsonReader,ScoreHelper.class);

            // close reader
            jsonReader.close();

            return game;

        } catch (Exception ex) {
            ex.printStackTrace();
            return new ScoreHelper();
        }
    }
}
