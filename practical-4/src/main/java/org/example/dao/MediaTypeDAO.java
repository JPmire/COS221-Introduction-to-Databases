package org.example.dao;

import org.example.model.MediaType;
import org.example.util.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MediaTypeDAO {

    public List<MediaType> getAll() {
        List<MediaType> list = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM MediaType ORDER BY Name");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(extractMediaTypeFromResultSet(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public MediaType getById(int id) {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM MediaType WHERE MediaTypeId = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return extractMediaTypeFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean insert(MediaType mt) {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO MediaType (MediaTypeId, Name) VALUES (?, ?)")) {
            ps.setInt(1, mt.getMediaTypeId());
            ps.setString(2, mt.getName());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(MediaType mt) {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement("UPDATE MediaType SET Name=? WHERE MediaTypeId=?")) {
            ps.setString(1, mt.getName());
            ps.setInt(2, mt.getMediaTypeId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int id) {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM MediaType WHERE MediaTypeId = ?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private MediaType extractMediaTypeFromResultSet(ResultSet rs) throws SQLException {
        return new MediaType(rs.getInt("MediaTypeId"), rs.getString("Name"));
    }
}
