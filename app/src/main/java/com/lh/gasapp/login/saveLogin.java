package com.lh.gasapp.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class saveLogin {
    static final String PREF_USER_NAME= "username";
    static final String PHONE_HELPER= "phonenumber";

    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setUserName(Context ctx, String userName)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_NAME, userName);
        editor.commit();
    }
    public static String getUserName(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_USER_NAME, "");
    }

    public static void setPhoneHelper(Context ctx, String phoneNumber)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PHONE_HELPER, phoneNumber);
        editor.commit();
    }

    public static String getPhoneHelper(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PHONE_HELPER, "0764690776");
    }
}
