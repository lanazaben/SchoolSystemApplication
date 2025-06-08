package com.example.schoolsystemapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.schoolsystemapplication.Data.SchoolSubject;
import com.example.schoolsystemapplication.Data.Teacher;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Adapter_recyclerview_className extends RecyclerView.Adapter<Adapter_recyclerview_className.ViewHolder> {

    private Context context;
    private List<String> className;
    private String nav;
    private static final String BASE_URL = "http://10.0.2.2:80/php_project/get_subjects_use_gradeLevel.php";

    public Adapter_recyclerview_className(List<String> className, Context context, String nav) {
        this.context = context;
        this.className = className;
        this.nav = nav;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_item_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CardView cardView = holder.cardView;
        TextView textView = cardView.findViewById(R.id.itemTitle);
        RecyclerView optionsRecycler = cardView.findViewById(R.id.optionsRecycler);
        textView.setText("grade level: " + className.get(position));

        cardView.setOnClickListener(v -> {
            int adapterPosition = holder.getAdapterPosition();

            if (adapterPosition == RecyclerView.NO_POSITION)
                return;

            if (optionsRecycler.getVisibility() == View.VISIBLE) {
                optionsRecycler.setVisibility(View.GONE);
                return;
            }

            String gradeLevel = className.get(adapterPosition);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, BASE_URL,
                response -> {
                    List<SchoolSubject> subjects = new ArrayList<>();
                    try {
                        JSONArray array = new JSONArray(response);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            int id = object.getInt("subject_id");
                            String name = object.getString("name");
                            int grade_level = object.getInt("grade_level");
                            Teacher teacher = null;
                            SchoolSubject subject = new SchoolSubject(id, name, grade_level, teacher);
                            subjects.add(subject);
                        }

                        optionsRecycler.setLayoutManager(new LinearLayoutManager(context));
                        OptionsAdapter_recyclerview adapter = new OptionsAdapter_recyclerview(subjects, context, nav, className.get(position));
                        optionsRecycler.setAdapter(adapter);
                        optionsRecycler.setVisibility(View.VISIBLE);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    error.printStackTrace();
                }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("grade_level", gradeLevel);
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(stringRequest);
        });
    }

    @Override
    public int getItemCount() {
        return className.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;

        public ViewHolder(CardView cardView) {
            super(cardView);
            this.cardView = cardView;
        }
    }
}
