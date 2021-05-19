package com.afeka.minesweeper;

import android.content.Context;
import android.util.Log;

import com.afeka.minesweeper.util.GameStatus;
import com.afeka.minesweeper.util.Generator;
import com.afeka.minesweeper.util.PrintGrid;
import com.afeka.minesweeper.views.grid.Cell;

public class GameEngine {
    private static GameEngine instance;

    static final String TAG = "GameEngine";

    public static double DEFAULT_PENALTY = 0.15;

    private Context context;

    private int boardSize;

    private int numberOfBombs;

    private double penalty;

    private GameStatus gameStatus = GameStatus.PLAY;

    private Cell[][] MinesweeperGrid;

    public static GameEngine getInstance(int boardSize, Boolean isRefresh) {
        if( instance == null || isRefresh) {
            instance = new GameEngine(boardSize);
        }
        else
            if (instance.boardSize != boardSize){
                instance = new GameEngine(boardSize);
            }
        return instance;
    }

    public static GameEngine getInstance() {
        if (instance == null) {
            return null;
        }
        return instance;
    }

    private GameEngine(int boardSize){
        this.boardSize = boardSize;
        this.setPenalty(0);
        this.MinesweeperGrid = new Cell[boardSize][boardSize];
    }

    public int getBoardSize() {
        return boardSize;
    }

    public void setBoardSize(int boardSize) {
        this.boardSize = boardSize;
    }

    public int getNumberOfBombs() {
        return numberOfBombs;
    }

    public void setNumberOfBombs(int numberOfBombs) {
        this.numberOfBombs = numberOfBombs;
    }

    public double getPenalty() {
        return penalty;
    }

    public void setPenalty(double penalty) {
        this.penalty = penalty;
        Log.e(TAG, "setPenalty: " + penalty);
        int numOfBomb=(int) ((this.penalty + DEFAULT_PENALTY) * (this.boardSize * this.boardSize));
        if (MinesweeperGrid != null)
            this.updateGridPenalty();
        if (getAvailableCells() > numOfBomb - this.getNumberOfBombs())
            this.setNumberOfBombs(numOfBomb);
        else
            this.setGameStatus(GameStatus.LOSE);

    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(GameStatus gameStatus) {
        if (this.gameStatus.equals(GameStatus.PLAY) && gameStatus.equals(GameStatus.PENALTY)) {
            Log.i(TAG, "setGameStatus: play to penalty" + gameStatus.toString());
            this.setPenalty((this.getPenalty() == 0) ? 0.1 : this.getPenalty() * 2);
        }
        this.gameStatus = gameStatus;
    }

    public void createGrid(Context context){
        this.context = context;
        int[][] GeneratedGrid = Generator.generate(
                this.getNumberOfBombs(),
                this.getBoardSize(),
                this.getBoardSize()
        );
        PrintGrid.print(
                GeneratedGrid,
                this.getBoardSize(),
                this.getBoardSize()
        );
        setGrid(context,GeneratedGrid);
    }

    private void updateGridPenalty(){
        Log.i(TAG, "updateGridPenalty: ");

        int[][] GeneratedGrid = Generator.regenerate(
                this.getGrid(),
                this.getNumberOfBombs(),
                this.getBoardSize(),
                this.getBoardSize()
        );
        PrintGrid.print(
                GeneratedGrid,
                this.getBoardSize(),
                this.getBoardSize()
        );
        updateGrid(GeneratedGrid);
    }

    private void setGrid( final Context context, final int[][] grid ){
        for( int x = 0 ; x < this.getBoardSize() ; x++ ){
            for( int y = 0 ; y < this.getBoardSize() ; y++ ){
                if( MinesweeperGrid[x][y] == null ){
                    MinesweeperGrid[x][y] = new Cell( context , x,y);
                }
                MinesweeperGrid[x][y].setValue(grid[x][y]);
                MinesweeperGrid[x][y].invalidate();
            }
        }
    }

    private void updateGrid( final int[][] grid ){
        for( int x = 0 ; x < this.getBoardSize() ; x++ ){
            for( int y = 0 ; y < this.getBoardSize() ; y++ ){
                if( !MinesweeperGrid[x][y].isClicked() && !MinesweeperGrid[x][y].isBomb() && !MinesweeperGrid[x][y].isRevealed()){
//                    MinesweeperGrid[x][y] = new Cell( context , x,y);
                    MinesweeperGrid[x][y].setValue(grid[x][y]);
                }
                else
                    MinesweeperGrid[x][y].updateValue(grid[x][y]);
                MinesweeperGrid[x][y].invalidate();
            }
        }
    }
    private int[][] getGrid(){
        int[][] grid = new int [this.getBoardSize()][this.getBoardSize()];
        for( int x = 0 ; x < this.getBoardSize() ; x++ ){
            for( int y = 0 ; y < this.getBoardSize() ; y++ ){
                if( getCellAt(x,y).isBomb() )
                    grid[x][y] = -1; // Bomb
                else{
                    if( getCellAt(x,y).isClicked() || getCellAt(x,y).isRevealed())
                        grid[x][y] = -2; // Clicked
                    else
                        grid[x][y] = 0; // Not Reveled
                }
            }
        }
        return grid;
    }

    public Cell getCellAt(int position) {
        int x = position % this.getBoardSize();
        int y = position / this.getBoardSize();

        return MinesweeperGrid[x][y];
    }

    public Cell getCellAt( int x , int y ){
        return MinesweeperGrid[x][y];
    }

    public int getAvailableCells(){
        int counter = 0;
        if (this.MinesweeperGrid == null)
            return this.getBoardSize() * this.getBoardSize();
        for( int y = 0 ; y < this.getBoardSize() ; y++ )
            for( int x = 0 ; x < this.getBoardSize() ; x++ )
                if( !getCellAt(x,y).isRevealed()  )
                    counter++;
        return counter;
    }

    public void click( int x , int y ){
        if (getGameStatus().equals(GameStatus.PLAY) || getGameStatus().equals(GameStatus.PENALTY)) {
            if (x >= 0 && y >= 0 && x < this.getBoardSize() && y < this.getBoardSize() && !getCellAt(x, y).isClicked()) {
                getCellAt(x, y).setClicked();
                if (getCellAt(x, y).getValue() == 0) {
                    for (int xt = -1; xt <= 1; xt++) {
                        for (int yt = -1; yt <= 1; yt++) {
                            if (xt != yt) {
                                click(x + xt, y + yt);
                            }
                        }
                    }
                }

                if (getCellAt(x, y).isBomb()) {
                    onGameLost();
                }
            }

            checkEnd();
        }
    }

    private boolean checkEnd(){
        int bombNotFound = this.getNumberOfBombs();
        int notRevealed = this.getBoardSize() * this.getBoardSize();
        for ( int x = 0 ; x < this.getBoardSize() ; x++ ){
            for( int y = 0 ; y < this.getBoardSize() ; y++ ){
                if( getCellAt(x,y).isRevealed() || getCellAt(x,y).isFlagged() ){
                    notRevealed--;
                }

                if( getCellAt(x,y).isFlagged() && getCellAt(x,y).isBomb() ){
                    bombNotFound--;
                }
            }
        }
        if( bombNotFound == 0 && notRevealed == 0 ){
            setGameStatus(GameStatus.WIN);
        }
        return false;
    }

    public void flag( int x , int y ){
        boolean isFlagged = getCellAt(x,y).isFlagged();
        getCellAt(x,y).setFlagged(!isFlagged);
        getCellAt(x,y).invalidate();
        checkEnd();
    }

    private void onGameLost(){
        // handle lost game
        setGameStatus(GameStatus.LOSE);

        for ( int x = 0 ; x < this.getBoardSize() ; x++ ) {
            for (int y = 0; y < this.getBoardSize(); y++) {
                getCellAt(x,y).setRevealed();
            }
        }
    }
}
