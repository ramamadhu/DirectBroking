package directbroking.client;

import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class MyPortfolio extends ListActivity {

	private ListView listView1;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview);
        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            if (extras.getString("htmlString") != null)
            {
                String htmlData = extras.getString("htmlString");
                processPortfolio(htmlData);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dbwebviewmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.signout:
            {
            	String url = "https://www.directbroking.co.nz/DirectTrade/dynamic/signoff.aspx";
            	HttpClient client = DBHttpClient.defaultInstance(getApplicationContext());
            	HttpPost httppost = new HttpPost(url);
            	try
            	{
            		client.execute(httppost);
            	}
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                Intent login = new Intent(getApplicationContext(), Login.class);
                login.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(login);
                
                break;
            }
            case R.id.MyOrders:
            {
            	try 
            	{
                    MyOrdersActionTask myOrdersTask = new MyOrdersActionTask(this);
                    myOrdersTask.execute();
            	}
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            	break;
            }
        }
        return true;
    }
    
    private StockDataSource stocksSource;
    static String stock;
    static String stockQuantity;
    static String costPrice;
    static String marketPrice;
    static String marketValue;
    static String unrealisedPLNZD;
    static String nzdMarketValue;
    /**
     * @param htmlData
     */
    private void processPortfolio(String htmlData) {
    	Document document = Jsoup.parse(htmlData);
        Elements tableRows = document.select("table[id=PortfolioPositionsTable] tr");
        tableRows.remove(0);
        
        stocksSource = new StockDataSource(this);
        stocksSource.open();
        System.out.println("DEBUG: " + tableRows.size());

        for(Element row : tableRows)
        {
        	Elements tds = row.getElementsByTag("td");
            System.out.println("DEBUG: " + tds.size());
        	if (tds.size() < 4)
        		continue;
        	
            stock = tds.first().text().replaceAll("\u00a0","").trim();
            System.out.printf("Parser Stock is %s\n", stock);
            if (stock.contentEquals("Code")) {
                System.out.println("skipping title row");
                continue;
            }

            if (stock.contentEquals("NZD Subtotal") || stock.contains("AUD Subtotal")) {
                System.out.println("NZD Subtotal");
                
                nzdMarketValue = tds.get(1).text().replaceAll("\u00a0","").trim();
                Element td = tds.get(3);
                System.out.println("td html: " + td.html());
                Document imgDoc = Jsoup.parse(td.html());
                Element img = imgDoc.select("img").first();
                String profit = img.attr("alt");
                System.out.println("profit: " + profit);


                if (profit.contentEquals("Up")){
                	marketPrice = tds.get(4).text().replaceAll("\u00a0","").trim();
                }
                else{
                	marketPrice = "-" + tds.get(4).text().replaceAll("\u00a0","").trim();
                }
                unrealisedPLNZD = costPrice = stockQuantity = "";
                
                Stock newStock = stocksSource.createStock(stock, stockQuantity, costPrice, marketPrice, nzdMarketValue, unrealisedPLNZD);
                System.out.printf("Insert test stock marketValue %s\n", newStock.getMarketPrice());

                continue;
            }
            
            if (stock.contentEquals("Total")) {
                nzdMarketValue = tds.get(1).text().replaceAll("\u00a0","").trim();
                marketPrice = tds.get(4).text().replaceAll("\u00a0","").trim();
                unrealisedPLNZD = costPrice = stockQuantity = "";
                
                Stock newStock = stocksSource.createStock(stock, stockQuantity, costPrice, marketPrice, nzdMarketValue, unrealisedPLNZD);
                System.out.printf("Insert test stock costPrice %s\n", newStock.getCostPrice());
                break;
            }
            
//            if (stock.contentEquals("Cash Positions") || stock.contentEquals("Account"))
//            {
//            	continue;
//            }
//            
//            if (stock.contentEquals("Direct Broking Call") || stock.contentEquals("Online Multi-Currency"))
//            {
//            	nzdMarketValue = tds.get(4).text().replaceAll("\u00a0","").trim();
//            	marketPrice = unrealisedPLNZD = costPrice = stockQuantity = "";
//            	Stock newStock = stocksSource.createStock(stock, stockQuantity, costPrice, marketPrice, nzdMarketValue, unrealisedPLNZD);
//            	System.out.printf("Call account %s\n", newStock.getMarketValue());
//            	continue;
//            }
//            
//            if (stock.contentEquals("Cash Subtotal"))
//            {
//            	nzdMarketValue = tds.get(1).text().replaceAll("\u00a0","").trim();
//            	marketPrice = unrealisedPLNZD = costPrice = stockQuantity = "";
//            	Stock newStock = stocksSource.createStock(stock, stockQuantity, costPrice, marketPrice, nzdMarketValue, unrealisedPLNZD);
//                System.out.printf("Cash Subtotal %s\n", newStock.getMarketValue());
//            	continue;
//            }
//
//            if (stock.contentEquals("Portfolio Total"))
//            {
//            	nzdMarketValue = tds.get(1).text().replaceAll("\u00a0","").trim();
//            	marketPrice = unrealisedPLNZD = costPrice = stockQuantity = "";
//            	Stock newStock = stocksSource.createStock(stock, stockQuantity, costPrice, marketPrice, nzdMarketValue, unrealisedPLNZD);
//                System.out.printf("Portfolio total %s\n", newStock.getMarketValue());
//            	break;
//            }
            
            stockQuantity = tds.get(2).text().replaceAll("\u00a0","").trim();
            costPrice = tds.get(3).text().replaceAll("\u00a0","").trim();
            marketPrice = tds.get(5).text().replaceAll("\u00a0","").trim();
            marketValue = tds.get(6).text().replaceAll("\u00a0","").trim();
            unrealisedPLNZD = tds.get(7).text().replaceAll("\u00a0","").trim();
            System.out.println("Parser: unrealisedPLNZD: " + unrealisedPLNZD);
            
            // sql insert
           Stock newStock = stocksSource.createStock(stock, stockQuantity, costPrice, marketPrice, marketValue, unrealisedPLNZD);
           System.out.printf("Insert test stock costPrice %s\n", newStock.getCostPrice());
        }

        List<Stock> values = stocksSource.getStockData();
        stocksSource.close();
        StockAdapter adapter = new StockAdapter(this, R.layout.myportfolio_item_row, values);
        listView1 = (ListView)findViewById(android.R.id.list);
         
        View header = (View)getLayoutInflater().inflate(R.layout.myportfolio_header_row, null);
        listView1.addHeaderView(header);
        listView1.setAdapter(adapter);

    }
    
    @Override
    protected void onResume() {
    stocksSource.open();
      super.onResume();
    }

    @Override
    protected void onPause() {
    stocksSource.close();
      super.onPause();
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (stocksSource != null) {
        	stocksSource.close();
        }
    }
    
    private void displayNotification(CharSequence text)
    {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, text, duration);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    private class MyOrdersActionTask extends AsyncTask<String,Void, String>
    {
        Context AppContext;

        private MyOrdersActionTask(Context context)
        {
            AppContext = context.getApplicationContext();
        }

        @Override
        protected String doInBackground(String...inputs)
        {
            String responseBody = "";
        	String url = "https://www.directbroking.co.nz/DirectTrade/secure/orders.aspx";
        	HttpClient client = DBHttpClient.defaultInstance(getApplicationContext());
        	HttpPost httppost = new HttpPost(url);
        	try
        	{
        		HttpResponse response = client.execute(httppost);
        		responseBody = EntityUtils.toString(response.getEntity());
        	}
            catch (Exception e)
            {
                e.printStackTrace();
            }

            return responseBody;
        }

		@Override
        protected void onPreExecute()
        {

        }

        @Override
        protected void onPostExecute(String result)
        {
            if(onLoginSuccess(result))
            {
            	finish();
                Intent myPortfolio = new Intent(AppContext, MyOrders.class);
                myPortfolio.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                myPortfolio.putExtra("htmlString", result);
                AppContext.startActivity(myPortfolio);
            }
            else
            {
            	finish();
                Intent login = new Intent(getApplicationContext(), Login.class);
                login.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(login);
            }
        }

        private boolean onLoginSuccess(String htmlString)
        {
        	boolean result= true;
        	Document document = Jsoup.parse(htmlString);
        	Element title = document.select("TITLE").first();

        	if( title != null && title.text().equals("Login | Direct Broking"))
        	{
        		Elements elements = document.select("span[id~=SystemMsg1_lblText]");
        		for (Element element : elements)
        		{
        			displayNotification(element.html());
        			break;
        		}

        		result = false;
        	}
        	else if (title == null)
        	{
        		result = false;
        	}

        	return result;
        }
    }

}
