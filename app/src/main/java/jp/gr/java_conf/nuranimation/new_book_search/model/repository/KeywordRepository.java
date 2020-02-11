package jp.gr.java_conf.nuranimation.new_book_search.model.repository;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import jp.gr.java_conf.nuranimation.new_book_search.model.database.AppDatabase;
import jp.gr.java_conf.nuranimation.new_book_search.model.entity.Keyword;

@SuppressWarnings("WeakerAccess")
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

    public void registerKeyword(Context context, Keyword keyword){
        AppDatabase.getInstance(context).keywordDao().registerKeyword(keyword);
    }

    public void deleteKeyword(Context context, Keyword keyword){
        AppDatabase.getInstance(context).keywordDao().deleteKeyword(keyword);
    }


    public List<Keyword> loadKeywords(Context context){
        return AppDatabase.getInstance(context).keywordDao().loadAllKeyword();
    }

    public void saveKeywords(Context context, List<Keyword> keywords){
        AppDatabase.getInstance(context).keywordDao().replaceAllKeyword(keywords);
    }

    public List<String> loadKeywordList(Context context){
        List<Keyword> allKeyword = loadKeywords(context);
        List<String> keywords = new ArrayList<>();
        for(Keyword keyword : allKeyword){
            if(!keywords.contains(keyword.getWord())){
                keywords.add(keyword.getWord());
            }
        }
        return keywords;
    }

    public void saveKeywordList(Context context, List<String> list){
        List<Keyword> keywords = new ArrayList<>();
        for(String keyword : list){
            keywords.add(new Keyword(keyword));
        }
        saveKeywords(context, keywords);
    }

}
