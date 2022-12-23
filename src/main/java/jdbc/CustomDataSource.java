package jdbc;

import javax.sql.DataSource;

import lombok.Getter;
import lombok.Setter;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;
import java.util.logging.Logger;

@Getter
@Setter
public class CustomDataSource implements DataSource {
    private static volatile CustomDataSource instance;
    private static final String filePathConnectionDB = "app.properties";
    private final String driver;
    private final String url;
    private final String name;
    private final String password;

    private CustomDataSource(String driver, String url, String password, String name) {
        this.driver = driver;
        this.url = url;
        this.name = name;
        this.password = password;
    }

    public static CustomDataSource getInstance() {
        if (instance == null) {
            String tmp_driver;
            String tmp_url;
            String tmp_name;
            String tmp_password;

            try (InputStream input = CustomDataSource.class.getClassLoader().getResourceAsStream(filePathConnectionDB)) {
                Properties prop = new Properties();
                prop.load(input);
                tmp_driver = prop.getProperty("postgres.driver");
                tmp_url = prop.getProperty("postgres.url");
                tmp_name = prop.getProperty("postgres.name");
                tmp_password = prop.getProperty("postgres.password");
                instance = new CustomDataSource(tmp_driver, tmp_url, tmp_password, tmp_name);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return instance;
    }

    @Override
    public Connection getConnection() {
        try {
            Class.forName(this.driver);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        try (Connection connection = DriverManager.getConnection(this.url, this.name, this.password)) {
            return connection;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Connection getConnection(String username, String password) {
        try {
            Class.forName(this.driver);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        try (Connection connection = DriverManager.getConnection(this.url, username, password)) {
            return connection;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PrintWriter getLogWriter() {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) {

    }

    @Override
    public void setLoginTimeout(int seconds) {

    }

    @Override
    public int getLoginTimeout() {
        return 0;
    }

    @Override
    public Logger getParentLogger() {
        return null;
    }

    @Override
    public <T> T unwrap(Class<T> iface) {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) {
        return false;
    }
}
