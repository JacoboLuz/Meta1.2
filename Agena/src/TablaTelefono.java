import java.sql.*;

public class TablaTelefono {
    private final DatabaseConnection dbConnection;

    public TablaTelefono(DatabaseConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public int create(int personaId, String telefono) throws SQLException {
        String sql = "INSERT INTO Telefonos(personaId, telefono) VALUES(?, ?)";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, personaId);
            ps.setString(2, telefono);
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
        String sql = "DELETE FROM Telefonos WHERE id=?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean update(int id, String telefono) throws SQLException {
        String sql = "UPDATE Telefonos SET telefono=? WHERE id=?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, telefono);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        }
    }
}
