package companie.client.gui;

import companie.model.User;
import companie.persistence.ValidationException;
import companie.services.ICompanieServices;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoginController{

    private ICompanieServices server;
    private MainController mainController;
    private User crtUser;

    private static final Logger logger = LogManager.getLogger(LoginController.class);
    @FXML
    TextField user;
    @FXML
    TextField password;

    Parent mainParent;

    public void setServer(ICompanieServices s){
        server=s;
    }


    public void setParent(Parent p){
        mainParent =p;
    }

    public void pressLogin(ActionEvent actionEvent) {
        //Parent root;
        String nume = user.getText();
        String passwd = password.getText();
        crtUser = new User(0, nume, passwd);


        try{
            User loggedInUser =  server.login(crtUser, mainController);
            setUser(loggedInUser);
            System.out.println("User succesfully logged in "+crtUser.toString());
            Stage stage=new Stage();
            stage.setTitle("Chat Window for " +crtUser.getUsername());
            stage.setScene(new Scene(mainParent));

            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    mainController.logout();
                    logger.debug("Closing application");
                    System.exit(0);
                }
            });

            stage.show();
            mainController.setServer(server);
            mainController.setUser(crtUser);
            ((Node)(actionEvent.getSource())).getScene().getWindow().hide();

        }   catch (ValidationException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("MPP chat");
            alert.setHeaderText("Authentication failure");
            alert.setContentText("Wrong username or password");
            alert.showAndWait();
        }


    }





    public void pressCancel(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void setUser(User user) {
        this.crtUser = user;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }
}
