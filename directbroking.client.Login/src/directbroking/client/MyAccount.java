package directbroking.client;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MyAccount extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_account);
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
    
    private void processAccounts(String htmlData) {
    	Document document = Jsoup.parse(htmlData);
    	Element title = document.select("TITLE").first();
    	
        if( title != null && title.text().equals("My Orders | Direct Broking"))
        {
            Elements elements = document.select("span[id~=SystemMsg1_lblText]");
            for (Element element : elements)
            {
                displayNotification(element.html());
                break;
            }
        }
        else
        {
            String table = document.select("table[id=PortfolioPositionsTable]").outerHtml();
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
}
