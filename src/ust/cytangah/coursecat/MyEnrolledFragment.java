package ust.cytangah.coursecat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class MyEnrolledFragment extends SherlockFragment {
	private ListView list;
	private ArrayList<String> temp;
	private ArrayList<HashMap<String,String>> mlist;
	private SimpleCursorAdapter mAdapter;
	private Thread mThread;
	private SherlockFragment f = this;
	private String[] account = new String[2];
	private boolean isFileFound;
	private ProgressDialog pd;
	private Context c;
	private EditText usr;
	private EditText pass;
	private Button submit;
	private LinearLayout enrollayout;
	private RelativeLayout loginlayout;
	private String _usr;
	private String _pass;
	
	public static MyEnrolledFragment newInstance(){
		MyEnrolledFragment mf = new MyEnrolledFragment();
		Bundle bundle = new Bundle();
		return mf;
	}
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		c = this.getSherlockActivity();
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(c);
		
		
		
		if(!pref.getString("usr", "null").equals("null")){
			View v = inflater.inflate(R.layout.login, container, false);
			enrollayout = (LinearLayout)v.findViewById(R.id.enrolllayout);
			list = (ListView)v.findViewById(R.id.enrolllist);
			ArrayAdapter<String> em = new ArrayAdapter<String>(c, android.R.layout.simple_list_item_1, new String[]{"Loading......."});
			list.setAdapter(em);
			enrollayout.setVisibility(View.VISIBLE);
			
			loginlayout = (RelativeLayout)v.findViewById(R.id.loginlayout);
			loginlayout.setVisibility(View.GONE);
			_usr = pref.getString("usr", "na");
			_pass = pref.getString("pass", "na");

			new DownloadEnrolledCourseTask().execute("normal");
			return v;
		}else{
			View v = inflater.inflate(R.layout.login, container, false);
			
			enrollayout = (LinearLayout)v.findViewById(R.id.enrolllayout);
			list = (ListView)v.findViewById(R.id.enrolllist);
			ArrayAdapter<String> em = new ArrayAdapter<String>(c, android.R.layout.simple_list_item_1, new String[]{"Empty"});
			list.setAdapter(em);
			enrollayout.setVisibility(View.GONE);
			
			loginlayout = (RelativeLayout)v.findViewById(R.id.loginlayout);
			loginlayout.setVisibility(View.VISIBLE);
			
			usr = (EditText)v.findViewById(R.id.usr);
			pass = (EditText)v.findViewById(R.id.pass);
			submit = (Button)v.findViewById(R.id.submit);
			
			submit.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(c);
					SharedPreferences.Editor editor = pref.edit();
					Log.d("DEBUG : ", "usr=" + usr.getText().toString());
					Log.d("DEBUG : ", "pass=" + pass.getText().toString());
					editor.putString("usr", usr.getText().toString());
					editor.putString("pass", pass.getText().toString());
					editor.commit();
					Toast.makeText(c, "Profile updated", Toast.LENGTH_SHORT).show();
					
					pd = ProgressDialog.show(c, "Loading..", "Please wait");
					//it should not be null since the commit
					_usr = pref.getString("usr", "na");
					_pass = pref.getString("pass", "na");
					
					new DownloadEnrolledCourseTask().execute("no_profile");
				}});
			//End onclick
			return v;
		}
		
		
	}
	
	class DownloadEnrolledCourseTask extends AsyncTask<String, Void, String>{

		@Override
		protected String doInBackground(String... cmd) {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		String html = null;
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(c);
		if(pref.getString("enrollhtml", "null").equals("null")){
		try {
			HttpGet httpget = new HttpGet("https://login.psft.ust.hk/cas/login?method=POST&service=https%3A%2F%2Fsisprod.psft.ust.hk%2Fpsp%2FSISPROD%2FEMPLOYEE%2FHRMS%2Fc%2FSA_LEARNER_SERVICES.SSS_STUDENT_CENTER.GBL%3Fpslnkid%3DZ_HC_SSS_STUDENT_CENTER_LNK%26FolderPath%3DPORTAL_ROOT_OBJECT.Z_HC_SSS_STUDENT_CENTER_LNK%26IsFolder%3Dfalse%26IgnoreParamTempl%3DFolderPath%252cIsFolder%26%26cmd%3Dlogin%26languageCd%3DENG%26userid%3Dcasproxy%26pwd%3Dna");
			HttpResponse response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();
			
			System.out.println("Login form get: " + response.getStatusLine());
			Document doc = Jsoup.parse(EntityUtils.toString(entity));
			String lt = doc.select("input[name=lt]").attr("value");
			Log.d("ROY", "HIDDEN : " + lt);
			HttpPost httpost = new HttpPost("https://login.psft.ust.hk/cas/login?method=POST&service=https%3A%2F%2Fsisprod.psft.ust.hk%2Fpsp%2FSISPROD%2FEMPLOYEE%2FHRMS%2Fc%2FSA_LEARNER_SERVICES.SSS_STUDENT_CENTER.GBL%3Fpslnkid%3DZ_HC_SSS_STUDENT_CENTER_LNK%26FolderPath%3DPORTAL_ROOT_OBJECT.Z_HC_SSS_STUDENT_CENTER_LNK%26IsFolder%3Dfalse%26IgnoreParamTempl%3DFolderPath%252cIsFolder%26%26cmd%3Dlogin%26languageCd%3DENG%26userid%3Dcasproxy%26pwd%3Dna");
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("username", _usr));
			nvps.add(new BasicNameValuePair("password", _pass));
			Log.d("DEBUG : ", "nvps.usr=" + _usr);
			Log.d("DEBUG : ", "nvps.pass=" + _pass);
			nvps.add(new BasicNameValuePair("lt", lt));
			nvps.add(new BasicNameValuePair("_eventId", "submit"));
			nvps.add(new BasicNameValuePair("submit", "LOGIN"));
			httpost.setEntity(new UrlEncodedFormEntity(nvps,"big5"));
			response = httpclient.execute(httpost);
			entity = response.getEntity();
			
			System.out.println("Login form get: " + response.getStatusLine());
			doc = Jsoup.parse(EntityUtils.toString(entity));
			String action = doc.select("form[name=acsForm]").attr("action");
			String ticket = doc.select("textarea[name=ticket]").text();
			System.out.println("[" + action + "],["+ ticket + "]");
			
			//Redirect //
			if(action.isEmpty()){
				return "login_fail";
			}else{
				httpost = new HttpPost(action);
				nvps.clear();
				nvps.add(new BasicNameValuePair("ticket", ticket));
				httpost.setEntity(new UrlEncodedFormEntity(nvps,"big5"));
				response = httpclient.execute(httpost);
				entity = response.getEntity();
				entity.consumeContent();
				
				//Frame : enrolled course //
				httpget = new HttpGet("https://sisprod.psft.ust.hk/psc/SISPROD/EMPLOYEE/HRMS/c/SA_LEARNER_SERVICES.SSS_STUDENT_CENTER.GBL?pslnkid=Z_HC_SSS_STUDENT_CENTER_LNK&FolderPath=PORTAL_ROOT_OBJECT.Z_HC_SSS_STUDENT_CENTER_LNK&IsFolder=false&IgnoreParamTempl=FolderPath%2cIsFolder&PortalActualURL=https%3a%2f%2fsisprod.psft.ust.hk%2fpsc%2fSISPROD%2fEMPLOYEE%2fHRMS%2fc%2fSA_LEARNER_SERVICES.SSS_STUDENT_CENTER.GBL%3fpslnkid%3dZ_HC_SSS_STUDENT_CENTER_LNK&PortalContentURL=https%3a%2f%2fsisprod.psft.ust.hk%2fpsc%2fSISPROD%2fEMPLOYEE%2fHRMS%2fc%2fSA_LEARNER_SERVICES.SSS_STUDENT_CENTER.GBL%3fpslnkid%3dZ_HC_SSS_STUDENT_CENTER_LNK&PortalContentProvider=HRMS&PortalCRefLabel=Student%20Center&PortalRegistryName=EMPLOYEE&PortalServletURI=https%3a%2f%2fsisprod.psft.ust.hk%2fpsp%2fSISPROD%2f&PortalURI=https%3a%2f%2fsisprod.psft.ust.hk%2fpsc%2fSISPROD%2f&PortalHostNode=HRMS&NoCrumbs=yes");
				response = httpclient.execute(httpget);
				entity = response.getEntity();
				html = EntityUtils.toString(entity); // To be parsed
				
				//Cache the enrolled course//
				
				SharedPreferences.Editor editor = pref.edit();
				editor.putString("enrollhtml", html);
				editor.commit();
			}
			//Login Finished//
			
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
            httpclient.getConnectionManager().shutdown();
        }
		}else{
			html = pref.getString("enrollhtml", "na");
		}
		// Html Parsing //
		Document doc = Jsoup.parse(html);
		System.out.println(html);
		Elements table = doc.select(".PSLEVEL1GRIDWBO");
		Elements trs = table.select("tr");
		
		mlist = new ArrayList<HashMap<String,String>>();
		
		for(Element tr : trs){
			HashMap<String,String> item = new HashMap<String,String>();
			Elements lecture = tr.select(".PSHYPERLINKDISABLED");
			Elements time = tr.select(".PSLONGEDITBOX");
			if(!lecture.isEmpty())
			{
				item.put("text1", lecture.text());
				Log.d("DEBUG", lecture.text());

				item.put("text2", time.text());
				Log.d("DEBUG", time.text());
				mlist.add(item);
			}
		}
			
			
		
		return cmd[0];
		
		}
		
		@Override
		protected void onPostExecute(String cmd){
			
			Toast.makeText(c, "Updated!", Toast.LENGTH_SHORT).show();
			
			if(cmd.equals("no_profile")){
				pd.dismiss();
				SimpleAdapter mAdapter = new SimpleAdapter(c, mlist, R.layout.custom_two_line, new String[]{"text1","text2"}, new int[]{R.id.text1,R.id.text2});
				list.setAdapter(mAdapter);
				loginlayout.setVisibility(View.GONE);
				enrollayout.setVisibility(View.VISIBLE);
				}else if(cmd.equals("login_fail")){
					AlertDialog.Builder builder = new AlertDialog.Builder(c);
					builder.setTitle("Login Fail");
					builder.setMessage("Please check your username and password");
					pd.dismiss();
					SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(c);
					SharedPreferences.Editor editor = pref.edit();
					editor.remove("usr");
					editor.remove("pass");
					editor.commit();
				AlertDialog dialog = builder.create();
				dialog.show();
				}else{
					SimpleAdapter mAdapter = new SimpleAdapter(c, mlist, R.layout.custom_two_line, new String[]{"text1","text2"}, new int[]{R.id.text1,R.id.text2});
					list.setAdapter(mAdapter);
				}
			
		}
	
		
	}
	@Override
	public void onCreateOptionsMenu(Menu menu,  MenuInflater mInflater) {
		super.onCreateOptionsMenu(menu, mInflater);
		
	}
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()){
		case 0:
			
		}
		return true;
	}
	
}
