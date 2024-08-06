package com.example.notificationreader;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.TextView;
import com.example.notificationreader.adapter.RecyclerViewAdapter;
import com.example.notificationreader.data.MyDbHandler;
import com.example.notificationreader.model.Notification;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
public class MainActivity extends AppCompatActivity {
MyDbHandler db;
private RecyclerView recyclerView;
private RecyclerViewAdapter recyclerViewAdapter;
private ArrayList<Notification> notificationArrayList;
private TextView datetime;
@Override
protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setContentView(R.layout.activity_main);
datetime = findViewById(R.id.serverDateTime);
db = new MyDbHandler(MainActivity.this);
// StrictMode.ThreadPolicy policy = new
StrictMode.ThreadPolicy.Builder().permitAll().build();
// StrictMode.setThreadPolicy(policy);
recyclerView = findViewById(R.id.recyclerView);
recyclerView.setHasFixedSize(true);
recyclerView.setLayoutManager(new LinearLayoutManager(this));
notificationArrayList = new ArrayList<>();
List<Notification> notificationList = null;
try {
notificationList = db.getAllNotifications();
} catch (Exception e) {
throw new RuntimeException(e);
}
for(Notification notif: notificationList){
notificationArrayList.add(notif);
}
//Using recyclerview
recyclerViewAdapter = new RecyclerViewAdapter(MainActivity.this,
notificationArrayList);
recyclerView.setAdapter(recyclerViewAdapter);
if(!isNotificationEnable()){
startActivity(new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS));
}
IntentFilter filter = new IntentFilter("notify");
LocalBroadcastManager.getInstance(this).registerReceiver(onNotify, filter);
}
private final BroadcastReceiver onNotify = new BroadcastReceiver() {
@Override
public void onReceive(Context context, Intent intent) {
String packageName = intent.getStringExtra("package");
String date = intent.getStringExtra("date");
notificationArrayList = new ArrayList<>();
List<Notification> notificationList = null;
try {
notificationList = db.getAllNotifications();
} catch (Exception e) {
throw new RuntimeException(e);
}
for(Notification notif: notificationList){
notificationArrayList.add(notif);
}
//Using recyclerview
recyclerViewAdapter = new RecyclerViewAdapter(MainActivity.this,
notificationArrayList);
recyclerView.setAdapter(recyclerViewAdapter);
}
};
private boolean isNotificationEnable(){
NotificationManager n = (NotificationManager)
getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
boolean isEnabled = n.isNotificationPolicyAccessGranted();
if(isEnabled){
return true;
}else{
// Log.e("Msg", "no permission");
return false;
}
}
@Override
protected void onStart() {
super.onStart();
String fetchDateTime = ServerTime.fetchServerTime(this);
datetime.setText(fetchDateTime);
}
}
