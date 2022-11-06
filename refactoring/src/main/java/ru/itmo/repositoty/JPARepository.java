package ru.itmo.repositoty;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(JPARepository.class);

    private final DataSource dataSource;

    public JPARepository(Properties properties) {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("org.postgresql.Driver");
        config.setJdbcUrl(properties.getProperty("database.url"));
        config.setUsername(properties.getProperty("database.username"));
        config.setPassword(properties.getProperty("database.password"));
        this.dataSource = new HikariDataSource(config);
    }

    public void insertProduct(Product product) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                 "insert into Product (Name, Price) values (?, ?);"
             )
        ) {
            statement.setString(1, product.name());
            statement.setLong(2, product.price());

            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Fail insert new product.", e);

            throw new RuntimeException(e);
        }
    }

    public List<Product> findAllProducts() {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("select * from Product;")
        ) {
            return fetchListProductFromSql(statement);
        } catch (SQLException e) {
            logger.error("Fail find all products.", e);

            throw new RuntimeException(e);
        }
    }

    public Product maxProductByPrice() {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                 "select * from Product order by price desc limit 1;"
             )
        ) {
            List<Product> productList = fetchListProductFromSql(statement);

            assert productList.size() == 1;

            return productList.get(0);
        } catch (SQLException e) {
            logger.error("Fail find max product by price.", e);

            throw new RuntimeException(e);
        }
    }

    public Product minProductByPrice() {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                 "select * from Product order by price limit 1;"
             )
        ) {
            List<Product> productList = fetchListProductFromSql(statement);

            assert productList.size() == 1;

            return productList.get(0);
        } catch (SQLException e) {
            logger.error("Fail find min product by price.", e);

            throw new RuntimeException(e);
        }
    }

    public int countProducts() {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("select count(*) from Product;");
        ) {
            resultSet.next();
            return resultSet.getInt(1);
        } catch (SQLException e) {
            logger.error("Fail counting products.", e);

            throw new RuntimeException(e);
        }
    }

    public long summaryPrice() {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("select sum(price) from Product;")
        ) {
            resultSet.next();
            return resultSet.getLong(1);
        } catch (SQLException e) {
            logger.error("Fail summing products.", e);

            throw new RuntimeException(e);
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
