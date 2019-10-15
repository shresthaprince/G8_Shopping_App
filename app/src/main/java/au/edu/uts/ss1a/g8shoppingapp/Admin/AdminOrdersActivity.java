package au.edu.uts.ss1a.g8shoppingapp.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import au.edu.uts.ss1a.g8shoppingapp.Model.AdminOrder;
import au.edu.uts.ss1a.g8shoppingapp.Model.Products;
import au.edu.uts.ss1a.g8shoppingapp.R;

public class AdminOrdersActivity extends AppCompatActivity {

    private RecyclerView ordersList;
    private DatabaseReference ordersRef, branchRef;
    private FloatingActionButton homeBtn;
    private boolean update = false;
    private String date = "", time = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_orders);

        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders");
        branchRef = FirebaseDatabase.getInstance().getReference().child("Branches");

        homeBtn = (FloatingActionButton) findViewById(R.id.home_btn);

        ordersList = findViewById(R.id.admin_orders_list);
        ordersList.setLayoutManager(new LinearLayoutManager(this));

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminOrdersActivity.this, AdminCategoryActivity.class);
                startActivity(intent);
            }
        });
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
                                intent.putExtra("User ID", userID);
                                startActivity(intent);


                            }
                        });

                        adminOrderViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                CharSequence options[] = new CharSequence[]{

                                        "Yes",
                                        "No"
                                };

                                AlertDialog.Builder builder = new AlertDialog.Builder(AdminOrdersActivity.this);
                                builder.setTitle("Has orders been shipped?");

                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if (i == 0) {
                                            String userID = getRef(i).getKey();

                                            update = true;

                                            date = adminOrder.getDate();
                                            time = adminOrder.getTime();

                                            updateOrder(userID);
                                            removeOrder(userID);
                                        } else {
                                            finish();
                                        }
                                    }
                                });

                                builder.show();
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

    private void updateOrder(final String userID) {

        DatabaseReference prodRef = FirebaseDatabase.getInstance().getReference().child("CartList").child("Admin View").child(userID).child("Products");
        prodRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot zoneSnapshot : dataSnapshot.getChildren()) {
                    final String prodID = zoneSnapshot.getKey();
                    final String branchID = zoneSnapshot.child("branchID").getValue(String.class);
                    String prodQuantity = zoneSnapshot.child("prodQuantity").getValue(String.class);
                    final int quantity = Integer.parseInt(prodQuantity);
                    if (update) {
                        branchRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                                String stock = dataSnapshot.child(branchID).child("Products").child(prodID).child("prodStock").getValue(String.class);
                                int existingStock = Integer.parseInt(stock);
                                HashMap<String, Object> productMap = new HashMap<>();
                                productMap.put("prodStock", Integer.toString(existingStock - quantity));
                                branchRef.child(branchID).child("Products").child(prodID).updateChildren(productMap);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }
                update = false;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        final DatabaseReference orderTransferRef = FirebaseDatabase.getInstance().getReference().child("CartList").child("Admin View").child(userID);


        final DatabaseReference orderHistoryRef = FirebaseDatabase.getInstance().getReference().child("Order History").child(date).child(time).child(userID);
        orderTransferRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                orderHistoryRef.setValue(dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        orderTransferRef.removeValue();
        ordersRef.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                orderHistoryRef.setValue(dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void removeOrder(final String userID) {


        ordersRef.child(userID).removeValue();
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
