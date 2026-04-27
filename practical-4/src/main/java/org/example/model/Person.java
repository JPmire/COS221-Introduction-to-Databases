package org.example.model;

public abstract class Person {

    private String firstName;
    private String lastName;
    private String address;
    private String city;
    private String state;
    private String country;
    private String postalCode;
    private String phone;
    private String fax;
    private String email;

    public Person() {}

    public Person(String firstName, String lastName, String address, String city,
                  String state, String country, String postalCode,
                  String phone, String fax, String email) {
        this.firstName  = firstName;
        this.lastName   = lastName;
        this.address    = address;
        this.city       = city;
        this.state      = state;
        this.country    = country;
        this.postalCode = postalCode;
        this.phone      = phone;
        this.fax        = fax;
        this.email      = email;
    }

    public String getFirstName()  { return firstName; }
    public void setFirstName(String firstName)  { this.firstName = firstName; }

    public String getLastName()   { return lastName; }
    public void setLastName(String lastName)   { this.lastName = lastName; }

    public String getAddress()    { return address; }
    public void setAddress(String address)    { this.address = address; }

    public String getCity()       { return city; }
    public void setCity(String city)       { this.city = city; }

    public String getState()      { return state; }
    public void setState(String state)      { this.state = state; }

    public String getCountry()    { return country; }
    public void setCountry(String country)    { this.country = country; }

    public String getPostalCode() { return postalCode; }
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }

    public String getPhone()      { return phone; }
    public void setPhone(String phone)      { this.phone = phone; }

    public String getFax()        { return fax; }
    public void setFax(String fax)        { this.fax = fax; }

    public String getEmail()      { return email; }
    public void setEmail(String email)      { this.email = email; }
}
