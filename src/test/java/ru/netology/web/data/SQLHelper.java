package ru.netology.web.data;

import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLHelper {
    private static final QueryRunner QUERY_RUNNER = new QueryRunner();

    private SQLHelper() {
    }

    private static Connection getConn() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/app", "app", "pass");
    }

    @SneakyThrows
    public static void cleanDatabase() {
        try (var conn = getConn()) {
            QUERY_RUNNER.execute(conn, "DELETE FROM credit_request_entity");
            QUERY_RUNNER.execute(conn, "DELETE FROM order_entity");
            QUERY_RUNNER.execute(conn, "DELETE FROM payment_entity");
        }
    }

    @SneakyThrows
    public static String getCardPayment() {
        var codeSQL = "SELECT status FROM payment_entity ORDER BY created DESC LIMIT 1;";
        try(var conn = getConn()) {
            return QUERY_RUNNER.query(conn, codeSQL, new ScalarHandler<>());
        }
    }

    @SneakyThrows
    public static String getCreditPayment() {
        var codeSQL = "SELECT status FROM credit_request_entity ORDER BY created DESC LIMIT 1;";
        try(var conn = getConn()) {
            return QUERY_RUNNER.query(conn, codeSQL, new ScalarHandler<>());
        }
    }


}
