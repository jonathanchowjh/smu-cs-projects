package com.example.demo.trades;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotNull;

@Entity
public class Trade {
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	@NotNull(message = "Action should not be null")
	private String action;
	@NotNull(message = "Symbol should not be null")
	private String symbol;
	@NotNull(message = "Quantity should not be null")
	private Integer quantity;
	@NotNull(message = "Bid should not be null")
	private Double bid;
	@NotNull(message = "Ask should not be null")
	private Double ask;
	private Double avgPrice;
	private Integer filledQuantity;
	private Long date;	// submitted time in Unix timestamp, expired after 5pm of the day
	@NotNull(message = "Account ID should not be null")
	private Integer accountId;
	@NotNull(message = "Customer ID should not be null")
	private Integer customerId;
	private String status;

	public Trade() {}

	public String toString() {
		return "Trade | " + id + " | " + action + symbol + quantity + " | bid=" + bid + " | ask=" + ask +
		" | avgPrice=" + avgPrice + " | filledQuantity=" + filledQuantity + " | accountId=" + accountId +
		" | customerId=" + customerId + " | status=" + status + " | date=" + date;
	}
	
	public Integer getId() { return this.id; }
	public String getAction() { return this.action; }
	public String getSymbol() { return this.symbol; }
	public Integer getQuantity() { return this.quantity; }
	public Double getBid() { return this.bid; }
	public Double getAsk() { return this.ask; }
	@JsonProperty("avg_price")
	public Double getAvgPrice() { return this.avgPrice; }
	@JsonProperty("filled_quantity")
	public Integer getFilledQuantity() { return this.filledQuantity; }
	public Long getDate() { return this.date; }
	@JsonProperty("account_id")
	public Integer getAccountId() { return this.accountId; }
	@JsonProperty("customer_id")
	public Integer getCustomerId() { return this.customerId; }
	public String getStatus() { return this.status; }

	public void setId(int id) { this.id = id; }
	public void setAction(String action) { this.action = action; }
	public void setSymbol(String symbol) { this.symbol = symbol; }
	public void setQuantity(int quantity) { this.quantity = quantity; }
	public void setBid(double bid) { this.bid = bid; }
	public void setAsk(double ask) { this.ask = ask; }
	@JsonProperty("avg_price")
	public void setAvgPrice(double avgPrice) { this.avgPrice = avgPrice; }
	@JsonProperty("filled_quantity")
	public void setFilledQuantity(int filledQuantity) { this.filledQuantity = filledQuantity; }
	public void setDate(long date) { this.date = date; }
	@JsonProperty("account_id")
	public void setAccountId(int accountId) { this.accountId = accountId; }
	@JsonProperty("customer_id")
	public void setCustomerId(int customerId) { this.customerId = customerId; }
	public void setStatus(String status) { this.status = status; }
}