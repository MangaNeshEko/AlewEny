package com.aleweny.prouction.View;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aleweny.prouction.MainActivity;
import com.aleweny.prouction.R;

public class SettingsAct extends AppCompatActivity implements View.OnClickListener {
    SwitchCompat switchCompat1;
    boolean switchBoolean;
    SharedPreferences sharedPreferences;
    LinearLayout settingsLin;
    SharedPreferences.Editor editor;
    boolean switherBack;
    TextView languageOption, showSimInfoshowSimInfo;
    int langInt;

    //SIM card information from boot
    String bootSIMSerialNumber;
    String bootSIMSubscriber;
    String bootIMEInumber;

    //SIM card stored
    String SIMSerialNumber;
    String SIMSubscriber;
    String IMEINumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        sharedPreferences = getSharedPreferences("Settings", 0);
        langInt = sharedPreferences.getInt("langSettings", 0);


        switchBoolean = sharedPreferences.getBoolean("switchOne", true);
        switchCompat1 = findViewById(R.id.backGroundColour);
        settingsLin = findViewById(R.id.settingsLinear);
        languageOption = findViewById(R.id.languagePreference);
        languageOption.setOnClickListener(this);

        showSimInfoshowSimInfo = findViewById(R.id.showSimInfo);
        showSimInfoshowSimInfo.setOnClickListener(this);
        editor = sharedPreferences.edit();

        switherBack = sharedPreferences.getBoolean("switchOne", true);

        if (switherBack) {
            settingsLin.setBackgroundColor(getResources().getColor(R.color.blackBack));
            switchCompat1.setTextColor(getResources().getColor(R.color.whileBack));
        } else {
            settingsLin.setBackgroundColor(getResources().getColor(R.color.whileBack));
            switchCompat1.setTextColor(getResources().getColor(R.color.blackBack));
        }


        switchCompat1.setChecked(switchBoolean);

        switchCompat1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                switch (buttonView.getId()) {
                    case R.id.backGroundColour:

                        switchBoolean = !switchBoolean;
                        switchCompat1.setChecked(switchBoolean);

                        editor.putBoolean("switchOne", switchBoolean);
                        editor.apply();

                        if (switchBoolean) {
                            settingsLin.setBackgroundColor(getResources().getColor(R.color.blackBack));
                            switchCompat1.setTextColor(getResources().getColor(R.color.whileBack));
                        } else {
                            settingsLin.setBackgroundColor(getResources().getColor(R.color.whileBack));
                            switchCompat1.setTextColor(getResources().getColor(R.color.blackBack));
                        }
                }
            }
        });

    }


    @Override
    public void onClick(View view) {
        if (view == languageOption) {
            createCustomLan();
        } else if (view == showSimInfoshowSimInfo) {

            CreateCustom();
        }
    }

    public void CreateCustom() {
        //Setting


        //getting Presences
        bootSIMSerialNumber = PreferenceManager.getDefaultSharedPreferences(this).getString("serialNumber", null);
        bootSIMSubscriber = PreferenceManager.getDefaultSharedPreferences(this).getString("subscriberNumber", null);
        bootIMEInumber = PreferenceManager.getDefaultSharedPreferences(this).getString("imeiNumber", null);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Stored SIM card Information");
        builder.setMessage("Serial Number " + bootSIMSerialNumber +
                "\nSIM Subscriber Number " + bootSIMSubscriber
                + "\nIMEI Number " + bootIMEInumber
        );


        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.setNeutralButton("Add Sim Info", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveSimState();
            }
        });
        builder.setNegativeButton("Clear Info", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();


    }

    public void saveSimState() {

        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        assert telephonyManager != null;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        SIMSerialNumber = telephonyManager.getSimSerialNumber();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        SIMSubscriber = telephonyManager.getSubscriberId();
        IMEINumber = telephonyManager.getDeviceId();

        PreferenceManager.getDefaultSharedPreferences(this).edit().putString("serialNumber", SIMSerialNumber).apply();
        PreferenceManager.getDefaultSharedPreferences(this).edit().putString("subscriberNumber", SIMSubscriber).apply();
        PreferenceManager.getDefaultSharedPreferences(this).edit().putString("imeiNumber", IMEINumber).apply();
    }

    public void createCustomLan() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(R.layout.languagechoice);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        final TextView englishLan = alertDialog.findViewById(R.id.englishID);
        final TextView amahricLan = alertDialog.findViewById(R.id.amahric);
        final TextView afanOromoLan = alertDialog.findViewById(R.id.afanOromo);
        final TextView tigeregaLan = alertDialog.findViewById(R.id.tigerega);

        langInt = sharedPreferences.getInt("langSettings", 0);
        if (langInt == 0) {
            englishLan.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.checkclick, 0);
        } else if (langInt == 1) {
            amahricLan.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.checkclick, 0);
        } else if (langInt == 2) {
            afanOromoLan.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.checkclick, 0);
        } else if (langInt == 3) {
            tigeregaLan.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.checkclick, 0);
        }

        englishLan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SettingsAct.this, "English Selected", Toast.LENGTH_SHORT).show();
                langInt = 0;
                editor.putInt("langSettings", langInt);
                editor.apply();
                englishLan.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.checkclick, 0);
                amahricLan.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                tigeregaLan.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                afanOromoLan.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);


            }
        });


        amahricLan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(SettingsAct.this, "Amharic Selected", Toast.LENGTH_LONG).show();
                langInt = 1;
                editor.putInt("langSettings", langInt);
                editor.apply();
                amahricLan.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.checkclick, 0);
                tigeregaLan.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                afanOromoLan.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                englishLan.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

            }
        });
        afanOromoLan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SettingsAct.this, "Afan Oromo Selected", Toast.LENGTH_LONG).show();
                langInt = 2;
                editor.putInt("langSettings", langInt);
                editor.apply();
                afanOromoLan.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.checkclick, 0);
                amahricLan.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                englishLan.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                tigeregaLan.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);


            }
        });
        tigeregaLan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SettingsAct.this, "Tigerga Selected", Toast.LENGTH_LONG).show();
                langInt = 3;
                editor.putInt("langSettings", langInt);
                editor.apply();
                tigeregaLan.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.checkclick, 0);
                afanOromoLan.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                amahricLan.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                englishLan.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);


            }
        });


    }

    @Override
    public void onBackPressed() {
        Intent mainIn = new Intent(SettingsAct.this, MainActivity.class);
        startActivity(mainIn);
    }


}
