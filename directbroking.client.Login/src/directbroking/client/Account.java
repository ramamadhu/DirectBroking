package directbroking.client;

public class Account {
	
	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(String interestRate) {
		this.interestRate = interestRate;
	}

	private String accountName;
	private String currency;
	private String balance;
	private String interestRate;
		
	public Account()
	{
	}

	// Will be used by the ArrayAdapter in the ListView
	@Override
	public String toString() {
		String accountData = accountName + balance;// + interestRate;
		return accountData;
	}	
}
