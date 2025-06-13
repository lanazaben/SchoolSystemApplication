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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
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

public class Adapter_recyclerview_className extends RecyclerView.Adapter<Adapter_recyclerview_className.ViewHolder> implements Filterable {

    private Context context;
    private List<String> className;
    private String nav;
    private static final String BASE_URL = "http://10.0.2.2:80/php_project/get_subjects_use_gradeLevel.php";
    private Adapter_teacherList.OnItemClickListener listener;
    private List<String> fullClassNameList; // for filtering

    public Adapter_recyclerview_className(List<String> className, Context context, String nav) {
        this.context = context;
        this.className = className;
        this.nav = nav;
        this.fullClassNameList = new ArrayList<>(className);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_item_layout, parent, false);
        return new ViewHolder(v);
    }
    public void setOnItemClickListener(Adapter_teacherList.OnItemClickListener listener) {
        this.listener = listener;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CardView cardView = holder.cardView;
        RecyclerView optionsRecycler = cardView.findViewById(R.id.optionsRecycler);
        holder.textView.setText("grade level: " + className.get(position));

        cardView.setOnClickListener(v -> {
            int adapterPosition = holder.getAdapterPosition();
            if (adapterPosition == RecyclerView.NO_POSITION) return;

            String gradeLevel = className.get(adapterPosition);

            // Registrar: Go directly to ViewClassSchedule
            if ("view_student".equals(nav)) {
                Intent intent = new Intent(context, ViewClassSchedule.class);
                intent.putExtra("grade_level", gradeLevel);
                //Toast.makeText(context,"class :"+ gradeLevel, Toast.LENGTH_SHORT);
                context.startActivity(intent);
                return;
            } else {
                // Teacher: Show subject list
                if (optionsRecycler.getVisibility() == View.VISIBLE) {
                    optionsRecycler.setVisibility(View.GONE);
                    return;
                }
                SharedPreferences sp = context.getSharedPreferences("teacher_session", context.MODE_PRIVATE);
                int id_teacher = Integer.parseInt(sp.getString("teacher_id", "0"));
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
                        params.put("teacher_id", String.valueOf(id_teacher));
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(context);
                requestQueue.add(stringRequest);
            }
        });
    }

    @Override
    public int getItemCount() {
        return className.size();
    }


    public void setClass(List<String> updatedClasses) {
        this.className.clear();
        this.className.addAll(updatedClasses);
        this.fullClassNameList.clear();
        this.fullClassNameList.addAll(updatedClasses);
        notifyDataSetChanged();
    }

    private final Filter classFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<String> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(fullClassNameList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (String className : fullClassNameList) {
                    if (className.toLowerCase().contains(filterPattern)) {
                        filteredList.add(className);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            className.clear();
            className.addAll((List<String>) results.values);
            notifyDataSetChanged();
        }
    };

    @Override
    public Filter getFilter() {
        return classFilter;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView textView;
        RecyclerView optionsRecycler;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            textView = itemView.findViewById(R.id.itemTitle);
            optionsRecycler = itemView.findViewById(R.id.optionsRecycler);
        }
    }

}
