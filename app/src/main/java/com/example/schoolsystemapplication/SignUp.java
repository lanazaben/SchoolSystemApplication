package com.example.school;

import android.os.Bundle;
import android.text.InputType;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SignUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageView imgShowPassword = findViewById(R.id.imgShowPassword);
        ImageView imgShowConfirmPassword = findViewById(R.id.imgShowConfirmPassword);
        EditText edtPassword = findViewById(R.id.edtPassword);
        EditText edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        Spinner spinnerGrade = findViewById(R.id.spinnerGrade);

        // تغيير البيانات في Spinner إلى Teacher, Student, Registrar
        String[] roles = {"Teacher", "Student", "Registrar"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, roles);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGrade.setAdapter(adapter);

        imgShowPassword.setOnClickListener(v -> {
            if (edtPassword.getInputType() == InputType.TYPE_TEXT_VARIATION_PASSWORD) {
                edtPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                imgShowPassword.setImageResource(R.drawable.ic_visibility);  // Change icon to "show"
            } else {
                edtPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                imgShowPassword.setImageResource(R.drawable.ic_visibility_off);  // Change icon to "hide"
            }
        });

        imgShowConfirmPassword.setOnClickListener(v -> {
            if (edtConfirmPassword.getInputType() == InputType.TYPE_TEXT_VARIATION_PASSWORD) {
                edtConfirmPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                imgShowConfirmPassword.setImageResource(R.drawable.ic_visibility);  // Change icon to "show"
            } else {
                edtConfirmPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                imgShowConfirmPassword.setImageResource(R.drawable.ic_visibility_off);  // Change icon to "hide"
            }
        });
    }
}
