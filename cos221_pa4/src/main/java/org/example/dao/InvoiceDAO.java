package org.example.dao;

import org.example.model.Invoice;
import org.example.util.DatabaseManager;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class InvoiceDAO {

    public List<Invoice> getAll() {
        List<Invoice> list = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM Invoice ORDER BY InvoiceDate DESC");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(extractInvoiceFromResultSet(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Invoice getById(int id) {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM Invoice WHERE InvoiceId = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return extractInvoiceFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Invoice> getByCustomerId(int customerId) {
        List<Invoice> list = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT * FROM Invoice WHERE CustomerId = ? ORDER BY InvoiceDate DESC")) {
            ps.setInt(1, customerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(extractInvoiceFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean insert(Invoice inv) {
        String sql = """
                INSERT INTO Invoice (InvoiceId, CustomerId, InvoiceDate, BillingAddress, BillingCity,
                    BillingState, BillingCountry, BillingPostalCode, Total)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, inv.getInvoiceId());
            ps.setInt(2, inv.getCustomerId());
            ps.setObject(3, inv.getInvoiceDate());
            ps.setString(4, inv.getBillingAddress());
            ps.setString(5, inv.getBillingCity());
            ps.setString(6, inv.getBillingState());
            ps.setString(7, inv.getBillingCountry());
            ps.setString(8, inv.getBillingPostalCode());
            ps.setBigDecimal(9, inv.getTotal());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(Invoice inv) {
        String sql = """
                UPDATE Invoice SET CustomerId=?, InvoiceDate=?, BillingAddress=?, BillingCity=?,
                    BillingState=?, BillingCountry=?, BillingPostalCode=?, Total=?
                WHERE InvoiceId=?
                """;
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, inv.getCustomerId());
            ps.setObject(2, inv.getInvoiceDate());
            ps.setString(3, inv.getBillingAddress());
            ps.setString(4, inv.getBillingCity());
            ps.setString(5, inv.getBillingState());
            ps.setString(6, inv.getBillingCountry());
            ps.setString(7, inv.getBillingPostalCode());
            ps.setBigDecimal(8, inv.getTotal());
            ps.setInt(9, inv.getInvoiceId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int id) {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM Invoice WHERE InvoiceId = ?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Genre name -> total revenue, ordered highest to lowest (task 4.4)
    public Map<String, BigDecimal> getRevenueByGenre() {
        String sql = """
                SELECT g.Name AS GenreName, SUM(il.UnitPrice * il.Quantity) AS Revenue
                FROM InvoiceLine il
                JOIN Track t ON il.TrackId = t.TrackId
                JOIN Genre  g ON t.GenreId  = g.GenreId
                GROUP BY g.GenreId, g.Name
                ORDER BY Revenue DESC
                """;
        Map<String, BigDecimal> result = new LinkedHashMap<>();
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) result.put(rs.getString("GenreName"), rs.getBigDecimal("Revenue"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    // --- Customer insights helpers for task 4.7 ---

    public BigDecimal getTotalSpentByCustomer(int customerId) {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT COALESCE(SUM(Total), 0) FROM Invoice WHERE CustomerId = ?")) {
            ps.setInt(1, customerId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getBigDecimal(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return BigDecimal.ZERO;
    }

    public int getPurchaseCountByCustomer(int customerId) {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT COUNT(*) FROM Invoice WHERE CustomerId = ?")) {
            ps.setInt(1, customerId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public LocalDateTime getLastPurchaseDateByCustomer(int customerId) {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT MAX(InvoiceDate) FROM Invoice WHERE CustomerId = ?")) {
            ps.setInt(1, customerId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Timestamp ts = rs.getTimestamp(1);
                    return ts != null ? ts.toLocalDateTime() : null;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Customer's most frequently purchased genre name (task 4.7)
    public String getFavouriteGenreByCustomer(int customerId) {
        String sql = """
                SELECT g.Name FROM InvoiceLine il
                JOIN Invoice i ON il.InvoiceId = i.InvoiceId
                JOIN Track   t ON il.TrackId   = t.TrackId
                JOIN Genre   g ON t.GenreId    = g.GenreId
                WHERE i.CustomerId = ?
                GROUP BY g.GenreId, g.Name
                ORDER BY COUNT(*) DESC
                LIMIT 1
                """;
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, customerId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Invoice extractInvoiceFromResultSet(ResultSet rs) throws SQLException {
        Invoice inv = new Invoice();
        inv.setInvoiceId(rs.getInt("InvoiceId"));
        inv.setCustomerId(rs.getInt("CustomerId"));
        Timestamp ts = rs.getTimestamp("InvoiceDate");
        inv.setInvoiceDate(ts != null ? ts.toLocalDateTime() : null);
        inv.setBillingAddress(rs.getString("BillingAddress"));
        inv.setBillingCity(rs.getString("BillingCity"));
        inv.setBillingState(rs.getString("BillingState"));
        inv.setBillingCountry(rs.getString("BillingCountry"));
        inv.setBillingPostalCode(rs.getString("BillingPostalCode"));
        inv.setTotal(rs.getBigDecimal("Total"));
        return inv;
    }
}
