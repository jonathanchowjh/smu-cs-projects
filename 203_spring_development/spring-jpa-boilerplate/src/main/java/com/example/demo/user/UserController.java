package com.example.demo.user;

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
public class UserController {
  private UserService US;

  @Autowired
  public UserController(UserService US) {
    this.US = US;
  }

  /**
  * Default Functions
  * @author     Jonathan Chow
  * getAllUsers
  */
  @RequestMapping(value = "/all", method = RequestMethod.GET)
  public @ResponseBody Iterable<User> getAllUsers() {
    return US.findAll();
  }
  /**
  * POST /reset
  * Authentication    no auth
  * @author           Jonathan Chow
  * @param            null
  * @return           null
  * @throws           BadRequestException ConflictException
  * @ResponseStatus   200 OK
  * DETAILS           resets all entities (except 1 manager and 2 analyst)
  */
  @RequestMapping(value = "/reset", method = RequestMethod.POST)
  @ResponseStatus(HttpStatus.OK)
  public @ResponseBody void reset() throws BadRequestException, ConflictException {
    System.out.println("POST /reset");
    this.US.reset();
  }

  /**
  * POST /customers
  * Authentication    MANAGER ONLY
  * @author           Jonathan Chow
  * @param            User Object to be Created
  * @return           User Object Created
  * @throws           BadRequestException
  * @ResponseStatus   201 CREATED
  * DETAILS           Create User if not exists, throw 400 BAD REQUEST if exists
  */
  @RequestMapping(value = "/customers", method = RequestMethod.POST)
  @ResponseStatus(HttpStatus.CREATED)
  public @ResponseBody User addNewUser (@Valid @RequestBody User user)
  throws BadRequestException, ConflictException, UnauthorizedException {
    System.out.println("POST /customers | " + user);
    AuthorizedUser context = new AuthorizedUser();
    context.validate();
    return this.US.createUser(user);
  }

  /**
  * GET /customers/{id}
  * Authentication    MANAGER view anyone, USER can view themselves
  * @author           Jonathan Chow
  * @param            id of user searched (@PathVariable)
  * @return           User Object Found (@ResponseBody)
  * @throws           BadRequestException NotFoundException
  * @ResponseStatus   200 OK
  * DETAILS           Return user if exists, unauthorized (403 FORBIDDEN), invalid ID (404 NOT FOUND)
  */
  @RequestMapping(value = "/customers/{id}", method = RequestMethod.GET)
  @ResponseStatus(HttpStatus.OK)
  public @ResponseBody User getUser (@PathVariable int id)
  throws ForbiddenException, NotFoundException, UnauthorizedException {
    System.out.println("GET /customers | " + id);
    AuthorizedUser context = new AuthorizedUser();
    context.validate();
    return this.US.getUser(id, context.getUser(), context.isManager());
  }

  /**
  * PUT /customers
  * Authentication    MANAGER edit anyone, USER & ANALYST can edit themselves (only address, password, phone)
  * @author           Jonathan Chow
  * @param            User Details to be edited (@RequestBody)
  * @return           User Object Edited (@ResponseBody)
  * @throws           BadRequestException NotFoundException BadRequestException
  * @ResponseStatus   200 OK
  * DETAILS           Return user if edited, unauthorized (403 FORBIDDEN), invalid edited user (400 BAD REQUEST)
  */
  @RequestMapping(value = "/customers/{id}", method = RequestMethod.PUT)
  @ResponseStatus(HttpStatus.OK)
  public @ResponseBody User editUser (@Valid @RequestBody User user, @PathVariable int id)
  throws NotFoundException, ForbiddenException, BadRequestException, UnauthorizedException {
    System.out.println("PUT /customers | " + user);
    AuthorizedUser context = new AuthorizedUser();
    context.validate();
    return this.US.editUser(user, id, context.getUser(), context.isManager());
  }
}