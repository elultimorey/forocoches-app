package es.elultimorey.forocoches;

import java.util.LinkedList;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TableRow;
import android.widget.Toast;

public class ForoCochesActivity extends Activity {

	URLHandler miURLHandler = new URLHandler(this);
	SharedPreferences mPrefs;
	WebView webView;
	ProgressBar mProgressBar;
	ProgressBar mProgressBarCircle;

	EnumShortcut firstEnum = EnumShortcut.DEFECTO_FIRST;
	EnumShortcut secondEnum = EnumShortcut.DEFECTO_SECOND;

	ImageButton firstShorcurt;
	ImageButton secondShorcurt;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getWindow().requestFeature(Window.FEATURE_PROGRESS);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);

		webView = (WebView) findViewById(R.id.webview);
		this.registerForContextMenu(webView);
		mProgressBar = (ProgressBar) findViewById(R.id.ProgressBar);
		mProgressBarCircle = (ProgressBar) findViewById(R.id.progress_bar_circle);
		final ImageView lexus = (ImageView) findViewById(R.id.lexus_home);
		firstShorcurt = (ImageButton) findViewById(R.id.first);
		secondShorcurt = (ImageButton) findViewById(R.id.second);
		final ImageButton reload = (ImageButton) findViewById(R.id.actualizar);

		webView.getSettings().setBuiltInZoomControls(true);
		webView.getSettings().setSavePassword(true);

		cargarPreferencias();

		webView.loadUrl(miURLHandler.getURL());

		webView.setWebViewClient(new WebViewClient() {
			@Override
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
				Log.i("WEB_VIEW_TEST", "error code:" + errorCode);
				super.onReceivedError(view, errorCode, description, failingUrl);
				view.loadUrl("file:///android_asset/net/net-error.html");
			}
		});

		webView.setWebChromeClient(new WebChromeClient() {
			public void onProgressChanged(WebView view, int progress) {				
				mProgressBar.setVisibility(0);
				mProgressBar.setProgress(progress);

				mProgressBarCircle.setVisibility(0);
				mProgressBarCircle.setProgress(progress);
				if (progress == 100) 
					mProgressBarCircle.setVisibility(100);
			}
		});
		lexus.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction()==MotionEvent.ACTION_DOWN) 
					lexus.setImageResource(R.drawable.lexus_touch);
				else if(event.getAction()==MotionEvent.ACTION_UP) 
					lexus.setImageResource(R.drawable.lexus);
				return false;
			}
		});

		lexus.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				webView.loadUrl(miURLHandler.getURL());
			}
		});

		lexus.setOnLongClickListener(new View.OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				openZonasDialog();
				return false;
			}
		});

		TableRow tr = (TableRow) findViewById(R.id.TR02);
		tr.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				toTop();
			}
		});

		firstShorcurt.setOnTouchListener(new View.OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction()==MotionEvent.ACTION_DOWN) 
					firstShorcurt.setBackgroundResource(R.drawable.bar_button_touch);
				else if(event.getAction()==MotionEvent.ACTION_UP) {
					firstShorcurt.setBackgroundResource(R.drawable.bar_button_normal);
					shorcurtAction(firstEnum);
				}

				return false;
			}
		});

		secondShorcurt.setOnTouchListener(new View.OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction()==MotionEvent.ACTION_DOWN) 
					secondShorcurt.setBackgroundResource(R.drawable.bar_button_touch);
				else if(event.getAction()==MotionEvent.ACTION_UP) {
					secondShorcurt.setBackgroundResource(R.drawable.bar_button_normal);
					shorcurtAction(secondEnum);
				}
				return false;
			}
		});

		reload.setOnTouchListener(new View.OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction()==MotionEvent.ACTION_DOWN) 
					reload.setBackgroundResource(R.drawable.bar_button_touch);
				else if(event.getAction()==MotionEvent.ACTION_UP) {
					reload.setBackgroundResource(R.drawable.bar_button_normal);
					Log.i("RELOAD URL", webView.getUrl()); 
					if (webView.getUrl().equals("file:///android_asset/net/net-error.html")) {
						webView.goBack();
					}
					else
						webView.reload();
				}
				return false;
			}
		});

	}

	@Override
	public void onPause() {
		super.onPause();
		SharedPreferences.Editor ed = mPrefs.edit();
		ed.putString("url_pause", webView.getUrl());
		ed.commit();
	}

	@Override
	public void onRestart() {
		super.onRestart();
		webView.loadUrl(mPrefs.getString("url_pause", new URLHandler(this).getURL()));
	}

	@Override
	public void onResume() {
		super.onResume();
		cargarPreferencias();
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu)
	{
		super.onPrepareOptionsMenu(menu);

		MenuItem adelante = menu.findItem(R.id.adelante);
		adelante.setVisible(true);
		if (!webView.canGoForward())
			adelante.setEnabled(false);
		else
			adelante.setEnabled(true);

		return true;
	}

	@Override
	public boolean onCreateOptionsMenu (Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);

		return true;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		if (v instanceof WebView) {                
			WebView.HitTestResult result = ((WebView) v).getHitTestResult();

			if (result != null) {
				openMenuContextURL(result.getExtra(), result.getType());
			}
		}
	}

	@Override
	public boolean onMenuItemSelected (int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case R.id.panel_usuario:
			openPanelUsuarioDialog();
			break;
		case R.id.panel_foro:
			openHerramientasDialog();
			break;
		case R.id.adelante:
			if(webView.canGoForward())
				webView.goForward();
			break;
		case R.id.ajustes:
			startActivity(new Intent(getApplicationContext(), PreferencesActivity.class));
			break;
		case R.id.salir:
			finish();
			break;
		}
		return true;
	}

	// Método para evitar que al pulsar KEYCODE_SEARCH aparezca el Quick Search Box
	@Override
	public boolean onSearchRequested() {
		return true;

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
			webView.goBack();
			return true;
		}
		else if (keyCode == KeyEvent.KEYCODE_SEARCH) 
			webView.loadUrl(miURLHandler.buscar());
		return super.onKeyDown(keyCode, event);
	}

	public void openZonasDialog() {
		final CharSequence[] items = {"Zona General", "Zona Forocoches", "Zona Técnica & Info", "Zona Misc.", "Zona Comercial", "Otros"};
		AlertDialog.Builder builder = new AlertDialog.Builder(this)
		.setTitle("Ir a...")
		.setIcon(R.drawable.ic_ad_herramientas)
		.setItems(items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case 0: openZGeneral();
				break;
				case 1: openZForocoches();
				break;
				case 2: openZTecnica();
				break;
				case 3: openZMisc();
				break;
				case 4: openZComercial();
				break;
				case 5: openZOtros();
				break;
				}
			}
		});

		AlertDialog alert = builder.create();
		alert.show();

	}

	public void openPanelUsuarioDialog(){
		final CharSequence[] items = {"Menciones", "Mensajes Privados", "Temas suscritos", "Temas suscritos con novedades"};

		AlertDialog.Builder builder = new AlertDialog.Builder(this)
		.setTitle(R.string.dialog_user_title)
		.setIcon(R.drawable.ic_ad_usuario)
		.setItems(items, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
				switch (item) {
				case 0:	openMenciones();
				break;
				case 1: webView.loadUrl(miURLHandler.privados());
				break;
				case 2: webView.loadUrl(miURLHandler.suscripciones());
				break;
				case 3: webView.loadUrl(miURLHandler.suscripcionesConNovedades());
				break;
				default: break;
				};
			}
		});

		AlertDialog alert = builder.create();	

		alert.show();
	}

	public void openMenciones() {
		if (mPrefs.getString("user_name", "#").equals("#")) {
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
			alertDialog.setTitle("Menciones");
			alertDialog.setIcon(R.drawable.ic_ad_usuario);
			alertDialog.setMessage("Escribe tu nombre de usuario para conocer quien te ha mencionado");
			final EditText input = new EditText(this);
			input.setText("");
			alertDialog.setView(input);
			alertDialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {  
				public void onClick(DialogInterface dialog, int whichButton) {
					webView.loadUrl(miURLHandler.menciones(input.getText().toString()));
				}
			}); 
			alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {  
				public void onClick(DialogInterface dialog, int whichButton) {
					// Nada
				}
			});
			alertDialog.show();
		}
		else 
			webView.loadUrl(miURLHandler.menciones(mPrefs.getString("user_name", "#")));				
	}

	public void openHerramientasDialog() {
		LinkedList<CharSequence> listItems = new LinkedList<CharSequence>();
		listItems.addLast("Buscar");
		listItems.addLast("Compartir");
		listItems.addLast("Temas de hoy");
		// Evita 'Force Close' si se pulsa y aún no hay url en webView
		final String url;
		if (webView.getUrl() != null)
			url = webView.getUrl();
		else
			url = miURLHandler.getURL();
		if (url.contains("showthread")) {
			listItems.addFirst("Suscribir");
			listItems.addFirst("Responder");
		}
		else if (url.contains("forumdisplay")) 
			listItems.addFirst("Nuevo Tema");

		final CharSequence[] items = new CharSequence[listItems.size()];
		for (int i = 0; i < listItems.size(); i++) 
			items[i]=listItems.get(i);

		AlertDialog.Builder builder = new AlertDialog.Builder(this)
		.setTitle(R.string.dialog_herramientas_title)
		.setIcon(R.drawable.ic_ad_herramientas)
		.setItems(items, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
				if (items[item].equals("Buscar"))
					webView.loadUrl(miURLHandler.buscar());
				else if (items[item].equals("Compartir"))
					openCompartir();
				else if (items[item].equals("Temas de hoy"))
					webView.loadUrl(miURLHandler.temasHoy());
				else if (items[item].equals("Responder"))
					webView.loadUrl(miURLHandler.responer(url));
				else if (items[item].equals("Suscribir")) {
					Log.e("SUSCRIBIR", url);
					Log.e("SUSCRIBIR", miURLHandler.suscribir(url));
					webView.loadUrl(miURLHandler.suscribir(url));
				}
				else if (items[item].equals("Nuevo Tema"))
					webView.loadUrl(miURLHandler.nuevoTema(url));
			}
		});
		AlertDialog alert = builder.create();
		alert.show();

	}

	public void openCompartir() {
		openCompartir(webView.getUrl(), webView.getTitle());
	}

	/**
	 * 
	 * @param url url a compartir
	 * @param title titulo de la url a compartir, null si no hay titulo
	 */
	public void openCompartir(String url, String title) {
		Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
		sharingIntent.setType("text/plain");
		if (title!=null)
			sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, title);
		sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT,url.replace("//m.", "//www."));
		startActivity(Intent.createChooser(sharingIntent, "Compartir"));
	}

	public void openZGeneral() {
		final CharSequence[] items = {"General", "Electrónica/Informática", "Empleo", "Viajes", "Quedadas (KDD)"};
		AlertDialog.Builder builder = new AlertDialog.Builder(this)
		.setTitle("Zona General")
		.setIcon(R.drawable.ic_ad_herramientas)
		.setItems(items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case 0: webView.loadUrl(miURLHandler.general());
				break;
				case 1: webView.loadUrl(miURLHandler.electronica());
				break;
				case 2: webView.loadUrl(miURLHandler.empleo());
				break;
				case 3: webView.loadUrl(miURLHandler.viajes());
				break;
				case 4: webView.loadUrl(miURLHandler.quedadas());
				break;
				}
			}
		});

		AlertDialog alert = builder.create();
		alert.show();
	}

	public void openZForocoches() {
		final CharSequence[] items = {"Forocoches", "Competición", "Clásicos", "Monovolumenes", "4x4/Ocio", "Camiones/Autobuses", "Motos"};
		AlertDialog.Builder builder = new AlertDialog.Builder(this)
		.setTitle("Zona Forocoches")
		.setIcon(R.drawable.ic_ad_herramientas)
		.setItems(items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case 0: webView.loadUrl(miURLHandler.forocoches());
				break;
				case 1: webView.loadUrl(miURLHandler.competicion());
				break;
				case 2: webView.loadUrl(miURLHandler.clasicos());
				break;
				case 3: webView.loadUrl(miURLHandler.monovolumenes());
				break;
				case 4: webView.loadUrl(miURLHandler.cuatroxcuatro());
				break;
				case 5: webView.loadUrl(miURLHandler.camiones());
				break;
				case 6: webView.loadUrl(miURLHandler.motos());
				break;
				}
			}
		});

		AlertDialog alert = builder.create();
		alert.show();
	}

	public void openZTecnica() {
		final CharSequence[] items = {"Mecánica", "Car-Audio", "Seguros", "Tráfico/Radares", "Tuning"};
		AlertDialog.Builder builder = new AlertDialog.Builder(this)
		.setTitle("Zona Técnica & Info")
		.setIcon(R.drawable.ic_ad_herramientas)
		.setItems(items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case 0: webView.loadUrl(miURLHandler.mecanica());
				break;
				case 1:	webView.loadUrl(miURLHandler.caraudio());
				break;
				case 2:	webView.loadUrl(miURLHandler.seguros());
				break;
				case 3:	webView.loadUrl(miURLHandler.trafico());
				break;
				case 4:	webView.loadUrl(miURLHandler.tuning());
				break;
				}
			}
		});

		AlertDialog alert = builder.create();
		alert.show();
	}

	public void openZMisc() {
		final CharSequence[] items = {"Modelismo", "Tuning Digital", "Juegos de Coches", "Juegos Online"};
		AlertDialog.Builder builder = new AlertDialog.Builder(this)
		.setTitle("Zona Misc.")
		.setIcon(R.drawable.ic_ad_herramientas)
		.setItems(items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case 0: webView.loadUrl(miURLHandler.modelismo());
				break;
				case 1:	webView.loadUrl(miURLHandler.tuningDigital());
				break;
				case 2:	webView.loadUrl(miURLHandler.juegosCoches());
				break;
				case 3:	webView.loadUrl(miURLHandler.juegosOnline());
				break;
				}
			}
		});

		AlertDialog alert = builder.create();
		alert.show();
	}


	public void openZComercial() {
		final CharSequence[] items = {"Compra - Venta Profesional", "Compra - Venta Motor", "Compra - Venta Audio/Tuning", "Compra - Venta Electrónica", "Compra - Venta Otros"};
		AlertDialog.Builder builder = new AlertDialog.Builder(this)
		.setTitle("Zona Comercial")
		.setIcon(R.drawable.ic_ad_herramientas)
		.setItems(items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case 0: webView.loadUrl(miURLHandler.cvProfesional());
				break;
				case 1:	webView.loadUrl(miURLHandler.cvMotor());
				break;
				case 2:	webView.loadUrl(miURLHandler.cvAudioTuning());
				break;
				case 3:	webView.loadUrl(miURLHandler.cvElectronica());
				break;
				case 4:	webView.loadUrl(miURLHandler.cvOtros());
				break;	
				}
			}
		});

		AlertDialog alert = builder.create();
		alert.show();
	}


	public void openZOtros() {
		final CharSequence[] items = {"Info/Ayuda", "Pruebas"};
		AlertDialog.Builder builder = new AlertDialog.Builder(this)
		.setTitle("Zona Otros")
		.setIcon(R.drawable.ic_ad_herramientas)
		.setItems(items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case 0: webView.loadUrl(miURLHandler.ayuda());
				break;
				case 1:	webView.loadUrl(miURLHandler.pruebas());
				break;
				}
			}
		});

		AlertDialog alert = builder.create();
		alert.show();
	}

	public void openMenuContextURL(final String url, int type) {
		final LinkedList<CharSequence> listItems = new LinkedList<CharSequence>();
		listItems.addLast("Abrir");
		listItems.addLast("Abrir en el navegador");
		listItems.addLast("Compartir");
		AlertDialog.Builder builder = new AlertDialog.Builder(this)
		.setIcon(R.drawable.ic_ad_herramientas);
		String title;
		if (type != WebView.HitTestResult.UNKNOWN_TYPE) {
			Log.e("###############", "################");
			if (type == WebView.HitTestResult.IMAGE_TYPE || type == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {
				title = "Imagen";
				listItems.addLast("Guardar");
			}
			else
				title = "Url";
			builder.setTitle(title);
			final CharSequence[] items = new CharSequence[listItems.size()];
			for (int i = 0; i < listItems.size(); i++) 
				items[i]=listItems.get(i);

			Log.e("###############", "################2");
			builder.setItems(items, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					switch (which) {
					case 0: webView.loadUrl(url);
					break;
					case 1: Intent i = new Intent(Intent.ACTION_VIEW); 
					i.setData(Uri.parse(url));
					startActivity(i);
					break;
					case 2:	openCompartir(url, null);
					break;					
					case 3: downloadImage(url);
					break;
					}
				}
			});

			AlertDialog alert = builder.create();
			alert.show();

			Log.e("###############", "################3");
		}
	}

	@TargetApi(Build.VERSION_CODES.GINGERBREAD) private void downloadImage(String url) {
		DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
		Request request = new Request(Uri.parse(url));
		notificationVisible(request);
		dm.enqueue(request);
	}
	@TargetApi(Build.VERSION_CODES.HONEYCOMB) private void notificationVisible(Request request) {
		request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
	}

	private void cargarPreferencias() {
		mPrefs = PreferenceManager.getDefaultSharedPreferences(this);

		String fs = mPrefs.getString("first_shortcurt_pref", "default");

		Log.d("FS", fs);

		if (fs.equals("panel_usuario")) {
			firstEnum = EnumShortcut.PANEL_U;
			firstShorcurt.setImageResource(R.drawable.ic_bar_usuario);
		}
		else if (fs.equals("menciones")) {
			firstEnum = EnumShortcut.MENCIONES;
			firstShorcurt.setImageResource(R.drawable.ic_bar_menciones);			
		}
		else if (fs.equals("privados")) {
			firstEnum = EnumShortcut.PRIVADOS;
			firstShorcurt.setImageResource(R.drawable.ic_bar_privados);
		}
		else if (fs.equals("temas_suscritos")) {
			firstEnum = EnumShortcut.TEMAS_S;
			firstShorcurt.setImageResource(R.drawable.ic_bar_temas);
		}
		else if (fs.equals("temas_s_novedades")) {	
			firstEnum = EnumShortcut.TEMAS_S_N;
			firstShorcurt.setImageResource(R.drawable.ic_bar_temas);
		}
		else if (fs.equals("herramientas_foro")) {	
			firstEnum = EnumShortcut.PANEL_H;
			firstShorcurt.setImageResource(R.drawable.ic_bar_herramientas);
		}
		else if (fs.equals("responder")) {	
			firstEnum = EnumShortcut.RESPONDER;
			firstShorcurt.setImageResource(R.drawable.ic_bar_responder);
		}
		else if (fs.equals("nuevo_tema")) {	
			firstEnum = EnumShortcut.NUEVO_TEMA;
			firstShorcurt.setImageResource(R.drawable.ic_bar_nuevo);
		}
		else if (fs.equals("buscar")) {	
			firstEnum = EnumShortcut.BUSCAR;
			firstShorcurt.setImageResource(R.drawable.ic_bar_buscar);
		}
		else if (fs.equals("compartir")) {	
			firstEnum = EnumShortcut.COMPARTIR;
			firstShorcurt.setImageResource(R.drawable.ic_bar_compartir);
		}
		else if (fs.equals("temas_de_hoy")) {		
			firstEnum = EnumShortcut.TEMAS_HOY;
			firstShorcurt.setImageResource(R.drawable.ic_bar_hoy);
		}
		else if (fs.equals("adelante")) {	
			firstEnum = EnumShortcut.ADELANTE;
			firstShorcurt.setImageResource(R.drawable.ic_bar_adelante);
		}
		else if (fs.equals("atras")) {
			firstEnum = EnumShortcut.ATRAS;
			firstShorcurt.setImageResource(R.drawable.ic_bar_atras);			
		}
		else {
			firstEnum = EnumShortcut.DEFECTO_FIRST;
			firstShorcurt.setImageResource(R.drawable.ic_bar_usuario);
		}

		String ss = mPrefs.getString("second_shortcurt_pref", "default");

		Log.d("SS", ss);

		if (ss.equals("panel_usuario")) {
			secondEnum = EnumShortcut.PANEL_U;
			secondShorcurt.setImageResource(R.drawable.ic_bar_usuario);
		}
		else if (ss.equals("menciones")) {
			secondEnum = EnumShortcut.MENCIONES;
			secondShorcurt.setImageResource(R.drawable.ic_bar_menciones);			
		}
		else if (ss.equals("privados")) {
			secondEnum = EnumShortcut.PRIVADOS;
			secondShorcurt.setImageResource(R.drawable.ic_bar_privados);
		}
		else if (ss.equals("temas_suscritos")) {
			secondEnum = EnumShortcut.TEMAS_S;
			secondShorcurt.setImageResource(R.drawable.ic_bar_temas);
		}
		else if (ss.equals("temas_s_novedades")) {	
			secondEnum = EnumShortcut.TEMAS_S_N;
			secondShorcurt.setImageResource(R.drawable.ic_bar_temas);
		}
		else if (ss.equals("herramientas_foro")) {	
			secondEnum = EnumShortcut.PANEL_H;
			secondShorcurt.setImageResource(R.drawable.ic_bar_herramientas);
		}
		else if (ss.equals("responder")) {	
			secondEnum = EnumShortcut.RESPONDER;
			secondShorcurt.setImageResource(R.drawable.ic_bar_responder);
		}
		else if (ss.equals("nuevo_tema")) {	
			secondEnum = EnumShortcut.NUEVO_TEMA;
			secondShorcurt.setImageResource(R.drawable.ic_bar_nuevo);
		}
		else if (ss.equals("buscar")) {	
			secondEnum = EnumShortcut.BUSCAR;
			secondShorcurt.setImageResource(R.drawable.ic_bar_buscar);
		}
		else if (ss.equals("compartir")) {	
			secondEnum = EnumShortcut.COMPARTIR;
			secondShorcurt.setImageResource(R.drawable.ic_bar_compartir);
		}
		else if (ss.equals("temas_de_hoy")) {		
			secondEnum = EnumShortcut.TEMAS_HOY;
			secondShorcurt.setImageResource(R.drawable.ic_bar_hoy);
		}
		else if (ss.equals("adelante")) {	
			secondEnum = EnumShortcut.ADELANTE;
			secondShorcurt.setImageResource(R.drawable.ic_bar_adelante);
		}
		else if (ss.equals("atras")) {
			secondEnum = EnumShortcut.ATRAS;
			secondShorcurt.setImageResource(R.drawable.ic_bar_atras);			
		}
		else {
			secondEnum = EnumShortcut.DEFECTO_SECOND;
			secondShorcurt.setImageResource(R.drawable.ic_bar_herramientas);
		}

		webView.getSettings().setJavaScriptEnabled(mPrefs.getBoolean("pref_navegacion_javascript", false));

		if (miURLHandler.verVersionCompleta(mPrefs.getBoolean("pref_navegacion_completa", false))) 
			webView.loadUrl(miURLHandler.renovarURL(webView.getUrl()));

	}



	private void shorcurtAction(EnumShortcut action) {
		String url;
		if (webView.getUrl() == null)
			url = miURLHandler.getURL();
		else
			url = webView.getUrl();

		switch(action) {
		case DEFECTO_FIRST:
			openPanelUsuarioDialog();
			break;
		case DEFECTO_SECOND:
			openHerramientasDialog();
			break;
		case PANEL_U:
			openPanelUsuarioDialog();
			break;
		case MENCIONES:
			openMenciones();
			break;
		case PRIVADOS:
			webView.loadUrl(miURLHandler.privados());
			break;
		case TEMAS_S:
			webView.loadUrl(miURLHandler.suscripciones());
			break;
		case TEMAS_S_N:
			webView.loadUrl(miURLHandler.suscripcionesConNovedades());
			break;
		case PANEL_H:
			openHerramientasDialog();
			break;
		case RESPONDER:
			if (url.contains("showthread"))
				webView.loadUrl(miURLHandler.responer(url));
			break;
		case NUEVO_TEMA:
			if (url.contains("forumdisplay")) 
				webView.loadUrl(miURLHandler.nuevoTema(url));
			else {
				Toast t = Toast.makeText(this, "O RLY", Toast.LENGTH_LONG);
				t.show();
			}
			break;
		case BUSCAR:
			webView.loadUrl(miURLHandler.buscar());
			break;
		case COMPARTIR:
			openCompartir();
			break;
		case TEMAS_HOY:
			webView.loadUrl(miURLHandler.temasHoy());
			break;
		case ADELANTE:
			if(webView.canGoForward())
				webView.goForward();
			break;
		case ATRAS:
			if(webView.canGoBack())
				webView.goBack();
		}
	}

	public void toTop() {
		webView.scrollTo(0, 0);
	}
}