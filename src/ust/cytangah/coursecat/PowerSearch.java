package ust.cytangah.coursecat;

import java.util.List;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;

import ust.cytangah.coursecat.GlobalData;
import ust.cytangah.coursecat.R;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.ListActivity;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.PaintDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;


import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;
import com.actionbarsherlock.widget.SearchView;


public class PowerSearch extends SherlockActivity{
	private ActionBar actionbar;
	private EditText searchCode;
	private EditText fromTimeHr;
	private EditText fromTimeMM;
	private EditText fromTimePeriod;
	private EditText toTimeHr;
	private EditText toTimeMM;
	private EditText toTimePeriod;
	private Button powerSearchButton;
	String[] weekday = new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
	String[] weekparams = new String[]{"mo", "tu", "we", "th", "fr"};
	private ListAdapter mAdapter;
	private ListView list;
	private CheckBox monday;
	private CheckBox tuesday;
	private CheckBox wednesday;
	private CheckBox thursday;
	private CheckBox friday;
	private CheckBox[] allWeeks = new CheckBox[]{monday,tuesday,wednesday,thursday,friday};
	private int listSelCount = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.powersearch);
        this.setTitle(RegCourse.SELECTED_SEM);
        searchCode = (EditText)findViewById(R.id.searchCode);
        fromTimeHr = (EditText)findViewById(R.id.fromTimeHr);
        fromTimeMM = (EditText)findViewById(R.id.fromTimeMM);
        fromTimePeriod = (EditText)findViewById(R.id.fromTimePeriod);
        toTimeHr = (EditText)findViewById(R.id.toTimeHr);
        toTimeMM = (EditText)findViewById(R.id.toTimeMM);
        toTimePeriod = (EditText)findViewById(R.id.toTimePeriod);
        monday = (CheckBox)findViewById(R.id.mo);
        tuesday = (CheckBox)findViewById(R.id.tu);
        wednesday = (CheckBox)findViewById(R.id.we);
        thursday = (CheckBox)findViewById(R.id.th);
        friday = (CheckBox)findViewById(R.id.fr);
        
        powerSearchButton = (Button)findViewById(R.id.powerSearchButton);
        
        powerSearchButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(), PowerSearchResult.class);
				String weekday = "%";
				i.putExtra("searchCode", searchCode.getText().toString());
				i.putExtra("fromTimeHr", fromTimeHr.getText().toString());
				i.putExtra("fromTimeMM", fromTimeMM.getText().toString());
				i.putExtra("fromTimePeriod", fromTimePeriod.getText().toString());
				i.putExtra("toTimeHr", toTimeHr.getText().toString());
				i.putExtra("toTimeMM", toTimeMM.getText().toString());
				i.putExtra("toTimePeriod", toTimePeriod.getText().toString());
				if(monday.isChecked() == true){
					weekday = weekday + "%mo%";
				}
				if(tuesday.isChecked() == true){
					weekday = weekday + "%tu%";
				}
				if(wednesday.isChecked() == true){
					weekday = weekday + "%we%";
				}
				if(thursday.isChecked() == true){
					weekday = weekday + "%th%";
				}
				if(friday.isChecked() == true){
					weekday = weekday + "%fr%";
				}
				i.putExtra("week", weekday);
			
				startActivity(i);	
			}	
        });
    }

    
}
