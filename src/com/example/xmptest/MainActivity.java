package com.example.xmptest;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.xmptest.XMPTest;

public class MainActivity extends Activity {
	
	final Context context = this;
	private File[] listFile;  
    ArrayList<String> f = new ArrayList<String>(); 
    ArrayAdapter<String> adapter;
    ArrayList<String> fRel = new ArrayList<String>();
    ListView fileList;
    String selected;
    public static XMPTest xmp = new XMPTest();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		getFromSDCard();
		
		if(f.size() == 0) {
			Log.e("MAIN_ACTIVITY", "Failed to get from SD Card");
		} else {
			String[] temp;
			for(int i = 0; i < f.size(); i++) {
				temp = f.get(i).split("/");
				fRel.add(temp[temp.length - 1]);
			}

			adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, fRel);
			
			fileList = (ListView) findViewById(R.id.imageList);
			
			fileList.setAdapter(adapter);
			
			fileList.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					selected = f.get(position);
					openSelectionWindow();
				}
			});
		}
	}
	
	public void getFromSDCard() {
		File file= new File(android.os.Environment.getExternalStorageDirectory(),"DCIM/Camera");
		
		if (file.isDirectory()) {
            listFile = file.listFiles();
            for (int i = 0; i < listFile.length; i++) {
                f.add(listFile[i].getAbsolutePath());
            }
        }
	}
	
	public void openSelectionWindow() {
		LayoutInflater li = LayoutInflater.from(context);
		View view = li.inflate(R.layout.dialog, null);
		
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
		
		alertDialogBuilder.setView(view);
		
		alertDialogBuilder
		.setPositiveButton("View License", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				Intent intent = new Intent(MainActivity.this, ViewXMPActivity.class);
				intent.putExtra("file", selected);
				startActivity(intent);
			}
		})
		.setNegativeButton("Add License", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				Intent intent = new Intent(MainActivity.this, AddXMPActivity.class);
				intent.putExtra("file", selected);
				startActivity(intent);
			}
		});
		
		AlertDialog alertDialog = alertDialogBuilder.create();
		
		alertDialog.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
