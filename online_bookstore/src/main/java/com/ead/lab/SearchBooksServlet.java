package com.ead.lab;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.Setter;

@RequestMapping("/searchBooks")
public class SearchBooksServlet {
    private static final String query = "SELECT * FROM books WHERE title LIKE ?";

    @Setter
    private DBConnectionManager db;

    @GetMapping
    @ResponseBody
    public String searchBooks(@RequestParam("title") String title) {
        StringBuilder htmlResponse = new StringBuilder();
        db.openConnection();
        Connection connection = db.getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, "%" + title + "%");
            ResultSet rs = ps.executeQuery();
            ArrayList<Book> books = new ArrayList<>();
            while (rs.next()) {
                books.add(new Book(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getDouble(5)));
            }

            htmlResponse.append("<html>");
            htmlResponse.append("<head>");
            htmlResponse.append("<style>");
            htmlResponse.append("body { font-family: Arial, sans-serif; margin: 20px; background-color: #f9f9f9; color: #333; }");
            htmlResponse.append("h2 { color: #007bff; }");
            htmlResponse.append("table { width: 100%; border-collapse: collapse; margin-top: 20px; border: 2px solid black; border-spacing: 10px; }");
            htmlResponse.append("th, td { border: 1px solid #ddd; padding: 10px; text-align: left; }");
            htmlResponse.append("th { background-color: #007bff; color: white; }");
            htmlResponse.append("tr:nth-child(even) { background-color: #f2f2f2; }");
            htmlResponse.append("tr:hover { background-color: #ddd; }");
            htmlResponse.append("</style>");
            htmlResponse.append("</head>");
            htmlResponse.append("<body>");
            htmlResponse.append("<h2>Showing search results for: '").append(title).append("'</h2>");
            htmlResponse.append("<table>");
            htmlResponse.append("<tr>");
            htmlResponse.append("<th>Book ID</th>");
            htmlResponse.append("<th>Title</th>");
            htmlResponse.append("<th>Author</th>");
            htmlResponse.append("<th>Price</th>");
            htmlResponse.append("</tr>");

            for (Book book : books) {
                htmlResponse.append("<tr>");
                htmlResponse.append("<td>").append(book.getId()).append("</td>");
                htmlResponse.append("<td>").append(book.getTitle()).append("</td>");
                htmlResponse.append("<td>").append(book.getAuthor()).append("</td>");
                htmlResponse.append("<td>").append(book.getPrice()).append("</td>");
                htmlResponse.append("</tr>");
            }

            htmlResponse.append("</table>");
            htmlResponse.append("</body>");
            htmlResponse.append("</html>");

        } catch (SQLException se) {
            se.printStackTrace();
            htmlResponse.append("<h1>").append(se.getMessage()).append("</h1>");
        } catch (Exception e) {
            e.printStackTrace();
            htmlResponse.append("<h1>").append(e.getMessage()).append("</h1>");
        }

        db.closeConnection();
        return htmlResponse.toString();
    }
}
