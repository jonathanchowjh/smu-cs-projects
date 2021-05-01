package com.example.demo.portfolio;

import java.util.*;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Portfolio {
    private Integer customerId;
    private List<Asset> assets;
    private Double unrealizedGainLoss;
    private Double totalGainLoss;


    public Portfolio(int customerId) {
        this.customerId = customerId;
        this.assets = new ArrayList<>();
        this.unrealizedGainLoss = 0.0;
        this.totalGainLoss = 0.0;
    }

    @JsonProperty("customer_id")
    public Integer getCustomerId() { return this.customerId; }
    public List<Asset> getAssets() { return this.assets; }
    @JsonProperty("unrealized_gain_loss")
    public Double getUnrealizedGainLoss() { return this.unrealizedGainLoss; }
    @JsonProperty("total_gain_loss")
    public Double getTotalGainLoss() { return this.totalGainLoss; }

    @JsonProperty("customer_id")
    public void setCustomerId(int customerId) { this.customerId = customerId; }
    public void setAssets(List<Asset> assets) { this.assets = assets; }
    @JsonProperty("unrealized_gain_loss")
    public void setUnrealizedGainLoss(double unrealizedGainLoss) { this.unrealizedGainLoss = unrealizedGainLoss; }
    @JsonProperty("total_gain_loss")
    public void setTotalGainLoss(double totalGainLoss) { this.totalGainLoss = totalGainLoss; }
}