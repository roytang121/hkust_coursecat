package ust.cytangah.coursecat;

import java.io.File;
import java.io.IOException;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;
import com.actionbarsherlock.widget.SearchView;


public class RegCourse extends SherlockActivity implements ActionBar.OnNavigationListener{
	private ListView list;
	private boolean FIRST_TIME_RUN;
	private ActionBar actionbar;
	private EditText search;
	private SearchView searchView;
	private ArrayAdapter<String> adapter;
	private EditText filterText;
	private String[] semesters; 
	public static final String CURRENT_VERSION = "1.2f_hotfix"; //Public release will be 1.0
	public static final String CURRENT_SEM = "Fall 2013"; // need to update
	public static String USER_VERSION;
	public static String SELECTED_SEM = CURRENT_SEM;
	public static int CURRENT_NUM = 1310; //need to update
	public static int SELECTED_NUM = CURRENT_NUM;
	public static int[] num_set = {1310, 1230, 1240, 1210}; // need to update * copy to ./assest  too
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        setContentView(R.layout.main);
        list = (ListView)findViewById(android.R.id.list);
        this.getSupportActionBar().setTitle(""); // empty title spaces
        
        // Semester Navigation
        Context context = this.getSupportActionBar().getThemedContext();
        ArrayAdapter<CharSequence> navigation = ArrayAdapter.createFromResource(context, R.array.semester, R.layout.sherlock_spinner_item);
        navigation.setDropDownViewResource(R.layout.sherlock_spinner_dropdown_item);
        semesters = this.getResources().getStringArray(R.array.semester);
        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        	//default selected 
        	int lastPos = pref.getInt("lastPos", 0); //0 == current sem
        			System.out.println("Pref - LastPos=" + lastPos);
        getSupportActionBar().setListNavigationCallbacks(navigation, this);
        getSupportActionBar().setSelectedNavigationItem(lastPos);
        //actionbar = this.getSupportActionBar();
        
        //Check if first time Run
        
        FIRST_TIME_RUN = pref.getBoolean("first", true);
        if(FIRST_TIME_RUN == true){
        	Log.d("FIRST TIME:", "FIRST TIME TRUE");
        	Intent i = new Intent(getApplicationContext(), Init.class);
        	startActivity(i);
        	this.finish();
        }else{
        //Check if different Version
	        USER_VERSION = pref.getString("versionId", "-1");
	        Log.d("USER_VERSION : ", USER_VERSION);
	        Log.d("CURRENT_VERSION : ", CURRENT_VERSION);
	        if(!(USER_VERSION.equals(CURRENT_VERSION))){
	        	Intent i = new Intent(getApplicationContext(), Init.class);
	        	startActivity(i);
	        	this.finish();
	        }
        }
        
        //Define the course code list
        //adapter= new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, GlobalData.subjectList);
	    if(!FIRST_TIME_RUN){
	        MySQLiteHelper dbhelper = new MySQLiteHelper(this);
	        SQLiteDatabase db = dbhelper.getReadableDatabase();
	        Cursor c = db.rawQuery("select * from courses group by marker", null);
	        SimpleCursorAdapter madapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, c, new String[]{"marker"}, new int[]{android.R.id.text1});
	        list.setAdapter(madapter);
	        db.close();
	        
	        list.setOnItemClickListener(new OnItemClickListener(){
	        	public void onItemClick(AdapterView<?> parent, View view,
	        	          int position, long id) {
	        		//String item = parent.getItemAtPosition(position).toString();
	        		TextView temp = (TextView) view.findViewById(android.R.id.text1);
	        		String item = temp.getText().toString();
	        			Intent i = new Intent(getApplicationContext(), codeActivity.class);
	        			i.putExtra("code",item);
	        			startActivity(i);
	        		
	        	}
	        });
	    }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.main, menu);

        menu.add(0, 1, 100, "Search")
            .setIcon(R.drawable.ic_action_search)
            .setActionView(R.layout.collapsible_edittext)
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
        /*filterText = (EditText)menu.findItem(1).getActionView();
        filterText.addTextChangedListener(filterTextWatcher);*/
        
        /*
         * Search Box
         * AutocompleteTextView
         */
        if(FIRST_TIME_RUN != true){
        AutoCompleteTextView searchBox = (AutoCompleteTextView)menu.findItem(1).getActionView();
        MySQLiteHelper dbhelper = new MySQLiteHelper(this);
        SQLiteDatabase database = dbhelper.getReadableDatabase();
        Cursor c = database.query(MySQLiteHelper.TABLE_COURSES, null, null, null, null, null, null);
        
        if(c.getCount() > 0){
        	String[] codelist = new String[c.getCount()];
        	int i = 0;
        	while(c.moveToNext()){
        		codelist[i] = c.getString(c.getColumnIndex(MySQLiteHelper.COLUMN_CODE));
        		i++;
        	}
        	ArrayAdapter<String> searchAdapter = new ArrayAdapter<String>(this, R.layout.searchhint,R.id.hint, codelist);
        	searchBox.setAdapter(searchAdapter);
        }
        
        
        searchBox.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
					Intent i = new Intent(RegCourse.this, Detail.class);
					i.putExtra("code", parent.getItemAtPosition(position).toString());
					startActivity(i);
			}});
        
        database.close();
        c.close();
        }
        
        
        SubMenu settingMenu = menu.addSubMenu(0, 2, 200, "Settings");
        settingMenu.add(1, 1, 100, "About");
        settingMenu.add(1, 2, 200,"Bookmarks");
        settingMenu.add(1, 3, 300, "Advanced Search");
        settingMenu.add(1, 4, 400, "Report an issue");
        settingMenu.add(1, 5, 350, "Common Core");
        settingMenu.add(1, 6, 360, "Barn Snapshots");
        
        MenuItem settingMenuItem = settingMenu.getItem();
        settingMenuItem.setIcon(R.drawable.abs__ic_menu_moreoverflow_normal_holo_dark);
        settingMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	if(item.getGroupId() == 1){
    		switch(item.getItemId()){
    		case 1:
    			Intent i_1 = new Intent(getApplicationContext(),About.class);
    			startActivity(i_1);
    			break;
    		case 2:
    			Intent i_2 = new Intent(getApplicationContext(),TitleTabBookmarksActivity.class);
    			startActivity(i_2);
    			break;
    		case 3:
    			Intent i_3 = new Intent(getApplicationContext(), PowerSearch.class);
    			startActivity(i_3);
    			break;
    		case 4:
    			new AlertDialog.Builder(this).setTitle("Report an issue").setIcon(
    					android.R.drawable.ic_dialog_alert).setView(
    					new EditText(this)).setPositiveButton("Submit", null)
    					.setNegativeButton("Cancel", null).show();
    			break;
    		case 5:
    			Intent i_5 = new Intent(getApplicationContext(), CommonCoreSearch.class);
    			startActivity(i_5);
    			break;
    		case 6:
    			Intent i_6 = new Intent(getApplicationContext(), BarnSnapshotsActivity.class);
    			startActivity(i_6);
    			break;
    		}
    		
    	}
		return true;
    	
    }
    private TextWatcher filterTextWatcher = new TextWatcher() {

        public void afterTextChanged(Editable s) {
        }

        public void beforeTextChanged(CharSequence s, int start, int count,
                int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before,
                int count) {
            adapter.getFilter().filter(s);
        }

    };
    
    // implement actionbar Navigation 
	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
		
		if(pref.getInt("lastPos", 0) == itemPosition){
			RegCourse.SELECTED_SEM = semesters[itemPosition];
			RegCourse.SELECTED_NUM = num_set[itemPosition];
			return false;
		}
	
		
			try {
				String selectedsem = semesters[itemPosition].split(" ")[0].toLowerCase();
				String src = "sems/" + selectedsem + "/" + MySQLiteHelper.DATABASE_NAME;
				File dst = new File(MySQLiteHelper.DB_PATH + MySQLiteHelper.DATABASE_NAME);
				CommonUtils.copyFromAsset(src, dst, this.getApplicationContext());
				//handle pref to save current sem selected
				SharedPreferences.Editor editor = pref.edit();
				editor.putInt("lastPos", itemPosition);
				System.out.println("Copier - LastPos=" + itemPosition);
				editor.commit();
				// end pref
				RegCourse.SELECTED_SEM = semesters[itemPosition];
				RegCourse.SELECTED_NUM = num_set[itemPosition];
				Intent intent = new Intent(this.getApplicationContext(), RegCourse.class);
				startActivity(intent);
				this.finish();
			} catch (IOException e) {
				Log.d("DEBUG : ", "Error in switching sem database");
				e.printStackTrace();
			}
		
		return false;
	}

}
