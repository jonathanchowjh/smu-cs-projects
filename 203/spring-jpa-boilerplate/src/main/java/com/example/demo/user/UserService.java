package com.example.demo.user;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;

import com.example.demo.config.ForbiddenException;
import com.example.demo.config.BadRequestException;
import com.example.demo.config.NotFoundException;
import com.example.demo.config.ConflictException;
import com.example.demo.portfolio.*;
// import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.demo.cms.*;
import com.example.demo.accounts.*;
import com.example.demo.trades.*;
import com.example.demo.portfolio.*;

@Service
public class UserService {
  private UserRepository UR;
  private StockService stockService;
  private CMSRepository cmsRepo;
  private AccountsRepository accountsRepo;
  private TransfersRepository transfersRepo;
  private AssetRepository assetRepo;
  private TradeRepository tradeRepo;
  
  @Autowired
  public UserService(UserRepository UR, StockService stockService,
  CMSRepository cmsRepo, AccountsRepository accountsRepo, TransfersRepository transfersRepo,
  AssetRepository assetRepo, TradeRepository tradeRepo) {
    this.UR = UR;
    this.stockService = stockService;
    this.cmsRepo = cmsRepo;
    this.accountsRepo = accountsRepo;
    this.transfersRepo = transfersRepo;
    this.assetRepo = assetRepo;
    this.tradeRepo = tradeRepo;
  }
  
  /**
  * Default Functions
  * @author     Jonathan Chow
  * findAll, save, findById, findByUsername
  */
  public Iterable<User> findAll() { return this.UR.findAll(); }
  public User save(User user) { return this.UR.save(user); }
  public User findById(int id) {
    Optional<User> search = this.UR.findById(id);
    if (!search.isPresent()) return null;
    return search.get();
  }
  public User findByUsername(String username) {
    Optional<User> search = this.UR.findByUsername(username);
    if (!search.isPresent()) return null;
    return search.get();
  }

  /**
  * reset - delete all users, then insert 1 manager, 2 analyst
  * @author     Jonathan Chow
  * @param      null
  * @return     null
  * @throws     BadRequestException ConflictException
  */
  public void reset() throws BadRequestException, ConflictException {
    this.UR.deleteAll();
    this.cmsRepo.deleteAll();
    this.accountsRepo.deleteAll();
    this.transfersRepo.deleteAll();
    this.assetRepo.deleteAll();
    this.tradeRepo.deleteAll();
    User m = new User("manager_1", "01_manager_01", "ROLE_MANAGER");
    User a1 = new User("analyst_1", "01_analyst_01", "ROLE_ANALYST");
    User a2 = new User("analyst_2", "02_analyst_02", "ROLE_ANALYST");
    User u = new User("user", "password", "ROLE_USER");
    this.createUser(m);
    this.createUser(a1);
    this.createUser(a2);
    this.createUser(u);
    this.stockService.marketOpen();
  }

  /**
  * getUser - if (user is not manager) only can view themselves
  * @author     Jonathan Chow
  * @param      int id, User user, boolean isManager
  * @return     User userOfId
  * @throws     ForbiddenException NotFoundException
  */
  public User getUser(int id, User user, boolean isManager)throws ForbiddenException, NotFoundException {
    if ((user.getId() != id) && !isManager) throw new ForbiddenException("Unauthorized to view another user");
    Optional<User> search = this.UR.findById(id);
    if (!search.isPresent()) throw new NotFoundException("Searched User does not exists");
    return search.get();
  }
  

  /**
  * createUser - if (user doesnt exists) create user
  * @author     Jonathan Chow
  * @param      User user
  * @return     User createdUser
  * @throws     BadRequestException
  */
  public User createUser(User user) throws BadRequestException, ConflictException {
    if (!this.validateNric(user.getNric())) throw new BadRequestException("Invalid NRIC");
    Optional<User> search = this.UR.findByUsername(user.getUsername());
    if (search.isPresent()) throw new ConflictException("User already exists");
    String passwordHash = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(12));
    user.setPassword(passwordHash);

    // create portfolio
    User saved = this.UR.save(user);
    return saved;
  }
  
  /**
  * editUser - if (manager) edit everything, else edit { password, address, phone }
  * @author     Jonathan Chow
  * @param      User edits, int id, User user, boolean isManager
  * @return     User editedUser
  * @throws     ForbiddenException NotFoundException BadRequestException
  */
  public User editUser(User edits, int id, User user, boolean isManager)
  throws NotFoundException, ForbiddenException, BadRequestException {
    Optional<User> search = this.UR.findById(id);
    if (!search.isPresent()) throw new NotFoundException("Invalid Id: " + id);
    if (edits.getNric() != null && !this.validateNric(edits.getNric())) throw new BadRequestException("Invalid NRIC");
    if ((id != user.getId()) && !isManager) throw new ForbiddenException("User cannot edit another user");
    User searchUser = search.get();

    String passwordHash = (edits.getPassword() != null) ? BCrypt.hashpw(edits.getPassword(), BCrypt.gensalt(12)) : null;
    if (passwordHash != null) searchUser.setPassword(passwordHash);
    if (edits.getAddress() != null) searchUser.setAddress(edits.getAddress());
    if (edits.getPhone() != null) searchUser.setPhone(edits.getPhone());
    if (!isManager) return this.UR.save(searchUser);

    if (edits.getActive() != null) searchUser.setActive(edits.getActive());
    if (edits.getFullName() != null) searchUser.setFullName(edits.getFullName());
    if (edits.getNric() != null) searchUser.setNric(edits.getNric());
    if (edits.getUsername() != null) searchUser.setUsername(edits.getUsername());
    if (edits.getStrAuthorities() != null) searchUser.setAuthorities(edits.getStrAuthorities());
    return this.UR.save(searchUser);
  }

  // ============================================================
  // Helper Functions
  // ============================================================

  /**
   * Validates Singapore NRIC / FIN in 2 stages:
   * 1) Ensure first letter starts with S, T, F or G and the last letter is A-Z
   * 2) Calculate weight of digits in between first and last character of the input string,
   * determine what the last letter should be, then match it against the last character of the
   * input string.
   *
   * @param inputString NRIC / FIN string to be validated
   * @return true if NRIC/FIN passes, false otherwise
   */
  private boolean validateNric(String inputString) {
    String nricToTest = inputString.toUpperCase();

    // first letter must start with S, T, F or G. Last letter must be A - Z
    if (!Pattern.compile("^[STFG]\\d{7}[A-Z]$").matcher(nricToTest).matches()) {
      return false;
    } else {
      char[] icArray = new char[9];
      char[] st = "JZIHGFEDCBA".toCharArray();
      char[] fg = "XWUTRQPNMLK".toCharArray();

      for (int i = 0; i < 9; i++) {
        icArray[i] = nricToTest.charAt(i);
      }

      // calculate weight of positions 1 to 7
      int weight = (Integer.parseInt(String.valueOf(icArray[1]), 10)) * 2 + 
        (Integer.parseInt(String.valueOf(icArray[2]), 10)) * 7 +
        (Integer.parseInt(String.valueOf(icArray[3]), 10)) * 6 +
        (Integer.parseInt(String.valueOf(icArray[4]), 10)) * 5 +
        (Integer.parseInt(String.valueOf(icArray[5]), 10)) * 4 +
        (Integer.parseInt(String.valueOf(icArray[6]), 10)) * 3 +
        (Integer.parseInt(String.valueOf(icArray[7]), 10)) * 2;

      int offset = icArray[0] == 'T' || icArray[0] == 'G' ? 4 : 0;

      int lastCharPosition = (offset + weight) % 11;

      if (icArray[0] == 'S' || icArray[0] == 'T') {
        return icArray[8] == st[lastCharPosition];
      } else if (icArray[0] == 'F' || icArray[0] == 'G') {
        return icArray[8] == fg[lastCharPosition];
      } else {
        return false; // this line should never reached due to regex above
      }
    }
  }
}