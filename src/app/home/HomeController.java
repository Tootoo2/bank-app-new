package app.home;

import app.Entities.Account;
import app.Entities.User;
import app.Main;
import app.account.AccountController;
import app.db.DB;
import app.login.LoginController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;

import java.io.IOException;
import java.util.List;

public class HomeController {
    public Label nameLabel;

    List<Account> userAccounts = null;

    @FXML
    VBox accountsBox;
    @FXML
    TextField accountName;
    @FXML
    VBox removeVbox;
    @FXML
    private ChoiceBox<Account> cbRemoveAcc = new ChoiceBox<>();
    @FXML
    private ChoiceBox<Account> cbChangeName = new ChoiceBox<>();
    @FXML
    VBox nameChangeBox;
    //private Object Account;
    private User currentUser = null;

    @FXML
    void initialize() {
        currentUser = LoginController.getUser();
        nameLabel.setText("Välkommen " + LoginController.getUser().getName());
        generateAccounts();
        System.out.println("initialize home");
    }

    @FXML
    void generateAccounts() {

        userAccounts = (List<Account>) DB.getAccounts(currentUser.getSocialNumber());
        userAccounts.forEach(account -> {
            // Account = account;
            Button accountBtn = new Button("" + account.getAccountName());
            accountBtn.setMinSize(500, 40);
            accountsBox.getChildren().add(accountBtn);
            System.out.println("generating accounts");
            accountBtn.setOnAction(event -> {
                try {
                    System.out.println("I'm inside the try of generate account");
                    goToAccount(account.getAccountNumber());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        });
        removeAccount();
    }

    @FXML
    private void clearAccounts() {
        accountsBox.getChildren().clear();
        removeVbox.getChildren().clear();
        nameChangeBox.getChildren().clear();
        initialize();
    }

    @FXML
    private void removeAccount() {
        cbRemoveAcc.getItems().clear();
        userAccounts = (List<Account>) DB.getAccounts(currentUser.getSocialNumber());
        userAccounts.forEach(account -> {
            if (account.getAccountType().equals("Savings Account")){
            cbRemoveAcc.getItems().add(account);}
        });
        Label label = new Label("Ta bort sparkonto");
        Button button = new Button("Ta bort");
        removeVbox.getChildren().add(label);
        removeVbox.getChildren().add(cbRemoveAcc);
        removeVbox.getChildren().add(button);
        button.setOnAction(event -> {
            DB.deleteSavingsAccount(cbRemoveAcc.getValue().getAccountName(), currentUser.getSocialNumber());
            clearAccounts();
        });
        changeAccountName();
    }

    @FXML
    private void changeAccountName() {
        cbChangeName.getItems().clear();
        userAccounts = (List<Account>) DB.getAccounts(currentUser.getSocialNumber());
        userAccounts.forEach(account -> {
            if (account.getAccountType().equals("Savings Account")){
            cbChangeName.getItems().add(account);}
        });
        Label label = new Label("Välj konto för namnbyte");
        Button button = new Button("Genomför namnbyte");
        TextField textField = new TextField("Nytt namn");
        nameChangeBox.getChildren().add(label);
        nameChangeBox.getChildren().add(cbChangeName);
        nameChangeBox.getChildren().add(textField);
        nameChangeBox.getChildren().add(button);
        button.setOnAction(event -> {
            DB.changeAccountName(cbChangeName.getValue().getAccountName(), textField.getText(), currentUser.getSocialNumber());
            clearAccounts();
        });
    }

    @FXML
    void createAccount() {
        boolean nameExists = false;
        userAccounts = (List<Account>) DB.getAccounts(currentUser.getSocialNumber());
        for (Account account : userAccounts) {
            if (account.getAccountName().equals(accountName.getText())) {
                System.out.println("Name Exists");
                nameExists = true;
            }
        }
        if (!nameExists) {
            DB.createAccount(accountName.getText(), currentUser.getSocialNumber());
        }
        clearAccounts();
    }


    @FXML
    void goToAccount(long number) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/account/account.fxml"));
        Parent fxmlInstance = loader.load();
        Scene scene = new Scene(fxmlInstance, 800, 600);
        AccountController controller = loader.getController();
        controller.setAccount(DB.getAccount(number));
        Main.stage.setScene(scene);
        Main.stage.show();
    }


}