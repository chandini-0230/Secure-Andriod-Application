package com.example.notificationreader.data;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import com.example.notificationreader.model.Notification;
import com.example.notificationreader.params.Params;
import java.util.ArrayList;
import java.util.List;
public class MyDbHandler extends SQLiteOpenHelper {
public MyDbHandler(Context context) {
super(context, Params.DB_NAME, null, Params.DB_VERSION);
}
@Override
public void onCreate(SQLiteDatabase db) {
createTable(db);
}
24
@Override
public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
}
public void createTable(SQLiteDatabase db){
String create = "CREATE TABLE " + Params.TABLE_NAME+"("
+ Params.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
+ Params.KEY_PACKAGE + " TEXT, "
+ Params.KEY_TITLE + " TEXT, "
+ Params.KEY_TEXT + " BLOB, "
+ Params.KEY_IV + " BLOB, "
+ Params.KEY_DATE + " TEXT "+ ")" ;
db.beginTransaction();
try {
SQLiteStatement statement = db.compileStatement(create);
statement.execute();
db.setTransactionSuccessful();
} catch (Exception e) {
e.printStackTrace();
} finally {
db.endTransaction();
}
}
public void addNotification(Notification notification) throws Exception {
SQLiteDatabase db = this.getWritableDatabase();
ContentValues values = new ContentValues();
EncryptedData encryptedText = EncryptDecrypt.encrypt(notification.getTextData());
values.put(Params.KEY_PACKAGE, notification.getPackageName());
values.put(Params.KEY_TITLE, notification.getTitleName());
values.put(Params.KEY_TEXT, encryptedText.getEncryptedText());
values.put(Params.KEY_IV, encryptedText.getIv());
values.put(Params.KEY_DATE, notification.getNotifyDate());
db.insert(Params.TABLE_NAME, null, values);
// Log.d("database", "Successfully Inserted");
db.close();
}
public List<Notification> getAllNotifications() throws Exception {
List<Notification> notificationList = new ArrayList<>();
SQLiteDatabase db = this.getReadableDatabase();
String sortOrder = Params.KEY_ID + " DESC";
//query to read from the db
// String select = "SELECT * FROM " + Params.TABLE_NAME +" ORDER BY "+Params.KEY_ID+"
DESC";
// String[] parameters = {};
Cursor cursor = db.query(
Params.TABLE_NAME,
null,
null,
null,
null,
null,
25
sortOrder);
if(cursor.moveToFirst()){
do{
Notification notification = new Notification();
String text = EncryptDecrypt.decrypt(cursor.getBlob(3), cursor.getBlob(4));
notification.setId(Integer.parseInt(cursor.getString(0)));
notification.setPackageName(cursor.getString(1));
notification.setTitleName(cursor.getString(2));
notification.setTextData(text);
notification.setNotifyDate(cursor.getString(5));
notificationList.add(notification);
}while (cursor.moveToNext());
}
return notificationList;
}
public void deleteNotification(int id){
SQLiteDatabase db = this.getWritableDatabase();
db.delete(Params.TABLE_NAME, Params.KEY_ID + "=?", new String[]{String.valueOf(id)});
db.close();
}
// public int getCount(){
// SQLiteDatabase db = this.getReadableDatabase();
// String query = "SELECT * FROM " + Params.TABLE_NAME;
// Cursor cursor = db.rawQuery(query, null);
// return cursor.getCount();
// }
}