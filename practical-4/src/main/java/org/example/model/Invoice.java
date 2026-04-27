package org.example.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Invoice {

    private int invoiceId;
    private int customerId;
    private LocalDateTime invoiceDate;
    private String billingAddress;
    private String billingCity;
    private String billingState;
    private String billingCountry;
    private String billingPostalCode;
    private BigDecimal total;

    public Invoice() {}

    public Invoice(int invoiceId, int customerId, LocalDateTime invoiceDate,
                   String billingAddress, String billingCity, String billingState,
                   String billingCountry, String billingPostalCode, BigDecimal total) {
        this.invoiceId         = invoiceId;
        this.customerId        = customerId;
        this.invoiceDate       = invoiceDate;
        this.billingAddress    = billingAddress;
        this.billingCity       = billingCity;
        this.billingState      = billingState;
        this.billingCountry    = billingCountry;
        this.billingPostalCode = billingPostalCode;
        this.total             = total;
    }

    public int getInvoiceId()                        { return invoiceId; }
    public void setInvoiceId(int invoiceId)          { this.invoiceId = invoiceId; }

    public int getCustomerId()                       { return customerId; }
    public void setCustomerId(int customerId)        { this.customerId = customerId; }

    public LocalDateTime getInvoiceDate()            { return invoiceDate; }
    public void setInvoiceDate(LocalDateTime date)   { this.invoiceDate = date; }

    public String getBillingAddress()                { return billingAddress; }
    public void setBillingAddress(String addr)       { this.billingAddress = addr; }

    public String getBillingCity()                   { return billingCity; }
    public void setBillingCity(String city)          { this.billingCity = city; }

    public String getBillingState()                  { return billingState; }
    public void setBillingState(String state)        { this.billingState = state; }

    public String getBillingCountry()                { return billingCountry; }
    public void setBillingCountry(String country)    { this.billingCountry = country; }

    public String getBillingPostalCode()             { return billingPostalCode; }
    public void setBillingPostalCode(String code)    { this.billingPostalCode = code; }

    public BigDecimal getTotal()                     { return total; }
    public void setTotal(BigDecimal total)           { this.total = total; }
}
