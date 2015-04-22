package akiniyaloctsImgur.services;

import akiniyaloctsImgur.imgurmodel.ImageResponse;
import akiniyaloctsImgur.imgurmodel.ImageResponse;

/**
 * Created by AKiniyalocts on 1/14/15.
 *
 * Listener for when an image is uploaded
 */
public interface OnImageUploadedListener {
  public void onImageUploaded(ImageResponse response);
}
