package com.afeka.minesweeper.util;

public enum BoardSize {
    BEGINNER(5),
    INTERMEDIATE(7),
    PROFESSIONAL(10);

    public final int value;

    private BoardSize(int value){
        this.value=value;
    }
}
