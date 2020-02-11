package jp.gr.java_conf.nuranimation.new_book_search.ui.keywords;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import jp.gr.java_conf.nuranimation.new_book_search.model.entity.Keyword;
import jp.gr.java_conf.nuranimation.new_book_search.model.repository.KeywordRepository;

@SuppressWarnings("WeakerAccess")
public class KeywordsViewModel extends AndroidViewModel {

    private Context context;
    private MutableLiveData<List<Keyword>> keywords;

    public KeywordsViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
        keywords = new MutableLiveData<>();
        loadKeywords();
    }


    public LiveData<List<Keyword>> getKeywords(){
        return keywords;
    }

    public void loadKeywords(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                keywords.postValue(KeywordRepository.getInstance().loadKeywords(context));
            }
        }).start();
    }

    public void registerKeyword(final Keyword keyword){
        new Thread(new Runnable() {
            @Override
            public void run() {
                KeywordRepository.getInstance().registerKeyword(context,keyword);
                keywords.postValue(KeywordRepository.getInstance().loadKeywords(context));
            }
        }).start();
    }

    public void deleteKeyword(final Keyword keyword){
        new Thread(new Runnable() {
            @Override
            public void run() {
                KeywordRepository.getInstance().deleteKeyword(context,keyword);
                keywords.postValue(KeywordRepository.getInstance().loadKeywords(context));
            }
        }).start();
    }


}