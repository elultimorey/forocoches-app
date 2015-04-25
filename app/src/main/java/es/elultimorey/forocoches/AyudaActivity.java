package es.elultimorey.forocoches;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

@SuppressLint("NewApi")
public class AyudaActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) { 
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB)
			setTheme(android.R.style.Theme_Light);
		else 
			setTheme(android.R.style.Theme_Holo_Light);  
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ayuda);

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
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}	
}
