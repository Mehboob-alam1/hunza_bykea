package com.mehboob.hunzabykea.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mehboob.hunzabykea.R;
import com.mehboob.hunzabykea.databinding.ActivityDashboardBinding;
import com.mehboob.hunzabykea.ui.adapters.BannerAdapter;
import com.mehboob.hunzabykea.ui.fragments.AccountFragment;
import com.mehboob.hunzabykea.ui.fragments.BookingFragment;
import com.mehboob.hunzabykea.ui.fragments.HomeFragment;
import com.mehboob.hunzabykea.ui.fragments.NotificaronsFragment;
import com.mehboob.hunzabykea.ui.fragments.WalletFragment;
import com.mehboob.hunzabykea.ui.models.Banner;
import com.mehboob.hunzabykea.ui.models.UserProfileInfo;

import java.util.ArrayList;
import java.util.Objects;

public class DashboardActivity extends AppCompatActivity {


    private ActivityDashboardBinding binding;
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    private Fragment fragment;

    private TextView username ,userEmail;
    private String currentUser;
    private DatabaseReference userInfoRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        drawerLayout = findViewById(R.id.drawerLayout);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);

        View headerView = binding.navView.getHeaderView(0);
        username = headerView.findViewById(R.id.usernameheader);
        userEmail = headerView.findViewById(R.id.useremailHeader);

        userInfoRef = FirebaseDatabase.getInstance().getReference("HunzaBykea");
        currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        userInfoRef.child("UserInfo").child(currentUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    UserProfileInfo userInfo = snapshot.getValue(UserProfileInfo.class);
                    assert userInfo != null;
                    username.setText(userInfo.getName());
                    userEmail.setText(userInfo.getEmail());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DashboardActivity.this, error.getMessage()+" ", Toast.LENGTH_SHORT).show();
            }
        });



        getSupportFragmentManager().beginTransaction().replace(R.id.frameContainer, new HomeFragment()).commit();
        binding.navView.setCheckedItem(R.id.nav_home);

        binding.navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        fragment = new HomeFragment();
                        binding.drawerLayout.closeDrawer(GravityCompat.START);
                        binding.bottomNavigation.setSelectedItemId(R.id.bottom_home);

                        callFragment(fragment);
                        break;
                    case R.id.nav_booking:
                        fragment = new BookingFragment();
                        binding.drawerLayout.closeDrawer(GravityCompat.START);
                        binding.bottomNavigation.setSelectedItemId(R.id.bottom_booking);

                        callFragment(fragment);
                        break;
                    case R.id.nav_walllet:
                        fragment = new WalletFragment();
                        binding.drawerLayout.closeDrawer(GravityCompat.START);
                        binding.bottomNavigation.setSelectedItemId(R.id.bottom_wallet);

                        callFragment(fragment);
                        break;

                }
                return true;
            }

        });

        binding.bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.bottom_home:
                        binding.appBar.appbar.setVisibility(View.VISIBLE);
                        callFragment(new HomeFragment());
                        break;
                    case R.id.bottom_booking:
                        binding.appBar.appbar.setVisibility(View.GONE);
                        callFragment(new BookingFragment());
                        break;
                    case R.id.bottom_wallet:
                        binding.appBar.appbar.setVisibility(View.VISIBLE);
                        callFragment(new WalletFragment());
                        break;
                    case R.id.bottom_notification:
                        binding.appBar.appbar.setVisibility(View.VISIBLE);
                        callFragment(new NotificaronsFragment());
                        break;
                    case R.id.bottom_account:
                        binding.appBar.appbar.setVisibility(View.GONE);
                        callFragment(new AccountFragment());
                        break;
                }
                return true;
            }
        });



        binding.appBar.navmenu.setOnClickListener(v -> {
            if (drawerLayout.isOpen())
                drawerLayout.closeDrawer(GravityCompat.START);
            else
                drawerLayout.openDrawer(GravityCompat.START);
        });
    }


    private void callFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setCustomAnimations(android.R.anim.slide_out_right, android.R.anim.slide_out_right);
        transaction.replace(R.id.frameContainer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isOpen())
            drawerLayout.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }
}