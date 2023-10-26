package com.example.tfood.project531.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.tfood.R;
import com.example.tfood.project531.Adapter.OrderDetailAdapter;
import com.example.tfood.project531.Common.Common;
import com.example.tfood.project531.Model.Order;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrderDetailActivity extends AppCompatActivity {
    ImageView backListOrder;
    RecyclerView recyclerViewOrder;
    RecyclerView.Adapter adapterOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        backListOrder = findViewById(R.id.backListOrder);
        recyclerViewOrder = findViewById(R.id.viewOrder);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewOrder.setLayoutManager(linearLayoutManager);

        ArrayList<Order> orderList = new ArrayList<>();
        adapterOrder = new OrderDetailAdapter(orderList);
        recyclerViewOrder.setAdapter(adapterOrder);

        DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference("Order").child(Common.currentUser.getPhone());
        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orderList.clear();

                for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()){
                    Order orderItem = orderSnapshot.getValue(Order.class);
                    orderList.add(orderItem);

                }
                adapterOrder.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        backListOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(OrderDetailActivity.this, ProfileActivity.class));
            }
        });
    }
}