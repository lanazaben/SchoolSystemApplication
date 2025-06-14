package com.example.schoolsystemapplication;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolsystemapplication.Data.Assignment;

import java.util.List;

public class AssignmentAdapter extends RecyclerView.Adapter<AssignmentAdapter.AssignmentViewHolder> {
    private List<Assignment> assignmentList;
    private Context context;


    public AssignmentAdapter(List<Assignment> assignmentList, Context context) {
        this.assignmentList = assignmentList;
        this.context = context;
    }

    @NonNull
    @Override
    public AssignmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.asslist, parent, false);
        return new AssignmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AssignmentViewHolder holder, int position) {
        Assignment item = assignmentList.get(position);

        holder.textView1.setText(item.getSubject());
        holder.textView2.setText(item.getTitle());

        Log.d("ADAPTER_DEBUG", "Title to send: " + item.getTitle());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, StudentAssDetails.class);
            intent.putExtra("title", item.getTitle());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return assignmentList.size();
    }

    static class AssignmentViewHolder extends RecyclerView.ViewHolder {
        TextView textView1, textView2;

        public AssignmentViewHolder(@NonNull View itemView) {
            super(itemView);
            textView1 = itemView.findViewById(R.id.textView1); // Subject
            textView2 = itemView.findViewById(R.id.textView2); // Title
        }
    }
}
