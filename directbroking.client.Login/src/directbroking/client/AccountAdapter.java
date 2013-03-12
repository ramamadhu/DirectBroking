package directbroking.client;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class AccountAdapter extends ArrayAdapter<Account> {

	Context context;
	int layoutResourceId;
	List<Account> accountList = null;

	public AccountAdapter(Context context, int textViewResourceId, List<Account> objects) {
		super(context, textViewResourceId, objects);
        this.layoutResourceId = textViewResourceId;
        this.context = context;
        this.accountList = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		AccountHolder holder = null;

		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);

			holder = new AccountHolder();
			holder.accountName = (TextView) row.findViewById(R.id.txtTitle1);
			holder.currency = (TextView) row.findViewById(R.id.txtTitle2);
			holder.balance = (TextView) row.findViewById(R.id.txtTitle3);
			holder.interestRate = (TextView) row.findViewById(R.id.txtTitle4);

			row.setTag(holder);
		} 
		else {
			holder = (AccountHolder) row.getTag();
		}

		Account account = accountList.get(position);
//		System.out.printf("StockAdapter code: %s\n", account.getTicker());
//		System.out.printf("StockAdapter quantity: %s\n", account.getQuantity());
//		System.out.printf("StockAdapter purchasePrice: %s\n", account.getCostPrice());
//		System.out.printf("StockAdapter getMarketPrice: %s\n", account.getMarketPrice());
//		System.out.printf("StockAdapter getMarketValue: %s\n", account.getMarketValue());
		holder.accountName.setText(account.getAccountName());
		holder.currency.setText(account.getCurrency());
		holder.balance.setText(account.getBalance());
		holder.interestRate.setText(account.getInterestRate());
		
		holder.accountName.setGravity(Gravity.CENTER);
		holder.currency.setGravity(Gravity.CENTER);
		holder.balance.setGravity(Gravity.CENTER);
		holder.interestRate.setGravity(Gravity.CENTER);
		
//		System.out.printf("%s\n", account.getCostPrice());
//		if (account.getTicker().contains("NZD Subtotal") || 
//			account.getTicker().contains("AUD Subtotal") ||
//			account.getTicker().contains("Total"))
//		{
//			System.out.println(account.getTicker() + " gain: " + account.getMarketPrice());
//			if (account.getMarketPrice().startsWith("-"))
//			{
//				System.out.println(account.getTicker() + " Setting color of gain: " + account.getMarketPrice());
//				holder.profitLoss.setImageResource(R.drawable.down_dbl);
//			}
//			else
//			{
//				holder.profitLoss.setImageResource(R.drawable.up_dbl);
//			}
//		}
//
//		try {
//			float costPrice = Float.parseFloat(account.getCostPrice());
//			float marketPrice = Float.parseFloat(account.getMarketPrice());
//			System.out.printf("%f %f, %f\n", costPrice, marketPrice, (costPrice - marketPrice));
//			
//			if (costPrice > marketPrice) 
//			{
//				holder.profitLoss.setImageResource(R.drawable.down_dbl);
//			}
//			else
//			{
//				holder.profitLoss.setImageResource(R.drawable.up_dbl);
//			}
//		}
//		catch(NumberFormatException nfe) {
//			   System.out.println("Could not parse " + nfe);
//		}

		return row;
	}

	static class AccountHolder {
		TextView accountName;
		TextView currency;
		TextView balance;
		TextView interestRate;
	}
}
