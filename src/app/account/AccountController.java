package app.account;


import app.Entities.Account;
import app.Entities.Transaction;
import app.Main;
import app.db.DB;
import app.login.LoginController;
import app.transaction.TransactionController;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

public class AccountController {

    @FXML
    VBox transactionBox;
    @FXML
    BorderPane bp;
    @FXML
    VBox choiceBox;
    @FXML
    VBox transferBox;
    private Account currentAccount = null;
    @FXML
    private ChoiceBox<Account> fromAccount = new ChoiceBox<>();
    @FXML
    private ChoiceBox<Account> toAccount = new ChoiceBox<>();
    @FXML
    private ChoiceBox<Account> myAccounts = new ChoiceBox<>();
    @FXML
    private TextField amount;
    @FXML
    private Label accountLabel;
    @FXML
    VBox cardButtonBox;
    @FXML
    TextField reciver;
    @FXML
    TextField amountToSend;

    public void setAccount(Account account) {
        currentAccount = account;
    }

    @FXML
    private void initialize() {
        Platform.runLater(this::showLabel);
    }

    @FXML
    void showPayWithCard() {
        if (currentAccount.getAccountType().equals("Card Account")) {
            Button button = new Button("Kortköp");
            cardButtonBox.getChildren().add(button);
            button.setOnAction(event -> {
                payWithCard();
            });
        }
        fillCBwithAccounts();
    }


    void loadTenTransactions() {
        transactionBox.getChildren().clear();
        List<Transaction> transactions = DB.getTransactions(currentAccount.getAccountNumber());
        displayTenTransactions(transactions);
    }

    void loadAllTransactions() {
        transactionBox.getChildren().clear();
        List<Transaction> transactions = DB.getTransactions(currentAccount.getAccountNumber());
        displayAllTransactions(transactions);
    }

    void displayTenTransactions(List<Transaction> transactions) {
        // For every transactcontroller.setTransaction(transacgtions);ion, do the followin:
        //  for (Transaction transaction : transactions)
        for (int i = 0; i < 10; i++)
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/transaction/transaction.fxml"));
                Parent fxmlInstance = loader.load();
                Scene scene = new Scene(fxmlInstance);
                TransactionController controller = loader.getController();
                controller.setTransaction(transactions.get(transactions.size() - (i + 1)));
                transactionBox.getChildren().add(scene.getRoot());
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    void displayAllTransactions(List<Transaction> transactions) {
        for (Transaction transaction : transactions) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/transaction/transaction.fxml"));
                Parent fxmlInstance = loader.load();
                Scene scene = new Scene(fxmlInstance);
                TransactionController controller = loader.getController();
                controller.setTransaction(transaction);
                transactionBox.getChildren().add(scene.getRoot());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @FXML
    void backToHome() {
        try {
            Parent bla = FXMLLoader.load(getClass().getResource("/app/home/home.fxml"));
            Scene scene = new Scene(bla, 800, 600);
            Main.stage.setScene(scene);
            Main.stage.show();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    @FXML
    private void fillTransferOtherAccount() {
        List<Account> results = null;
        myAccounts.getItems().clear();
        results = DB.getAccounts(LoginController.getUser().getSocialNumber());
        results.forEach(account -> {
            myAccounts.getItems().add(account);
        });
        Label from = new Label("Från");
        Label to = new Label("Till");
        transferBox.getChildren().add(from);
        transferBox.getChildren().add(myAccounts);
        transferBox.getChildren().add(to);
        reciver = new TextField("Mottagare");
        transferBox.getChildren().add(reciver);
        amountToSend = new TextField("Summa");
        Button button = new Button("Överför");
        transferBox.getChildren().add(amountToSend);
        transferBox.getChildren().add(button);
        button.setOnAction(event -> {
            makeTransferOtherAccount();
        });
    }

    @FXML
    private void fillCBwithAccounts() {
        List<Account> results = null;
        fromAccount.getItems().clear();
        toAccount.getItems().clear();
        results = DB.getAccounts(LoginController.getUser().getSocialNumber());
        results.forEach(account -> {
            fromAccount.getItems().add(account);
            toAccount.getItems().add(account);
        });
        Label from = new Label("Från");
        Label to = new Label("Till");
        choiceBox.getChildren().add(from);
        choiceBox.getChildren().add(fromAccount);
        choiceBox.getChildren().add(to);
        choiceBox.getChildren().add(toAccount);
        amount = new TextField("Summa");
        Button button = new Button("Överför");
        choiceBox.getChildren().add(amount);
        choiceBox.getChildren().add(button);
        button.setOnAction(event -> {
            makeTransfer();
        });
        fillTransferOtherAccount();
    }

    @FXML
    private void showLabel() {
        accountLabel.setText("Konto: " + currentAccount.getAccountName());
        showPayWithCard();
    }

    @FXML
    private void clearCB() {
        cardButtonBox.getChildren().clear();
        choiceBox.getChildren().clear();
        transferBox.getChildren().clear();
        initialize();
    }


    @FXML
    private void makeTransfer() {
        String stringAmount = amount.getText();
        String message = "Transfer Own Account";
        float f = Float.parseFloat(stringAmount);
        if (checkAccountBalance(f)) {
            DB.transferMoney(fromAccount.getValue().getAccountNumber(), toAccount.getValue().getAccountNumber(), f, message);
        } else {
            System.out.println("Not enough money");
        }
        clearCB();
    }

    @FXML
    private void makeTransferOtherAccount() {
        String message = "Transfer Other Account";
        String stringAmount = amountToSend.getText();
        float f = Float.parseFloat(stringAmount);
        String reciverText = reciver.getText();
        long l = Long.parseLong(reciverText);
        if (checkAccountBalanceOtherAccount(f)) {
            DB.transferMoney(myAccounts.getValue().getAccountNumber(), l, f, message);
        } else {
            System.out.println("Not enough money");
        }
        clearCB();
    }


    @FXML
    void payWithCard() {
        float snusPrice = 32f;
        if (currentAccount.getAccountBalance() > snusPrice) {
            DB.makeCardPayment(currentAccount.getAccountNumber());
        } else {
            System.out.println("Error");
        }
        clearCB();
    }

    private boolean checkAccountBalance(float amountToSend) {
        return (fromAccount.getValue().getAccountBalance() >= amountToSend);
    }

    private boolean checkAccountBalanceOtherAccount(float amountToSend) {
        return (myAccounts.getValue().getAccountBalance() >= amountToSend);
    }

    @FXML
    void clickLoadTransactions(Event e) {
        loadTenTransactions();
    }

    @FXML
    void clickAllTransactions(Event e) {
        loadAllTransactions();
    }
}
