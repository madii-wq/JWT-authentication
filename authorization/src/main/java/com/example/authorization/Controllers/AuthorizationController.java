package com.example.authorization.Controllers;

import com.example.authorization.Entity.UserEntity;
import com.example.authorization.Repositories.UserEntityRepository;
import com.example.authorization.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("auth")
public class AuthorizationController {

    @Autowired
    private UserEntityRepository userEntityRepository;
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody UserEntity user) {
        String token = userService.login(user);
        UserEntity currentUser = userEntityRepository.findByUsername(user.getUsername());
        Map<String, Object> response = new HashMap<>();
        response.put("id", currentUser.getId());
        response.put("username", currentUser.getUsername());
        response.put("token", token);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/register")
    public String register(@RequestBody UserEntity user) {
        return userService.register(user.getUsername(), user.getPassword());
    }
    @GetMapping("works")
    public ResponseEntity<Map<String, Object>> getWorks() {
        Map<String, Object> response = new HashMap<>();
        response.put("condition", "works");
        return ResponseEntity.ok(response);
    }
}