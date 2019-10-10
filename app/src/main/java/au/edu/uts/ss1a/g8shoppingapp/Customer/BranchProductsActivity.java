package au.edu.uts.ss1a.g8shoppingapp.Customer;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import au.edu.uts.ss1a.g8shoppingapp.Branches.BranchManageProductsActivity;
import au.edu.uts.ss1a.g8shoppingapp.CurrentModel.CurrentModel;
import au.edu.uts.ss1a.g8shoppingapp.Model.Customers;
import au.edu.uts.ss1a.g8shoppingapp.R;
import au.edu.uts.ss1a.g8shoppingapp.ViewHolder.BranchViewHolder;

public class BranchProductsActivity extends AppCompatActivity {

    private Button searchBtn;
    private FloatingActionButton homeBtn;
    private RecyclerView recyclerView;
    private EditText searchName;
    private String inputText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch_search_products);


        searchBtn = (Button) findViewById(R.id.branch_search_btn);

        homeBtn = (FloatingActionButton) findViewById(R.id.branch_home_btn_search_page);

        searchName = (EditText) findViewById(R.id.branch_search_product_name);

        recyclerView = findViewById(R.id.branch_search_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        searchName.setHint("Search Branch");

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputText = searchName.getText().toString();
                onStart();
            }
        });

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(BranchProductsActivity.this, UserHomeActivity.class);

                startActivity(intent);

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        DatabaseReference branchesRef = FirebaseDatabase.getInstance().getReference().child("Branches");

        FirebaseRecyclerOptions<Customers> options =
                new FirebaseRecyclerOptions.Builder<Customers>()
                        .setQuery(branchesRef.orderByChild("name").startAt(inputText), Customers.class)
                        .build();

        FirebaseRecyclerAdapter<Customers, BranchViewHolder> adapter =
                new FirebaseRecyclerAdapter<Customers, BranchViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull BranchViewHolder productViewHolder, int i, @NonNull final Customers products) {
                        productViewHolder.branchName.setText(products.getName());

                        Picasso.get().load(products.getImage()).into(productViewHolder.imageView);


                        productViewHolder.imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {


                                Intent intent = new Intent(BranchProductsActivity.this, SearchProductsActivity.class);
                                intent.putExtra("Admin", products.getPhonenumber());

                                startActivity(intent);


                            }
                        });

                    }

                    @NonNull
                    @Override
                    public BranchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.branch_layout, parent, false);
                        BranchViewHolder holder = new BranchViewHolder(view);
                        return holder;
                    }
                };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}
