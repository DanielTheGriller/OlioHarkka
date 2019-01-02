package com.example.tehogrilli.olio_harkka;

import java.io.Serializable;
import java.util.ArrayList;

public class Transaction implements Serializable {
    private String transactionType;
    private String fromAccount;
    private String toAccount;
    private int amount;

    public Transaction(String transactionType, String fromAccount, String toAccount, int amount){
        this.transactionType = transactionType;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public int getTransactionAmount() {
        return amount;
    }

    public String getTransactionFromAccount() {
        return fromAccount;
    }

    public String getTransactionToAccount() {
        return toAccount;
    }
}
