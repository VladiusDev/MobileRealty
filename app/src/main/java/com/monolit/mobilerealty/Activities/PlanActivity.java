package com.monolit.mobilerealty.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.monolit.mobilerealty.AlertDialog.DialogFactory;
import com.monolit.mobilerealty.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class PlanActivity extends AppCompatActivity {

    private final int PERMISSION_GRANTED_CODE = 202;

    private ImageView img_plan;
    private ImageView img_floor_plan;
    private Button btn_close;
    private Button btn_save;
    private TextView tv_objectName;
    private Bitmap[] bitmaps;
    private Activity activity;
    private LinearLayout progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        img_plan = findViewById(R.id.object_plan_img_plan);
        img_floor_plan = findViewById(R.id.object_plan_img_floor_plan);
        btn_close = findViewById(R.id.object_plan_btn_close);
        btn_save = findViewById(R.id.object_plan_btn_savePlan);
        tv_objectName = findViewById(R.id.object_plan_txt_name);
        progressBar = findViewById(R.id.object_card_progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        activity = this;

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        DialogFactory.showDialogAccessToGallery(activity);
                    } else {
                        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_GRANTED_CODE);
                    }
                }else{
                    saveBitmaps();
                }
            }
        });

        Intent intent = getIntent();

        String[] filesArray = intent.getStringArrayExtra("urlArray");
        String objectName = intent.getStringExtra("objectName");

        tv_objectName.setText(objectName);

        for (int i = 0, fileIndex = 1; i < filesArray.length; i++, fileIndex++) {
            String fileAddress = filesArray[i];
            if (i == 0) {
                picassoSetImage(fileAddress, img_plan);
            }else{
                picassoSetImage(fileAddress, img_floor_plan);
            }
        }
    }

    private void picassoSetImage(String fileAddress, ImageView imageView){
        try {
            Picasso.get().load(fileAddress).into(imageView);
        } catch (Exception e) {
            Picasso.get().load(fileAddress).resize(1280,720).into(imageView);
        }
    }

    private void saveBitmaps(){
        Bitmap bitmap = Bitmap.createBitmap(img_floor_plan.getMeasuredWidth(),
                img_floor_plan.getMeasuredHeight(),Bitmap.Config.ARGB_8888);

        for (Bitmap imageBitmap:bitmaps){
            File storageLoc = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            File file = new File(storageLoc, imageBitmap.toString() + ".jpg");

            try{
                FileOutputStream fos = new FileOutputStream(file);
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.close();

                scanFile(getApplicationContext(), Uri.fromFile(file));

                Toast.makeText(activity, activity.getString(R.string.gallery_save), Toast.LENGTH_LONG).show();
            } catch (FileNotFoundException e) {
                DialogFactory.showAlertDialog(getApplicationContext(), e.toString(), getString(R.string.error_save));
            } catch (IOException e) {
                DialogFactory.showAlertDialog(getApplicationContext(), e.toString(), getString(R.string.error_save));
            }
        }
    }

    private static void scanFile(Context context, Uri imageUri){
        Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        scanIntent.setData(imageUri);
        context.sendBroadcast(scanIntent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_GRANTED_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    saveBitmaps();
                } else {
                    DialogFactory.showAlertDialog(activity, activity.getString(R.string.save_error_permission_gallery), getString(R.string.error_save));
                }
                return;
            }
        }
    }
}
