package directbroking.client;

import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class DBListView extends ListActivity {

	private ListView listView1;
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

    private StockDataSource stocksSource;
    static String stock;
    static String stockQuantity;
    static String costPrice;
    static String marketPrice;
    static String marketValue;
    /**
     * @param htmlData
     */
    private void processPortfolio(String htmlData) {
    	Document document = Jsoup.parse(htmlData);
        Elements tableRows = document.select("table[id=PortfolioPositionsTable] tr:gt(0):lt(18)");

        stocksSource = new StockDataSource(this);
        stocksSource.open();
        
        String s[] = new String[tableRows.size()];
        for(Element row : tableRows)
        {
            s[0] = row.child(0).text();
            s[1] = row.child(1).text();
            s[2] = row.child(2).text();
            s[3] = row.child(3).text();
            s[4] = row.child(4).text();
            s[5] = row.child(5).text();
            s[6] = row.child(6).text();

            stock = s[0];
            System.out.printf("Parser Stock is %s\n", stock);
            if (stock.contentEquals("Code"))
            {
                System.out.println("skipping title row");
                continue;
            }

            if (s[2] !=""){
                stockQuantity = s[2];
                System.out.printf("Parser Quantity %s\n", stockQuantity);
            }
            
            if (s[3] !=""){
            	costPrice = s[3];
            	System.out.printf("Parser costPrice %s\n", costPrice);
            }

            if (s[5] !=""){
            	marketPrice = s[5];
            	System.out.printf("Parser marketPrice %s\n", marketPrice);
            }

            if (s[6] !=""){
            	marketValue = s[6];
            	System.out.printf("Parser marketValue %s\n", marketValue);
            }
            // sql insert
           Stock newStock = stocksSource.createStock(stock, stockQuantity, costPrice, marketPrice, marketValue);
           System.out.printf("Insert test stock costPrice %s\n", newStock.getCostPrice());
        }

        List<Stock> values = stocksSource.getStockData();
        stocksSource.close();
//        ArrayAdapter<Stock> adapter = new ArrayAdapter<Stock>(this, android.R.layout.simple_list_item_1, values);
//        setListAdapter(adapter);
        StockAdapter adapter = new StockAdapter(this, R.layout.listview_item_row, values);
        listView1 = (ListView)findViewById(android.R.id.list);
         
        View header = (View)getLayoutInflater().inflate(R.layout.listview_header_row, null);
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
