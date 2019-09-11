package au.edu.uts.ss1a.g8shoppingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import au.edu.uts.ss1a.g8shoppingapp.Model.Customers;

@SuppressWarnings("deprecation")
public class LoginActivity extends AppCompatActivity {

    private EditText inputPhnNumber, inputPassword;
    private Button loginBtn;
    private TextView AdminPageLink, BranchPageLink, UserPageLink1, UserPageLink2;
    private ProgressDialog dialogBox;

    String parentDbName = "Customers";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginBtn = (Button) findViewById(R.id.login_btn);
        inputPassword = (EditText) findViewById(R.id.login_password_input);
        inputPhnNumber = (EditText) findViewById(R.id.login_phnumber_input);
        AdminPageLink = (TextView) findViewById(R.id.admin_page_link);
        BranchPageLink = (TextView) findViewById(R.id.branch_page_link);
        UserPageLink1 = (TextView) findViewById(R.id.user_page_link1);
        UserPageLink2 = (TextView) findViewById(R.id.user_page_link2);
        dialogBox = new ProgressDialog(this);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginCustomer();
            }
        });

        AdminPageLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginBtn.setText("Login As Admin");
                AdminPageLink.setVisibility(View.INVISIBLE);
                UserPageLink2.setVisibility(View.VISIBLE);
                UserPageLink1.setVisibility(View.INVISIBLE);
                BranchPageLink.setVisibility(View.VISIBLE);
                parentDbName = "Administrators";
            }
        });

        UserPageLink2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginBtn.setText("Login");
                UserPageLink2.setVisibility(View.INVISIBLE);
                AdminPageLink.setVisibility(View.VISIBLE);
                UserPageLink1.setVisibility(View.INVISIBLE);
                BranchPageLink.setVisibility(View.VISIBLE);
                parentDbName = "Customers";
            }
        });
        BranchPageLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginBtn.setText("Login As Seller");
                BranchPageLink.setVisibility(View.INVISIBLE);
                UserPageLink1.setVisibility(View.VISIBLE);
                AdminPageLink.setVisibility(View.VISIBLE);
                UserPageLink2.setVisibility(View.INVISIBLE);
                parentDbName = "Branches";
            }
        });

        UserPageLink1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginBtn.setText("Login");
                UserPageLink1.setVisibility(View.INVISIBLE);
                BranchPageLink.setVisibility(View.VISIBLE);
                AdminPageLink.setVisibility(View.VISIBLE);
                UserPageLink2.setVisibility(View.INVISIBLE);
                parentDbName = "Customers";
            }
        });
    }

    private void loginCustomer() {
        String phnumber = inputPhnNumber.getText().toString();
        String password = inputPassword.getText().toString();

        if (TextUtils.isEmpty(phnumber)) {
            Toast.makeText(this, "Please write your phone number", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please write your password", Toast.LENGTH_SHORT).show();
        } else {
            dialogBox.setTitle("Login");
            dialogBox.setMessage("Checking info");
            dialogBox.setCanceledOnTouchOutside(false);
            dialogBox.show();

            login(phnumber, password);
        }
    }

    private void login(final String phnumber, final String password) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(parentDbName).child(phnumber).exists()) {
                    Customers userData = dataSnapshot.child(parentDbName).child(phnumber).getValue(Customers.class);

                    if (userData.getPhonenumber().equals(phnumber)) {
                        if (userData.getPassword().equals(password)) {
                            if (parentDbName.equals("Administrators")){
                                Toast.makeText(LoginActivity.this, "Welcome God", Toast.LENGTH_SHORT).show();
                                dialogBox.dismiss();

                                Intent intent = new Intent(LoginActivity.this, AdminCategoryActivity.class);
                                startActivity(intent);
                            } else if (parentDbName.equals("Customers")){
                                Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                dialogBox.dismiss();

                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                            dialogBox.dismiss();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Incorrect Number", Toast.LENGTH_SHORT).show();
                        dialogBox.dismiss();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Number does not exist", Toast.LENGTH_SHORT).show();
                    dialogBox.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
