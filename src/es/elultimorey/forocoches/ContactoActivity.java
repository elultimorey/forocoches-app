package es.elultimorey.forocoches;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class ContactoActivity extends Activity {

	public void onCreate(Bundle savedInstanceState) {    
		super.onCreate(savedInstanceState);      
		setContentView(R.layout.contacto);

		ImageView gmail = (ImageView) findViewById(R.id.contacto_gmail);
		gmail.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

				emailIntent.setType("plain/text");
				emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"elultimorey.dev@gmail.com"});
				emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Forocoches App");
				startActivity(Intent.createChooser(emailIntent, "Enviar email..."));
				finish();


			}
		});

		ImageView twitter = (ImageView) findViewById(R.id.contacto_twitter);
		twitter.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
                Uri uri = Uri.parse("http://twitter.com/elultimorey");
                Intent intent = new Intent("android.intent.action.VIEW", uri);
                startActivity(intent);
                finish();
			}
		});
	}


}
