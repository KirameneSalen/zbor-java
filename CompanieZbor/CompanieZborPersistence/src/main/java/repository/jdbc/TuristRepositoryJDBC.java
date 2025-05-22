package repository.jdbc;

import companie.persistence.ITuristRepository;
import companie.persistence.ValidationException;
import companie.model.Turist;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class TuristRepositoryJDBC implements ITuristRepository {
    private final JdbcUtils dbUtils;
    private static final Logger logger = LogManager.getLogger(TuristRepositoryJDBC.class);

    public TuristRepositoryJDBC(Properties props) {
        logger.info("Initializing TuristRepositoryJDBC with properties: {}", props);
        dbUtils = new JdbcUtils(props);
    }

    @Override
    public Iterable<Turist> getAll() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Turist> tourists = new ArrayList<>();
        try(PreparedStatement preStmt = con.prepareStatement("select * from turist")){
            try(ResultSet result = preStmt.executeQuery()){
                while(result.next()){
                    int id = result.getInt("id");
                    String name = result.getString("name");
                    Turist turist = new Turist(id, name);
                    tourists.add(turist);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
        }
        return tourists;
    }

    @Override
    public Turist add(Turist entity) throws ValidationException {
        logger.traceEntry("saving tourist {}", entity);
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStmt = con.prepareStatement("select * from turist where id = ? or name = ?")){
            preStmt.setInt(1, entity.getId() != null ? entity.getId() : 0);
            preStmt.setString(2, entity.getNume());
            try(ResultSet result = preStmt.executeQuery()){
                if(result.next()){
                    logger.traceExit("Did not save {}, tourist already exists", entity);
                    return entity;
                }
            }
        } catch (SQLException e) {
            logger.error(e);
        }
        try(PreparedStatement preStmt = con.prepareStatement("insert into turist (name) values (?)")){
            preStmt.setString(1, entity.getNume());
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
    public Turist update(Turist entity) throws ValidationException {
        logger.traceEntry("updating tourist {}", entity);
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStmt = con.prepareStatement("update turist set name=? where id = ?")){
            preStmt.setString(1, entity.getNume());
            preStmt.setInt(2, entity.getId());
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
    public Turist findOne(String nume) {
        logger.traceEntry("finding tourist {}", nume);
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStmt = con.prepareStatement("select * from turist where name=?")){
            preStmt.setString(1, nume);
            try(ResultSet result = preStmt.executeQuery()){
                if (result.next()){
                    int id = result.getInt("id");
                    Turist turist = new Turist(id, nume);
                    logger.traceExit("Found {},", turist);
                    return turist;
                }
            }
        } catch (SQLException e) {
            logger.error(e);
        }
        logger.traceExit("No tourist found with the name {}", nume);
        return null;
    }

    @Override
    public Turist findById(int id) {
        logger.traceEntry("finding tourist {}", id);
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStmt = con.prepareStatement("select * from turist where id=?")){
            preStmt.setInt(1, id);
            try(ResultSet result = preStmt.executeQuery()){
                if (result.next()){
                    String nume = result.getString("name");
                    Turist turist = new Turist(id, nume);
                    logger.traceExit("Found {},", turist);
                    return turist;
                }
            }
        } catch (SQLException e) {
            logger.error(e);
        }
        logger.traceExit("No tourist found with the id {}", id);
        return null;
    }
}
