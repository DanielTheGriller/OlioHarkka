package com.example.tehogrilli.olio_harkka;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class TransactionListActivity extends AppCompatActivity {
    Account account;
    ArrayList<Transaction> transactionList;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_list);

        // Get intent and store data to variables
        Intent i = getIntent();
        account = (Account) i.getSerializableExtra("obj");
        transactionList = account.getTransactionList();
        textView = (TextView) findViewById(R.id.translist_textView);

        // Write transactionList content to textView
        Transaction transaction;
        for (int j = 0; j < transactionList.size(); j++){
            transaction = transactionList.get(j);

            // Write transaction type
            textView.append(transaction.getTransactionType() + "\n");

            // If type is deposit
            if (transaction.getTransactionType().equals("Deposit")){
                // No need to write account numbers for deposit
                textView.append(transaction.getTransactionAmount() + "\n\n");
            }
            else if (transaction.getTransactionType().equals("Withdraw") || transaction.getTransactionType().equals("Card payment")){
                textView.append("-" + transaction.getTransactionAmount() + "\n\n");
            }
            // If transferred from this account
            else if (transaction.getTransactionFromAccount().equals(account.getAccountNumber())){
                textView.append("To account: " + transaction.getTransactionToAccount() + "\n");
                // Write '-' before amount, because transferred from this account
                textView.append("-" + transaction.getTransactionAmount() + "\n\n");
            } else {
                // If transferred to this account
                textView.append("From account: " + transaction.getTransactionFromAccount() + "\n");
                textView.append(transaction.getTransactionAmount() + "\n\n");
            }
        }


    }
}
