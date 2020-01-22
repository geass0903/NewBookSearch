package jp.gr.java_conf.nuranimation.new_book_search.ui.dialog;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ProgressDialogViewModel extends ViewModel {

    private MutableLiveData<String> title;
    private MutableLiveData<String> message;
    private MutableLiveData<String> progress;

    public ProgressDialogViewModel() {
        title = new MutableLiveData<>();
        message = new MutableLiveData<>();
        progress = new MutableLiveData<>();
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

    public void setTitle(String title) {
        this.title.postValue(title);
    }

    public void setMessage(String message) {
        this.message.postValue(message);
    }

    public void setProgress(String progress) {
        Log.d("ProgressDialogViewModel", "setProgress" + progress);
        this.progress.postValue(progress);
    }

}
