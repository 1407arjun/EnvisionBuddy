package com.teaminversion.envisionbuddy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import java.util.ArrayList;
import java.util.Map;


public class ChoiceActivity extends AppCompatActivity {

    static ArrayList<String> choiceList = new ArrayList<>();
    static ArrayList<Map<String, String>> models = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);
        RecyclerView choiceRecyclerView = findViewById(R.id.choiceRecyclerView);

        Intent intent = getIntent();
        String text = intent.getStringExtra("text");
        choiceList.clear();
        analyzeText(text);

        // added data from arraylist to adapter class.
        ChoiceRecyclerViewAdapter choiceAdapter = new ChoiceRecyclerViewAdapter(choiceList,this);

        // setting grid layout manager to implement grid view.
        // in this method '2' represents number of columns to be displayed in grid view.
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        // at last set adapter to recycler view.
        choiceRecyclerView.setLayoutManager(layoutManager);
        choiceRecyclerView.setAdapter(choiceAdapter);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChoiceActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    private void analyzeText(String inputText){
        //Code here
        choiceList.add("magnet");
        choiceList.add("brain");
        choiceList.add("plant");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ChoiceActivity.this, MainActivity.class));
        finish();
    }
}