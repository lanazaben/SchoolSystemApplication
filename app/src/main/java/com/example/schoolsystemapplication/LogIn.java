package com.example.schoolsystemapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LogIn extends AppCompatActivity {
    public static final String NAME = "NAME";
    public static final String FLAG = "FLAG"; // Removed PASS constant
    private boolean flag = false;
    private EditText edtName;
    private EditText edtPassword;
    private CheckBox chk;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private Button btnLogIn;
    private static final String BASE_URL = "http://10.0.2.2:80/php_project/login.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        setupViews();

        btnLogIn = findViewById(R.id.btnLogin);

        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnLoginOnClick(v);
            }
        });
        setupSharedPrefs();
        checkPrefs();
    }

    private void checkPrefs() {
        flag = prefs.getBoolean(FLAG, false);

        if (flag) {
            String name = prefs.getString(NAME, "");
            edtName.setText(name);
            chk.setChecked(true);
        }
    }

    private void setupSharedPrefs() {
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        editor = prefs.edit();
    }

    private void setupViews() {
        edtName = findViewById(R.id.edtName);
        edtPassword = findViewById(R.id.edtPassword);
        chk = findViewById(R.id.chk);
    }

    public void btnLoginOnClick(View view) {
        String name = edtName.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (name.isEmpty()) {
            edtName.setError("Username cannot be empty");
            edtName.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            edtPassword.setError("Password cannot be empty");
            edtPassword.requestFocus();
            return;
        }

        if (chk.isChecked()) {
            editor.putString(NAME, name);
            editor.putBoolean(FLAG, true);
            editor.apply();
        } else {
            editor.clear();
            editor.apply();
        }

        sendData(name, password);
    }

    private void sendData(String user_name, String password) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, BASE_URL,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");
                        String message = jsonResponse.getString("message");

                        if (success) {
                            Toast.makeText(this, "Login successful: " + message, Toast.LENGTH_LONG).show();
                            String userRole = jsonResponse.optString("user_role", "");
                            String id_number = jsonResponse.optString("id_number", "");

                            Intent intent;
                            switch (userRole) {
                                case "teacher":
                                    String url = "http://10.0.2.2:80/php_project/get_Teacher_id.php";
                                    StringRequest stringRequest1 = new StringRequest(Request.Method.POST, url,
                                            response1 -> {
                                                try {
                                                    JSONObject object = new JSONObject(response1);

                                                    SharedPreferences sp = getSharedPreferences("teacher_session", MODE_PRIVATE);
                                                    SharedPreferences.Editor editor = sp.edit();
                                                    editor.putString("teacher_id", object.getInt("teacher_id")+"");
                                                    editor.apply();
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
                                            params.put("id_number", id_number);
                                            return params;
                                        }
                                    };
                                    RequestQueue requestQueue = Volley.newRequestQueue(LogIn.this);
                                    requestQueue.add(stringRequest1);
                                    intent = new Intent(this, TeacherHome.class);
                                    break;
                                case "student":
                                    String url2 = "http://10.0.2.2:80/php_project/get_Student_id.php";
                                    StringRequest stringRequest2 = new StringRequest(Request.Method.POST, url2,
                                            response2 -> {
                                                try {
                                                    JSONObject object = new JSONObject(response2);

                                                    SharedPreferences sp = getSharedPreferences("student_session", MODE_PRIVATE);
                                                    SharedPreferences.Editor editor = sp.edit();
                                                    editor.putString("student_id", object.getInt("student_id")+"");
                                                    editor.apply();
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
                                            params.put("id_number", id_number);
                                            return params;
                                        }
                                    };
                                    RequestQueue requestQueue2 = Volley.newRequestQueue(LogIn.this);
                                    requestQueue2.add(stringRequest2);
                                    intent = new Intent(this, StudentHome.class);
                                    break;
                                case "admin":
                                    intent = new Intent(this, RegistrarsHome.class);
                                    break;
                                default:
                                    intent = new Intent(this, LogIn.class);
                                    break;
                            }
                            startActivity(intent);
                            finish(); // Close login activity

                        } else {
                            Toast.makeText(this, "Login failed: " + message, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(this, "Error parsing response: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                },
                error -> {
                    Toast.makeText(this, "Network Error: " + error.toString(), Toast.LENGTH_LONG).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_name", user_name);
                params.put("password", password);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}


