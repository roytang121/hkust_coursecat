package ust.cytangah.coursecat;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.webkit.WebView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;


public class MyCalendarActivity extends SherlockActivity {
private Context c;
private WebView webview;
public static final String CALENDAR_URL = "https://sisprod.psft.ust.hk/psc/SISPROD/EMPLOYEE/HRMS/c/SA_LEARNER_SERVICES.SSR_SSENRL_SCHD_W.GBL?Page=SSR_SS_WEEK&Action=A&ExactKeys=Y&EMPLID=20118554&&&";
private final String mimeType = "text/html";
private final String encoding = "UTF-8";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_calendar);
		this.c = this;
		this.setTitle("My Calendar");
		webview = (WebView) findViewById(R.id.calendarWebView);
		
        // Empty webview
        //webview.loadData("", mimeType, encoding);
        
		new DownloadCalendarTask().execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.activity_my_calendar, menu);

		menu.add(0, 0, 200, "Update")
        .setIcon(R.drawable.ic_action_refresh)
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		return true;
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case 0:
			SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(c);
			SharedPreferences.Editor editor = pref.edit();
			editor.remove("calendar");
			editor.commit();
			new DownloadCalendarTask().execute();
			break;
		}
		return false;
	}
	class DownloadCalendarTask extends AsyncTask<String, Void, String>{
		private ProgressDialog pd;

		@Override
		protected void onPreExecute(){
			pd = ProgressDialog.show(c, "Loading..", "Please wait");
		}
		@Override
		protected String doInBackground(String... arg0) {
			
			SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(c);
			if(!pref.contains("calendar")){
				String usr = pref.getString("usr", "na");
				String pass = pref.getString("pass", "na");
				SISClient sisclient = new SISClient(usr,pass);
				
				String calendar = sisclient.getpage(CALENDAR_URL);
				
				//parse
				Document doc = Jsoup.parse(calendar);
				Elements tables = doc.select(".PABACKGROUNDINVISIBLEWBO");
				tables = tables.eq(10);
				calendar = "<html><head><link rel=\"stylesheet\" type=\"text/css\" href=\"cal.css\" /></head><body>";
				calendar = calendar + tables.html() + "</body></html>";
				
				SharedPreferences.Editor editor = pref.edit();
				editor.putString("calendar", calendar);
				editor.commit();
				return calendar;
			}else{
				return pref.getString("calendar", "errors");
			}
			
		}
		
		@Override
		protected void onPostExecute(String html){
			pd.dismiss();
			webview.loadDataWithBaseURL("file:///android_asset/", html, mimeType, encoding, null);
		}
	}
}
