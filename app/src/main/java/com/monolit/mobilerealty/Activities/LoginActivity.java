package com.monolit.mobilerealty.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.VideoView;

import com.monolit.mobilerealty.DataBase.RealtyDataBase;
import com.monolit.mobilerealty.AlertDialog.DialogFactory;
import com.monolit.mobilerealty.Interfaces.Constants;
import com.monolit.mobilerealty.JsonUtils.JsonParser1C;
import com.monolit.mobilerealty.R;
import com.monolit.mobilerealty.WebServiceUtils.WebService1C;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;

public class LoginActivity extends AppCompatActivity {

    private CircularProgressButton btnAuth;
    private ConstraintLayout layout;
    private EditText edt_login;
    private EditText edt_password;
    private SharedPreferences preferences;
    private CheckBox cb_savePassword;
    private String savedLogin;
    private String savedPassword;
    private Boolean savePassword;
    private TextView txt_forgot_pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnAuth      = findViewById(R.id.btn_auth);
        layout       = findViewById(R.id.const_layout);
        edt_login    = findViewById(R.id.edt_login);
        edt_password = findViewById(R.id.edt_password);
        cb_savePassword = findViewById(R.id.cb_savePassword);
        txt_forgot_pass = findViewById(R.id.login_screen_forgot_password);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        savedLogin = "";
        savedPassword = "";
        savePassword  = false;

        if (preferences.contains(Constants.PREFERENCE_SAVE_PASSWORD)) {
            savePassword = preferences.getBoolean(Constants.PREFERENCE_SAVE_PASSWORD, false);
        }

        if (preferences.contains(Constants.PREFERENCE_LOGIN) || preferences.contains(Constants.PREFERENCE_PASSWORD)) {
            savedLogin = preferences.getString(Constants.PREFERENCE_LOGIN, "");
            savedPassword = preferences.getString(Constants.PREFERENCE_PASSWORD, "");
        }

        edt_login.setText(savedLogin);
        cb_savePassword.setChecked(savePassword);

        if (savePassword==true) {
            if (!savedLogin.isEmpty() && !savedPassword.isEmpty()) {
                new CheckAuthorizationTask().execute();
            }
        }

        txt_forgot_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFactory.showAlertDialog(LoginActivity.this, getString(R.string.login_screen_forgot_password_hint), getString(R.string.login_screen_forgot_password_title));
            }
        });

        btnAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String login = edt_login.getText().toString();
                String password = edt_password.getText().toString();
                Boolean login_filled = true;

                if (login.isEmpty() && !password.isEmpty()){
                    login_filled = false;

                    DialogFactory.showAlertDialog(LoginActivity.this, getString(R.string.auth_error_login), getString(R.string.check_error_authorization));
                }

                if (!login.isEmpty() && password.isEmpty()){
                    login_filled = false;

                    DialogFactory.showAlertDialog(LoginActivity.this, getString(R.string.auth_error_password), getString(R.string.check_error_authorization));
                }

                if (login.isEmpty() && password.isEmpty()){
                    login_filled = false;

                    DialogFactory.showAlertDialog(LoginActivity.this, getString(R.string.auth_error_login_password), getString(R.string.check_error_authorization));
                }

                if (login_filled==true) {
                    preferences.edit().putString(Constants.PREFERENCE_LOGIN, login).apply();
                    preferences.edit().putString(Constants.PREFERENCE_PASSWORD, password).apply();
                    preferences.edit().putBoolean(Constants.PREFERENCE_SAVE_PASSWORD, cb_savePassword.isChecked()).apply();

                    btnAuth.startAnimation();

                    // Set focusable
                    edt_login.setFocusable(true);
                    edt_password.setFocusable(true);

                    // Start authorization task
                    AuthorizationTask authTask = new AuthorizationTask();
                    authTask.execute();
                }
            }
        });

        btnAuth.setDrawableBackground(getDrawable(R.drawable.button_rounded));

        // Disable actionbar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.hide();
        }

        //  Start background video
        startBackroundVideo();
    }

    @Override
    protected void onResume() {
        btnAuth.setDrawableBackground(getDrawable(R.drawable.button_rounded));

        startBackroundVideo();

        super.onResume();
    }

    public void startBackroundVideo(){
        // Background video
        VideoView bg_video = findViewById(R.id.vw_bg);
        Uri bg_uri         = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.monolit_bg);

        bg_video.setVideoURI(bg_uri);
        bg_video.start();
        bg_video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setLooping(true);
            }
        });
    }

    class AuthorizationTask extends AsyncTask<Void, Void, String> implements Constants{
        private Boolean checkAuthorization;

        @Override
        protected String doInBackground(Void... voids) {
           return WebService1C.sendRequest(SERVER_SOAP_METHOD_GET_USER_INFO, getApplicationContext());
        }

        @Override
        protected void onPostExecute(String result) {
            // Set focusable and visibility
            edt_login.setFocusable(true);
            edt_password.setFocusable(true);

            // Check result
            checkAuthorization = JsonParser1C.getJsonResult(result);

            // Completing the authorization task
            if (checkAuthorization) {
                JSONObject jsonObject = null;

                try {
                    jsonObject = new JSONObject(result);
                    String data = jsonObject.getString("data");
                    HashMap<String, String> userInfo = JsonParser1C.getUserInfo(data);

                    if (userInfo.size() > 0) {
                        // Save user info
                        String fullName = userInfo.get("fullName");
                        String department = userInfo.get("department");
                        String phoneNumber = userInfo.get("phoneNumber");

                        preferences.edit().putString("fullName", fullName).apply();
                        preferences.edit().putString("department", department).apply();
                        preferences.edit().putString("phoneNumber", phoneNumber).apply();

                        // Clean the database if the user changes
                        if (preferences.contains(Constants.PREFERENCE_OLD_USER)){
                            String oldUser = preferences.getString(Constants.PREFERENCE_OLD_USER, "");
                            if (!oldUser.isEmpty()){
                                if (!fullName.equals(oldUser)){
                                    RealtyDataBase.DataBaseHelper dataBaseHelper = new RealtyDataBase.DataBaseHelper(getApplicationContext());
                                    dataBaseHelper.clearDB();
                                }
                            }
                        }

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);

                        finish();
                    }else{
                        btnAuth.revertAnimation();
                        btnAuth.setDrawableBackground(getDrawable(R.drawable.button_rounded));
                    }
                } catch (JSONException e) {
                    DialogFactory.showAlertDialog(LoginActivity.this, e.toString());
                }
          }else{
              btnAuth.revertAnimation();
              btnAuth.setDrawableBackground(getDrawable(R.drawable.button_rounded));

              DialogFactory.showAlertDialog(LoginActivity.this, result);
           }
        }
    }

    class CheckAuthorizationTask extends AsyncTask<Void, Void, Boolean> implements Constants{

        @Override
        protected void onPostExecute(Boolean authResult) {
            if (authResult == true) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);

                finish();
            }else{
                preferences.edit().putString(Constants.PREFERENCE_PASSWORD, "").apply();
                edt_password.setText("");
            }
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            String result = WebService1C.sendRequest(SERVER_SOAP_METHOD_GET_USER_INFO, getApplicationContext());

            Boolean checkAuthorization;

            try {
                checkAuthorization = JsonParser1C.getJsonResult(result);

                return checkAuthorization;
            } catch (Exception e) {
                return false;
            }

        }
    }

}
