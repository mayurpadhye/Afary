package com.cube9.afary.helperClass;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;


import com.cube9.afary.R;
import com.valdesekamdem.library.mdtoast.MDToast;

import java.io.ByteArrayOutputStream;

import dmax.dialog.SpotsDialog;


public class CustomUtils {

static    AlertDialog waiting_dialog;
    public static final String ENGLISH="1";
    public static final String ENGLISH_CODE="en";
    public static final String FRENCH="2";
    public static final String FRENCH_CODE="fr";
    public static final int IMAGE_LIMIT = 5 ;
    public static final int VIDEO_LIMIT = 2 ;

    public static void showToast(String msg, Context context,int type){

        MDToast mdToast= MDToast.makeText(context, msg, MDToast.LENGTH_SHORT, type);
        mdToast.setGravity(Gravity.CENTER,0,0);
        mdToast.show();
    }//showToastClose


    public static void ShowDialog(Context context)
    {

        waiting_dialog = new SpotsDialog.Builder()
                .setContext(context)
                .setMessage(R.string.loading)
                .setCancelable(false)
                .build();
        waiting_dialog.show();
  }

    public static void DismissDialog()
    {
        if (waiting_dialog.isShowing())
            waiting_dialog.dismiss();
    }



    public static boolean isNetworkAvailable(Context context) {
        if(context != null) {

            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netinfo = cm.getActiveNetworkInfo();

            if (netinfo != null && netinfo.isConnectedOrConnecting()) {
                NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

                return (mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting());
            } else
                return false;
        } else return false;
    }

    public static void hideKeyboard(View view, Context context) {
        InputMethodManager inputMethodManager =(InputMethodManager)context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    public static void showOKAlertDialog(Context context, String title, String message)
    {
        android.support.v7.app.AlertDialog.Builder dialogBuilder = new android.support.v7.app.AlertDialog.Builder(context,R.style.AppCompatAlertDialogStyle);
        dialogBuilder.setTitle(title);
        dialogBuilder.setCancelable(false);


        dialogBuilder.setMessage(message);
        dialogBuilder.setPositiveButton(context.getString(R.string.ok), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                dialog.dismiss();

            }
        });
        final android.support.v7.app.AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }
    public static void openAlertDialog(Context context, String messgae)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(messgae)
                .setCancelable(false)
                .setPositiveButton(context.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();

                    }
                });

        AlertDialog alert = builder.create();
        alert.setTitle(context.getResources().getString(R.string.success));
        alert.show();
        alert.setCancelable(false);
        alert.setCanceledOnTouchOutside(false);
    }//

    public static void showAlertDialog(Context context,String message)
    {
        android.support.v7.app.AlertDialog.Builder dialogBuilder = new android.support.v7.app.AlertDialog.Builder(context,R.style.AppCompatAlertDialogStyle);
        dialogBuilder.setMessage(message);
        dialogBuilder.setPositiveButton(context.getString(R.string.ok), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                dialog.dismiss();
            }
        });
        final android.support.v7.app.AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public static void showLog(String tag, String message){

        Log.e(tag,message);

    }
    public static String getRealPathFromURI(Context context,Uri uri) {
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }
}
