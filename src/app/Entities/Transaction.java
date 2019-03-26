package app.Entities;


import app.annotations.Column;

import java.sql.Timestamp;
import java.time.LocalDate;

public class Transaction {
    @Column
    private long id;
    @Column
    private String message;
    @Column
    private float amount;
    @Column
    private Timestamp date;
    @Column
    private long receiver;
    @Column
    private long sender;

    public String getMessage() { return message; }
    public float getAmount() { return amount; }
    public Timestamp getDate() { return date; }
    public long getId() { return id; }
    public long getReceiver() { return receiver; }
    public long getSender() { return sender; }


}
