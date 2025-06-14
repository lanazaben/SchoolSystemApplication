package com.example.schoolsystemapplication;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.schoolsystemapplication.Data.Assignment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class allAssignmentPage extends AppCompatActivity {
    Assignment ass=new Assignment();
    private RecyclerView recyclerView;
    private List<Assignment> assignmentList;
    private AssignmentAdapter adapter;
    private RequestQueue requestQueue;
   // private String URL = "http://10.0.2.2/phpFiles/get_assignmentss.php?grade_level="+ ass.getGradeLevel();
    private String URL = "http://10.0.2.2/phpFiles/get_assignmentss.php?grade_level=7";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_assignment_page);

        recyclerView = findViewById(R.id.Recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        assignmentList = new ArrayList<>();

        adapter = new AssignmentAdapter(assignmentList, allAssignmentPage.this);
        recyclerView.setAdapter(adapter);

        requestQueue = Volley.newRequestQueue(this);
        loadAssignments();
    }


    private void loadAssignments() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                URL,
                null,
                response -> {
                    assignmentList.clear();
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject assignment = response.getJSONObject(i);
                            String title = assignment.getString("title");
                            String subject = assignment.getString("subject_name");

                            Log.d("API_RESPONSE", "Title: " + title + ", Subject: " + subject);

                            assignmentList.add(new Assignment(title, subject));
                        }
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(allAssignmentPage.this, "Error parsing data", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Toast.makeText(allAssignmentPage.this, "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                });

        requestQueue.add(jsonArrayRequest);
    }
}