package com.teaminversion.envisionbuddy;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Map;

public class ModelsRecyclerViewAdapter extends RecyclerView.Adapter<ModelsRecyclerViewAdapter.RecyclerViewHolder> {

    private ArrayList<Map<String, String>> arrayList;
    private final Context context;

    public ModelsRecyclerViewAdapter(ArrayList<Map<String, String>> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate Layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_layout_models, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        // Set the data to textview and imageview.
        holder.nameTextView.setText(arrayList.get(position).get("name"));
        Picasso.with(context).load(arrayList.get(position).get("thumbnail"))
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_error_placeholder)
                .into(holder.modelImageView);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sceneViewerIntent = new Intent(Intent.ACTION_VIEW);
                sceneViewerIntent.setData(Uri.parse("https://arvr.google.com/scene-viewer/1.0?file=" + ChoiceActivity.models.get(position).get("url")));
                sceneViewerIntent.setPackage("com.google.android.googlequicksearchbox");
                context.startActivity(sceneViewerIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        // this method returns the size of recyclerview
        return arrayList.size();
    }

    // View Holder Class to handle Recycler View.
    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private TextView nameTextView;
        private ImageView modelImageView;
        private CardView cardView;

        public RecyclerViewHolder(@NonNull View view) {
            super(view);
            nameTextView = view.findViewById(R.id.nameTextView);
            modelImageView = view.findViewById(R.id.modelImageView);
            cardView = view.findViewById(R.id.cardView);
        }
    }
}
