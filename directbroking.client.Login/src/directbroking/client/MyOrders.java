package directbroking.client;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.Menu;
import android.widget.Toast;

public class MyOrders extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);
        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            if (extras.getString("htmlString") != null)
            {
                String htmlData = extras.getString("htmlString");
                processOrders(htmlData);
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_my_orders, menu);
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
    
    private void processOrders(String htmlData) {
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
