package jp.gr.java_conf.nuranimation.new_book_search.service;

import android.content.Context;
import android.util.Log;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.WriteMode;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;

import jp.gr.java_conf.nuranimation.new_book_search.ApplicationData;
import jp.gr.java_conf.nuranimation.new_book_search.model.entity.Keyword;
import jp.gr.java_conf.nuranimation.new_book_search.model.entity.Result;
import jp.gr.java_conf.nuranimation.new_book_search.model.utils.NewBookUtils;

public class DropboxThread extends BaseThread {
    private static final String TAG = DropboxThread.class.getSimpleName();
    private static final boolean D = true;

    public static final int TYPE_BACKUP  = 1;
    public static final int TYPE_RESTORE = 2;

    private static final String APP_NAME = "NewBookSearch";

    private static final String CLIENT_IDENTIFIER = APP_NAME + File.separator + "1.0";
    private static final String DROPBOX_APP_DIR_PATH = File.separator + APP_NAME + File.separator;
    private static final String FILE_NAME = "backup_keyword.csv";

    private final ApplicationData app;
    private final Context context;
    private final int type;

    public DropboxThread(Context context, int type) {
        super(context);
        this.app = (ApplicationData)context.getApplicationContext();
        this.context = context;
        this.type = type;
    }

    public void run() {
        Result mResult;
        switch(type) {
            case TYPE_BACKUP:
                mResult = backup();
                break;
            case TYPE_RESTORE:
                mResult = restore();
                break;
            default:
                mResult = Result.DropboxError(0,Result.ERROR_CODE_UNKNOWN,"Type Error");
                break;
        }
        if (getThreadListener() != null && !isCanceled()) {
            getThreadListener().deliverResult(mResult);
        }
    }

    public void cancel() {
        super.cancel();
        if (D) Log.d(TAG, "thread cancel");
    }

    private Result backup() {

        File dir = context.getFilesDir();
        File file = new File(dir, FILE_NAME);

        try {
            List<Keyword> keywords = app.getDatabase().keywordDao().loadAllKeyword();
            int recodeCount = keywords.size();
            int exportCount = 0;
            String progress = exportCount + "/" + recodeCount;
            getThreadListener().deliverProgress("", progress);

            BufferedWriter writer = NewBookUtils.getBufferedWriter(new FileOutputStream(file), Charset.forName("UTF-8"));
            for (Keyword keyword : keywords) {
                if (isCanceled()) {
                    writer.flush();
                    writer.close();
                    return Result.DropboxError(TYPE_BACKUP, Result.ERROR_CODE_BACKUP_CANCELED, "Backup Canceled.");
                }
                writer.write(keyword.getWord() + "\r\n");
                exportCount++;
                progress = exportCount + "/" + recodeCount;
                getThreadListener().deliverProgress("", progress);
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            return Result.DropboxError(TYPE_BACKUP, Result.ERROR_CODE_IO_EXCEPTION, "IOException");
        }

        String token = app.getPreferences().getString(ApplicationData.KEY_ACCESS_TOKEN, null);
        if (token == null) {
            return Result.DropboxError(TYPE_BACKUP, Result.ERROR_CODE_DBX_EXCEPTION, "No token");
        }
        DbxRequestConfig config = new DbxRequestConfig(CLIENT_IDENTIFIER);
        DbxClientV2 client = new DbxClientV2(config, token);
        try {
            if (isCanceled()) {
                return Result.DropboxError(TYPE_BACKUP, Result.ERROR_CODE_BACKUP_CANCELED, "Backup Canceled");
            }
            getThreadListener().deliverProgress("", "");
            InputStream input_authors = new FileInputStream(FILE_NAME);
            client.files().uploadBuilder(DROPBOX_APP_DIR_PATH + FILE_NAME).withMode(WriteMode.OVERWRITE).uploadAndFinish(input_authors);
            return Result.DropboxSuccess(TYPE_BACKUP);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.DropboxError(TYPE_BACKUP, Result.ERROR_CODE_IO_EXCEPTION, "IOException");
        } catch (DbxException e) {
            e.printStackTrace();
            return Result.DropboxError(TYPE_BACKUP, Result.ERROR_CODE_DBX_EXCEPTION, "DbxException");
        }
    }

    private Result restore(){
        return null;
    }




}
