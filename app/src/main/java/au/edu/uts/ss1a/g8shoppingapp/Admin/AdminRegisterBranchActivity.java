package au.edu.uts.ss1a.g8shoppingapp.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import au.edu.uts.ss1a.g8shoppingapp.Customer.LoginActivity;
import au.edu.uts.ss1a.g8shoppingapp.Customer.RegisterActivity;
import au.edu.uts.ss1a.g8shoppingapp.R;

@SuppressWarnings("deprecation")
public class AdminRegisterBranchActivity extends AppCompatActivity {
    private Button addBranchBtn;
    private EditText inputName, inputID, inputPassword;
    private ProgressDialog dialogBox;
    private ImageView inputBranchImage;
    private static final int GalleryPick = 1;
    private Uri imageUri;
    private StorageReference branchImageRef;
    private String downloadImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_register_branch);

        addBranchBtn = (Button) findViewById(R.id.admin_branch_register_btn);
        inputName = (EditText) findViewById(R.id.admin_branch_register_name_input);
        inputID = (EditText) findViewById(R.id.admin_branch_id_input);
        inputPassword = (EditText) findViewById(R.id.admin_branch_register_password_input);
        dialogBox = new ProgressDialog(this);

        branchImageRef = FirebaseStorage.getInstance().getReference().child("Branch Images");

        inputBranchImage = (ImageView) findViewById(R.id.select_branch_image);

        addBranchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addBranch();
            }
        });

        inputBranchImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
    }

    private void addBranch() {
        final String name = inputName.getText().toString();
        final String idnumber = inputID.getText().toString();
        final String password = inputPassword.getText().toString();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Please specify branch name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(idnumber)) {
            Toast.makeText(this, "Please specify unique branch ID", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please specify branch password", Toast.LENGTH_SHORT).show();
        } else {
            dialogBox.setTitle("Adding Branch");
            dialogBox.setMessage("Checking if entered data already exists in our amazing database");
            dialogBox.setCanceledOnTouchOutside(false);
            dialogBox.show();

            final StorageReference filePath = branchImageRef.child(imageUri.getLastPathSegment() + idnumber + ".jpg");

            final UploadTask uploadTask = filePath.putFile(imageUri);

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    String message = e.toString();
                    Toast.makeText(AdminRegisterBranchActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                    dialogBox.dismiss();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(AdminRegisterBranchActivity.this, "Branch Image uploaded Successfully...", Toast.LENGTH_SHORT).show();

                    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }

                            downloadImageUrl = filePath.getDownloadUrl().toString();
                            return filePath.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                downloadImageUrl = task.getResult().toString();

                                Toast.makeText(AdminRegisterBranchActivity.this, "got the Product image Url Successfully...", Toast.LENGTH_SHORT).show();

                                validateBranchID(name, idnumber, password, downloadImageUrl);
                            }
                        }
                    });
                }
            });


        }
    }

    private void validateBranchID(final String name, final String idnumber, final String password, final String image) {

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!(dataSnapshot.child("Branches").child(idnumber).exists())) {
                    HashMap<String, Object> userDataMap = new HashMap<>();
                    userDataMap.put("phonenumber", idnumber);
                    userDataMap.put("name", name);
                    userDataMap.put("password", getMd5(password));
                    userDataMap.put("image", image);

                    RootRef.child("Branches").child(idnumber).updateChildren(userDataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(AdminRegisterBranchActivity.this, "Successfully registered branch in our amazing database", Toast.LENGTH_SHORT).show();
                                        dialogBox.dismiss();

                                        Intent intent = new Intent(AdminRegisterBranchActivity.this, AdminManageBranchesActivity.class);
                                        startActivity(intent);
                                    } else {
                                        dialogBox.dismiss();
                                        Toast.makeText(AdminRegisterBranchActivity.this, "Error. Please Try Again.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(AdminRegisterBranchActivity.this, "This ID is already in use", Toast.LENGTH_SHORT).show();
                    dialogBox.dismiss();
                    Toast.makeText(AdminRegisterBranchActivity.this, "Please try again using another ID", Toast.LENGTH_SHORT).show();

                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void openGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GalleryPick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GalleryPick && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            inputBranchImage.setImageURI(imageUri);
        }
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
