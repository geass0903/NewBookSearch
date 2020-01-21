package jp.gr.java_conf.nuranimation.new_book_search.service;

import android.app.Notification;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import java.util.ArrayList;
import java.util.List;

import jp.gr.java_conf.nuranimation.new_book_search.model.entity.Result;

public class NewBookService extends BaseService implements ReloadThread.ThreadListener{
    private static final String TAG = NewBookService.class.getSimpleName();
    private static final boolean D = true;

    public static final int STATE_NONE                      =  0;
    public static final int STATE_BACKGROUND_INCOMPLETE     =  1;
    public static final int STATE_BACKGROUND_COMPLETE       =  2;

    private ReloadThread newBooksThread;

    private Result mResult;


    public class MBinder extends Binder {
        public NewBookService getService() {
            return NewBookService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        if (D) Log.d(TAG, "onBind");
        return new MBinder();
    }

    @Override
    public void onRebind(Intent intent) {
        if (D) Log.d(TAG, "onRebind");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        if (D) Log.d(TAG, "onUnbind");
        return true;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (D) Log.d(TAG, "onCreate");
    }

    @Override
    public void onDestroy() {
        if (D) Log.d(TAG, "onDestroy");
        if(getServiceState() == STATE_BACKGROUND_INCOMPLETE){
            if(newBooksThread != null){
                newBooksThread.cancel();
                newBooksThread = null;
            }
        }
    }

    @Override
    public void deliverResult(Result result) {
        mResult = result;
        if(getServiceState() == STATE_BACKGROUND_INCOMPLETE){
            setServiceState(STATE_BACKGROUND_COMPLETE);
        }
    }

    @Override
    public void deliverProgress(String message, String value) {
        updateProgress(message, value);
    }

    public Result getResult() {
        if (mResult == null) {
            return Result.Error("get result failed");
        }else{
            Result result = Result.DeepCopy(mResult);
            mResult = null;
            return result;
        }
    }

    public void startReloadNewBooks() {
        setServiceState(STATE_BACKGROUND_INCOMPLETE);
        newBooksThread = new ReloadThread(this);
        newBooksThread.start();
    }

    public void stopReloadNewBooks() {
        if (newBooksThread != null) {
            newBooksThread.cancel();
            newBooksThread = null;
        }
        setServiceState(STATE_NONE);
    }

    protected Notification createNotification(int state) {
        Notification notification;
        NotificationParam notificationParam = NotificationParam.createNotificationParam(this);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notification = new Notification.Builder(getApplicationContext(), notificationParam.getChannelId())
                    .setContentTitle(notificationParam.getTitle())
                    .setSmallIcon(notificationParam.getIconId())
                    .setContentText(notificationParam.getMessage())
                    .setContentIntent(notificationParam.getPendingIntent())
                    .build();
        } else {
            notification = new NotificationCompat.Builder(this, notificationParam.getChannelId())
                    .setContentTitle(notificationParam.getTitle())
                    .setSmallIcon(notificationParam.getIconId())
                    .setContentText(notificationParam.getMessage())
                    .setContentIntent(notificationParam.getPendingIntent())
                    .build();
        }
        return notification;
    }

}
