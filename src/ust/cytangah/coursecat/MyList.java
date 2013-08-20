package ust.cytangah.coursecat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class MyList {
	private boolean isExist;
	private ArrayList<String> list;
	private PrintWriter out;

	public MyList() {
		/*
		 * File file = new File("/sdcard/RegCourse/mylist.txt"); try { out = new
		 * PrintWriter(file); Log.d("MYLIST", "DEBUG-mylist.txt loaded x0"); }
		 * catch (FileNotFoundException e) { e.printStackTrace();
		 * file.getParentFile().mkdirs(); Log.d("MYLIST",
		 * "DEBUG-mylist.txt has been created"); try { out = new
		 * PrintWriter(file); Log.d("MYLIST", "DEBUG-mylist.txt loaded x1"); }
		 * catch (FileNotFoundException e1) { e1.printStackTrace();
		 * Log.d("MYLIST", "DEBUG-mylist.txt cannot be loaded"); } }
		 */
	}

	public boolean create(String code) {
		// confirm no repeat
		File file = new File("/sdcard/RegCourse/mylist.txt");
		file.getParentFile().mkdirs();
		String content = null;
		try {
			Scanner in = new Scanner(file);
			while (in.hasNextLine()) {
				if (in.nextLine().equals(Detail.code)) {
					return false;
				}
			}
		} catch (FileNotFoundException e1) {
		}
		
		String path = "/sdcard/RegCourse/mylist.txt";
		try {
			FileWriter fw = new FileWriter(path, true);
			fw.write(Detail.code + "\n");
			fw.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

	public void close() {
		if (out != null) {
			out.close();
		}
	}

	public void remove(String selectedCode) {
		File file = new File("/sdcard/RegCourse/mylist.txt");
		File temp = new File("/sdcard/RegCourse/mylist.tmp");
		temp.getParentFile().mkdirs();
		try {
			Scanner in = new Scanner(file);
			PrintWriter out = new PrintWriter(temp);
			
			while (in.hasNextLine()){
				String current = in.nextLine();
				if(current.trim().equals(selectedCode))
					continue;
				else
				{
					out.println(current);
					out.flush();
				}
			}
			
			if(!file.delete()){
				Log.d("DEBUG", "Orginal mylist.txt cannot be deleted");
				return;
			}
			if(!temp.renameTo(file)){
				Log.d("DEBUG", "mylist.tmp cannot be renamed");
			}
			
			in.close();
			out.close();
		} catch (FileNotFoundException e) {
		}
	}

}
