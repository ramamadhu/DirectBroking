package directbroking.client;

import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class DBListtView extends ListActivity {

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
//                TextView textView = (TextView) findViewById(R.id.textView1);
//                String htmlStringRequest = extras.getString("htmlString");
//                CharSequence htmlStringRequest = extras.getCharSequence("htmlString");
//                textView.setText(htmlStringRequest);
                
                String htmlData = extras.getString("htmlString");
                processPortfolio(htmlData);
            }
        }
    }

    private StockDataSource stocksSource;
    static String stock;
    static String stockQuantity;
    /**
     * @param htmlData
     */
    private void processPortfolio(String htmlData) {
    	Document document = Jsoup.parse(htmlData);
//        String table = document.select("tr[class=dgitTR]").outerHtml();
        Elements tableRows = document.select("table[id=PortfolioPositionsTable] tr:gt(0):lt(18)");

        String s[] = new String[tableRows.size()];
        for(Element row : tableRows)
        {
            s[0] = row.child(0).text();
            s[1] = row.child(1).text();
            s[2] = row.child(2).text();

            stock = s[0];
            System.out.printf("Stock is %s\n", stock);
            if (stock.contentEquals("Code"))
            {
                System.out.println("skipping title row");
                continue;
            }

            if (s[2] !=""){
                stockQuantity = s[2];
            }

            // sql insert
//           stocksSource = new StockDataSource(this);
//           stocksSource.open();
           System.out.printf("create stock %s, quantity %s\n", stock, stockQuantity);
           Stock newStock = stocksSource.createStock(stock, stockQuantity);
           System.out.printf("RM stock name: %s\n", newStock.getTicker());
           System.out.printf("RM stock quantity: %s\n", newStock.getQuantity());
        }

        stocksSource = new StockDataSource(this);
        stocksSource.open();

        List<Stock> values = stocksSource.getStockData();
        ArrayAdapter<Stock> adapter = new ArrayAdapter<Stock>(this, android.R.layout.simple_list_item_1, values);
        setListAdapter(adapter);
//        ListView myList=(ListView)findViewById(android.R.id.list);
//        myList.setAdapter(adapter);
        stocksSource.close();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu)
//    {
//        getMenuInflater().inflate(R.menu.dbtextview, menu);
//        return true;
//    }


//    @Override
//    public boolean onOptionsItemSelected(MenuItem item)
//    {
//        switch (item.getItemId())
//        {
//            case android.R.id.home:
//                NavUtils.navigateUpFromSameTask(this);
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

}
