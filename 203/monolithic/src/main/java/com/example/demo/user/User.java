package com.example.demo.user;

import java.util.Arrays;
import java.util.Collection;

import java.util.regex.*;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Column;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.Size;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.NotNull;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity // This tells Hibernate to make a table out of this class
// @Table(uniqueConstraints=@UniqueConstraint(columnNames = {"username"})) 
public class User implements UserDetails {
  @Id @GeneratedValue(strategy=GenerationType.SEQUENCE)
  private Integer id;
  private String fullName;
  @Pattern(regexp = "^[ST]\\d{7}[A-JZ]$")
  private String nric;
  // @Pattern(regexp = "^[+]65(6|8|9)\\d{7}$")
  @Pattern(regexp = "^(6|8|9)\\d{7}$")
  private String phone;
  private String address;
  @Column(name="username", nullable=false, unique=true)
  private String username;
  private String password;
  @NotNull(message = "Authorities should not be null")
  private String authorities;
  private Boolean active;
  
  public User() {}
  public User(Integer id, String fullName, String nric, String phone,
  String address, String username, String password, String authorities, boolean active) {
    this.id = id;
    this.fullName = fullName;
    this.nric = nric;
    this.phone = phone;
    this.address = address;
    this.username = username;
    this.password = password;
    this.authorities = authorities;
    this.active = active;
  }
  public User(String username, String password, String authorities) {
    this.fullName = "fullName";
    this.nric = "S9704803G";
    this.phone = "86983829";
    this.address = "address";
    this.active = true;
    this.username = username;
    this.password = password;
    this.authorities = authorities;
  }

  @Override
  public String toString() {
    return String.format(
      "id=%d // fullName=%s // nric=%s // phone=%s // address=%s // username=%s // password=%s " +
      "// authorities=%s // active=%s",
      this.id, this.fullName, this.nric, this.phone, this.address, this.username, this.password,
      this.authorities, this.active == null ? "null" : Boolean.toString(this.active)
      );
  }

  @Override
  public boolean isAccountNonExpired() { return true; }
  @Override
  public boolean isAccountNonLocked() { return true; }
  @Override
  public boolean isCredentialsNonExpired() { return true; }
  @Override
  public boolean isEnabled() { return true; }
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Arrays.asList(new SimpleGrantedAuthority(authorities));
  }
  
  public Integer getId() { return id; }
  public void setId(Integer id) { this.id = id; }

  @JsonProperty("full_name")
  public String getFullName() { return fullName; }
  @JsonProperty("full_name")
  public void setFullName(String fullName) { this.fullName = fullName; }
  
  public String getNric() { return nric; }
  public void setNric(String nric) { this.nric = nric; }

  public String getPhone() { return phone; }
  public void setPhone(String phone) { this.phone = phone; }

  public String getAddress() { return address; }
  public void setAddress(String address) { this.address = address; }

  public String getUsername() { return username; }
  public void setUsername(String username) { this.username = username; }

  public String getPassword() { return password; }
  public void setPassword(String password) { this.password = password; }

  public String getStrAuthorities() { return authorities; }
  public void setAuthorities(String authorities) { this.authorities = authorities; }

  public Boolean getActive() { return active; }
  public void setActive(Boolean active) { this.active = active; }
}