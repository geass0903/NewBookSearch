package jp.gr.java_conf.nuranimation.new_book_search.model.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "new_books")
public class Item {
    @PrimaryKey
    @NonNull
    private String isbn;
    private String title;
    private String author;
    @ColumnInfo(name = "publisher_name")
    private String publisherName;
    @ColumnInfo(name = "sales_date")
    private String salesDate;
    @ColumnInfo(name = "large_image_url")
    private String largeImageUrl;

    /*
    private String artistName;
    private String label;
    private String jan;
    private String hardware;
    private String os;
    private String itemCaption;
    private int itemPrice;
    private int listPrice;
    private int discountRate;
    private int discountPrice;
    private String itemUrl;
    private String affiliateUrl;
    private String smallImageUrl;
    private String mediumImageUrl;
    private String chirayomiUrl;
    private String availability;
    private int postageFlag;
    private int limitedFlag;
    private int reviewCount;
    private String reviewAverage;
    private String booksGenreId;
     */


    @NonNull
    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(@NonNull String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

    public String getLargeImageUrl() {
        return largeImageUrl;
    }

    public void setLargeImageUrl(String largeImageUrl) {
        this.largeImageUrl = largeImageUrl;
    }

    public String getSalesDate() {
        return salesDate;
    }

    public void setSalesDate(String salesDate) {
        this.salesDate = salesDate;
    }

    public Item(){
        this.isbn = "dummy";
    }

    @Ignore
    public Item(@NonNull String isbn, String title, String author, String publisherName,
                String salesDate, String largeImageUrl){
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.publisherName = publisherName;
        this.salesDate = salesDate;
        this.largeImageUrl = largeImageUrl;
    }

    public Item(Item book){
        this.isbn = book.getIsbn();
        this.title = book.getTitle();
        this.author = book.getAuthor();
        this.publisherName = book.getPublisherName();
        this.salesDate = book.getSalesDate();
        this.largeImageUrl = book.getLargeImageUrl();
    }



}
