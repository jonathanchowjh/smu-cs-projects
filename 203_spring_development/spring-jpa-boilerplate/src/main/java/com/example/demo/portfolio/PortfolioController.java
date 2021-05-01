package com.example.demo.portfolio;

import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import com.example.demo.security.AuthorizedUser;
import com.example.demo.config.NotFoundException;

import java.util.Optional;
import java.lang.Iterable;

@RestController
public class PortfolioController {
    private AssetRepository assetRepository;

    @Autowired
    public PortfolioController(AssetRepository assetRepository) {
        this.assetRepository = assetRepository;
    }

    // @RequestMapping(value = "/portfolio", method = RequestMethod.GET)
    // @ResponseStatus(HttpStatus.OK)
    // public @ResponseBody Portfolio getPortfolio() throws NotFoundException {
    //     AuthorizedUser context = new AuthorizedUser();
    //     Optional<Portfolio> portfolio = this.pfRepository.findById(context.getId());
    //     if (!portfolio.isPresent()) throw new NotFoundException("Portfolio Not Found");
    //     return portfolio.get();
    // }
}