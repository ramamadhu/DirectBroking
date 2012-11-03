package directbroking.client;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	static final String dbName = "DirectBrokingDB";
	static final String portfolioTable = "Portfolio";

	static final String colTicker = "Code";
	public static final String COLUMN_ID = "_id";
	static final String colQuantity = "Quantity";
	static final String colCostPrice = "CostPrice";
	static final String colMarketPrice = "Price";

	static final String colMarketValue = "Value";
	static final String colUnrealisedPL = "UnrealisedPL";
	static final String colValueNZD = "ValueNZD";
	static final String colUnrealisedPLNZD = "UnrealisedPLNZD";
	static final String colPercentPortfolio = "PercentPortfolio";

	private static final String DATABASE_CREATE = "create table "
	        + portfolioTable
	        + " ("
	        + COLUMN_ID + " integer primary key autoincrement , "
//	        + colTicker + " TEXT PRIMARY KEY , "
	        + colTicker + " TEXT , "
	        + colQuantity + " INTEGER , "
	        + colCostPrice + " INTEGER , "
	        + colMarketPrice + " INTEGER"
	        + ");";
	public DatabaseHelper(Context context) {
		super(context, dbName, null, 37);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
	    db.execSQL(DATABASE_CREATE);
//		db.execSQL("CREATE TABLE " + portfolioTable + " ("+colTicker+ " TEXT PRIMARY KEY , "+colQuantity+ " INTEGER)");
//				+ colCostPrice + " INTEGER," + colMarketPrice + " INTEGER,"
//				+ colMarketValue + " INTEGER," + colUnrealisedPL + " INTEGER"
//				+ colValueNZD + " INTEGER" + colUnrealisedPLNZD + " INTEGER"
//				+ colPercentPortfolio + "INTEGER)");

		// TODO: This is test code. Inserts pre-defined departments
//		InsertCodes(db);
	}

	@Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

		db.execSQL("DROP TABLE IF EXISTS " + portfolioTable);
		onCreate(db);
	}

	// TODO: This is test code
	void InsertCodes(SQLiteDatabase db) {
		ContentValues cv = new ContentValues();
		cv.put(colTicker, "MHI");
		cv.put(colQuantity, 4230);
		db.insert(portfolioTable, colTicker, cv);
	}

	public String GetTickerQuantity(String ticker) {
		SQLiteDatabase db = this.getReadableDatabase();

		String[] params = new String[] { String.valueOf(ticker) };
		Cursor c = db.rawQuery("SELECT " + colQuantity + " FROM"
				+ portfolioTable + " WHERE " + colTicker + "=?", params);
		c.moveToFirst();
		int index = c.getColumnIndex(colQuantity);
		return c.getString(index);
	}
}