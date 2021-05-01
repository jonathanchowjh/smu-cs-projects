package com.example.demo.trades;

import java.util.*;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.json.JSONObject;
import org.json.JSONArray;

@Entity
public class Stock {
	@Id @GeneratedValue(strategy=GenerationType.SEQUENCE)
	private Integer id;
	private String symbol;
	private Integer bidVolume;
	private Integer askVolume;
	private Double bid;
	private Double ask;
	private Double lastPrice;

	public Stock() {}
	public Stock(String symbol) {
		this.symbol = symbol;
		this.bidVolume = 20000;
		this.askVolume = 20000;
		this.bid = this.random(1, 10);
		this.ask = this.random(10, 20);
		this.lastPrice = this.random(this.bid, this.ask);
	}
	public Stock(JSONObject json) {
		this.symbol = json.getString("symbol");
		this.lastPrice = json.optDouble("close", 0);
		this.bidVolume = json.getInt("volume");
		this.bid = json.optDouble("low", 0);
		this.askVolume = json.getInt("volume");
		this.ask = json.optDouble("high", 0);
	}

	private double random(double min, double max) {
		return Math.round((min + ((max - min) * Math.random())) * 100) / 100;
	}

	public Integer getId() { return this.id; }
	public String getSymbol() { return this.symbol; }
	@JsonProperty("bid_volume")
	public Integer getBidVolume() { return this.bidVolume; }
	@JsonProperty("ask_volume")
	public Integer getAskVolume() { return this.askVolume; }
	public Double getBid() { return this.bid; }
	public Double getAsk() { return this.ask; }
	@JsonProperty("last_price")
	public Double getLastPrice() { return this.lastPrice; }
	
	public void setId(int id) { this.id = id; }
	public void setSymbol(String symbol) { this.symbol = symbol; }
	@JsonProperty("bid_volume")
	public void setBidVolume(int bidVolume) { this.bidVolume = bidVolume; }
	@JsonProperty("ask_volume")
	public void setAskVolume(int askVolume) { this.askVolume = askVolume; }
	public void setBid(double bid) { this.bid = bid; }
	public void setAsk(double ask) { this.ask = ask; }
	@JsonProperty("last_price")
	public void setLastPrice(double lastPrice) { this.lastPrice = lastPrice; }

}