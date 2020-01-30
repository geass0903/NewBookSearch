package jp.gr.java_conf.nuranimation.new_book_search.ui.jan_code_dialog;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class JanCodeDialogViewModel extends ViewModel {
    private static final String TAG = JanCodeDialogViewModel.class.getSimpleName();
    private static final boolean D = true;

    private MutableLiveData<String> isbn;
    private MutableLiveData<String> title;


    public JanCodeDialogViewModel(){
        isbn = new MutableLiveData<>();
        title = new MutableLiveData<>();
    }

    @Override
    protected void onCleared() {
        if (D) Log.d(TAG, "onCleared()");
    }


    public MutableLiveData<String> getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn.postValue(isbn);
    }

    public MutableLiveData<String> getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title.postValue(title);
    }
}
