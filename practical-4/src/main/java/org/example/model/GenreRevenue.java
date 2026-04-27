package org.example.model;

import java.math.BigDecimal;

public class GenreRevenue {
    private String genreName;
    private BigDecimal totalRevenue;

    public GenreRevenue() {}

    public GenreRevenue(String genreName, BigDecimal totalRevenue) {
        this.genreName = genreName;
        this.totalRevenue = totalRevenue;
    }

    public String getGenreName() {
        return genreName;
    }

    public void setGenreName(String genreName) {
        this.genreName = genreName;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }
}
