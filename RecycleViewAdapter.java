package com.example.notificationreader.adapter;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.notificationreader.R;
import com.example.notificationreader.model.Notification;
import java.util.List;
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>
{
private Context context;
private List<Notification> notificationList;
public RecyclerViewAdapter(Context context, List<Notification> notificationList) {
this.context = context;
this.notificationList = notificationList;;
}
@NonNull
@Override
public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int
viewType) {
View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent,
false);
}
return new ViewHolder(view);
@Override
public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position)
{
Notification notification = notificationList.get(position);
holder.dateTime.setText(notification.getNotifyDate());
holder.packageName.setText(notification.getPackageName()); 
holder.titleName.setText(notification.getTitleName());
28
holder.textData.setText(notification.getTextData());
}
@Override
public int getItemCount() {
return notificationList.size();
}
public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
public TextView packageName;
public TextView titleName;
public TextView textData;
public TextView dateTime;
public ViewHolder(@NonNull View itemView) {
super(itemView);
itemView.setOnClickListener(this);
titleName = itemView.findViewById(R.id.titleName);
dateTime = itemView.findViewById(R.id.dateTime);
textData = itemView.findViewById(R.id.textData);
packageName = itemView.findViewById(R.id.packageName);
}
@Override
public void onClick(View view){
int position = this.getAdapterPosition();
Notification notification = notificationList.get(position);
String packageName = notification.getPackageName();
String title = notification.getTitleName();
String text = notification.getTextData();
String dateTime = notification.getNotifyDate();
int id = notification.getId();
// Toast.makeText(context, "The position is " + String.valueOf(position) + " title:
" + title+ " text: " + text, Toast.LENGTH_SHORT).show();
Intent intent = new Intent(context, DisplayNotification.class);
intent.putExtra("Rdate", dateTime);
intent.putExtra("RpackageName", packageName);
intent.putExtra("Rtitle", title);
intent.putExtra("Rtext", text);
intent.putExtra("Rid", id);
context.startActivity(intent);
}
}
}
