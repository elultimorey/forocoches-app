package es.elultimorey.forocoches;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

@SuppressLint("NewApi")
public class AboutActivity extends Activity {

	private Activity app = this;

	protected void onCreate(Bundle savedInstanceState) {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB)
			setTheme(android.R.style.Theme_Light);
		else 
			setTheme(android.R.style.Theme_Holo_Light);  
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acerca);

		TextView normas = (TextView) findViewById(R.id.textview_normas);
		normas.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Uri uri = Uri.parse(new URLHandler(app).normas());
				Intent intent = new Intent("android.intent.action.VIEW", uri);
				startActivity(intent);
			}
		});

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){ 
			ActionBar actionBar = getActionBar();
			actionBar.setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// app icon in action bar clicked; go home
			Intent intent = new Intent(this, PreferencesActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}	
}
