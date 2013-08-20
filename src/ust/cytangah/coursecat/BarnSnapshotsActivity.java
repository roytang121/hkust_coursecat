package ust.cytangah.coursecat;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import ust.cytangah.coursecat.R;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;

public class BarnSnapshotsActivity extends SherlockActivity {
	final Context context = this;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.barnsnapshotactivity);
		ActionBar actionbar = this.getSupportActionBar();
		actionbar.setTitle("Barns Snapshots");
		
		GridView gridview = (GridView) findViewById(R.id.barnsnapshot_gridview);
		gridview.setAdapter(new BarnImageAdapter(this));
		gridview.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position,
					long id) {
				switch (position){
				case 0:
				case 1:{
					AlertDialog.Builder builder = new AlertDialog.Builder(context);
					builder.setMessage("Barn A Teaching Area : Rm 4402 (Lift 17/18)")
							.setTitle("Room");
					builder.setPositiveButton("Confirm", null);
					AlertDialog dialog = builder.create();
					dialog.show();}
				break;
				case 2:
				case 3:{
					AlertDialog.Builder builder = new AlertDialog.Builder(context);
					builder.setMessage("Barn B Entrance Area : Rm 1101")
							.setTitle("Room");
					builder.setPositiveButton("Confirm", null);
					AlertDialog dialog = builder.create();
					dialog.show();}
				break;
				case 4:
				case 5:{
					AlertDialog.Builder builder = new AlertDialog.Builder(context);
					builder.setMessage("Barn C General Area : Rm 4580 (Lift 27/28)")
							.setTitle("Room");
					builder.setPositiveButton("Confirm", null);
					AlertDialog dialog = builder.create();
					dialog.show();}
				break;
					
			}
		}
	});
	}
	public class BarnImageAdapter extends BaseAdapter {
		private Context mContext;
		
		public BarnImageAdapter(Context c) {
			mContext = c;
			Log.d("DEBUG", "setContext : True");
		}

		public int getCount() {
			return mUrls.length;
		}

		public Object getItem(int arg0) {
			return null;
		}

		public long getItemId(int arg0) {
			return 0;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView imageView;
			if (convertView == null) {
				imageView = new ImageView(mContext);
	            //imageView.setLayoutParams(new GridView.LayoutParams(100, 100));
	            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
	            imageView.setPadding(8, 8, 8, 8);
	            Log.d("DEBUG", "convertView == null : True");
			} else {
				imageView = (ImageView) convertView;
				Log.d("DEBUG", "convertView == null : False");
			}
			
			UrlImageViewHelper.setUrlDrawable(imageView, mUrls[position], null, 60000);
			//imageView.setImageResource(R.drawable.ic_action_search);
			return imageView;
		}
		
		//Url References
		private String[] mUrls = {
				"http://www.ust.hk/~realcam/video0.1.jpg",
				"http://www.ust.hk/~realcam/video2.0.jpg",
				"http://www.ust.hk/~realcam/video1.1.jpg",
				"http://www.ust.hk/~realcam/video2.1.jpg",
				"http://www.ust.hk/~realcam/video0.0.jpg",
				"http://www.ust.hk/~realcam/video1.0.jpg"
		};
	}
}
