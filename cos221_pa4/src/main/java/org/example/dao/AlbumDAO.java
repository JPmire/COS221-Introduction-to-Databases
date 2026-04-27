package org.example.dao;

import org.example.model.Album;
import org.example.util.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlbumDAO {

    public List<Album> getAll() {
        List<Album> list = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM Album ORDER BY Title");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(extractAlbumFromResultSet(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Album getById(int id) {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM Album WHERE AlbumId = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return extractAlbumFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean insert(Album a) {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO Album (AlbumId, Title, ArtistId) VALUES (?, ?, ?)")) {
            ps.setInt(1, a.getAlbumId());
            ps.setString(2, a.getTitle());
            ps.setInt(3, a.getArtistId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(Album a) {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement("UPDATE Album SET Title=?, ArtistId=? WHERE AlbumId=?")) {
            ps.setString(1, a.getTitle());
            ps.setInt(2, a.getArtistId());
            ps.setInt(3, a.getAlbumId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int id) {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM Album WHERE AlbumId = ?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Album extractAlbumFromResultSet(ResultSet rs) throws SQLException {
        return new Album(rs.getInt("AlbumId"), rs.getString("Title"), rs.getInt("ArtistId"));
    }
}
