package jp.gr.java_conf.nuranimation.new_book_search.ui.new_book;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import jp.gr.java_conf.nuranimation.new_book_search.ApplicationData;
import jp.gr.java_conf.nuranimation.new_book_search.model.entity.Item;

@SuppressWarnings("WeakerAccess")
public class NewBooksViewModel extends AndroidViewModel {

    private ApplicationData mApp;
    private MutableLiveData<List<Item>> mBooks;


    public NewBooksViewModel(@NonNull Application application) {
        super(application);
        Context context = application.getApplicationContext();
        mApp = (ApplicationData)context;
        if(mBooks != null) {
            Log.d("NewBooksViewModel", "mBooks" + mBooks.getValue());
        }
        mBooks = new MutableLiveData<>();
        loadAllBooks();
    }

    public LiveData<List<Item>> getBooks() {
        return mBooks;
    }

    public void loadAllBooks(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                mBooks.postValue(mApp.getDatabase().bookDao().loadAllBooks());
            }
        }).start();
    }

    @Override
    protected void onCleared() {
        Log.d("NewBooksViewModel", "onCleared()");
    }


}
