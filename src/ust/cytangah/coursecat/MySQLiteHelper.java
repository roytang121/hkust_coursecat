package ust.cytangah.coursecat;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

  public static final String TABLE_COURSES = "courses";
  public static final String TABLE_SECTIONS = "sections";
  public static final String COLUMN_ID = "_id";
  public static final String COLUMN_CODE = "code";
  public static final String COLUMN_TITLE = "title";
  public static final String COLUMN_MARKER = "marker";
  public static final String COLUMN_SECTION = "section";
  public static final String COLUMN_TIME = "time";
  public static final String COLUMN_ROOM = "room";
  public static final String COLUMN_INSTRUCTOR = "instructor";
  //catalog entry
  public static final String COLUMN_ATTRIBUTES = "attr";
  public static final String COLUMN_VECTOR = "vector";
  public static final String COLUMN_PRE_REQUISITE = "pre_reg";
  public static final String COLUMN_CO_REQUISITE = "co_reg";
  public static final String COLUMN_EXCLUSION = "exclusion";
  public static final String COLUMN_PREVIOUS_CODE = "prev_code";
  public static final String COLUMN_DESCRIPTION = "description";
  
  public static final String DATABASE_NAME = "courses.db";
  public static final String DB_PATH = "/data/data/ust.cytangah.coursecat/databases/";
  private static final int DATABASE_VERSION = 12; //increment upon each version update
  private Context myContext;
  private SQLiteDatabase myDataBase;

  // Database creation sql statement
  private static final String DATABASE_CREATE = "create table "
      + TABLE_COURSES + "(" + COLUMN_ID
      + " integer primary key autoincrement, " + COLUMN_MARKER
      + " text not null, " + COLUMN_CODE
      + " text not null, " + COLUMN_TITLE
      + " text not null);";


  public MySQLiteHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
    myContext = context;
  }

  @Override
  public void onCreate(SQLiteDatabase database) {

    //database.execSQL(DATABASE_CREATE);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    onCreate(db);
  }
  
  private boolean checkDataBase(){
	  
  	SQLiteDatabase checkDB = null;

  	try{
  		String myPath = DB_PATH + DATABASE_NAME;
  		checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

  	}catch(SQLiteException e){

  		//database does't exist yet.

  	}

  	if(checkDB != null){

  		checkDB.close();

  	}

  	return checkDB != null ? true : false;
  }
  
  private void copyDataBase() throws IOException{
	  
  	//Open your local db as the input stream
  	InputStream myInput = myContext.getAssets().open(DATABASE_NAME);

  	// Path to the just created empty db
  	String outFileName = DB_PATH + DATABASE_NAME;

  	//Open the empty db as the output stream
  	OutputStream myOutput = new FileOutputStream(outFileName, false);

  	//transfer bytes from the inputfile to the outputfile
  	byte[] buffer = new byte[1024];
  	int length;
  	while ((length = myInput.read(buffer))>0){
  		myOutput.write(buffer, 0, length);
  	}

  	//Close the streams
  	myOutput.flush();
  	myOutput.close();
  	myInput.close();

  }
  
  public void openDataBase() throws SQLException{
	  
  	//Open the database
      String myPath = DB_PATH + DATABASE_NAME;
  	myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

  }
  
  public void createDataBase() throws IOException{
	  
  	boolean dbExist = checkDataBase();
/*
  	if(dbExist){
  		//do nothing - database already exist
  	}else{
*/
  		//By calling this method and empty database will be created into the default system path
             //of your application so we are gonna be able to overwrite that database with our database.
      	this.getReadableDatabase();

      	try {

  			copyDataBase();

  		} catch (IOException e) {

      		throw new Error("Error copying database");

      	}
  	

  }
  
    @Override
	public synchronized void close() {

  	    if(myDataBase != null)
  		    myDataBase.close();

  	    super.close();

	}

} 