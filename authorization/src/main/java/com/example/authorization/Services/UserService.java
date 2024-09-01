package com.example.authorization.Services;

import com.example.authorization.Entity.UserEntity;
import com.example.authorization.Repositories.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserEntityRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public String login(UserEntity user) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        return authentication.isAuthenticated() ? jwtService.generateToken(user.getUsername()) : "fail";
    }
    public String register(String username, String password) {

        UserEntity user = userRepository.findByUsername(username);
        if (user == null) {
            user = new UserEntity();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user);
            return "success";
        }else{
            return "The username is already in use";
        }
    }
}
