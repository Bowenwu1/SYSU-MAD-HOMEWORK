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

public class DynamicReveiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = new Bundle(intent.getExtras());
        Notification.Builder builder = new Notification.Builder(context);
        Intent[] i = {new Intent(context, ShoppingCarActivity.class)};
        PendingIntent pi = PendingIntent.getActivities(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentTitle("马上下单")
                .setContentText((String)bundle.get("product_name")+"已添加到购物车")
                .setTicker("您有一条新消息")
                .setSmallIcon((int)bundle.get("image_rid"))
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), (int)bundle.get("image_rid")))
                .setContentIntent(pi);
        NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notify = builder.build();
        manager.notify((int)System.currentTimeMillis(), notify);
    }
}
