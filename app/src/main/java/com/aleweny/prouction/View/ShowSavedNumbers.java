package com.aleweny.prouction.View;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aleweny.prouction.R;
import com.aleweny.prouction.controller.Controller;
import com.aleweny.prouction.controller.ListViewAdapterShowData;

public class ShowSavedNumbers extends AppCompatActivity implements View.OnClickListener {
    Button btnShowData;
    Controller controller;
    TextView textView;
    ListViewAdapterShowData listViewAdapterShowData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_saved_numbers);

//        btnShowData = findViewById(R.id.shwo);
//        btnShowData.setOnClickListener(this);
        controller = new Controller();
//        String[] rowIdSaved = {"Add A Number", "Find Your phone", "Show Data", "Settings", "Developer"};
//        String[] phoneNumbersSaved = {"Add A Number", "Find Your phone", "Show Data", "Settings", "Developer"};
//        String[] securityNumber = {"Add A Number", "Find Your phone", "Show Data", "Settings", "Developer"};
        String rowIdSaved[] = controller.justRow(this);
        String phoneNumbersSaved[] = controller.justPhone(this);
        String securityNumber[] = controller.justSeq(this);


        ListView listView = findViewById(R.id.displayItems);
        listViewAdapterShowData = new ListViewAdapterShowData(this, rowIdSaved, phoneNumbersSaved, securityNumber);
        listView.setAdapter(listViewAdapterShowData);
        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    }
                }
        );

    }

    @Override
    public void onClick(View view) {
//        if(view == btnShowData){
//            Toast.makeText(this, "Are u working", Toast.LENGTH_SHORT).show();
//            StringBuffer hey = controller.showAllDatainDatabase(this);
////            System.out.println(hey);
//
//            String rowIdSaved[] = controller.justRow(this);
//            String phoneNumbersSaved[] = controller.justNumbers(this);
//            String securityNumber[] = controller.justSecurity(this);
////            for(int i =0; i<phoneNumbersSaved.length;i++){
////                System.out.println("The phone numbers are :- "+ i + " "+phoneNumbersSaved[i]);
////                if(i==30){
////                    break;
////                }
////
////            }
////            System.out.println(phoneNumbersSaved.toString());
////            System.out.println(securityNumber.toString());
//
//
//        }
    }
}
