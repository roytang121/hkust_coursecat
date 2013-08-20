package ust.cytangah.coursecat;

import ust.cytangah.coursecat.R;

import com.actionbarsherlock.app.SherlockActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;


public class CommonCoreSearch extends SherlockActivity{
private Spinner year_spinner;
private Spinner common_core_spinner;
private ListView common_core_list;
private String[] years = {"3Y 2010 & 2011 cohorts",
						  "3Y 2012 cohort",
						  "4Y 2012 or after"};
private String[] _3Y_2010 = {"A&H",
							 "A&H special list",
							 "SA",
							 "SA special list",
							 "S&T"};
private String[] _3Y_2012 = {"A&H",
							 "SA",
							 "S&T"};
private String[] _4Y_2012 = {"SSC-H",
							 "SSC-SA",
							 "SSC-S&T",
							 "H",
							 "SA",
							 "S&T",
							 "QR",
							 "Arts",
							 "E-Comm",
							 "C-Comm",
							 "HLTH"};
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_core_search);
        this.setTitle(RegCourse.SELECTED_SEM);
        year_spinner = (Spinner)findViewById(R.id.year_spinner);
        common_core_spinner = (Spinner)findViewById(R.id.common_core_spinner);
        common_core_list = (ListView)findViewById(R.id.common_core_list);
        
        ArrayAdapter<String> year_adapter = new ArrayAdapter<String>(this,
        		android.R.layout.simple_spinner_item, years);
        year_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        year_spinner.setAdapter(year_adapter);
        
        ArrayAdapter<String> common_core_adapter = new ArrayAdapter<String>(this,
        		android.R.layout.simple_spinner_item, _3Y_2010);
        common_core_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        common_core_spinner.setAdapter(common_core_adapter);
        
        year_spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
        	
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view,
					int position, long id) {
				switch(position){
					case 0:
					{
						ArrayAdapter<String> common_core_adapter = new ArrayAdapter<String>(adapterView.getContext(),
				        		android.R.layout.simple_spinner_item, _3Y_2010);
				        common_core_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				        common_core_spinner.setAdapter(common_core_adapter);}
				        break;
					case 1:
					{
						ArrayAdapter<String> common_core_adapter = new ArrayAdapter<String>(adapterView.getContext(),
				        		android.R.layout.simple_spinner_item, _3Y_2012);
				        common_core_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				        common_core_spinner.setAdapter(common_core_adapter);}
				        break;
					case 2:
					{
						ArrayAdapter<String> common_core_adapter = new ArrayAdapter<String>(adapterView.getContext(),
				        		android.R.layout.simple_spinner_item, _4Y_2012);
				        common_core_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				        common_core_spinner.setAdapter(common_core_adapter);}
				        break;	
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {			
			}});
       //Secondary Listener
        	common_core_spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){

				@Override
				public void onItemSelected(AdapterView<?> adapterView, View view,
						int position, long id) {
					updateList(common_core_list, year_spinner.getSelectedItemPosition(), common_core_spinner.getSelectedItem().toString());
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub
					
				}});
      //List Adapter
        updateList(common_core_list,year_spinner.getSelectedItemPosition(),common_core_spinner.getSelectedItem().toString());
    }
    
	private void updateList(ListView list, int position, String _type) {
		String year = "";
		String type = "%(%" + _type + "%)%";
		switch(position){
		case 0:
			year = "%2010 & 2011 3Y%";
			break;
		case 1:
			year = "%2012 3Y%";
			break;
		case 2:
			year = "%4Y%";
			break;
		}
		
		MySQLiteHelper dbhelper = new MySQLiteHelper(this);
        SQLiteDatabase database = dbhelper.getReadableDatabase();
        String selectionSQL = "select * from courses where attr like '" + year + "'" + " and attr like '"+ type + "'";
        Log.d("SQLDEBUG", selectionSQL); //debug sql
        Cursor c = database.rawQuery(selectionSQL, null);
        
        String[] from = {MySQLiteHelper.COLUMN_CODE, MySQLiteHelper.COLUMN_TITLE};
        int[] to = {R.id.about_text_1,R.id.about_text_2};
        
        if(c.moveToFirst()){
        	SimpleCursorAdapter mAdapter = new SimpleCursorAdapter(this,
        		R.layout.about_two_line,
        		c,
        		from,
        		to,
        		0);
        list.setAdapter(mAdapter);
        list.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				TextView code = (TextView)view.findViewById(R.id.about_text_1);
				String codeText = (String) code.getText();
				Intent i = new Intent(getApplicationContext(), Detail.class);
				i.putExtra("code", code.getText());
				startActivity(i);
			}});

	}else{
		ArrayAdapter<String> no_result = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new String[]{"No records found."});
	}
    	
}


    
}
