package org.example.dao;

import org.example.model.Employee;
import org.example.util.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {

    public List<Employee> getAll() {
        String sql = """
                SELECT e.*,
                       CONCAT(m.FirstName, ' ', m.LastName) AS SupervisorName,
                       EXISTS(SELECT 1 FROM Customer c WHERE c.SupportRepId = e.EmployeeId) AS Active
                FROM Employee e
                LEFT JOIN Employee m ON e.ReportsTo = m.EmployeeId
                ORDER BY e.LastName, e.FirstName
                """;
        List<Employee> list = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(extractEmployeeFromResultSet(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Employee getById(int id) {
        String sql = """
                SELECT e.*,
                       CONCAT(m.FirstName, ' ', m.LastName) AS SupervisorName,
                       EXISTS(SELECT 1 FROM Customer c WHERE c.SupportRepId = e.EmployeeId) AS Active
                FROM Employee e
                LEFT JOIN Employee m ON e.ReportsTo = m.EmployeeId
                WHERE e.EmployeeId = ?
                """;
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return extractEmployeeFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Filters by first name, last name, or city
    public List<Employee> search(String query) {
        String sql = """
                SELECT e.*,
                       CONCAT(m.FirstName, ' ', m.LastName) AS SupervisorName,
                       EXISTS(SELECT 1 FROM Customer c WHERE c.SupportRepId = e.EmployeeId) AS Active
                FROM Employee e
                LEFT JOIN Employee m ON e.ReportsTo = m.EmployeeId
                WHERE e.FirstName LIKE ? OR e.LastName LIKE ? OR e.City LIKE ?
                ORDER BY e.LastName, e.FirstName
                """;
        List<Employee> list = new ArrayList<>();
        String like = "%" + query + "%";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, like);
            ps.setString(2, like);
            ps.setString(3, like);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(extractEmployeeFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean insert(Employee emp) {
        String sql = """
                INSERT INTO Employee (EmployeeId, LastName, FirstName, Title, ReportsTo,
                    BirthDate, HireDate, Address, City, State, Country, PostalCode, Phone, Fax, Email)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, emp.getEmployeeId());
            ps.setString(2, emp.getLastName());
            ps.setString(3, emp.getFirstName());
            ps.setString(4, emp.getTitle());
            if (emp.getReportsTo() != null) ps.setInt(5, emp.getReportsTo()); else ps.setNull(5, Types.INTEGER);
            ps.setObject(6, emp.getBirthDate());
            ps.setObject(7, emp.getHireDate());
            ps.setString(8, emp.getAddress());
            ps.setString(9, emp.getCity());
            ps.setString(10, emp.getState());
            ps.setString(11, emp.getCountry());
            ps.setString(12, emp.getPostalCode());
            ps.setString(13, emp.getPhone());
            ps.setString(14, emp.getFax());
            ps.setString(15, emp.getEmail());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(Employee emp) {
        String sql = """
                UPDATE Employee SET LastName=?, FirstName=?, Title=?, ReportsTo=?,
                    BirthDate=?, HireDate=?, Address=?, City=?, State=?, Country=?,
                    PostalCode=?, Phone=?, Fax=?, Email=?
                WHERE EmployeeId=?
                """;
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, emp.getLastName());
            ps.setString(2, emp.getFirstName());
            ps.setString(3, emp.getTitle());
            if (emp.getReportsTo() != null) ps.setInt(4, emp.getReportsTo()); else ps.setNull(4, Types.INTEGER);
            ps.setObject(5, emp.getBirthDate());
            ps.setObject(6, emp.getHireDate());
            ps.setString(7, emp.getAddress());
            ps.setString(8, emp.getCity());
            ps.setString(9, emp.getState());
            ps.setString(10, emp.getCountry());
            ps.setString(11, emp.getPostalCode());
            ps.setString(12, emp.getPhone());
            ps.setString(13, emp.getFax());
            ps.setString(14, emp.getEmail());
            ps.setInt(15, emp.getEmployeeId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int id) {
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM Employee WHERE EmployeeId = ?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Employee extractEmployeeFromResultSet(ResultSet rs) throws SQLException {
        Employee emp = new Employee();
        emp.setEmployeeId(rs.getInt("EmployeeId"));
        emp.setFirstName(rs.getString("FirstName"));
        emp.setLastName(rs.getString("LastName"));
        emp.setTitle(rs.getString("Title"));
        int reportsTo = rs.getInt("ReportsTo");
        emp.setReportsTo(rs.wasNull() ? null : reportsTo);
        Timestamp birth = rs.getTimestamp("BirthDate");
        emp.setBirthDate(birth != null ? birth.toLocalDateTime() : null);
        Timestamp hire = rs.getTimestamp("HireDate");
        emp.setHireDate(hire != null ? hire.toLocalDateTime() : null);
        emp.setAddress(rs.getString("Address"));
        emp.setCity(rs.getString("City"));
        emp.setState(rs.getString("State"));
        emp.setCountry(rs.getString("Country"));
        emp.setPostalCode(rs.getString("PostalCode"));
        emp.setPhone(rs.getString("Phone"));
        emp.setFax(rs.getString("Fax"));
        emp.setEmail(rs.getString("Email"));
        try {
            emp.setSupervisorName(rs.getString("SupervisorName"));
            emp.setActive(rs.getBoolean("Active"));
        } catch (SQLException ignored) {}
        return emp;
    }
}
