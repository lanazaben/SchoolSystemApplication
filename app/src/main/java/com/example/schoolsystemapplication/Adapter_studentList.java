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

public class Adapter_studentList extends RecyclerView.Adapter<Adapter_studentList.ViewHolder> implements Filterable {
    Context context;
    private List<Student> students;
    private List<Student> fullList;

    public Adapter_studentList(List<Student> students, Context context){
        this.context = context;
        this.students = students;
        this.fullList = new ArrayList<>(students);
    }

    public void setStudentss(List<Student> updatedStudentss) {
        this.students.clear();
        this.students.addAll(updatedStudentss);
        this.fullList.clear();
        this.fullList.addAll(updatedStudentss);
        notifyDataSetChanged();
    }

    private Filter teacherFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Student> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(fullList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Student student : fullList) {
                    if (student.getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(student);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            students.clear();
            students.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    @Override
    public Filter getFilter() {
        return teacherFilter;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView v = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem,
                parent,
                false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Student student = students.get(position);
        CardView cardView = holder.cardView;
        TextView textView = (TextView) cardView.findViewById(R.id.listName);
        RecyclerView optionsRecycler = (RecyclerView) cardView.findViewById(R.id.optionsRecycler);
        textView.setText(student.getName());
        cardView.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //
            }
        });
    }

    @Override
    public int getItemCount() {
        return students.toArray().length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private CardView cardView;
        public ViewHolder(CardView cardView){
            super(cardView);
            this.cardView = cardView;
        }

    }
}
