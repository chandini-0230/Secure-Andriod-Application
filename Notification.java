package com.example.notificationreader.model;
public class Notification {
private int id;
private String packageName;
private String titleName;
private String textData;
private String notifyDate;
public Notification(int id, String packageName, String titleName, String textData, String
notifyDate) {
this.id = id;
this.packageName = packageName;
21
this.titleName = titleName;
this.textData = textData;
this.notifyDate = notifyDate;
}
public Notification(String packageName, String titleName, String textData, String
notifyDate) {
this.packageName = packageName;
this.titleName = titleName;
this.textData = textData;
this.notifyDate = notifyDate;
}
public Notification() {
}
public int getId() {
return id;
}
public void setId(int id) {
this.id = id;
}
public String getPackageName() {
return packageName;
}
public void setPackageName(String packageName) {
this.packageName = packageName;
}
public String getTitleName() {
return titleName;
}
public void setTitleName(String titleName) {
this.titleName = titleName;
}
public String getTextData() {
return textData;
}
public void setTextData(String textData) {
this.textData = textData;
}
public String getNotifyDate() {
return notifyDate;
}
public void setNotifyDate(String notifyDate) {
this.notifyDate = notifyDate;
}
}