package com.mehboob.hunzabykea.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.mehboob.hunzabykea.R;
import com.mehboob.hunzabykea.databinding.ActivityDashboardBinding;
import com.mehboob.hunzabykea.ui.fragments.AccountFragment;
import com.mehboob.hunzabykea.ui.fragments.BookingFragment;
import com.mehboob.hunzabykea.ui.fragments.HomeFragment;
import com.mehboob.hunzabykea.ui.fragments.NotificaronsFragment;
import com.mehboob.hunzabykea.ui.fragments.WalletFragment;

public class DashboardActivity extends AppCompatActivity {


    ActivityDashboardBinding binding;
    ActionBarDrawerToggle toggle;


    HomeFragment homeFragment = new HomeFragment();
    BookingFragment bookingFragment = new BookingFragment();
    WalletFragment walletFragment = new WalletFragment();

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
//



//        toggle = new ActionBarDrawerToggle(this, binding.drawerLayout, R.string.nav_open, R.string.nav_close);
//
//        binding.drawerLayout.addDrawerListener(toggle);


//        binding.navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.id.home:
//                        Toast.makeText(DashboardActivity.this, "Home is clicked", Toast.LENGTH_SHORT).show();
//
//                    case R.id.booking:
//                        Toast.makeText(DashboardActivity.this, "Booking is clicked", Toast.LENGTH_SHORT).show();
//
//                    case R.id.walllet:
//                        Toast.makeText(DashboardActivity.this, "Wallet is clicked", Toast.LENGTH_SHORT).show();
//
//                    case R.id.rate:
//                        Toast.makeText(DashboardActivity.this, "Rates is clicked", Toast.LENGTH_SHORT).show();
//
//                    case R.id.about:
//                        Toast.makeText(DashboardActivity.this, "About is clicked", Toast.LENGTH_SHORT).show();
//
//                    case R.id.contact:
//
//                        Toast.makeText(DashboardActivity.this, "Contact is clicked", Toast.LENGTH_SHORT).show();
//
//
//                }
//                return true;
//            }
//        });


        binding.bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.framlayout,homeFragment).commit();

      binding.bottomNavigationView.setOnItemReselectedListener(new NavigationBarView.OnItemReselectedListener() {
          @Override
          public void onNavigationItemReselected(@NonNull MenuItem item) {
              switch (item.getItemId())
              {
                  case R.id.home1:
                      getSupportFragmentManager().beginTransaction().replace(R.id.framlayout,homeFragment).commit();
                      break;
                  case R.id.wallet1:
                      getSupportFragmentManager().beginTransaction().replace(R.id.framlayout,walletFragment).commit();
                      break;

                  case R.id.booking1:
                      getSupportFragmentManager().beginTransaction().replace(R.id.framlayout,bookingFragment).commit();
                      break;


              }
          }
      });
        getSupportFragmentManager().beginTransaction().replace(R.id.framlayout,homeFragment).commit();

    }



    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Fragment fragment = new HomeFragment();
            switch (item.getItemId()) {
                case R.id.home1:
                    fragment = new HomeFragment();
                    break;
                case R.id.wallet1:
                    fragment = new WalletFragment();
                    break;
                case R.id.booking1:
                    fragment = new BookingFragment();
                    break;
                case R.id.account1:
                    fragment = new AccountFragment();
                    break;
                case R.id.notification1:
                    fragment = new NotificaronsFragment();
                    break;

            }
            getSupportFragmentManager().beginTransaction().replace(R.id.framlayout,fragment).commit();
            return true ;
        }
    };
}