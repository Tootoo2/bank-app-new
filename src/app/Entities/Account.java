package app.Entities;

import app.annotations.Column;

public class Account {
    @Column("account_number")
    private long accountNumber;
    @Column("account_name")
    private String accountName;
    @Column("balance")
    private float accountBalance;
    @Column("account_type")
    private String accountType;
    @Column("account_owner")
    private String accountOwner;

    public long getAccountNumber() {
        return accountNumber;
    }

    public float getAccountBalance() {
        return accountBalance;
    }

    public String getAccountName(){
        return accountName;
    }

    public String getAccountType() {
        return accountType;
    }

    @Override
    public String toString() {
        return accountName + " - " +
                 + accountBalance;
    }
}
