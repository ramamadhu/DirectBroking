package directbroking.client;

import java.util.List;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

public class MyAccount extends Activity {

	private ListView listView1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.listview);
        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            if (extras.getString("htmlString") != null)
            {
                String htmlData = extras.getString("htmlString");
                processAccounts(htmlData);
            }
        }
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dbwebviewmenu, menu);
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
            case R.id.MyPortfolio:
            {
            	try 
            	{
            		String url = "https://www.directbroking.co.nz/DirectTrade/secure/portfolios.aspx";
            		SwitchActivity myPortfolioTask = new SwitchActivity(this, url, MyPortfolio.class);
                    myPortfolioTask.execute();
            	}
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            	break;
            }
            case R.id.MyOrders:
            {
            	try 
            	{
                	String url = "https://www.directbroking.co.nz/DirectTrade/secure/orders.aspx";
                    SwitchActivity myOrdersTask = new SwitchActivity(this, url, MyOrders.class);
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

    @Override
    protected void onResume() {
      super.onResume();
    }

    @Override
    protected void onPause() {
      super.onPause();
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    
    private StockDataSource accountSource;
    
    private void processAccounts(String htmlData) {
    	Document document = Jsoup.parse(htmlData);
    	Elements tableRows = document.select("table[id=CAccountListTable] tr");
        tableRows.remove(0);
        tableRows.remove(1);
//        tableRows.remove(2);

    	accountSource = StockDataSource.Instance(this);
    	accountSource.open();

        for(Element row : tableRows)
        {
			Elements tds = row.getElementsByTag("td");
            System.out.println("DEBUG: " + tds.size());
        	if (tds.size() < 4)
        		continue;

        	String currency;
			String balance;
			String rate;
			String name;
            name = tds.first().text().replaceAll("\u00a0","").trim();
            currency = tds.get(1).text().replaceAll("\u00a0","").trim();
            balance = tds.get(2).text().replaceAll("\u00a0","").trim();
            rate = tds.get(3).text().replaceAll("\u00a0","").trim();
            System.out.printf("Name %s, currency %s, balance %s, rate %s\n", name, currency, balance, rate);
			accountSource.addAccount(name, currency, balance, rate);
//        	accountSource.close();
        }
        
        List<Account> values = accountSource.getAccountData();
        accountSource.close();
        AccountAdapter adapter = new AccountAdapter(this, R.layout.myaccount_item_row, values);
        listView1 = (ListView)findViewById(android.R.id.list);
         
        View header = (View)getLayoutInflater().inflate(R.layout.activity_my_account, null);
        listView1.addHeaderView(header);
        listView1.setAdapter(adapter);

    }
}
