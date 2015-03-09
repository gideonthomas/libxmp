package com.example.xmptest;

import com.xmptest.XMPTest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class AddXMPActivity extends Activity {
	
	ArrayAdapter<String> adapter;
	ListView licenseList;
	String filePath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_xmp);
		
		filePath = getIntent().getStringExtra("file");
		Log.i("File Path", filePath);
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, MainActivity.xmp.Licenses);
		licenseList = (ListView) findViewById(R.id.licenseList);
		
		licenseList.setAdapter(adapter);
		
		licenseList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				addLicenseToFile(XMPTest.Licenses.get(position));
			}
		});
	}
	
	public void addLicenseToFile(String license) {
		int response = MainActivity.xmp.setLicense(filePath, license);
		
		if(response == -1) {
			Toast.makeText(this, "Failed to add License", Toast.LENGTH_LONG);
		} else {
			Toast.makeText(this, "Successfully added License", Toast.LENGTH_LONG);
		}
		
		Intent intent = new Intent(AddXMPActivity.this, MainActivity.class);
		
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_xm, menu);
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
