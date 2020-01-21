package jp.gr.java_conf.nuranimation.new_book_search.service;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import jp.gr.java_conf.nuranimation.new_book_search.MainActivity;
import jp.gr.java_conf.nuranimation.new_book_search.R;

class NotificationParam {

    private final String channelId;
    private final String title;
    private final String message;
    private final int iconId;
    private final PendingIntent pendingIntent;


    private NotificationParam(String channelId, String title, String message, int iconId, PendingIntent pendingIntent){
        this.channelId = channelId;
        this.title = title;
        this.message = message;
        this.iconId = iconId;
        this.pendingIntent = pendingIntent;
    }

    String getChannelId(){
        return channelId;
    }

    String getTitle(){
        return title;
    }

    String getMessage(){
        return message;
    }

    int getIconId(){
        return iconId;
    }

    PendingIntent getPendingIntent(){
        return pendingIntent;
    }



    static NotificationParam createNotificationParam(Context context){
        String channelId = context.getString(R.string.notification_channel_id);
        String title = context.getString(R.string.app_name);
        String message = context.getString(R.string.notification_channel_title);
        int iconId = R.drawable.ic_new_books_24dp;

        Intent ni = new Intent();
        ni.addCategory(Intent.CATEGORY_LAUNCHER);
        ni.setClassName(context.getApplicationContext().getPackageName(), MainActivity.class.getName());
        ni.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context.getApplicationContext(), 0, ni, PendingIntent.FLAG_CANCEL_CURRENT);

        return new NotificationParam(channelId,title,message,iconId,pendingIntent);
    }

}
