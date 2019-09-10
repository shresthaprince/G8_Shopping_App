package au.edu.uts.ss1a.g8shoppingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
@SuppressWarnings("deprecation")
public class RegisterActivity extends AppCompatActivity {

    private Button createAccountBtn;
    private EditText inputName, inputPhnNumber, inputPassword;
    private ProgressDialog dialogBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        createAccountBtn = (Button) findViewById(R.id.register_btn);
        inputName = (EditText) findViewById(R.id.register_name_input);
        inputPhnNumber = (EditText) findViewById(R.id.register_phnumber_input);
        inputPassword = (EditText) findViewById(R.id.register_password_input);
        dialogBox = new ProgressDialog(this);

        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount();
            }
        });
    }

    private void createAccount() {
        String name = inputName.getText().toString();
        String phnumber = inputPhnNumber.getText().toString();
        String password = inputPassword.getText().toString();


        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Please write your name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(phnumber)) {
            Toast.makeText(this, "Please write your phone number", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please write your password", Toast.LENGTH_SHORT).show();
        } else {
            dialogBox.setTitle("Creating Account");
            dialogBox.setMessage("Checking if entered data already exists in our amazing database");
            dialogBox.setCanceledOnTouchOutside(false);
            dialogBox.show();

            validatePhoneNumber(name, phnumber, password);
        }


    }

    private void validatePhoneNumber(final String name, final String phnumber, final String password) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!(dataSnapshot.child("Customers").child(phnumber).exists())) {
                    HashMap<String, Object> userDataMap = new HashMap<>();
                    userDataMap.put("phonenumber", phnumber);
                    userDataMap.put("name", name);
                    userDataMap.put("password", password);

                    RootRef.child("Customers").child(phnumber).updateChildren(userDataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(RegisterActivity.this, "Successfully registered you in our amazing database", Toast.LENGTH_SHORT).show();
                                        dialogBox.dismiss();

                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                    } else {
                                        dialogBox.dismiss();
                                        Toast.makeText(RegisterActivity.this, "Error. Please Try Again.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(RegisterActivity.this, "This phone number already exists", Toast.LENGTH_SHORT).show();
                    dialogBox.dismiss();
                    Toast.makeText(RegisterActivity.this, "Please try again using another phone number", Toast.LENGTH_SHORT).show();

                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
