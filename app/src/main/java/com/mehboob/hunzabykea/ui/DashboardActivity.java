package com.mehboob.hunzabykea.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.mehboob.hunzabykea.R;
import com.mehboob.hunzabykea.databinding.ActivityDashboardBinding;

public class DashboardActivity extends AppCompatActivity {


    ActivityDashboardBinding binding;
    ActionBarDrawerToggle toggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      binding = ActivityDashboardBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);

        toggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.nav_open,R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
      getSupportActionBar().setHomeButtonEnabled(false);

      binding.navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
          @Override
          public boolean onNavigationItemSelected(@NonNull MenuItem item) {
              switch (item.getItemId())
              {
                  case R.id.home:
                      Toast.makeText(DashboardActivity.this, "Home is clicked", Toast.LENGTH_SHORT).show();

                  case R.id.booking:
                      Toast.makeText(DashboardActivity.this, "Booking is clicked", Toast.LENGTH_SHORT).show();

                  case R.id.walllet:
                      Toast.makeText(DashboardActivity.this, "Wallet is clicked", Toast.LENGTH_SHORT).show();

                  case R.id.rate:
                      Toast.makeText(DashboardActivity.this, "Rates is clicked", Toast.LENGTH_SHORT).show();

                  case R.id.about:
                      Toast.makeText(DashboardActivity.this, "About is clicked", Toast.LENGTH_SHORT).show();

                  case R.id.contact:

                      Toast.makeText(DashboardActivity.this, "Contact is clicked", Toast.LENGTH_SHORT).show();


              }
              return true;
          }
      });

    }
}