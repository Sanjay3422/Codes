package sanjay.navigation;

/**
 * Created by Sanjay's PC on 3/23/2017.
 */

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.RemoteMessage;


public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    String cir;
    Uri sound= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String msg = remoteMessage.getData().get("message");
        SharedPreferences sp = getSharedPreferences("User", MODE_PRIVATE);
        String nc=sp.getString("nc","");
        String ng=sp.getString("ng","");
        String na=sp.getString("na","");
        if(nc.equals("yes")&&((msg.equalsIgnoreCase("Circular"))||(msg.equalsIgnoreCase("Circular2"))))
            showNotification(msg);
        if(ng.equals("yes")&&(msg.equals("group")))
            showNotification1();
        if(na.equals("yes")&&(msg.equals("albums")))
            showNotification2();

    }

    private void showNotification2() {
        SharedPreferences sp = getSharedPreferences("User", MODE_PRIVATE);
        cir=sp.getString("cir","");
        if(!(cir.equals(" ")))
        {
            Intent i = new Intent(this,Main6Activity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this,0,i,PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setAutoCancel(true)
                    .setContentTitle("Share")
                    .setContentText("You have got a new image post")
                    .setSound(sound)
                    .setSmallIcon(R.mipmap.share)
                    .setContentIntent(pendingIntent);
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.notify(0,builder.build());

        }
    }

    private void showNotification1() {
        SharedPreferences sp = getSharedPreferences("User", MODE_PRIVATE);
        cir=sp.getString("cir","");
        if(!(cir.equals(" ")))
        {
            Intent i = new Intent(this,Main4Activity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this,0,i,PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setAutoCancel(true)
                    .setContentTitle("Share")
                    .setContentText("New group chat is live now. Check it!")
                    .setSound(sound)
                    .setSmallIcon(R.mipmap.share)
                    .setContentIntent(pendingIntent);
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.notify(0,builder.build());

        }
    }

    private void showNotification(String message) {
        SharedPreferences sp = getSharedPreferences("User", MODE_PRIVATE);
        cir=sp.getString("cir","");
        if(cir.equalsIgnoreCase(message))
        {
            Intent i = new Intent(this,MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this,0,i,PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setAutoCancel(true)
                    .setContentTitle("Share")
                    .setContentText("You have received new Circular")
                    .setSound(sound)
                    .setSmallIcon(R.mipmap.share)
                    .setContentIntent(pendingIntent);
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.notify(0,builder.build());

        }

    }
}
