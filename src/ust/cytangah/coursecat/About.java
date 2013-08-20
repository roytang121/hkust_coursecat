package ust.cytangah.coursecat;

import java.util.ArrayList;
import java.util.List;

import ust.cytangah.coursecat.R;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class About extends Activity {
	ListView list;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/* FULL SCREEN - DUMPED 15/1/2013
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);*/

		setContentView(R.layout.about);
		
		ArrayList<AboutItem> aboutItems = new ArrayList<AboutItem>();
		AboutItem help = new AboutItem("Author", "Roy Tang");
		AboutItem donation = new AboutItem("Contact", "cytangah@ust.hk");
		AboutItem disclaimer = new AboutItem("Disclaimer", "This app is not an official release from HKUST. All information provided should not be treaten as offical data. No compensation but apology for any invalid information.");
		AboutItem appver = new AboutItem("App version", RegCourse.USER_VERSION);
		aboutItems.add(help);
		aboutItems.add(donation);
		aboutItems.add(disclaimer);
		aboutItems.add(appver);
		
		
		list = (ListView) findViewById(R.id.aboutlist);
		AboutAdapter adapter = new AboutAdapter(this, R.layout.about_two_line, aboutItems);
		list.setAdapter(adapter);
	}

	private class AboutItem {
		private String parent;
		private String child;

		public AboutItem(String parent, String child) {
			this.parent = parent;
			this.child = child;
		}

		public String parent() {
			return this.parent;
		}

		public String child() {
			return this.child;
		}
	}

	private class AboutAdapter extends ArrayAdapter<AboutItem> {
		private ArrayList<AboutItem> items;

		public AboutAdapter(Context context, int textViewResourceId,
				ArrayList<AboutItem> items) {
			super(context, textViewResourceId, items);
			this.items = items;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.about_two_line, null);

				AboutItem item = items.get(position);
				if (item != null) {
					TextView title = (TextView) v.findViewById(R.id.about_text_1);
					TextView content = (TextView) v.findViewById(R.id.about_text_2);
					if (title != null) {
						title.setText(item.parent());
						content.setText(item.child());
					}
				}

			}
			return v;
		}

	}
}
