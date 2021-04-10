package com.teaminversion.envisionbuddy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class MainRecyclerViewAdapter extends RecyclerView.Adapter<MainRecyclerViewAdapter.RecyclerViewHolder> {

    private ArrayList<Map<String, String>> arrayList;
    private final Context context;

    public MainRecyclerViewAdapter(ArrayList<Map<String, String>> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_layout_main, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        holder.recentTextView.setText(arrayList.get(position).get("name"));
        Picasso.with(context).load(arrayList.get(position).get("thumbnail"))
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_error_placeholder)
                .into(holder.mainImageView);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> word = arrayList.get(position);
                context.startActivity(new Intent(context, ModelsActivity.class));
                arrayList.remove(position);
                arrayList.add(word);
                SharedPreferences sharedPreferences = context.getSharedPreferences("com.teaminversion.envisionbuddy", Context.MODE_PRIVATE);
                try {
                    sharedPreferences.edit().putString("recentList", ObjectSerializer.serialize(arrayList)).apply();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                notifyDataSetChanged();
                Intent sceneViewerIntent = new Intent(Intent.ACTION_VIEW);
                sceneViewerIntent.setData(Uri.parse("https://arvr.google.com/scene-viewer/1.0?file=" + word.get("url")));
                sceneViewerIntent.setPackage("com.google.android.googlequicksearchbox");
                context.startActivity(sceneViewerIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private TextView recentTextView;
        private ImageView mainImageView;
        private CardView cardView;

        public RecyclerViewHolder(@NonNull View view) {
            super(view);
            recentTextView = view.findViewById(R.id.recentTextView);
            mainImageView = view.findViewById(R.id.mainImageView);
            cardView = view.findViewById(R.id.cardView);
        }
    }
}
