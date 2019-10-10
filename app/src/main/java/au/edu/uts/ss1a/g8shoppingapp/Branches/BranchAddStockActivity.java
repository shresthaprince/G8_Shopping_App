package au.edu.uts.ss1a.g8shoppingapp.Branches;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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
import au.edu.uts.ss1a.g8shoppingapp.Customer.CartActivity;
import au.edu.uts.ss1a.g8shoppingapp.Customer.ProductDetailActivity;
import au.edu.uts.ss1a.g8shoppingapp.Model.Products;
import au.edu.uts.ss1a.g8shoppingapp.R;

public class BranchAddStockActivity extends AppCompatActivity {

    private FloatingActionButton addBtn;
    private ImageView productImage;
    private EditText stock;
    private TextView productName, productDesc, productPrice;
    private String productID = "", imageLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch_add_stock);

        addBtn = (FloatingActionButton) findViewById(R.id.branch_add_btn);

        productID = getIntent().getStringExtra("prodID");

        stock = (EditText) findViewById(R.id.branch_product_stock);

        productImage = (ImageView) findViewById(R.id.branch_product_image);
        productName = (TextView) findViewById(R.id.branch_product_name);
        productDesc = (TextView) findViewById(R.id.branch_product_desc);
        productPrice = (TextView) findViewById(R.id.branch_product_price);

        getProductDetails();

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addProductToBranch();
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

                    imageLink = products.getImage();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void addProductToBranch() {
        String saveCurrentTime, saveCurrentDate;

        Calendar calDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calDate.getTime());

        final DatabaseReference branchListReference = FirebaseDatabase.getInstance().getReference().child("Branches");

        final HashMap<String, Object> branchMap = new HashMap<>();
        branchMap.put("prodID", productID);
        branchMap.put("prodName", productName.getText().toString());
        branchMap.put("prodPrice", productPrice.getText().toString());
        branchMap.put("prodDesc", productDesc.getText().toString());
        branchMap.put("prodTime", saveCurrentTime);
        branchMap.put("prodDate", saveCurrentDate);
        branchMap.put("prodStock", stock.getText().toString());
        branchMap.put("image", imageLink);

        branchListReference.child(CurrentModel.currentUser.getPhonenumber()).child("Products").child(productID).updateChildren(branchMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                    Toast.makeText(BranchAddStockActivity.this, "Added to Branch", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(BranchAddStockActivity.this, BranchAddProductActivity.class);
                    startActivity(intent);

                }

            }

        });
    }
}
