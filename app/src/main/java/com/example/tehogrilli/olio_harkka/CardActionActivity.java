package com.example.tehogrilli.olio_harkka;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class CardActionActivity extends AppCompatActivity {
    User user;
    ArrayList<Account> accountList = new ArrayList<Account>();
    ArrayList<Card> cardList = new ArrayList<Card>();
    RadioGroup radioGroup;
    RadioButton withdrawRadio;
    RadioButton paymentRadio;
    Spinner cardSpinner;
    EditText amountField;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_action);

        // Define radio buttons
        radioGroup = (RadioGroup) findViewById(R.id.cardaction_radioGroup);
        withdrawRadio = (RadioButton) findViewById(R.id.cardaction_withdrawRadio);
        paymentRadio = (RadioButton) findViewById(R.id.cardaction_paymentRadio);

        // Define input fields
        cardSpinner = (Spinner) findViewById(R.id.cardaction_cardSpinner);
        amountField = (EditText) findViewById(R.id.cardaction_amountField);

        // Get intent and user
        Intent i = getIntent();
        user = (User) i.getSerializableExtra("obj");
        accountList = user.getAccountList();

        // This part creates content for the selection spinner
        if (accountList.isEmpty() == false) {
            ArrayList<Card> tempList = new ArrayList<Card>();
            // Loop through accountList and add all cards found to cardList
            for (int j = 0; j < accountList.size(); j++) {
                // Read cardlist from j-index account to tempList
                tempList = accountList.get(j).getCardList();
                // If tempList is not empty, loop it through and add all cards to cardList
                if (tempList.isEmpty() == false) {
                    for (int a = 0; a < tempList.size(); a++) {
                        cardList.add(tempList.get(a));
                    }
                }
            }
            if (cardList.isEmpty()) {
                // If no cards found, display text and exit activity
                Toast.makeText(getApplicationContext(), "No cards added", Toast.LENGTH_SHORT).show();
                finish();
            }

            // Loop through cardlist and set cards to array for the spinner
            String cardSpinnerArray[] = new String[cardList.size()];
            for (int j = 0; j < cardList.size(); j++) {
                cardSpinnerArray[j] = cardList.get(j).getCardNumber();
            }

            // Define adapter for spinners
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, cardSpinnerArray);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // Set the adapter to account spinner
            cardSpinner.setAdapter(adapter);

            // Add notifier to adapter
            adapter.notifyDataSetChanged();
        }
    }

    public void onUseButtonClick(View v){
        int amount = Integer.parseInt(amountField.getText().toString());
        String actionType = "";
        String cardNumber = cardSpinner.getSelectedItem().toString();
        if (withdrawRadio.isChecked()){
            actionType = "Withdraw";
        } else if (paymentRadio.isChecked()) {
            actionType = "Card payment";
        } else {
            // If no radio button checked, finish activity
            finish();
        }

        // New intent and set variables to it with putExtra
        Intent i = new Intent();
        i.putExtra("amount", amount);
        i.putExtra("action", actionType);
        i.putExtra("card", cardNumber);

        // Set result and finish activity
        setResult(RESULT_OK, i);
        finish();
    }
}

