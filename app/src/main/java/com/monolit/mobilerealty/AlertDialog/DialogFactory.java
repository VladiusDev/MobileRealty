package com.monolit.mobilerealty.AlertDialog;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import com.monolit.mobilerealty.R;

public class DialogFactory {

    public static void showAlertDialog(Context context, String text){
        showDialog(context, text, null);
    }

    public static void showAlertDialog(Context context, String text, String title){
        showDialog(context, text, title);
    }

    public static void showDialogAccessToGallery(final Activity context){
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(context.getString(R.string.permission_error));
        alertDialog.setMessage(context.getString(R.string.gallery_permission));
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, context.getString(R.string.alertdialog_ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 202);
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, context.getString(R.string.alertdialog_permission_gained),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        alertDialog.show();
    }

    private static void showDialog(Context context, String text, String title){
        String mTitle = "";
        if (title != null) {
            mTitle = title;
        }else{
            mTitle = context.getString(R.string.check_error);
        }

        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(mTitle);
        alertDialog.setMessage(text);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, context.getString(R.string.alertdialog_ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

}
