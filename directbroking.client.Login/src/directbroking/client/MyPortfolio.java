package directbroking.client;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.ListActivity;
import android.app.ProgressDialog;
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
    private ProgressDialog dialog;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview);
//        getActionBar().setDisplayHomeAsUpEnabled(true);
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
            	HttpClient client = DBHttpClient.defaultInstance();
            	HttpPost httppost = new HttpPost(url);
            	try
            	{
            		client.execute(httppost);
            	}
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            	finish();
                break;
            }
            case R.id.MyOrders:
            {
            	try 
            	{
                    dialog = new ProgressDialog(this);
                    dialog.setTitle("Please wait");
                    dialog.setMessage("Verifying username and password...");
                    dialog.show();

                    LoginActionTask loginActionTask = new LoginActionTask(this);
                    loginActionTask.execute(new String[] {"2139723", "Srinivas60"});

//            	HttpClient client = DBHttpClient.sslSessionClientInstance(getApplicationContext());
//
//                  HttpPost httppost = new HttpPost("https://www.directbroking.co.nz/DirectTrade/dynamic/signon.aspx?Login=Go+%3E%3E");
//                  List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
//                  nameValuePairs.add(new BasicNameValuePair("username", "2139723"));
//                  nameValuePairs.add(new BasicNameValuePair("password", "Srinivas60"));
//                  nameValuePairs.add(new BasicNameValuePair("startin", "../secure/orders.aspx"));
//                  httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//                  HttpResponse response = client.execute(httppost);
//                  String responseBody = EntityUtils.toString(response.getEntity());
//
//            	Context AppContext = getApplicationContext();
//            	Intent myOrders = new Intent(AppContext, MyOrders.class);
//            	myOrders.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////            	myOrders.putExtra("htmlString", responseBody);
//            	AppContext.startActivity(myOrders);

//            	String url = "https://www.directbroking.co.nz/DirectTrade/secure/orders.aspx";
//            	HttpClient client = DBHttpClient.sslSessionClientInstance(getApplicationContext());
//            	HttpPost httppost = new HttpPost(url);
//            	try
//            	{
//                    HttpResponse response = client.execute(httppost);
//                    String responseBody = EntityUtils.toString(response.getEntity());
//
//            		Context AppContext = getApplicationContext();
//                    Intent myOrders = new Intent(AppContext, MyOrders.class);
//                    myOrders.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    myOrders.putExtra("htmlString", responseBody);
//            		AppContext.startActivity(myOrders);

//                    HttpPost httppost = new HttpPost("https://www.directbroking.co.nz/DirectTrade/dynamic/signon.aspx?Login=Go+%3E%3E");
//                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
//                    nameValuePairs.add(new BasicNameValuePair("username", "2139723"));
//                    nameValuePairs.add(new BasicNameValuePair("password", "Srinivas60"));
//                    nameValuePairs.add(new BasicNameValuePair("startin", "../secure/orders.aspx"));
//                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//                    HttpResponse response = client.execute(httppost);
//                    
//                    String responseBody = EntityUtils.toString(response.getEntity());
//                    
//            		Context AppContext = getApplicationContext();
//                    Intent myOrders = new Intent(AppContext, DirectBrokingWebView.class);
//                    myOrders.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    myOrders.putExtra("htmlString", responseBody);
//            		AppContext.startActivity(myOrders);
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
//        Elements tableRows = document.select("table[id=PortfolioPositionsTable] tr:gt(0):lt(21)");
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

//                continue;
                break;
            }
            
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
//        }

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

    private class LoginActionTask extends AsyncTask<String,Void, String>
    {
        Context AppContext;

        private LoginActionTask(Context context)
        {
            AppContext = context.getApplicationContext();
        }

        @Override
        protected String doInBackground(String...inputs)
        {
            String accountNumString = inputs[0];
            String passwordString = inputs[1];
            String responseBody = "";

            HttpClient client = DBHttpClient.sslSessionClientInstance(AppContext);
            
            try
            {
                HttpPost httppost = new HttpPost("https://www.directbroking.co.nz/DirectTrade/dynamic/signon.aspx?Login=Go+%3E%3E");
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
                nameValuePairs.add(new BasicNameValuePair("username", accountNumString));
                nameValuePairs.add(new BasicNameValuePair("password", passwordString));
                nameValuePairs.add(new BasicNameValuePair("startin", "../secure/orders.aspx"));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
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
            dialog.dismiss();
            if(onLoginSuccess(result))
            {
                Intent myPortfolio = new Intent(AppContext, MyOrders.class);
                myPortfolio.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                myPortfolio.putExtra("htmlString", result);
                AppContext.startActivity(myPortfolio);
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
