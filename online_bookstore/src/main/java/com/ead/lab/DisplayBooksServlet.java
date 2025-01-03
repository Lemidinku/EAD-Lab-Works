package com.ead.lab;
//Name Lemi Dinku Gilo

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.Setter;

@RequestMapping("/displayBooks")
public class DisplayBooksServlet {
    private static final String query = "SELECT * FROM books";

    @Setter
    private DBConnectionManager db;

    @GetMapping
    @ResponseBody
    public String displayBooks() {
        StringBuilder htmlResponse = new StringBuilder();
        ResultSet rs = null;
        PreparedStatement ps = null;
        Connection connection = null;

        try {
            db.openConnection();
            connection = db.getConnection();
            ps = connection.prepareStatement(query);
            rs = ps.executeQuery();

            ArrayList<Book> books = new ArrayList<>();
            while (rs.next()) {
                books.add(new Book(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getDouble(4)));
            }

            htmlResponse.append("<html><head><title>Book List</title></head><body>");
            htmlResponse.append("<h2>Book List</h2>");
            htmlResponse.append("<table border='1'>");
            htmlResponse.append("<tr>")
                        .append("<th>Book ID</th>")
                        .append("<th>Title</th>")
                        .append("<th>Author</th>")
                        .append("<th>Price</th>")
                        .append("<th>Delete</th>")
                        .append("</tr>");

            for (Book book : books) {
                htmlResponse.append("<tr>")
                            .append("<td>").append(book.id).append("</td>")
                            .append("<td>").append(escapeHtml(book.title)).append("</td>")
                            .append("<td>").append(escapeHtml(book.author)).append("</td>")
                            .append("<td>").append(book.price).append("</td>")
                            .append("<td><form method='post' action='/books/deleteBook?id=").append(book.id)
                            .append("'>")
                            .append("<input type='submit' value='Delete'/>")
                            .append("</form></td>")
                            .append("</tr>");
            }
            htmlResponse.append("</table></body></html>");
        } catch (SQLException se) {
            se.printStackTrace();
            htmlResponse.append("<h1>Error occurred: ").append(escapeHtml(se.getMessage())).append("</h1>");
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                db.closeConnection();
            } catch (SQLException ignored) {
            }
        }

        return htmlResponse.toString();
    }

    private String escapeHtml(String input) {
        // Simple HTML escaping (consider using Apache Commons Lang or similar libraries in real projects)
        return input.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }
}
