package com.example.schoolsystemapplication;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import android.widget.Toast;


public class AddTeacher extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private Toolbar toolbar;
    TextView textViewDOB;
    EditText  editTextFullName, editTextID, editTextEmail, editTextPhone,gender;
    Button buttonAdd;

    private static  final String BASE_URL = "http://10.0.2.2:80/php_project/teacher_sign_up.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_teacher);

        initialize();

        setSupportActionBar(toolbar);
        // ActionBarDrawerToggle allows us to link the Toolbar with the Drawer
        toggle = new ActionBarDrawerToggle(this, drawerLayout,
                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        // Add the toggle to the drawer
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Setup NavigationView and its item listener
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            Intent intent = null;
            if (id == R.id.nav_teacher)
                intent = new Intent(this, teacher_list.class);
            else if (id == R.id.nav_GradeLevel){
                intent=new Intent (this,ClassList_Activity.class);
            } else if (id == R.id.nav_subject)
                intent = new Intent(this, AddSubject.class);
            else if (id == R.id.nav_logout)
                intent = new Intent(this, LogIn.class);
            else if (id == R.id.nav_addTeacher)
                intent = new Intent(this, AddTeacher.class);

            startActivity(intent);

            drawerLayout.closeDrawers();
            return true;
        });

        textViewDOB.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(AddTeacher.this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        String dobString = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay;
                        textViewDOB.setText(dobString);
                    }, year, month, day);

            datePickerDialog.show();
        });

        buttonAdd.setOnClickListener(v -> {
            String name = editTextFullName.getText().toString().trim();
            String email = editTextEmail.getText().toString().trim();
            String dobString = textViewDOB.getText().toString().trim();
            String id = editTextID.getText().toString().trim();
            String phone = editTextPhone.getText().toString().trim();
            String genderV = gender.getText().toString().trim();


            sendData(name, email, dobString, id, phone, genderV);
        });



    }

    @Override
    public void onBackPressed() {
        // If the drawer is open, close it; otherwise, exit the activity
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    public void initialize(){
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        textViewDOB = findViewById(R.id.textViewDOB);
        editTextFullName = findViewById(R.id.editTextFullName);
        editTextID = findViewById(R.id.editTextID);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPhone = findViewById(R.id.editTextPhone);
        gender = findViewById(R.id.gender);
        buttonAdd = findViewById(R.id.buttonAdd);
    }

    private void sendData(String name, String email, String dob, String id, String phone, String gender) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, BASE_URL,
                response -> {
                    Toast.makeText(this, "Successfully added", Toast.LENGTH_LONG).show();

                    showAccountDialog(id);                },
                error -> {
                    Toast.makeText(this, "Error" + error.toString(), Toast.LENGTH_LONG).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("id_number", id);
                params.put("email", email);
                params.put("phone_number", phone);
                params.put("date_of_birth", dob);
                params.put("gender", gender);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void showAccountDialog(String idNumber) {
        android.app.Dialog dialog = new android.app.Dialog(this);
        dialog.setContentView(R.layout.dialog_account_created);

        TextView usernameText = dialog.findViewById(R.id.dialog_username);
        TextView passwordText = dialog.findViewById(R.id.dialog_password);
        Button okButton = dialog.findViewById(R.id.dialog_ok_button);

        usernameText.setText("User name: " + idNumber);
        passwordText.setText("Password: " + idNumber);

        okButton.setOnClickListener(btn -> dialog.dismiss());
        dialog.show();
    }

}