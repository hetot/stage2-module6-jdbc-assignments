package jdbc;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SimpleJDBCRepository {

    private Connection connection = null;
    private PreparedStatement ps = null;
    private Statement st = null;

    private static final String createUserSQL = "insert into myusers(id, firstname, lastname, age) values(default, ?, ?, ?) returning id";
    private static final String updateUserSQL = "update myusers set firstname = ?, lastname = ?, age = ? where id = ?";
    private static final String deleteUser = "delete from myusers where id = ?";
    private static final String findUserByIdSQL = "select * from myusers where id = ?";
    private static final String findUserByNameSQL = "select * from myusers where name = ?";
    private static final String findAllUserSQL = "select * from myusers";

    public Long createUser(User user) throws SQLException {
        if (connection == null)
            connection = CustomDataSource.getInstance().getConnection();
        ps = connection.prepareStatement(createUserSQL);
        ps.setString(1, user.getFirstName());
        ps.setString(2, user.getLastName());
        ps.setInt(3, user.getAge());
        ResultSet rs = ps.executeQuery();
        rs.next();
        return rs.getLong(1);
    }

    public User findUserById(Long userId) throws SQLException {
        if (connection == null)
            connection = CustomDataSource.getInstance().getConnection();
        ps = connection.prepareStatement(findUserByIdSQL);
        ps.setLong(1, userId);
        ResultSet rs = ps.executeQuery();
        if (rs.next())
            return new User(rs.getLong(1), rs.getString(2), rs.getString(3), rs.getInt(4));
        return null;
    }

    public User findUserByName(String userName) throws SQLException {
        if (connection == null)
            connection = CustomDataSource.getInstance().getConnection();
        ps = connection.prepareStatement(findUserByNameSQL);
        ps.setString(1, userName);
        ResultSet rs = ps.executeQuery();
        if (rs.next())
            return new User(rs.getLong(1), rs.getString(2), rs.getString(3), rs.getInt(4));
        return null;
    }

    public List<User> findAllUser() throws SQLException {
        List<User> returnList = new ArrayList<>();
        if (connection == null)
            connection = CustomDataSource.getInstance().getConnection();
        ps = connection.prepareStatement(findAllUserSQL);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            returnList.add(new User(rs.getLong(1), rs.getString(2), rs.getString(3), rs.getInt(4)));
        }
        return returnList;
    }

    public User updateUser(User user) throws SQLException {
        if (connection == null)
            connection = CustomDataSource.getInstance().getConnection();
        ps = connection.prepareStatement(updateUserSQL);
        ps.setString(1, user.getFirstName());
        ps.setString(2, user.getLastName());
        ps.setInt(3, user.getAge());
        ps.setLong(4, user.getId());
        ps.executeUpdate();
        return user;
    }

    private void deleteUser(Long userId) throws SQLException {
        if (connection == null)
            connection = CustomDataSource.getInstance().getConnection();
        ps = connection.prepareStatement(deleteUser);
        ps.setLong(1, userId);
        ps.executeUpdate();
    }
}
