package au.edu.uts.ss1a.g8shoppingapp.Customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import au.edu.uts.ss1a.g8shoppingapp.CurrentModel.CurrentModel;
import au.edu.uts.ss1a.g8shoppingapp.R;

public class CheckoutActivity extends AppCompatActivity {

    private EditText nameEditText, phoneEditText, addressStreetEditText, addressCityEditText, addressPostcodeEditText, cardNumberEditText, CVVEditText;
    private Button checkoutBtn;
    private TextView totalPriceTxt;
    private CheckBox cardTypeDebit, cardTypeCredit;
    private String cardType = "";

    CheckBox debitCardCBox, creditCardCBox;

    private String totalPrice = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        checkoutBtn = (Button) findViewById(R.id.checkout_btn);
        nameEditText = (EditText) findViewById(R.id.checkout_name);
        phoneEditText = (EditText) findViewById(R.id.checkout_phnumber);
        addressStreetEditText = (EditText) findViewById(R.id.checkout_address_street);
        addressCityEditText = (EditText) findViewById(R.id.checkout_address_city);
        addressPostcodeEditText = (EditText) findViewById(R.id.checkout_address_postcode);
        cardNumberEditText = (EditText) findViewById(R.id.checkout_payment_card_no);
        CVVEditText = (EditText) findViewById(R.id.checkout_payment_cvv);
        totalPriceTxt = (TextView) findViewById(R.id.checkout_total_price);
        cardTypeDebit = (CheckBox) findViewById(R.id.debit_checkbox);
        cardTypeCredit = (CheckBox) findViewById(R.id.credit_checkbox);

        totalPrice = getIntent().getStringExtra("Total Price");

        nameEditText.setText(CurrentModel.currentUser.getName());
        phoneEditText.setText(CurrentModel.currentUser.getPhonenumber());
        totalPriceTxt.setText("Total Price: $" + totalPrice);

        cardTypeDebit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((CheckBox) view).isChecked()) {
                    cardTypeCredit.setChecked(false);
                }
            }
        });

        cardTypeCredit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((CheckBox) view).isChecked()) {
                    cardTypeDebit.setChecked(false);
                }
            }
        });

        checkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check();
            }
        });
    }

    private void check() {

        if (cardTypeCredit.isChecked()) {
            cardType = "Credit";
        } else if (cardTypeDebit.isChecked()) {
            cardType = "Debit";
        } else {
            cardType = "";
        }

        if (TextUtils.isEmpty(nameEditText.getText().toString())) {
            Toast.makeText(this, "Please provide your name.", Toast.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(phoneEditText.getText().toString())) {
            Toast.makeText(this, "Please provide your phone number.", Toast.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(addressStreetEditText.getText().toString())) {
            Toast.makeText(this, "Please provide your street and unit number.", Toast.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(addressCityEditText.getText().toString())) {
            Toast.makeText(this, "Please provide your city.", Toast.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(addressPostcodeEditText.getText().toString())) {
            Toast.makeText(this, "Please provide your postcode.", Toast.LENGTH_SHORT).show();

        } else if (!(cardNumberEditText.getText().toString().length() == 16 || cardNumberEditText.getText().toString().length() == 19)) {
            Toast.makeText(this, "Invalid card no.", Toast.LENGTH_SHORT).show();

        } else if (CVVEditText.getText().toString().length() != 3) {
            Toast.makeText(this, "Invalid CVV", Toast.LENGTH_SHORT).show();

        } else if (cardType.equals("")) {
            Toast.makeText(this, "Please select card type.", Toast.LENGTH_SHORT).show();

        } else {
            confirmOrder();
        }

    }

    private void confirmOrder() {
        final String saveCurrentDate, saveCurrentTime;

        Calendar calDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calDate.getTime());

        final DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders")
                .child(CurrentModel.currentUser.getPhonenumber());

        HashMap<String, Object> ordersMap = new HashMap<>();
        ordersMap.put("TotalAmount", totalPrice);
        ordersMap.put("userName", nameEditText.getText().toString());
        ordersMap.put("userPhonenumber", phoneEditText.getText().toString());
        ordersMap.put("userAddressStreet", addressStreetEditText.getText().toString());
        ordersMap.put("userAddressCity", addressCityEditText.getText().toString());
        ordersMap.put("userAddressPostcode", addressPostcodeEditText.getText().toString());
        ordersMap.put("date", saveCurrentDate);
        ordersMap.put("time", saveCurrentTime);
        ordersMap.put("totalPrice", "Not Shipped");
        ordersMap.put("cardType", cardType);
        ordersMap.put("cardNo", getMd5(cardNumberEditText.getText().toString()));
        ordersMap.put("CVV", getMd5(CVVEditText.getText().toString()));

        ordersRef.updateChildren(ordersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    FirebaseDatabase.getInstance().getReference().child("CartList").child("User View").child(CurrentModel.currentUser.getPhonenumber()).removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(CheckoutActivity.this, "Your order has been placed successfully.", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(CheckoutActivity.this, UserHomeActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                }
            }
        });
    }

    public static String getMd5(String input) {
        try {

            // Static getInstance method is called with hashing MD5
            MessageDigest md = MessageDigest.getInstance("MD5");

            // digest() method is called to calculate message digest
            //  of an input digest() return array of byte
            byte[] messageDigest = md.digest(input.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }

        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
