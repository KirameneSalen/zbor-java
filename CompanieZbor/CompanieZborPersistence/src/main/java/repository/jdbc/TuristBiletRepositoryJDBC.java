package repository.jdbc;

import companie.persistence.ITuristBiletRepository;
import companie.persistence.ValidationException;
import companie.model.Bilet;
import companie.model.Turist;
import companie.model.TuristBilet;
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

public class TuristBiletRepositoryJDBC implements ITuristBiletRepository {
    private final JdbcUtils dbUtils;
    private static final Logger logger = LogManager.getLogger(TuristBiletRepositoryJDBC.class);

    public TuristBiletRepositoryJDBC(Properties props) {
        logger.info("Initializing UserRepositoryJDBC with properties: {}", props);
        dbUtils = new JdbcUtils(props);
    }
    @Override
    public Iterable<TuristBilet> getAll() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        List<TuristBilet> tourists = new ArrayList<>();
        try(PreparedStatement preStmt = con.prepareStatement("select turist_bilet.id as turist_bilet_id, turist.id as turist_id, turist.name, bilet.id as bilet_id, zbor.id as zbor_id, zbor.destinatie, zbor.data_plecarii, zbor.ora_plecarii, zbor.aeroport, zbor.nr_locuri_disponibile from turist_bilet inner join bilet on turist_bilet.id_bilet = bilet.id inner join zbor on bilet.id_zbor = zbor.id inner join turist on turist_bilet.id_turist = turist.id")){
            try(ResultSet result = preStmt.executeQuery()){
                while(result.next()){
                    int idTurist = result.getInt("turist_id");
                    String name = result.getString("name");
                    Turist turist = new Turist(idTurist, name);
                    int idZbor = result.getInt("zbor_id");
                    String destinatie = result.getString("destinatie");
                    String dataPlecarii = result.getString("data_plecarii");
                    String oraPlecarii = result.getString("ora_plecarii");
                    String aeroport = result.getString("aeroport");
                    int nrLocuriDisponibile = result.getInt("nr_locuri_disponibile");
                    Zbor zbor = new Zbor(idZbor, destinatie, dataPlecarii, oraPlecarii, aeroport, nrLocuriDisponibile);
                    int idTuristBilet = result.getInt("turist_bilet_id");
                    int idBilet = result.getInt("bilet_id");
                    Bilet bilet = new Bilet(idBilet, zbor);
                    TuristBilet turistBilet = new TuristBilet(idTuristBilet, turist, bilet);
                    tourists.add(turistBilet);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
        }
        return tourists;
    }

    @Override
    public TuristBilet add(TuristBilet entity) throws ValidationException {
        logger.traceEntry("saving turistBilet {}", entity);
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStmt = con.prepareStatement("select * from turist_bilet where id = ?")){
            preStmt.setInt(1, entity.getId() != null ? entity.getId() : 0);
            try(ResultSet result = preStmt.executeQuery()){
                if(result.next()){
                    logger.traceExit("Did not save {}, turistBilet already exists", entity);
                    return entity;
                }
            }
        } catch (SQLException e) {
            logger.error(e);
        }
        try(PreparedStatement preStmt = con.prepareStatement("insert into turist_bilet (id_turist, id_bilet) values (?, ?)")){
            preStmt.setInt(1, entity.getTurist().getId());
            preStmt.setInt(2, entity.getBilet().getId());
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
    public TuristBilet update(TuristBilet entity) throws ValidationException {
        logger.traceEntry("updating turistBilet {}", entity);
        Connection con = dbUtils.getConnection();
        try(PreparedStatement preStmt = con.prepareStatement("update turist_bilet set id_turist=?, id_bilet=? where id = ?")){
            preStmt.setInt(1, entity.getTurist().getId());
            preStmt.setInt(2, entity.getBilet().getId());
            preStmt.setInt(3, entity.getId());
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
