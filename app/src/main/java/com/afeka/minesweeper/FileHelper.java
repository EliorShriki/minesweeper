package com.afeka.minesweeper;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;

public class FileHelper {

    public void saveBoardJSONToFile(int game, String filePath, String fileName) throws IOException {

        Gson gson = new Gson();
        createPathIfNeeded(filePath);
        FileWriter fw = new FileWriter(filePath + "\\" + fileName);
        gson.toJson(game, fw);
        fw.flush(); //flush data to file   <---
        fw.close(); //close write

    }

    private void createPathIfNeeded(String filePath) {

        File direct = new File(filePath);
        if(!direct.exists()) {
            direct.mkdirs();
        }

    }

    public Integer readGameFromFile(String filePath, String fileName) {

        try {
            // create Gson instance
            Gson gson = new Gson();

            // create a reader
            Reader jsonReader = new FileReader(filePath + "\\" + fileName);

            // convert JSON string to Game object
            int game = gson.fromJson(jsonReader,Integer.class);

            // close reader
            jsonReader.close();

            return game;

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
