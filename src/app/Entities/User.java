package app.Entities;


import app.annotations.Column;

public class User {
    @Column("id")
    private long id;
    @Column("social_number")
    private String socialNumber;
    @Column("last_name")
    private String lastName;
    @Column ("first_name")
    private String name;
    @Column
    private String password;
    @Column
    private String mail;

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    public String getSocialNumber() {
        return socialNumber;
    }
}
