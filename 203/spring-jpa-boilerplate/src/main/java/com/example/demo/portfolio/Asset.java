package com.example.demo.portfolio;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class Asset {
    @Id
    private String code;
    private Integer quantity;
    private Double avgPrice;
    private Double currentPrice;
    private Double value;
    private Double gainLoss;
    private Integer customerId;

    public Asset() {}
    public Asset(String code, int quantity, double avgPrice, int customerId) {
        this.code = code;
        this.quantity = quantity;
        this.avgPrice = avgPrice;
        this.currentPrice = 0.0;
        this.value = 0.0;
        this.gainLoss = 0.0;
        this.customerId = customerId;
    }

    public String toString() { return code + " | " + quantity + " | " + avgPrice + " | " + customerId; }

    // public Integer getId() { return this.id; }
    public String getCode() { return this.code; }
    public Integer getQuantity() { return this.quantity; }
    @JsonProperty("avg_price")
    public Double getAvgPrice() { return this.avgPrice; }
    @JsonProperty("current_price")
    public Double getCurrentPrice() { return this.currentPrice; }
    public Double getValue() { return this.value; }
    @JsonProperty("gain_loss")
    public Double getGainLoss() { return this.gainLoss; }
    @JsonProperty("customer_id")
    public Integer customerId() { return this.customerId; }

    // public void setId(int id) { this.id = id; }
    public void setCode(String code) { this.code = code; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    @JsonProperty("avg_price")
    public void setAvgPrice(double avgPrice) { this.avgPrice = avgPrice; }
    @JsonProperty("current_price")
    public void setCurrentPrice(double currentPrice) { this.currentPrice = currentPrice; }
    public void setValue(double value) { this.value = value; }
    @JsonProperty("gain_loss")
    public void setGainLoss(double gainLoss) { this.gainLoss = gainLoss; }
    @JsonProperty("customer_id")
    public void setCustomerId(int customerId) { this.customerId = customerId; }
}