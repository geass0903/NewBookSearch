package jp.gr.java_conf.nuranimation.new_book_search.model.repository;

import android.content.Context;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.files.SearchMatchV2;
import com.dropbox.core.v2.files.SearchV2Result;
import com.dropbox.core.v2.files.WriteMode;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import jp.gr.java_conf.nuranimation.new_book_search.ApplicationData;

public class DropboxRepository {

    private static DropboxRepository dropboxRepository;

    private static final String APP_NAME = "NewBookSearch";
    private static final String CLIENT_IDENTIFIER = APP_NAME + File.separator + "1.0";
    private static final String DROPBOX_APP_DIR_PATH = File.separator + APP_NAME + File.separator;

    private DropboxRepository() {
    }

    public synchronized static DropboxRepository getInstance() {
        if (dropboxRepository == null) {
            dropboxRepository = new DropboxRepository();
        }
        return dropboxRepository;
    }

    public void upload(Context context, File file) throws DbxException, IOException {
        ApplicationData app = (ApplicationData) context.getApplicationContext();
        String token = app.getPreferences().getString(ApplicationData.KEY_ACCESS_TOKEN, null);
        if (token == null) {
            throw new DbxException("No token");
        }
        DbxRequestConfig config = new DbxRequestConfig(CLIENT_IDENTIFIER);
        DbxClientV2 client = new DbxClientV2(config, token);
        InputStream input = new FileInputStream(file);
        client.files().uploadBuilder(DROPBOX_APP_DIR_PATH + file.getName()).withMode(WriteMode.OVERWRITE).uploadAndFinish(input);
    }

    public void download(Context context, File file) throws DbxException, IOException {
        ApplicationData app = (ApplicationData) context.getApplicationContext();
        String token = app.getPreferences().getString(ApplicationData.KEY_ACCESS_TOKEN, null);
        if (token == null) {
            throw new DbxException("No token");
        }
        DbxRequestConfig config = new DbxRequestConfig(CLIENT_IDENTIFIER);
        DbxClientV2 client = new DbxClientV2(config, token);

        Metadata metadata = getMetadata(client, file.getName());
        if (metadata == null) {
            throw new DbxException("metadata not found");
        }
        OutputStream output = new FileOutputStream(file);
        client.files().download(metadata.getPathLower()).download(output);
    }

    private static Metadata getMetadata(DbxClientV2 client, final String file_name) throws DbxException {
        SearchV2Result searchV2Result = client.files().searchV2(file_name);
        List<SearchMatchV2> matches = searchV2Result.getMatches();
        for (SearchMatchV2 match : matches) {
            Metadata metadata = match.getMetadata().getMetadataValue();
            if (metadata.getPathLower().equalsIgnoreCase(DROPBOX_APP_DIR_PATH + file_name)) {
                return metadata;
            }
        }
        return null;
    }

}
