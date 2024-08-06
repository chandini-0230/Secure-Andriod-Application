package com.example.notificationreader;
import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONObject;
public class ServerTime {
private static final String SERVER_URL =
"https://worldtimeapi.org/api/timezone/Asia/Kolkata";
private static String datetime = "Server time";
20
public static String fetchServerTime(Context context){
RequestQueue requestQueue = Volley.newRequestQueue(context);
JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
Request.Method.GET,
SERVER_URL,
null,
new Response.Listener<JSONObject>() {
@Override
public void onResponse(JSONObject response) {
datetime = response.optString("datetime");
}
},
new Response.ErrorListener() {
@Override
public void onErrorResponse(VolleyError error) {
error.printStackTrace();
}
}
);
requestQueue.add(jsonObjectRequest);
return datetime;
}
}