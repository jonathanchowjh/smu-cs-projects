package com.example.demo.trades;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

import com.example.demo.config.ForbiddenException;
import com.example.demo.config.BadRequestException;
import com.example.demo.config.NotFoundException;
import com.example.demo.config.ConflictException;
import com.example.demo.config.UnauthorizedException;
import com.example.demo.security.AuthorizedUser;

@RestController
public class TradeController {
  private TradeService tradeService;

  @Autowired
  public TradeController(TradeService tradeService) {
    this.tradeService = tradeService;
  }

  /**
  * POST /trades
  * Authentication    USER ONLY
  * @author           Jonathan Chow
  * @param            Trade
  * @return           Trade
  * @throws           BadRequestException ConflictException
  * @ResponseStatus   201 CREATED
  * DETAILS           
  */
  @RequestMapping(value = "/trades", method = RequestMethod.POST)
  @ResponseStatus(HttpStatus.CREATED)
  public @ResponseBody Trade addNewTrade (@Valid @RequestBody Trade trade)
  throws UnauthorizedException, BadRequestException, NotFoundException {
    System.out.println("POST /trades | " + trade);
    try {
      // AuthorizedUser context = new AuthorizedUser();
      // context.validate();
      Trade saved = this.tradeService.createTrade(trade);
      System.out.println(saved);
      return saved;
    } catch (Exception e) {
      System.out.println(e.getMessage());
      throw e;
    }
  }

  // // Cancel Trade
  @PutMapping("/trades/{id}")
  @ResponseStatus(HttpStatus.OK)
  public @ResponseBody Trade cancelTrade(@PathVariable int id) throws UnauthorizedException {
      System.out.println("PUT /trades | ");
      AuthorizedUser context = new AuthorizedUser();
      context.validate();

      Trade trade = tradeService.getTrade(id);
      trade.setStatus("cancelled");
      return trade;
  }

    // // Get Trade by ID
    // @GetMapping("/trades/{id}")
    // @ResponseStatus(HttpStatus.OK)
    // public @ResponseBody Trade getTradeByID(@PathVariable int id) throws UnauthorizedException {
    //     AuthorizedUser context = new AuthorizedUser();
    //     context.validate();
    //     Trade trade = tradeService.getTrade(id);

    //     if (trade != null) {
    //       if (trade.getCustomerId() != context.getId()) {
    //          System.out.println("Trade of this ID is not accessible to this user");
    //          // return or throw
    //       }
    //     }

    // }

}