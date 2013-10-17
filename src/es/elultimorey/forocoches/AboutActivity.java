package es.elultimorey.forocoches;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class AboutActivity extends Activity {
	
	private Activity app = this;
	
	protected void onCreate(Bundle savedInstanceState) {
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
	}
}
