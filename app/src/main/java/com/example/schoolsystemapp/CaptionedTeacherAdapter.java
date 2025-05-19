package com.example.schoolsystemapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolsystemapp.Data.Teacher;

public class CaptionedTeacherAdapter
        extends RecyclerView.Adapter<CaptionedTeacherAdapter.ViewHolder> {

    private Teacher[] teachers;
    private Context context;
    public CaptionedTeacherAdapter(Teacher[] teachers, Context context){
        this.teachers = teachers;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView v = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.card_captioned_teacher,
                parent,
                false);

        return new ViewHolder(v);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private CardView cardView;
        public ViewHolder(CardView cardView){
            super(cardView);
            this.cardView = cardView;
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CardView cardView = holder.cardView;
        TextView txt = (TextView)cardView.findViewById(R.id.txtName);
        txt.setText(teachers[position].getName());
        cardView.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(context, teacherSchedule.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return teachers.length;
    }
}