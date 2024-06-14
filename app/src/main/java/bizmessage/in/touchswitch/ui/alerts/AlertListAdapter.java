package bizmessage.in.touchswitch.ui.alerts;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import bizmessage.in.touchswitch.LocaleHelper;
import bizmessage.in.touchswitch.R;
import bizmessage.in.touchswitch.utils.PreferenceData;

import java.util.List;

import static bizmessage.in.touchswitch.utils.AppConstant.DBG;

public class AlertListAdapter extends RecyclerView.Adapter<AlertListAdapter.MyViewHolder> {

    RecylerViewVideoClickListener recylerViewVideoClickListener;
    List<String> alertList;
    Context context;
    Resources resources;


    public AlertListAdapter(Context context,List<String> alertList, RecylerViewVideoClickListener recylerViewVideoClickListener) {
        this.context = context;
        this.alertList = alertList;
        this.recylerViewVideoClickListener = recylerViewVideoClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_alert_list,parent,false);
        return new MyViewHolder(view,recylerViewVideoClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        if(PreferenceData.getlanguage().equalsIgnoreCase("hi")) {
            context = LocaleHelper.setLocale(context, "hi");
            resources = context.getResources();
        }
        if(PreferenceData.getlanguage().equalsIgnoreCase("ka")) {
            context = LocaleHelper.setLocale(context, "ka");
            resources = context.getResources();
        }
        if(PreferenceData.getlanguage().equalsIgnoreCase("ta")) {
            context = LocaleHelper.setLocale(context, "ta");
            resources = context.getResources();
        }
        if(PreferenceData.getlanguage().equalsIgnoreCase("te")) {
            context = LocaleHelper.setLocale(context, "te");
            resources = context.getResources();
        }
        if(PreferenceData.getlanguage().equalsIgnoreCase("bn")) {
            context = LocaleHelper.setLocale(context, "bn");
            resources = context.getResources();
        }
        if(PreferenceData.getlanguage().equalsIgnoreCase("ml")) {
            context = LocaleHelper.setLocale(context, "ml");
            resources = context.getResources();
        }
        if(PreferenceData.getlanguage().equalsIgnoreCase("en")) {
            context = LocaleHelper.setLocale(context, "en");
            resources = context.getResources();
        }

        if(PreferenceData.getlanguage().equalsIgnoreCase("mr")) {
            context = LocaleHelper.setLocale(context, "mr");
            resources = context.getResources();
        }

        if(PreferenceData.getlanguage().equalsIgnoreCase("gu")) {
            context = LocaleHelper.setLocale(context, "gu");
            resources = context.getResources();
        }
        String alertText = alertList.get(position);
        if(DBG) Log.i("ALERT", "onFailure: " + alertText);
         //మీ పరికరం ఆన్‌లైన్‌లో ఉంది




        if (alertText!=null && alertText.contains("Motion")){
            holder.imageViewThumbnail.setImageDrawable(context.getResources().getDrawable(R.drawable.motion));

            String newstring1 = alertText.replace("Motion detected on ",resources.getString(R.string.device_motion)+" ");

            holder.text_alert.setText(newstring1);

        }else if (alertText!=null && alertText.contains("BATTERY")){

            String newstring2 = alertText.replace("LOW BATTERY detected ",resources.getString(R.string.lowbattery_alert)+" ");

            holder.text_alert.setText(newstring2);


            holder.imageViewThumbnail.setImageDrawable(context.getResources().getDrawable(R.drawable.lowbattery));
        }
        else if (alertText!=null && alertText.contains("Fire")){

            String newstring2 = alertText.replace("Fire detected on ",resources.getString(R.string.device_fire)+" ");

            holder.text_alert.setText(newstring2);


            holder.imageViewThumbnail.setImageDrawable(context.getResources().getDrawable(R.drawable.fire));
        }
        else if (alertText!=null && alertText.contains("Vibration")){

            String newstring2 = alertText.replace("Vibration detected on ",resources.getString(R.string.device_vibration)+" ");

            holder.text_alert.setText(newstring2);

            holder.imageViewThumbnail.setImageDrawable(context.getResources().getDrawable(R.drawable.vibration));
        }else if (alertText!=null && alertText.contains("Online")){
            String newstring3 = alertText.replace("Your Device is Online On ",resources.getString(R.string.device_online)+" ");

            holder.text_alert.setText(newstring3);

            holder.imageViewThumbnail.setImageDrawable(context.getResources().getDrawable(R.drawable.online));
        }else if (alertText!=null && alertText.contains("Offline")){
            String newstring4 = alertText.replace("Your Device is Offline On ",resources.getString(R.string.devicealert_offline)+" ");

            holder.text_alert.setText(newstring4);

            holder.imageViewThumbnail.setImageDrawable(context.getResources().getDrawable(R.drawable.offline));
        }
        else {
            holder.imageViewThumbnail.setImageDrawable(context.getResources().getDrawable(R.drawable.offline));
        }
        holder.itemView.setOnClickListener(v -> holder.clickListener.onItemClickListener(alertText));

    }

    @Override
    public int getItemCount() {
        return alertList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView text_alert;
        ImageView imageViewThumbnail;
        RecylerViewVideoClickListener clickListener;

        public MyViewHolder(@NonNull View itemView, RecylerViewVideoClickListener recylerViewVideoClickListener) {
            super(itemView);

            this.clickListener = recylerViewVideoClickListener;
            text_alert = itemView.findViewById(R.id.text_alert);
            imageViewThumbnail = itemView.findViewById(R.id.imgDeviceAlert);
        }
    }
}
