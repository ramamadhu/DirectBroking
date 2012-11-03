package directbroking.client;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class StockAdapter extends ArrayAdapter<Stock> {

	Context context;
	int layoutResourceId;
	List<Stock> data = null;

	public StockAdapter(Context context, int textViewResourceId, List<Stock> objects) {
		super(context, textViewResourceId, objects);
        this.layoutResourceId = textViewResourceId;
        this.context = context;
        this.data = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		StockHolder holder = null;

		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);

			holder = new StockHolder();
			holder.ticker = (TextView) row.findViewById(R.id.txtTitle);
			holder.purchasePrice = (TextView) row.findViewById(R.id.txtTitle2);
			holder.marketPrice = (TextView) row.findViewById(R.id.txtTitle3);

			row.setTag(holder);
		} 
		else {
			holder = (StockHolder) row.getTag();
		}

		Stock stock = data.get(position);
		System.out.printf("Stock code: %s\n", stock.getTicker());
		System.out.printf("Stock quantity: %s\n", stock.getCostPrice());
		holder.ticker.setText(stock.getTicker());
		holder.purchasePrice.setText(stock.getCostPrice());
		holder.marketPrice.setText(stock.getMarketPrice());

		return row;
	}

	static class StockHolder {
		TextView purchasePrice;
		TextView ticker;
		TextView marketPrice;
	}
}
