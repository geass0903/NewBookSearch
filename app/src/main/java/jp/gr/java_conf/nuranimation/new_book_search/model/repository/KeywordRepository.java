package jp.gr.java_conf.nuranimation.new_book_search.model.repository;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import jp.gr.java_conf.nuranimation.new_book_search.ApplicationData;
import jp.gr.java_conf.nuranimation.new_book_search.model.entity.Keyword;

public class KeywordRepository {

    private static  KeywordRepository keywordRepository;

    private KeywordRepository(){
    }


    public synchronized static KeywordRepository getInstance(){
        if(keywordRepository == null){
            keywordRepository = new KeywordRepository();
        }
        return keywordRepository;
    }


    public List<String> loadKeywordList(Context context){
        ApplicationData app = (ApplicationData) context.getApplicationContext();
        List<Keyword> allKeyword = app.getDatabase().keywordDao().loadAllKeyword();
        List<String> keywords = new ArrayList<>();
        for(Keyword keyword : allKeyword){
            if(!keywords.contains(keyword.getWord())){
                keywords.add(keyword.getWord());
            }
        }
        return keywords;
    }

    public void saveKeywordList(Context context, List<String> list){
        ApplicationData app = (ApplicationData) context.getApplicationContext();
        List<Keyword> keywords = new ArrayList<>();
        for(String keyword : list){
            keywords.add(new Keyword(keyword));
        }
        app.getDatabase().keywordDao().replaceAllKeyword(keywords);
    }

}