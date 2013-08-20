package ust.cytangah.coursecat;

import java.io.IOException;
import java.util.ArrayList;
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

import android.content.SharedPreferences;
import android.util.Log;

public class SISClient {
	private String usr;
	private String pass;
	private DefaultHttpClient httpclient;
	private HttpResponse response;
	private HttpEntity entity;
	private SharedPreferences pref;
	public SISClient(String usr, String pass){
		this.usr = usr;
		this.pass = pass;
		httpclient = new DefaultHttpClient();
		
		if(login()){
			Log.d("DEBUG:" ,"SISClient:Login Success");
		}else{
			Log.d("DEBUG:" ,"SISClient:Login Failed");
		}
	}
	
	private boolean login(){
		HttpGet httpget = new HttpGet("https://login.psft.ust.hk/cas/login?method=POST&service=https%3A%2F%2Fsisprod.psft.ust.hk%2Fpsp%2FSISPROD%2FEMPLOYEE%2FHRMS%2Fc%2FSA_LEARNER_SERVICES.SSS_STUDENT_CENTER.GBL%3Fpslnkid%3DZ_HC_SSS_STUDENT_CENTER_LNK%26FolderPath%3DPORTAL_ROOT_OBJECT.Z_HC_SSS_STUDENT_CENTER_LNK%26IsFolder%3Dfalse%26IgnoreParamTempl%3DFolderPath%252cIsFolder%26%26cmd%3Dlogin%26languageCd%3DENG%26userid%3Dcasproxy%26pwd%3Dna");
		try {
			response = httpclient.execute(httpget);
			entity = response.getEntity();
			System.out.println("Login form get: " + response.getStatusLine());
			Document doc = Jsoup.parse(EntityUtils.toString(entity));
			String lt = doc.select("input[name=lt]").attr("value");
			Log.d("ROY", "HIDDEN : " + lt);
			HttpPost httpost = new HttpPost("https://login.psft.ust.hk/cas/login?method=POST&service=https%3A%2F%2Fsisprod.psft.ust.hk%2Fpsp%2FSISPROD%2FEMPLOYEE%2FHRMS%2Fc%2FSA_LEARNER_SERVICES.SSS_STUDENT_CENTER.GBL%3Fpslnkid%3DZ_HC_SSS_STUDENT_CENTER_LNK%26FolderPath%3DPORTAL_ROOT_OBJECT.Z_HC_SSS_STUDENT_CENTER_LNK%26IsFolder%3Dfalse%26IgnoreParamTempl%3DFolderPath%252cIsFolder%26%26cmd%3Dlogin%26languageCd%3DENG%26userid%3Dcasproxy%26pwd%3Dna");
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("username", usr));
			nvps.add(new BasicNameValuePair("password", pass));
			Log.d("DEBUG : ", "nvps.usr=" + usr);
			Log.d("DEBUG : ", "nvps.pass=" + pass);
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
				return false;
			}else{
				httpost = new HttpPost(action);
				nvps.clear();
				nvps.add(new BasicNameValuePair("ticket", ticket));
				httpost.setEntity(new UrlEncodedFormEntity(nvps,"big5"));
				response = httpclient.execute(httpost);
				entity = response.getEntity();
				entity.consumeContent();
			}
			//Login Finished//
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return true;
	}
	
	public String getpage(String html){
		String result = "failed get page";
		HttpGet httpget = new HttpGet(html);
		try {
			response = httpclient.execute(httpget);
			entity = response.getEntity();
			result = EntityUtils.toString(entity); // To be parsed
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
		
	}
}
