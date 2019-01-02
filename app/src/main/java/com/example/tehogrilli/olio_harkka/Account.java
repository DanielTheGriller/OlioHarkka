package com.example.tehogrilli.olio_harkka;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class Account implements Serializable {
    protected String account;
    protected boolean canPay;
    protected int balance;
    protected int paymentFee;
    protected int accType;
    protected ArrayList<Card> cardList = new ArrayList<Card>();
    protected ArrayList<Transaction> transactionList = new ArrayList<Transaction>();
    protected Card card;


    // SET FUNCTIONS //

    public void setAccountNumber(String account){
        this.account = account;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public void setCanPay(boolean canPay){
        this.canPay = canPay;
    }

    public void setCardList(ArrayList<Card> cardList){
        this.cardList = cardList;
    }

    // GET FUNCTIONS //

    public String getAccountNumber() {
        return account;
    }

    public int getPaymentFee() { return paymentFee; }

    public int getBalance() { return balance; }

    public boolean isCanPay() { return canPay; }

    public Card getCard(int position){
        return this.cardList.get(position);
    }

    public ArrayList<Card> getCardList(){
        return this.cardList;
    }

    public int getCardListLength(){ return this.cardList.size(); }

    public ArrayList<Transaction> getTransactionList() { return transactionList; }

    // OTHER FUNCTIONS //

    public void addCardToList(Card card, int position){
        if (position < cardList.size()){
            this.cardList.set(position, card);
        } else {
            this.cardList.add(card);
        }

    }

    public void addTransactionToList(Transaction transaction){
        transactionList.add(transaction);
    }
}

// Sub class accounts
// useAccount = every day checking account
// saveAccount =  savings account

class useAccount extends Account {
    public useAccount(String account, boolean canPay, int balance){
        this.account = account;
        this.canPay = canPay;
        this.balance = balance;
        this.paymentFee = 0;
        this.accType = 0;
    }
}

class saveAccount extends Account {
    public saveAccount(String account, boolean canPay, int balance){
        this.account = account;
        this.canPay = canPay;
        this.balance = balance;
        this.paymentFee = 10;
        this.accType = 1;
    }
}