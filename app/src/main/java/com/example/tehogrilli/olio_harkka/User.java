package com.example.tehogrilli.olio_harkka;

import android.text.Editable;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
    private String firstName;
    private String lastName;
    private String eMail;
    private String phoneNumber;
    private ArrayList<Account> accountList = new ArrayList<Account>();
    private useAccount useAccount;
    private saveAccount saveAccount;

    public User(){}

    public User(String firstName, String lastName, String eMail, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.eMail = eMail;
        this.phoneNumber = phoneNumber;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName){
        this.lastName = lastName;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String geteMail() {
        return eMail;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void addUseAccount(String account, boolean canPay, int balance){
        this.useAccount = new useAccount(account, canPay, balance);
        addAccountToList(this.useAccount);
    }

    public void addSaveAccount(String account, boolean canPay, int balance){
        this.saveAccount = new saveAccount(account, canPay, balance);
        addAccountToList(this.saveAccount);
    }

    public void addAccountToList(Account account){
        accountList.add(account);
    }

    public ArrayList<Account> getAccountList(){
        return this.accountList;
    }

    public Account getSingleAccount(int position){
        return this.accountList.get(position);
    }

    public void setSingleAccount(Account account, int position){
        this.accountList.set(position, account);
    }

    public Card getCardWithNumber(String cardNumber){
        Account tempAccount;
        for (int i = 0; i < this.accountList.size(); i++){
            tempAccount = accountList.get(i);
            for (int j = 0; j < tempAccount.getCardList().size(); j++){
                if (tempAccount.getCardList().get(j).getCardNumber().equals(cardNumber)){
                    return tempAccount.getCardList().get(j);
                }
            }
        }
        return null;
    }




}
