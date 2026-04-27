package org.example.dao;

import org.example.model.Track;
import org.example.util.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TrackDAO {

    public List<Track> getAll() {
        List<Track> list = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM Track ORDER BY Name");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(extractTrackFromResultSet(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Track getById(int id) {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM Track WHERE TrackId = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return extractTrackFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean insert(Track t) {
        String sql = """
                INSERT INTO Track (TrackId, Name, AlbumId, MediaTypeId, GenreId, Composer, Milliseconds, Bytes, UnitPrice)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, t.getTrackId());
            ps.setString(2, t.getName());
            if (t.getAlbumId() != null) ps.setInt(3, t.getAlbumId()); else ps.setNull(3, Types.INTEGER);
            ps.setInt(4, t.getMediaTypeId());
            if (t.getGenreId() != null) ps.setInt(5, t.getGenreId()); else ps.setNull(5, Types.INTEGER);
            ps.setString(6, t.getComposer());
            ps.setInt(7, t.getMilliseconds());
            if (t.getBytes() != null) ps.setInt(8, t.getBytes()); else ps.setNull(8, Types.INTEGER);
            ps.setBigDecimal(9, t.getUnitPrice());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(Track t) {
        String sql = """
                UPDATE Track SET Name=?, AlbumId=?, MediaTypeId=?, GenreId=?, Composer=?,
                    Milliseconds=?, Bytes=?, UnitPrice=?
                WHERE TrackId=?
                """;
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, t.getName());
            if (t.getAlbumId() != null) ps.setInt(2, t.getAlbumId()); else ps.setNull(2, Types.INTEGER);
            ps.setInt(3, t.getMediaTypeId());
            if (t.getGenreId() != null) ps.setInt(4, t.getGenreId()); else ps.setNull(4, Types.INTEGER);
            ps.setString(5, t.getComposer());
            ps.setInt(6, t.getMilliseconds());
            if (t.getBytes() != null) ps.setInt(7, t.getBytes()); else ps.setNull(7, Types.INTEGER);
            ps.setBigDecimal(8, t.getUnitPrice());
            ps.setInt(9, t.getTrackId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int id) {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM Track WHERE TrackId = ?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int getNextId() {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT COALESCE(MAX(TrackId), 0) + 1 FROM Track");
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 1;
    }

    // Tracks in customer's top genres that the customer has not yet purchased (task 4.7)
    public List<Track> getRecommendationsForCustomer(int customerId, int limit) {
        String sql = """
                SELECT t.* FROM Track t
                JOIN (
                    SELECT tg.GenreId FROM InvoiceLine il
                    JOIN Invoice i  ON il.InvoiceId = i.InvoiceId
                    JOIN Track tg   ON il.TrackId   = tg.TrackId
                    WHERE i.CustomerId = ?
                    GROUP BY tg.GenreId
                    ORDER BY COUNT(*) DESC
                    LIMIT 3
                ) AS TopGenres ON t.GenreId = TopGenres.GenreId
                WHERE t.TrackId NOT IN (
                    SELECT il2.TrackId FROM InvoiceLine il2
                    JOIN Invoice i2 ON il2.InvoiceId = i2.InvoiceId
                    WHERE i2.CustomerId = ?
                )
                ORDER BY RAND()
                LIMIT ?
                """;
        List<Track> list = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, customerId);
            ps.setInt(2, customerId);
            ps.setInt(3, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(extractTrackFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private Track extractTrackFromResultSet(ResultSet rs) throws SQLException {
        Track t = new Track();
        t.setTrackId(rs.getInt("TrackId"));
        t.setName(rs.getString("Name"));
        int albumId = rs.getInt("AlbumId");
        t.setAlbumId(rs.wasNull() ? null : albumId);
        t.setMediaTypeId(rs.getInt("MediaTypeId"));
        int genreId = rs.getInt("GenreId");
        t.setGenreId(rs.wasNull() ? null : genreId);
        t.setComposer(rs.getString("Composer"));
        t.setMilliseconds(rs.getInt("Milliseconds"));
        int bytes = rs.getInt("Bytes");
        t.setBytes(rs.wasNull() ? null : bytes);
        t.setUnitPrice(rs.getBigDecimal("UnitPrice"));
        return t;
    }
}
