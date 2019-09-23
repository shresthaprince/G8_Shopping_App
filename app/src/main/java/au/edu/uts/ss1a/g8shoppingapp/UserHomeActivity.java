package au.edu.uts.ss1a.g8shoppingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import au.edu.uts.ss1a.g8shoppingapp.CurrentModel.CurrentModel;
import au.edu.uts.ss1a.g8shoppingapp.ui.BranchFragment;
import au.edu.uts.ss1a.g8shoppingapp.ui.CategoryFragment;
import au.edu.uts.ss1a.g8shoppingapp.ui.HomeFragment;
import au.edu.uts.ss1a.g8shoppingapp.ui.OrdersFragment;
import au.edu.uts.ss1a.g8shoppingapp.ui.WhatsNewFragment;
import io.paperdb.Paper;

public class UserHomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Paper.init(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        TextView userNameTextView = headerView.findViewById(R.id.user_profile_name);
        ImageView userImageView = headerView.findViewById(R.id.user_profile_image);

        userNameTextView.setText(CurrentModel.currentUser.getName());

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new HomeFragment()).commit();

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new HomeFragment()).commit();
                break;
            case R.id.nav_whats_new:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new WhatsNewFragment()).commit();
                break;
            case R.id.nav_all_products:
                Intent intent = new Intent(UserHomeActivity.this, AllProductsActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_category:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new CategoryFragment()).commit();
                break;
            case R.id.nav_branch:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new BranchFragment()).commit();
                break;
            case R.id.nav_cart:
                intent = new Intent(UserHomeActivity.this, CartActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_orders:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new OrdersFragment()).commit();
                break;
            case R.id.nav_settings:
                intent = new Intent(UserHomeActivity.this, SettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_logout:

                Paper.book().destroy();

                intent = new Intent(UserHomeActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
