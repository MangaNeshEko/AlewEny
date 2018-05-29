package com.aleweny.prouction.View;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.aleweny.prouction.R;

public class ShowData extends AppCompatActivity {

    TextView text1;
    TextView text2;
    TextView text3;
    TextView text4;
    TextView text5;
    ScrollView scrollViewLay;

    SharedPreferences sharedPreferences;
    boolean backBoolean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_data);
        //This class is what displays the developer

        scrollViewLay = findViewById(R.id.showdeve);

        text1 = findViewById(R.id.text0);
        text2 = findViewById(R.id.text1);
        text3 = findViewById(R.id.text2);
        text4 = findViewById(R.id.text3);
        text5 = findViewById(R.id.text4);


        sharedPreferences = getSharedPreferences("Settings",0);

        backBoolean = sharedPreferences.getBoolean("switchOne",true);

        if(backBoolean){
            scrollViewLay.setBackgroundColor(getResources().getColor(R.color.blackBack));

        }else{
            scrollViewLay.setBackgroundColor(getResources().getColor(R.color.backgroundcolor));
        }


    }

}
