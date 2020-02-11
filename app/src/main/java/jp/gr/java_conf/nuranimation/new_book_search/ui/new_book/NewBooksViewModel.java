package jp.gr.java_conf.nuranimation.new_book_search.ui.new_book;

import android.app.Application;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import jp.gr.java_conf.nuranimation.new_book_search.model.entity.Item;
import jp.gr.java_conf.nuranimation.new_book_search.model.repository.BookRepository;

@SuppressWarnings("WeakerAccess")
public class NewBooksViewModel extends AndroidViewModel {

    private Context context;
    private MutableLiveData<List<Item>> items;


    public NewBooksViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
        items = new MutableLiveData<>();
        loadAllBooks();
    }

    public LiveData<List<Item>> getBooks() {
        return items;
    }

    public void loadAllBooks(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                items.postValue(BookRepository.getInstance().loadAllBooks(context));
            }
        }).start();
    }


}
