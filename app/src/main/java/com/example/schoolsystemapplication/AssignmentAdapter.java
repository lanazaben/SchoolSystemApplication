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
        List<Assignment> assignmentList;

        public AssignmentAdapter(List<Assignment> assignmentList) {
            this.assignmentList = assignmentList;
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
            holder.tvTitle.setText(item.getTitle());
            holder.tvSubject.setText(item.getSubject());
            holder.tvDueDate.setText(item.getDueDate());
        }

        @Override
        public int getItemCount() {
            return assignmentList.size();
        }

        static class AssignmentViewHolder extends RecyclerView.ViewHolder {
            TextView tvTitle, tvSubject, tvDueDate;

            public AssignmentViewHolder(@NonNull View itemView) {
                super(itemView);
                tvTitle = itemView.findViewById(R.id.tvTitle);
                tvSubject = itemView.findViewById(R.id.tvSubject);
                tvDueDate = itemView.findViewById(R.id.tvDueDate);
            }
        }
    }

