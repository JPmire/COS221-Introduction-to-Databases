package org.example.dao;

import org.example.model.InvoiceLine;
import org.example.util.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InvoiceLineDAO {

    public List<InvoiceLine> getAll() {
        List<InvoiceLine> list = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM InvoiceLine");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(extractInvoiceLineFromResultSet(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<InvoiceLine> getByInvoiceId(int invoiceId) {
        List<InvoiceLine> list = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM InvoiceLine WHERE InvoiceId = ?")) {
            ps.setInt(1, invoiceId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(extractInvoiceLineFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean insert(InvoiceLine il) {
        String sql = "INSERT INTO InvoiceLine (InvoiceLineId, InvoiceId, TrackId, UnitPrice, Quantity) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, il.getInvoiceLineId());
            ps.setInt(2, il.getInvoiceId());
            ps.setInt(3, il.getTrackId());
            ps.setBigDecimal(4, il.getUnitPrice());
            ps.setInt(5, il.getQuantity());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(InvoiceLine il) {
        String sql = "UPDATE InvoiceLine SET InvoiceId=?, TrackId=?, UnitPrice=?, Quantity=? WHERE InvoiceLineId=?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, il.getInvoiceId());
            ps.setInt(2, il.getTrackId());
            ps.setBigDecimal(3, il.getUnitPrice());
            ps.setInt(4, il.getQuantity());
            ps.setInt(5, il.getInvoiceLineId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int id) {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM InvoiceLine WHERE InvoiceLineId = ?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private InvoiceLine extractInvoiceLineFromResultSet(ResultSet rs) throws SQLException {
        InvoiceLine il = new InvoiceLine();
        il.setInvoiceLineId(rs.getInt("InvoiceLineId"));
        il.setInvoiceId(rs.getInt("InvoiceId"));
        il.setTrackId(rs.getInt("TrackId"));
        il.setUnitPrice(rs.getBigDecimal("UnitPrice"));
        il.setQuantity(rs.getInt("Quantity"));
        return il;
    }
}
