package com.example.tfood.project531.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tfood.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignupActivity extends AppCompatActivity {
    private EditText edtNameSignup,edtPhoneSignup,edtPassSignup,edtRePassSignup;
    Button btnSignup;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://foodapp-3ba8e-default-rtdb.firebaseio.com/");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        LinearLayout formSignup = findViewById(R.id.formsignup);
        TextView tvLogin;
        tvLogin = findViewById(R.id.tVlogin);
        edtNameSignup = findViewById(R.id.edtNameSignup);
        edtPhoneSignup = findViewById(R.id.edtPhoneSignup);
        edtPassSignup = findViewById(R.id.edtPasswordSignup);
        edtRePassSignup = findViewById(R.id.edtRePassword);
        btnSignup = findViewById(R.id.btnSignup);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String nameTxt = edtNameSignup.getText().toString();
                final String phoneTxt = edtPhoneSignup.getText().toString();
                final String passTxt = edtPassSignup.getText().toString();
                final String repassTxt = edtRePassSignup.getText().toString();
                final String phoneNumberPattern = "^[0-9]+$";
//                kiểm tra user đã điền đầy đủ thông tin
                if(nameTxt.isEmpty() || phoneTxt.isEmpty() || passTxt.isEmpty() || repassTxt.isEmpty()) {
                    Toast.makeText(SignupActivity.this, "Please fill all information", Toast.LENGTH_SHORT).show();
                } else if (!phoneTxt.matches(phoneNumberPattern) ) {
                    Toast.makeText(SignupActivity.this,"Invalid phone number", Toast.LENGTH_SHORT).show();
                } else if (passTxt.length() < 8) {
                    Toast.makeText(SignupActivity.this,"Password must be longer than 8 characters", Toast.LENGTH_SHORT).show();
                    
                } else if (!passTxt.equals(repassTxt)) {
                    Toast.makeText(SignupActivity.this, "Password are not matching", Toast.LENGTH_SHORT).show();

                }
                else {
                    databaseReference.child("User").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            Kiểm tra đã đăng ký số điện thoai này chưa
                            if (snapshot.hasChild(phoneTxt)){
                                Toast.makeText(SignupActivity.this,"Phone is already registered", Toast.LENGTH_SHORT).show();
                            }else {
                                databaseReference.child("User").child(phoneTxt).child("name").setValue(nameTxt);
                                databaseReference.child("User").child(phoneTxt).child("password").setValue(passTxt);

                                Toast.makeText(SignupActivity.this, "User Registered successfully", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }

            }
        });


        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            }
        });
    }
}