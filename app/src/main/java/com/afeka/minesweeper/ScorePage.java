package com.afeka.minesweeper;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ScorePage extends AppCompatActivity {

    String text = "Item";

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_page);
        getSupportActionBar().hide();

        listView = (ListView)findViewById(R.id.listview);
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Professional");
        arrayList.add("Score1");
        arrayList.add("Score2");
        arrayList.add("Score3");
        arrayList.add("Score4");
        arrayList.add("Score5");
        arrayList.add("Score6");
        arrayList.add("Score7");
        arrayList.add("Score8");
        arrayList.add("Score9");
        arrayList.add("Score10");
        arrayList.add("Intermediate");
        arrayList.add("Score2");
        arrayList.add("Score3");
        arrayList.add("Score4");
        arrayList.add("Score5");
        arrayList.add("Score6");
        arrayList.add("Score7");
        arrayList.add("Score8");
        arrayList.add("Score9");
        arrayList.add("Score10");
        arrayList.add("Beginner");
        arrayList.add("Score2");
        arrayList.add("Score3");
        arrayList.add("Score4");
        arrayList.add("Score5");
        arrayList.add("Score6");
        arrayList.add("Score7");
        arrayList.add("Score8");
        arrayList.add("Score9");
        arrayList.add("Score10");

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,arrayList);

        listView.setAdapter(arrayAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}