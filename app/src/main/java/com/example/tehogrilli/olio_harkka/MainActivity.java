package com.example.tehogrilli.olio_harkka;

import android.annotation.SuppressLint;
import android.app.LauncherActivity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    User user;
    Bank bank;
    TextView nameField;
    ListView accountList;
    ArrayList<String> array = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    SaveTransactionInXML save;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bank = new Bank();
        user = new User();
        // Set header
        nameField = (TextView) findViewById(R.id.userName);
        nameField.setText("Add name from 'User Settings'");
        // Set List of accounts
        accountList = (ListView) findViewById(R.id.main_listAccounts);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, array);
        accountList.setAdapter(adapter);

        // Set onClickListener to Accounts ListView
        accountList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Account account = user.getSingleAccount(position);
                Intent i = new Intent(view.getContext(), AccountDetailActivity.class);
                i.putExtra("obj", account);
                i.putExtra("pos", position);
                startActivityForResult(i,3);
            }
        });

        // Set instance of SaveTransactionInXml for saving transactions
        Context context = MainActivity.this;
        save = new SaveTransactionInXML(context);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    // Get result from activities
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Return if no resultCode so the app won't crash
        if(resultCode == RESULT_CANCELED){
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);

        // requestCode 1 means UserDetailActivity
        if(requestCode==1){
            //Get changes to temporary user and move them to the real one
            User tempUser = (User) data.getSerializableExtra("obj");
            user.setFirstName(tempUser.getFirstName());
            user.setLastName(tempUser.getLastName());
            user.seteMail(tempUser.geteMail());
            user.setPhoneNumber(tempUser.getPhoneNumber());
            bank.addUser(user);

            //Update text if there is name added
            if (!user.getFirstName().equals("") || !user.getLastName().equals("")) {
                nameField.setText(user.getFirstName() + " " + user.getLastName());
            }
            Toast.makeText(getApplicationContext(), "Changes saved.", Toast.LENGTH_SHORT).show();



        }
        // requestCode 2 is from AccountAddActivity
        else if (requestCode == 2){
            // Get user and accountlist
            user = (User) data.getSerializableExtra("obj");
            ArrayList<Account> tempList = user.getAccountList();
            // Clear ListView array and adapter
            array.clear();
            adapter.clear();
            // Add new values to array to display on ListView
            for (int i = 0; i<tempList.size(); i++){
                array.add(tempList.get(i).account);
            }
            Toast.makeText(getApplicationContext(), "Account added", Toast.LENGTH_SHORT).show();
        }
        // requestCode 3 is from AccountDetailActivity
        else if (requestCode == 3){
            int position = (int) data.getSerializableExtra("pos");
            Account tempAccount = (Account) data.getSerializableExtra("obj");
            user.setSingleAccount(tempAccount, position);
            array.clear();
            adapter.clear();
            ArrayList<Account> tempList = user.getAccountList();
            for (int i = 0; i<tempList.size(); i++){
                array.add(tempList.get(i).account);
            }
        }
        // requestCode 4 is from NewTransactionActivity
        else if (requestCode == 4){
            int toCurrentAmount, fromCurrentAmount, toNewAmount, fromNewAmount, toIndex, fromIndex;
            String toAccount, fromAccount, transType;
            ArrayList<Account> accountList = user.getAccountList();

            // Get transaction object from activity
            Transaction transaction = (Transaction) data.getSerializableExtra("trans");
            transType = transaction.getTransactionType();

            if (transType.equals("Deposit")){
                // Get account information to variables
                toAccount = transaction.getTransactionToAccount();
                toIndex = findAccountIndex(toAccount, accountList);
                toCurrentAmount = accountList.get(toIndex).getBalance();

                // Calculate new balance and set it to account
                toNewAmount = toCurrentAmount + transaction.getTransactionAmount();
                accountList.get(toIndex).setBalance(toNewAmount);

                // Send transaction to account instance
                accountList.get(toIndex).addTransactionToList(transaction);

                // Display message and return
                Toast.makeText(getApplicationContext(), "Deposit made", Toast.LENGTH_SHORT).show();
                return;
            }
            else if (transType.equals("Internal transfer")){
                // Get 'from' account information to variables
                fromAccount = transaction.getTransactionFromAccount();
                fromIndex = findAccountIndex(fromAccount, accountList);
                fromCurrentAmount = accountList.get(fromIndex).getBalance();

                // Return if canPay is set to false
                if (accountList.get(fromIndex).isCanPay() == false){
                    Toast.makeText(getApplicationContext(), "Payments are not allowed on this account", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Return if transfer is more than account's balance
                if (accountList.get(fromIndex).getBalance() < transaction.getTransactionAmount()){
                    Toast.makeText(getApplicationContext(), "Not enough money to complete transaction", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Get 'to' account information to variables
                toAccount = transaction.getTransactionToAccount();
                toIndex = findAccountIndex(toAccount, accountList);
                toCurrentAmount = accountList.get(toIndex).getBalance();

                // Calculate new balances and set them to accounts
                fromNewAmount = fromCurrentAmount - transaction.getTransactionAmount() - accountList.get(fromIndex).getPaymentFee();
                toNewAmount = toCurrentAmount + transaction.getTransactionAmount();
                accountList.get(fromIndex).setBalance(fromNewAmount);
                accountList.get(toIndex).setBalance(toNewAmount);

                // Send transaction to account instances
                accountList.get(fromIndex).addTransactionToList(transaction);
                accountList.get(toIndex).addTransactionToList(transaction);

                // Display message and return
                Toast.makeText(getApplicationContext(), "Internal transfer made", Toast.LENGTH_SHORT).show();
                return;
            }
            else if (transType.equals("External transfer")){
                // Get 'from' account information to variables
                fromAccount = transaction.getTransactionFromAccount();
                fromIndex = findAccountIndex(fromAccount, accountList);
                fromCurrentAmount = accountList.get(fromIndex).getBalance();

                // Return if canPay is set to false
                if (accountList.get(fromIndex).isCanPay() == false){
                    Toast.makeText(getApplicationContext(), "Payments are not allowed from this account", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Return if transfer is more than account's balance
                if (accountList.get(fromIndex).getBalance() < transaction.getTransactionAmount()){
                    Toast.makeText(getApplicationContext(), "Not enough money to complete transaction", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Calculate new balance and set it
                fromNewAmount = fromCurrentAmount - transaction.getTransactionAmount() - accountList.get(fromIndex).getPaymentFee();
                accountList.get(fromIndex).setBalance(fromNewAmount);

                // Send transaction to account instance
                accountList.get(fromIndex).addTransactionToList(transaction);

                // Display message and return
                Toast.makeText(getApplicationContext(), "External transfer made", Toast.LENGTH_SHORT).show();
                return;
            }


        }
        else if (requestCode == 5){
            // Get values from activity
            String cardNumber = (String) data.getSerializableExtra("card");
            String actionType = (String) data.getSerializableExtra("action");
            int amount = (int) data.getSerializableExtra("amount");
            System.out.println("kortti:" + cardNumber);
            // Find card object with number
            Card card = user.getCardWithNumber(cardNumber);
            // Find account
            int accountIndex = findAccountIndex(card.getBelongsToAccount(), user.getAccountList());
            Account account = user.getAccountList().get(accountIndex);

            // Display message and return if not allowed to pay from this account
            if (account.isCanPay() == false){
                Toast.makeText(getApplicationContext(), "Payments are not allowed from this account", Toast.LENGTH_SHORT).show();
                return;
            }

            int currentBalance = account.getBalance();

            if (currentBalance >= amount) {
                if (actionType.equals("Withdraw")){

                    // Return if withdraw limit is smaller than amount
                    if (card.getMaxWithdraw() < amount){
                        Toast.makeText(getApplicationContext(), "Withdraw amount exeeds withdraw limit", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                else if (actionType.equals("Card payment")){
                    // Return if payment limit is smaller than amount
                    if (card.getMaxPayment() < amount){
                        Toast.makeText(getApplicationContext(), "Payment amount exeeds payment limit", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                int newBalance = currentBalance - amount;
                account.setBalance(newBalance);
                Transaction transaction = new Transaction(actionType, account.getAccountNumber(), "", amount);
                account.addTransactionToList(transaction);

                // Display message and return
                Toast.makeText(getApplicationContext(), "Card action made", Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }

    public int findAccountIndex(String account, ArrayList<Account> accountList){
        // Loop through accountList and compare input accountnumber
        for (int i = 0; i < accountList.size(); i++){
            if (accountList.get(i).getAccountNumber().equals(account)){
                //return index if match
                return i;
            }
        }
        // Return -1 if no match found
        return -1;
    }

    // Function to show/hide the add menu
    @SuppressLint("WrongConstant")
    public void showMenu(View v){
        View menu = findViewById(R.id.addMenu);
        // If menu is visible, hide it onclick
        if (menu.getVisibility() == 0){
            menu.setVisibility(View.INVISIBLE);
        } else {
            // Else, show it onclick
            menu.setVisibility(View.VISIBLE);
        }
    }

    public void openUserSettings(MenuItem item){
        Intent openNewActivity = new Intent(this, UserDetailActivity.class);
        openNewActivity.putExtra("obj", user);
        startActivityForResult(openNewActivity, 1);
    }

    // Function to open AddAccountActivity view for account creation
    public void openAccountCreation(View v){
        // Hide the menu after click
        showMenu(v);
        // Open AddAccountActivity view to add a new account.
        Intent openNewActivity = new Intent(this, AddAccountActivity.class);
        openNewActivity.putExtra("obj", user);
        startActivityForResult(openNewActivity, 2);
    }

    public void openNewTransaction(View v){
        // Hide the menu after click
        showMenu(v);
        Intent openNewActivity = new Intent(this, NewTransactionActivity.class);
        openNewActivity.putExtra("obj", user);
        startActivityForResult(openNewActivity, 4);
    }

    public void openNewCardAction(View v){
        // Hide the menu after click
        showMenu(v);
        Intent openNewActivity = new Intent(this, CardActionActivity.class);
        openNewActivity.putExtra("obj", user);
        startActivityForResult(openNewActivity, 5);
    }




}
