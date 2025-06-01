package com.example.schoolsystemapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class Adapter_teacherList extends RecyclerView.Adapter<Adapter_teacherList.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(String teacherName);
    }

    private Context context;
    private String[] teacherNames;
    private OnItemClickListener listener;

    public Adapter_teacherList(String[] teacherNames, Context context, OnItemClickListener listener) {
        this.context = context;
        this.teacherNames = teacherNames;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listitem, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CardView cardView = holder.cardView;
        TextView textView = cardView.findViewById(R.id.itemTitle);
        textView.setText(teacherNames[position]);

        cardView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(teacherNames[position]);
            }
        });
    }

    @Override
    public int getItemCount() {
        return teacherNames.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;

        public ViewHolder(CardView cardView) {
            super(cardView);
            this.cardView = cardView;
        }
    }
}
