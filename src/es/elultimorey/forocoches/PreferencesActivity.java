package es.elultimorey.forocoches;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;

public class PreferencesActivity extends PreferenceActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {    	
		super.onCreate(savedInstanceState);   
		addPreferencesFromResource(R.xml.preferences);
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
