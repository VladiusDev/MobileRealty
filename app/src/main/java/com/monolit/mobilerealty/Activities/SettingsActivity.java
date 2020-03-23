package com.monolit.mobilerealty.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.monolit.mobilerealty.AlertDialog.DialogFactory;
import com.monolit.mobilerealty.Interfaces.Constants;
import com.monolit.mobilerealty.R;

public class SettingsActivity extends AppCompatActivity {

    private TextView txt_username;
    private TextView txt_department;
    private TextView txt_phoneNumber;
    private String   fullname;
    private String   department;
    private String   phoneNumber;
    private SharedPreferences preferences;
    private Button btn_exit;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        btn_exit = findViewById(R.id.btn_exit_task);
        txt_department = findViewById(R.id.txt_department);
        txt_username = findViewById(R.id.txt_username);
        txt_phoneNumber = findViewById(R.id.txt_phoneNumber);

        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (preferences.contains(Constants.PREFERENCE_PASSWORD) ){
                    try {
                        // Clear password and save old user
                        preferences.edit().putString(Constants.PREFERENCE_PASSWORD, "").apply();
                        preferences.edit().putString(Constants.PREFERENCE_OLD_USER, fullname).apply();

                        // Show login screen
                        finish();

                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } catch (Exception e) {
                        DialogFactory.showAlertDialog(SettingsActivity.this, e.toString(), getString(R.string.error));
                    }
                }
            }
        });

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(getString(R.string.user_profile));

        fullname    = "";
        department  = "";
        phoneNumber = "";

        if (preferences.contains(Constants.PREFERENCE_FULL_NAME)){
            fullname = preferences.getString(Constants.PREFERENCE_FULL_NAME, "");
        }

        if (preferences.contains(Constants.PREFERENCE_DEPARTMENT)){
            department = preferences.getString(Constants.PREFERENCE_DEPARTMENT, "");
        }

        if (preferences.contains(Constants.PREFERENCE_PHONE_NUMBER)){
            phoneNumber = preferences.getString(Constants.PREFERENCE_PHONE_NUMBER, "");
        }

        txt_username.setText(fullname);
        txt_department.setText(department);
        txt_phoneNumber.setText(phoneNumber);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();

                return true;
            default:

                return false;
        }
    }
}
