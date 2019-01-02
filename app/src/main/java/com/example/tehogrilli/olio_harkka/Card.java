package com.example.tehogrilli.olio_harkka;

import java.io.Serializable;

public class Card implements Serializable {
    private String cardNumber;
    private int maxWithdraw;
    private int maxPayment;
    private String belongsToAccount;

    public Card(){
        this.cardNumber = "";
        this.maxWithdraw = 0;
        this.maxPayment = 0;
        this.belongsToAccount = "";
    }

    public Card(String number, int withdraw, int payment, String account){
        this.cardNumber = number;
        this.maxWithdraw = withdraw;
        this.maxPayment = payment;
        this.belongsToAccount = account;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public int getMaxWithdraw() {
        return maxWithdraw;
    }

    public int getMaxPayment() {
        return maxPayment;
    }

    public String getBelongsToAccount() { return this.belongsToAccount; }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public void setMaxWithdraw(int maxWithdraw) {
        this.maxWithdraw = maxWithdraw;
    }

    public void setMaxPayment(int maxPayment) {
        this.maxPayment = maxPayment;
    }

    public void setBelongsToAccount(String account) { this.belongsToAccount = account; }


}
