package com.NowChallenge.minride;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Dùng layout chính mới

        // Ẩn Action Bar nếu cần
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        // Mặc định load DashboardFragment khi mở app
        if (savedInstanceState == null) {
            loadFragment(new DashboardFragment());
        }

        // Bắt sự kiện chọn item trên menu
        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                int itemId = item.getItemId();

                if (itemId == R.id.nav_home) {
                    selectedFragment = new DashboardFragment();
                } else if (itemId == R.id.nav_drivers) {
                    selectedFragment = new DriversFragment();
                } else if (itemId == R.id.nav_customers) {
                    // Chưa có Fragment, dùng tạm Dashboard hoặc tạo CustomersFragment sau
                    selectedFragment = new DashboardFragment(); 
                } else if (itemId == R.id.nav_rides) {
                     // Tương tự
                    selectedFragment = new DashboardFragment();
                } else if (itemId == R.id.nav_booking) {
                     // Tương tự
                    selectedFragment = new DashboardFragment();
                }

                if (selectedFragment != null) {
                    loadFragment(selectedFragment);
                    return true;
                }
                return false;
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}