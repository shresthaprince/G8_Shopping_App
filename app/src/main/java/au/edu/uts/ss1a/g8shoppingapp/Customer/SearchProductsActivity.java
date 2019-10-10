package au.edu.uts.ss1a.g8shoppingapp.Customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import au.edu.uts.ss1a.g8shoppingapp.Admin.AdminCategoryActivity;
import au.edu.uts.ss1a.g8shoppingapp.Admin.AdminManageProductsActivity;
import au.edu.uts.ss1a.g8shoppingapp.Model.Products;
import au.edu.uts.ss1a.g8shoppingapp.R;
import au.edu.uts.ss1a.g8shoppingapp.ViewHolder.ProductViewHolder;

public class SearchProductsActivity extends AppCompatActivity {

    private Button searchBtn;
    private FloatingActionButton homeBtn;
    private RecyclerView recyclerView;
    private EditText searchName;
    private String inputText, type = "", branchID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_products);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            type = getIntent().getExtras().get("Admin").toString();
        }

        branchID = type;

        searchBtn = (Button) findViewById(R.id.search_btn);

        homeBtn = (FloatingActionButton) findViewById(R.id.home_btn_search_page);

        searchName = (EditText) findViewById(R.id.search_product_name);

        recyclerView = findViewById(R.id.search_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

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
                if (type.equals("Admin")) {
                    Intent intent = new Intent(SearchProductsActivity.this, AdminCategoryActivity.class);

                    startActivity(intent);
                } else {
                    Intent intent = new Intent(SearchProductsActivity.this, UserHomeActivity.class);

                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Branches").child(branchID).child("Products");

        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(productsRef.orderByChild("name").startAt(inputText), Products.class)
                        .build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder productViewHolder, int i, @NonNull final Products products) {
                        productViewHolder.textProductName.setText(products.getProdName());
                        productViewHolder.textProductDesc.setText(products.getProdDesc());
                        productViewHolder.textProductPrice.setText("Price: $" + products.getProdPrice());

                        Picasso.get().load(products.getImage()).into(productViewHolder.imageView);


                        productViewHolder.imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                if (type.equals("Admin")) {
                                    Intent intent = new Intent(SearchProductsActivity.this, AdminManageProductsActivity.class);


                                    startActivity(intent);
                                } else {
                                    Intent intent = new Intent(SearchProductsActivity.this, ProductDetailActivity.class);
                                    Bundle b = new Bundle();
                                    b.putString("prodID", products.getProdID());
                                    b.putString("branchID", branchID);
                                    intent.putExtra("personBdl", b);

                                    startActivity(intent);
                                }

                            }
                        });

                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.products_layout, parent, false);
                        ProductViewHolder holder = new ProductViewHolder(view);
                        return holder;
                    }
                };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}
