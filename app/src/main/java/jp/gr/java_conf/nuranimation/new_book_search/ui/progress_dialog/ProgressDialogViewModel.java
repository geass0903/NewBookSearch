package jp.gr.java_conf.nuranimation.new_book_search.ui.progress_dialog;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.List;
import java.util.ArrayList;

import jp.gr.java_conf.nuranimation.new_book_search.R;
import jp.gr.java_conf.nuranimation.new_book_search.ApplicationData;
import jp.gr.java_conf.nuranimation.new_book_search.model.entity.Item;
import jp.gr.java_conf.nuranimation.new_book_search.model.entity.Result;
import jp.gr.java_conf.nuranimation.new_book_search.model.repository.BookRepository;
import jp.gr.java_conf.nuranimation.new_book_search.model.repository.KeywordRepository;


@SuppressWarnings("WeakerAccess")
public class ProgressDialogViewModel extends AndroidViewModel {
    private static final String TAG = ProgressDialogViewModel.class.getSimpleName();
    private static final boolean D = true;

    public static final int STATE_NONE = 0;
    public static final int STATE_PROGRESS = 1;
    public static final int STATE_COMPLETE = 2;

    private Context context;
    private int state;
    private MutableLiveData<String> title;
    private MutableLiveData<String> message;
    private MutableLiveData<String> progress;
    private MutableLiveData<Result> result;

    private ApplicationData mApp;

    public ProgressDialogViewModel(@NonNull Application application) {
        super(application);
        if (D) Log.d(TAG, "ProgressDialogViewModel()");
        context = application.getApplicationContext();
        mApp = (ApplicationData) context;
        state = STATE_NONE;
        title = new MutableLiveData<>();
        message = new MutableLiveData<>();
        progress = new MutableLiveData<>();
        result = new MutableLiveData<>();
    }

    public int getState() {
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

    public MutableLiveData<Result> getResult() {
        return result;
    }

    public void setState(int state) {
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

    public void setResult(Result result) {
        this.result.postValue(result);
    }


    public void reloadNewBooks() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                setState(STATE_PROGRESS);
                Result mResult = searchNewBooks();
                setState(STATE_COMPLETE);
                setResult(mResult);
            }
        }).start();
    }


    @Override
    protected void onCleared() {
        if (D) Log.d(TAG, "onCleared()");
        setState(STATE_NONE);
    }


    private Result searchNewBooks() {
        List<String> keywords = KeywordRepository.getInstance(context).getKeywordList();
        if (keywords.size() == 0) {
            return Result.Error(Result.ERROR_CODE_EMPTY_KEYWORDS, context.getString(R.string.message_error_empty_keyword));
        }
        int size = keywords.size();
        int count = 0;
        String message;
        String value;

        List<Item> books = new ArrayList<>();
        for (String keyword : keywords) {
            if (state != STATE_PROGRESS) {
                return Result.Error(Result.ERROR_CODE_RELOAD_CANCELED, context.getString(R.string.message_error_canceled));
            }
            if (D) Log.d(TAG, "keyword : " + keyword);
            count++;
            message = keyword;
            value = count + "/" + size;
            setMessage(message);
            setProgress(value);
            List<Item> newBooks = BookRepository.getInstance().searchNewBooks(keyword);
            books.addAll(newBooks);
        }
        if (books.size() > 0) {
            mApp.getDatabase().bookDao().replaceAll(books);
            return Result.Success();
        } else {
            return Result.Error(Result.ERROR_CODE_IO_EXCEPTION, context.getString(R.string.message_error_no_books));
        }
    }

}
