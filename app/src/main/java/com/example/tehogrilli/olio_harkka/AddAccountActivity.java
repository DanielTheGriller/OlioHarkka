package com.example.tehogrilli.olio_harkka;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

public class AddAccountActivity extends AppCompatActivity {
    private RadioGroup accountTypeGroup;
    private RadioButton accountTypeButton;
    private EditText accountNumberField;
    private EditText balanceField;
    private Switch canPaySwitch;
    User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);
        // Get intent from main view
        Intent i = getIntent();
        // Get user object
        user = (User)i.getSerializableExtra("obj");
        accountTypeGroup = (RadioGroup) findViewById(R.id.account_type_group);
        accountNumberField = (EditText) findViewById(R.id.account_number_field);
        balanceField = (EditText) findViewById(R.id.balance_field);
        canPaySwitch = (Switch) findViewById(R.id.can_pay_switch);
    }

    // Create a new account when 'Create' is clicked
    public void btnClick(View v){
        int selectedId = accountTypeGroup.getCheckedRadioButtonId();
        boolean canPay;
        // Return if account type not selected
        if (selectedId == 0){
            Toast.makeText(this, "Account type not selected", Toast.LENGTH_SHORT).show();
            return;
        }
        String accountNumber = accountNumberField.getText().toString();
        String tempBalance = balanceField.getText().toString();
        int balance = Integer.parseInt(tempBalance);
        if (canPaySwitch.isChecked()){
            canPay = true;
        } else {
            canPay = false;
        }

        accountTypeButton = (RadioButton) findViewById(selectedId);

        if (selectedId == R.id.useRadio){
            user.addUseAccount(accountNumber, canPay, balance);
        }
        else if (selectedId == R.id.saveRadio){
            user.addSaveAccount(accountNumber, canPay, balance);
        }

        Intent i = new Intent();
        i.putExtra("obj", user);
        setResult(RESULT_OK, i);
        finish();
    }
}
