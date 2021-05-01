package com.example.demo.trades;

import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import com.example.demo.config.ForbiddenException;
import com.example.demo.config.BadRequestException;
import com.example.demo.config.NotFoundException;
import com.example.demo.config.ConflictException;

@Service
public class StockService {
  private StocksRepository stockRepo;
  private String[] stockList = { "A17U", "C31", "C38U", "C09", "C52", "D01", "D05",
	"G13", "H78", "C07", "J36", "J37", "BN4", "AJBU", "N2IU", "ME8U", "M44U", "O39",
	"S58", "U96", "S68", "C6L", "Z74", "S63", "Y92", "U11", "U14", "V03", "F34", "BS6" };

  @Autowired
  public StockService(StocksRepository stockRepo) {
    this.stockRepo = stockRepo;
  }

  // @Scheduled(fixedDelay = 600000) // every 10 mins
  @Scheduled(cron = "0 9 * * 1-5 ?", zone = "Asia/Singapore")
  public void marketOpen() {
    if (!this.isOpen()) {
      this.marketClose();
      return;
    }
    System.out.println(" === market open === ");
    this.stockRepo.deleteAll();
    for (String symbol : this.stockList) {
      Stock stock = new Stock(symbol);
      this.stockRepo.save(stock);
    }
  }

  @Scheduled(cron = "0 17 * * 1-5 ?", zone = "Asia/Singapore")
  public void marketClose() {
    System.out.println(" === market close === ");
    this.stockRepo.deleteAll();
  }

  public boolean isOpen() {
    // int hour = LocalDateTime.now().getHour();
    // int day = LocalDateTime.now().getDayOfWeek().getValue();
    // if (day > 5 || day < 1) return false;
    // if (hour >= 17 || hour < 9) return false;
    return true;
  }

  public Stock getStock(String symbol) throws NotFoundException {
    if (!this.isOpen()) return null;
    if (symbol == null) throw new NotFoundException("Invalid Stock symbol");
    Optional<Stock> search = this.stockRepo.findBySymbol(symbol);
    if (!search.isPresent()) throw new NotFoundException("Invalid Stock symbol");
    return search.get();
  }

  public Iterable<Stock> getStocks() {
    if (!this.isOpen()) return null;
    return this.stockRepo.findAll();
  }

  public Stock setStock(Stock stock) throws NotFoundException {
    Stock search = this.getStock(stock.getSymbol());
    if (search == null) return null;

    // put all notNull values into new saved stock
    if (stock.getLastPrice() != null) search.setLastPrice(stock.getLastPrice());
    if (stock.getAskVolume() != null) search.setAskVolume(stock.getAskVolume());
    if (stock.getBidVolume() != null) search.setBidVolume(stock.getBidVolume());
    if (stock.getAsk() != null) search.setAsk(stock.getAsk());
    if (stock.getBid() != null) search.setBid(stock.getBid());
    return this.stockRepo.save(search);
  }
}