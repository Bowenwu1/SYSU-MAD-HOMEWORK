package com.bowenwu.simpleshopping;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class mWidght extends AppWidgetProvider {
    // 0 for toMain
    // 1 for product detail
    // 2 for to shopping list
    private static int status = 0;
    private static int product_id = 0;
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        System.out.println("update app widget");

        CharSequence widgetText = context.getString(R.string.no_info);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.m_widght);
        switch (mWidght.status) {
            case 0:
                views.setTextViewText(R.id.appwidget_text, widgetText);
                views.setImageViewResource(R.id.product_preview, R.mipmap.shoplist);
                Intent i = new Intent(context, MainActivity.class);
                PendingIntent pi = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
                views.setOnClickPendingIntent(R.id.widght, pi);
                break;
        }
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }


    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("mWidght onReveive : " + intent.getAction());
        System.out.println(status);
        super.onReceive(context, intent);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.m_widght);
        Bundle bundle = intent.getExtras();
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        if (intent.getAction().equals(BroadcastType.recommend)) {
            System.out.println("widght get broadcast" + (String)bundle.get(ProductManagement.product_name)+context.getResources().getString(R.string.only_sale)+(String)bundle.get(ProductManagement.product_price));
            remoteViews.setTextViewText(R.id.appwidget_text, (String)bundle.get(ProductManagement.product_name)+context.getResources().getString(R.string.only_sale)+(String)bundle.get(ProductManagement.product_price));
            remoteViews.setImageViewResource(R.id.product_preview, (int)bundle.get(ProductManagement.image));
            Intent[] i = {new Intent(context, DetailActivity.class).putExtras(bundle)};
            PendingIntent pi = PendingIntent.getActivities(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.widght, pi);
            appWidgetManager.updateAppWidget(new ComponentName(context, mWidght.class), remoteViews);
            // status = 1;
        } else if (intent.getAction().equals(BroadcastType.buy_now)) {
            remoteViews.setTextViewText(R.id.appwidget_text, (String)bundle.get(ProductManagement.product_name)+context.getResources().getString(R.string.already_in_shopping_car));
            remoteViews.setImageViewResource(R.id.product_preview, (int)bundle.get(ProductManagement.image));
            Intent i = new Intent(context, ShoppingCarActivity.class);
            PendingIntent pi = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.widght, pi);
            appWidgetManager.updateAppWidget(new ComponentName(context, mWidght.class), remoteViews);
            // status = 2;
        }
    }
}

