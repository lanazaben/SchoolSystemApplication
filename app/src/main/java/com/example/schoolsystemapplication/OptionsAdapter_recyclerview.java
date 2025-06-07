package com.example.schoolsystemapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class OptionsAdapter_recyclerview extends RecyclerView.Adapter<OptionsAdapter_recyclerview.ViewHolder> {
    private Context context;
    private String[] subject;
    private String nav;

    public  OptionsAdapter_recyclerview(String[] subject, Context context, String nav){
        this.context = context;
        this.subject = subject;
        this.nav = nav;
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
        CardView cardView = holder.cardView;
        TextView textView = (TextView) cardView.findViewById(R.id.itemTitle);
        textView.setText(subject[position]);
        cardView.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (nav.equals("marks")){
                    Intent intent = new Intent(context , markOrStudent_activity.class);
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
        return subject.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private CardView cardView;
        public ViewHolder(CardView cardView){
            super(cardView);
            this.cardView = cardView;
        }

    }
}