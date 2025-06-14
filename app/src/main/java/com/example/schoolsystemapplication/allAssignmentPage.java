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

    RecyclerView recyclerView;
    List<Assignment> assignmentList;
    AssignmentAdapter adapter;
    RequestQueue requestQueue;

    private String URL = "http://10.0.2.2/phpFiles/get_assignmentss.php?student_id=1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_assignment_page);

        recyclerView = findViewById(R.id.recyclerAssignments);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        assignmentList = new ArrayList<>();
        adapter = new AssignmentAdapter(assignmentList);
        recyclerView.setAdapter(adapter);

        requestQueue = Volley.newRequestQueue(this);

        loadAssignments();
    }

    private void loadAssignments() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        assignmentList.clear();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject assignment = response.getJSONObject(i);
                                String title = assignment.getString("title");
                                String subject = assignment.getString("subject_name");
                                String dueDate = assignment.getString("due_date");

                                assignmentList.add(new Assignment(title, subject, dueDate));
                            }
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(allAssignmentPage.this, "Parsing error", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(allAssignmentPage.this, "Volley Error: " + error.toString(), Toast.LENGTH_LONG).show();
                    }

                });

        requestQueue.add(jsonArrayRequest);
    }
}
