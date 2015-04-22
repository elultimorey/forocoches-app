package akiniyaloctsImgur.helpers;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import akiniyaloctsImgur.imgurmodel.ImageResponse;
import es.elultimorey.forocoches.R;

/**
 * Created by AKiniyalocts on 1/15/15.
 *
 * This class is just created to help with notifications, definitely not necessary.
 */
public class NotificationHelper {
  public final static String TAG = NotificationHelper.class.getSimpleName();

  private Context mContext;


  public NotificationHelper(Context mContext) {
    this.mContext = mContext;
  }



  public void createUploadingNotification(){
    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext);
    mBuilder.setSmallIcon(android.R.drawable.ic_menu_upload);
    mBuilder.setContentTitle("Subiendo imagen...");

    mBuilder.setAutoCancel(true);

    NotificationManager mNotificationManager =
        (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

    mNotificationManager.notify(mContext.getString(R.string.app_name).hashCode(), mBuilder.build());

  }
  public void createUploadedNotification(ImageResponse response){
      NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext);
      mBuilder.setSmallIcon(android.R.drawable.ic_menu_gallery);
      mBuilder.setContentTitle("Imagen subida correctamente");

      mBuilder.setContentText(response.data.link);

      mBuilder.setColor(mContext.getResources().getColor(R.color.rojo_gmail));


      Intent resultIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(response.data.link));
      PendingIntent intent = PendingIntent.getActivity(mContext, 0, resultIntent, 0);
      mBuilder.setContentIntent(intent);
      mBuilder.setAutoCancel(true);

      NotificationManager mNotificationManager =
              (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

      mNotificationManager.notify(mContext.getString(R.string.app_name).hashCode(), mBuilder.build());
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
          final android.content.ClipboardManager clipboardManager = (android.content.ClipboardManager) mContext
                  .getSystemService(Context.CLIPBOARD_SERVICE);
          final android.content.ClipData clipData = android.content.ClipData
                  .newPlainText("URL Imagen", "[IMG]"+response.data.link+"[/IMG]");
          clipboardManager.setPrimaryClip(clipData);
      } else {
          final android.text.ClipboardManager clipboardManager = (android.text.ClipboardManager) mContext
                  .getSystemService(Context.CLIPBOARD_SERVICE);
          clipboardManager.setText("[IMG]"+response.data.link+"[/IMG]");
      }

      Toast.makeText(mContext, "URL copiada al portapapeles", Toast.LENGTH_SHORT).show();
  }
  public void createFailedUploadNotification(int E){
      NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext);
      mBuilder.setSmallIcon(android.R.drawable.ic_dialog_alert);
      mBuilder.setContentTitle("Error al subir la imagen...");
      if (E == 0) {
          mBuilder.setContentText("Compruebe que la imagen está en el dispositivo");
      }
      else if (E == 1 ) {
          mBuilder.setContentText("Compruebe que el dispositivo esté conectado a internet");
      }


      mBuilder.setColor(mContext.getResources().getColor(R.color.rojo_gmail));

      mBuilder.setAutoCancel(true);

      NotificationManager mNotificationManager =
              (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

      mNotificationManager.notify(mContext.getString(R.string.app_name).hashCode(), mBuilder.build());

      Toast.makeText(mContext, "Error al subir la imagen", Toast.LENGTH_SHORT).show();
  }



}
