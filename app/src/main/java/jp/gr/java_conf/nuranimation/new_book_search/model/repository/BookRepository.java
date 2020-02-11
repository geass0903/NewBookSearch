package jp.gr.java_conf.nuranimation.new_book_search.model.repository;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import jp.gr.java_conf.nuranimation.new_book_search.model.database.AppDatabase;
import jp.gr.java_conf.nuranimation.new_book_search.model.entity.Books;
import jp.gr.java_conf.nuranimation.new_book_search.model.entity.Item;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BookRepository {

    private IBooksTotalApi booksTotalApi;
    private static BookRepository bookRepository;

    private static final String QUERY_FORMAT = "json";
    private static final String QUERY_GENRE = "001"; // Books
    private static final String QUERY_SORT = "-releaseDate"; // sort desc releaseDate
    private static final int QUERY_FORMAT_VERSION = 2;
    private static final int QUERY_HITS = 20;
    private static final int QUERY_STOCK_FLAG = 1;
    private static final int QUERY_FIELD = 0;

    private BookRepository() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(IBooksTotalApi.HTTPS_API_RAKUTEN_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        booksTotalApi = retrofit.create(IBooksTotalApi.class);
    }

    public synchronized static BookRepository getInstance() {
        if (bookRepository == null) {
            bookRepository = new BookRepository();
        }
        return bookRepository;
    }

    public List<Item> loadAllBooks(Context context){
        return AppDatabase.getInstance(context).bookDao().loadAllBooks();
    }

    public void replaceAllBooks(Context context,List<Item> books){
        AppDatabase.getInstance(context).bookDao().replaceAll(books);
    }


    public List<Item> searchNewBooks(String keyword){
        List<Item> new_books = new ArrayList<>();
        int page = 1;
        boolean hasNext = true;
        search : while(hasNext) {
            int retried = 0;
            while (retried < 3) {
                try {
                    Thread.sleep(1000);
                    Response<Books> response = bookRepository.search(keyword, page);
                    if (response.isSuccessful() && response.code() == HttpURLConnection.HTTP_OK) {
                        Books books = response.body();
                        if (books != null) {
                            int count = books.getCount();
                            List<Item> items = books.getItems();
                            int last = books.getLast();
                            hasNext = count - last > 0;
                            for (Item book : items) {
                                if (isNewBook(book)) {
                                    new_books.add(book);
                                } else {
                                    hasNext = false;
                                    break search;
                                }
                            }
                            page++;
                            break;
                        } else {
                            // No books
                            hasNext = false;
                            break search;
                        }
                    } else {
                        int code = response.code();
                        switch (code) {
                            case HttpURLConnection.HTTP_BAD_REQUEST:    // 400 wrong parameter
                                break search;
                            case HttpURLConnection.HTTP_NOT_FOUND:      // 404 not success
                            case 429:                                   // 429 too many requests
                            case HttpURLConnection.HTTP_INTERNAL_ERROR: // 500 system error
                            case HttpURLConnection.HTTP_UNAVAILABLE:    // 503 service unavailable
                                // retry
                                break;
                        }
                    }
                } catch (InterruptedException | IOException e) {
                    e.printStackTrace();
                    break;
                }
                retried++;
            }
        }
        return new_books;
    }


    private Response<Books> search(String keyword, int page) throws IOException {
        return booksTotalApi.search(IBooksTotalApi.APPLICATION_ID, QUERY_FORMAT, QUERY_FORMAT_VERSION, QUERY_GENRE,
                QUERY_HITS, QUERY_STOCK_FLAG, QUERY_FIELD, QUERY_SORT, keyword, page).execute();
    }


    private static boolean isNewBook(@NonNull Item book){
        Calendar baseDate = Calendar.getInstance();
        baseDate.add(Calendar.MONTH,-1);
        Calendar salesDate = parseDateString(book.getSalesDate());
        if(salesDate != null){
            return salesDate.compareTo(baseDate) >= 0;
        }else{
            return false;
        }
    }

    @Nullable
    private static Calendar parseDateString(@NonNull String source) {
        final String DATE_FORMAT_1 = "yyyy年MM月dd日";
        final String DATE_FORMAT_2 = "yyyy年MM月";
        final String DATE_FORMAT_3 = "yyyy年";
        final String DATE_FORMAT_4 = "yyyy/MM/dd";

        String[] enableFormats = new String[]{DATE_FORMAT_1, DATE_FORMAT_4, DATE_FORMAT_2, DATE_FORMAT_3};
        for (String format : enableFormats) {

            SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.JAPAN);
            sdf.setLenient(false);
            try {
                Date date = sdf.parse(source);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                switch (format) {
                    case DATE_FORMAT_2:
                        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                        break;
                    case DATE_FORMAT_3:
                        calendar.set(Calendar.MONTH, calendar.getActualMaximum(Calendar.MONTH));
                        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                        break;
                }
                return calendar;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


}
