package com.example.tfood.project531.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tfood.R;
import com.example.tfood.project531.Common.Common;
import com.example.tfood.project531.Model.User;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    LinearLayout formLogin;
    EditText edtPhoneLogin;
    TextInputLayout edtPassLogin;
    Button btnLogin;
    TextView tvSignup, forgotPassTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        formLogin = findViewById(R.id.formlogin);
        tvSignup = findViewById(R.id.tVsignup);
        edtPhoneLogin = findViewById(R.id.edtPhoneLogin);
        edtPassLogin = findViewById(R.id.edtPasswordLogin);
        btnLogin = findViewById(R.id.btnlogin);
        forgotPassTxt = findViewById(R.id.forgotPassTxt);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final  String userPhone = edtPhoneLogin.getText().toString().trim();
                final String userPass = edtPassLogin.getEditText().toString().trim();
                final ProgressDialog mDialog = new ProgressDialog(LoginActivity.this);
                mDialog.setMessage("Please waiting...");
                mDialog.show();

                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (TextUtils.isEmpty(userPhone)){
                            mDialog.dismiss();
                            edtPhoneLogin.setError("Field cannot be empty");
                            return; // Thoát khỏi phương thức onClick để ngăn tiếp tục thực hiện xử lý
                        }

                        if (TextUtils.isEmpty(userPass)){
                            mDialog.dismiss();
                            edtPassLogin.setError("Field cannot be empty");
                            return; // Thoát khỏi phương thức onClick để ngăn tiếp tục thực hiện xử lý

                        }


                        if(snapshot.child(userPhone).exists()) {
                            mDialog.dismiss();
                            User user = snapshot.child(edtPhoneLogin.getText().toString()).getValue(User.class);
                            user.setPhone(edtPhoneLogin.getText().toString());

                            if (user.getPassword().equals(edtPassLogin.getEditText().getText().toString().trim()))
                            {

                                Intent homeIntent = new Intent(LoginActivity.this, HomeActivity.class);
                                Common.currentUser = user;
                                startActivity(homeIntent);
                                finish();
                            }
                            else
                            {
//                                Đối với thông báo lỗi đăng nhập không thành công
                                edtPassLogin.setError("Wrong Password");

                            }
                        }
                        else
                        {
                            mDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "User not exist", Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {

                    }
                });
            }
        });

        tvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });

        forgotPassTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SendOTPActivity.class));
            }
        });

    }

}