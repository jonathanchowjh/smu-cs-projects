package com.example.demo;

import com.example.demo.user.User;
import com.example.demo.user.UserRepository;
import com.example.demo.user.UserService;

import static org.mockito.Mockito.*;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.*;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
  @Mock
  private UserRepository users;
  @InjectMocks
  private UserService userService;

  @Test
  public void addUser_NewUser_ReturnSavedUser()  {
    User user  = new User(900,"fullName","S9804803G","+6591234567","Address","manager_1","01_manager_01","ROLE_MANAGER",true);

    // mock Repository Operations
    when(users.save(any(User.class)))
      .thenReturn(user);
    when(users.findByUsername(any(String.class)))
      .thenReturn(Optional.of(user));
    when(users.findById(1))
      .thenReturn(Optional.of(user));
    
    User savedUser = userService.save(user);
    Assert.assertEquals(savedUser, user);

    // verify
    verify(users).save(user);
    verify(users).findByUsername("manager_1");
    verify(users).findById(900);
  }
}
