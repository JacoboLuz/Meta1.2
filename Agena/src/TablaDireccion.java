import java.sql.*;

public class TablaDireccion {
    private final DatabaseConnection dbConnection;

    public TablaDireccion(DatabaseConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public int create(String calle, String ciudad) throws SQLException {
        String sql = "INSERT INTO Direcciones(calle, ciudad) VALUES(?, ?)";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, calle);
            ps.setString(2, ciudad);
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return -1;
    }

    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM Direcciones WHERE id=?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean update(int id, String calle, String ciudad) throws SQLException {
        String sql = "UPDATE Direcciones SET calle=?, ciudad=? WHERE id=?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, calle);
            ps.setString(2, ciudad);
            ps.setInt(3, id);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean exists(int id) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Direcciones WHERE id=?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    public boolean assignToPerson(int direccionId, int personaId, String tipo) throws SQLException {
        String sql = "INSERT INTO Persona_Direccion(personaId, direccionId, tipo) VALUES(?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE tipo=?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, personaId);
            ps.setInt(2, direccionId);
            ps.setString(3, tipo);
            ps.setString(4, tipo);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean unassignFromPerson(int direccionId, int personaId) throws SQLException {
        String sql = "DELETE FROM Persona_Direccion WHERE personaId=? AND direccionId=?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, personaId);
            ps.setInt(2, direccionId);
            return ps.executeUpdate() > 0;
        }
    }
}
