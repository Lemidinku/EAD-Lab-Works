package com.ead.lab;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.Setter;
//Name Lemi Dinku Gilo

@RequestMapping("/addBook")
public class BookRegistrationServlet {
    private static final String query = "INSERT INTO books(title, author, price) VALUES(?, ?, 10)";
    @Setter
    private DBConnectionManager db;

    @GetMapping
    @ResponseBody
    public String showForm() {
        return "<html><body>"
                + "<h2>Book Registration Form</h2>"
                + "<form method='post' action='/books/addBook'>"
                + "Title: <input type='text' name='title'><br>"
                + "Author: <input type='text' name='author'><br>"
                + "Price: <input type='number' step='0.01' name='price'><br>"
                + "<input type='submit' value='Add Book'>"
                + "</form>"
                + "</body></html>";
    }

    @PostMapping
    @ResponseBody
    public String addBook(@RequestParam String title, @RequestParam String author, @RequestParam Double price) {
        db.openConnection();

        try {
            PreparedStatement ps = db.getConnection().prepareStatement(query);
            ps.setString(1, title);
            ps.setString(2, author);
//            ps.setDouble(3, price);
            int count = ps.executeUpdate();
            db.closeConnection();
            if (count == 1) {
                return "Book Registration Successful";
            } else {
                return "Book Registration Failed";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Book Registration Failed";
        } finally {
            db.closeConnection();
        }
    }
}
