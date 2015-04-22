package akiniyaloctsImgur;

/**
 * Created by AKiniyalocts on 2/23/15.
 */
public class Constants {
  /*
    Logging flag
   */
  public static final boolean LOGGING = false;

  /*
    Your imgur client id. You need this to upload to imgur.

    More here: https://api.imgur.com/
   */
  public static final String MY_IMGUR_CLIENT_ID = "60e835617decdea";
  public static final String MY_IMGUR_CLIENT_SECRET = "be903d3e57e68684b7984b2f24dd1aa5e9a30d27";

  /*
    Redirect URL for android.
   */
  public static final String MY_IMGUR_REDIRECT_URL = "http://android";

  /*
    Client Auth
   */
  public static String getClientAuth(){
    return "Client-ID " + MY_IMGUR_CLIENT_ID;
  }

}
