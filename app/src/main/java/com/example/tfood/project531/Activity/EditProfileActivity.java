package com.example.tfood.project531.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tfood.R;
import com.example.tfood.project531.Interface.OnDataChangeListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class EditProfileActivity extends AppCompatActivity {
    private OnDataChangeListener onDataChangeListener;
    EditText editName, editPass;
    Button editSaveBtn;
    String phoneUser, nameUser, passUser;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        editName = findViewById(R.id.edit_name);
        editPass = findViewById(R.id.edit_pass);
        editSaveBtn = findViewById(R.id.btnSave);
        databaseReference = FirebaseDatabase.getInstance().getReference("User");

        showData();

        editSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isNameChange() || isPassChange()){
                    Toast.makeText(EditProfileActivity.this, "Saved",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(EditProfileActivity.this, "No Changes Found",Toast.LENGTH_SHORT).show();

                }
            }
        });


    }
    public void setOnDataChangeListener(OnDataChangeListener listener) {

        this.onDataChangeListener = listener;

    }
    public boolean isNameChange() {
        if (nameUser != null && !nameUser.equals(editName.getText().toString())) {
            databaseReference.child(phoneUser).child("name").setValue(editName.getText().toString());
            nameUser = editName.getText().toString();
//            reloadUserData();
//******************
            if (onDataChangeListener != null) {

                onDataChangeListener.onDataChanged();

            }
//            *************************
            return true;
        } else {
            return false;
        }
    }

    public boolean isPassChange() {
        if (passUser != null && !passUser.equals(editPass.getText().toString())) {
            final ProgressDialog progressDialog = new ProgressDialog(EditProfileActivity.this);
            progressDialog.setMessage("Loading..."); // Đặt thông báo cho ProgressDialog
            progressDialog.show(); // Hiển thị ProgressDialog

            databaseReference.child(phoneUser).child("password").setValue(editPass.getText().toString());
            passUser = editPass.getText().toString();
//            reloadUserData();
//            startActivity(new Intent(EditProfileActivity.this, LoginActivity.class));
//******************

            // Delay 2 giây trước khi chuyển sang màn hình đăng nhập
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Chuyển tới màn hình đăng nhập
                    startActivity(new Intent(EditProfileActivity.this, LoginActivity.class));
                }
            }, 2000); // 2000 milliseconds = 2 giây

            if (onDataChangeListener != null) {

                onDataChangeListener.onDataChanged();
            }
//            *************************
            return true;
        } else {
            return false;
        }
    }
    public  void showData() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras != null) {
             nameUser = intent.getStringExtra("name");
             passUser = intent.getStringExtra("pass");
             phoneUser = intent.getStringExtra("phone");


            // Hiển thị thông tin người dùng trong giao diện
            editName.setText(nameUser);
            editPass.setText(passUser);

        }
    }


    private void reloadUserData() {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("User").child(phoneUser);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String nameFromDB = snapshot.child("name").getValue(String.class);
                    String passFromDB = snapshot.child("password").getValue(String.class);

                    // Cập nhật giao diện với dữ liệu từ cơ sở dữ liệu
                    editName.setText(nameFromDB);
                    editPass.setText(passFromDB);

                    // Gửi broadcast thông báo rằng dữ liệu đã được thay đổi
                    Intent dataChangedIntent = new Intent("DATA_CHANGED");
                    sendBroadcast(dataChangedIntent);


//                    Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
//                    intent.putExtra("username", nameFromDB);
//                    intent.putExtra("passEdit",passFromDB);
//                    intent.putExtra("phoneEdit",phoneUser);
//                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi khi truy cập cơ sở dữ liệu thất bại (nếu cần)
            }
        });
    }
}