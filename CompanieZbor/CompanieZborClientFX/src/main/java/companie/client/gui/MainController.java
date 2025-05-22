package companie.client.gui;

import companie.model.Entity;
import companie.model.Turist;
import companie.model.User;
import companie.model.Zbor;
import companie.persistence.ValidationException;
import companie.services.ICompanieObserver;
import companie.services.ICompanieServices;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class MainController implements Initializable, ICompanieObserver {
    @FXML
    private Button cumparaBileteButton;
    @FXML
    private TextField nrBileteTextField;
    @FXML
    private TextField oraPlecariiFilter;
    @FXML
    private DatePicker dataPlecariiFilter;
    @FXML
    private TableColumn<Zbor, String> aeroportColumn;
    @FXML
    private TableColumn<Zbor, String> destinatieColumn;
    @FXML
    private TableColumn<Zbor, String> dataPlecariiColumn;
    @FXML
    private TableColumn<Zbor, String> oraPlecariiColumn;
    @FXML
    private TableColumn<Zbor, Integer> nrLocuriDisponibileColumn;
    @FXML
    private TableView<Zbor> zborTable;
    @FXML
    private TableColumn<Zbor, Integer> idColumn;
    @FXML
    private Label loggedUserLabel;
    private ObservableList<Zbor> zborObservableList = FXCollections.observableArrayList();

    private ICompanieServices server;
    private User user;

    private static final Logger logger = LogManager.getLogger(MainController.class);

    public MainController() {
        logger.debug("Constructor MainController");
    }

    public MainController(ICompanieServices server) {
        this.server = server;
        logger.debug("Constructor MainController cu server param");
    }

    public void setServer(ICompanieServices server) {
        this.server = server;
        zborObservableList.clear();
        zborObservableList.addAll(server.getZboruri(null, null));
        zborTable.setItems(zborObservableList);
        Callback<TableView<Zbor>, TableRow<Zbor>> rowFactory = new Callback<>() {
            @Override
            public TableRow<Zbor> call(TableView<Zbor> param) {
                return new TableRow<>() {
                    @Override
                    protected void updateItem(Zbor item, boolean empty) {
                        super.updateItem(item, empty);
                        try {
                            if(item.getNrLocuriDisponibile() <= 0) {
                                setStyle("-fx-background-color: #ff8080;");
                            }
                            else {
                                setStyle("");
                            }
                        } catch (NullPointerException ignored) {
                        }
                    }
                };
            }
        };
        zborTable.setRowFactory(rowFactory);
        zborTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            cumparaBileteButton.setDisable(newValue == null);
        });
    }

    @Override
    public void biletCumparat(Zbor zbor, int nrLocuriCumparate) throws ValidationException {
        System.out.println("GUI " + user.getUsername().toUpperCase() + " Bilet cumparat");
        // big update
        try {
            Platform.runLater(() -> {
                for (int i = 0; i < zborObservableList.size(); i++) {
                    if (zborObservableList.get(i).getId().equals(zbor.getId())) {
                        zborObservableList.set(i, zbor);
                        break;
                    }
                }
                zborTable.refresh();
            });
        } catch (ValidationException e) {
            e.printStackTrace();
            Util.showWarning("Error!", "Error buying tickets", e.getMessage());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        aeroportColumn.setCellValueFactory(new PropertyValueFactory<>("aeroport"));
        destinatieColumn.setCellValueFactory(new PropertyValueFactory<>("destinatie"));
        dataPlecariiColumn.setCellValueFactory(new PropertyValueFactory<>("dataPlecarii"));
        oraPlecariiColumn.setCellValueFactory(new PropertyValueFactory<>("oraPlecarii"));
        nrLocuriDisponibileColumn.setCellValueFactory(new PropertyValueFactory<>("nrLocuriDisponibile"));
        logger.debug("END INIT!!!!!!!!!");
    }

    public void logout() {
        try {
            server.logout(user, this);
            loggedUserLabel.setText("");
        } catch (ValidationException e) {
            logger.error("Logout error {}", String.valueOf(e));
        }
        logger.debug("logout");
    }

    public void setUser(User crtUser) {
        user = crtUser;
        loggedUserLabel.setText(user.getUsername());
    }

    public void handleFiltreaza(ActionEvent actionEvent) {

    }

    public void stergeFiltru(ActionEvent actionEvent) {

    }

    public void handleCumparaBilete(ActionEvent actionEvent) {
        Zbor zbor = zborTable.getSelectionModel().getSelectedItem();
        String nrBileteString = nrBileteTextField.getText();
        int nrBilete = 0;
        try {
            nrBilete = Integer.parseInt(nrBileteString);
        } catch (NumberFormatException ignored) {

        }
        Turist[] lista = new Turist[nrBilete];
        server.cumparaBilet(zbor, nrBilete, lista, this);
    }
}
