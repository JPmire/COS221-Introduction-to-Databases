package org.example.dao;

import org.example.model.Genre;
import org.example.model.GenreRevenue;
import org.example.util.DatabaseManager;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GenreDAO {

    public List<GenreRevenue> getGenreRevenueReport() {
        List<GenreRevenue> list = new ArrayList<>();
        String sql = """
                SELECT g.Name AS GenreName, SUM(il.UnitPrice * il.Quantity) AS TotalRevenue
                FROM Genre g
                JOIN Track t ON g.GenreId = t.GenreId
                JOIN InvoiceLine il ON t.TrackId = il.TrackId
                GROUP BY g.GenreId, g.Name
                ORDER BY TotalRevenue DESC
                """;
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new GenreRevenue(rs.getString("GenreName"), rs.getBigDecimal("TotalRevenue")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Genre> getAll() {
        List<Genre> list = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM Genre ORDER BY Name");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(extractGenreFromResultSet(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Genre getById(int id) {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM Genre WHERE GenreId = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return extractGenreFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean insert(Genre g) {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO Genre (GenreId, Name) VALUES (?, ?)")) {
            ps.setInt(1, g.getGenreId());
            ps.setString(2, g.getName());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(Genre g) {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement("UPDATE Genre SET Name=? WHERE GenreId=?")) {
            ps.setString(1, g.getName());
            ps.setInt(2, g.getGenreId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int id) {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM Genre WHERE GenreId = ?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Genre extractGenreFromResultSet(ResultSet rs) throws SQLException {
        return new Genre(rs.getInt("GenreId"), rs.getString("Name"));
    }
}
