package au.edu.uts.ss1a.g8shoppingapp;

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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import au.edu.uts.ss1a.g8shoppingapp.Model.AdminOrder;

public class AdminOrdersActivity extends AppCompatActivity {

    private RecyclerView ordersList;
    private DatabaseReference ordersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_orders);

        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders");

        ordersList = findViewById(R.id.admin_orders_list);
        ordersList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<AdminOrder> options =
                new FirebaseRecyclerOptions.Builder<AdminOrder>()
                        .setQuery(ordersRef, AdminOrder.class)
                        .build();

        FirebaseRecyclerAdapter<AdminOrder, AdminOrderViewHolder> adapter =
                new FirebaseRecyclerAdapter<AdminOrder, AdminOrderViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull AdminOrderViewHolder adminOrderViewHolder, final int i, @NonNull final AdminOrder adminOrder) {
                        adminOrderViewHolder.userName.setText("User Details: " + adminOrder.getUserName());
                        adminOrderViewHolder.userPhonenumber.setText(adminOrder.getUserPhonenumber());
                        adminOrderViewHolder.userAddressStreet.setText(adminOrder.getUserAddressStreet());
                        adminOrderViewHolder.userAddressCity.setText(adminOrder.getUserAddressCity());
                        adminOrderViewHolder.userAddressPostcode.setText(adminOrder.getUserAddressPostcode());
                        adminOrderViewHolder.TotalAmount.setText("Total Bill: " + adminOrder.getTotalAmount());
                        adminOrderViewHolder.date.setText("Ordered at: " + adminOrder.getDate());
                        adminOrderViewHolder.time.setText(adminOrder.getTime());

                        adminOrderViewHolder.showProductsBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String userID = getRef(i).getKey();

                                Intent intent = new Intent(AdminOrdersActivity.this, AdminUserProductsActivity.class);
                                intent.putExtra("UserID", userID);
                                startActivity(intent);
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public AdminOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_layout, parent, false);
                        return new AdminOrderViewHolder(view);
                    }
                };
        ordersList.setAdapter(adapter);
        adapter.startListening();
    }

    public static class AdminOrderViewHolder extends RecyclerView.ViewHolder {


        public TextView TotalAmount, userName, userPhonenumber, userAddressStreet, userAddressCity, userAddressPostcode, date, time, tracker;
        public Button showProductsBtn;

        public AdminOrderViewHolder(@NonNull View itemView) {
            super(itemView);

            TotalAmount = itemView.findViewById(R.id.order_total_price);
            userName = itemView.findViewById(R.id.order_user_name);
            userPhonenumber = itemView.findViewById(R.id.order_user_phonenumber);
            userAddressStreet = itemView.findViewById(R.id.order_address_street);
            userAddressCity = itemView.findViewById(R.id.order_address_city);
            userAddressPostcode = itemView.findViewById(R.id.order_address_postcode);
            date = itemView.findViewById(R.id.order_user_date);
            time = itemView.findViewById(R.id.order_user_time);
            showProductsBtn = itemView.findViewById(R.id.admin_show_product_btn);
        }
    }
}
