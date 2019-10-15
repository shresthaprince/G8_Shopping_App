package au.edu.uts.ss1a.g8shoppingapp.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import au.edu.uts.ss1a.g8shoppingapp.Branches.BranchesHomeActivity;
import au.edu.uts.ss1a.g8shoppingapp.Customer.LoginActivity;
import au.edu.uts.ss1a.g8shoppingapp.R;
import au.edu.uts.ss1a.g8shoppingapp.Customer.SearchProductsActivity;

public class AdminCategoryActivity extends AppCompatActivity {

    private ImageView pc, laptop, phone, usb, console, headphones, router, tablet;
    private Button checkOrderBtn, logoutBtn, manageProductsBtn, manageBranchesBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        logoutBtn = (Button) findViewById(R.id.admin_logout_btn);
        checkOrderBtn = (Button) findViewById(R.id.admin_check_order_btn);
        manageProductsBtn = (Button) findViewById(R.id.admin_manage_products);
        manageBranchesBtn = (Button) findViewById(R.id.admin_manage_branches);

        pc = (ImageView) findViewById(R.id.pc);
        laptop = (ImageView) findViewById(R.id.laptop);
        phone = (ImageView) findViewById(R.id.phone);
        usb = (ImageView) findViewById(R.id.usb);
        console = (ImageView) findViewById(R.id.console);
        headphones = (ImageView) findViewById(R.id.headphones);
        router = (ImageView) findViewById(R.id.router);
        tablet = (ImageView) findViewById(R.id.tablet);

        pc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddProductActivity.class);
                intent.putExtra("category", "Computers");
                startActivity(intent);
            }
        });

        laptop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddProductActivity.class);
                intent.putExtra("category", "Laptops");
                startActivity(intent);
            }
        });

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddProductActivity.class);
                intent.putExtra("category", "Smartphones");
                startActivity(intent);
            }
        });

        usb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddProductActivity.class);
                intent.putExtra("category", "usb");
                startActivity(intent);
            }
        });

        console.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddProductActivity.class);
                intent.putExtra("category", "console");
                startActivity(intent);
            }
        });

        headphones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddProductActivity.class);
                intent.putExtra("category", "Headphones");
                startActivity(intent);
            }
        });

        router.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddProductActivity.class);
                intent.putExtra("category", "router");
                startActivity(intent);
            }
        });

        tablet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddProductActivity.class);
                intent.putExtra("category", "tablet");
                startActivity(intent);
            }
        });

        checkOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminOrdersActivity.class);
                startActivity(intent);
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this, LoginActivity.class);
                intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        manageProductsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this, SearchProductsActivity.class);
                intent.putExtra("Admin", "Admin");
                startActivity(intent);
                finish();
            }
        });

        manageBranchesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminManageBranchesActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
