package agndul.gramiejska;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBase {
	  private static final String TAG = "Database";
	  
	    private static final String DATABASE_NAME = "trasa_txt";

	     // Database Version
	    private static final int DATABASE_VERSION = 1;
	 
	     // Table Name
	    private static final String DATABASE_TABLE = "miejsce";
	 
	     // Table columns
	    public static final String KEY_NAME = "nazwa";
	    public static final String KEY_LAT = "lat";
	    public static final String KEY_LON = "lon";
	    public static final String KEY_DESCRIPTION_PLACE_SHORT= "opis_miejsca_krotki";
	    public static final String KEY_DESCRIPTION_PLACE= "opis_miejsca";
	    public static final String KEY_DESCRIPTION_PUZZLE= "opis_zagadka";
	    public static final String KEY_ROWID = "_id";
	 
	    // Database creation sql statement

	    private static final String CREATE_TABLE =
	        "create table " + DATABASE_TABLE + " (" + KEY_ROWID + " integer primary key autoincrement, "
	        + KEY_NAME +" text not null, " + KEY_LAT +" text not null, " + KEY_LON +" text not null, " + KEY_DESCRIPTION_PLACE_SHORT + " text not null, " + KEY_DESCRIPTION_PLACE + " text not null, " + KEY_DESCRIPTION_PUZZLE +" text not null);";
	 

	    private final Context mCtx; 
	    private DatabaseHelper mDbHelper;
	    private SQLiteDatabase mDb;
	    
	    private static boolean isNowCreated = false;
	 

	    private static class DatabaseHelper extends SQLiteOpenHelper {
	        DatabaseHelper(Context context) {
	            super(context, DATABASE_NAME, null, DATABASE_VERSION);
	        }

	        //onCreate method is called for the 1st time when database doesn't exists.
	        @Override
	        public void onCreate(SQLiteDatabase db) {
	        	isNowCreated = true;
	            Log.i(TAG, "Creating DataBase: " + CREATE_TABLE);
	            db.execSQL(CREATE_TABLE);
	        }

	        //onUpgrade method is called when database version changes.
	        @Override
	        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
	                    + newVersion);
	        }
	    }
	 

	    //Constructor - takes the context to allow the database to be opened/created
	    public DataBase(Context ctx) {
	        this.mCtx = ctx;
	    }

	    public DataBase open() throws SQLException {
	        mDbHelper = new DatabaseHelper(mCtx);
	        mDb = mDbHelper.getWritableDatabase();
	        return this;
	    }

	    public void close() {
	        mDbHelper.close();
	    }
	 
	    public long createPlace(String name, double lat, double lon, String desc_place_s,int desc_place, String desc_puzzle) {
	        ContentValues initialValues = new ContentValues();
	        initialValues.put(KEY_NAME, name);
	        initialValues.put(KEY_LAT, lat);
	        initialValues.put(KEY_LON, lon);
	        initialValues.put(KEY_DESCRIPTION_PLACE_SHORT, desc_place_s);
	        initialValues.put(KEY_DESCRIPTION_PLACE, desc_place);
	        initialValues.put(KEY_DESCRIPTION_PUZZLE, desc_puzzle);
	        
	        return mDb.insert(DATABASE_TABLE, null, initialValues);
	    }
	
	    public boolean deleteStudent(long rowId) {
	        return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
	    }
	    
	 

	    public Cursor fetchAllTXT() {
	        return mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_NAME,
	        		KEY_LAT,KEY_LON,KEY_DESCRIPTION_PLACE_SHORT,KEY_DESCRIPTION_PLACE,KEY_DESCRIPTION_PUZZLE}, null, null, null, null, null);
	    }
	    
	    
	    public Cursor fetchTXTByName(String inputText) throws SQLException {
	    	  Log.w(TAG, inputText);
	    	  Cursor mCursor = null;
	    	  if (inputText == null  ||  inputText.length () == 0)  {
	    	   mCursor = mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_NAME,
	    			   KEY_LAT,KEY_LON,KEY_DESCRIPTION_PLACE_SHORT,KEY_DESCRIPTION_PLACE,KEY_DESCRIPTION_PUZZLE}, null, null, null, null, null);
	    	 
	    	  }
	    	  else {
	    	   mCursor = mDb.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
	    	     KEY_NAME, KEY_LAT,KEY_LON,KEY_DESCRIPTION_PLACE_SHORT,KEY_DESCRIPTION_PLACE,KEY_DESCRIPTION_PUZZLE}, 
	    	     KEY_NAME + " like '%" + inputText + "%'", null,
	    	     null, null, null, null);
	    	  }
	    	  if (mCursor != null) {
	    	   mCursor.moveToFirst();
	    	  }
	    	  return mCursor;
	    	 
	    	 }
	    	 
	 
	    public Cursor fetchStudent(long id) throws SQLException {

	        Cursor mCursor =
	            mDb.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
	   	    	     KEY_NAME, KEY_LAT,KEY_LON,KEY_DESCRIPTION_PLACE_SHORT,KEY_DESCRIPTION_PLACE,KEY_DESCRIPTION_PUZZLE}, KEY_ROWID + "=" + id, null,
	                    null, null, null, null);
	        if (mCursor != null) {
	            mCursor.moveToFirst();
	        }
	        return mCursor;
	    }

	    public boolean updateStudent(long id, double newLat, double newLon) {
	        ContentValues args = new ContentValues();
	        args.put(KEY_LAT, newLat);
	        args.put(KEY_LON, newLon);
	        return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + id, null) > 0;
	    }
	    
	    
	    public boolean deleteAll(){
	    	int delete=0;
	    	delete = mDb.delete(DATABASE_TABLE, null, null);
	    	Log.w(TAG, Integer.toString(delete));
	        return delete > 0;
	    }
	    
	    
	    //pozwol� ci one dotrzec do
	    
	    public void insertSomeS() {
	    	if(isNowCreated){
				createPlace("Wawel", 50.05405, 19.935412 , " zabytkowej rezydencji królewskiej", 2, " Jedna cegła waży kilogram i pół cegły. Ile kg waży cegła? ");
				createPlace("Kopiec Kościuszki", 50.05494, 19.902323, " jednego z czterech kopców krakowskich", 1, "Ile krawędzi ma wstęga Möbiusa? ");
				createPlace("Kamienica Hipolitów", 50.06195, 19.94011, " słynnej kamienicy usytuowanej przy placu Mariackim", 24, "Ile jest typowo lat przestępnych w ciągu stulecia?");
				createPlace("Muzeum Narodowe", 50.061311, 19.937078, " miejsca dla pasjonatów sztuki", 2048,  "Ile wynosi dwa do potęgi dziesiątej? ");
				createPlace("brama floriańska", 50.064722, 19.941389 , "średniowiecznej bramy z basztą",1,"Ile krawędzi ma wstęga Möbiusa?");
				createPlace("zoo", 50.0547141, 19.854859 , "dzikich zwierząt", 22, "Ile razy podczas doby zachodzą na siebie wskazówki godzinowa i minutowa?");
			}
	    }    
	    	
	    
	/*   		2kg
	    		1
	    		 22
	    		 24
	    		 1024
	    		 1
	    		 */
	    
}