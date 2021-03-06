package jp.gr.java_conf.nuranimation.new_book_search;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;

import java.io.File;

import jp.gr.java_conf.nuranimation.new_book_search.model.database.AppDatabase;

public class ApplicationData extends Application {
    private static final String TAG = ApplicationData.class.getSimpleName();
    private static final boolean D = true;

    public static final String KEY_ACCESS_TOKEN = "ApplicationData.KEY_ACCESS_TOKEN";

    private static final String LARGE_IMAGE_CACHE_DIR_NAME = "largeImageCache";
    private static final String SMALL_IMAGE_CACHE_DIR_NAME = "smallImageCache";

    private AppDatabase database;
    private SharedPreferences preferences;


    @Override
    public void onCreate() {
        super.onCreate();
        if (D) Log.d(TAG, "onCreate");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(getApplicationContext(),getString(R.string.notification_channel_id), R.string.notification_channel_title, R.string.notification_channel_description);
        }

        File cacheDir = getCacheDir();
        DiskCacheConfig largeImageCacheConfig = DiskCacheConfig.newBuilder(this)
                .setBaseDirectoryName(LARGE_IMAGE_CACHE_DIR_NAME)
                .setBaseDirectoryPath(cacheDir)
                .build();
        DiskCacheConfig smallImageCacheConfig = DiskCacheConfig.newBuilder(this)
                .setBaseDirectoryName(SMALL_IMAGE_CACHE_DIR_NAME)
                .setBaseDirectoryPath(cacheDir)
                .build();
        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(this)
                .setMainDiskCacheConfig(largeImageCacheConfig)
                .setSmallImageDiskCacheConfig(smallImageCacheConfig)
                .build();
        Fresco.initialize(this, config);

        database = AppDatabase.getInstance(getApplicationContext());
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    }

    @Override
    public void onTerminate(){
        super.onTerminate();
        if(D) Log.d(TAG,"onTerminate");
    }

    public AppDatabase getDatabase(){
        return database;
    }

    public SharedPreferences getPreferences() {
        return preferences;
    }

    @SuppressWarnings({"SameParameterValue"})
    @RequiresApi(Build.VERSION_CODES.O)
    private static void createNotificationChannel(Context context, String channelId, int titleResId, int descriptionResId) {
        String title = context.getString(titleResId);
        String description = context.getString(descriptionResId);

        NotificationChannel channel = new NotificationChannel(channelId, title, NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription(description);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);
    }



}
