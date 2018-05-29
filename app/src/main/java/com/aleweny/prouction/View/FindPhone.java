package com.aleweny.prouction.View;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aleweny.prouction.MainActivity;
import com.aleweny.prouction.R;
import com.aleweny.prouction.controller.Controller;

public class FindPhone extends AppCompatActivity implements View.OnClickListener {
    //TODO remember to add unregister()
    Button backButton;
    EditText phoneNumber_validator;
    EditText security;
    Button searchButton;
    Controller controller;
    BroadcastReceiver broadcastReceiver;

    LinearLayout linearLayoutFindPhone;

    SharedPreferences sharedPreferences;
    boolean backBoolean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.findphonelayout);

        linearLayoutFindPhone = findViewById(R.id.findphoneLayout);
        TextView txt = findViewById(R.id.explainTow);
        phoneNumber_validator = findViewById(R.id.phoneNumber_check);
        security = findViewById(R.id.keyword_check);

        sharedPreferences = getSharedPreferences("Settings",0);
        backBoolean = sharedPreferences.getBoolean("switchOne",true);

        if(backBoolean){
            linearLayoutFindPhone.setBackgroundColor(getResources().getColor(R.color.blackBack));
            txt.setTextColor(getResources().getColor(R.color.whileBack));
            phoneNumber_validator.setTextColor(getResources().getColor(R.color.whileBack));
            security.setTextColor(getResources().getColor(R.color.whileBack));


        }else{
            linearLayoutFindPhone.setBackgroundColor(getResources().getColor(R.color.backgroundcolor));
        }



        backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(this);



        searchButton = findViewById(R.id.searchPhone);
        searchButton.setOnClickListener(this);

        controller = new Controller();

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                readSMS(intent);
            }
        };

        IntentFilter intent = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(broadcastReceiver, intent);
    }

    @Override
    public void onClick(View v) {
        if(v==backButton){
            backButtonMethod();
        }else if(v== searchButton){
            searchLostPhone(v);
        }
    }

    private void backButtonMethod() {
        Intent intent = new Intent(this, MainActivity.class);
        finish();
        startActivity(intent);
    }

    private void searchLostPhone(View view){
//        Toast.makeText(this,"AM here", Toast.LENGTH_LONG).show();
        String phoneNumber_to_controller = phoneNumber_validator.getText().toString().trim();
        String secutity = security.getText().toString().trim();

        //Instantiate the Controller
        Controller controller = new Controller();
        controller.findPhoneController(this.getApplicationContext(), view, phoneNumber_to_controller, secutity);
    }
    public void readSMS(Intent intent) {
//        Toast.makeText(MainActivity.this, "New message", Toast.LENGTH_LONG).show();

        Bundle pudsBundle = intent.getExtras();
        Object[] pdus = (Object[]) pudsBundle.get("pdus");
        SmsMessage messages =SmsMessage.createFromPdu((byte[]) pdus[0]);

        String  MessageBody = messages.getMessageBody();
        String phoneNumberFromView = messages.getOriginatingAddress();
//        System.out.println("The number:- "+MessageBody +"\nThe MessageBody"+phoneNumberFromView);
        Toast.makeText(this, "View Class", Toast.LENGTH_LONG).show();

        //This phone number will be treated as the recovery phone number
        boolean filled = controller.newMessageReceived(phoneNumberFromView, MessageBody, FindPhone.this);

        if(!filled){
            Toast.makeText(this, "Not read successfully", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onStart(){
        super.onStart();
        IntentFilter intentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    public void onPause(){
        super.onPause();
        IntentFilter intentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);

    }


}
