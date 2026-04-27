package org.example.dao;

import org.example.model.Playlist;
import org.example.util.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlaylistDAO {

    public List<Playlist> getAll() {
        List<Playlist> list = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM Playlist ORDER BY Name");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(extractPlaylistFromResultSet(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Playlist getById(int id) {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM Playlist WHERE PlaylistId = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return extractPlaylistFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean insert(Playlist p) {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO Playlist (PlaylistId, Name) VALUES (?, ?)")) {
            ps.setInt(1, p.getPlaylistId());
            ps.setString(2, p.getName());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(Playlist p) {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement("UPDATE Playlist SET Name=? WHERE PlaylistId=?")) {
            ps.setString(1, p.getName());
            ps.setInt(2, p.getPlaylistId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int id) {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM Playlist WHERE PlaylistId = ?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Playlist extractPlaylistFromResultSet(ResultSet rs) throws SQLException {
        return new Playlist(rs.getInt("PlaylistId"), rs.getString("Name"));
    }
}
