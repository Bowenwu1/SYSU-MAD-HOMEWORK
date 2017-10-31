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
        builder.setContentTitle(context.getResources().getString(R.string.new_product_on_sale))
                .setContentText((String)bundle.get(ProductManagement.product_name)+context.getResources().getString(R.string.only_sale)+(String)bundle.get(ProductManagement.product_price))
                .setTicker(context.getResources().getString(R.string.you_have_new_message))
                .setSmallIcon((int)bundle.get(ProductManagement.image))
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), (int)bundle.get(ProductManagement.image)))
                .setContentIntent(pi);
        NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notify = builder.build();
        manager.notify((int)System.currentTimeMillis(), notify);
    }
}
