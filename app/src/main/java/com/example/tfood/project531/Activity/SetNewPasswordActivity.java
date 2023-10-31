package com.example.tfood.project531.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.tfood.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SetNewPasswordActivity extends AppCompatActivity {
    TextInputLayout new_password, confirm_password;
    Button set_new_password_btn;
    ImageView backImage;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_new_password);

        backImage = findViewById(R.id.backImage);
        new_password = findViewById(R.id.new_password);
        confirm_password = findViewById(R.id.confirm_password);
        set_new_password_btn = findViewById(R.id.set_new_password_btn);
        progressBar = findViewById(R.id.progressBar);

        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });



    }

    public  void setNewPasswordBtn(View view) {
        if (TextUtils.isEmpty(new_password.getEditText().getText().toString().trim()) || TextUtils.isEmpty(confirm_password.getEditText().getText().toString().trim()) ){
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        set_new_password_btn.setVisibility(View.INVISIBLE);

        String _newPass = new_password.getEditText().getText().toString().trim();
        String _phoneNumber = getIntent().getStringExtra("mobile");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User").child(_phoneNumber);
        reference.child("password").setValue(_newPass);
        // Hiển thị thông báo thành công bằng Toast
        Toast.makeText(getApplicationContext(), "Update password successfully", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();


    }
}