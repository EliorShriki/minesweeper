package com.afeka.minesweeper.util;

import android.util.Log;

import com.afeka.minesweeper.views.grid.Cell;

import java.util.Random;


public class Generator {

    static final String TAG = "Generator";

    public static int[][] generate( int bombnumber , final int width , final int height){
        // Random for generating numbers
        Random r = new Random();

        int [][] grid = new int[width][height];
        for( int x = 0 ; x< width ;x++ ){
            grid[x] = new int[height];
        }

        while( bombnumber > 0 ){
            int x = r.nextInt(width);
            int y = r.nextInt(height);

            // -1 is the bomb
            if( grid[x][y] != -1 ){
                grid[x][y] = -1;
                bombnumber--;
            }
        }
        grid = calculateNeigbours(grid,width,height);

        return grid;
    }

    public static int[][] regenerate( int[][] currentGrid, int bombnumber , final int width , final int height){
        // Random for generating numbers
        Random r = new Random();

        Log.i(TAG, "regenerate: ");

        int currentBombs=0;
        int available = 0;
        int x,y;
//        int [][] grid = new int[width][height];
//        for( int x = 0 ; x< width ;x++ ){
//            grid[x] = new int[height];
//        }
        for( x = 0 ; x < width ; x++ ) {
            for (y = 0; y < height; y++) {
                if (currentGrid[x][y] == -1)
                    currentBombs++;
                else if (currentGrid[x][y] == 0)
                    available++;
            }
        }

        while ((bombnumber - currentBombs) > 0) {
            x = r.nextInt(width);
            y = r.nextInt(height);

            if (currentGrid[x][y] == 0) {
                currentGrid[x][y] = -1;
                currentBombs++;
            }
        }
        currentGrid = calculateNeigbours(currentGrid, width, height);

        return currentGrid;
    }

    private static int[][] calculateNeigbours( int[][] grid , final int width , final int height){
        for( int x = 0 ; x < width ; x++){
            for( int y = 0 ; y < height ; y++){
                grid[x][y] = getNeighbourNumber(grid,x,y,width,height);
            }
        }

        return grid;
    }

    private static int getNeighbourNumber( final int grid[][] , final int x , final int y , final int width , final int height){
        if( grid[x][y] == -1 ){
            return -1;
        }

        int count = 0;

        if( isMineAt(grid,x - 1 ,y + 1,width,height)) count++; // top-left
        if( isMineAt(grid,x     ,y + 1,width,height)) count++; // top
        if( isMineAt(grid,x + 1 ,y + 1,width,height)) count++; // top-right
        if( isMineAt(grid,x - 1 ,y    ,width,height)) count++; // left
        if( isMineAt(grid,x + 1 ,y    ,width,height)) count++; // right
        if( isMineAt(grid,x - 1 ,y - 1,width,height)) count++; // bottom-left
        if( isMineAt(grid,x     ,y - 1,width,height)) count++; // bottom
        if( isMineAt(grid,x + 1 ,y - 1,width,height)) count++; // bottom-right

        return count;
    }

    private static boolean isMineAt( final int [][] grid, final int x , final int y , final int width , final int height){
        if( x >= 0 && y >= 0 && x < width && y < height ){
            if( grid[x][y] == -1 ){
                return true;
            }
        }
        return false;
    }

}
