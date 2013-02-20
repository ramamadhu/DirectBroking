package directbroking.client;

public class Stock 
{
	private String ticker;
	private String quantity;
	public String getTicker() {
		return ticker;
	}
	public void setTicker(String ticker) {
		this.ticker = ticker;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public String getCostPrice() {
		return costPrice;
	}
	public void setCostPrice(String costPrice) {
		this.costPrice = costPrice;
	}
	public String getMarketPrice() {
		return marketPrice;
	}
	public void setMarketPrice(String marketPrice) {
		this.marketPrice = marketPrice;
	}
	public String getMarketValue() {
		return marketValue;
	}
	public void setMarketValue(String marketValue) {
		this.marketValue = marketValue;
	}
	public String getUnrealisedPL() {
		return unrealisedPL;
	}
	public void setUnrealisedPL(String unrealisedPL) {
		this.unrealisedPL = unrealisedPL;
	}
	public String getValueNZD() {
		return valueNZD;
	}
	public void setValueNZD(String valueNZD) {
		this.valueNZD = valueNZD;
	}
	public String getUnrealisedPLNZD() {
		return unrealisedPLNZD;
	}
	public void setUnrealisedPLNZD(String unrealisedPLNZD) {
		this.unrealisedPLNZD = unrealisedPLNZD;
	}
	public String getPercentPortfolio() {
		return percentPortfolio;
	}
	public void setPercentPortfolio(String percentPortfolio) {
		this.percentPortfolio = percentPortfolio;
	}
	
	public String getLastOrder() {
		return lastOrder;
	}
	public void setLastOrder(String lastOrder) {
		this.lastOrder = lastOrder;
	}
	
	private String costPrice;
	private String marketPrice;

	private String marketValue;
	private String unrealisedPL;
	private String valueNZD;
	private String unrealisedPLNZD;
	private String percentPortfolio;
	private String lastOrder;
	
	public Stock()
	{
	}

	// Will be used by the ArrayAdapter in the ListView
	@Override
	public String toString() {
		String stockData = ticker + ", " + "Qty " + quantity;
		return stockData;
	}
}
