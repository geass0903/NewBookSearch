package jp.gr.java_conf.nuranimation.new_book_search.model.entity;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "keyword_list")
public class Keyword {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String word;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Keyword(){
    }

    @Ignore
    public Keyword(String word){
        this.word = word;
    }

    @Ignore
    public Keyword(int id, String word){
        this.id = id;
        this.word = word;
    }

    public Keyword(Keyword keyword){
        this.id = keyword.getId();
        this.word = keyword.getWord();
    }


}
