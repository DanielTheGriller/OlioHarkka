package com.example.tehogrilli.olio_harkka;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import java.util.ArrayList;

public class NewTransactionActivity extends AppCompatActivity {
    User user;
    ArrayList<Account> accountList;
    RadioGroup typeRadio;
    RadioButton internalBtn;
    RadioButton externalBtn;
    RadioButton depositBtn;
    LinearLayout field1;
    LinearLayout field2;
    LinearLayout field3;
    EditText amountField;
    Spinner toSpinner;
    Spinner fromSpinner;
    EditText toTextField;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_transaction);

        //Define radio buttons and linearlayouts
        internalBtn = (RadioButton) findViewById(R.id.newtrans_internalRadio);
        externalBtn = (RadioButton) findViewById(R.id.newtrans_transferRadio);
        depositBtn = (RadioButton) findViewById(R.id.newtrans_depositRadio);
        field1 = (LinearLayout) findViewById(R.id.newtrans_field1);
        field2 = (LinearLayout) findViewById(R.id.newtrans_field2);
        field3 = (LinearLayout) findViewById(R.id.newtrans_field3);

        // Define activity input fields
        typeRadio = (RadioGroup) findViewById(R.id.newtrans_radioGroup);
        amountField = (EditText) findViewById(R.id.newtrans_amountField);
        toSpinner = (Spinner) findViewById(R.id.newtrans_toSpinner);
        fromSpinner = (Spinner) findViewById(R.id.newtrans_fromSpinner);
        toTextField = (EditText) findViewById(R.id.newtrans_toTextField);

        // Get intent and user
        Intent i = getIntent();
        user = (User) i.getSerializableExtra("obj");
        accountList = user.getAccountList();

        // Read user's account numbers to array for later use
        if (accountList.isEmpty() == false) {
            String spinnerArray[] = new String[accountList.size()];
            for (int j = 0; j < accountList.size(); j++) {
                spinnerArray[j] = accountList.get(j).getAccountNumber();
            }


            // Define adapter for spinners
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerArray);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // Set the adapter to different spinners
            toSpinner.setAdapter(adapter);
            fromSpinner.setAdapter(adapter);

            // Add notifier to adapter
            adapter.notifyDataSetChanged();

        }


    }

    // Function to hide/show fields depending on radio button selected
    public void onRadioButtonClick(View v){
        if (internalBtn.isChecked()){
            field1.setVisibility(View.VISIBLE);
            field2.setVisibility(View.VISIBLE);
            field3.setVisibility(View.GONE);
        }
        else if (externalBtn.isChecked()){
            field1.setVisibility(View.GONE);
            field2.setVisibility(View.VISIBLE);
            field3.setVisibility(View.VISIBLE);
        }
        else if (depositBtn.isChecked()){
            field1.setVisibility(View.VISIBLE);
            field2.setVisibility(View.GONE);
            field3.setVisibility(View.GONE);
        }
    }

    // Function to handle transaction after Confirm button is clicked
    public void onConfirmButtonClick(View v){
        // Read amount from field to variable
        int amount = Integer.parseInt(amountField.getText().toString());

        // Set Strings ready for reading
        String toAccount = "";
        String fromAccount = "";
        String transactionType = "";

        // Different read methods for different transaction types
        if (internalBtn.isChecked()){
            transactionType = "Internal transfer";
            toAccount = toSpinner.getSelectedItem().toString();
            fromAccount = fromSpinner.getSelectedItem().toString();
        }
        else if (externalBtn.isChecked()){
            transactionType = "External transfer";
            toAccount = toTextField.getText().toString();
            fromAccount = fromSpinner.getSelectedItem().toString();
        }
        else if (depositBtn.isChecked()){
            transactionType = "Deposit";
            toAccount = toSpinner.getSelectedItem().toString();
        }
        else {
            // If no radio button checked, just finish this activity
            finish();
        }

        // Create new Transaction instance from values read
        Transaction transaction = new Transaction(transactionType, fromAccount, toAccount, amount);

        // Create intent and put transaction to it as extra
        Intent i = new Intent();
        i.putExtra("trans", transaction);

        // Set ok result and finish
        setResult(RESULT_OK, i);
        finish();
    }
}
