package com.bowenwu.simpleshopping;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
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

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.m_widght);
        switch (mWidght.status) {
            case 0:
                views.setTextViewText(R.id.appwidget_text, widgetText);
                views.setImageViewResource(R.id.product_preview, R.mipmap.borggreve);
                Intent[] i = {new Intent(context, MainActivity.class)};
                PendingIntent pi = PendingIntent.getActivities(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
                views.setOnClickPendingIntent(R.id.widght, pi);
                break;
            case 1:

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
}

