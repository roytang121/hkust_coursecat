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
import ust.cytangah.sectioncontentprovider.SectionsContentProvider;

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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TwoLineListItem;

import android.widget.Toast;

public class PowerSearchResult extends SherlockActivity //use Fragment for supportLoaderManager
 {
	 private String code;
	 private List<String> titles;
	 private ListView list;
	 private String[] allColumns = {MySQLiteHelper.COLUMN_ID,MySQLiteHelper.COLUMN_MARKER,MySQLiteHelper.COLUMN_CODE, MySQLiteHelper.COLUMN_TITLE};
	 private MySQLiteHelper dbhelper;
	 private SQLiteDatabase database;
	 private Cursor cursor;
	 private SimpleCursorAdapter mAdapter;
	 private String searchCode;
	 private String fromTimeHr;
	 private String fromTimeMM;
	 private String fromTimePeriod;
	 private String toTimeHr;
	 private String toTimeMM;
	 private String toTimePeriod;
	 //private MiniParser par;
	private String weekincluded;
	 
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        this.setTitle(RegCourse.SELECTED_SEM);
	        setContentView(R.layout.main);
	        list = (ListView)findViewById(android.R.id.list);
	        
	        Bundle extras = getIntent().getExtras();
	        
	        if (extras != null) {
	        	searchCode = extras.getString("searchCode");
	        		if(searchCode == null){searchCode = "%";}
	        	fromTimeHr = extras.getString("fromTimeHr");  
	        		if(fromTimeHr == ""){fromTimeHr = "__";}
	        	fromTimeMM = extras.getString("fromTimeMM");
	        		if(fromTimeMM == ""){fromTimeMM = "__";}
	        	fromTimePeriod = extras.getString("fromTimePeriod");  
	        		if(fromTimePeriod == null){fromTimePeriod = "__";}
	        	toTimeHr = extras.getString("toTimeHr"); 
	        		if(toTimeHr == ""){toTimeHr = "__";}
	        	toTimeMM = extras.getString("toTimeMM");  
	        		if(toTimeMM == ""){toTimeMM = "__";}
	        	toTimePeriod = extras.getString("toTimePeriod");
	        		if(toTimePeriod == null){toTimePeriod = "%";}
	        	weekincluded = extras.getString("week");
	        }  
	        
	        MySQLiteHelper dbhelper = new MySQLiteHelper(this);
	        SQLiteDatabase database = dbhelper.getReadableDatabase();
	        String selectionTimeSQL = "'%" + fromTimeHr + ":" + fromTimeMM + "%" + fromTimePeriod + "%-%" + toTimeHr + ":" + toTimeMM + "%" + toTimePeriod + "%'";
	        //week inclusion
	        
	        	Log.d("WEEK", weekincluded);
	        	selectionTimeSQL = selectionTimeSQL + "and time like '" + weekincluded +"'";
	        
	        Cursor c = database.rawQuery("select * from sections where time like" + selectionTimeSQL + "and code like '" + searchCode + "%'", null);
	        Log.d("MYDEBUG", selectionTimeSQL);
	        Log.d("MYDEBUG", "select * from sections where time like" + selectionTimeSQL + "and code like '" + searchCode + "%'");
	        String[] from = {MySQLiteHelper.COLUMN_CODE, MySQLiteHelper.COLUMN_SECTION, MySQLiteHelper.COLUMN_TIME};
	        int[] to = {R.id.text1,R.id.text2, R.id.text3};
	        
	        if(c.moveToFirst()){
	        	SimpleCursorAdapter mAdapter = new SimpleCursorAdapter(this,
	        		R.layout.searchresult_three_line,
	        		c,
	        		from,
	        		to,
	        		0);
	        list.setAdapter(mAdapter);
	        list.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {

					TextView code = (TextView)view.findViewById(R.id.text1);
					String codeText = (String) code.getText();
					Intent i = new Intent(getApplicationContext(), Detail.class);
					i.putExtra("code", code.getText());
					startActivity(i);
				}});
	        }else{
	        	//Fail to search
	        	ListAdapter mAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,new String[]{"No match result, please expand the search filter"});
	        	list.setAdapter(mAdapter);
	        }
	        
	        
	        
	        /*
			list.setOnItemClickListener(new OnItemClickListener(){

				public void onItemClick(AdapterView<?> parent, View view, int position,
						long id) {
					Intent i = new Intent(getApplicationContext(), Detail.class);
					i.putExtra("position", position);
					i.putExtra("code", searchCode);
					i.putExtra("id", id);
					startActivity(i);
					//For testing
					//Toast.makeText(getApplicationContext(), "fuck", Toast.LENGTH_LONG).show();
				}
				
			});*/
	        //Prepare the Loader
			
	        //context menu
	        
			this.registerForContextMenu(list);
	        
	 }
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;

    }
	
	@Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(0, 1, 100, "Add to Bookmarks");
        menu.add(0, 2, 200, "Add to Class Book");        
    }
	
	@Override
    public boolean onContextItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case 1:
			Toast.makeText(getApplicationContext(), "Bookmarked", Toast.LENGTH_SHORT).show();
			break;
		
		case 2:
			Toast.makeText(getApplicationContext(), "Class Book Updated", Toast.LENGTH_SHORT).show();
			break;
		}
		return false;
	}
	
		
}
