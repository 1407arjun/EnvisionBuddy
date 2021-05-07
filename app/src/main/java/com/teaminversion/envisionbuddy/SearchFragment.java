package com.teaminversion.envisionbuddy;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchFragment extends Fragment {

    ArrayList<Map<String, String>> searchList = new ArrayList<>();
    InputMethodManager manager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_search, container, false);

        manager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (getActivity().getCurrentFocus() != null) {
            manager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
            getActivity().getCurrentFocus().clearFocus();
        }

        TextInputLayout inputLayout = root.findViewById(R.id.filledTextField);
        EditText searchText = root.findViewById(R.id.editText);

        RecyclerView searchRecyclerView = root.findViewById(R.id.searchRecyclerView);
        SearchRecyclerViewAdapter searchAdapter = new SearchRecyclerViewAdapter(searchList ,getActivity());
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(),2);
        searchRecyclerView.setLayoutManager(layoutManager);
        searchRecyclerView.setAdapter(searchAdapter);
        Button searchButton = root.findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputLayout.setError(null);
                searchList.clear();
                String word = searchText.getText().toString();
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
                        for (int i = 0; i < searchResults.size(); i++) {
                            if (searchResults.get(i).getSource().equals("Poly")) {
                                Map<String, String> modelInfo = new HashMap<>();
                                modelInfo.put("name", searchResults.get(i).getName());
                                modelInfo.put("thumbnail", searchResults.get(i).getThumbnail());
                                modelInfo.put("url", searchResults.get(i).getGltfUrl());
                                searchList.add(modelInfo);
                            }
                        }
                        if (!searchList.isEmpty()) {
                            searchAdapter.notifyDataSetChanged();
                        } else {
                            inputLayout.setError("No 3D models found");
                        }
                        InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        manager.hideSoftInputFromWindow(searchText.getWindowToken(), 0);
                        searchText.clearFocus();
                    }

                    @Override
                    public void onFailure(Call<ArrayList<JSONProcessActivity>> call, Throwable t) {
                        inputLayout.setError("Couldn't fetch data");
                    }
                });
            }
        });
        return root;
    }
}