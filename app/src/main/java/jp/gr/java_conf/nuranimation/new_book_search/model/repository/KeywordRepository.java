package jp.gr.java_conf.nuranimation.new_book_search.model.repository;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import jp.gr.java_conf.nuranimation.new_book_search.ApplicationData;
import jp.gr.java_conf.nuranimation.new_book_search.model.entity.Keyword;

public class KeywordRepository {

    private static  KeywordRepository keywordRepository;
    private ApplicationData mApp;

    private KeywordRepository(Context context){
        mApp = (ApplicationData) context;
    }


    public synchronized static KeywordRepository getInstance(Context context){
        if(keywordRepository == null){
            keywordRepository = new KeywordRepository(context);
        }
        return keywordRepository;
    }


    public List<String> getKeywordList(){
        List<Keyword> allKeyword = mApp.getDatabase().keywordDao().loadAllKeyword();
        List<String> keywords = new ArrayList<>();
        for(Keyword keyword : allKeyword){
            if(!keywords.contains(keyword.getWord())){
                keywords.add(keyword.getWord());
            }
        }
        return keywords;
    }

}
