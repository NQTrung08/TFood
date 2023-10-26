package com.example.tfood.project531.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.tfood.R;
import com.example.tfood.project531.Adapter.FoodListAdapter;
import com.example.tfood.project531.Adapter.PopularAdapter;
import com.example.tfood.project531.Model.Food;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListFoodActivity extends AppCompatActivity {
    EditText edtSearchList;
    ImageView backListFoodBtn;

    ImageView searchListFood;
    private RecyclerView.Adapter adapterListFood;
    private RecyclerView recyclerViewListFood;
    FirebaseDatabase database;
    DatabaseReference category;
    DatabaseReference foodRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_product);
        backListFoodBtn = findViewById(R.id.backListFoodBtn);
        searchListFood = findViewById(R.id.searchListFood);

        int selectedCategoryId = getIntent().getIntExtra("selectedCategoryId", 1);
        recyclerViewListFood(selectedCategoryId);

        backListFoodBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Sử dụng phương thức finish để kết thúc Activity hiện tại và quay trở lại trang trước đó
                finish();
            }
        });

        searchListFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ListFoodActivity.this, SearchActivity.class));
            }
        });
    }

    private void recyclerViewListFood(final int selectedCategoryId) {
        recyclerViewListFood = findViewById(R.id.viewListFood);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerViewListFood.setLayoutManager(gridLayoutManager);
        database = FirebaseDatabase.getInstance();
        foodRef = database.getReference("Food");

        ArrayList<Food> foodArrayList = new ArrayList<>();
        adapterListFood = new FoodListAdapter(foodArrayList);
        recyclerViewListFood.setAdapter(adapterListFood);
        // Lấy dữ liệu từ Firebase
        foodRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                foodArrayList.clear();
                for (DataSnapshot foodSnapshot : dataSnapshot.getChildren()) {
                        Long foodIdLong = foodSnapshot.child("foodId").getValue(Long.class);
                        int foodId = foodIdLong != null ? foodIdLong.intValue() : -1;
                        String foodName = foodSnapshot.child("foodName").getValue(String.class);
                        String foodPic = foodSnapshot.child("foodPic").getValue(String.class);
                        Double foodFee = foodSnapshot.child("fee").getValue(Double.class);
                        Long menuIdLong = foodSnapshot.child("menuId").getValue(Long.class);
                        int menuId = menuIdLong != null ? menuIdLong.intValue() : -1;

                        Food food = new Food(foodId,foodName, foodPic, foodFee, menuId);
                            if(food.getMenuId() == selectedCategoryId){
                                foodArrayList.add(food);

                            }
                }
                // cập nhập dữ liệu
                adapterListFood.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý lỗi nếu cần
            }
        });
    }

}