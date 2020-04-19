package com.example.butymovaloftcoin.job;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.example.butymovaloftcoin.R;
import com.example.butymovaloftcoin.data.extra.CompareRatesResult;
import com.example.butymovaloftcoin.screens.main.MainActivity;

import javax.inject.Inject;

import static android.content.Context.NOTIFICATION_SERVICE;
import static com.example.butymovaloftcoin.job.SyncRateJobService.CHANNEL_ID;
import static com.example.butymovaloftcoin.job.SyncRateJobService.NOTIFY_ID;
import static com.example.butymovaloftcoin.job.SyncRateJobService.REQUEST_CODE;
import static com.example.butymovaloftcoin.screens.main.MainViewImpl.KEY_OPEN_FROM_NOTIFICATION;

public class SyncRateJobRouterImpl implements SyncRateJobRouter {

    private JobService service;

    @Inject
    public SyncRateJobRouterImpl() {

    }

    @Override
    public void attach(JobService service) {
        this.service = service;
    }

    @Override
    public void jobFinished(JobParameters params, boolean wantsReschedule) {
        service.jobFinished(params, wantsReschedule);
    }

    @Override
    public void showRateChangedNotification(CompareRatesResult compareRatesResult) {
        NotificationManager manager = (NotificationManager) service.getSystemService(NOTIFICATION_SERVICE);
        if (manager==null)
            return;

        NotificationCompat.Builder builder = createBuilder(manager);
        if (builder==null)
            return;

        String text = service.getString(R.string.notification_rate_changed_body, compareRatesResult.getPriceDiffStr());
        SpannableStringBuilder spannableText = new SpannableStringBuilder(text);
        ForegroundColorSpan style = new ForegroundColorSpan(ContextCompat.getColor(service, compareRatesResult.getColor()));
        spannableText.setSpan(style, text.indexOf(compareRatesResult.getPriceDiffStr()), text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        builder.setContentTitle(compareRatesResult.getCoinEntity().name);
        builder.setContentText(spannableText);

        Notification notification = builder.build();
        manager.notify(NOTIFY_ID, notification);
    }

    @Override
    public void showRateChangedNotificationFailed(Integer errorId) {
        NotificationManager manager = (NotificationManager) service.getSystemService(NOTIFICATION_SERVICE);
        if (manager==null)
            return;

        NotificationCompat.Builder builder = createBuilder(manager);
        if (builder==null)
            return;

        String text = service.getString(errorId);
        SpannableStringBuilder spannableText = new SpannableStringBuilder(text);
        ForegroundColorSpan style = new ForegroundColorSpan(ContextCompat.getColor(service, R.color.red));
        spannableText.setSpan(style, 0, text.length()-1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        builder.setContentTitle(service.getString(R.string.notification_channel_rate_changed));
        builder.setContentText(spannableText);

        Notification notification = builder.build();
        manager.notify(NOTIFY_ID, notification);
    }

    private NotificationCompat.Builder createBuilder(NotificationManager manager){
        Intent intent = new Intent(service, MainActivity.class);
        intent.putExtra(KEY_OPEN_FROM_NOTIFICATION, true);
        PendingIntent pendingIntent = PendingIntent.getActivity (service, REQUEST_CODE, intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(service, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setLargeIcon(BitmapFactory.decodeResource(service.getResources(), R.mipmap.ic_launcher_round))
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, service.getString(R.string.notification_channel_rate_changed), NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(service.getResources().getString(R.string.notification_channel_rate_changed_description));
            manager.createNotificationChannel(channel);
        }

        return builder;
    }

    @Override
    public void detach() {
        this.service =null;
    }
}
