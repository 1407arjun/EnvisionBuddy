package com.teaminversion.envisionbuddy;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;


public class ChoiceRecyclerViewAdapter extends RecyclerView.Adapter<ChoiceRecyclerViewAdapter.RecyclerViewHolder> {

    private ArrayList<String> arrayList;
    private final Context context;

    public ChoiceRecyclerViewAdapter(ArrayList<String> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ChoiceRecyclerViewAdapter.RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate Layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_layout_choice, parent, false);
        return new ChoiceRecyclerViewAdapter.RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChoiceRecyclerViewAdapter.RecyclerViewHolder holder, int position) {
        // Set the data to textview.
        holder.choiceTextView.setText(arrayList.get(position));

         // OnClick to be added

    }

    @Override
    public int getItemCount() {
        // this method returns the size of recyclerview
        return arrayList.size();
    }

    // View Holder Class to handle Recycler View.
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