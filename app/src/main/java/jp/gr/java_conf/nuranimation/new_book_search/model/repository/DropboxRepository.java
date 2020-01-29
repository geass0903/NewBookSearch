package jp.gr.java_conf.nuranimation.new_book_search.model.repository;

import android.content.Context;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.WriteMode;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import jp.gr.java_conf.nuranimation.new_book_search.ApplicationData;

public class DropboxRepository {

    private static DropboxRepository dropboxRepository;

    private static final String APP_NAME = "NewBookSearch";
    private static final String CLIENT_IDENTIFIER = APP_NAME + File.separator + "1.0";
    private static final String DROPBOX_APP_DIR_PATH = File.separator + APP_NAME + File.separator;

    private DropboxRepository(){
    }


    public synchronized static DropboxRepository getInstance(){
        if(dropboxRepository == null){
            dropboxRepository = new DropboxRepository();
        }
        return dropboxRepository;
    }


    public void upload(Context context, File file) throws DbxException, IOException {
        ApplicationData app = (ApplicationData)context.getApplicationContext();
        String token = app.getPreferences().getString(ApplicationData.KEY_ACCESS_TOKEN, null);
        if(token == null){
            throw new DbxException("No token");
        }
        DbxRequestConfig config = new DbxRequestConfig(CLIENT_IDENTIFIER);
        DbxClientV2 client = new DbxClientV2(config, token);
        InputStream input = new FileInputStream(file);
        client.files().uploadBuilder(DROPBOX_APP_DIR_PATH + file.getName()).withMode(WriteMode.OVERWRITE).uploadAndFinish(input);
    }

    public List<String> download(){
        return null;
    }

}
