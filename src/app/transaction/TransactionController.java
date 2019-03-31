package app.transaction;


import app.Entities.Account;
import app.Entities.Transaction;
import app.db.DB;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class TransactionController {

    @FXML Label message;
    @FXML Label amount;
    @FXML Label date;
    private Account currentAccount;

    @FXML
    private void initialize(){}

    public void setTransaction(Transaction transaction) {
        message.setText(transaction.getMessage());
        amount.setText(senderOrReceiver(transaction)+transaction.getAmount());
        date.setText(""+transaction.getDate());
    }

    public void setCurrentAccount(Account account){
        currentAccount=account;
    }

    public String senderOrReceiver(Transaction transaction){
        if(currentAccount.getAccountNumber()==transaction.getSender()){
            return "-";
        }
        else{
            return "+";
        }
    }

}
