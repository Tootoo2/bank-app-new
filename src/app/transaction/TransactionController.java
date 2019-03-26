package app.transaction;


import app.Entities.Transaction;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class TransactionController {

    @FXML Label message;
    @FXML Label amount;
    @FXML Label date;

    @FXML
    private void initialize(){
        System.out.println("initialize transaction");
    }

    public void setTransaction(Transaction transaction) {
        message.setText(transaction.getMessage());
        amount.setText(""+transaction.getAmount());
        date.setText(""+transaction.getDate());
        System.out.println("hello im here");
        // etc
    }
}
