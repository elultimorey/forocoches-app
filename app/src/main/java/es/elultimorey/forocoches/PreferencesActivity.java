package es.elultimorey.forocoches;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.view.MenuItem;

@SuppressLint("NewApi")
public class PreferencesActivity extends PreferenceActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB)
			setTheme(android.R.style.Theme_Light);
		else 
			setTheme(android.R.style.Theme_Holo_Light);    	
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);

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
	
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
		String s = preference.getKey();
		if (s.equals("acerca_pref"))
			startActivity(new Intent(getApplicationContext(), AboutActivity.class));
		else if (s.equals("ayuda_pref"))
			startActivity(new Intent(getApplicationContext(), AyudaActivity.class));
		return super.onPreferenceTreeClick(preferenceScreen, preference);
	}

}
