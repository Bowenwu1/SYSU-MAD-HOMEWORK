package com.bowenwu.simpleshopping;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;

/**
 * Created by bowenwu on 2017/10/29.
 */

public class StaticReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = new Bundle(intent.getExtras());
        Notification.Builder builder = new Notification.Builder(context);
        Intent[] i = {new Intent(context, DetailActivity.class).putExtras(bundle)};
        PendingIntent pi = PendingIntent.getActivities(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentTitle("新商品热卖")
                .setContentText((String)bundle.get("product_name")+"仅售"+(String)bundle.get("product_price"))
                .setTicker("您有一条新消息")
                .setSmallIcon((int)bundle.get("image_rid"))
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), (int)bundle.get("image_rid")))
                .setContentIntent(pi);
        NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notify = builder.build();
        manager.notify((int)System.currentTimeMillis(), notify);
    }
}
