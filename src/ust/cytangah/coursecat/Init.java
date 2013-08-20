package ust.cytangah.coursecat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import ust.cytangah.coursecat.*;
import ust.cytangah.coursecat.R;

	public class Init extends Activity{
		private ProgressDialog progDia;
		@Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.init);
	        
	        
	        
	        //View
	        Button startButton = (Button)findViewById(R.id.startButton);
	        startButton.setOnClickListener(new View.OnClickListener(){
	        	@SuppressLint("NewApi")
				@Override
	        	public void onClick(View v){
	        		
	        		progDia = new ProgressDialog(Init.this);
	        		progDia.setMessage("#Updating the database #It may takes few minutes");
	        		progDia.setCancelable(true);
	        		progDia.setProgress(0);
	        		progDia.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
	        		progDia.show();
	        			        		
	        		new Thread(){
	        			@Override
	        			public void run(){
	        				MySQLiteHelper myDbHelper = new MySQLiteHelper(getApplicationContext());
	        				try {
	    	    				//init();
	        					 myDbHelper.createDataBase();
	    	    			} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} finally {
								//Set the first time run = false, this Activity runs only one time
						        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(Init.this);
						        Editor edit = pref.edit();
						        edit.putBoolean("first", false);
						        edit.putString("versionId", RegCourse.CURRENT_VERSION);
						        edit.commit(); 
	    	    				progDia.dismiss();
	    	    				
	    	    				 Intent intent = new Intent(Init.this, RegCourse.class);
	    	    			     startActivity(intent); 
	    	    			     finish(); 
	    	    			}
	        			}
	        		}.start();    		
	        	}
	        });
	        
	        
	        
		}
}
