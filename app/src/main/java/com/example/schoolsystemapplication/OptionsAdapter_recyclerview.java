package com.example.schoolsystemapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolsystemapplication.Data.SchoolSubject;
import com.example.schoolsystemapplication.Data.Student;

import java.util.ArrayList;
import java.util.List;

public class OptionsAdapter_recyclerview extends RecyclerView.Adapter<OptionsAdapter_recyclerview.ViewHolder> implements Filterable {
    private Context context;
    private List<SchoolSubject> subjects;
    private String nav;
    private String classNum;
    private List<SchoolSubject> fullList;

    public  OptionsAdapter_recyclerview(List<SchoolSubject> subjects, Context context, String nav, String classNum){
        this.context = context;
        this.subjects = subjects;
        this.nav = nav;
        this.classNum = classNum;
        this.fullList = new ArrayList<>(subjects);
    }

    @Override
    public OptionsAdapter_recyclerview.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView v = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_layout,
                parent,
                false);
        return new OptionsAdapter_recyclerview.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(OptionsAdapter_recyclerview.ViewHolder holder, int position) {
        int pos = position;
        SchoolSubject subject = subjects.get(pos);
        CardView cardView = holder.cardView;
        TextView textView = (TextView) cardView.findViewById(R.id.itemTitle);
        textView.setText(subject.getName());
        cardView.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (nav.equals("studentWantsSubjects")){
                    SharedPreferences sp = context.getSharedPreferences("student_session", context.MODE_PRIVATE);
                    int student_id = Integer.parseInt(sp.getString("student_id", "0"));
                    Intent intent = new Intent(context , ViewMarks.class);
                    intent.putExtra("student_id", student_id);
                    intent.putExtra("subject_id",subject.getId());
                    context.startActivity(intent);
                }else if (nav.equals("marks")){
                    Intent intent = new Intent(context , StudentList_insertMark.class);
                    intent.putExtra("classNum", classNum);
                    intent.putExtra("subject",subject.getId());
                    context.startActivity(intent);
                } else if (nav.equals("addmarks")){
                    Intent intent = new Intent(context , AddExamActivity.class);
                    intent.putExtra("classNum", classNum);
                    intent.putExtra("subject",subject.getId());
                    context.startActivity(intent);
                } else {
                    Intent intent = new Intent(context , UploadAsgByTeacher.class);
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return subjects.toArray().length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private CardView cardView;
        public ViewHolder(CardView cardView){
            super(cardView);
            this.cardView = cardView;
        }

    }

    private Filter subjectFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<SchoolSubject> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(fullList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (SchoolSubject subject : fullList) {
                    if (subject.getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(subject);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            subjects.clear();
            subjects.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    @Override
    public Filter getFilter() {
        return subjectFilter;
    }

    public void setStudents(List<SchoolSubject> updatedStudents) {
        this.subjects.clear();
        this.subjects.addAll(updatedStudents);
        this.fullList.clear();
        this.fullList.addAll(updatedStudents);
        notifyDataSetChanged();
    }
}