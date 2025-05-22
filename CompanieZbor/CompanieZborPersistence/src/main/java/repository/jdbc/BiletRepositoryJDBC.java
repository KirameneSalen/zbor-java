package repository.jdbc;

import companie.persistence.IBiletRepository;
import companie.persistence.ValidationException;
import companie.model.Bilet;
import companie.model.Zbor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class BiletRepositoryJDBC implements IBiletRepository {
    private final JdbcUtils dbUtils;
    private static final Logger logger = LogManager.getLogger(BiletRepositoryJDBC.class);

    public BiletRepositoryJDBC(Properties props) {
        logger.info("Initializing BiletRepositoryJDBC with properties: {}", props);
        dbUtils = new JdbcUtils(props);
    }
    @Override
    public Iterable<Bilet> getAll() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<Bilet> tourists = new ArrayList<>();
        try(PreparedStatement preStmt = con.prepareStatement("select bilet.id as bilet_id, zbor.id as zbor_id, bilet.id_zbor, zbor.destinatie, zbor.data_plecarii, zbor.ora_plecarii, zbor.aeroport, zbor.nr_locuri_disponibile from bilet join zbor on bilet.id_zbor = zbor.id")){
            try(ResultSet result = preStmt.executeQuery()){
                while(result.next()){
                    int idZbor = result.getInt("id_zbor");
                    String destinatie = result.getString("destinatie");
                    String dataPlecarii = result.getString("data_plecarii");
                    String oraPlecarii = result.getString("ora_plecarii");
                    String aeroport = result.getString("aeroport");
                    int nrLocuriDisponibile = result.getInt("nr_locuri_disponibile");
                    Zbor zbor = new Zbor(idZbor, destinatie, dataPlecarii, oraPlecarii, aeroport, nrLocuriDisponibile);
                    int idBilet = result.getInt("bilet_id");
                    Bilet bilet = new Bilet(idBilet, zbor);
                    tourists.add(bilet);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
        }
        return tourists;
    }

    @Override
    public Bilet add(Bilet entity) throws ValidationException {
        logger.traceEntry("saving bilet {}", entity);
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStmt = con.prepareStatement("select * from bilet where id = ?")){
            preStmt.setInt(1, entity.getId() != null ? entity.getId() : 0);
            try(ResultSet result = preStmt.executeQuery()){
                if(result.next()){
                    logger.traceExit("Did not save {}, bilet already exists", entity);
                    return entity;
                }
            }
        } catch (SQLException e) {
            logger.error(e);
        }
        try(PreparedStatement preStmt = con.prepareStatement("insert into bilet (id_zbor) values (?)")){
            preStmt.setInt(1, entity.getZbor().getId());
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
    public Bilet update(Bilet entity) throws ValidationException {
        logger.traceEntry("updating bilet {}", entity);
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStmt = con.prepareStatement("update bilet set id_zbor=? where id = ?")){
            preStmt.setInt(1, entity.getZbor().getId());
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
}
