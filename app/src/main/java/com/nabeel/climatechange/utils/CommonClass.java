package com.nabeel.climatechange.utils;

import android.content.Context;
import android.net.ConnectivityManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CommonClass {
    public static String currentDateandTime="";
    //Internet
    public static boolean isInternetOn(Context context) {
        ConnectivityManager connec = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connec != null;
        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED
                || connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING
                || connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING
                || connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {
            return true;
        } else if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED
                || connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {
            return false;
        }
        return false;
    }


    public static String getUniqueId() {
        Date date1 = new Date();
        //Calendar calUUID = Calendar.getInstance();
        DateFormat dateFormatUUID = new SimpleDateFormat("ddmmss");
        String uuuid= dateFormatUUID.format(date1);
        String tchdID=uuuid;

        return tchdID;
    }
}
