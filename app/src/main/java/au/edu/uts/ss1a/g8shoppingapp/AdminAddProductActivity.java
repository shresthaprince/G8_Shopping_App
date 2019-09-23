package au.edu.uts.ss1a.g8shoppingapp;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

@SuppressWarnings("deprecation")
public class AdminAddProductActivity extends AppCompatActivity {

    private String categoryName, productName, description, price, saveCurrentDate, saveCurrentTime, productID, downloadImageUrl;
    private Button addNewProductBtn;
    private ImageView inputProductImage;
    private EditText inputProductName, inputProductDesc, inputProductPrice;
    private static final int GalleryPick = 1;
    private Uri imageUri;
    private StorageReference productImageRef;
    private DatabaseReference productRef;
    private ProgressDialog dialogBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_product);

        categoryName = getIntent().getExtras().get("category").toString();
        productImageRef = FirebaseStorage.getInstance().getReference().child("Product Images");
        productRef = FirebaseDatabase.getInstance().getReference().child("Products");
        dialogBox = new ProgressDialog(this);

        addNewProductBtn = (Button) findViewById(R.id.add_product_button);
        inputProductImage = (ImageView) findViewById(R.id.select_product_image);
        inputProductName = (EditText) findViewById(R.id.add_product_name);
        inputProductDesc = (EditText) findViewById(R.id.add_product_description);
        inputProductPrice = (EditText) findViewById(R.id.add_product_price);

        inputProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        addNewProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateProduct();
            }
        });
    }

    private void validateProduct() {
        productName = inputProductName.getText().toString();
        description = inputProductDesc.getText().toString();
        price = inputProductPrice.getText().toString();

        if (imageUri == null) {
            Toast.makeText(this, "Picture please", Toast.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(productName)) {
            Toast.makeText(this, "Please write product name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(description)) {
            Toast.makeText(this, "Please write product description", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(price)) {
            Toast.makeText(this, "Please write product price", Toast.LENGTH_SHORT).show();
        } else {
            storeProductInformation();
        }
    }

    private void storeProductInformation() {
        dialogBox.setTitle("Adding Product");
        dialogBox.setMessage("Adding image to our amazing database");
        dialogBox.setCanceledOnTouchOutside(false);
        dialogBox.show();

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("mm dd yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        productID = saveCurrentDate + saveCurrentTime;

        final StorageReference filePath = productImageRef.child(imageUri.getLastPathSegment() + productID + ".jpg");

        final UploadTask uploadTask = filePath.putFile(imageUri);


        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(AdminAddProductActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                dialogBox.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AdminAddProductActivity.this, "Product Image uploaded Successfully...", Toast.LENGTH_SHORT).show();

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

                            Toast.makeText(AdminAddProductActivity.this, "got the Product image Url Successfully...", Toast.LENGTH_SHORT).show();

                            saveProductInfo();
                        }
                    }
                });
            }
        });
    }

    private void saveProductInfo() {
        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("prodID", productID);
        productMap.put("name", productName);
        productMap.put("category", categoryName);
        productMap.put("description", description);
        productMap.put("price", price);
        productMap.put("date", saveCurrentDate);
        productMap.put("time", saveCurrentTime);
        productMap.put("image", downloadImageUrl);

        productRef.child(productID).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(AdminAddProductActivity.this, AdminCategoryActivity.class);
                            startActivity(intent);
                            dialogBox.dismiss();
                            Toast.makeText(AdminAddProductActivity.this, "Product is added successfully", Toast.LENGTH_SHORT).show();
                        } else {


                            dialogBox.dismiss();
                            String msg = task.getException().toString();
                            Toast.makeText(AdminAddProductActivity.this, "Error" + msg, Toast.LENGTH_SHORT).show();
                        }
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
            inputProductImage.setImageURI(imageUri);
        }
    }
}
