package com.example.tehogrilli.olio_harkka;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class AccountDetailActivity extends AppCompatActivity {
    Account account;
    TextView accountField;
    TextView balanceField;
    TextView accountTypeField;
    Switch canPayField;
    TextView paymentFeeField;
    TextView cardField;
    ListView cardsList;
    ArrayList<String> array = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    ArrayList<Card> toSendList;
    LinearLayout toggleVisibility;
    int position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_detail);
        // Get intent from main view
        Intent i = getIntent();
        // Get account object
        account = (Account)i.getSerializableExtra("obj");
        // Get list item position
        position = (int) i.getSerializableExtra("pos");
        toSendList = account.getCardList();
        toggleVisibility = findViewById(R.id.setInvisible);
        accountField = findViewById(R.id.accdetail_accountField);
        accountField.setText(account.account);
        balanceField = findViewById(R.id.accdetail_balanceField);
        balanceField.setText(Integer.toString(account.balance));
        accountTypeField = findViewById(R.id.accdetail_accountTypeField);
        if (account.accType == 0){
            accountTypeField.setText("Checking Account");
            toggleVisibility.setVisibility(View.VISIBLE);
            for (int j = 0; j<toSendList.size(); j++){
                array.add(toSendList.get(j).getCardNumber());
            }
            cardsList = (ListView) findViewById(R.id.accdetail_listCards);
            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, array);
            cardsList.setAdapter(adapter);
            // Set onClickListener to ListView
            cardsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int cardPosition, long id) {
                    Card card = account.getCard(cardPosition);
                    Intent i = new Intent(view.getContext(), CardDetailActivity.class);
                    i.putExtra("obj", card);
                    i.putExtra("pos", cardPosition);
                    startActivityForResult(i,1);
                }
            });

        } else {
            accountTypeField.setText("Savings Account");
            toggleVisibility.setVisibility(View.GONE);

        }
        canPayField = findViewById(R.id.accdetail_canPaySwitch);
        if (account.canPay){
            canPayField.setChecked(true);
        } else {
            canPayField.setChecked(false);
        }
        paymentFeeField = findViewById(R.id.accdetail_paymentFeeField);
        paymentFeeField.setText(Integer.toString(account.paymentFee));




    }

    // Function to apply changes made to account on button click and return to main view
    public void applyChanges(View v){
        account.setAccountNumber(accountField.getText().toString());
        if (canPayField.isChecked()){
            account.setCanPay(true);
        } else {
            account.setCanPay(false);
        }
        account.setCardList(toSendList);
        // Set intent and return
        Intent i = new Intent();
        i.putExtra("obj", account);
        i.putExtra("pos", position);
        setResult(RESULT_OK, i);
        finish();
    }

    public void onAddCardClick(View v){
        Card card = new Card();
        card.setBelongsToAccount(account.getAccountNumber());
        Intent openNewActivity = new Intent(this, CardDetailActivity.class);
        openNewActivity.putExtra("obj", card);
        openNewActivity.putExtra("pos", account.getCardListLength());
        openNewActivity.putExtra("acc", account.getAccountNumber());
        startActivityForResult(openNewActivity, 1);
    }

    public void onTransactionButtonClick(View v){
        Intent openNewActivity = new Intent(this, TransactionListActivity.class);
        openNewActivity.putExtra("obj", account);
        startActivity(openNewActivity);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_CANCELED){
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1){
            Card tempCard = (Card) data.getSerializableExtra("obj");
            int position = (int) data.getSerializableExtra("pos");
            account.addCardToList(tempCard, position);
            array.clear();
            adapter.clear();
            toSendList = account.getCardList();

            for (int i = 0; i<toSendList.size(); i++){
                array.add(toSendList.get(i).getCardNumber());
            }

        }
    }
}
