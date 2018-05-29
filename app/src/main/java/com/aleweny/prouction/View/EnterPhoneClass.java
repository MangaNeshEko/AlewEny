package com.aleweny.prouction.View;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

/**
 * Created by littELGOsht on 4/9/2018.
 */
//Unregister the receiver of this class
public class EnterPhoneClass extends AppCompatActivity implements View.OnClickListener{

    Button backButton;
    EditText recoveryPhoneNumber;
    EditText securityCode;
    Button saveButton;
    String recoveryPhoneNumber_To_Be_saved;
    String securityCode_To_Be_saved;
    BroadcastReceiver broadcastReceiver;
    Controller controller;

    LinearLayout linearLayoutEnterPhone;
    TextView texV;
    SharedPreferences sharedPreferences;
    boolean backBoolean;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enterphonelayout);

        recoveryPhoneNumber = findViewById(R.id.recovery_phone_num);
        securityCode = findViewById(R.id.idKeyword);
        linearLayoutEnterPhone = findViewById(R.id.enterPhoneLayout);
        texV = findViewById(R.id.explainOne);

        sharedPreferences = getSharedPreferences("Settings",0);
        backBoolean = sharedPreferences.getBoolean("switchOne",true);

        if(backBoolean){
            linearLayoutEnterPhone.setBackgroundColor(getResources().getColor(R.color.blackBack));
            texV.setTextColor(getResources().getColor(R.color.whileBack));
            recoveryPhoneNumber.setTextColor(getResources().getColor(R.color.whileBack));
            securityCode.setTextColor(getResources().getColor(R.color.whileBack));

        }else{
            linearLayoutEnterPhone.setBackgroundColor(getResources().getColor(R.color.backgroundcolor));
        }






        controller = new Controller();

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                readSMS(intent);
            }

        };
        IntentFilter intentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(broadcastReceiver, intentFilter);
        //Getting the backButton
        backButton = findViewById(R.id.back_button);
//        phoneNumber = findViewById(R.id.idphoneNumber);

        saveButton = findViewById(R.id.idSaveButton);

        backButton.setOnClickListener(this);
        saveButton.setOnClickListener(this);
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
        boolean filled = controller.newMessageReceived(phoneNumberFromView, MessageBody, EnterPhoneClass.this);

        if(!filled){
            Toast.makeText(this, "Not read successfully", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onClick(View v){
        if(v == backButton){
            backButtonMethod();
        }else if(v == saveButton){
            saveButtonMethod(v);
        }
    }

    private void saveButtonMethod(View v) {

        //Getting The input values
//        phoneNumber_To_Be_saved = phoneNumber.getText().toString().trim();
        recoveryPhoneNumber_To_Be_saved = recoveryPhoneNumber.getText().toString().trim();
        securityCode_To_Be_saved = securityCode.getText().toString().trim();

        //Sending To the Controller
//        System.out.print(recoveryPhoneNumber_To_Be_saved +"\n"+securityCode_To_Be_saved);
        Controller controller = new Controller();
        controller.enterPhoneToDatabase(this.getApplicationContext(), recoveryPhoneNumber_To_Be_saved,securityCode_To_Be_saved, v);
    }

    public void backButtonMethod(){
        finish();
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
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
