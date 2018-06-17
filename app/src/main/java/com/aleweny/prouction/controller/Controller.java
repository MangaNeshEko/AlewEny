package com.aleweny.prouction.controller;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.aleweny.prouction.Model.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${Abel_Tilahun} on ${4/9/2018}.
 */

public class Controller {
    private Model model;

    private LocationManager locationManager;
    private double latitude = 0.0;
    private double longitude = 0.0;
    private String messageBody="";

    public void enterPhoneToDatabase(Context context,
                                     String recoveringPhoneNumber,
                                     String keyword,
                                     View view) {
        //Validating inputs of EnterPhoneClass
        if (TextUtils.isEmpty(recoveringPhoneNumber) || TextUtils.isEmpty(keyword)) {
            //If this empty create a SnackBar
            Snackbar.make(view, "Please Fill in all the inputs", Snackbar.LENGTH_LONG).show();
//            Toast.makeText(context, "Please Enter Fill in the forms", Toast.LENGTH_LONG).show();
        } else {
            //Saving in the database
            this.model = new Model(context);
//            Model model = new Model();
            if (recoveringPhoneNumber.startsWith("+251")) {
                if (recoveringPhoneNumber.length() == 13) {
                    //Here the user have inserted the correct format of the phone number,
                    //Automatically the model class is called
                    saveToDatabase(context, recoveringPhoneNumber, keyword);
                } else {
                    Toast.makeText(context, "Phone Number Not a valid length", Toast.LENGTH_SHORT).show();
                }
            } else {
                long phoneNum = Long.parseLong(recoveringPhoneNumber);
                String withOutZero = String.valueOf(phoneNum);
                if (withOutZero.startsWith("0")) {
                    withOutZero = withOutZero.replace("0", "+251");
                    saveToDatabase(context, withOutZero, keyword);
                } else {
//                    Toast.makeText(context, "I am here", Toast.LENGTH_SHORT).show();
                    withOutZero = "+251" + withOutZero;
                    saveToDatabase(context, withOutZero, keyword);
                }
            }


        }


    }

    public void saveToDatabase(Context context, String withOutZero, String keyword) {
        boolean successful = model.insertData(withOutZero, keyword);
        if (successful) {
            Toast.makeText(context, "Successful", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "Unsuccessful", Toast.LENGTH_LONG).show();
        }
    }

    public void findPhoneController(Context context,
                                    View view,
                                    String phoneNumber,
                                    String securityKeyWord) {
        if (TextUtils.isEmpty(phoneNumber) || TextUtils.isEmpty(securityKeyWord)) {
            Snackbar.make(view, "Please Fill in all the input forms", Snackbar.LENGTH_LONG).show();
        } else {
            //Call a method that will send a message
            sendMessage(context, phoneNumber, securityKeyWord);

        }

    }

    private void sendMessage(Context context, String phoneNumber, String securityKeyWord) {
        //15555215554
        //Send a message to the phone number that have been specified in the parameter
        if(phoneNumber.contains("+")){
            //Toast.makeText(context, "This is the problem", Toast.LENGTH_SHORT).show();
           phoneNumber = phoneNumber.replace("+", "");
        }
        if (TextUtils.isDigitsOnly(phoneNumber)) {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, securityKeyWord, null, null);
            Toast.makeText(context, "Successfully Sent", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Make Sure the phone number is digit", Toast.LENGTH_SHORT).show();
        }
//        System.out.println("The Phone Number is:- "+ phoneNumber +"The security code is:- "+ securityKeyWord);
    }

    public String getThePhoneNumber(Context context) {
        //This is for getting the phone number of the phone that is sending the message to trigger the broadcast receiver
        String currentPhoneNumber = "";
        List<SubscriptionInfo> subscription = SubscriptionManager.from(context).getActiveSubscriptionInfoList();

        for (int i = 0; i < subscription.size(); i++) {
            SubscriptionInfo info = subscription.get(i);
            currentPhoneNumber = info.getNumber();
//                System.out.println("network name : " + info.getCarrierName());
//                System.out.println( "country iso " + info.getCountryIso());
        }
        return currentPhoneNumber;
    }

    public boolean newMessageReceived(String phoneNumber, String securityCode, Context context) {
        //TODO change this Return type to a void method
        /*
        *
        Here Checking if the phone number in the database and the number gotten from the view matches
        First get the phone number n security code from both sides
        The match them, if the match then Trigger the "LEBA" mode, if not leave it

        The difference between findPhoneController and newMessageReceived
        findPhone is triggered when the user triggers the method from other phone
        newMessage is triggred by the broadcast
        *
        */
        //Toast.makeText(context, "Stolen 2", Toast.LENGTH_LONG).show();

        if (TextUtils.isEmpty(phoneNumber) || TextUtils.isEmpty(securityCode)) {
            return false;
        } else {
//            showAllDatainDatabase(context, phoneNumber, securityCode);
            //This Phone number is the recoveryPhone number, The incoming text phone number
            matching(context, phoneNumber, securityCode);

        }
        return true;
    }
    public void bootSIMChange(Context context, String message){
        String phoneNumberRecovery = "";
        Model model = new Model(context);
        Cursor res = model.showAllData();
        while (res.moveToNext()) {
            phoneNumberRecovery = res.getString(1);
        }

        sendMessage(context, phoneNumberRecovery, "Your Sim Card has been Changed");

    }

    private void matching(Context context, String phoneNumberRecoveryMSG, String securityCodeMSG) {
//        String phoneNumberDatabase="";
        String phoneNumberRecovery = "";
        String securityCode = "";
        Model model = new Model(context);
        Cursor res = model.showAllData();
        while (res.moveToNext()) {

//            phoneNumberDatabase = res.getString(1);
            phoneNumberRecovery = res.getString(1);
            securityCode = res.getString(2);
        }
        System.out.println("Phone Number Recovery Stored:- " + phoneNumberRecovery);
        System.out.println("Security Code Stored:- " + securityCode);

        System.out.println("The RecoverPhone Number Message:- " + phoneNumberRecoveryMSG);
        System.out.println("Security Code:- " + securityCodeMSG);


        if (phoneNumberRecovery.equals(phoneNumberRecoveryMSG) && securityCode.equals(securityCodeMSG)) {
            //Means all of the requirement for the phone to be stolen is met.
            LEBA(context,phoneNumberRecoveryMSG);
        }

    }

    private void LEBA(Context context, String phoneNumber) {
        Toast.makeText(context, "The phone is Stolen", Toast.LENGTH_LONG).show();
        boolean locationFound = getLocation(context);
        String messageBody = "";
        if (locationFound) {
            messageBody = "The Location of the phone is \n@ Latitude " + Double.toString(latitude) + "\n@ Longitude" + Double.toString(this.longitude);
//            System.out.print(messageBody);
            sendMessage(context,phoneNumber, messageBody);
        } else {
            Toast.makeText(context, "Something Wrong when sending a message", Toast.LENGTH_SHORT).show();
        }
    }
    private void getLatitude(double latitudes) {
        this.latitude = latitudes;
    }
    private void getLongitude(double longitudes) {
        this.longitude = longitudes;
    }
    private boolean getLocation(final Context context) {
        //Get the location of the phone
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            double longitude;
            double latitude;
            @Override
            public void onLocationChanged(Location location) {
//                System.out.println("The Location lat;- " + location.getLatitude() + " The location long:- " + location.getLongitude());
                longitude =location.getLongitude();
                latitude = location.getLatitude();

                if(!(longitude == 0.0 && latitude ==0.0)){
                    getLatitude(latitude);
                    getLongitude(longitude);
                }else {
                    longitude = location.getLongitude();
                    latitude = location.getLatitude();
                }

//               String messageBody = "The Location of the phone is \n@ Latitude " + Double.toString(latitude) + "\n@ Longitude" + Double.toString(longitude);

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(context, "Thank Your for giving us permission", Toast.LENGTH_LONG).show();
        }
        assert locationManager != null;
        locationManager.requestLocationUpdates("gps", 50000, 0, locationListener);
        return true;
    }

//    public void showAllDatainDatabase(Context context, String phoneNumber, String securityCode)
    public StringBuffer showAllDatainDatabase(Context context){
        model = new Model(context);
        Cursor res = model.showAllData();
        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext()) {
            buffer.append("\nID:- " + res.getString(0)
                    + "\nRecovery Phone Number:- " + res.getString(1)
                    + "\nSecurity Code:- " + res.getString(2));
        }
        System.out.println(buffer.toString());
//        System.out.println("Phone Sent:- " + phoneNumber + "\nMessage Body:-" + securityCode);

        return buffer;
    }





    public String[] justRow(Context context){
        model = new Model(context);
        Cursor res = model.showAllData();
        if(res == null){
            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
            //return null;
        }
//        String phoneNumBuffer[] = new String[];
//        ArrayList phoneNumBuffer = new ArrayList<String>();
        StringBuilder phoneNumBuffer = new StringBuilder();

        List<String> list = new ArrayList<String>();
        assert res != null;
        while (res.moveToNext()){
            list.add(res.getString(0));
        }
        String phonNumSotred[] = new String[list.size()];
        for(int i =0; i< list.size(); i++){
            phonNumSotred[i] = list.get(i);
        }

        return phonNumSotred;
    }
    public String[] justPhone(Context context){
        model = new Model(context);
        Cursor res = model.showAllData();
        if(res == null){
            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
            //return null;
        }
//        String phoneNumBuffer[] = new String[];
//        ArrayList phoneNumBuffer = new ArrayList<String>();
        StringBuilder phoneNumBuffer = new StringBuilder();
        List<String> list = new ArrayList<String>();
        assert res != null;
        while (res.moveToNext()){
            list.add(res.getString(1));
        }
        String phonNumSotred[] = new String[list.size()];
        for(int i =0; i< list.size(); i++){
            phonNumSotred[i] = list.get(i);
        }

        return phonNumSotred;

    }
    public String[] justSeq(Context context){
        model = new Model(context);
        Cursor res = model.showAllData();
        if(res == null){
            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
            //return null;
        }
//        String phoneNumBuffer[] = new String[];
//        ArrayList phoneNumBuffer = new ArrayList<String>();
        List<String> list = new ArrayList<String>();
        assert res != null;
        while (res.moveToNext()){
            list.add(res.getString(2));
        }
        String phonNumSotred[] = new String[list.size()];
        for(int i =0; i< list.size(); i++){
            phonNumSotred[i] = list.get(i);
        }

        return phonNumSotred;
    }
}