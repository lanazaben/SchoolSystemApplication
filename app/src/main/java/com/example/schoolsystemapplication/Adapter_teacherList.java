package com.example.schoolsystemapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolsystemapplication.Data.Student;
import com.example.schoolsystemapplication.Data.Teacher;

import java.util.ArrayList;
import java.util.List;

public class Adapter_teacherList extends RecyclerView.Adapter<Adapter_teacherList.ViewHolder> implements Filterable {

    public interface OnItemClickListener {
        void onItemClick(String teacherName);
    }

    private Context context;
    private List<Teacher> teachers;
    private List<Teacher> fullList;

    public Adapter_teacherList(List<Teacher> teachers, Context context) {
        this.context = context;
        this.teachers = teachers;
        this.fullList = new ArrayList<>(teachers);
    }

    public void setTeachers(List<Teacher> updatedTeachers) {
        this.teachers.clear();
        this.teachers.addAll(updatedTeachers);
        this.fullList.clear();
        this.fullList.addAll(updatedTeachers);
        notifyDataSetChanged();
    }

    private Filter teacherFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Teacher> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(fullList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Teacher teacher : fullList) {
                    if (teacher.getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(teacher);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            teachers.clear();
            teachers.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    @Override
    public Filter getFilter() {
        return teacherFilter;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listitem, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Teacher teacher = teachers.get(position);
        CardView cardView = holder.cardView;
        TextView textView = cardView.findViewById(R.id.listName);
        textView.setText(teacher.getName());

        cardView.setOnClickListener(v -> {
            //
        });
    }

    @Override
    public int getItemCount() {
        return teachers.toArray().length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;

        public ViewHolder(CardView cardView) {
            super(cardView);
            this.cardView = cardView;
        }
    }
}
