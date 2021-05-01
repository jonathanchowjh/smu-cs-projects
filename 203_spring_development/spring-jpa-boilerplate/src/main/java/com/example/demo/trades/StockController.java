package com.example.demo.trades;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.util.MultiValueMap;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.*;
import java.lang.IllegalArgumentException;

import com.example.demo.user.UserRepository;
import com.example.demo.config.ForbiddenException;
import com.example.demo.config.BadRequestException;
import com.example.demo.config.NotFoundException;
import com.example.demo.config.ConflictException;
import com.example.demo.config.UnauthorizedException;
import com.example.demo.security.AuthorizedUser;

@RestController
public class StockController {
	private StockService stockService;

	@Autowired
	public StockController(StockService stockService) {
		this.stockService = stockService;
	}

	/**
  * GET /stocks
  * Authentication    USER ONLY
  * @author           Jonathan Chow
  * @param            null
  * @return           Iterable<Stock>
  * @throws           BadRequestException UnauthorizedException
  * @ResponseStatus   200 OK
  */
  @RequestMapping(value = "/stocks", method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
	public @ResponseBody Iterable<Stock> getStocks()
	throws BadRequestException, UnauthorizedException {
		System.out.println("GET /stocks");
		AuthorizedUser context = new AuthorizedUser();
    context.validate();
		Iterable<Stock> stocks = this.stockService.getStocks();
		if (stocks == null) throw new BadRequestException("Market Closed");
    return stocks;
	}
	
	/**
  * GET /stocks/{id}
  * Authentication    USER ONLY
  * @author           Jonathan Chow
  * @param            symbol
  * @return           Stock
  * @throws           BadRequestException UnauthorizedException
  * @ResponseStatus   200 OK
  */
  @RequestMapping(value = "/stocks/{symbol}", method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
	public @ResponseBody Stock getStock(@PathVariable String symbol)
	throws BadRequestException, NotFoundException, UnauthorizedException {
		System.out.println("GET /stocks/{symbol}");
		AuthorizedUser context = new AuthorizedUser();
    context.validate();
		Stock stock = this.stockService.getStock(symbol);
		if (stock == null) throw new BadRequestException("Market Closed");
    return stock;
  }

}

