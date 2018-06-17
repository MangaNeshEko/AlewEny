package com.aleweny.prouction;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aleweny.prouction.View.EnterPhoneClass;
import com.aleweny.prouction.View.FindPhone;
import com.aleweny.prouction.View.ListViewAdapter;
import com.aleweny.prouction.View.SettingsAct;
import com.aleweny.prouction.View.ShowSavedNumbers;
import com.aleweny.prouction.controller.Controller;

import java.util.ArrayList;

import static com.aleweny.prouction.R.layout.activity_main;
import static com.aleweny.prouction.R.layout.findphonelayout;

//TODO If there is a change in SIM Card Find out
public class MainActivity extends AppCompatActivity {
    String[] title = {"Add A Number", "Find Your phone", "Show Data", "Settings", "Developer"};
    String[] titleAm = {"ስልክ ቁጥር አስቀምጥ", "የጠፋውን ስልክ አግኝ", "የተቀመጠ ቁጥር አሳይ", "ቅንብር", "ገንቢ"};
    int[] images = {R.drawable.add, R.drawable.lightbulb, R.drawable.questionmark, R.drawable.settings, R.drawable.person};
    int[] imagesForDark = {R.drawable.addwhiteback, R.drawable.bulbwhilte, R.drawable.savedatawhite, R.drawable.settingswhite, R.drawable.personwhite};
    //    int[] background = {R.drawable.cerclebackgroundgreen, R.drawable.cerclebackgroundpink, R.drawable.cerclebackgroundpurple, R.drawable.cerclebackgroundbule};
    String[] desc = {
            "Add a phone number so that it can be save",
            "Find your phone by sending a message to your phone",
            "Here you can see what phone you have saved in your database",
            "Change Settings of your app",
            "Created By, Contact Information"
    };
    String[] descAmarhic = {
            "ማስቀመጥ እንዲችል የስልክ ቁጥር ያክሉ",
            "ወደ ስልክዎ መልዕክት በመላክ ስልክዎን ያግኙ",
            "እዚህ የውሂብ ጎታዎ ውስጥ ያስቀመጥዎትን ስልክ ቁጥር ማየት ይችላሉ",
            "የመተግበሪያዎ ቅንብሮችን ይቀይሩ",
            "ማን ሰራው"
    };
    private int PERMISSION_REQUEST_SMS_RECEIVEDIn = 110;
    ArrayList SMSList;
    ListView listView;
    public BroadcastReceiver broadcastReceiver;
    public BroadcastReceiver simCardChangeReciver;
    public BroadcastReceiver bootUpReciever;
    Controller controller;

    SharedPreferences sharedPreferences;
    boolean backBoolean;
    int languageInt;
    LinearLayout lin;

    //Reading SIM card information
    String IMEINumber;
    String SIMSerialNumber;
    String SIMSubscriber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_main);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        lin = findViewById(R.id.MainActivityBack);

        sharedPreferences = getSharedPreferences("Settings", 0);

        languageInt = sharedPreferences.getInt("langSettings", 0);

//        Toast.makeText(this, String.valueOf(languageInt), Toast.LENGTH_SHORT).show();


        backBoolean = sharedPreferences.getBoolean("switchOne", true);

        if (backBoolean) {
            lin.setBackgroundColor(getResources().getColor(R.color.blackBack));

        } else {
            lin.setBackgroundColor(getResources().getColor(R.color.backgroundcolor));
        }


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



        simCardChangeReciver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Toast.makeText(context, "Sim Card changed", Toast.LENGTH_LONG).show();
            }
        };

        bootUpReciever = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                readSIM();
            }
        };

        //Registering on BOOT
        IntentFilter intentFilterBoot = new IntentFilter("android.intent.action.BOOT_COMPLETED");
        registerReceiver(bootUpReciever, intentFilterBoot);



        //Registering the broadcast receiver
        IntentFilter intentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(broadcastReceiver, intentFilter);

        //Registering the broadcast receiver
        IntentFilter intentFilter1 = new IntentFilter("android.intent.action.SIM_STATE_CHANGED");
        registerReceiver(simCardChangeReciver, intentFilter1);


        //Find the list view
        listView = findViewById(R.id.idListItem);
        //Let create an instance of the ListView Adapter
        if (backBoolean) {
            //This is when its dark
            if (languageInt == 0) {
                //Passing the english language data
                ListViewAdapter listViewAdapter = new ListViewAdapter(this.getApplicationContext(), title, desc, imagesForDark);
                listView.setAdapter(listViewAdapter);
            } else if (languageInt == 1) {
                ListViewAdapter listViewAdapter = new ListViewAdapter(this.getApplicationContext(), titleAm, descAmarhic, imagesForDark);
                listView.setAdapter(listViewAdapter);
            } else if (languageInt == 2) {
                ListViewAdapter listViewAdapter = new ListViewAdapter(this.getApplicationContext(), title, desc, imagesForDark);
                listView.setAdapter(listViewAdapter);
            } else if (languageInt == 3) {
                ListViewAdapter listViewAdapter = new ListViewAdapter(this.getApplicationContext(), title, desc, imagesForDark);
                listView.setAdapter(listViewAdapter);
            }

        } else {
            if (languageInt == 0) {
                //Passing the english language data
                ListViewAdapter listViewAdapter = new ListViewAdapter(this.getApplicationContext(), title, desc, images);
                listView.setAdapter(listViewAdapter);
            } else if (languageInt == 1) {
                ListViewAdapter listViewAdapter = new ListViewAdapter(this.getApplicationContext(), titleAm, descAmarhic, images);
                listView.setAdapter(listViewAdapter);
            } else if (languageInt == 2) {
                ListViewAdapter listViewAdapter = new ListViewAdapter(this.getApplicationContext(), title, desc, imagesForDark);
                listView.setAdapter(listViewAdapter);
            } else if (languageInt == 3) {
                ListViewAdapter listViewAdapter = new ListViewAdapter(this.getApplicationContext(), title, desc, imagesForDark);
                listView.setAdapter(listViewAdapter);
            }
        }


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
//                                showDialogMethod(1);
                            Intent intSetting = new Intent(MainActivity.this, SettingsAct.class);
                            startActivity(intSetting);
                        } else if (position == 4) {
                            //Developer AlertDialog
                            showDialogMethod(0);
                        }
                    }
                }
        );
    }

    @SuppressLint("HardwareIds")
    public void readSIM() {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        assert telephonyManager != null;
        SIMSerialNumber = telephonyManager.getSimSerialNumber();
        SIMSubscriber = telephonyManager.getSubscriberId();
        IMEINumber = telephonyManager.getDeviceId();

//        System.out.println("The SIM Serial number is ");
//        System.out.println("The SIM ")
        Toast.makeText(this, "Serial Number added to Preference", Toast.LENGTH_SHORT).show();
        String storedSIMSerialNumber = PreferenceManager.getDefaultSharedPreferences(this).getString("serialNumber", null);
        String storedSIMSubscriber = PreferenceManager.getDefaultSharedPreferences(this).getString("subscriberNumber", null);
        String storedIMEInumber = PreferenceManager.getDefaultSharedPreferences(this).getString("imeiNumber", null);

        if(SIMSubscriber.equals(storedSIMSubscriber) || SIMSerialNumber.equals(storedSIMSerialNumber) || storedIMEInumber.equals(storedIMEInumber)){
            Toast.makeText(this, "Safa New", Toast.LENGTH_SHORT).show();
        }else {
            String message = "The New Sim Card "+ SIMSerialNumber + "\nThe IMEI Number "+IMEINumber + "\nThe Subscriber "+ SIMSubscriber;
            controller.bootSIMChange(this,message);
        }

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
            alertD.setView(R.layout.activity_show_data);
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
    protected void onResume() {
        super.onResume();
//        Toast.makeText(this, "I am back", Toast.LENGTH_SHORT).show();
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
