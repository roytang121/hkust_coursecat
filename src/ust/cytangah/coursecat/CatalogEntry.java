package ust.cytangah.coursecat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import ust.cytangah.contentprovider.CourseContentProvider;
import ust.cytangah.coursecat.R;


import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockListFragment;

import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SimpleCursorAdapter;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class CatalogEntry extends SherlockListFragment{

private ListView list;
private List<HashMap<String, String>> lv;
private ListAdapter adapter;
private SimpleCursorAdapter xAdapter;
private String codeTitle;
private String fullTitle;
private String attrText;
private String vectorText;
private String coRegText;
private String preRegText;
private String exclusionText;
private String prevcodeText;
private String descText;
private TextView title;
private TextView attr;
private TextView vector;
private TextView preReg;
private TextView coReg;
private TextView exclusion;
private TextView prevcode;
private TextView desc;
private View linearLayout;
private Handler handler;
private boolean mReady;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
	    View v = inflater.inflate(R.layout.catalogentry, container, false);

	    return v;
	    }
	
	@Override
    public void onActivityCreated(Bundle savedInstanceState) {
     super.onActivityCreated(savedInstanceState);
	
     	attr = new TextView(CatalogEntry.this.getSherlockActivity());
		vector = new TextView(CatalogEntry.this.getSherlockActivity());
		preReg = new TextView(CatalogEntry.this.getSherlockActivity());
		coReg = new TextView(CatalogEntry.this.getSherlockActivity());
		exclusion = new TextView(CatalogEntry.this.getSherlockActivity());
		prevcode = new TextView(CatalogEntry.this.getSherlockActivity());
		desc = new TextView(CatalogEntry.this.getSherlockActivity());
	    title = (TextView)this.getView().findViewById(R.id.title);
	    
	    doInBackground();
	    onPostExecute();

	    
	}
	
	private void doInBackground(){
		MySQLiteHelper mhelper = new MySQLiteHelper(CatalogEntry.this.getSherlockActivity());
		SQLiteDatabase database = mhelper.getReadableDatabase();
		Cursor c = database.query(MySQLiteHelper.TABLE_COURSES, null, "code=?", new String[]{Detail.code}, null, null, null);
		if(c.moveToFirst()){
			codeTitle = c.getString(c.getColumnIndex("code"));
			fullTitle = c.getString(c.getColumnIndex("title"));
			attrText = c.getString(c.getColumnIndex("attr"));
			vectorText = c.getString(c.getColumnIndex("vector"));
			preRegText = c.getString(c.getColumnIndex("pre_reg"));
			coRegText = c.getString(c.getColumnIndex("co_reg"));
			exclusionText = c.getString(c.getColumnIndex("exclusion"));
			prevcodeText = c.getString(c.getColumnIndex("prev_code"));
			descText = c.getString(c.getColumnIndex("description"));
			c.close();
			database.close();
			}
		}

	private void onPostExecute() {
		
			
			title.setText(codeTitle + fullTitle);
	  		attr.setText(attrText);
	  		vector.setText(vectorText);
	  		preReg.setText(preRegText);
	  		coReg.setText(coRegText);
	  		exclusion.setText(exclusionText);
	  		prevcode.setText(prevcodeText);
	  		desc.setText(descText);
	  		
	  		TextView[] map = new TextView[]{attr,vector,preReg,coReg,exclusion,prevcode,desc};
	  		String[] mapName = new String[]{"ATTRIBUTES", "VECTOR", "PRE-REQUISITE", "CO-REQUISITE", "EXCLUSION", "PREV-CODE", "DETAILS"};
	  		int i=0;
	  		int color=0;
	  		for(TextView n : map){
	  			if (!(n.getText().equals("n/a"))){
	  				LinearLayout linearLayout = (LinearLayout)CatalogEntry.this.getView().findViewById(R.id.layout1);
			  		View layoutView = null;

			  		layoutView = LayoutInflater.from(getSherlockActivity()).inflate(R.layout.my_field, null);

	  				layoutView.setPadding(20, 0, 20, 0);
			  		TextView th = new TextView(CatalogEntry.this.getSherlockActivity());
			  			th.setText(mapName[i] + " : ");
			  			th.setId(0);
			  			th.setPadding(30, 30, 0, 0);
			  			th.setLayoutParams((new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT)));
			  			th.setTextColor(getResources().getColor(android.R.color.tertiary_text_light));
			  		
			  		View sepLine = new View(CatalogEntry.this.getSherlockActivity());
			  			sepLine.setId(1);
			  			sepLine.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 3));
			  			sepLine.setPadding(15, 0, 15, 5);
			  			sepLine.setBackgroundColor(this.getResources().getColor(android.R.color.holo_blue_light));
			  			
			  			n.setLayoutParams((new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT)));
			  			n.setId(2);
			  			n.setPadding(0, 0, 0, 20);
			  			((LinearLayout)layoutView).addView(th);
			  			((LinearLayout)layoutView).addView(sepLine);
			  			((LinearLayout)layoutView).addView(n);
			  		linearLayout.addView(layoutView);
	  			}
	  			i++;
	  		}
		
    }
}


