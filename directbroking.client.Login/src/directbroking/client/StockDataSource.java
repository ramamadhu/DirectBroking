package directbroking.client;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class StockDataSource {
	  private SQLiteDatabase database;
	  private final DatabaseHelper dbHelper;
	  private final String[] allColumns = { DatabaseHelper.COLUMN_ID, DatabaseHelper.colTicker, DatabaseHelper.colQuantity };

	  public StockDataSource(Context context) {
	    dbHelper = new DatabaseHelper(context);
	  }

	  public void open() throws SQLException {
	    database = dbHelper.getWritableDatabase();
	  }

	  public void close() {
	    dbHelper.close();
	  }

	  public Stock createStock(String ticker, String stockQuantity) {
//	  public void createStock(String ticker, String stockQuantity) {
	    ContentValues values = new ContentValues();
	    values.put(DatabaseHelper.colTicker, ticker);
	    values.put(DatabaseHelper.colQuantity, stockQuantity);
//	    long insertId = database.insert(DatabaseHelper.portfolioTable, DatabaseHelper.colTicker, values);
	    long insertId = database.insert(DatabaseHelper.portfolioTable, null, values);
	    System.out.printf("InsertId for ticker code %s quantity %s, is %f\n", ticker, stockQuantity, (float)insertId);
//	    Cursor cursor = database.query(DatabaseHelper.portfolioTable, allColumns, DatabaseHelper.colTicker + "=" + insertId, null, null, null, null);
	    Cursor cursor = database.query(DatabaseHelper.portfolioTable, allColumns, DatabaseHelper.COLUMN_ID + "=" + insertId, null, null, null, null);
	    cursor.moveToFirst();
//	    cursor.moveToNext();
	    Stock newStock = null;
	    if(cursor.moveToFirst()) {
	        newStock = cursorToStock(cursor);
	    }
	    else {
	        System.out.println("cursor is null");
	    }
	    cursor.close();
	    return newStock;
	  }

	  public void deleteStock(Stock stock) {
	    String ticker = stock.getTicker();
	    System.out.println("Stock deleted with id: " + ticker);
//	    database.delete(DatabaseHelper.portfolioTable, DatabaseHelper.colTicker + " = " + ticker, null);
	    database.delete(DatabaseHelper.portfolioTable, DatabaseHelper.COLUMN_ID + " = " + ticker, null);
	  }

	  public List<Stock> getStockData() {
	    List<Stock> stockList = new ArrayList<Stock>();

	    Cursor cursor = database.query(DatabaseHelper.portfolioTable, allColumns, null, null, null, null, null);

//	    cursor.moveToFirst();
	    cursor.moveToNext();
	    while (!cursor.isAfterLast()) {
	      Stock stock = cursorToStock(cursor);
	      stockList.add(stock);
	      System.out.printf("getStockData: Ticker %s Qty %s\n", stock.getTicker(), stock.getQuantity());
	      cursor.moveToNext();
	    }
	    // Make sure to close the cursor
	    cursor.close();
	    return stockList;
	  }

	  private Stock cursorToStock(Cursor cursor) {
	    Stock stock = new Stock();
	    stock.setTicker(cursor.getString(1));
	    stock.setQuantity(cursor.getString(2));
	    return stock;
	  }
}
