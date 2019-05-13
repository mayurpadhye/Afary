package com.cube9.afary.user.payment;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.cube9.afary.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PaymentActivity extends AppCompatActivity {
@BindView(R.id.btn_pay)
Button btn_pay;
    @BindView(R.id.btn_cancel)
    Button btn_cancel;
    AlertDialog.Builder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        ButterKnife.bind(this);
        builder = new AlertDialog.Builder(this);
    }
    @OnClick(R.id.btn_cancel)
    public void onCancel()
    {
        builder.setMessage(R.string.cancel_payment) .setTitle(R.string.dialog_title);

        //Setting message manually and performing action on button click
        builder.setMessage(R.string.cancel_payment)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();

                    }
                });
        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle(R.string.dialog_title);
        alert.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        builder.setMessage(R.string.cancel_payment) .setTitle(R.string.dialog_title);

        //Setting message manually and performing action on button click
        builder.setMessage(R.string.cancel_payment)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();

                    }
                });
        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle(R.string.dialog_title);
        alert.show();

    }
}
