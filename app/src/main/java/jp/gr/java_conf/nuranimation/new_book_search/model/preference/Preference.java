package jp.gr.java_conf.nuranimation.new_book_search.model.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Preference {

    private static final String KEY_ACCESS_TOKEN = "Preference.KEY_ACCESS_TOKEN";

    private static Preference preference;


    private Preference(){
    }


    public static synchronized Preference getInstance(){
        if(preference == null){
            preference = new Preference();
        }
        return preference;
    }


    public String getAccessToken(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        return sharedPreferences.getString(KEY_ACCESS_TOKEN, null);
    }

    public void setAccessToken(Context context, String token){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        sharedPreferences.edit().putString(KEY_ACCESS_TOKEN, token).apply();
    }

    public void deleteAccessToken(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        sharedPreferences.edit().remove(KEY_ACCESS_TOKEN).apply();
    }

}
