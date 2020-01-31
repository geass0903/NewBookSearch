package jp.gr.java_conf.nuranimation.new_book_search.ui.settings;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.dropbox.core.android.Auth;

import jp.gr.java_conf.nuranimation.new_book_search.model.preference.Preference;
import jp.gr.java_conf.nuranimation.new_book_search.model.usecase.DropboxApi;

@SuppressWarnings("WeakerAccess")
public class SettingsViewModel extends AndroidViewModel {

    private static final String DROP_BOX_KEY = "sf7d9ckccl57xvf";
    private Context context;
    private MutableLiveData<String> mToken;


    public SettingsViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
        mToken = new MutableLiveData<>();
    }

    public LiveData<String> getToken() {
        return mToken;
    }

    public void authDropbox(){
        DropboxApi.getInstance().startAuth(context);
    }


    public void checkAccessToken(){
        String token = Auth.getOAuth2Token();
        if(token != null){
            Preference.getInstance().setAccessToken(context, token);
            mToken.postValue(token);
        }else{
            token = Preference.getInstance().getAccessToken(context);
            mToken.postValue(token);
        }
    }

    public void deleteAccessToken(){
        Preference.getInstance().deleteAccessToken(context);
        mToken.postValue(null);
    }

}