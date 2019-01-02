package com.example.tehogrilli.olio_harkka;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class UserDetailActivity extends AppCompatActivity {
    EditText field1;
    EditText field2;
    EditText field3;
    EditText field4;
    User user;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        // Get intent from main view
        Intent i = getIntent();
        // Get user object
        user = (User)i.getSerializableExtra("obj");
        // Set text to fields from user object
        field1 = (EditText) findViewById(R.id.editText1);
        field1.setText(user.getFirstName());
        field2 = (EditText) findViewById(R.id.editText2);
        field2.setText(user.getLastName());
        field3 = (EditText) findViewById(R.id.editText3);
        field3.setText(user.geteMail());
        field4 = (EditText) findViewById(R.id.editText4);
        field4.setText(user.getPhoneNumber());
    }

    // Function to save new values on button click and then return to main view
    public void btnClick(View v){
        user.setFirstName(field1.getText().toString());
        user.setLastName(field2.getText().toString());
        user.seteMail(field3.getText().toString());
        user.setPhoneNumber(field4.getText().toString());

        Intent i = new Intent();
        i.putExtra("obj", user);
        setResult(RESULT_OK, i);
        finish();
    }

}
