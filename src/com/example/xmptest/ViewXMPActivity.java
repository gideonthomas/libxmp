package com.example.xmptest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ViewXMPActivity extends Activity {
	
	String filePath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_xmp);
		
		filePath = getIntent().getStringExtra("file");
		Log.i("File Path", filePath);
		
		String licenseURL = MainActivity.xmp.getLicense(filePath);
		
		TextView view = (TextView) findViewById(R.id.licenseURL);
		
		Button back = (Button) findViewById(R.id.viewBack);
		
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ViewXMPActivity.this, MainActivity.class);
				startActivity(intent);
			}
		});
		
		if(licenseURL != null) {
			view.setText(licenseURL);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_xm, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
