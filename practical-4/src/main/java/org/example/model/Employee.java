package org.example.model;

import java.time.LocalDateTime;

public class Employee extends Person {

    private int employeeId;
    private String title;
    private Integer reportsTo;
    private LocalDateTime birthDate;
    private LocalDateTime hireDate;

    // Transient: resolved via self-join in EmployeeDAO
    private String supervisorName;
    // Transient: true if employee has at least one customer assigned
    private boolean active;

    public Employee() {}

    public Employee(int employeeId, String firstName, String lastName, String title,
                    Integer reportsTo, LocalDateTime birthDate, LocalDateTime hireDate,
                    String address, String city, String state, String country,
                    String postalCode, String phone, String fax, String email) {
        super(firstName, lastName, address, city, state, country, postalCode, phone, fax, email);
        this.employeeId = employeeId;
        this.title      = title;
        this.reportsTo  = reportsTo;
        this.birthDate  = birthDate;
        this.hireDate   = hireDate;
    }

    public int getEmployeeId()               { return employeeId; }
    public void setEmployeeId(int employeeId){ this.employeeId = employeeId; }

    public String getTitle()                 { return title; }
    public void setTitle(String title)       { this.title = title; }

    public Integer getReportsTo()            { return reportsTo; }
    public void setReportsTo(Integer reportsTo) { this.reportsTo = reportsTo; }

    public LocalDateTime getBirthDate()      { return birthDate; }
    public void setBirthDate(LocalDateTime birthDate) { this.birthDate = birthDate; }

    public LocalDateTime getHireDate()       { return hireDate; }
    public void setHireDate(LocalDateTime hireDate)  { this.hireDate = hireDate; }

    public String getSupervisorName()        { return supervisorName; }
    public void setSupervisorName(String supervisorName) { this.supervisorName = supervisorName; }

    public boolean isActive()                { return active; }
    public void setActive(boolean active)    { this.active = active; }
}
