package au.edu.uts.ss1a.g8shoppingapp.Customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import au.edu.uts.ss1a.g8shoppingapp.Admin.AdminCategoryActivity;
import au.edu.uts.ss1a.g8shoppingapp.Branches.BranchesHomeActivity;
import au.edu.uts.ss1a.g8shoppingapp.CurrentModel.CurrentModel;
import au.edu.uts.ss1a.g8shoppingapp.Model.Customers;
import au.edu.uts.ss1a.g8shoppingapp.R;
import io.paperdb.Paper;

@SuppressWarnings("deprecation")
public class LoginActivity extends AppCompatActivity {

    private EditText inputPhnNumber, inputPassword;
    private Button loginBtn;
    private TextView AdminPageLink, BranchPageLink, UserPageLink1, UserPageLink2;
    private ProgressDialog dialogBox;
    private CheckBox rememberMeCBox;

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
        rememberMeCBox = (CheckBox) findViewById(R.id.remember_me_checkbox);
        dialogBox = new ProgressDialog(this);

        Paper.init(this);

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
                rememberMeCBox.setVisibility(View.INVISIBLE);
                rememberMeCBox.setChecked(false);
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
                rememberMeCBox.setVisibility(View.VISIBLE);
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
                rememberMeCBox.setVisibility(View.INVISIBLE);
                rememberMeCBox.setChecked(false);
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
                rememberMeCBox.setVisibility(View.VISIBLE);
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


        if (rememberMeCBox.isChecked()) {
            Paper.book().write(CurrentModel.userPhoneNumber, phnumber);
            Paper.book().write(CurrentModel.userPassword, password);
        }

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(parentDbName).child(phnumber).exists()) {
                    Customers userData = dataSnapshot.child(parentDbName).child(phnumber).getValue(Customers.class);

                    if (userData.getPhonenumber().equals(phnumber)) {
                        if (userData.getPassword().equals(getMd5(password))) {
                            if (parentDbName.equals("Administrators")) {
                                Toast.makeText(LoginActivity.this, "Welcome God", Toast.LENGTH_SHORT).show();
                                dialogBox.dismiss();

                                Intent intent = new Intent(LoginActivity.this, AdminCategoryActivity.class);
                                startActivity(intent);
                            } else if (parentDbName.equals("Customers")) {
                                Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                dialogBox.dismiss();

                                Intent intent = new Intent(LoginActivity.this, UserHomeActivity.class);
                                CurrentModel.currentUser = userData;
                                startActivity(intent);

                            } else if (parentDbName.equals("Branches")) {
                                Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                CurrentModel.currentUser = userData;
                                dialogBox.dismiss();


                                Intent intent = new Intent(LoginActivity.this, BranchesHomeActivity.class);
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
