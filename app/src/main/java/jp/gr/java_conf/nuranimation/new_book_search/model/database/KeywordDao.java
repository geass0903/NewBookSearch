package jp.gr.java_conf.nuranimation.new_book_search.model.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import jp.gr.java_conf.nuranimation.new_book_search.model.entity.Keyword;

@Dao
public abstract class KeywordDao {

    @Query("select * from keyword_list order by word asc")
    public abstract List<Keyword> loadAllKeyword();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void registerKeyword(Keyword keyword);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void registerKeywords(List<Keyword> keywords);

    @Delete
    public abstract void deleteKeyword(Keyword keyword);

    @Query("delete from keyword_list")
    public abstract void deleteAllKeyword();

    @Transaction
    public void replaceAllKeyword(List<Keyword> keywords) {
        deleteAllKeyword();
        registerKeywords(keywords);
    }

}
