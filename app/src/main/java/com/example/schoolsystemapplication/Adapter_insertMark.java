package com.example.schoolsystemapplication;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolsystemapplication.Data.Student;
import com.example.schoolsystemapplication.Data.Student_Mark;

import java.util.ArrayList;
import java.util.List;

public class Adapter_insertMark extends RecyclerView.Adapter<Adapter_insertMark.ViewHolder> implements Filterable {
    private Context context;
    private List<Student> students;
    private List<Student> fullList;
    private String type;
    private List<Student_Mark> studentMarks = new ArrayList<>();
    private double maxMark;
    private int subjectNum;
    private String classNum;

    public Adapter_insertMark(List<Student> students, Context context, String type, double maxMark){
        this.context = context;
        this.students = students;
        this.fullList = new ArrayList<>(students);
        this.type = type;
        this.maxMark = maxMark;
    }

    public void setSubjectNumAndclassNum(int subjectNum, String classNum) {
        this.subjectNum = subjectNum;
        this.classNum = classNum;
    }

    private Filter studentFilter = new Filter() {
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

    public void setStudents(List<Student> updatedStudents) {
        this.students.clear();
        this.students.addAll(updatedStudents);
        this.fullList.clear();
        this.fullList.addAll(updatedStudents);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView v;
        if (type.equals("student")) {
            v = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem,
                    parent, false);
        }else {
            v = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.card_add_mark,
                    parent, false);
        }
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int pos = position;
        if(type.equals("student")){
            final Student student = students.get(pos);
            CardView cardView = holder.cardView;
            TextView textView_name = (TextView) cardView.findViewById(R.id.listName);
            TextView textView_score = (TextView) cardView.findViewById(R.id.listquantity);
//        RecyclerView optionsRecycler = (RecyclerView) cardView.findViewById(R.id.nestedRecyclerView);
            textView_name.setText(student.getName());
            textView_score.setText(student.getScore()+"");
            cardView.setOnClickListener( new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    Intent intent10 = new Intent(context, viewMark_andEdit_activity.class);
                    intent10.putExtra("student_id", student.getId());
                    intent10.putExtra("subject_id", subjectNum);
                    intent10.putExtra("classNum", classNum);
                    context.startActivity(intent10);
                }
            });
        }else {
            final Student student = students.get(pos);
            CardView cardView = holder.cardView;
            TextView textView_name = (TextView) cardView.findViewById(R.id.itemTitle);
//            EditText markEditText = (EditText) cardView.findViewById(R.id.markEditText);
            holder.markEditText = cardView.findViewById(R.id.markEditText);
//        RecyclerView optionsRecycler = (RecyclerView) cardView.findViewById(R.id.optionsRecycler);
            textView_name.setText(student.getName());
            cardView.setOnClickListener( new View.OnClickListener(){
                @Override
                public void onClick(View v){
                }
            });

            if (holder.markEditText != null) {
                holder.markEditText.addTextChangedListener(new TextWatcher() {
                    Student_Mark studentMark = new Student_Mark(student.getId(), 0);
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        String markText = s.toString();
                        double mark = 0;
                        try {
                            mark = (double) Double.parseDouble(markText);
                        } catch (NumberFormatException e) {
                            mark = 0;
                        }
                        boolean updated = false;
                        for (Student_Mark sm : studentMarks) {
                            if (sm.getStudentId() == student.getId()) {
                                if (mark > maxMark){
                                    mark = maxMark;
                                }
                                sm.setMark(mark);
                                updated = true;
                                break;
                            }
                        }
                        if (!updated) {
                            studentMarks.add(new Student_Mark(student.getId(), mark));
                        }
                    }

                    @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                    @Override public void afterTextChanged(Editable s) {}
                });
            }

        }
    }

    public List<Student_Mark> getStudentMarks() {
        return studentMarks;
    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    @Override
    public Filter getFilter() {
        return studentFilter;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private CardView cardView;
        EditText markEditText;

        public ViewHolder(CardView cardView){
            super(cardView);
            this.cardView = cardView;
//            markEditText = itemView.findViewById(R.id.markEditText);
        }
    }

}