package com.example.notificationreader.adapter;
import com.example.notificationreader.R;
import com.example.notificationreader.data.MyDbHandler;
import java.util.Locale;
public class DisplayNotification extends AppCompatActivity {
String packageName = "";
String title = "";
String text = "";
String date = "";
int id;
private TextToSpeech tts;
MyDbHandler db;
@Override
protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setContentView(R.layout.activity_display_notification);
db = new MyDbHandler(DisplayNotification.this);
26
Intent intent = getIntent();
packageName = intent.getStringExtra("RpackageName");
title = intent.getStringExtra("Rtitle");
text = intent.getStringExtra("Rtext");
date = intent.getStringExtra("Rdate");
id = intent.getIntExtra("Rid", 0);
TextView dateTextView = findViewById(R.id.displayDateTime);
dateTextView.setText(date);
TextView packageTextView = findViewById(R.id.displayPackageName);
packageTextView.setText(packageName);
TextView titleTextView = findViewById(R.id.displayTitleName);
titleTextView.setText(title);
TextView textTextView = findViewById(R.id.displayTextData);
textTextView.setText(text);
initializeTextToSpeech();
}
public void read(View view){
if (tts != null) {
tts.speak("Notification at " + date + " from " + packageName + " " + title + " "
+ text + " " , TextToSpeech.QUEUE_FLUSH, null, "notification");
}
}
public void deleteNotification(View view){
db.deleteNotification(id);
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
27
@Override
public void onDone(String utteranceId) {
}
@Override
public void onError(String utteranceId) {
}
});
}
}