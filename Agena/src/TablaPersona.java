import java.sql.*;

public class TablaPersona {
    private final DatabaseConnection dbConnection;

    public TablaPersona(DatabaseConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public int create(String nombre) throws SQLException {
        String sql = "INSERT INTO Personas(nombre) VALUES(?)";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, nombre);
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
        String sql = "DELETE FROM Personas WHERE id=?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean update(int id, String nombre) throws SQLException {
        String sql = "UPDATE Personas SET nombre=? WHERE id=?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean exists(int id) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Personas WHERE id=?";
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
}
