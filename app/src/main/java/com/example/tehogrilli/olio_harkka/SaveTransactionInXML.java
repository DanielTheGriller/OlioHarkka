package com.example.tehogrilli.olio_harkka;

import android.content.Context;
import android.util.Xml;

import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.ArrayList;

public class SaveTransactionInXML {
    ArrayList<Transaction> transactionList = new ArrayList<Transaction>();
    Transaction transaction;
    Context context;

    // Constructor gets context from activity
    public SaveTransactionInXML(Context context){
        this.context = context;
    }

    // Function which gets transaction as input, transforms it and writes it to XML
    public void Save(Transaction transaction){
        transactionList.add(transaction);

        XmlSerializer serializer = Xml.newSerializer();
        StringWriter sw = new StringWriter();
        try {
            // Set starting properties
            serializer.setOutput(sw);
            serializer.startDocument("utf-8", true);
            serializer.startTag("", "Transactions");
            serializer.attribute("", "transactionAmount", String.valueOf(transactionList.size()));
            // Loop through transactions in transactionList and write actual content
            for (Transaction trans : transactionList) {

                serializer.startTag("", "transaction");

                serializer.startTag("", "type");
                serializer.text(trans.getTransactionType());
                serializer.endTag("", "type");

                serializer.startTag("", "amount");
                serializer.text(Integer.toString(trans.getTransactionAmount()));
                serializer.endTag("", "amount");

                serializer.startTag("", "fromAccount");
                serializer.text(trans.getTransactionFromAccount());
                serializer.endTag("", "fromAccount");

                serializer.startTag("", "toAccount");
                serializer.text(trans.getTransactionToAccount());
                serializer.endTag("", "toAccount");

                serializer.endTag("", "transaction");
            }
            // Set ending properties
            serializer.endTag("", "Transactions");
            serializer.endDocument();
            String outcome = sw.toString();

            try {
                String fileName = "transactions.xml";
                OutputStreamWriter output = new OutputStreamWriter(context.openFileOutput(fileName, Context.MODE_PRIVATE));
                output.write(outcome);
                output.close();
            } catch (Exception ex) {
                System.out.println(ex);
            }
        } catch (IOException ex){
            System.out.println(ex);
        }
    }
}
