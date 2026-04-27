package org.example.model;

public class Customer extends Person {

    private int customerId;
    private String company;
    private Integer supportRepId;

    public Customer() {}

    public Customer(int customerId, String firstName, String lastName, String company,
                    String address, String city, String state, String country,
                    String postalCode, String phone, String fax, String email,
                    Integer supportRepId) {
        super(firstName, lastName, address, city, state, country, postalCode, phone, fax, email);
        this.customerId   = customerId;
        this.company      = company;
        this.supportRepId = supportRepId;
    }

    public int getCustomerId()                { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public String getCompany()                { return company; }
    public void setCompany(String company)    { this.company = company; }

    public Integer getSupportRepId()          { return supportRepId; }
    public void setSupportRepId(Integer id)   { this.supportRepId = id; }

    @Override
    public String toString() {
        return getFirstName() + " " + getLastName();
    }
}
