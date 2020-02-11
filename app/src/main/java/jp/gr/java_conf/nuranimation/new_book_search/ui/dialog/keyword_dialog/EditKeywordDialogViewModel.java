package jp.gr.java_conf.nuranimation.new_book_search.ui.dialog.keyword_dialog;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import jp.gr.java_conf.nuranimation.new_book_search.model.repository.KeywordRepository;

@SuppressWarnings("WeakerAccess")
public class EditKeywordDialogViewModel extends AndroidViewModel {
    private static final String TAG = EditKeywordDialogViewModel.class.getSimpleName();
    private static final boolean D = true;

    private Context context;


    private MutableLiveData<String> errorMessage;
    private MutableLiveData<String> tmpKeyword;
    private MutableLiveData<List<String>> keywordList;

    public EditKeywordDialogViewModel(@NonNull Application application) {
        super(application);
        if (D) Log.d(TAG, "EditKeywordDialogViewModel()");
        context = application.getApplicationContext();
        errorMessage = new MutableLiveData<>();
        tmpKeyword = new MutableLiveData<>();
        keywordList = new MutableLiveData<>();
        loadKeywordList();
    }

    public MutableLiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage.postValue(errorMessage);
    }

    public MutableLiveData<String> getTmpKeyword() {
        return tmpKeyword;
    }

    public void setTmpKeyword(String tmpKeyword) {
        this.tmpKeyword.postValue(tmpKeyword);
    }

    public MutableLiveData<List<String>> getKeywordList(){
        return keywordList;
    }

    public void loadKeywordList(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<String> keywords = KeywordRepository.getInstance().loadKeywordList(context);
                keywordList.postValue(keywords);
            }
        }).start();
    }


}
