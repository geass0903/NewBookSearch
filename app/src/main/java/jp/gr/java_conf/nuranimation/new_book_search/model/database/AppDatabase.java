package jp.gr.java_conf.nuranimation.new_book_search.model.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import jp.gr.java_conf.nuranimation.new_book_search.model.entity.Item;
import jp.gr.java_conf.nuranimation.new_book_search.model.entity.Keyword;

@Database(entities = {Item.class, Keyword.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "new_book.db";
    private static AppDatabase sInstance;


    public abstract BookDao bookDao();
    public abstract KeywordDao keywordDao();

    public static AppDatabase getInstance(Context context){
        if(sInstance == null){
            synchronized (AppDatabase.class) {
                sInstance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DATABASE_NAME).build();
            }
        }
        return sInstance;
    }

}
