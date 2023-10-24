package com.example.tfood.project531.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tfood.R;
import com.example.tfood.project531.Common.Common;
import com.example.tfood.project531.Model.Cart;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class PlaceOrderActivity extends AppCompatActivity {
    EditText edtNameOrder, edtPhoneOrder, edtAddressOrder;
    TextView subTotalOrder, deliveryOrder, totalOrderPlace ;
    ImageView backConfirmOrder;
    ConstraintLayout PlaceOrderBtn;
    ArrayList<String> foodNames ;
    ArrayList<Integer> quantities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);

        edtNameOrder = findViewById(R.id.edtNameOrder);
        edtPhoneOrder = findViewById(R.id.edtPhoneOrder);
        edtAddressOrder = findViewById(R.id.edtAddressOrder);
        subTotalOrder = findViewById(R.id.subTotalOrder);
        deliveryOrder = findViewById(R.id.deliveryOrder);
        PlaceOrderBtn = findViewById(R.id.PlaceOrderBtn);
        backConfirmOrder = findViewById(R.id.backConfirmOrder);
        totalOrderPlace = findViewById(R.id.totalOrderPlace);

        // Lấy dữ liệu từ Intent
        Intent intent = getIntent();
        if (intent != null) {
            foodNames = intent.getStringArrayListExtra("foodNames");
            quantities = intent.getIntegerArrayListExtra("quantities");
            double subTotal = intent.getDoubleExtra("subTotal", 0);
            double totalOrder = subTotal + 3.0;

            // Sử dụng dữ liệu này để cập nhật giao diện người dùng trong PlaceOrderActivity
            subTotalOrder.setText("$ " + subTotal);
            totalOrderPlace.setText("$ " + totalOrder);
        }

//      BackOrder ///////////////
        backConfirmOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
//      PlaceOrder ////////////
        PlaceOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Lấy dữ liệu từ các EditTexts
                String name = edtNameOrder.getText().toString();
                String phone = edtPhoneOrder.getText().toString();
                String address = edtAddressOrder.getText().toString();
                String totalOrder = totalOrderPlace.getText().toString();

                // Tính tổng số sản phẩm
                int totalQuantities = 0;
                for (int i = 0; i < quantities.size(); i++) {
                    totalQuantities += quantities.get(i);
                }


                // Tạo một DatabaseReference đến nút "Orders" trong cơ sở dữ liệu Firebase
                DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference("Order").child(Common.currentUser.getPhone());
                // Tạo một đơn hàng mới bằng cách sử dụng push()
                DatabaseReference newOrderRef = ordersRef.push();

                newOrderRef.child("name").setValue(name);
                newOrderRef.child("phone").setValue(phone);
                newOrderRef.child("address").setValue(address);
                newOrderRef.child("totalOrder").setValue(totalOrder);
                newOrderRef.child("totalQuantities").setValue(totalQuantities);

                // Lưu danh sách món ăn và số lượng vào Firebase
                for (int i = 0; i < foodNames.size(); i++) {
                    newOrderRef.child("foodNames").child(String.valueOf(i)).setValue(foodNames.get(i));
                }

                final ProgressDialog progressDialog = new ProgressDialog(PlaceOrderActivity.this);
                progressDialog.setMessage("Loading..."); // Đặt thông báo cho ProgressDialog
                progressDialog.show(); // Hiển thị ProgressDialog


//                Thong bao thanh cong
                // Sử dụng Handler để đợi một khoảng thời gian trước khi hiển thị thông báo
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Hiển thị thông báo đặt hàng thành công
                        Toast.makeText(PlaceOrderActivity.this, "Order Success", Toast.LENGTH_SHORT).show();

                        // Khi đặt hàng thành công
                        setResult(RESULT_OK);
                        finish();
                        // Xóa giỏ hàng sau khi đặt hàng thành công
                        clearCart();

                        Intent orderDetailIntent = new Intent(PlaceOrderActivity.this, OrderDetailActivity.class);
                        startActivity(orderDetailIntent);

                    }
                }, 2000); // 2000 milliseconds (2 giây)





            }
        });

    }
    // Hàm để xóa giỏ hàng
    private void clearCart() {
        DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("Cart").child(Common.currentUser.getPhone());
        cartRef.removeValue(); // Xóa toàn bộ nút giỏ hàng
    }
}