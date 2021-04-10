package com.teaminversion.envisionbuddy;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ChoiceRecyclerViewAdapter extends RecyclerView.Adapter<ChoiceRecyclerViewAdapter.RecyclerViewHolder> {

    private ArrayList<String> arrayList;
    private final Context context;

    public ChoiceRecyclerViewAdapter(ArrayList<String> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_layout_choice, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        holder.choiceTextView.setText(arrayList.get(position));
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String word = arrayList.get(position);
                ProgressDialog progress = new ProgressDialog(context);
                progress.setMessage("Retrieving data");
                progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progress.setIndeterminate(true);
                progress.setCancelable(false);
                progress.show();
                ChoiceActivity.models.clear();
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(API.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                API myApi = retrofit.create(API.class);
                Call<ArrayList<JSONProcessActivity>> call = myApi.getResult("old-smoke-4544", word);
                call.enqueue(new Callback<ArrayList<JSONProcessActivity>>() {
                    @Override
                    public void onResponse(Call<ArrayList<JSONProcessActivity>> call, Response<ArrayList<JSONProcessActivity>> response) {
                        ArrayList<JSONProcessActivity> searchResults = response.body();
                        for (int i=0; i<searchResults.size(); i++){
                            if (searchResults.get(i).getSource().equals("Poly")) {
                                Map<String, String> modelInfo = new HashMap<>();
                                modelInfo.put("name", searchResults.get(i).getName());
                                modelInfo.put("thumbnail", searchResults.get(i).getThumbnail());
                                modelInfo.put("url", searchResults.get(i).getGltfUrl());
                                ChoiceActivity.models.add(modelInfo);
                            }
                        }

                        progress.dismiss();
                        if (!ChoiceActivity.models.isEmpty()) {
                            context.startActivity(new Intent(context, ModelsActivity.class));
                        }else{
                            Snackbar snackbar = Snackbar.make(v, "No 3D models found", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<JSONProcessActivity>> call, Throwable t) {
                        progress.dismiss();
                        Snackbar snackbar = Snackbar.make(v, "Couldn't fetch data", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private TextView choiceTextView;
        private CardView cardView;

        public RecyclerViewHolder(@NonNull View view) {
            super(view);
            choiceTextView = view.findViewById(R.id.choiceTextView);
            cardView = view.findViewById(R.id.cardView);
        }
    }

}