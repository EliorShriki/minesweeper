package com.afeka.minesweeper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ScoreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScoreFragment extends Fragment {

    interface ScoreFragmentListener {

        public void startTapped();

        public void stopTapped();

    }
    final static String TAG = "ScoreFragment";

    public static final String ARG_FILE_PATH = "FILE_PATH";

    public static final String ARG_BOARD_SIZE = "BOARD_SIZE";

    private ScoreFragmentListener listener;

    private int listVisibiltyFlag;
    private String filePath;
    private String boardSize;
    public Button button;

    public ScoreFragment() {
        // Required empty public constructor
    }

    public void setListener(ScoreFragmentListener listener) {
        this.listener = listener;
    }

    public static ScoreFragment newInstance(String filePath, String boardSize) {
        ScoreFragment fragment = new ScoreFragment();
        Bundle args = new Bundle();
        args.putString(ARG_FILE_PATH,filePath);
        args.putString(ARG_BOARD_SIZE,boardSize);
        fragment.setArguments(args);
        return fragment;
    }

    public void setBoardSize(String boardSize) {
        this.boardSize=boardSize;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listVisibiltyFlag = -1;
        if (getArguments() != null) {
            Log.d(TAG, "On Create started!");
            this.filePath = getArguments().getString(ARG_FILE_PATH);
            this.boardSize = getArguments().getString(ARG_BOARD_SIZE);
        }
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_score, container, false);

//        fragmentView.setBackgroundColor(Color.GREEN);
//        fragmentView.setVisibility(View.VISIBLE);
        // Inflate the layout for this fragment
        return fragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        button = view.findViewById(R.id.fragButton);
        button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                startTapped(v);
            }
        });

        ListView listView = (ListView)view.findViewById(R.id.listview);
        ArrayList<String> arrayList = new ArrayList<>();
//        arrayList.add("Professional");
//        arrayList.add("Score1");
//        arrayList.add("Score2");
//        arrayList.add("Score3");
//        arrayList.add("Score4");
//        arrayList.add("Score5");
//        arrayList.add("Score6");
//        arrayList.add("Score7");
//        arrayList.add("Score8");
//        arrayList.add("Score9");
//        arrayList.add("Score10");
//        arrayList.add("Intermediate");
//        arrayList.add("Score2");
//        arrayList.add("Score3");
//        arrayList.add("Score4");
//        arrayList.add("Score5");
//        arrayList.add("Score6");
//        arrayList.add("Score7");
//        arrayList.add("Score8");
//        arrayList.add("Score9");
//        arrayList.add("Score10");
//        arrayList.add("Beginner");
//        arrayList.add("Score2");
//        arrayList.add("Score3");
//        arrayList.add("Score4");
//        arrayList.add("Score5");
//        arrayList.add("Score6");
//        arrayList.add("Score7");
//        arrayList.add("Score8");
//        arrayList.add("Score9");
//        arrayList.add("Score10");


        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1,arrayList);

        listView.setAdapter(arrayAdapter);
        listView.setVisibility(View.GONE);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        listener = (ScoreFragmentListener) context;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void updateScores(){
        ArrayList<Integer> arrayList = null;
        ListView listView = (ListView)getActivity().findViewById(R.id.listview);

        arrayList = loadGameRecordFromFile();


        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1,arrayList);

        listView.setAdapter(arrayAdapter);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void startTapped(View v) {
        Log.d(TAG, "start button tapped!");
        ArrayList<Integer> arrayList = null;
        ListView listView = (ListView)getActivity().findViewById(R.id.listview);

        if(listener != null) {
            listener.startTapped();
            listVisibiltyFlag*=-1;
            if (listVisibiltyFlag==1) {
                arrayList = loadGameRecordFromFile();
                listView.setVisibility(View.VISIBLE);
                ((Button) v).setText(R.string.Hide);
                ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1,arrayList);

                listView.setAdapter(arrayAdapter);
            }
            else {
                listView.setVisibility(View.GONE);
                ((Button) v).setText(R.string.topScores);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public ArrayList<Integer> loadGameRecordFromFile() {
        com.afeka.minesweeper.FileHelper fh = new com.afeka.minesweeper.FileHelper();
        String filePath = this.filePath + FileHelper.SCORE_FILE_PREFIX + this.boardSize + "/";
        String fileName = "scores.txt";
        ScoreHelper scores = fh.readGameFromFile(filePath, fileName);

        ArrayList<Integer> list = scores.getScores();

        Log.i(TAG,"Score "+list.toString());
        return list;
    }

    public void stopTapped(View v) {
        Log.d(TAG, "stop button tapped!");
        if(listener != null) {
            listener.stopTapped();
        }

    }
}