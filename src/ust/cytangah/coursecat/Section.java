package ust.cytangah.coursecat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import ust.cytangah.coursecat.R;
import ust.cytangah.sectioncontentprovider.SectionsContentProvider;

import com.actionbarsherlock.ActionBarSherlock;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;


import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.MergeCursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class Section extends SherlockListFragment implements LoaderManager.LoaderCallbacks<Cursor>{
private TextView title;
private ListView list;
private ListAdapter adapter;
private MyCursorAdapter mAdapter;
private String codeTitle;
private String fullTitle;
private ProgressDialog progDialog;
public Handler handler = new Handler();
ActionBarSherlock mSherlock = ActionBarSherlock.wrap(getSherlockActivity());
protected List<String> quotaTextList = new ArrayList<String>();
private Handler quotaHandler = new Handler(){
	@Override
	public void handleMessage(Message msg){
		super.handleMessage(msg);
		mAdapter.notifyDataSetChanged();
		progDialog.dismiss();
	}
};

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	
	    View v = inflater.inflate(R.layout.section, container, false);
	    return v;
	}
	@Override
    public void onActivityCreated(Bundle savedInstanceState) {
     super.onActivityCreated(savedInstanceState);
     
	    list = (ListView)this.getView().findViewById(android.R.id.list);
	    title = (TextView)this.getView().findViewById(R.id.title);

	    title.setText("");
	    init();
	    //Add a empty list.
	    
	    String[] from = {MySQLiteHelper.COLUMN_SECTION, "group_concat(time)",MySQLiteHelper.COLUMN_ROOM, MySQLiteHelper.COLUMN_INSTRUCTOR};
        int[] to = {R.id.section1,R.id.section2,R.id.section3,R.id.section4};
        /*
        mAdapter = new SimpleCursorAdapter(this.getSherlockActivity(),
        		R.layout.section_three_line,
        		null,
        		from,
        		to,
        		0);*/
        mAdapter = new MyCursorAdapter(this.getSherlockActivity(),R.layout.section_three_line,null,from,to);
        list.setAdapter(mAdapter);
        for(int i = 0; i< list.getCount(); i++ ){
        	Log.d("Add to List : " , Integer.toString(i));
	    	quotaTextList.add(Integer.toString(i));
	    }
	    this.getSherlockActivity().getSupportLoaderManager().initLoader(0, null, this);
	    
	    }
	private void init(){
		MySQLiteHelper mhelper = new MySQLiteHelper(this.getSherlockActivity());
		SQLiteDatabase database = mhelper.getReadableDatabase();
		Cursor c = database.query(MySQLiteHelper.TABLE_COURSES, new String[]{MySQLiteHelper.COLUMN_CODE, MySQLiteHelper.COLUMN_TITLE}, "code=?", new String[]{Detail.code}, null, null, null);
		if(c.moveToFirst()){
			codeTitle = c.getString(c.getColumnIndex("code"));
			fullTitle = c.getString(c.getColumnIndex("title"));
			title.setText(codeTitle + fullTitle);
			c.close();
			database.close();
		}
	}
	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		String[] projection = { MySQLiteHelper.COLUMN_ID, MySQLiteHelper.COLUMN_CODE, MySQLiteHelper.COLUMN_SECTION, "group_concat(" + MySQLiteHelper.COLUMN_TIME +")", MySQLiteHelper.COLUMN_ROOM , MySQLiteHelper.COLUMN_INSTRUCTOR};
		CursorLoader cursorLoader = new CursorLoader(this.getSherlockActivity(),
				SectionsContentProvider.CONTENT_URI, projection, "code=?", new String[]{codeTitle}, null);
		return cursorLoader;
	}
	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor data) {
		mAdapter.swapCursor(data);
	}
	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		mAdapter.swapCursor(null);		
	}

	public class MyCursorAdapter extends CursorAdapter {
		private final LayoutInflater mInflater;
		
		public MyCursorAdapter(Context context, int layout, Cursor c,
				String[] from, int[] to) {
			super(context, c);
			 mInflater=LayoutInflater.from(context);
			
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			// TODO Auto-generated method stub
			TextView section = (TextView)view.findViewById(R.id.section1);
			Typeface light =  Typeface.createFromAsset(Section.this.getSherlockActivity().getAssets(), "fonts/Roboto-Light.ttf");
			section.setTypeface(light);
			TextView time = (TextView)view.findViewById(R.id.section2);
			TextView room = (TextView)view.findViewById(R.id.section3);
			TextView quota = (TextView)view.findViewById(R.id.quota);
			TextView instructor = (TextView)view.findViewById(R.id.section4);
			
			section.setText(cursor.getString(cursor.getColumnIndex(MySQLiteHelper.COLUMN_SECTION)));
			//time.setText(cursor.getString(cursor.getColumnIndex( "group_concat(time)")));
			String temp = cursor.getString(cursor.getColumnIndex( "group_concat(time)"));
			temp = temp.replaceAll(",", "\r\n");
			time.setText(temp);
			room.setText(cursor.getString(cursor.getColumnIndex(MySQLiteHelper.COLUMN_ROOM)));
			instructor.setText(cursor.getString(cursor.getColumnIndex(MySQLiteHelper.COLUMN_INSTRUCTOR)));
			if(quotaTextList.size()>0){
			quota.setText(quotaTextList.get(cursor.getPosition()));
			}
			}
		

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			final View view=mInflater.inflate(R.layout.section_three_line,parent,false); 
	        return view;
		}
		
		

	}

	
	@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater mInflater) {
		super.onCreateOptionsMenu(menu, mInflater);
        menu.add(0, 1, 200, "Update Quota")
            .setIcon(R.drawable.ic_action_refresh)
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.add(0, 2, 100, "Add to My List")
        	.setIcon(R.drawable.ic_bookmark)
        	.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

    }	
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
    		switch(item.getItemId()){
    		case 1:
    			//TODO Check Network Status
    			progDialog = ProgressDialog.show
                (
                  this.getSherlockActivity(),
                  "Updating Quota",
                  "Please wait",
                  true
                );
    			new Thread()
    			{
    				public void run(){
    					try {  //Thread Content
    						Log.d("myDEBUG", "startdownloading");
    						Document doc = Jsoup.connect(GlobalData.url + RegCourse.SELECTED_NUM + "/subject/" + Detail.code).get();
    						Log.d("list length", Integer.toString(list.getCount()));
    						doc.select(".quotadetail").remove();
    						
    						for(int i = 0; i < list.getCount(); i++){
    							Element sectionTable = doc.select("h2:contains(" + Detail.code + ")").get(0).nextElementSibling();
    							Elements rows = sectionTable.select("tr.newsect");
    							Elements tds = rows.eq(i).select("td");
    							String quotaText = tds.eq(4).text() + " " +
    										tds.eq(5).text() + " " +
    										tds.eq(6).text() + " " +
    										tds.eq(7).text();
    							quotaTextList.add(quotaText);
    							Log.d("add : ", Integer.toString(i));
    							
    						}
    					} catch (IOException e) {
    						e.printStackTrace();
    					} finally{
    						quotaHandler.sendEmptyMessage(0);
    					}		
    				}		
    			}.start();
    			
    			break;
    			//END Update quota
    		case 2: //Add to My List
    			MyList mylist = new MyList();
    			if(mylist.create(Detail.code)){
    				Toast.makeText(getSherlockActivity(), "Added to bookmarks", Toast.LENGTH_SHORT).show();
    			}else{
    				Toast.makeText(getSherlockActivity(), "Bookmark already exist!", Toast.LENGTH_SHORT).show();
    			}
    			mylist.close();
    		}
		return true;
    	
    }
	
	
}
