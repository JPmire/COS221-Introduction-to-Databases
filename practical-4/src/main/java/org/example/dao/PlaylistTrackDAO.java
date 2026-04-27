package org.example.dao;

import org.example.model.PlaylistTrack;
import org.example.util.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlaylistTrackDAO {

    public List<PlaylistTrack> getAll() {
        List<PlaylistTrack> list = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM PlaylistTrack");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(extractPlaylistTrackFromResultSet(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<PlaylistTrack> getByPlaylistId(int playlistId) {
        List<PlaylistTrack> list = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM PlaylistTrack WHERE PlaylistId = ?")) {
            ps.setInt(1, playlistId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(extractPlaylistTrackFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<PlaylistTrack> getByTrackId(int trackId) {
        List<PlaylistTrack> list = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM PlaylistTrack WHERE TrackId = ?")) {
            ps.setInt(1, trackId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(extractPlaylistTrackFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // PlaylistTrack has a composite PK so insert serves as the "add track to playlist" operation
    public boolean insert(PlaylistTrack pt) {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT IGNORE INTO PlaylistTrack (PlaylistId, TrackId) VALUES (?, ?)")) {
            ps.setInt(1, pt.getPlaylistId());
            ps.setInt(2, pt.getTrackId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // No update — composite PK is the whole row; delete + re-insert to change it
    public boolean update(PlaylistTrack pt) {
        throw new UnsupportedOperationException("PlaylistTrack has no updateable fields; delete and re-insert instead.");
    }

    // Removes a specific track from a specific playlist
    public boolean delete(int playlistId, int trackId) {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "DELETE FROM PlaylistTrack WHERE PlaylistId = ? AND TrackId = ?")) {
            ps.setInt(1, playlistId);
            ps.setInt(2, trackId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Satisfies DAO contract — deletes all rows for a given PlaylistId
    public boolean delete(int playlistId) {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "DELETE FROM PlaylistTrack WHERE PlaylistId = ?")) {
            ps.setInt(1, playlistId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private PlaylistTrack extractPlaylistTrackFromResultSet(ResultSet rs) throws SQLException {
        return new PlaylistTrack(rs.getInt("PlaylistId"), rs.getInt("TrackId"));
    }
}
