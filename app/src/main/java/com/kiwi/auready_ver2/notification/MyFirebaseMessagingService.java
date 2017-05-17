/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kiwi.auready_ver2.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.kiwi.auready_ver2.Injection;
import com.kiwi.auready_ver2.R;
import com.kiwi.auready_ver2.UseCase;
import com.kiwi.auready_ver2.UseCaseHandler;
import com.kiwi.auready_ver2.data.Notification;
import com.kiwi.auready_ver2.notification.domain.usecase.SaveNotification;
import com.kiwi.auready_ver2.taskheads.TaskHeadsActivity;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    /**
     * Called when mMessage is received.
     *
     * @param remoteMessage Object representing the mMessage received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if mMessage contains a data payload, a notification payload.
        if (remoteMessage.getData().size() > 0) {
            Map<String, String> data = remoteMessage.getData();
            Log.d(TAG, "Message data payload: " + data);

            sendNotification(data.get(Notification.NOTI_TITLE),
                    data.get(Notification.NOTI_BODY));

//            saveNotification(data);
        }
        // Also if you intend on generating your own notifications as a result of a received FCM
        // mMessage, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received FCM mMessage.
     *
     * @param title
     * @param body FCM mMessage body received.
     */
    private void sendNotification(String title, String body) {
        Intent intent = new Intent(this, TaskHeadsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_stat_ic_notification)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    /*
    * Save new notification mMessage to Local storage
    * and update Notification UI
    * */
    private void saveNotification(Map<String, String> data) {
        // Data type = notification type
        String dataType = data.get(Notification.TYPE);
        String fromUserId = data.get(Notification.FROM_USERID);
        String fromUserName = data.get(Notification.FROM_USERNAME);
        String messageBody = data.get(Notification.NOTI_BODY);

        final Notification notification = new Notification(dataType, fromUserId, fromUserName, messageBody);
        // Save to local repository
        // Todo ; needs refactoring
        UseCaseHandler useCaseHandler = Injection.provideUseCaseHandler();
        SaveNotification saveNotification = Injection.provideSaveNotification(getApplicationContext());
        useCaseHandler.execute(saveNotification, new SaveNotification.RequestValues(notification),
                new UseCase.UseCaseCallback<SaveNotification.ResponseValue>() {

                    @Override
                    public void onSuccess(SaveNotification.ResponseValue response) {
                        Log.d(TAG, "saved notification - " + notification.toString());
                    }

                    @Override
                    public void onError() {
                        Log.d(TAG, "Fail to save a new notification.");
                    }
                });
    }


}
