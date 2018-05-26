package com.aleweny.prouction;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.telephony.SmsMessage;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.aleweny.prouction.View.EnterPhoneClass;
import com.aleweny.prouction.View.FindPhone;
import com.aleweny.prouction.View.ListViewAdapter;
import com.aleweny.prouction.View.ShowSavedNumbers;
import com.aleweny.prouction.controller.Controller;

import java.util.ArrayList;

import static com.aleweny.prouction.R.layout.activity_main;

//TODO If there is a change in SIM Card Find out
public class MainActivity extends AppCompatActivity {
    String[] title = {"Add A Number", "Find Your phone", "Show Data", "Settings", "Developer"};
    int[] images = {R.drawable.add, R.drawable.lightbulb, R.drawable.questionmark, R.drawable.settings, R.drawable.person};
//    int[] background = {R.drawable.cerclebackgroundgreen, R.drawable.cerclebackgroundpink, R.drawable.cerclebackgroundpurple, R.drawable.cerclebackgroundbule};
    String[] desc = {
            "Add a phone number so that it can be save",
            "Find your phone by sending a message to your phone",
            "Here you can see what phone you have saved in your database",
            "Change Settings of your app",
            "Created By, Contact Information"
    };
    private int PERMISSION_REQUEST_SMS_RECEIVEDIn = 110;
    ArrayList SMSList;
    ListView listView;
    public BroadcastReceiver broadcastReceiver;
    public BroadcastReceiver simCardChangeReciver;
    Controller controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_main);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);


        //Instantiating the Controller Class
        controller = new Controller();


        if (Build.VERSION.SDK_INT >= 23) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.READ_SMS,
                    Manifest.permission.RECEIVE_SMS,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.SEND_SMS,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, PERMISSION_REQUEST_SMS_RECEIVEDIn);
        }



        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                readSMS(intent);
            }

        };

        simCardChangeReciver = new BroadcastReceiver(){
            @Override
            public void onReceive (Context context, Intent intent){
                Toast.makeText(context, "Sim Card changed", Toast.LENGTH_LONG).show();
            }
        };


        //Registering the broadcast receiver
        IntentFilter intentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(broadcastReceiver, intentFilter);

        //Registering the broadcast receiver
        IntentFilter intentFilter1 = new IntentFilter("android.intent.action.SIM_STATE_CHANGED");
        registerReceiver(simCardChangeReciver, intentFilter1);


        //Find the list view
        listView = findViewById(R.id.idListItem);
        //Let create an instance of the ListView Adapter
        ListViewAdapter listViewAdapter = new ListViewAdapter(this.getApplicationContext(), title, desc, images);
        listView.setAdapter(listViewAdapter);

        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        Toast.makeText(MainActivity.this, "The item checked is:-" + position,Toast.LENGTH_LONG).show();
                        if (position == 0) {
//                            Toast.makeText(MainActivity.this, "Adding A Phone Number", Toast.LENGTH_SHORT).show();
//                            Snackbar.make(, "Hey There",Snackbar.LENGTH_LONG).show();
                            Intent intent = new Intent(MainActivity.this, EnterPhoneClass.class);
                            startActivity(intent);
                        } else if (position == 1) {
//                            Toast.makeText(MainActivity.this, "Finding Your phone ", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this, FindPhone.class);
                            startActivity(intent);
                        } else if (position == 2) {
                            //Toast.makeText(MainActivity.this, "Lets see how you can set up everything", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this, ShowSavedNumbers.class);
                            startActivity(intent);
                        } else if (position == 3) {
//                            Toast.makeText(MainActivity.this, "Haha It's done by the one and only ATM a.k.a littlEGhost", Toast.LENGTH_SHORT).show();?
                                showDialogMethod(1);
                        } else if (position == 4){
                            //Developer AlertDialog
                           showDialogMethod(0);
                        }
                    }
                }
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permission, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permission, grantResults);
    }


    public void readSMS(Intent intent) {
//        Toast.makeText(MainActivity.this, "New message", Toast.LENGTH_LONG).show();

        Bundle pudsBundle = intent.getExtras();
        assert pudsBundle != null;
        Object[] pdus = (Object[]) pudsBundle.get("pdus");
        assert pdus != null;
        SmsMessage messages =SmsMessage.createFromPdu((byte[]) pdus[0]);

        String  MessageBody = messages.getMessageBody();
        String phoneNumberFromView = messages.getOriginatingAddress();

//        System.out.println("The number:- "+MessageBody +"\nThe MessageBody"+phoneNumberFromView);
        //Toast.makeText(this, "View Class", Toast.LENGTH_LONG).show();

        //This phone number will be treated as the recovery phone number
        boolean filled = controller.newMessageReceived(phoneNumberFromView, MessageBody, MainActivity.this);

        if(!filled){
            Toast.makeText(this, "Not read successfully", Toast.LENGTH_SHORT).show();
        }

    }
    public void showDialogMethod(int id) {
        AlertDialog.Builder alertD = new AlertDialog.Builder(this);

        if(id==1){
            alertD.setView(R.layout.activity_settings);
        }else if(id == 2){
            alertD.setView(R.layout.show_saved_numbers);
        }
        else{
            alertD.setView(R.layout.content_show_data);
        }


        AlertDialog alertDialog = alertD.create();
        alertDialog.show();
    }


    @Override
    protected void onPause(){
        //Here i have implemented to register the reciever even when the app is on pause
        super.onPause();
        IntentFilter intentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(broadcastReceiver,intentFilter);
    }
    @Override
    protected void onStop(){
        super.onStop();
//        unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);

    }


}
