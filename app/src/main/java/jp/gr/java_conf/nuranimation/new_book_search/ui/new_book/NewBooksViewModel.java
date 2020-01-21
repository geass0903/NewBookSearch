package jp.gr.java_conf.nuranimation.new_book_search.ui.new_book;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import jp.gr.java_conf.nuranimation.new_book_search.ApplicationData;
import jp.gr.java_conf.nuranimation.new_book_search.model.entity.Item;


public class NewBooksViewModel extends AndroidViewModel {

    private ApplicationData mApp;
    private MutableLiveData<List<Item>> mBooks;


    public NewBooksViewModel(@NonNull Application application) {
        super(application);
        Context context = application.getApplicationContext();
        mApp = (ApplicationData)context;
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

}
