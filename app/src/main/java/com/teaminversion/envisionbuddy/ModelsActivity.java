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

        ModelsRecyclerViewAdapter modelsAdapter = new ModelsRecyclerViewAdapter(ChoiceActivity.models,this);
        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
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