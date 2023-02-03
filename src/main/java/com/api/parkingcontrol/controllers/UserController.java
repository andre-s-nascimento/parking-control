package com.api.parkingcontrol.controllers;

import com.api.parkingcontrol.models.UserModel;
import com.api.parkingcontrol.services.UserService;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*", maxAge = 3400)
@RequestMapping("/user")
public class UserController {
  @Autowired
  UserService userService;

//  @PostMapping("/save")
//  public String save(@RequestBody UserModel user) {
//    //save user
//  }
//  @PutMapping("/update/{id}")
//  public String update(@PathVariable UUID id, @RequestBody UserModel user) {
//    //update user record
//  }
//  @DeleteMapping("/delete/{id}")
//  public String delete(@PathVariable UUID id) {
//    //delete user record
//  }
//  @GetMapping("/get/{id}")
//  public UserModel get(@PathVariable UUID id) {
//    //get user record
//  }
@PreAuthorize("hasRole('ROLE_ADMIN')")
@GetMapping("/{id}")
  public ResponseEntity<Object> get(@PathVariable UUID id) {
    Optional<UserModel> userModelOptional = userService.findById(id);
    if (!userModelOptional.isPresent()){
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User id: " + id + " dont exist");
    }
return ResponseEntity.status(HttpStatus.OK).body(userModelOptional.get());
  }

}
