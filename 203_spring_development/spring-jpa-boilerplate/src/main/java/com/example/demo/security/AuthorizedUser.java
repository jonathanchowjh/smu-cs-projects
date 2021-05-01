package com.example.demo.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.GrantedAuthority;
import com.example.demo.user.User;
import com.example.demo.config.UnauthorizedException;

public class AuthorizedUser {
  private User user;
  public AuthorizedUser() {
    if (SecurityContextHolder.getContext().getAuthentication() == null) {
      this.user = null;
    } else {
      this.user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
  }
  public User getUser() { return this.user; }
  public int getId() { return this.user.getId(); }
  public boolean isManager() {
    boolean isManager = false;
    for (GrantedAuthority authority : this.user.getAuthorities()) {
      if (authority.getAuthority().equals("ROLE_MANAGER")) {
        isManager = true;
        break;
      }
    }
    return isManager;
  }

  public void validate() throws UnauthorizedException {
    if (this.user == null || !this.user.getActive()) throw new UnauthorizedException("user not active");
  }
  
  public boolean isAnalyst() {
    boolean isAnalyst = false;
    for (GrantedAuthority authority : this.user.getAuthorities()) {
      if (authority.getAuthority().equals("ROLE_ANALYST")) {
        isAnalyst = true;
        break;
      }
    }
    return isAnalyst;
  }
  
}