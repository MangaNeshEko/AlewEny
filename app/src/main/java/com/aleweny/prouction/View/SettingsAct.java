package com.aleweny.prouction.View;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aleweny.prouction.MainActivity;
import com.aleweny.prouction.R;

public class SettingsAct extends AppCompatActivity  {
    SwitchCompat switchCompat1;
    boolean switchBoolean;
    SharedPreferences sharedPreferences;
    LinearLayout settingsLin;
    SharedPreferences.Editor editor;
    boolean switherBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        sharedPreferences = getSharedPreferences("Settings",0);
        switchBoolean = sharedPreferences.getBoolean("switchOne", true);
        switchCompat1 = findViewById(R.id.backGroundColour);
        settingsLin = findViewById(R.id.settingsLinear);
        editor = sharedPreferences.edit();

        switherBack = sharedPreferences.getBoolean("switchOne",true);

        if(switherBack){
            settingsLin.setBackgroundColor(getResources().getColor(R.color.blackBack));
            switchCompat1.setTextColor(getResources().getColor(R.color.whileBack));
        }else{
            settingsLin.setBackgroundColor(getResources().getColor(R.color.whileBack));
            switchCompat1.setTextColor(getResources().getColor(R.color.blackBack));
        }



        switchCompat1.setChecked(switchBoolean);

        switchCompat1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                switch (buttonView.getId()){
                    case R.id.backGroundColour:

                        switchBoolean = !switchBoolean;
                        switchCompat1.setChecked(switchBoolean);

                        editor.putBoolean("switchOne", switchBoolean);
                        editor.apply();

                        if(switchBoolean){
                            settingsLin.setBackgroundColor(getResources().getColor(R.color.blackBack));
                            switchCompat1.setTextColor(getResources().getColor(R.color.whileBack));
                        }else{
                            settingsLin.setBackgroundColor(getResources().getColor(R.color.whileBack));
                            switchCompat1.setTextColor(getResources().getColor(R.color.blackBack));
                        }
                }
            }
        });

    }

    @Override
    public void onBackPressed(){
        Intent mainIn = new Intent(SettingsAct.this, MainActivity.class);
        startActivity(mainIn);
    }



}
