package com.itsc.auction.Auth;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.itsc.auction.User.User;
import com.itsc.auction.User.UserService;
import com.itsc.auction.Utils.JwtUtil;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.servlet.http.Cookie;

@RestController
public class AuthController {
    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/auth/signup")
    public ResponseEntity<Map<String, String>>signup(@RequestBody @Valid User user){
        
        Map<String, String> response = new HashMap<>();

        Boolean emailTaken = userService.emailTaken(user.getEmail());
        if (emailTaken){
            response.put("email", "Email is Taken");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        Boolean usernameTaken = userService.usernameTaken(user.getUsername());
        if (usernameTaken){
            response.put("username", "Username is Taken");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }


        userService.addUser(user);
        
        response.put("message", "Signup successful");

        return ResponseEntity.status(HttpStatus.CREATED)
                .header(org.springframework.http.HttpHeaders.SET_COOKIE)
                .body(response);
   
    }

    
    @PostMapping("/auth/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody @Valid Login loginData){
        Map<String, String> response = new HashMap<>();
        User user = userService.findUserByUsername(loginData.getUsername());
        if (user == null){
            response.put("error", "Unauthorized");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        Boolean passwordMatch = userService.checkPassword(loginData.getPassword(), user.getPassword());
        if (!passwordMatch){
            response.put("error", "Unauthorized");
            return ResponseEntity.status(422).body(response);
        }


        String token = jwtUtil.generateToken(user.getUsername());
       
        ResponseCookie cookie = ResponseCookie.from("accessToken", token)
            .httpOnly(true) 
            .secure(true) 
            .path("/") 
            .maxAge(60 * 60 * 24) 
            .sameSite("Strict")
            .build();

        response.put("message", "Login successful");
        response.put("token", token); // delete this later


        return ResponseEntity.status(HttpStatus.OK)
                .header(org.springframework.http.HttpHeaders.SET_COOKIE, cookie.toString())
                .body(response);


        }

    @DeleteMapping("/auth/logout")
    public ResponseEntity<Map<String, Object>> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("accessToken", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        
        // Return success response
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", "Logged out successfully");
        
        return ResponseEntity.ok(responseBody);
}



    }

   


