package com.example.tehogrilli.olio_harkka;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.io.Serializable;

public class CardDetailActivity extends AppCompatActivity {
    EditText numberField;
    EditText maxWithdrawField;
    EditText maxPaymentField;
    Card card;
    int position;
    String accountNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_detail);
        //Get intent
        Intent i = getIntent();
        card = (Card) i.getSerializableExtra("obj");
        position = (int) i.getSerializableExtra("pos");
        accountNumber = (String) i.getSerializableExtra("acc");
        // Get views by ID and set values
        numberField = findViewById(R.id.carddetail_number);
        numberField.setText(card.getCardNumber());
        maxWithdrawField = findViewById(R.id.carddetail_maxWithdraw);
        maxWithdrawField.setText(Integer.toString(card.getMaxWithdraw()));
        maxPaymentField = findViewById(R.id.carddetail_maxPayment);
        maxPaymentField.setText(Integer.toString(card.getMaxPayment()));
    }

    public void saveCard(View v){
        card.setCardNumber(numberField.getText().toString());
        card.setMaxWithdraw(Integer.parseInt(maxWithdrawField.getText().toString()));
        card.setMaxPayment(Integer.parseInt(maxPaymentField.getText().toString()));
        card.setBelongsToAccount(accountNumber);

        Intent i = new Intent();
        i.putExtra("obj", card);
        i.putExtra("pos", position);
        setResult(RESULT_OK, i);
        finish();
    }
}
