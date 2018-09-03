package main_activity;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import java.util.Random;


import database_vocabulary.VocabularyDatabase;
import database_vocabulary.VocabularyDatabaseColumnNames;
import lessons_vocabulary_list.LessonsVocabularyList;
import pl.flanelowapopijava.duel_with_english.R;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        VocabularyDatabase dbInstance = VocabularyDatabase.getInstance(context);
        Cursor cursor = dbInstance.getAllValues();
        Random random = new Random();
        cursor.moveToPosition(random.nextInt(cursor.getCount()-1));
        intent = new Intent(context, LessonsVocabularyList.class);
        intent.putExtra("levelLanguage", cursor.getString(VocabularyDatabaseColumnNames.lvlColumn));
        intent.putExtra("categoryName", cursor.getString(VocabularyDatabaseColumnNames.categoryColumn));
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1234, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context).setContentIntent(pendingIntent);
        notificationBuilder.setContentText(cursor.getString(VocabularyDatabaseColumnNames.plwordColumn) + " - " + cursor.getString(VocabularyDatabaseColumnNames.enwordColumn));
        notificationBuilder.setSubText("Słowo dnia - " + cursor.getString(VocabularyDatabaseColumnNames.categoryColumn));
        cursor.close();
        dbInstance.close();
        notificationBuilder.setVibrate(new long[] {0, 1000, 500, 1000});
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        notificationBuilder.setSound(soundUri);
        notificationBuilder.setSmallIcon(R.drawable.ic_notifications_black_24dp);
        notificationBuilder.setAutoCancel(true);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(111,notificationBuilder.build());
    }
}
