package utils.mahmoudmabrok.eventplanner.dataLayer.local;

import android.content.Context;
import android.content.SharedPreferences;

import static utils.mahmoudmabrok.eventplanner.feature.login.LogIn.PREF_ACCOUNT_NAME;

public class SharedPref {

    SharedPreferences.Editor editor;
    private SharedPreferences sharedPref;
    private String name = "data";


    public SharedPref(Context context) {
        sharedPref = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        editor = sharedPref.edit();
    }

    public void setAccountName(String accountName) {
        editor.putString(PREF_ACCOUNT_NAME, accountName).apply();
    }

    public String getName() {
        return sharedPref.getString(PREF_ACCOUNT_NAME, null);
    }
}