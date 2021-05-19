package com.afeka.minesweeper.util;

import android.util.Log;

public class PrintGrid {

    private static final String TAG = "PrintGrid";

    public static void print(final int[][] grid, final int width, final int height) {
//        Log.i(TAG, "Original Grid");
//        for( int x = 0 ; x < width ; x++ ){
//            String printedText = "| ";
//            for( int y = 0 ; y < height ; y++ ){
//                printedText += String.valueOf(grid[x][y]).replace("-1", "B") + " | ";
//            }
//            Log.i(TAG, printedText);
//        }
//        Log.i(TAG, "--------------------------------------------------------------------------------------");
        Log.i(TAG, "Layout Grid");
        for( int y = 0 ; y < height ; y++ ){
            String printedText = "| ";
            for( int x = 0 ; x < width ; x++ ){
                printedText += String.valueOf(grid[x][y]).replace("-1", "B") + " | ";
            }
            Log.i(TAG, printedText);
        }
    }
}