package com.example.tfood.project531.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.tfood.R;
import com.example.tfood.project531.Common.Common;
import com.example.tfood.project531.Model.Cart;
import com.example.tfood.project531.Model.Food;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ShowDetailActivity extends AppCompatActivity {
    TextView foodNameDetail, foodDes, minus, add, quantity, feeFood, star;
    ImageView foodPic, backDetailFoodBtn;
    ConstraintLayout addToCartBtn;
    DatabaseReference food = FirebaseDatabase.getInstance().getReference("Food");
    String foodId ="";
    int quantityCurrent = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_detail);

        foodNameDetail = findViewById(R.id.foodNameDetail);
        foodDes = findViewById(R.id.foodDes);
        foodPic = findViewById(R.id.foodPic);
        feeFood = findViewById(R.id.feeFood);
        minus = findViewById(R.id.minus);
        add = findViewById(R.id.addFood);
        quantity = findViewById(R.id.quantity);
        star = findViewById(R.id.star);
        addToCartBtn = findViewById(R.id.addToCartBtn);
        backDetailFoodBtn = findViewById(R.id.backDetailFoodBtn);



        // Lấy ID của sản phẩm từ Intent
        foodId = getIntent().getStringExtra("FoodId");
        if (foodId != null && !foodId.isEmpty()) {
            // Bây giờ bạn có thể sử dụng foodId để lấy thông tin chi tiết của sản phẩm
            getDetailFood(foodId);
        } else {
            // Xử lý trường hợp foodId là null hoặc rỗng
            Log.e("ShowDetailActivity", "foodId is null or empty");
        }

//      add food ---------------------------------
        quantity.setText(String.valueOf(quantityCurrent));
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quantityCurrent++;
                quantity.setText(String.valueOf(quantityCurrent));
            }
        });
//        minus food------------------------------
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(quantityCurrent <= 1){
                    quantityCurrent = 1;
                }else {
                    quantityCurrent--;
                }
                quantity.setText(String.valueOf(quantityCurrent));
            }
        });

//        ADD TO CART
        addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("Cart").child(Common.currentUser.getPhone());
                cartRef.child(foodId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            // Sản phẩm đã tồn tại trong giỏ hàng
                            Cart cartItem = snapshot.getValue(Cart.class);
                            if (cartItem != null) {
                                // Tăng số lượng sản phẩm trong giỏ hàng
                                cartItem.setQuantity(cartItem.getQuantity() + quantityCurrent);
                                // Cập nhật tổng giá
                                cartItem.setTotalPrice(cartItem.getFee() * cartItem.getQuantity());
                                cartRef.child(String.valueOf(foodId)).setValue(cartItem);
                            }
                        } else {
                            // Lấy URL hình ảnh của sản phẩm từ foodData
                            String imageURL = foodPic.getTag().toString();

                            Double quantityCurrentDouble = (double) quantityCurrent;
                            String feeText = feeFood.getText().toString();
                            feeText = feeText.replace("$ ", ""); // Loại bỏ ký tự "$"
                            Double fee = Double.parseDouble(feeText);
                            Double totalPrice = fee * quantityCurrentDouble;
                            // Sản phẩm chưa tồn tại trong giỏ hàng
                            Cart cartItem = new Cart(Integer.parseInt(foodId), foodNameDetail.getText().toString(), imageURL, fee, quantityCurrent, totalPrice);
                            cartRef.child(foodId).setValue(cartItem);
                        }
                        // Hiển thị thông báo "Added to cart"
                        Toast.makeText(view.getContext(), "Added to cart", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Xử lý lỗi nếu cần
                    }
                });
            }
        });

//        Back Activity
        backDetailFoodBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Sử dụng phương thức finish để kết thúc Activity hiện tại và quay trở lại trang trước đó
                finish();
            }
        });

//

    }




    private void getDetailFood(String foodId) {
        DatabaseReference foodRef = food.child(foodId); // Đường dẫn Firebase tới thực phẩm cụ thể
        foodRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Food foodData = dataSnapshot.getValue(Food.class);
                    if (foodData != null) {
                        // Hiển thị thông tin chi tiết của thực phẩm lên giao diện
                        foodNameDetail.setText(foodData.getFoodName());
                        foodDes.setText(foodData.getDescription());
                        feeFood.setText("$ " + foodData.getFee());
                        star.setText(String.valueOf(foodData.getStar()));
                        // Sử dụng thư viện Glide để hiển thị hình ảnh thực phẩm
                        Glide.with(ShowDetailActivity.this)
                                .load(foodData.getFoodPic())
                                .into(foodPic);

                        // Đặt tag cho foodPic để lấy URL hình ảnh sau này
                        foodPic.setTag(foodData.getFoodPic());

                    }
                }
            }

            @Override
            public void onCancelled( DatabaseError databaseError) {
                // Xử lý lỗi nếu có
                Log.e("ShowDetailActivity", "Lỗi khi truy vấn dữ liệu từ Firebase: " + databaseError.getMessage());
            }
        });
    }
}