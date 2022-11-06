package ru.itmo.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.itmo.model.Product;
import ru.itmo.repositoty.JPARepository;

import java.io.IOException;
import java.sql.SQLException;

public class AddProductServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(AddProductServlet.class);

    private final JPARepository repository;

    public AddProductServlet(JPARepository repository) {
        this.repository = repository;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getParameter("name");
        long price = Long.parseLong(request.getParameter("price"));

        try {
            repository.insertProduct(new Product(
                    name,
                    price
            ));
            response.setContentType("text/html");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println("OK");
        } catch (SQLException e) {
            logger.error("Sql exception occurred: ", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}