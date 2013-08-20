package ust.cytangah.coursecat;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import ust.cytangah.coursecat.R;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockFragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;

public class MyListFragment extends SherlockFragment {
	private ListView list;
	private ArrayList<String> temp;
	private SimpleCursorAdapter mAdapter;
	private Thread mThread;
	private SherlockFragment f = this;
	
	public static MyListFragment newInstance(){
		MyListFragment mf = new MyListFragment();
		Bundle bundle = new Bundle();
		return mf;
	}
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View v = inflater.inflate(R.layout.main, container, false);
		
		list = (ListView) v.findViewById(android.R.id.list);

		ArrayAdapter<String> tmpAdapter = new ArrayAdapter<String>(this.getSherlockActivity(),
				android.R.layout.simple_list_item_1, new String[] {});
		list.setAdapter(tmpAdapter);
		
		list.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				TextView code = (TextView)view.findViewById(R.id.text1);
				String codeText = (String) code.getText();
				Intent i = new Intent(f.getSherlockActivity(), Detail.class);
				i.putExtra("code", code.getText());
				startActivity(i);
			}});
		this.registerForContextMenu(list);
		
	    mThread = new Thread(new Runnable() {

			@Override
			public void run() {
				File myList = new File("/sdcard/RegCourse/mylist.txt");
				Message message;
				String obj = "run";
				temp = new ArrayList<String>();
				try {
					Scanner in = new Scanner(myList);
					while (in.hasNextLine()) {
						temp.add(in.nextLine());
						// Log.d("DEBUG", in.nextLine() + " loaded");
					}
					message = handler.obtainMessage(1, obj);
					handler.sendMessage(message);
					in.close();

				} catch (FileNotFoundException e) {
				}
			}
		});
		mThread.start();
		return v;
		
	}

	Handler handler = new Handler() {
		

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			String sql = "select * from courses where ";
			if (temp.size() > 0 && temp != null) {
				for (int i = 0; i < temp.size(); i++) {
					if (i != temp.size() - 1)
						sql = sql + "code = '" + temp.get(i) + "'" + " or ";
					else
						sql = sql + "code = '" + temp.get(i) + "'";
				}
				Log.d("SQLDEBUG", sql);
				MySQLiteHelper dbhelper = new MySQLiteHelper(
						f.getSherlockActivity());
				SQLiteDatabase database = dbhelper.getReadableDatabase();
				Cursor c = database.rawQuery(sql, null);
				String[] from = { MySQLiteHelper.COLUMN_CODE,
						MySQLiteHelper.COLUMN_TITLE };
				int[] to = { R.id.text1, R.id.text2 };
				mAdapter = new SimpleCursorAdapter(
						f.getSherlockActivity(), R.layout.custom_two_line, c,
						from, to, 0);
				list.setAdapter(mAdapter);
				database.close();
			}else{
				ArrayAdapter<String> tmpAdapter = new ArrayAdapter<String>(f.getSherlockActivity(),
						android.R.layout.simple_list_item_1, new String[] {"No reconds found."});
				list.setAdapter(tmpAdapter);
			}

		}
	};
	//ContextMenu
	private String selectedCode, selectedTitle;
	private AdapterContextMenuInfo info;
	@Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
		info = (AdapterContextMenuInfo)menuInfo;
        menu.add(0, 1, 100, "Remove from Bookmarks");
        selectedCode = ((TextView) info.targetView.findViewById(R.id.text1)).getText().toString();
       }
	
	@Override
    public boolean onContextItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case 1:
			MyList mylist = new MyList();
			mylist.remove(selectedCode);
			mylist.close();
			mThread.run();
			break;
			
		}
		return false;
	}

}
