package jp.gr.java_conf.nuranimation.new_book_search.ui.progress_dialog;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import jp.gr.java_conf.nuranimation.new_book_search.ApplicationData;
import jp.gr.java_conf.nuranimation.new_book_search.model.entity.Books;
import jp.gr.java_conf.nuranimation.new_book_search.model.entity.Item;
import jp.gr.java_conf.nuranimation.new_book_search.model.entity.Keyword;
import jp.gr.java_conf.nuranimation.new_book_search.model.entity.Result;
import jp.gr.java_conf.nuranimation.new_book_search.model.repository.BookRepository;
import retrofit2.Response;


public class ProgressDialogViewModel extends AndroidViewModel {
    private static final String TAG = ProgressDialogViewModel.class.getSimpleName();
    private static final boolean D = true;

    public static final int STATE_NONE     = 0;
    public static final int STATE_PROGRESS = 1;
    public static final int STATE_COMPLETE = 2;

    private int state;
    private MutableLiveData<String> title;
    private MutableLiveData<String> message;
    private MutableLiveData<String> progress;
    private MutableLiveData<Result> result;

    private ApplicationData mApp;

    public ProgressDialogViewModel(@NonNull Application application) {
        super(application);
        Log.d("ProgressDialogViewModel", "ProgressDialogViewModel()");
        Context context = application.getApplicationContext();
        mApp = (ApplicationData) context;
        state = STATE_NONE;
        title = new MutableLiveData<>();
        message = new MutableLiveData<>();
        progress = new MutableLiveData<>();
        result = new MutableLiveData<>();
    }

    public int getState(){
        return state;
    }



    public MutableLiveData<String> getTitle() {
        return title;
    }

    public MutableLiveData<String> getMessage() {
        return message;
    }

    public MutableLiveData<String> getProgress() {
        return progress;
    }

    public MutableLiveData<Result> getResult(){
        return result;
    }

    public void setState(int state){
        this.state = state;
    }

    public void setTitle(String title) {
        this.title.postValue(title);
    }

    public void setMessage(String message) {
        this.message.postValue(message);
    }

    public void setProgress(String progress) {
        this.progress.postValue(progress);
    }

    public void setResult(Result result){
        this.result.postValue(result);
    }


    public void reload(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                setState(STATE_PROGRESS);
                Result mResult = reloadNewBooks();
                setState(STATE_COMPLETE);
                setResult(mResult);
            }
        }).start();
    }


    @Override
    protected void onCleared() {
        Log.d("ProgressDialogViewModel", "onCleared()");
        setState(STATE_NONE);
    }




    private Result reloadNewBooks(){
        List<Keyword> allKeyword = mApp.getDatabase().keywordDao().loadAllKeyword();
        List<String> keywords = new ArrayList<>();
        for(Keyword keyword : allKeyword){
            if(!keywords.contains(keyword.getWord())){
                keywords.add(keyword.getWord());
            }
        }
        if(keywords.size() == 0){
            return Result.ReloadError(Result.ERROR_CODE_EMPTY_KEYWORDS, "empty keywords");
        }

        int size = keywords.size();
        int count = 0;
        String message;
        String value;

        List<Item> books = new ArrayList<>();
        for (String keyword : keywords) {
            if (state != STATE_PROGRESS) {
                return Result.ReloadError(Result.ERROR_CODE_RELOAD_CANCELED, "reload canceled");
            }
            if(D) Log.d(TAG, "keyword : " + keyword);
            count++;
            message = keyword;
            value = count + "/" + size;
            setMessage(message);
            setProgress(value);
            List<Item> newBooks = getNewBooks(keyword);
            books.addAll(newBooks);
        }
        if(books.size() > 0) {
            mApp.getDatabase().bookDao().replaceAll(books);
            return Result.ReloadSuccess(books);
        }else{
            return Result.ReloadError(Result.ERROR_CODE_IO_EXCEPTION, "no books");
        }
    }

    private List<Item> getNewBooks(String keyword) {
        List<Item> books = new ArrayList<>();
        int page = 1;
        boolean hasNext = true;
        while(hasNext){
            Result result = search(keyword,page);
            if(result.isSuccess()){
                hasNext = result.hasNext();
                List<Item> result_books = result.getBooks();
                for(Item book : result_books){
                    if(isNewBook(book)){
                        books.add(book);
                    }else{
                        hasNext = false;
                        break;
                    }
                }
                page++;
            }else{
                hasNext = false;
            }
        }
        return books;
    }

    private Result search(String keyword, int page) {
        Result mResult = Result.SearchError(Result.ERROR_CODE_UNKNOWN, "search failed");
        int retried = 0;
        while (retried < 3) {
            if (state != STATE_PROGRESS) {
                return Result.SearchError(Result.ERROR_CODE_IO_EXCEPTION, "search canceled");
            }

            try {
                Thread.sleep(1000);

                Response<Books> response = BookRepository.getInstance().search(keyword,page);
                if (response.isSuccessful() && response.code() == HttpURLConnection.HTTP_OK) {
                    Books books = response.body();
                    if (books != null) {
                        int count = books.getCount();
                        List<Item> items = books.getItems();
                        int last = books.getLast();
                        boolean hasNext = count - last > 0;
                        return Result.SearchSuccess(items, hasNext);
                    } else {
                        return Result.SearchError(Result.ERROR_CODE_IO_EXCEPTION, "No item");
                    }
                } else {
                    int code = response.code();
                    switch (code) {
                        case HttpURLConnection.HTTP_BAD_REQUEST:    // 400 wrong parameter
                            return Result.SearchError(Result.ERROR_CODE_IO_EXCEPTION, "wrong parameter");
                        case HttpURLConnection.HTTP_NOT_FOUND:      // 404 not success
                        case 429:                                   // 429 too many requests
                        case HttpURLConnection.HTTP_INTERNAL_ERROR: // 500 system error
                        case HttpURLConnection.HTTP_UNAVAILABLE:    // 503 service unavailable
                            // retry
                            break;
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                if (D) Log.d(TAG, "InterruptedException");
                mResult = Result.SearchError(Result.ERROR_CODE_INTERRUPTED_EXCEPTION, "InterruptedException");
                // retry
            } catch (IOException e) {
                e.printStackTrace();
                if (D) Log.d(TAG, "IOException");
                mResult = Result.SearchError(Result.ERROR_CODE_IO_EXCEPTION, "IOException");
                // retry
            }
            retried++;
        }
        return mResult;
    }

    private boolean isNewBook(Item book){
        Calendar baseDate = Calendar.getInstance();
        baseDate.add(Calendar.MONTH,-12);
        Calendar salesDate = parseDateString(book.getSalesDate());
        if(salesDate != null){
            return salesDate.compareTo(baseDate) >= 0;
        }else{
            return false;
        }
    }

    private static Calendar parseDateString(String source) {
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
                if (D) e.printStackTrace();
            }
        }
        return null;
    }




}
