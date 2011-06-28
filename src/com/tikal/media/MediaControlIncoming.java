package com.tikal.media;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tikal.controlcontacts.ControlContacts;
import com.tikal.softphone.R;

public class MediaControlIncoming extends Activity {
	private static final String LOG_TAG = "MediaControlIncoming";

	private ControlContacts controlcontacts = new ControlContacts(this);
	Vibrator vibrator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.control_call_incomingcall);
		Log.d(LOG_TAG, "Media Control Incoming Created");

		Bundle extras = getIntent().getExtras();

		String uri = (String) extras.getSerializable("Uri");

		TextView text = (TextView) findViewById(R.id.incoming_sip);
		text.setText(uri);
		
		String[] sipArray = uri.split(":");
		String sipUri = "";
		if (sipArray.length > 0) sipUri = sipArray[1];
		else sipUri = sipArray[0];
		
		Log.d(LOG_TAG, "sipUri = " + sipUri);
		Integer idContact = controlcontacts.getId(sipUri);
		Log.d(LOG_TAG, "idContact = " + idContact);
		if (!idContact.equals("")){
			ImageView imageCall = (ImageView) findViewById(R.id.image_call);
			Bitmap bm = controlcontacts.getPhoto(idContact);
			if (bm != null) {
				imageCall.setImageBitmap(bm);
			}
		}
	
		vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		long[] pattern = { 0, 1000, 2000, 3000 };

		vibrator.vibrate(pattern, 1);

	}
	
	@Override
	protected void onStart() {
		super.onStart();
		Log.d(LOG_TAG, "onStart");
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		Log.d(LOG_TAG, "onRestart");
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d(LOG_TAG, "onResume");

		final Button buttonCall = (Button) findViewById(R.id.button_call_accept);
		buttonCall.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d(LOG_TAG, "Call Accepted " + RESULT_OK);
				vibrator.cancel();
				setResult(RESULT_OK);
				finish();
			}
		});

		final Button buttonReject = (Button) findViewById(R.id.button_call_reject);
		buttonReject.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d(LOG_TAG, "Call Canceled");
				vibrator.cancel();
				setResult(RESULT_CANCELED);
				finish();
			}
		});

	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.d(LOG_TAG, "onStop");
		vibrator.cancel();
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.d(LOG_TAG, "onPause");
		vibrator.cancel();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d(LOG_TAG, "onDestroy");
		vibrator.cancel();
	}

}
