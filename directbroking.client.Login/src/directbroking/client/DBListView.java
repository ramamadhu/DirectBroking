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
    static String nzdMarketValue;
    /**
     * @param htmlData
     */
    private void processPortfolio(String htmlData) {
    	Document document = Jsoup.parse(htmlData);
        Elements tableRows = document.select("table[id=PortfolioPositionsTable] tr:gt(0):lt(21)");

        tableRows.remove(0);
        stocksSource = new StockDataSource(this);
        stocksSource.open();
        System.out.println("DEBUG: " + tableRows.size());

        for(Element row : tableRows)
        {
        	Elements tds = row.getElementsByTag("td");
            Element td = tds.first();
            System.out.println("DEBUG: " + td.text());
            System.out.println("DEBUG: " + tds.size());
            System.out.println("DEBUG: " + tds.get(2).text());

            stock = tds.first().text().replaceAll("\u00a0","").trim();
            System.out.printf("Parser Stock is %s\n", stock);
            if (stock.contentEquals("Code")) {
                System.out.println("skipping title row");
                continue;
            }

            if (stock.contentEquals("NZD Subtotal") || stock.contains("AUD Subtotal")) {
                System.out.println("NZD Subtotal");
                
                nzdMarketValue = tds.get(1).text().replaceAll("\u00a0","").trim();
                marketValue = tds.get(4).text().replaceAll("\u00a0","").trim();
                costPrice = stockQuantity = "";
                
                Stock newStock = stocksSource.createStock(stock, stockQuantity, costPrice, marketPrice, nzdMarketValue);
                System.out.printf("Insert test stock costPrice %s\n", newStock.getCostPrice());

                continue;
            }
            
            if (stock.contentEquals("Total")) {
                nzdMarketValue = tds.get(1).text().replaceAll("\u00a0","").trim();
                marketValue = tds.get(4).text().replaceAll("\u00a0","").trim();
                costPrice = stockQuantity = "";
                
                Stock newStock = stocksSource.createStock(stock, stockQuantity, costPrice, marketPrice, nzdMarketValue);
                System.out.printf("Insert test stock costPrice %s\n", newStock.getCostPrice());

                continue;
            }
            
            stockQuantity = tds.get(2).text().replaceAll("\u00a0","").trim();
            costPrice = tds.get(3).text().replaceAll("\u00a0","").trim();
            marketPrice = tds.get(5).text().replaceAll("\u00a0","").trim();
            marketValue = tds.get(6).text().replaceAll("\u00a0","").trim();
            // sql insert
           Stock newStock = stocksSource.createStock(stock, stockQuantity, costPrice, marketPrice, marketValue);
           System.out.printf("Insert test stock costPrice %s\n", newStock.getCostPrice());
        }

        List<Stock> values = stocksSource.getStockData();
        stocksSource.close();
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
