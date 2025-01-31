package com.itsc.auction.Auth;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.itsc.auction.Auction.Auction;
import com.itsc.auction.User.User;
import com.itsc.auction.User.UserService;
import com.itsc.auction.Utils.JwtUtil;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@CrossOrigin(origins = "http://127.0.0.1:5500")
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


    @GetMapping("/auth/profile")
    public ResponseEntity<?> getUser(HttpServletRequest request) {
    System.out.println("Getting user profile");
    try {
        Claims claims = (Claims) request.getAttribute("user");

        if (claims == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Unauthorized: No user claims found in request.");
        }

        String username = claims.getSubject();
        System.out.println("Username: " + username);
        User user = userService.findUserByUsername(username);

        System.out.println("User found with username: " + user);
        if (user == null) {
            System.out.println("User not found with username: " + username);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User not found with username: " + username);
        }
        return ResponseEntity.ok(user);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An unexpected error occurred: " + e.getMessage());
    }
}

    }

   


