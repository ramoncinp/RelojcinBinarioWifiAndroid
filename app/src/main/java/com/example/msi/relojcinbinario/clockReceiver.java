package com.example.msi.relojcinbinario;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by MSI on 19/07/2017.
 */

public class clockReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive (Context context, Intent intent)
    {
        DateFormat df = new SimpleDateFormat("HH,mm");
        String date = df.format(new Date());
    }
}
