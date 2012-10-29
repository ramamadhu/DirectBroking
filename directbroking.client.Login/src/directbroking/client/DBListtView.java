package directbroking.client;

import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

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
           Stock newStock = stocksSource.createStock(stock, stockQuantity);
        }

        stocksSource = new StockDataSource(this);
        stocksSource.open();

        List<Stock> values = stocksSource.getStockData();
        stocksSource.close();
        ArrayAdapter<Stock> adapter = new ArrayAdapter<Stock>(this, android.R.layout.simple_list_item_1, values);
        setListAdapter(adapter);
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
