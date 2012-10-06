package directbroking.client;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager.LayoutParams;
import android.view.Menu;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class TableView extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tableview);


        int PROVINCE_Alberta = 0;
        int PROVINCE_BC = 1;
        int PROVINCE_Manitoba = 2;
        int PROVINCE_NewBrunswick = 3;
        int PROVINCE_Newfoundland = 4;
        int PROVINCE_Northwest = 5;
        int PROVINCE_NovaScotia = 6;
        int PROVINCE_Nunavut = 7;
        int PROVINCE_Ontario = 8;
        int PROVINCE_PEI = 9;
        int PROVINCE_Quebec = 10;
        int PROVINCE_Saskatchewan = 11;
        int PROVINCE_Yukon = 12;
        int numProvinces = 13;
        String[] provinces = new String[numProvinces];
        provinces[PROVINCE_Alberta] = "Alberta";
        provinces[PROVINCE_BC] = "British Columbia";
        provinces[PROVINCE_Manitoba] = "Manitoba";
        provinces[PROVINCE_NewBrunswick] = "New Brunswick";
        provinces[PROVINCE_Newfoundland] = "Newfoundland and Labrador";
        provinces[PROVINCE_Northwest] = "Northwest Territories";
        provinces[PROVINCE_NovaScotia] = "Nova Scotia";
        provinces[PROVINCE_Nunavut] = "Nunavut";
        provinces[PROVINCE_Ontario] = "Ontario";
        provinces[PROVINCE_PEI] = "Prince Edward Island";
        provinces[PROVINCE_Quebec] = "Quebec";
        provinces[PROVINCE_Saskatchewan] = "Saskatchewan";
        provinces[PROVINCE_Yukon] =  "Yukon";

//        // Get the TableLayout
//        TableLayout tl = (TableLayout) findViewById(R.id.TableLayout1);
//
//        // Go through each item in the array
//        for (int current = 0; current < numProvinces; current++)
//        {
//            // Create a TableRow and give it an ID
//            TableRow tr = new TableRow(this);
//            tr.setId(100+current);
//            tr.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
//
//            // Create a TextView to house the name of the province
//            TextView labelTV = new TextView(this);
//            labelTV.setId(200+current);
//            labelTV.setText(provinces[current]);
//            labelTV.setTextColor(Color.BLACK);
//            labelTV.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
//            tr.addView(labelTV);
//
//            // Create a TextView to house the value of the after-tax income
//            TextView valueTV = new TextView(this);
//            valueTV.setId(current);
//            valueTV.setText("$0");
//            valueTV.setTextColor(Color.BLACK);
//            valueTV.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
//            tr.addView(valueTV);
//
//            // Add the TableRow to the TableLayout
//            tl.addView(tr, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
//        }
        TableLayout tl = (TableLayout) findViewById(R.id.TableLayout1);

        // Create a TableRow and give it an ID
        TableRow tr = new TableRow(this);
        tr.setId(100);
        tr.setLayoutParams(new LayoutParams());

        // Create a TextView to house the name of the province
        TextView labelTV = new TextView(this);
        labelTV.setId(200);
        labelTV.setText("MHI");
        labelTV.setTextColor(Color.BLACK);
        tr.addView(labelTV);

        // Create a TextView to house the value of the after-tax income
        TextView valueTV = new TextView(this);
        valueTV.setId(1);
        valueTV.setText("$0");
        valueTV.setTextColor(Color.BLACK);
        tr.addView(valueTV);

        // Add the TableRow to the TableLayout
        tl.addView(tr, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tableview, menu);
        return true;
    }


}
