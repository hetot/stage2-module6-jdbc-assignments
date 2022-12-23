package jdbc;

import javax.sql.DataSource;

import lombok.Getter;
import lombok.Setter;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

@Getter
@Setter
public class CustomDataSource implements DataSource {
    private static volatile CustomDataSource instance;
    private static final String filePathConnectionDB = "src/main/resources/app.properties";
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

            try (InputStream input = new FileInputStream(filePathConnectionDB)) {
                Properties prop = new Properties();
                prop.load(input);
                tmp_driver = prop.getProperty("postgres.driver");
                tmp_url = prop.getProperty("postgres.url");
                tmp_name = prop.getProperty("postgres.name");
                tmp_password = prop.getProperty("postgres.password");
                instance = new CustomDataSource(tmp_driver, tmp_url, tmp_password, tmp_name);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return instance;
    }

    @Override
    public Connection getConnection() throws SQLException {
        CustomConnector connector = new CustomConnector();
        return connector.getConnection(this.url, this.name, this.password);
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return getConnection();
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {

    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {

    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }
}
