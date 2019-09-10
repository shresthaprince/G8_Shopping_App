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

    private String categoryName, productName, productDesc, productPrice, saveCurrentDate, saveCurrentTime, productID, downloadImageURL;
    private Button addNewProductBtn;
    private ImageView inputProductImage;
    private EditText inputProductName, inputProductDesc, inputProductPrice;
    private static final int galleryPick = 1;
    private Uri ImageUri;
    private StorageReference productImagesRef;
    private DatabaseReference productsRef;
    private ProgressDialog dialogBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_product);

        categoryName = getIntent().getExtras().get("category").toString();
        productImagesRef = FirebaseStorage.getInstance().getReference().child("Product Images");
        productsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        addNewProductBtn = (Button) findViewById(R.id.add_product_button);
        inputProductImage = (ImageView) findViewById(R.id.select_product_image);
        inputProductName = (EditText) findViewById(R.id.add_product_name);
        inputProductDesc = (EditText) findViewById(R.id.add_product_description);
        inputProductPrice = (EditText) findViewById(R.id.add_product_price);
        dialogBox = new ProgressDialog(this);

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

    private void openGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, galleryPick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == galleryPick && resultCode == RESULT_OK && data != null) {
            ImageUri = data.getData();
            inputProductImage.setImageURI(ImageUri);
        }
    }

    private void validateProduct() {

        productName = inputProductName.getText().toString();
        productDesc = inputProductDesc.getText().toString();
        productPrice = inputProductPrice.getText().toString();

        if (ImageUri == null) {
            Toast.makeText(this, "Please add an image", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(productName)) {
            Toast.makeText(this, "Please write the product name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(productDesc)) {
            Toast.makeText(this, "Please write the product description", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(productPrice)) {
            Toast.makeText(this, "Please declare the product price", Toast.LENGTH_SHORT).show();
        } else {
            storeProductInformation();
        }
    }

    private void storeProductInformation() {
        dialogBox.setTitle("Adding new Product");
        dialogBox.setMessage("Please wait while we add the product to our amazing database");
        dialogBox.setCanceledOnTouchOutside(false);
        dialogBox.show();

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        productID = getCategoryNumber(categoryName) + saveCurrentDate + saveCurrentTime;

        final StorageReference filePath = productImagesRef.child(ImageUri.getLastPathSegment() + productID);

        final UploadTask uploadTask = filePath.putFile(ImageUri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String msg = e.toString();
                Toast.makeText(AdminAddProductActivity.this, "Error:" + msg, Toast.LENGTH_SHORT).show();
                dialogBox.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AdminAddProductActivity.this, "Image Uploaded Successfully", Toast.LENGTH_SHORT).show();

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();

                        }
                        downloadImageURL = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(AdminAddProductActivity.this, "Saving product url", Toast.LENGTH_SHORT).show();

                            saveProductInfo();
                        }
                    }
                });
            }
        });
    }

    private void saveProductInfo(){
        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("id", productID);
        productMap.put("date", saveCurrentDate);
        productMap.put("time", saveCurrentTime);
        productMap.put("name", productName);
        productMap.put("description", productDesc);
        productMap.put("price", productPrice);
        productMap.put("image", downloadImageURL);
        productMap.put("category", categoryName);

        productsRef.child(productID).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            downloadImageURL = task.getResult().toString();

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

    private String getCategoryNumber(String categoryName) {
        if (categoryName.equals("pc")) {
            return "001";
        } else if (categoryName.equals("laptop")) {
            return "002";
        } else if (categoryName.equals("phone")) {
            return "003";
        } else if (categoryName.equals("usb")) {
            return "004";
        } else if (categoryName.equals("console")) {
            return "005";
        } else if (categoryName.equals("headphones")) {
            return "006";
        } else if (categoryName.equals("router")) {
            return "007";
        } else if (categoryName.equals("tablet")) {
            return "008";
        }
        return "000";
    }
}
