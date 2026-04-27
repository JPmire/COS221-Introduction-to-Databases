package org.example.dao;

import org.example.model.Artist;
import org.example.util.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ArtistDAO {

    public List<Artist> getAll() {
        List<Artist> list = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM Artist ORDER BY Name");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(extractArtistFromResultSet(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Artist getById(int id) {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM Artist WHERE ArtistId = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return extractArtistFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean insert(Artist a) {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO Artist (ArtistId, Name) VALUES (?, ?)")) {
            ps.setInt(1, a.getArtistId());
            ps.setString(2, a.getName());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(Artist a) {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement("UPDATE Artist SET Name=? WHERE ArtistId=?")) {
            ps.setString(1, a.getName());
            ps.setInt(2, a.getArtistId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int id) {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM Artist WHERE ArtistId = ?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Artist extractArtistFromResultSet(ResultSet rs) throws SQLException {
        return new Artist(rs.getInt("ArtistId"), rs.getString("Name"));
    }
}
