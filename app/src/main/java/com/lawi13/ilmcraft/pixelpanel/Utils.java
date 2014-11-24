package com.lawi13.ilmcraft.pixelpanel;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;

public class Utils {

    public static boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connectivityManager != null) {
            networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        }
        return networkInfo != null && networkInfo.isConnected();
    }

    public static String getIpAddress(Context context) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPrefs.getString(context.getString(R.string.prefEditTextIP), "");
    }

    public static boolean isValidIp(String ip) {
        return ip.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}");
    }

    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 3];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 3] = hexArray[v >>> 4];
            hexChars[j * 3 + 1] = hexArray[v & 0x0F];
            hexChars[j * 3 + 2] = ' ';
        }
        return new String(hexChars);
    }

    public static String StringToHex(String s) {
        char[] hexChars = new char[s.length() * 3];
        for (int j = 0; j < s.length(); j++) {
            int v = s.charAt(j) & 0xFF;
            hexChars[j * 3] = hexArray[v >>> 4];
            hexChars[j * 3 + 1] = hexArray[v & 0x0F];
            hexChars[j * 3 + 2] = ' ';
        }
        return new String(hexChars);
    }

    public static int intToARGB(int a, int r, int g, int b) {
        int argb = 0;
        argb |= (a & 0xff) << 24;
        argb |= (r & 0xff) << 16;
        argb |= (g & 0xff) << 8;
        argb |= (b & 0xff);
        return argb;
    }

    public static int ARGBtoA(int argb) {
        return (argb >> 24) & 0xff;
    }

    public static int ARGBtoR(int argb) {
        return (argb >> 16) & 0xff;
    }

    public static int ARGBtoG(int argb) {
        return (argb >> 8) & 0xff;
    }

    public static int ARGBtoB(int argb) {
        return argb & 0xff;
    }

    public static int AToARGB(int oldargb, int a) {
        oldargb &= 0x00ffffff;
        return oldargb |= (a & 0xff) << 24;
    }

    public static int RToARGB(int oldargb, int r) {
        oldargb &= 0xff00ffff;
        return oldargb |= (r & 0xff) << 16;
    }

    public static int GToARGB(int oldargb, int g) {
        oldargb &= 0xffff00ff;
        return oldargb |= (g & 0xff) << 8;
    }

    public static int BToARGB(int oldargb, int b) {
        oldargb &= 0xffffff00;
        return oldargb |= (b & 0xff);
    }


}
