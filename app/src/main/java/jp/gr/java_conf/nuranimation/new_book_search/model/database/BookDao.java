package jp.gr.java_conf.nuranimation.new_book_search.model.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import jp.gr.java_conf.nuranimation.new_book_search.model.entity.Item;

@Dao
public abstract class BookDao {

    @Query("select * from new_books order by sales_date desc")
    public abstract List<Item> loadAllBooks();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void registerBooks(List<Item> books);

    @Query("delete from new_books")
    public abstract void deleteAllBooks();

    @Transaction
    public void replaceAll(List<Item> books){
        deleteAllBooks();
        registerBooks(books);
    }

}
