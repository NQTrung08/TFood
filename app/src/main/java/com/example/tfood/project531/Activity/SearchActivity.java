package com.example.tfood.project531.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import com.example.tfood.R;
import com.example.tfood.project531.Adapter.FoodListAdapter;
import com.example.tfood.project531.Model.Food;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    RecyclerView recyclerSearchView;
    RecyclerView.Adapter searchAdapter;
    SearchView edtSearchView;

    private DatabaseReference databaseReference;
    private ArrayList<Food> searchResults ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        edtSearchView = findViewById(R.id.edtSearchView);
        recyclerSearchView = findViewById(R.id.viewSearch);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerSearchView.setLayoutManager(gridLayoutManager);

        searchResults = new ArrayList<>();
        searchAdapter = new FoodListAdapter(searchResults);
        recyclerSearchView.setAdapter(searchAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("Food");

        edtSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Xóa kết quả tìm kiếm trước đó
                searchResults.clear();
                if (newText.length() >= 1) {
                    // Nếu có ít nhất 1 ký tự được nhập, thực hiện tìm kiếm
                    updateSearchResults(newText);
                } else {
                    // Nếu ô tìm kiếm trống, hiển thị toàn bộ sản phẩm
                    retrieveAllFood();
                }
                return true;
            }
        });



    }

    private void updateSearchResults(String query) {

        Query searchQuery = databaseReference.orderByChild("foodName").startAt(query).endAt(query + "\uf8ff");
        searchQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // Duyệt qua các sản phẩm tìm được và thêm vào danh sách kết quả
                for (DataSnapshot foodSnapshot : dataSnapshot.getChildren()) {
                    Food food = foodSnapshot.getValue(Food.class);
                    searchResults.add(food);
                }
                // Cập nhật kết quả tìm kiếm lên giao diện
                searchAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private  void  retrieveAllFood(){
        // Hiển thị tất cả sản phẩm khi ô tìm kiếm trống
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                searchResults.clear();
                for (DataSnapshot foodSnapshot : dataSnapshot.getChildren()) {
                    Food food = foodSnapshot.getValue(Food.class);
                    searchResults.add(food);
                }
                searchAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý lỗi nếu cần
            }
        });
    }
}