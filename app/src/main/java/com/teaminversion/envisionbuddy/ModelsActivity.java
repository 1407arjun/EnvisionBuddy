package com.teaminversion.envisionbuddy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ModelsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_models);
        RecyclerView modelsRecyclerView = findViewById(R.id.modelsRecyclerView);

        // added data from arraylist to adapter class.
        ModelsRecyclerViewAdapter modelsAdapter = new ModelsRecyclerViewAdapter(ChoiceActivity.models,this);

        // setting grid layout manager to implement grid view.
        // in this method '2' represents number of columns to be displayed in grid view.
        GridLayoutManager layoutManager = new GridLayoutManager(this,2);

        // at last set adapter to recycler view.
        modelsRecyclerView.setLayoutManager(layoutManager);
        modelsRecyclerView.setAdapter(modelsAdapter);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ModelsActivity.this, ChoiceActivity.class));
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ModelsActivity.this, ChoiceActivity.class));
        finish();
    }
}