package akiniyaloctsImgur.services;

import akiniyaloctsImgur.Constants;
import akiniyaloctsImgur.helpers.NotificationHelper;
import akiniyaloctsImgur.imgurmodel.BasicResponse;
import akiniyaloctsImgur.imgurmodel.ImageResponse;
import akiniyaloctsImgur.imgurmodel.ImgurAPI;
import akiniyaloctsImgur.imgurmodel.Upload;
import akiniyaloctsImgur.utils.NetworkUtils;
import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import retrofit.RestAdapter;
import retrofit.mime.TypedFile;

/**
 * Created by AKiniyalocts on 1/12/15.
 *
 * Our upload service. This creates our restadapter, uploads our image, and notifies us of the response.
 *
 *
 */
public class UploadService extends AsyncTask<Void, Void, Void> {
    public final static String TAG = UploadService.class.getSimpleName();


    public String title, description, albumId;
    private ImageResponse response;
    private Activity activity;
    private OnImageUploadedListener mUploaded;
    private File image;

    private NotificationHelper notificationHelper;


    public UploadService(Upload upload, Activity activity){
        this.image = upload.image;
        this.title = upload.title;
        this.description = upload.description;
        this.albumId = upload.albumId;
        this.activity = activity;
        mUploaded = (OnImageUploadedListener) activity;

        notificationHelper = new NotificationHelper(activity);

    }

    @Override protected void onPreExecute() {
        super.onPreExecute();
        notificationHelper.createUploadingNotification();
    }

    @Override protected Void doInBackground(Void... params) {
        try {
        /*
          Create rest adapter using our imgur API
         */
            RestAdapter imgurAdapter = new RestAdapter.Builder()
                    .setEndpoint(ImgurAPI.server)
                    .build();

        /*
          Set rest adapter logging if we're already logging
         */

            if (Constants.LOGGING)
                imgurAdapter.setLogLevel(RestAdapter.LogLevel.FULL);

        /*
          Upload image, get response for image
         */

            response = imgurAdapter.create(ImgurAPI.class)
                    .postImage(
                            Constants.getClientAuth(), title, description, albumId, null, new TypedFile("image/*", image)
                    );

        }
        catch (Exception e) {
            response = new ImageResponse();
            response.success = false;
        }



        return null;
    }

    @Override protected void onPostExecute(Void aVoid) {

        super.onPostExecute(aVoid);

        if(response != null){

            if(response.success) {
                mUploaded.onImageUploaded(response);
                notificationHelper.createUploadedNotification(response);
            }

            else{
                notificationHelper.createFailedUploadNotification(1);
            }

        }
    }

}
