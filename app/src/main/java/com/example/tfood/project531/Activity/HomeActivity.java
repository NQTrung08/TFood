package com.example.tfood.project531.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.tfood.R;
import com.example.tfood.project531.Adapter.CategoryAdapter;
import com.example.tfood.project531.Adapter.PopularAdapter;
import com.example.tfood.project531.Adapter.SlideAdapter;
import com.example.tfood.project531.Common.Common;
import com.example.tfood.project531.Interface.BadgeProvider;
import com.example.tfood.project531.Model.Category;
import com.example.tfood.project531.Model.Food;
import com.example.tfood.project531.Model.SlideItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nex3z.notificationbadge.NotificationBadge;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private RecyclerView.Adapter adapter, adapter2;
    private RecyclerView recyclerViewCategoryList, recyclerViewPopularList;
    ViewPager2 viewPager2;
    EditText edtSearch;
    NotificationBadge badge;
    FirebaseDatabase database;
    DatabaseReference category;
    DatabaseReference foodRef;

    private Handler slideHandler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        edtSearch = findViewById(R.id.edtSearch);
        badge = findViewById(R.id.badge);
        LinearLayout profileBtn = findViewById(R.id.profileBtn);
        LinearLayout cartBtn = findViewById(R.id.cartBtn);
        viewPager2 = findViewById(R.id.viewPager);


        List<SlideItem> slideItemList = new ArrayList<>();
        slideItemList.add( new SlideItem(R.drawable.slider_banner));
        slideItemList.add( new SlideItem(R.drawable.slide2_banner));
        slideItemList.add( new SlideItem(R.drawable.slide3_banner));

        viewPager2.setAdapter(new SlideAdapter(slideItemList, viewPager2));
        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r*0.15f);

            }
        });
        viewPager2.setPageTransformer(compositePageTransformer);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                slideHandler.removeCallbacks(sliderRunnable);
                slideHandler.postDelayed(sliderRunnable,3000);
            }
        });


        cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cartIntent = new Intent(HomeActivity.this, CartActivity.class);
                startActivity(cartIntent);
            }
        });

        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent profileIntent = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivity(profileIntent);
            }
        });

        edtSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, SearchActivity.class));
            }
        });


        recyclerViewCategory();
        recyclerViewPopularList();
        updateCartBadge();
    }

    private void updateCartBadge() {
        DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("Cart").child(Common.currentUser.getPhone());
        cartRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int itemCount = (int) dataSnapshot.getChildrenCount();
                badge.setNumber(itemCount);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý lỗi nếu cần
            }
        });
    }

    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
        }
    };
    @Override
    protected void onPause(){
        super.onPause();
        slideHandler.removeCallbacks(sliderRunnable);
    }
    @Override
    protected void onResume(){
        super.onResume();
        slideHandler.postDelayed(sliderRunnable, 3000);
    }

    private void recyclerViewPopularList() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);
        recyclerViewPopularList = findViewById(R.id.view2);
        recyclerViewPopularList.setLayoutManager(linearLayoutManager);
        database = FirebaseDatabase.getInstance();
        foodRef = database.getReference("Food");


        ArrayList<Food> RecommendedArrayList = new ArrayList<>();
        adapter2 = new PopularAdapter(RecommendedArrayList);
        recyclerViewPopularList.setAdapter(adapter2);
        // Lấy dữ liệu từ Firebase
        foodRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                RecommendedArrayList.clear();
                int count = 0;
                for (DataSnapshot foodSnapshot : dataSnapshot.getChildren()) {
                    if(count > 5){
                        break;
                    }else {
                        Long foodIdLong = foodSnapshot.child("foodId").getValue(Long.class);
                        int foodId = foodIdLong != null ? foodIdLong.intValue() : -1;
                        String foodName = foodSnapshot.child("foodName").getValue(String.class);
                        String foodPic = foodSnapshot.child("foodPic").getValue(String.class);
                        Double foodFee = foodSnapshot.child("fee").getValue(Double.class);
                        Food food = new Food(foodId,foodName, foodPic, foodFee);
                        RecommendedArrayList.add(food);
                        count++;
                    }
                }

                // cập nhập dữ liệu
                adapter2.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý lỗi nếu cần
            }
        });
    }

    private void recyclerViewCategory() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewCategoryList = findViewById(R.id.view1);
        recyclerViewCategoryList.setLayoutManager(linearLayoutManager);

        database = FirebaseDatabase.getInstance();
        category = database.getReference("Category");
        ArrayList<Category> categoryArrayList = new ArrayList<>();
        adapter = new CategoryAdapter(categoryArrayList);
        recyclerViewCategoryList.setAdapter(adapter);



        // Lấy dữ liệu danh mục từ Firebase
        category.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                categoryArrayList.clear(); // Xóa danh sách cũ trước khi thêm dữ liệu mới

                for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
                    String categoryId = categorySnapshot.getKey();
                    String categoryName = categorySnapshot.child("categoryName").getValue(String.class);
                    String categoryPic = categorySnapshot.child("categoryPic").getValue(String.class);

                    Category category = new Category(categoryName, categoryPic);
                    categoryArrayList.add(category);
                }

                // Cập nhật RecyclerView khi có dữ liệu mới
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý lỗi nếu có
            }
        });



    }

}