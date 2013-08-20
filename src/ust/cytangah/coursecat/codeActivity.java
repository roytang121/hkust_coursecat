package ust.cytangah.coursecat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;

import ust.cytangah.contentprovider.CourseContentProvider;
import ust.cytangah.coursecat.*;

import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SimpleCursorAdapter;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.util.Xml;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TwoLineListItem;

import android.widget.Toast;

public class codeActivity extends SherlockFragmentActivity //use Fragment for supportLoaderManager
		implements LoaderManager.LoaderCallbacks<Cursor> {
	 private String code;
	 private List<String> titles;
	 private ListView list;
	 private String[] allColumns = {MySQLiteHelper.COLUMN_ID,MySQLiteHelper.COLUMN_MARKER,MySQLiteHelper.COLUMN_CODE, MySQLiteHelper.COLUMN_TITLE};
	 private MySQLiteHelper dbhelper;
	 private SQLiteDatabase database;
	 private Cursor cursor;
	 private SimpleCursorAdapter mAdapter;
	 //private MiniParser par;
	private View longClickView;
	private int longClickViewPosition;
	 
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.main);
	        this.setTitle(RegCourse.SELECTED_SEM);
	        list = (ListView)findViewById(android.R.id.list);
	        
	        Bundle extras = getIntent().getExtras();
	        
	        if (extras != null) {
	            code = extras.getString("code");            
	        }  
	        
	        String[] from = {MySQLiteHelper.COLUMN_CODE, MySQLiteHelper.COLUMN_TITLE};
	        int[] to = {R.id.text1,R.id.text2};
	        
	        mAdapter = new SimpleCursorAdapter(this,
	        		R.layout.custom_two_line,
	        		null,
	        		from,
	        		to,
	        		0);
	        list.setAdapter(mAdapter);
	        
	        this.getSupportLoaderManager().initLoader(0, null, this);
	        
			list.setOnItemClickListener(new OnItemClickListener(){

				public void onItemClick(AdapterView<?> parent, View view, int position,
						long id) {
					Intent i = new Intent(getApplicationContext(), Detail.class);
					TextView code = (TextView)view.findViewById(R.id.text1);
					i.putExtra("code", code.getText());
					i.putExtra("codePosition", position);
					startActivity(i);
					//For testing
					//Toast.makeText(getApplicationContext(), "fuck", Toast.LENGTH_LONG).show();
				}
				
			});
	        //Prepare the Loader
			
	        //context menu
			
			//this.registerForContextMenu(list);
	        
	 }
	 

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		String[] projection = { MySQLiteHelper.COLUMN_ID, MySQLiteHelper.COLUMN_CODE, MySQLiteHelper.COLUMN_TITLE };
		CursorLoader cursorLoader = new CursorLoader(this,
				CourseContentProvider.CONTENT_URI, projection, "marker=?", new String[]{code}, null);
		return cursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor data) {
		// TODO Auto-generated method stub
		mAdapter.swapCursor(data);
		
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Auto-generated method stub
		mAdapter.swapCursor(null);
		
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;

    }
	/*
	private String selectedCode, selectedTitle;
	@Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo)menuInfo;
        menu.add(0, 1, 100, "Add to Bookmarks");
        selectedCode = ((TextView) info.targetView.findViewById(R.id.text1)).getText().toString();
       }
	
	@Override
    public boolean onContextItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case 1:
			MyList mylist = new MyList();
			if(mylist.create(selectedCode)){
				Toast.makeText(this, selectedCode + "Added to bookmarks", Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(this, "Bookmark already exist!", Toast.LENGTH_SHORT).show();
			}
			mylist.close();
			break;
		}
		return false;
	}
	*/
		
}
