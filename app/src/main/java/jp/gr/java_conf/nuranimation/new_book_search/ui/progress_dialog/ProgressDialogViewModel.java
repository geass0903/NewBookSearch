package jp.gr.java_conf.nuranimation.new_book_search.ui.progress_dialog;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.dropbox.core.DbxException;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

import jp.gr.java_conf.nuranimation.new_book_search.R;
import jp.gr.java_conf.nuranimation.new_book_search.ApplicationData;
import jp.gr.java_conf.nuranimation.new_book_search.model.entity.Item;
import jp.gr.java_conf.nuranimation.new_book_search.model.entity.Result;
import jp.gr.java_conf.nuranimation.new_book_search.model.repository.BookRepository;
import jp.gr.java_conf.nuranimation.new_book_search.model.repository.DropboxRepository;
import jp.gr.java_conf.nuranimation.new_book_search.model.repository.FileRepository;
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

    private ReloadAsyncTask reloadAsyncTask;
    private BackupAsyncTask backupAsyncTask;

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
        reloadAsyncTask = null;
        backupAsyncTask = null;
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


    public void reload() {
        reloadAsyncTask = new ReloadAsyncTask(this);
        reloadAsyncTask.execute();
    }

    public void backup(){
        backupAsyncTask = new BackupAsyncTask(this);
        backupAsyncTask.execute();
    }

    public void restore(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                setState(STATE_PROGRESS);

                setState(STATE_COMPLETE);
                setResult(null);
            }
        }).start();
    }


    public void cancel(){
        if(reloadAsyncTask != null){
            reloadAsyncTask.onCancel();
        }
        if(backupAsyncTask != null){
            backupAsyncTask.onCancel();
        }
    }

    @Override
    protected void onCleared() {
        if (D) Log.d(TAG, "onCleared()");
        if(reloadAsyncTask != null){
            reloadAsyncTask.onCancel();
        }

    }



    private static class ReloadAsyncTask extends AsyncTask<Void,Void,Result>{
        private final ProgressDialogViewModel model;

        public ReloadAsyncTask(ProgressDialogViewModel model){
            this.model = model;
        }

        @Override
        protected void onPreExecute() {
            if (D) Log.d(TAG, "ReloadAsyncTask.onPreExecute()");
            model.setState(ProgressDialogViewModel.STATE_PROGRESS);
        }

        @Override
        protected Result doInBackground(Void... aVoid) {
            if (D) Log.d(TAG, "ReloadAsyncTask.doInBackground()");
            List<String> keywords = KeywordRepository.getInstance(model.context).getKeywordList();
            if (keywords.size() == 0) {
                return Result.Error(Result.ERROR_CODE_EMPTY_KEYWORDS, model.context.getString(R.string.message_error_empty_keywords));
            }
            int size = keywords.size();
            int count = 0;
            String message;
            String value;

            List<Item> books = new ArrayList<>();
            for (String keyword : keywords) {
                if(isCancelled()){
                    return Result.Error(Result.ERROR_CODE_CANCELED, model.context.getString(R.string.message_error_canceled));
                }
                if (D) Log.d(TAG, "keyword : " + keyword);
                count++;
                message = keyword;
                value = count + "/" + size;
                model.setMessage(message);
                model.setProgress(value);
                List<Item> newBooks = BookRepository.getInstance().searchNewBooks(keyword);
                books.addAll(newBooks);
            }

            if(!isCancelled()){
                model.mApp.getDatabase().bookDao().replaceAll(books);
                return Result.Success();
            }else{
                return Result.Error(Result.ERROR_CODE_CANCELED, model.context.getString(R.string.message_error_canceled));
            }
        }

        @Override
        protected void onPostExecute(Result result) {
            if (D) Log.d(TAG, "ReloadAsyncTask.onPostExecute()");
            if(result.isSuccess()) {
                Toast.makeText(model.context, model.context.getString(R.string.message_success_reload), Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(model.context, result.getErrorMessage(), Toast.LENGTH_SHORT).show();
            }
            model.setState(STATE_COMPLETE);
            model.setResult(result);
        }

        @Override
        protected void onCancelled() {
            if (D) Log.d(TAG, "ReloadAsyncTask.onCancelled()");
            model.setState(STATE_NONE);
        }

        public void onCancel(){
            cancel(true);
        }

    }


    private static class BackupAsyncTask extends AsyncTask<Void,Void,Result>{
        private static final String FILE_NAME = "backup_keyword.csv";
        private final ProgressDialogViewModel model;

        public BackupAsyncTask(ProgressDialogViewModel model){
            this.model = model;
        }

        @Override
        protected void onPreExecute() {
            if (D) Log.d(TAG, "BackupAsyncTask.onPreExecute()");
            model.setState(ProgressDialogViewModel.STATE_PROGRESS);
        }

        @Override
        protected Result doInBackground(Void... aVoid) {
            if (D) Log.d(TAG, "BackupAsyncTask.doInBackground()");
            File file = new File(model.context.getFilesDir(), FILE_NAME);
            List<String> keywords = KeywordRepository.getInstance(model.context).getKeywordList();

            try {
                FileRepository.getInstance().write(file, keywords);
                if (isCancelled()) {
                    return Result.Error(Result.ERROR_CODE_CANCELED, model.context.getString(R.string.message_error_canceled));
                }
                DropboxRepository.getInstance().upload(model.context, file);
                return Result.Success();
            } catch (IOException e) {
                e.printStackTrace();
                return Result.Error(Result.ERROR_CODE_IO_EXCEPTION, model.context.getString(R.string.message_error_io_exception));
            } catch (DbxException e) {
                e.printStackTrace();
                return Result.Error(Result.ERROR_CODE_DBX_EXCEPTION, model.context.getString(R.string.message_error_dbx_exception));
            }
        }

        @Override
        protected void onPostExecute(Result result) {
            if (D) Log.d(TAG, "BackupAsyncTask.onPostExecute()");
            if(result.isSuccess()) {
                Toast.makeText(model.context, model.context.getString(R.string.message_success_backup), Toast.LENGTH_SHORT).show();
                model.setState(STATE_COMPLETE);
                model.setResult(result);
            }else{
                Toast.makeText(model.context, result.getErrorMessage(), Toast.LENGTH_SHORT).show();
                if(result.getErrorCode() != Result.ERROR_CODE_CANCELED){
                    model.setState(STATE_COMPLETE);
                    model.setResult(result);
                }
            }
        }

        @Override
        protected void onCancelled() {
            if (D) Log.d(TAG, "BackupAsyncTask.onCancelled()");
            model.setState(STATE_NONE);
        }

        public void onCancel(){
            cancel(true);
        }

    }

}
