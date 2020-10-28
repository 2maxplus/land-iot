package com.hyf.iot.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;

import com.hyf.iot.R;
import com.hyf.iot.ui.activity.MainActivity;


public class NotificationUtils extends ContextWrapper {

    private NotificationManager manager;
    public static final String id = "channel_1";
    public static final String name = "channel_name_1";
    public Notification notification;
    private Context mContext;

    public NotificationUtils(Context context) {
        super(context);
        mContext = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createNotificationChannel() {
        NotificationChannel channel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH);
        channel.enableVibration(true);
        getManager().createNotificationChannel(channel);
    }

    private NotificationManager getManager() {
        if (manager == null) {
            manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        }
        return manager;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Notification.Builder getChannelNotification(String title, String content, String content_type) {
        Intent intent = new Intent();
//        if(content_type.equals("1")){
//            Bundle bundle = new Bundle();
//            bundle.putString(KEY_TITLE,"进度查询");
//            bundle.putString(KEY_TYPE,"");
//            intent.putExtra("bundle",bundle);
//            intent.setClass(mContext, WorkOrderScheduleActivity.class);
//        }else{
//            Bundle bundle = new Bundle();
//            bundle.putString(KEY_TITLE,"我的消息");
//            bundle.putString(KEY_TYPE,"2");
//            intent.putExtra("bundle",bundle);
//            intent.setClass(mContext,MessageListActivity.class);
//        }
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        return new Notification.Builder(getApplicationContext(), id)
                .setContentTitle(title)
                .setContentText(content)
                .setContentIntent(pendingIntent)
                .setSmallIcon(android.R.drawable.stat_notify_more)
                .setAutoCancel(true);
    }

    public Notification.Builder getNotification_25(String title, String content, String content_type) {
        Intent intent = new Intent();
        if(content_type.equals("1")){
            Bundle bundle = new Bundle();
//            bundle.putString(KEY_TITLE,"进度查询");
//            bundle.putString(KEY_TYPE,"");
            intent.putExtra("bundle",bundle);
            intent.setClass(mContext, MainActivity.class);
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        return new Notification.Builder(this).setTicker(title)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_launcher).setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher))
                .setContentText(content)
                .setAutoCancel(true)
                .setContentTitle(title);

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void sendNotification(String title, String content,String content_type) {
        if (Build.VERSION.SDK_INT >= 26) {
            createNotificationChannel();
            this.notification = getChannelNotification
                    (title, content, content_type).build();
            getManager().notify(1, notification);
        } else {
            this.notification = getNotification_25(title, content, content_type).build();
            getManager().notify(1, notification);
        }
    }


    public Notification getNotification() {

        return this.notification;
    }
}