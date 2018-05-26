package com.aleweny.prouction.controller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by ${Abel_Tilahun} on ${4/9/2018}.
 */
public class SimCardChangeReciver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent){
        Toast.makeText(context, "Sim card change detected", Toast.LENGTH_LONG).show();

    }
}
