package au.edu.uts.ss1a.g8shoppingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import au.edu.uts.ss1a.g8shoppingapp.CurrentModel.CurrentModel;
import au.edu.uts.ss1a.g8shoppingapp.Model.Customers;
import io.paperdb.Paper;

@SuppressWarnings("deprecation")

public class MainActivity extends AppCompatActivity {

    private Button regsiterBtn, loginBtn;
    private ProgressDialog dialogBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        regsiterBtn = (Button) findViewById(R.id.main_register_btn);
        loginBtn = (Button) findViewById(R.id.main_login_btn);

        dialogBox = new ProgressDialog(this);


        Paper.init(this);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        regsiterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        String userPhoneNumber = Paper.book().read(CurrentModel.userPhoneNumber);
        String userPassword = Paper.book().read(CurrentModel.userPassword);

        if (userPhoneNumber != "" && userPassword != "") {
            if (!TextUtils.isEmpty(userPhoneNumber) && !TextUtils.isEmpty(userPassword)) {
                login(userPhoneNumber, userPassword);
                dialogBox.setTitle("Already Logged In");
                dialogBox.setMessage("Checking info");
                dialogBox.setCanceledOnTouchOutside(false);
                dialogBox.show();
            }
        }
    }

    private void login(final String phnumber, final String password) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("Customers").child(phnumber).exists()) {
                    Customers userData = dataSnapshot.child("Customers").child(phnumber).getValue(Customers.class);

                    if (userData.getPhonenumber().equals(phnumber)) {
                        if (userData.getPassword().equals(password)) {

                            Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            dialogBox.dismiss();

                            Intent intent = new Intent(MainActivity.this, UserHomeActivity.class);
                            CurrentModel.currentUser = userData;
                            startActivity(intent);
                        } else {
                            Toast.makeText(MainActivity.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                            dialogBox.dismiss();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Incorrect Number", Toast.LENGTH_SHORT).show();
                        dialogBox.dismiss();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Number does not exist", Toast.LENGTH_SHORT).show();
                    dialogBox.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
