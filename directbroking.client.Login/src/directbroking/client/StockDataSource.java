package directbroking.client;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class StockDataSource {
	  private static StockDataSource uniqueInstance;
	  private SQLiteDatabase database;
	  private final DatabaseHelper dbHelper;
	  private final String[] portfolioColumns = { DatabaseHelper.COLUMN_ID, DatabaseHelper.colTicker, DatabaseHelper.colQuantity,
			  								DatabaseHelper.colCostPrice, DatabaseHelper.colMarketPrice, 
			  								DatabaseHelper.colMarketValue, DatabaseHelper.colUnrealisedPLNZD };

	  private final String[] ordersColumns = { DatabaseHelper.COLUMN_ID, DatabaseHelper.colTicker, DatabaseHelper.colLastOrder,
				DatabaseHelper.colQuantity };

	  private final String[] accountColumns = { DatabaseHelper.COLUMN_ID, DatabaseHelper.colAccountName, DatabaseHelper.colCCY,
				DatabaseHelper.colBalance, DatabaseHelper.colInterestRate };

	  private StockDataSource(Context context) {
	    dbHelper = new DatabaseHelper(context);
	  }

	  public static synchronized StockDataSource Instance(Context context) {
		    if(uniqueInstance == null) {
		    	uniqueInstance = new StockDataSource(context); 
		    }
		    return uniqueInstance;
		  }
	  
	  public void open() throws SQLException {
	    database = dbHelper.getWritableDatabase();
	    dbHelper.deleteAllEntries(database);
	  }

	  public void close() {
	    dbHelper.close();
	  }
	  
	  public Stock createStock(String ticker, String stockQuantity, String costPrice, String marketPrice, String marketValue,
			  					String unrealisedPLNZD) {
	    ContentValues values = new ContentValues();
	    values.put(DatabaseHelper.colTicker, ticker);
	    values.put(DatabaseHelper.colQuantity, stockQuantity);
	    values.put(DatabaseHelper.colCostPrice, costPrice);
	    values.put(DatabaseHelper.colMarketPrice, marketPrice);
	    values.put(DatabaseHelper.colMarketValue, marketValue);
	    values.put(DatabaseHelper.colUnrealisedPLNZD, unrealisedPLNZD);

	    long insertId = database.insert(DatabaseHelper.portfolioTable, null, values);
	    System.out.printf("InsertId for ticker code %s quantity %s costPrice %s, is %f\n", ticker, stockQuantity, costPrice, (float)insertId);

	    Cursor cursor = database.query(DatabaseHelper.portfolioTable, portfolioColumns, DatabaseHelper.COLUMN_ID + "=" + insertId, null, null, null, null);
	    cursor.moveToFirst();
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

	  public void addAccount(String accountName, String accountCurrency, String accountBalance, String interestRate)
	  {
		  ContentValues values = new ContentValues();
		  values.put(DatabaseHelper.colAccountName, accountName);
		  values.put(DatabaseHelper.colCCY, accountCurrency);
		  values.put(DatabaseHelper.colBalance, accountBalance);
		  values.put(DatabaseHelper.colInterestRate, interestRate);
		  long insertId = database.insert(DatabaseHelper.accountTable, null, values);
	  }
	  
	  public void deleteStock(Stock stock) {
	    String ticker = stock.getTicker();
	    System.out.println("Stock deleted with id: " + ticker);
	    database.delete(DatabaseHelper.portfolioTable, DatabaseHelper.COLUMN_ID + " = " + ticker, null);
	  }

	  public List<Stock> getStockData() {
	    List<Stock> stockList = new ArrayList<Stock>();

	    Cursor cursor = database.query(DatabaseHelper.portfolioTable, portfolioColumns, null, null, null, null, null);
	    cursor.moveToFirst();
	    while (!cursor.isAfterLast()) {
	      Stock stock = cursorToStock(cursor);
	      stockList.add(stock);
	      System.out.printf("getStockData: Ticker %s Qty %s costPrice %s\n", stock.getTicker(), stock.getQuantity(), stock.getCostPrice());
	      cursor.moveToNext();
	    }
	    // Make sure to close the cursor
	    cursor.close();
	    return stockList;
	  }

	  public List<Account> getAccountData() {
		    List<Account> accountList = new ArrayList<Account>();

		    Cursor cursor = database.query(DatabaseHelper.accountTable, accountColumns, null, null, null, null, null);
		    cursor.moveToFirst();
		    while (!cursor.isAfterLast()) {
		      Account account = cursorToAccount(cursor);
		      accountList.add(account);
		      System.out.printf("getAccountData: AccName %s CCY %s Balance %s\n", account.getAccountName(), account.getCurrency(), account.getBalance());
		      cursor.moveToNext();
		    }
		    // Make sure to close the cursor
		    cursor.close();
		    return accountList;
		  }
	  
	  private Stock cursorToStock(Cursor cursor) {
	    Stock stock = new Stock();
	    stock.setTicker(cursor.getString(1));
	    stock.setQuantity(cursor.getString(2));
	    stock.setCostPrice(cursor.getString(3));
	    stock.setMarketPrice(cursor.getString(4));
	    stock.setMarketValue(cursor.getString(5));
	    stock.setUnrealisedPLNZD(cursor.getString(6));
	    return stock;
	  }
	  
	  private Account cursorToAccount(Cursor cursor) {
		  Account account = new Account();
		  account.setAccountName(cursor.getString(1));
		  account.setCurrency(cursor.getString(2));
		  account.setBalance(cursor.getString(3));
		  account.setInterestRate(cursor.getString(4));

		  return account;
	  }
	  
	  public Stock createOrder(String ticker, String stockQuantity, String lastOrder)
	  {
		    ContentValues values = new ContentValues();
		    values.put(DatabaseHelper.colTicker, ticker);
		    values.put(DatabaseHelper.colQuantity, stockQuantity);
		    values.put(DatabaseHelper.colLastOrder, lastOrder);

		    long insertId = database.insert(DatabaseHelper.ordersTable, null, values);
		    System.out.printf("InsertId for ticker code %s quantity %s lastOrder %s, is %f\n", ticker, stockQuantity, lastOrder, (float)insertId);

		    Cursor cursor = database.query(DatabaseHelper.ordersTable, ordersColumns, DatabaseHelper.COLUMN_ID + "=" + insertId, null, null, null, null);
		    cursor.moveToFirst();
		    Stock newStock = null;
		    if(cursor.moveToFirst()) {
		        newStock = cursorToStockOrder(cursor);
		    }
		    else {
		        System.out.println("cursor is null");
		    }
		    cursor.close();
		    return newStock;
		    
	  }
	  
	  private Stock cursorToStockOrder(Cursor cursor) {
		    Stock stock = new Stock();
		    stock.setTicker(cursor.getString(1));
		    stock.setLastOrder(cursor.getString(2));
		    stock.setQuantity(cursor.getString(3));
		    
		    return stock;
		  }	  
}
