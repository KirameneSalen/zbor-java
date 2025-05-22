package repository.jdbc;

import companie.persistence.IUserRepository;
import companie.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class UserRepositoryJDBC implements IUserRepository {
    private final JdbcUtils dbUtils;
    private static final Logger logger = LogManager.getLogger(UserRepositoryJDBC.class);

    public UserRepositoryJDBC(Properties props) {
        logger.info("Initializing UserRepositoryJDBC with properties: {}", props);
        dbUtils = new JdbcUtils(props);

    }

    @Override
    public Iterable<User> getAll() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<User> users = new ArrayList<>();
        try(PreparedStatement preStmt = con.prepareStatement("select * from user")){
            try(ResultSet result = preStmt.executeQuery()){
                while(result.next()){
                    int id = result.getInt("id");
                    String username = result.getString("username");
                    String password = result.getString("password");
                    User user = new User(id, username, password);
                    users.add(user);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
        }
        return users;
    }

    @Override
    public User add(User entity) {
        logger.traceEntry("saving user {}", entity);
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStmt = con.prepareStatement("select * from user where id = ? or username = ?")){
            preStmt.setInt(1, entity.getId() != null ? entity.getId() : 0);
            preStmt.setString(2, entity.getUsername());
            try(ResultSet result = preStmt.executeQuery()){
                if(result.next()){
                    logger.traceExit("Did not save {}, user already exists", entity);
                    return entity;
                }
            }
        } catch (SQLException e) {
            logger.error(e);
        }
        try(PreparedStatement preStmt = con.prepareStatement("insert into user (username, password) values (?,?)")){
            preStmt.setString(1, entity.getUsername());
            preStmt.setString(2, entity.getPassword());
            int result = preStmt.executeUpdate();
            ResultSet generatedKeys = preStmt.getGeneratedKeys();
            if(generatedKeys.next()){
                entity.setId(generatedKeys.getInt(1));
            }
            logger.trace("Saved {} instances", result);
        } catch (SQLException e) {
            logger.error(e);
        }
        logger.traceExit();
        return entity;
    }

    @Override
    public User update(User entity) {
        logger.traceEntry("updating user {}", entity);
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStmt = con.prepareStatement("update user set password=? where username = ?")){
            preStmt.setString(1, entity.getPassword());
            preStmt.setString(2, entity.getUsername());
            int result = preStmt.executeUpdate();
            logger.trace("Updated {} instances", result);
            return entity;
        } catch (SQLException e) {
            logger.error(e);
        }
        logger.traceExit();
        return null;
    }

    @Override
    public User findByUsername(String username) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<User> users = new ArrayList<>();
        try(PreparedStatement preStmt = con.prepareStatement("select * from user where username=?")){
            preStmt.setString(1, username);
            try(ResultSet result = preStmt.executeQuery()){
                if(result.next()){
                    int id = result.getInt("id");
                    String password = result.getString("password");
                    return new User(id, username, password);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
        }
        return null;
    }
}
