package com.teaminversion.envisionbuddy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
                Intent intent = new Intent(ModelsActivity.this, ChoiceActivity.class);
                intent.putExtra("text", "");
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ModelsActivity.this, ChoiceActivity.class);
        intent.putExtra("text", "");
        startActivity(intent);
        finish();
    }
}