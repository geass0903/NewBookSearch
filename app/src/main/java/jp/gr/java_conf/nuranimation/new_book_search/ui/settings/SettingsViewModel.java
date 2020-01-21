package jp.gr.java_conf.nuranimation.new_book_search.ui.settings;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.dropbox.core.android.Auth;

import jp.gr.java_conf.nuranimation.new_book_search.ApplicationData;

public class SettingsViewModel extends AndroidViewModel {

    private ApplicationData mApp;
    private MutableLiveData<String> mToken;

    public SettingsViewModel(@NonNull Application application) {
        super(application);
        Context context = application.getApplicationContext();
        mApp = (ApplicationData)context;
        mToken = new MutableLiveData<>();
    }

    public LiveData<String> getToken() {
        return mToken;
    }

    public void checkAccessToken(){
        String token = Auth.getOAuth2Token();
        if(token != null){
            mApp.getPreferences().edit().putString(ApplicationData.KEY_ACCESS_TOKEN,token).apply();
            Log.d("checkAccessToken","token : " + token);
            mToken.postValue(token);
        }else{
            token = mApp.getPreferences().getString(ApplicationData.KEY_ACCESS_TOKEN, null);
            Log.d("checkAccessToken","preference token : " + token);
            mToken.postValue(token);
        }
    }

    public void deleteAccessToken(){
        mApp.getPreferences().edit().remove(ApplicationData.KEY_ACCESS_TOKEN).apply();
        Log.d("checkAccessToken","delete token : ");
        mToken.postValue(null);
    }

}