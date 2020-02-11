package jp.gr.java_conf.nuranimation.new_book_search.model.usecase;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

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

public class DropboxApi {

    private static DropboxApi dropboxApi;

    private static final String KEY_ACCESS_TOKEN = "DropboxApi.KEY_ACCESS_TOKEN";
    private static final String APP_NAME = "NewBookSearch";
    private static final String CLIENT_IDENTIFIER = APP_NAME + File.separator + "1.0";
    private static final String DROPBOX_APP_DIR_PATH = File.separator + APP_NAME + File.separator;

    private DropboxApi() {
    }

    public synchronized static DropboxApi getInstance() {
        if (dropboxApi == null) {
            dropboxApi = new DropboxApi();
        }
        return dropboxApi;
    }

    public String getAccessToken(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        return sharedPreferences.getString(KEY_ACCESS_TOKEN, null);
    }

    public void setAccessToken(Context context, String token){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        sharedPreferences.edit().putString(KEY_ACCESS_TOKEN, token).apply();
    }

    public void deleteAccessToken(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        sharedPreferences.edit().remove(KEY_ACCESS_TOKEN).apply();
    }

    public void upload(String token, File file) throws DbxException, IOException {
        if (token == null) {
            throw new DbxException("No token");
        }
        DbxRequestConfig config = new DbxRequestConfig(CLIENT_IDENTIFIER);
        DbxClientV2 client = new DbxClientV2(config, token);
        InputStream input = new FileInputStream(file);
        client.files().uploadBuilder(DROPBOX_APP_DIR_PATH + file.getName()).withMode(WriteMode.OVERWRITE).uploadAndFinish(input);
    }

    public void download(String token, File file) throws DbxException, IOException {
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
