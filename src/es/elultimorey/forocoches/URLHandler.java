package es.elultimorey.forocoches;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class URLHandler {

	private final static String URL_M_FOROCOCHES = "http://m.forocoches.com/foro/";
	private final static String URL_FOROCOCHES = "http://www.forocoches.com/foro/";
	private boolean versionCompleta = false;
	
	private final Activity app;
	
	public URLHandler (Activity app) {
		this.app = app;
	}
	
	/**
	 * 
	 * @param flag 
	 * @return Si cambia y es necesario hacer un reload
	 */
	public boolean verVersionCompleta(boolean flag) {
		if (versionCompleta != flag) {
			versionCompleta = flag;
			return true;
		}
		else
			return false;
				
	}
	
	/**
	 * Método que devuelve la url principal del foro
	 * @return
	 */
	public String getURL() {
		if (versionCompleta)
			return URL_FOROCOCHES;
		else
			return URL_M_FOROCOCHES;
	}
	
	/**
	 * Metodo que transforma una URL de la version completa a la versión móvil y viceversa.
	 * @param url
	 * @return 
	 */
	public String renovarURL(String url) {
		if (url == null)
			return getURL();
		if (versionCompleta)
			return url.replace(URL_M_FOROCOCHES, URL_FOROCOCHES);
		else
			return url.replace(URL_FOROCOCHES, URL_M_FOROCOCHES);
	}

	/**
	 * Método para obtener una url que sirva para buscar las menciones a "usuario".
	 * Siempre vendrá el usuario.
	 * @param usuario : usuario del que queremos ver las menciones
	 * @return url para buscar a "usuario"
	 */
	public String menciones(String usuario) {
		SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(app);
		String url = getURL() + "search.php?do=process&searchthreadid=&query="
			+ usuario
			+ "&exactname=1&showposts="
			+ mPrefs.getString("user_menciones", "0")
			+ "&sortby=lastpost&";
			
		return url;
	}
	
	public String privados() {
		return getURL() + "private.php?";
	}
	
	public String suscripciones() {
		return getURL() + "subscription.php?do=viewsubscription";
	}
	
	public String suscripcionesConNovedades() {
		return getURL() + "usercp.php";
	}
	
	public String buscar() {
		return getURL() + "search.php";
	}
	
	public String temasHoy() {
		return getURL() + "search.php?do=getdaily";
	}
	
	public String responer(String hilo) {
		if (!hilo.contains("#"))
			return hilo+"#respuesta";
		else {
			String[] url = hilo.split("#");
			return url[0]+"#respuesta";
		}
	}
	
	public String suscribir(String hilo) {
		String[] splt = null;
		String top = "t";
		// Si llegamos desde un thread
		if (hilo.contains("?t="))
			splt = hilo.split("t=");
		// Si llegamos desde un post
		else if (hilo.contains("?p=")){
			splt = hilo.split("#post");
			top = "p";
		}
		if (splt == null)
			return hilo;
		String[] codigo = splt[1].split("&");
		return getURL() + "subscription.php?do=addsubscription&"+top+"="+codigo[0];
	}
	
	public String nuevoTema(String url) {
		String[] zona = url.split("f=");
		return getURL() + "newthread.php?do=newthread&f="+zona[1];
	}
	
	public String general() {
		return getURL() + "forumdisplay.php?f=2";
	}
	
	public String electronica() {
		return getURL() + "forumdisplay.php?f=17";
	}
	
	public String empleo() {
		return getURL() + "forumdisplay.php?f=23";
	}
	
	public String viajes() {
		return getURL() + "forumdisplay.php?f=27";
	}
	
	public String quedadas() {
		return getURL() + "forumdisplay.php?f=15";
	}
	
	public String forocoches() {
		return getURL() + "forumdisplay.php?f=4";
	}
	
	public String competicion() {
		return getURL() + "forumdisplay.php?f=18";
	}
	
	public String clasicos() {
		return getURL() + "forumdisplay.php?f=20";
	}
	
	public String monovolumenes() {
		return getURL() + "forumdisplay.php?f=47";
	}
	
	public String cuatroxcuatro() {
		return getURL() + "forumdisplay.php?f=21";
	}
	
	public String camiones() {
		return getURL() + "forumdisplay.php?f=76";
	}
	
	public String motos() {
		return getURL() + "forumdisplay.php?f=48";
	}
	
	public String mecanica() {
		return getURL() + "forumdisplay.php?f=19";
	}
	
	public String caraudio() {
		return getURL() + "forumdisplay.php?f=5";
	}
	
	public String seguros() {
		return getURL() + "forumdisplay.php?f=31";
	}
	
	public String trafico() {
		return getURL() + "forumdisplay.php?f=30";
	}
	
	public String tuning() {
		return getURL() + "forumdisplay.php?f=6";
	}
	
	public String modelismo() {
		return getURL() + "forumdisplay.php?f=28";
	}
	
	public String tuningDigital() {
		return getURL() + "forumdisplay.php?f=24";
	}
	
	public String juegosCoches() {
		return getURL() + "forumdisplay.php?f=16";
	}
	
	public String juegosOnline() {
		return getURL() + "forumdisplay.php?f=43";
	}
	
	public String cvProfesional() {
		return getURL() + "forumdisplay.php?f=34";
	}
	
	public String cvMotor() {
		return getURL() + "forumdisplay.php?f=11";
	}
	
	public String cvAudioTuning() {
		return getURL() + "forumdisplay.php?f=25";
	}
	
	public String cvElectronica() {
		return getURL() + "forumdisplay.php?f=22";
	}
	
	public String cvOtros() {
		return getURL() + "forumdisplay.php?f=69";
	}
	
	public String ayuda() {
		return getURL() + "forumdisplay.php?f=12";
	}
	
	public String pruebas() {
		return getURL() + "forumdisplay.php?f=8";
	}
	
	public String normas() {
		return "http://www.forocoches.com/index.php?p=normas";
	}
}
