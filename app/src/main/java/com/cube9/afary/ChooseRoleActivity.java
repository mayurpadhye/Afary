package com.cube9.afary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.cube9.afary.user.UserSignUpActivity;
import com.cube9.afary.vendor.view.VendorSignUpActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChooseRoleActivity extends AppCompatActivity {

    @BindView(R.id.btn_vendor)
    Button btn_vendor;

    @BindView(R.id.btn_user)
    Button btn_user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_role);
        ButterKnife.bind(this);

    }
    @OnClick(R.id.btn_user)
    public void  onUserSignUpClick(View v)
    {
        startActivity(new Intent(ChooseRoleActivity.this,UserSignUpActivity.class));
    }
    @OnClick(R.id.btn_vendor)
    public void  onVendorSignUpClick(View v)
    {
        startActivity(new Intent(ChooseRoleActivity.this,VendorSignUpActivity.class));
    }
}
