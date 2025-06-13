package com.example.schoolsystemapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import android.app.DatePickerDialog;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;

public class AddStudent extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private Toolbar toolbar;
    TextView textViewDOB;
    EditText birthCertEditText , editTextFullName, editTextID, editTextEmail, editTextPhone, editTextGrade, gender;
    TextView filesTextView;
    Button buttonAdd;

    private static  final String BASE_URL = "http://10.0.2.2:80/php_project/student_sign_up.php";
    private Uri selectedFileUri = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

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

            // Close the drawer after an item is selected
            drawerLayout.closeDrawers();
            return true;
        });


        String gradeFromIntent = getIntent().getStringExtra("grade_level");
        if (gradeFromIntent != null) {
            editTextGrade.setText(gradeFromIntent);
            editTextGrade.setFocusable(false);
            editTextGrade.setClickable(false);
        }

        birthCertEditText.setFocusable(false); // المستخدم ما يكتب فيه
        birthCertEditText.setClickable(true);  // بس يضغط عليه

        birthCertEditText.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*"); // أو "application/pdf" لو بس شهادات PDF
            startActivityForResult(intent, 1); // رقم 1 كود الطلب
        });

        textViewDOB.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(AddStudent.this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        String dobString = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay;
                        textViewDOB.setText(dobString);
                    }, year, month, day);

            datePickerDialog.show();
        });

        buttonAdd.setOnClickListener(v -> {
            if (selectedFileUri == null) {
                Toast.makeText(this, "Please select a birth certificate file first", Toast.LENGTH_SHORT).show();
                return;
            }

            String name = editTextFullName.getText().toString().trim();
            String email = editTextEmail.getText().toString().trim();
            String gradeLevel = editTextGrade.getText().toString().trim();
            String dobString = textViewDOB.getText().toString().trim();
            String parentPhone = editTextPhone.getText().toString().trim();
            String id = editTextID.getText().toString().trim();
            String genderV = gender.getText().toString().trim();

            if (name == null || email == null || gradeLevel == null || dobString == null || parentPhone == null || id == null || genderV == null) {
                Toast.makeText(this, "Please fill all information", Toast.LENGTH_SHORT).show();
                return;
            }

            sendData(selectedFileUri, name, email, gradeLevel, dobString, parentPhone, id, genderV);
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

        editTextFullName = findViewById(R.id.editTextFullName);
        editTextID = findViewById(R.id.editTextID);
        editTextEmail = findViewById(R.id.editTextEmail);
        textViewDOB = findViewById(R.id.textViewDOB);
        gender = findViewById(R.id.gender);
        editTextGrade = findViewById(R.id.editTextGrade);
        editTextPhone = findViewById(R.id.editTextPhone);
        birthCertEditText = findViewById(R.id.BirthCert);
        filesTextView = findViewById(R.id.filesID);


        buttonAdd = findViewById(R.id.buttonAdd);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            selectedFileUri = data.getData();
            String fileName = selectedFileUri.getLastPathSegment();
            filesTextView.setText(fileName);
        }
    }

    private void sendData(Uri fileUri, String name, String email, String gradeLevel, String dob,
                          String phone, String id, String gender) {

        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, BASE_URL,
                response -> {
                    Toast.makeText(this, "Successfully added", Toast.LENGTH_LONG).show();

                    showAccountDialog(id);
                },
                error -> {
                    Toast.makeText(this, "Upload Error: " + error.toString(), Toast.LENGTH_LONG).show();
                });


        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("id_number", id);
        params.put("email", email);
        params.put("date_of_birth", dob);
        params.put("gender", gender);
        params.put("grade_level", gradeLevel);
        params.put("parent_phone", phone);

        multipartRequest.setParams(params);

        // Read file content from Uri and convert to byte[]
        try {
            InputStream inputStream = getContentResolver().openInputStream(fileUri);
            ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];

            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                byteBuffer.write(buffer, 0, len);
            }

            byte[] fileData = byteBuffer.toByteArray();
            multipartRequest.addFile("birth_certificate", new VolleyMultipartRequest.DataPart("file.pdf", fileData));
        } catch (IOException e) {
            Toast.makeText(this, "File Read Error", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(multipartRequest);
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
