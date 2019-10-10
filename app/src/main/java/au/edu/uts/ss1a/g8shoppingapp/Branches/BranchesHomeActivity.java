package au.edu.uts.ss1a.g8shoppingapp.Branches;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import au.edu.uts.ss1a.g8shoppingapp.Admin.AdminCategoryActivity;
import au.edu.uts.ss1a.g8shoppingapp.CurrentModel.CurrentModel;
import au.edu.uts.ss1a.g8shoppingapp.Customer.LoginActivity;
import au.edu.uts.ss1a.g8shoppingapp.R;

public class BranchesHomeActivity extends AppCompatActivity {

    private Button logoutBtn, addProductBtn, manageProductsBtn;
    private ImageView branchImage;
    private DatabaseReference branchRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branches_home);

        logoutBtn = (Button) findViewById(R.id.branch_logout_btn);
        addProductBtn = (Button) findViewById(R.id.branch_add_product_btn);
        manageProductsBtn = (Button) findViewById(R.id.branch_manage_product_btn);

        branchImage = (ImageView) findViewById(R.id.branch_home_image);

        branchRef = FirebaseDatabase.getInstance().getReference().child("Branches").child(CurrentModel.currentUser.getPhonenumber()).child("image");

        Picasso.get().load(branchRef.toString()).into(branchImage);

        addProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BranchesHomeActivity.this, BranchAddProductActivity.class);
                startActivity(intent);
            }
        });

        manageProductsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BranchesHomeActivity.this, BranchSearchProductsActivity.class);
                startActivity(intent);
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BranchesHomeActivity.this, LoginActivity.class);
                intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

}
