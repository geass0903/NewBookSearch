package jp.gr.java_conf.nuranimation.new_book_search.ui.new_book;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import jp.gr.java_conf.nuranimation.new_book_search.model.database.AppDatabase;
import jp.gr.java_conf.nuranimation.new_book_search.model.entity.Item;
import jp.gr.java_conf.nuranimation.new_book_search.model.repository.BookRepository;

@SuppressWarnings("WeakerAccess")
public class NewBooksViewModel extends AndroidViewModel {

    private Context context;
    private MutableLiveData<List<Item>> mBooks;


    public NewBooksViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
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
                mBooks.postValue(BookRepository.getInstance().loadAllBooks(context));
            }
        }).start();
    }

    @Override
    protected void onCleared() {
        Log.d("NewBooksViewModel", "onCleared()");
    }


}
