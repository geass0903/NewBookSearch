package jp.gr.java_conf.nuranimation.new_book_search.ui.keywords;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import jp.gr.java_conf.nuranimation.new_book_search.ApplicationData;
import jp.gr.java_conf.nuranimation.new_book_search.model.entity.Keyword;


public class KeywordsViewModel extends AndroidViewModel {

    private ApplicationData mApp;
    private MutableLiveData<List<Keyword>> mKeywords;

    public KeywordsViewModel(@NonNull Application application) {
        super(application);
        Context context = application.getApplicationContext();
        mApp = (ApplicationData)context;
        mKeywords = new MutableLiveData<>();
        loadKeywords();
    }


    public LiveData<List<Keyword>> getKeywords(){
        return mKeywords;
    }

    public void loadKeywords(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                mKeywords.postValue(mApp.getDatabase().keywordDao().loadAllKeyword());
            }
        }).start();
    }



    public void registerKeyword(final Keyword keyword){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("TAG","id: " + keyword.getId());
                Log.d("TAG","word: " + keyword.getWord());
                mApp.getDatabase().keywordDao().registerKeyword(keyword);
                mKeywords.postValue(mApp.getDatabase().keywordDao().loadAllKeyword());
            }
        }).start();
    }

    public void deleteKeyword(final Keyword keyword){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("TAG","id: " + keyword.getId());
                Log.d("TAG","word: " + keyword.getWord());
                mApp.getDatabase().keywordDao().deleteKeyword(keyword);
                mKeywords.postValue(mApp.getDatabase().keywordDao().loadAllKeyword());
            }
        }).start();
    }

}