package ust.cytangah.coursecat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AccoutInfoHelper {
	
	private static SharedPreferences pref;
	private static EditText username;
	private static EditText password;
	public static void showdialog(Context c){
		LayoutInflater factory = LayoutInflater.from(c);
		final View dialogView = factory.inflate(R.layout.accountinfodialog, null);
		username = (EditText)dialogView.findViewById(R.id.username_input);
		password = (EditText)dialogView.findViewById(R.id.password_input);
		pref = PreferenceManager.getDefaultSharedPreferences(c);

		username.setText(pref.getString("usr", ""));
		password.setText(pref.getString("pwd", ""));
		
		AlertDialog.Builder builder = new AlertDialog.Builder(c);
		builder.setTitle("Account Info");
		builder.setView(dialogView);
		builder.setPositiveButton("Update", new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				SharedPreferences.Editor editor = pref.edit();
				editor.putString("usr", username.getText().toString());
				editor.putString("usr", username.getText().toString());
				editor.commit();
			}});
		builder.setNegativeButton("Logout", new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				SharedPreferences.Editor editor = pref.edit();
				editor.remove("usr");
				editor.remove("pass");
				editor.commit();
			}});
		
		AlertDialog dialog = builder.create();
		dialog.show();
		
	}
}
