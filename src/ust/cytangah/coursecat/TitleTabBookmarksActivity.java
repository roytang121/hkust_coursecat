package ust.cytangah.coursecat;

import ust.cytangah.coursecat.MyEnrolledFragment.DownloadEnrolledCourseTask;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.viewpagerindicator.PageIndicator;
import com.viewpagerindicator.TitlePageIndicator;

public class TitleTabBookmarksActivity extends SherlockFragmentActivity {
private Context c;
private ViewPager mPager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_title_tab_bookmarks);
		this.c = this;
		ActionBar actionbar = this.getSupportActionBar();
		actionbar.setDisplayShowTitleEnabled(false);
		//actionbar.setDisplayShowHomeEnabled(false);
		BookmarkFragmentAdapter mAdapter;
		PageIndicator mIndicator;
		mAdapter = new BookmarkFragmentAdapter(this.getSupportFragmentManager());
		mPager = (ViewPager)this.findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);
		mIndicator = (TitlePageIndicator)this.findViewById(R.id.indicator);
		mIndicator.setViewPager(mPager);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.activity_title_tab_bookmarks, menu);
		menu.add(0, 0, 100, "Update")
        .setIcon(R.drawable.ic_action_refresh)
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		 menu.add(0, 1, 300, "Account")
         .setIcon(android.R.drawable.ic_menu_preferences)
         .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		 menu.add(0, 2, 200, "Calendar")
		 .setIcon(android.R.drawable.ic_menu_my_calendar)
		 .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		return true;
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()){
		case 0:
			SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(c);
			SharedPreferences.Editor editor = pref.edit();
			editor.remove("enrollhtml");
			editor.commit();
			Toast.makeText(this, "Reopen bookmarks to update", Toast.LENGTH_SHORT).show();
			break;
		case 1:
			//Toast.makeText(TitleTabBookmarksActivity.this, "Not yet Available", Toast.LENGTH_SHORT).show();
			AccoutInfoHelper.showdialog(c);
			break;
		case 2:
			Intent i_2 = new Intent(TitleTabBookmarksActivity.this, MyCalendarActivity.class);
			startActivity(i_2);
			break;
		}
		return true;
		}
	class BookmarkFragmentAdapter extends FragmentPagerAdapter{
		protected  String[] CONTENT = new String[]{"Bookmarks", "Enrolled"};
		private int mCount = CONTENT.length;
		public BookmarkFragmentAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			switch(position % CONTENT.length){
			case 0:
				return MyListFragment.newInstance();
			case 1:
				return MyEnrolledFragment.newInstance();
			default:
				return null;
			}
			
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mCount;
		}
		
		@Override
	    public CharSequence getPageTitle(int position) {
	      return CONTENT[position % CONTENT.length];
	    }
	}

}
