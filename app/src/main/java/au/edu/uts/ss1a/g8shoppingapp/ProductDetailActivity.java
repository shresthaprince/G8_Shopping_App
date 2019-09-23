package au.edu.uts.ss1a.g8shoppingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import au.edu.uts.ss1a.g8shoppingapp.CurrentModel.CurrentModel;
import au.edu.uts.ss1a.g8shoppingapp.Model.Products;

public class ProductDetailActivity extends AppCompatActivity {

    private FloatingActionButton cartBtn;
    private ImageView productImage;
    private ElegantNumberButton stockCounter;
    private TextView productName, productDesc, productPrice;
    private String productID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        productID = getIntent().getStringExtra("prodID");

        cartBtn = (FloatingActionButton) findViewById(R.id.cart_btn);
        stockCounter = (ElegantNumberButton) findViewById(R.id.product_details_counter);

        productImage = (ImageView) findViewById(R.id.product_details_image);
        productName = (TextView) findViewById(R.id.product_details_name);
        productDesc = (TextView) findViewById(R.id.product_details_desc);
        productPrice = (TextView) findViewById(R.id.product_details_price);

        getProductDetails();

        cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addProductToCart();
            }
        });
    }

    private void getProductDetails() {
        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        productsRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Products products = dataSnapshot.getValue(Products.class);

                    productName.setText(products.getName());
                    productDesc.setText(products.getDescription());
                    productPrice.setText(products.getPrice());
                    Picasso.get().load(products.getImage()).into(productImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void addProductToCart() {
        String saveCurrentTime, saveCurrentDate;

        Calendar calDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentDate.format(calDate.getTime());

        final DatabaseReference cartListReference = FirebaseDatabase.getInstance().getReference().child("CartList");

        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("prodID", productID);
        cartMap.put("prodName", productName.getText().toString());
        cartMap.put("prodPrice", productPrice.getText().toString());
        cartMap.put("prodDesc", productDesc.getText().toString());
        cartMap.put("prodTime", saveCurrentTime);
        cartMap.put("prodDate", saveCurrentDate);
        cartMap.put("prodQuantity", stockCounter.getNumber());

        cartListReference.child("User View").child(CurrentModel.currentUser.getPhonenumber()).child("Products").child(productID).updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    cartListReference.child("Admin View").child(CurrentModel.currentUser.getPhonenumber()).child("Products").child(productID).updateChildren(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ProductDetailActivity.this, "Added to Cart", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ProductDetailActivity.this, CartActivity.class);
                                startActivity(intent);
                            }
                        }
                    });
                }
            }
        });
    }
}
