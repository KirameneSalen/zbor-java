package repository.jdbc;

import companie.persistence.IZborRepository;
import companie.persistence.ValidationException;
import companie.model.Zbor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Component
public class ZborRepositoryJDBC implements IZborRepository {
    private final JdbcUtils dbUtils;
    private static final Logger logger = LogManager.getLogger(ZborRepositoryJDBC.class);

    public ZborRepositoryJDBC(Properties props) {
        logger.info("Initializing ZborRepositoryJDBC with properties: {}", props);
        dbUtils = new JdbcUtils(props);
    }
    @Override
    public Iterable<Zbor> getAll() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Zbor> zborList = new ArrayList<>();
        try(PreparedStatement preStmt = con.prepareStatement("select * from zbor")){
            try(ResultSet result = preStmt.executeQuery()){
                while(result.next()){
                    int id = result.getInt("id");
                    String destinatie = result.getString("destinatie");
                    String dataPlecarii = result.getString("data_plecarii");
                    String oraPlecarii = result.getString("ora_plecarii");
                    String aeroport = result.getString("aeroport");
                    int nrLocuriDisponibile = result.getInt("nr_locuri_disponibile");
                    Zbor zbor = new Zbor(id, destinatie, dataPlecarii, oraPlecarii, aeroport, nrLocuriDisponibile);
                    zborList.add(zbor);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
        }
        return zborList;
    }

    @Override
    public Zbor add(Zbor entity) throws ValidationException {
        logger.traceEntry("saving zbor {}", entity);
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStmt = con.prepareStatement("select * from zbor where id = ?")){
            preStmt.setInt(1, entity.getId() != null ? entity.getId() : 0);
            try(ResultSet result = preStmt.executeQuery()){
                if(result.next()){
                    logger.traceExit("Did not save {}, zbor already exists", entity);
                    return entity;
                }
            }
        } catch (SQLException e) {
            logger.error(e);
        }
        try(PreparedStatement preStmt = con.prepareStatement("insert into zbor (destinatie, data_plecarii, ora_plecarii, aeroport, nr_locuri_disponibile) values (?, ?, ?, ?, ?)")){
            preStmt.setString(1, entity.getDestinatie());
            preStmt.setString(2, entity.getDataPlecarii());
            preStmt.setString(3, entity.getOraPlecarii());
            preStmt.setString(4, entity.getAeroport());
            preStmt.setInt(5, entity.getNrLocuriDisponibile());
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
    public Zbor update(Zbor entity) throws ValidationException {
        logger.traceEntry("updating zbor {}", entity);
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStmt = con.prepareStatement("update zbor set destinatie=?, data_plecarii=?, ora_plecarii=?, aeroport=?, nr_locuri_disponibile=? where id = ?")){
            preStmt.setString(1, entity.getDestinatie());
            preStmt.setString(2, entity.getDataPlecarii());
            preStmt.setString(3, entity.getOraPlecarii());
            preStmt.setString(4, entity.getAeroport());
            preStmt.setInt(5, entity.getNrLocuriDisponibile());
            preStmt.setInt(6, entity.getId());
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
    public Zbor findOne(int id) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Zbor> zborList = new ArrayList<>();
        try(PreparedStatement preStmt = con.prepareStatement("select * from zbor where id=?")){
            preStmt.setInt(1, id);
            try(ResultSet result = preStmt.executeQuery()){
                if(result.next()){
                    String destinatie = result.getString("destinatie");
                    String dataPlecarii = result.getString("data_plecarii");
                    String oraPlecarii = result.getString("ora_plecarii");
                    String aeroport = result.getString("aeroport");
                    int nrLocuriDisponibile = result.getInt("nr_locuri_disponibile");
                    Zbor zbor = new Zbor(id, destinatie, dataPlecarii, oraPlecarii, aeroport, nrLocuriDisponibile);
                    logger.traceExit("Found zbor {}", zbor);
                    return zbor;
                }
            }
        } catch (SQLException e) {
            logger.error(e);
        }
        logger.traceExit("Did find any zbor with id {}", id);
        return null;
    }

    @Override
    public Iterable<Zbor> filter(String destinatie, String dataPlecarii) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        boolean isDestinatieEmpty = destinatie == null || destinatie.isEmpty();
        boolean isDataPlecariiEmpty = dataPlecarii == null || dataPlecarii.isEmpty();
        if(isDestinatieEmpty || isDataPlecariiEmpty){
            return getAll();
        }
        List<Zbor> zborList = new ArrayList<>();
        try(PreparedStatement preStmt = con.prepareStatement("select * from zbor where destinatie=? and data_plecarii=?")){
            preStmt.setString(1, destinatie);
            preStmt.setString(2, dataPlecarii);
            try(ResultSet result = preStmt.executeQuery()){
                while(result.next()){
                    int id = result.getInt("id");
                    String oraPlecarii = result.getString("ora_plecarii");
                    String aeroport = result.getString("aeroport");
                    int nrLocuriDisponibile = result.getInt("nr_locuri_disponibile");
                    Zbor zbor = new Zbor(id, destinatie, dataPlecarii, oraPlecarii, aeroport, nrLocuriDisponibile);
                    zborList.add(zbor);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
        }
        return zborList;
    }
}