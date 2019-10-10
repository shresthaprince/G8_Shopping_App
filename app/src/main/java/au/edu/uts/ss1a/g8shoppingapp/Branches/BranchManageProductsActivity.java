package au.edu.uts.ss1a.g8shoppingapp.Branches;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import au.edu.uts.ss1a.g8shoppingapp.Admin.AdminCategoryActivity;
import au.edu.uts.ss1a.g8shoppingapp.CurrentModel.CurrentModel;
import au.edu.uts.ss1a.g8shoppingapp.R;

public class BranchManageProductsActivity extends AppCompatActivity {

    private Button applyChangesBtn, deleteBtn;
    private EditText productName, productDesc, productPrice, productStock;
    private ImageView productImage;
    private String productID = "";
    private DatabaseReference productsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch_manage_products);

        applyChangesBtn = (Button) findViewById(R.id.branch_manage_apply_changes_btn);
        deleteBtn = (Button) findViewById(R.id.branch_manage_delete_btn);

        productName = (EditText) findViewById(R.id.branch_manage_product_name);
        productDesc = (EditText) findViewById(R.id.branch_manage_product_description);
        productPrice = (EditText) findViewById(R.id.branch_manage_product_price);
        productStock = (EditText) findViewById(R.id.branch_manage_product_stock);

        productImage = (ImageView) findViewById(R.id.branch_manage_product_image);

        productID = getIntent().getStringExtra("prodID");

        productsRef = FirebaseDatabase.getInstance().getReference().child("Branches").child(CurrentModel.currentUser.getPhonenumber()).child("Products").child(productID);

        displayProductInfo();

        applyChangesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                applyChanges();
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteProduct();
            }
        });
    }

    private void deleteProduct() {
        productsRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(BranchManageProductsActivity.this, "The product is deleted.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(BranchManageProductsActivity.this, BranchSearchProductsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void applyChanges() {
        String name = productName.getText().toString();
        String desc = productDesc.getText().toString();
        String price = productPrice.getText().toString();
        String stock = productStock.getText().toString();

        if (name.equals("")) {
            Toast.makeText(this, "Name field is empty", Toast.LENGTH_SHORT).show();
        } else if (desc.equals("")) {
            Toast.makeText(this, "Description is empty", Toast.LENGTH_SHORT).show();
        } else if (price.equals("")) {
            Toast.makeText(this, "Price field is empty", Toast.LENGTH_SHORT).show();
        } else if (stock.equals("")) {
            Toast.makeText(this, "Stock field is empty", Toast.LENGTH_SHORT).show();
        } else {
            HashMap<String, Object> productMap = new HashMap<>();
            productMap.put("prodID", productID);
            productMap.put("prodName", name);
            productMap.put("prodDesc", desc);
            productMap.put("prodPrice", price);
            productMap.put("prodStock", stock);


            productsRef.updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(BranchManageProductsActivity.this, "Products details updated.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(BranchManageProductsActivity.this, BranchSearchProductsActivity.class);
                        startActivity(intent);
                    }
                }
            });
        }
    }

    /*productMap.put("prodID", productID);
            productMap.put("name", productName);
            productMap.put("category", categoryName);
            productMap.put("description", description);
            productMap.put("price", price);
            productMap.put("date", saveCurrentDate);
            productMap.put("time", saveCurrentTime);
            productMap.put("image", downloadImageUrl);*/
    private void displayProductInfo() {

        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String name = dataSnapshot.child("prodName").getValue().toString();
                    String desc = dataSnapshot.child("prodDesc").getValue().toString();
                    String price = dataSnapshot.child("prodPrice").getValue().toString();
                    String stock = dataSnapshot.child("prodStock").getValue().toString();
                    String imageUrl = dataSnapshot.child("image").getValue().toString();

                    productName.setText(name);
                    productDesc.setText(desc);
                    productPrice.setText(price);
                    productStock.setText(stock);
                    Picasso.get().load(imageUrl).into(productImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
