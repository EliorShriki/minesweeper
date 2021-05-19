package com.afeka.minesweeper.views.grid;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.afeka.minesweeper.GameEngine;

public class Grid extends GridView{

    public int width;
    public int hieght;

    public Grid(Context context , AttributeSet attrs){
        super(context,attrs);
        int boardSize = GameEngine.getInstance().getBoardSize();
        this.width = boardSize;
        this.hieght = boardSize;
        setNumColumns(this.width);
        setAdapter(new GridAdapter());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private class GridAdapter extends BaseAdapter{

        private static final String TAG = "GridAdapter";

        @Override
        public int getCount() {
            return width * hieght;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Cell cell = GameEngine.getInstance().getCellAt(position);
//            Log.i(TAG, "getView: position:" + position + " getCellAt:" + cell.getXPos() + "," + cell.getYPos() + " val:" + cell.getValue());
            return cell;
        }
    }
}
