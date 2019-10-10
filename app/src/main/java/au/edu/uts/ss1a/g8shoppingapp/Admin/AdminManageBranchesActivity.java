package au.edu.uts.ss1a.g8shoppingapp.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import au.edu.uts.ss1a.g8shoppingapp.R;

public class AdminManageBranchesActivity extends AppCompatActivity {

    private Button addNewBranchBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_manage_branches);

        addNewBranchBtn = (Button) findViewById(R.id.admin_add_branch_btn);

        addNewBranchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminManageBranchesActivity.this, AdminRegisterBranchActivity.class);
                startActivity(intent);
            }
        });
    }
}
