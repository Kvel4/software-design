package ru.itmo.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.itmo.model.Product;
import ru.itmo.repositoty.JPARepository;
import ru.itmo.html.HtmlUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class GetProductsServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(GetProductsServlet.class);

    private final JPARepository repository;

    public GetProductsServlet(JPARepository repository) {
        this.repository = repository;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            List<Product> productList = repository.findAllProducts();
            response.getWriter().println(HtmlUtils.htmlProducts(productList));
            response.setContentType("text/html");
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (SQLException e) {
            logger.error("Sql exception occurred: ", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}