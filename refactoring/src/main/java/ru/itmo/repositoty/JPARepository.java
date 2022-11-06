package ru.itmo.repositoty;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import ru.itmo.model.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.sql.DataSource;

public class JPARepository {
    private final DataSource dataSource;

    public JPARepository(Properties properties) {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("org.postgresql.Driver");
        config.setJdbcUrl(properties.getProperty("database.url"));
        config.setUsername(properties.getProperty("database.username"));
        config.setPassword(properties.getProperty("database.password"));
        this.dataSource = new HikariDataSource(config);
    }

    public void insertProduct(Product product) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                 "insert into Product (Name, Price) values (?, ?);"
             )
        ) {
            statement.setString(1, product.name());
            statement.setLong(2, product.price());

            statement.executeUpdate();
        }
    }

    public List<Product> findAllProducts() throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("select * from Product;")
        ) {
            return fetchListProductFromSql(statement);
        }
    }

    public Product maxProductByPrice() throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                 "select * from Product order by price desc limit 1;"
             )
        ) {
            List<Product> productList = fetchListProductFromSql(statement);

            assert productList.size() == 1;

            return productList.get(0);
        }
    }

    public Product minProductByPrice() throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                 "select * from Product order by price limit 1;"
             )
        ) {
            List<Product> productList = fetchListProductFromSql(statement);

            assert productList.size() == 1;

            return productList.get(0);
        }
    }

    public int countProducts() throws SQLException {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
        ) {
            ResultSet resultSet = statement.executeQuery("select count(*) from Product;");
            resultSet.next();

            return resultSet.getInt(1);
        }
    }

    public long summaryPrice() throws SQLException {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
        ) {
            ResultSet resultSet = statement.executeQuery("select sum(price) from Product;");
            resultSet.next();

            return resultSet.getLong(1);
        }
    }

    public void clear() throws SQLException {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
        ) {
            statement.executeUpdate("delete from Product;");
        }
    }

    private List<Product> fetchListProductFromSql(PreparedStatement statement) throws SQLException {
        try (ResultSet resultSet = statement.executeQuery()) {
            ArrayList<Product> products = new ArrayList<>();
            while (resultSet.next()) {
                products.add(
                    new Product(
                        resultSet.getString("name"),
                        resultSet.getInt("price")
                    )
                );
            }
            return products;
        }
    }
}
