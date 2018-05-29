package com.aleweny.prouction.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aleweny.prouction.R;

import org.w3c.dom.Text;

/**
 * Created by littELGOsht on 4/9/2018.
 */

public class ListViewAdapterShowData extends ArrayAdapter{

    private String[] rowIdToBe;
    private String[] phoneNumberToBe;
    private String[] securityCodeToBe;

    public static SharedPreferences sharedPreferences;
    boolean backBoolean;

    public ListViewAdapterShowData(Context context, String[] rowId, String[] phoneNumber, String[] securityCode ){
        super(context, R.layout.listitem,R.id.title, rowId);

        this.rowIdToBe = rowId;
        this.phoneNumberToBe = phoneNumber;
        this.securityCodeToBe = securityCode;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.showdatalistview, parent,false);

        TextView rowView = row.findViewById(R.id.rowNumber);
        TextView phoneNumbView = row.findViewById(R.id.phoneNumberRow);
        TextView security = row.findViewById(R.id.securityNum);

        sharedPreferences = getContext().getSharedPreferences("Settings",0);
        backBoolean = sharedPreferences.getBoolean("switchOne",true);

        if(backBoolean){
            rowView.setTextColor(getContext().getResources().getColor(R.color.whileBack));
            phoneNumbView.setTextColor(getContext().getResources().getColor(R.color.whileBack));
            security.setTextColor(getContext().getResources().getColor(R.color.whileBack));

            rowView.setText(rowIdToBe[position]);
            phoneNumbView.setText(phoneNumberToBe[position]);
            security.setText(securityCodeToBe[position]);
//        image.setBackgroundResource(backgroundToBe[position]);
        }else{
            //By Iterating using the value of position lets make a layout
            rowView.setText(rowIdToBe[position]);
            phoneNumbView.setText(phoneNumberToBe[position]);
            security.setText(securityCodeToBe[position]);
        }

        return row;
    }
}
