package au.edu.uts.ss1a.g8shoppingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import au.edu.uts.ss1a.g8shoppingapp.CurrentModel.CurrentModel;

public class SettingsActivity extends AppCompatActivity {

    private ImageView profileImageView;
    private TextView profileImageTextBtn, closeTextBtn, updateTextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        profileImageView = (ImageView) findViewById(R.id.settings_profile_image);
        profileImageTextBtn = (TextView) findViewById(R.id.profile_image_change_btn);
        closeTextBtn = (TextView) findViewById(R.id.close_settings);
        updateTextBtn = (TextView) findViewById(R.id.update_settings);

        closeTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, UserHomeActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }


}


