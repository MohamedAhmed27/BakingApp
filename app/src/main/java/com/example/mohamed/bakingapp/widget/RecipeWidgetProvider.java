package com.example.mohamed.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.example.mohamed.bakingapp.R;

/**
 * Created by Mohamed on 8/25/2018.
 */

public class RecipeWidgetProvider  extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                 int appWidgetId,String ingredients,String recName) {


        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_steps);
        views.setTextViewText(R.id.ingredients_widget_text,ingredients);
        views.setTextViewText(R.id.recipe_name_widget,recName);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

    }
    public static void updateWidget(Context context, AppWidgetManager appWidgetManager,
                                          int[] appWidgetIds, String ingredients,String recName) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId,ingredients,recName);
        }
    }



    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // Perform any action when one or more AppWidget instances have been deleted
    }

    @Override
    public void onEnabled(Context context) {
        // Perform any action when an AppWidget for this provider is instantiated
    }

    @Override
    public void onDisabled(Context context) {
        // Perform any action when the last AppWidget instance for this provider is deleted
    }

}