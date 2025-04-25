package com.mtran.mvc.service;

import com.mtran.mvc.dto.UserDTO;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
  UserDTO findByEmail(String email);
  void createUser(UserDTO user);
  void updateUser(UserDTO user);
  void forgotPassword(String email, String password);
  void changePassword(String email, String oldPassword, String newPassword);
}
