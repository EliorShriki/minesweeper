package com.afeka.minesweeper.util;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;

public class FileHelper {

    public void saveBoardJSONToFile(BoardSize boardSize, String filePath, String fileName) throws IOException {

        Gson gson = new Gson();
        createPathIfNeeded(filePath);
        FileWriter fw = new FileWriter(filePath + "\\" + fileName);
        gson.toJson(boardSize, fw);
        fw.flush(); //flush data to file   <---
        fw.close(); //close write

    }

    private void createPathIfNeeded(String filePath) {

        File direct = new File(filePath);
        if(!direct.exists()) {
            direct.mkdirs();
        }

    }

//    public Game readGameFromFile(String filePath, String fileName) {
//
//        try {
//            // create Gson instance
//            Gson gson = new Gson();
//
//            // create a reader
//            Reader jsonReader = new FileReader(filePath + "\\" + fileName);
//
//            // convert JSON string to Game object
//            Game game = gson.fromJson(jsonReader,Game.class);
//
//            // close reader
//            jsonReader.close();
//
//            return game;
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            return null;
//        }
//    }
}
