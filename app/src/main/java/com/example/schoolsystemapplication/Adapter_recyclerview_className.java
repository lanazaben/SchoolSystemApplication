package com.example.schoolsystemapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Adapter_recyclerview_className extends RecyclerView.Adapter<Adapter_recyclerview_className.ViewHolder> {
    private Context context;
    private String[] className;
    private String nav;

    public Adapter_recyclerview_className(String[] className, Context context, String nav){
        this.context = context;
        this.className = className;
        this.nav = nav;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView v = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_layout,
                parent,
                false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CardView cardView = holder.cardView;
        TextView textView = (TextView) cardView.findViewById(R.id.itemTitle);
        RecyclerView optionsRecycler = (RecyclerView) cardView.findViewById(R.id.optionsRecycler);
        textView.setText(className[position]);
        cardView.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (optionsRecycler.getVisibility() == View.GONE){
                    String[] subject = {"Arabic", "English" ,"math"};
                    optionsRecycler.setLayoutManager(new LinearLayoutManager(context));
                    OptionsAdapter_recyclerview adapter = new OptionsAdapter_recyclerview(subject, context, nav);
                    optionsRecycler.setAdapter(adapter);
                    optionsRecycler.setVisibility(View.VISIBLE);
                }else {
                    optionsRecycler.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return className.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private CardView cardView;
        public ViewHolder(CardView cardView){
            super(cardView);
            this.cardView = cardView;
        }

    }
}
