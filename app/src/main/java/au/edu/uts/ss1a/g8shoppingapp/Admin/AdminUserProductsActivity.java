package au.edu.uts.ss1a.g8shoppingapp.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import au.edu.uts.ss1a.g8shoppingapp.Model.Cart;
import au.edu.uts.ss1a.g8shoppingapp.Model.Customers;
import au.edu.uts.ss1a.g8shoppingapp.R;
import au.edu.uts.ss1a.g8shoppingapp.ViewHolder.CartViewHolder;

public class AdminUserProductsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_products);

        recyclerView = findViewById(R.id.admin_user_products_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        userID = getIntent().getStringExtra("User ID");




    }

    @Override
    protected void onStart() {
        super.onStart();

        final DatabaseReference productListRef = FirebaseDatabase.getInstance().getReference().child("CartList");

        FirebaseRecyclerOptions<Cart> options = new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(productListRef.child("Admin View")
                        .child(userID).child("Products"), Cart.class)
                .build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter =
                new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull CartViewHolder cartViewHolder, int i, @NonNull Cart cart) {
                        cartViewHolder.productNameTxt.setText(cart.getProdName());
                        cartViewHolder.productQuantityTxt.setText(cart.getProdQuantity());
                        cartViewHolder.productPriceTxt.setText(cart.getProdPrice());
                    }

                    @NonNull
                    @Override
                    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout, parent, false);
                        CartViewHolder holder = new CartViewHolder(view);
                        return holder;
                    }
                };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}
