package com.example.tfood.project531.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.tfood.R;
import com.example.tfood.project531.Common.Common;
import com.example.tfood.project531.Interface.OnDataChangeListener;
import com.example.tfood.project531.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nex3z.notificationbadge.NotificationBadge;

public class ProfileActivity extends AppCompatActivity implements OnDataChangeListener {
    TextView profilePass, profilePhone, profileMyAddress;
    TextView titleName, titlePhone;
    Button editProfile;
    NotificationBadge badge;
    LinearLayout settingProfile, invoiceProfile, homeBtn, cartBtn, logOutBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profilePass = findViewById(R.id.profilePass);
        profilePhone = findViewById(R.id.profilePhone);
        titleName = findViewById(R.id.titleName);
        titlePhone = findViewById(R.id.titlePhone);
        settingProfile = findViewById(R.id.settingProfile);
        invoiceProfile = findViewById(R.id.invoiceProfile);
        profileMyAddress = findViewById(R.id.profileAddress);
        homeBtn = findViewById(R.id.homeBtn);
        cartBtn = findViewById(R.id.cartBtn);
        logOutBtn = findViewById(R.id.logOutBtn);
        badge = findViewById(R.id.badge);


        printDataUser();
        updateCartBadge();
        settingProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditProfileActivity editProfileActivity = new EditProfileActivity();
                editProfileActivity.setOnDataChangeListener(ProfileActivity.this);
                passUserData();
            }
        });

        cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cartIntent = new Intent(ProfileActivity.this, CartActivity.class);
                startActivity(cartIntent);
            }
        });

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent homeIntent = new Intent(ProfileActivity.this, HomeActivity.class);
                startActivity(homeIntent);
            }
        });

        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Đăng xuất người dùng (nếu cần)
                // Ví dụ: Xóa thông tin người dùng đã lưu trong SharedPreferences, cài đặt trạng thái đăng nhập về false, vv.
                // Tạo Intent để chuyển đến LoginActivity
                Intent loginIntent = new Intent(ProfileActivity.this, LoginActivity.class);
                // Xóa tất cả các Activity trướng LoginActivity khỏi ngăn xếp (back stack)
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                // Mở LoginActivity và đồng thời đóng ProfileActivity
                startActivity(loginIntent);

                // Đóng ProfileActivity
                finish();
            }
        });

        invoiceProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, OrderDetailActivity.class));
            }
        });




    }

    private void updateCartBadge() {
        DatabaseReference carRef = FirebaseDatabase.getInstance().getReference("Cart").child(Common.currentUser.getPhone());
        carRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int itemCount = (int) dataSnapshot.getChildrenCount();
                badge.setNumber(itemCount);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public  void onDataChanged(){
        String phoneUser = titlePhone.getText().toString().trim();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User").child(phoneUser);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String nameFromDB = snapshot.child("name").getValue(String.class);
                    String passFromDB = snapshot.child("password").getValue(String.class);
                    titleName.setText(nameFromDB);
                    profilePass.setText(passFromDB);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    public  void showUserData() {
        Intent intent = getIntent();
        if (intent != null) {
            String nameUser = intent.getStringExtra("name");
            String passUser = intent.getStringExtra("password");
            String phoneUser = intent.getStringExtra("phone");

            // Kiểm tra null trước khi gán giá trị vào TextView
            if (titleName != null) {
                titleName.setText(nameUser);
            }
            if (profilePass != null) {
                profilePass.setText(passUser);
            }
            if (profilePhone != null) {
                profilePhone.setText(phoneUser);
            }
            if (titlePhone != null) {
                titlePhone.setText(phoneUser);
            }
        }
    }

    public void printDataUser() {
        // Truy cập thông tin người dùng từ Common.currentUser
        User currentUser = Common.currentUser;
        // Sử dụng thông tin người dùng
        String username = currentUser.getName();
        String password = currentUser.getPassword();
        String phone = currentUser.getPhone();
        String address = currentUser.getAddress();

        titleName.setText(username);
        profilePass.setText(password);
        profilePhone.setText(phone);
        titlePhone.setText(phone);
        profileMyAddress.setText(address);
    }

    public void passUserData(){
        String nameUser = titleName.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User");
        Query checkUserDatabase = reference.orderByChild("name").equalTo(nameUser);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String phoneUser = titlePhone.getText().toString().trim();
                    DataSnapshot userSnapshot = snapshot.getChildren().iterator().next();
                    String nameFromDB = userSnapshot.child("name").getValue(String.class);
                    String passFromDB = userSnapshot.child("password").getValue(String.class);

                    Intent intent1 = new Intent(ProfileActivity.this, EditProfileActivity.class);

                    intent1.putExtra("name", nameFromDB);
                    intent1.putExtra("pass",passFromDB);
                    intent1.putExtra("phone",phoneUser);

                    startActivity(intent1);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}