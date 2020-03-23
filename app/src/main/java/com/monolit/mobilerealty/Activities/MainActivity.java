package com.monolit.mobilerealty.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.monolit.mobilerealty.Fragments.FragmentObjects;
import com.monolit.mobilerealty.Fragments.FragmentClients;
import com.monolit.mobilerealty.Fragments.FragmentReservation;
import com.monolit.mobilerealty.Fragments.FragmentTasks;
import com.monolit.mobilerealty.Interfaces.Constants;
import com.monolit.mobilerealty.R;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView mBottomNav;
    private FrameLayout mMainFrame;
    private FragmentTasks fragmentTasks;
    private FragmentClients fragmentClients;
    private FragmentObjects fragmentObjects;
    private FragmentReservation fragmentReservation;
    private ActionBar actionBar;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        actionBar = getSupportActionBar();

        fragmentTasks       = new FragmentTasks();
        fragmentClients     = new FragmentClients();
        fragmentObjects     = new FragmentObjects();
        fragmentReservation = new FragmentReservation();

        mMainFrame = findViewById(R.id.mainFrameLayout);
        mBottomNav = findViewById(R.id.bottomNavigationView);
        mBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.nav_tasks:
                        setFragment(fragmentTasks);

                        actionBar.setTitle(getString(R.string.tasks));

                        return true;

                    case R.id.nav_clients:
                        setFragment(fragmentClients);

                        actionBar.setTitle(getString(R.string.clients));

                        return true;

                    case R.id.nav_objects:
                        setFragment(fragmentObjects);

                        actionBar.setTitle(getString(R.string.realty_objects));

                        return true;
                    case R.id.nav_reservations:
                        setFragment(fragmentReservation);

                        actionBar.setTitle(getString(R.string.reservations));

                        return true;

                    default:

                        return false;
                }
            }
        });

        setDefaultFragment();
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainFrameLayout, fragment);
        fragmentTransaction.commit();
    }

    private void setDefaultFragment(){
        setFragment(fragmentClients);
        mBottomNav.setSelectedItemId(R.id.nav_clients);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_account:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        String oldUser = preferences.getString(Constants.PREFERENCE_OLD_USER, "");

        preferences.edit().putString(Constants.PREFERENCE_OLD_USER, oldUser).apply();
    }

}
