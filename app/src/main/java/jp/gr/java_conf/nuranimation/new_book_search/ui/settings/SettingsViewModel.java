package jp.gr.java_conf.nuranimation.new_book_search.ui.settings;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.dropbox.core.android.Auth;

import jp.gr.java_conf.nuranimation.new_book_search.model.usecase.DropboxApi;

@SuppressWarnings("WeakerAccess")
public class SettingsViewModel extends AndroidViewModel {

    private Context context;
    private MutableLiveData<String> token;


    public SettingsViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
        token = new MutableLiveData<>();
    }

    public LiveData<String> getToken() {
        return token;
    }


    public void checkLogin(boolean isRequestAuth){
        String token;
        if(isRequestAuth){
            token = Auth.getOAuth2Token();
            if(token != null) {
                DropboxApi.getInstance().setAccessToken(context, token);
            }
        }else{
            token = DropboxApi.getInstance().getAccessToken(context);
        }
        this.token.postValue(token);
    }

    public void logout(){
        DropboxApi.getInstance().deleteAccessToken(context);
        this.token.postValue(null);
    }

}