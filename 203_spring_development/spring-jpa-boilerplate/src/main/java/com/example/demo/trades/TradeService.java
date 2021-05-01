package com.example.demo.trades;

import java.util.*;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;

import com.example.demo.config.ForbiddenException;
import com.example.demo.config.BadRequestException;
import com.example.demo.config.NotFoundException;
import com.example.demo.config.ConflictException;
import java.util.regex.Pattern;

import com.example.demo.cms.*;
import com.example.demo.accounts.*;
import com.example.demo.trades.*;
import com.example.demo.portfolio.*;

@Service
public class TradeService {
  private TradeRepository tradeRepo;
  private StockService stockService;
  private AssetRepository assetRepo;
  private AccountsRepository accRepo;

  @Autowired
  public TradeService(TradeRepository tradeRepo, StockService stockService,
  AssetRepository assetRepo, AccountsRepository accRepo) {
    this.tradeRepo = tradeRepo;
    this.stockService = stockService;
    this.assetRepo = assetRepo;
    this.accRepo = accRepo;
  }

  public Trade getTrade(int id) throws NotFoundException {
    return tradeRepo.findById(id).orElseThrow(() -> new NotFoundException("Trade of this ID not found"));
  }

  public Trade saveTrade(Trade trade) {
    return tradeRepo.save(trade);
  }

  public Trade createTrade(Trade trade) throws BadRequestException, NotFoundException {
    Stock stock = stockService.getStock(trade.getSymbol());
    Account account = getAccount(trade.getAccountId());
    Asset asset = getAsset(trade.getSymbol(), trade.getCustomerId());
    if (trade.getStatus().equals("cancelled")) {
      
    }
    if (trade.getAsk() == 0 && trade.getBid() == 0) {
      // market order
      if (trade.getAction().equals("buy")) return buyTrade(trade, stock, account, asset);
      if (trade.getAction().equals("sell")) return sellTrade(trade, stock, account, asset);
    } else {
      // limit order
      return this.createOrder(trade, stock, account, asset);
    }
    // if (trade.getAction() == "sell") return sellTrade(trade);
    throw new BadRequestException("Invalid Action");
  }

  private Trade createOrder(Trade trade, Stock stock, Account account, Asset asset)
  throws BadRequestException, NotFoundException {
    // if bid >= stock ask
    // fill quantity => if extra => fill sell orders
    trade.setFilledQuantity(0);
    trade.setAvgPrice(0.0);
    if (asset == null) asset = new Asset(trade.getSymbol(), 0, 0, trade.getCustomerId());
    if (trade.getAction().equals("buy")) {
      if (trade.getBid() > stock.getAsk()) {
        int leftoverQuantity = trade.getQuantity() - trade.getFilledQuantity();
        int quantity = (int) Math.min(Math.min(account.getAvailable_balance() / stock.getAsk(),
          stock.getAskVolume()), leftoverQuantity);
        trade.setAvgPrice(calcAvgPrice(trade, quantity, stock.getAsk()));
        trade.setFilledQuantity(trade.getFilledQuantity() + quantity);
        asset.setAvgPrice(calcAvgPrice(asset, quantity, stock.getAsk()));
        asset.setQuantity(quantity);
        account.updateBalance(-(quantity * stock.getAsk()));
        this.editStock(stock, quantity, stock.getAsk(), "buy");
      }
    } else {
      if (trade.getAsk() < stock.getBid()) {
        int leftoverQuantity = trade.getQuantity() - trade.getFilledQuantity();
        int quantity = (int) Math.min(Math.min(account.getAvailable_balance() / stock.getBid(), stock.getBidVolume()), leftoverQuantity);
        trade.setAvgPrice(calcAvgPrice(trade, quantity, stock.getBid()));
        trade.setFilledQuantity(trade.getFilledQuantity() + quantity);
        asset.setAvgPrice(calcAvgPrice(asset, quantity, stock.getBid()));
        asset.setQuantity(quantity);
        account.updateBalance(quantity * stock.getBid());
        this.editStock(stock, quantity, stock.getBid(), "sell");
      }
    }
    return this.fillTrades(trade, asset, account);
  }

  private Trade buyTrade(Trade trade, Stock stock, Account account, Asset asset)
  throws BadRequestException, NotFoundException {
    if (stock.getAskVolume() < trade.getQuantity()) throw new BadRequestException("too much market order trade");
    boolean sufficientFunds = account.getAvailable_balance() >= (trade.getQuantity() * stock.getAsk());
    boolean sufficientQuantity = trade.getQuantity() <= stock.getAskVolume();

    // System.out.println("" + account.getAvailable_balance() + " | " + trade.getQuantity() * stock.getAsk());
    if (sufficientFunds) {
      if (sufficientQuantity) {
        // sufficient funds + sufficient quantity
        int quantity = trade.getQuantity() + (asset == null ? 0 : asset.getQuantity());
        this.createAsset(trade.getSymbol(), quantity, stock.getAsk(), trade.getCustomerId());
        this.editStock(stock, trade.getQuantity(), stock.getAsk(), "buy");
        this.editBalance(account, -(trade.getQuantity() * stock.getAsk()), true);
        trade = this.createTrade(trade, "filled", trade.getQuantity(), stock.getAsk());
      } else {
        // sufficient funds + insufficient quantity
        int quantity = stock.getAskVolume() + (asset == null ? 0 : asset.getQuantity());
        this.createAsset(trade.getSymbol(), quantity, stock.getAsk(), trade.getCustomerId());
        this.editStock(stock, stock.getAskVolume(), stock.getAsk(), "buy");
        this.editBalance(account, -(stock.getAskVolume() * stock.getAsk()), true);
        trade = this.createTrade(trade, "partial-filled", stock.getAskVolume(), stock.getAsk());
      }
    } else {
        if (sufficientQuantity) {
          // insufficient funds + sufficient quantity
          int qtyToFill = (int)(account.getAvailable_balance() / stock.getAsk());
          int quantity = qtyToFill + (asset == null ? 0 : asset.getQuantity());
          this.createAsset(trade.getSymbol(), quantity, stock.getAsk(), trade.getCustomerId());
          this.editStock(stock, qtyToFill, stock.getAsk(), "buy");
          this.editBalance(account, -(qtyToFill * stock.getAsk()), true);
          trade = this.createTrade(trade, "partial-filled", qtyToFill, stock.getAsk());
        } else {
          // insufficient funds + insufficient quantity = cannot be filled
          throw new BadRequestException("Insufficient Funds | Insufficient Quantity");
        }
    }
    return trade;
  }

  private Trade sellTrade(Trade trade, Stock stock, Account account, Asset asset)
  throws BadRequestException, NotFoundException {
    // check asset to sell is in portfolio
    boolean sufficientQuantity = trade.getQuantity() <= stock.getBidVolume();
    if (asset != null) {
      if (sufficientQuantity) {
        int quantity = trade.getQuantity() + (asset == null ? 0 : asset.getQuantity());
        this.createAsset(trade.getSymbol(), quantity, stock.getBid(), trade.getCustomerId());
        this.editStock(stock, trade.getQuantity(), stock.getBid(), "sell");
        this.editBalance(account, trade.getQuantity() * stock.getBid(), true);
        trade = this.createTrade(trade, "filled", trade.getQuantity(), stock.getBid());
      } else {
        int quantity = stock.getBidVolume() + (asset == null ? 0 : asset.getQuantity());
        this.createAsset(trade.getSymbol(), quantity, stock.getBid(), trade.getCustomerId());
        this.editStock(stock, stock.getBidVolume(), stock.getBid(), "sell");
        this.editBalance(account, stock.getBidVolume() * stock.getBid(), true);
        trade = this.createTrade(trade, "partial-filled", stock.getBidVolume(), stock.getBid());
      }
    } else {
      throw new BadRequestException("Not in portfolio");
    }
    return trade;
  }

  /**
  * create asset - always a BUY route
  * @author           Jonathan Chow
  * @param            customer id
  * @param            trade symbol
  * @param            quantity of stock
  * @param            price paid of stock
  * @return           quantity not filled
  * @throws           NotFoundException
  */
  private void createAsset(String symbol, int quantity, double price, int id) throws NotFoundException {
    Asset asset = new Asset(symbol, quantity, price, id);
    this.assetRepo.save(asset);
  }

  private Trade createTrade(Trade trade, String status, int quantity, double price) {
    trade.setDate(new Date().getTime());            // date = now
    trade.setStatus(status);
    trade.setFilledQuantity(quantity);              // filledQuantity = quantity
    trade.setAvgPrice(price);                       // avgPrice = askPrice
    if (trade.getAction().equals("buy")) {
      trade.setAsk(0);
    } else { trade.setBid(0); }
    return this.tradeRepo.save(trade);
  }

  private void editStock(Stock stock, int quantity, double lastPrice, String action)
  throws BadRequestException, NotFoundException {
    Stock newAskQuantity = new Stock();
    newAskQuantity.setSymbol(stock.getSymbol());
    if (action.equals("buy")) {
      int volume = stock.getAskVolume() - quantity;
      if (volume < 0) throw new BadRequestException("Invalid quantity deducted from stock ask vol");
      newAskQuantity.setAskVolume(volume);
    } else {
      int volume = stock.getBidVolume() - quantity;
      if (volume < 0) throw new BadRequestException("Invalid quantity deducted from stock bid vol");
      newAskQuantity.setBidVolume(volume);
    }
    newAskQuantity.setLastPrice(lastPrice);
    this.stockService.setStock(newAskQuantity);
  }

  private void editBalance(Account account, double value, boolean updateBalance) throws NotFoundException, BadRequestException {
    if (updateBalance) {
      account.updateBalance(value);  // deduct from both balance and available_balance
    } else {
      account.setAvailableBalance(account.getAvailable_balance() + value); // deduct only from available_balance
    }
    this.accRepo.save(account);
  }

  private Trade fillTrades(Trade trade, Asset asset, Account account) {
    String action = trade.getAction().equals("buy") ? "sell" : "buy";
    List<Trade> trades = this.getTrades(trade.getSymbol(), action);
    int counter = 0;
    if (action.equals("sell")) { // buying from sell orders
      while (trade.getFilledQuantity() < trade.getQuantity() && counter < trades.size()) {
        Trade pop = trades.get(counter++);
        if (pop.getAsk() > trade.getBid()) break; // break if ask > bid (limited)
        int maxPopQuantity = pop.getQuantity() - pop.getFilledQuantity();
        int leftoverQuantity = trade.getQuantity() - trade.getFilledQuantity();
        int quantity = (int) Math.min(Math.min(account.getAvailable_balance() / pop.getAsk(), maxPopQuantity), leftoverQuantity);
        trade.setAvgPrice(calcAvgPrice(trade, quantity, pop.getAsk()));
        trade.setFilledQuantity(trade.getFilledQuantity() + quantity);
        asset.setAvgPrice(calcAvgPrice(asset, quantity, pop.getAsk()));
        asset.setQuantity(quantity);
        account.updateBalance(-(quantity * pop.getAsk()));
      }
    } else { // sell
      while (trade.getFilledQuantity() < trade.getQuantity() && counter < trades.size()) {
        Trade pop = trades.get(counter++);
        if (pop.getBid() > trade.getAsk()) break; // break if ask > bid (limited)
        int maxPopQuantity = pop.getQuantity() - pop.getFilledQuantity();
        int leftoverQuantity = trade.getQuantity() - trade.getFilledQuantity();
        int quantity = (int) Math.min(maxPopQuantity, leftoverQuantity);
        trade.setAvgPrice(calcAvgPrice(trade, quantity, pop.getBid()));
        trade.setFilledQuantity(trade.getFilledQuantity() + quantity);
        asset.setAvgPrice(calcAvgPrice(asset, quantity, pop.getBid()));
        asset.setQuantity(quantity);
        account.updateBalance(quantity * pop.getBid());
      }
    }
    if (trade.getFilledQuantity() < trade.getQuantity()) {
      trade.setStatus("open");
    } else { trade.setStatus("filled"); }
    if (trade.getAction().equals("buy")) {
      trade.setAsk(0);
    } else { trade.setBid(0); }
    trade.setDate(new Date().getTime());
    this.accRepo.save(account);
    this.assetRepo.save(asset);
    return this.tradeRepo.save(trade);
  }


  // ===========================================
  // Helper function
  // ===========================================

  private Account getAccount(int id) throws NotFoundException {
    Optional<Account> accountOptional = this.accRepo.findById(id);
    if (!accountOptional.isPresent()) throw new NotFoundException("Invalid Account Id");
    return accountOptional.get();
  }

  private Asset getAsset(String symbol, int id) throws NotFoundException {
    Iterable<Asset> portfolio = this.assetRepo.findAllByCustomerId(id);
    System.out.println(portfolio);
    for (Asset asset : portfolio) {
      if (asset.getCode().equals(symbol)) return asset;
    }
    return null;
  }

  public List<Trade> getTrades(String symbol, String action) {
    Iterable<Trade> tradesIterable = this.tradeRepo.findAllBySymbol(symbol);
    List<Trade> trades = new ArrayList<>();
    for (Trade trade : tradesIterable) {
      if (trade.getAction().equals(action) && trade.getStatus().equals("open")) {
        trades.add(trade);
      }
    }
    boolean hasChange = true;
    while (hasChange) {
      hasChange = false;
      for (int i = 1; i < trades.size(); i++) {
        Trade trade1 = trades.get(i - 1);
        Trade trade2 = trades.get(i);
        boolean buy = action.equals("buy") && (trade1.getBid() > trade2.getBid());
        boolean sell = action.equals("sell") && (trade1.getAsk() < trade2.getAsk());
        if (buy || sell) {
          trades.set(i - 1, trade2);
          trades.set(i, trade1);
          hasChange = true;
        }
      }
    }
    return trades;
  }

  private double calcAvgPrice(Asset asset, int amount, double price) { // b4 quantity change
    double buffer = asset.getAvgPrice() * asset.getQuantity();
    buffer += amount * price;
    int divisor = asset.getQuantity() + amount;
    buffer = divisor == 0 ? 0 : buffer / divisor;
    return buffer;
  }

  private double calcAvgPrice(Trade trade, int amount, double price) { // b4 filled quantity change
    double buffer = trade.getAvgPrice() * trade.getFilledQuantity();
    buffer += amount * price;
    int divisor = trade.getFilledQuantity() + amount;
    buffer = divisor == 0 ? 0 : buffer / divisor;
    return buffer;
  }

}