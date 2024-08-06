package com.example.notificationreader;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.speech.tts.TextToSpeech;
17
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.example.notificationreader.data.MyDbHandler;
import com.example.notificationreader.model.Notification;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Locale;
import java.util.concurrent.ConcurrentLinkedQueue;
public class MyNotificationListenerService extends NotificationListenerService {
Context context;
String title="", text="";
private TextToSpeech tts;
private HashSet<String> processedNotificationIds = new HashSet<>();
private ConcurrentLinkedQueue<StatusBarNotification> notificationQueue;
@Override
public void onCreate() {
super.onCreate();
context = getApplicationContext();
initializeTextToSpeech();
notificationQueue = new ConcurrentLinkedQueue<>();
}
private void initializeTextToSpeech(){
tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
@Override
public void onInit(int status) {
if(status == TextToSpeech.SUCCESS){
int result = tts.setLanguage(Locale.getDefault());
if (result == TextToSpeech.LANG_MISSING_DATA || result ==
TextToSpeech.LANG_NOT_SUPPORTED) {
// Log.e("TTS", "Language not supported");
}
}
else {
// Log.e("TTS", "Initialization failed");
}
}
});
tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
@Override
public void onStart(String utteranceId) {
}
@Override
public void onDone(String utteranceId) {
if (!notificationQueue.isEmpty()) {
try {
readNextNotification();
} catch (IllegalAccessException e) {
throw new RuntimeException(e);
} catch (Exception e) {
throw new RuntimeException(e);
}
18
}
}
@Override
public void onError(String utteranceId) {
}
});
}
@Override
public void onNotificationPosted(StatusBarNotification sbn) {
String notificationId = getNotificationId(sbn);
if (processedNotificationIds.contains(notificationId)) {
return;
}
processedNotificationIds.add(notificationId);
notificationQueue.offer(sbn);
if(notificationQueue.size() == 1){
try {
readNextNotification();
} catch (IllegalAccessException e) {
throw new RuntimeException(e);
} catch (Exception e) {
throw new RuntimeException(e);
}
}
}
private void readNextNotification() throws Exception {
StatusBarNotification currentNotification = notificationQueue.poll();
if(currentNotification != null) {
String packageName = currentNotification.getPackageName();
Bundle extras = currentNotification.getNotification().extras;
if (extras.getString("android.title") != null) {
title = extras.getString("android.title");
} else {
title = "";
}
if (extras.getCharSequence("android.text") != null) {
text = extras.getCharSequence("android.text").toString();
} else {
text = "";
}
LocalDateTime notificationDate = null;
String formattedNotificationDate = "";
DateTimeFormatter myFormatObj = null;
notificationDate = LocalDateTime.now();
myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
formattedNotificationDate = notificationDate.format(myFormatObj);
Intent notify = new Intent("notify");
notify.putExtra("package", packageName);
notify.putExtra("date", formattedNotificationDate);
MyDbHandler db = new MyDbHandler(context);
19
Notification notification = new Notification();
notification.setPackageName(packageName);
notification.setTitleName(title);
notification.setTextData(text);
notification.setNotifyDate(formattedNotificationDate); 
db.addNotification(notification);
LocalBroadcastManager.getInstance(context).sendBroadcast(notify);
//read out notification
if (tts != null) {
tts.speak("Notification from " + packageName + " ." + title ,
TextToSpeech.QUEUE_ADD, null, "notification");
}
}
}
@Override
public void onNotificationRemoved(StatusBarNotification sbn) {
String notificationId = getNotificationId(sbn);
processedNotificationIds.remove(notificationId);
// Remove the processed notification from the queue
notificationQueue.poll();
// Log.d("Msg", "Notification Removed");
}
private String getNotificationId(StatusBarNotification sbn) {
return sbn.getPackageName() + sbn.getId();
}
@Override
public void onDestroy(){
super.onDestroy();
if(tts != null){
tts.stop();
tts.shutdown();
}
}
}