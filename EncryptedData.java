package com.example.notificationreader.data;
22
public class EncryptedData {
private final byte[] encryptedText;
private final byte[] iv;
public EncryptedData(byte[] encryptedText, byte[] iv) {
this.encryptedText = encryptedText;
this.iv = iv;
}
public byte[] getEncryptedText() {
return encryptedText;
}
public byte[] getIv() {
return iv;
}
}