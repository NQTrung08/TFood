package com.example.tfood.project531.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.tfood.R;
import com.example.tfood.project531.Adapter.CartAdapter;
import com.example.tfood.project531.Common.Common;
import com.example.tfood.project531.Model.Cart;
import com.example.tfood.project531.Model.Food;
import com.example.tfood.project531.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {
    private double total = 0;
    RecyclerView recyclerViewCart;
    RecyclerView.Adapter adapterCart;
    TextView totalPrice;
    ConstraintLayout confirmBtn;
    LinearLayout homeBtn, profileBtn;

    // Định nghĩa một hằng số để xác định mã yêu cầu
    private static final int PLACE_ORDER_REQUEST_CODE = 1;
    ArrayList<String> foodNames = new ArrayList<>();
    ArrayList<Integer> quantities = new ArrayList();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false);
        recyclerViewCart = findViewById(R.id.viewCart);
        recyclerViewCart.setLayoutManager(linearLayoutManager);
        totalPrice = findViewById(R.id.totalPrice);
        confirmBtn = findViewById(R.id.confirmBtn);
        homeBtn = findViewById(R.id.homeBtn);
        profileBtn = findViewById(R.id.profileBtn);

        ArrayList<Cart> cartList = new ArrayList<>();

        adapterCart = new CartAdapter(cartList);
        recyclerViewCart.setAdapter(adapterCart);

        DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("Cart").child(Common.currentUser.getPhone());
        cartRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                cartList.clear();

                total = 0;
                for (DataSnapshot cartSnapshot : dataSnapshot.getChildren()) {
                    Cart cartItem = cartSnapshot.getValue(Cart.class);
                    if (cartItem != null) {
                        cartList.add(cartItem);
                        total += cartItem.getTotalPrice();

                        // Lấy tên món ăn và tổng số sản phẩm
                        String foodName = cartItem.getFoodName();
                        int quantity = cartItem.getQuantity();

                        // Thêm vào danh sách
                        foodNames.add(foodName);
                        quantities.add(quantity);
                    }
                }

                adapterCart.notifyDataSetChanged();
                totalPrice.setText("$" + total);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý lỗi nếu có
            }
        });



        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent placeOrderIntent = new Intent(CartActivity.this, PlaceOrderActivity.class);
                placeOrderIntent.putExtra("foodNames", foodNames);
                placeOrderIntent.putExtra("quantities", quantities);
                placeOrderIntent.putExtra("subTotal", total);
                startActivityForResult(placeOrderIntent, PLACE_ORDER_REQUEST_CODE);
            }
        });

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent homeIntent = new Intent(CartActivity.this, HomeActivity.class);
                startActivity(homeIntent);
            }
        });

        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent profileIntent = new Intent(CartActivity.this, ProfileActivity.class);
                startActivity(profileIntent);
            }
        });





    }

    // Xử lý sự kiện khi bạn nhận được kết quả từ PlaceOrderActivity
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PLACE_ORDER_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Đặt hàng thành công, xóa dữ liệu cũ khỏi foodNames và quantities
                foodNames.clear();
                quantities.clear();

                // Kiểm tra xem có dữ liệu mới từ PlaceOrderActivity không
                if (data != null) {
                    ArrayList<String> updatedFoodNames = data.getStringArrayListExtra("foodNames");
                    ArrayList<Integer> updatedQuantities = data.getIntegerArrayListExtra("quantities");

                    // Cập nhật dữ liệu mới
                    if (updatedFoodNames != null && updatedQuantities != null) {
                        foodNames.addAll(updatedFoodNames);
                        quantities.addAll(updatedQuantities);
                    }
                }
            }
        }
    }



}