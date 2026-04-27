package org.example.dao;

import org.example.model.Customer;
import org.example.util.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {

    public List<Customer> getAll() {
        List<Customer> list = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM Customer ORDER BY LastName, FirstName");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(extractCustomerFromResultSet(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Customer getById(int id) {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM Customer WHERE CustomerId = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return extractCustomerFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean insert(Customer c) {
        String sql = """
                INSERT INTO Customer (CustomerId, FirstName, LastName, Company, Address, City, State,
                    Country, PostalCode, Phone, Fax, Email, SupportRepId)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, c.getCustomerId());
            ps.setString(2, c.getFirstName());
            ps.setString(3, c.getLastName());
            ps.setString(4, c.getCompany());
            ps.setString(5, c.getAddress());
            ps.setString(6, c.getCity());
            ps.setString(7, c.getState());
            ps.setString(8, c.getCountry());
            ps.setString(9, c.getPostalCode());
            ps.setString(10, c.getPhone());
            ps.setString(11, c.getFax());
            ps.setString(12, c.getEmail());
            if (c.getSupportRepId() != null) ps.setInt(13, c.getSupportRepId()); else ps.setNull(13, Types.INTEGER);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(Customer c) {
        String sql = """
                UPDATE Customer SET FirstName=?, LastName=?, Company=?, Address=?, City=?, State=?,
                    Country=?, PostalCode=?, Phone=?, Fax=?, Email=?, SupportRepId=?
                WHERE CustomerId=?
                """;
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getFirstName());
            ps.setString(2, c.getLastName());
            ps.setString(3, c.getCompany());
            ps.setString(4, c.getAddress());
            ps.setString(5, c.getCity());
            ps.setString(6, c.getState());
            ps.setString(7, c.getCountry());
            ps.setString(8, c.getPostalCode());
            ps.setString(9, c.getPhone());
            ps.setString(10, c.getFax());
            ps.setString(11, c.getEmail());
            if (c.getSupportRepId() != null) ps.setInt(12, c.getSupportRepId()); else ps.setNull(12, Types.INTEGER);
            ps.setInt(13, c.getCustomerId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int id) {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM Customer WHERE CustomerId = ?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Customers with no invoices or most recent invoice older than 2 years (task 4.6)
    public List<Customer> getInactiveCustomers() {
        String sql = """
                SELECT c.* FROM Customer c
                WHERE NOT EXISTS (SELECT 1 FROM Invoice i WHERE i.CustomerId = c.CustomerId)
                   OR (SELECT MAX(i.InvoiceDate) FROM Invoice i WHERE i.CustomerId = c.CustomerId)
                      < DATE_SUB(NOW(), INTERVAL 2 YEAR)
                ORDER BY c.LastName, c.FirstName
                """;
        List<Customer> list = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(extractCustomerFromResultSet(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Customer> searchInactive(String query) {
        String sql = """
                SELECT c.* FROM Customer c
                WHERE (NOT EXISTS (SELECT 1 FROM Invoice i WHERE i.CustomerId = c.CustomerId)
                    OR (SELECT MAX(i.InvoiceDate) FROM Invoice i WHERE i.CustomerId = c.CustomerId)
                       < DATE_SUB(NOW(), INTERVAL 2 YEAR))
                  AND (c.FirstName LIKE ? OR c.LastName LIKE ? OR c.Email LIKE ? OR c.Country LIKE ?)
                ORDER BY c.LastName, c.FirstName
                """;
        List<Customer> list = new ArrayList<>();
        String like = "%" + query + "%";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 1; i <= 4; i++) ps.setString(i, like);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(extractCustomerFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public int getNextId() {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT COALESCE(MAX(CustomerId), 0) + 1 FROM Customer");
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 1;
    }

    private Customer extractCustomerFromResultSet(ResultSet rs) throws SQLException {
        Customer c = new Customer();
        c.setCustomerId(rs.getInt("CustomerId"));
        c.setFirstName(rs.getString("FirstName"));
        c.setLastName(rs.getString("LastName"));
        c.setCompany(rs.getString("Company"));
        c.setAddress(rs.getString("Address"));
        c.setCity(rs.getString("City"));
        c.setState(rs.getString("State"));
        c.setCountry(rs.getString("Country"));
        c.setPostalCode(rs.getString("PostalCode"));
        c.setPhone(rs.getString("Phone"));
        c.setFax(rs.getString("Fax"));
        c.setEmail(rs.getString("Email"));
        int repId = rs.getInt("SupportRepId");
        c.setSupportRepId(rs.wasNull() ? null : repId);
        return c;
    }
}
