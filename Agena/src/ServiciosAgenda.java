import java.sql.*;
import java.util.Optional;

public class ServiciosAgenda {
    private final TablaPersona perTabla;
    private final TablaTelefono telTabla;
    private final TablaDireccion dirTabla;

    public ServiciosAgenda(DatabaseConnection dbConnection) {
        this.perTabla = new TablaPersona(dbConnection);
        this.telTabla = new TablaTelefono(dbConnection);
        this.dirTabla = new TablaDireccion(dbConnection);
    }

    public String mostrarInformacionCompleta() {
        StringBuilder sb = new StringBuilder();

        try {
            DatabaseConnection dbConnection = new DatabaseConnection();
            try (Connection conn = dbConnection.getConnection();
                 Statement stm = conn.createStatement();
                 ResultSet rs = stm.executeQuery("SELECT * FROM Personas")) {

                sb.append("=== LISTADO DE PERSONAS ===\n\n");
                while (rs.next()) {
                    int personaId = rs.getInt("id");
                    sb.append(String.format("ID: %d\nNombre: %s\n",
                            personaId, rs.getString("nombre")));

                    try (PreparedStatement psDir = conn.prepareStatement(
                            "SELECT d.id, d.calle, d.ciudad, pd.tipo " +
                                    "FROM Direcciones d " +
                                    "JOIN Persona_Direccion pd ON d.id = pd.direccionId " +
                                    "WHERE pd.personaId = ?")) {
                        psDir.setInt(1, personaId);
                        ResultSet rsDir = psDir.executeQuery();

                        sb.append("Direcciones:\n");
                        while (rsDir.next()) {
                            sb.append(String.format("  - %s, %s (%s)\n",
                                    rsDir.getString("calle"),
                                    rsDir.getString("ciudad"),
                                    rsDir.getString("tipo")));
                        }
                    }

                    try (Statement stmtTelefonos = conn.createStatement();
                         ResultSet rsTelefonos = stmtTelefonos.executeQuery(
                                 "SELECT id, telefono FROM Telefonos WHERE personaId = " + personaId)) {

                        sb.append("Teléfonos:\n");
                        while (rsTelefonos.next()) {
                            sb.append(String.format("  - ID: %d, Número: %s\n",
                                    rsTelefonos.getInt("id"),
                                    rsTelefonos.getString("telefono")));
                        }
                    }
                    sb.append("\n");
                }
            }

            return sb.toString();
        } catch (SQLException e) {
            return "Error al mostrar información.";
        }
    }

    public String mostrarTelefonos() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== LISTADO DE TELÉFONOS ===\n\n");

        try {
            DatabaseConnection dbConnection = new DatabaseConnection();
            try (Connection conn = dbConnection.getConnection();
                 Statement stm = conn.createStatement();
                 ResultSet rs = stm.executeQuery("SELECT * FROM Telefonos")) {

                while (rs.next()) {
                    sb.append(String.format("ID: %d | Persona ID: %d | Teléfono: %s\n",
                            rs.getInt("id"),
                            rs.getInt("personaId"),
                            rs.getString("telefono")));
                }
            }

            return sb.toString();
        } catch (SQLException e) {
            return "Error al mostrar información.";
        }
    }

    public String mostrarDirecciones() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== LISTADO DE DIRECCIONES ===\n\n");

        try {
            DatabaseConnection dbConnection = new DatabaseConnection();
            try (Connection conn = dbConnection.getConnection();
                 Statement stm = conn.createStatement();
                 ResultSet rs = stm.executeQuery("SELECT * FROM Direcciones")) {

                while (rs.next()) {
                    sb.append(String.format("ID: %d\nCalle: %s\nCiudad: %s\n",
                            rs.getInt("id"),
                            rs.getString("calle"),
                            rs.getString("ciudad")));

                    try (PreparedStatement psPer = conn.prepareStatement(
                            "SELECT p.id, p.nombre, pd.tipo " +
                                    "FROM Personas p " +
                                    "JOIN Persona_Direccion pd ON p.id = pd.personaId " +
                                    "WHERE pd.direccionId = ?")) {
                        psPer.setInt(1, rs.getInt("id"));
                        ResultSet rsPer = psPer.executeQuery();

                        sb.append("Personas asociadas:\n");
                        while (rsPer.next()) {
                            sb.append(String.format("  - %s (ID: %d) - %s\n",
                                    rsPer.getString("nombre"),
                                    rsPer.getInt("id"),
                                    rsPer.getString("tipo")));
                        }
                    }
                    sb.append("\n");
                }
            }

            return sb.toString();
        } catch (SQLException e) {
            return "Error al mostrar información.";
        }
    }

    public Optional<String> altaPersona(String nombre) {
        try {
            int id = perTabla.create(nombre);
            if (id != -1) {
                return Optional.of(String.format("El usuario %s se ha almacenado en el ID %d.", nombre, id));
            }
            return Optional.of("No se pudo crear la persona.");
        } catch (SQLException e) {
            return Optional.of("No se pudo completar esta acción.");
        }
    }

    public Optional<String> bajaPersona(int id) {
        try {
            if (perTabla.delete(id)) {
                return Optional.of(String.format("La persona de ID: %d ha sido dado de baja exitosamente.", id));
            }
            return Optional.of("No se ha encontrado el ID.");
        } catch (SQLException e) {
            return Optional.of("Los datos no pudieron ser eliminados.");
        }
    }

    public Optional<String> modificarPersona(int id, String nombre) {
        try {
            if (perTabla.update(id, nombre)) {
                return Optional.of(String.format("Se han actualizado correctamente los datos de la persona %s (ID: %d)", nombre, id));
            }
            return Optional.of("No se ha encontrado el ID.");
        } catch (SQLException e) {
            return Optional.of("Error al modificar persona.");
        }
    }

    public Optional<String> altaTelefono(int personaId, String telefono) {
        try {
            int id = telTabla.create(personaId, telefono);
            if (id != -1) {
                return Optional.of(String.format("El teléfono %s (ID:%d) ha sido almacenado en el ID %d.",
                        telefono, personaId, id));
            }
            return Optional.of("No se pudo crear el teléfono.");
        } catch (SQLException e) {
            return Optional.of("No se pudo completar esta acción.");
        }
    }

    public Optional<String> bajaTelefono(int id) {
        try {
            if (telTabla.delete(id)) {
                return Optional.of(String.format("El teléfono de ID: %d ha sido dado de baja exitosamente.", id));
            }
            return Optional.of("No se ha encontrado el ID.");
        } catch (SQLException e) {
            return Optional.of("No se pudo completar esta acción.");
        }
    }

    public Optional<String> modificarTelefono(int id, String telefono) {
        try {
            if (telTabla.update(id, telefono)) {
                return Optional.of(String.format("Se ha actualizado el número telefónico %s (ID: %d)", telefono, id));
            }
            return Optional.of("No se ha encontrado el ID.");
        } catch (SQLException e) {
            return Optional.of("No se pudo completar esta acción.");
        }
    }

    public Optional<String> altaDireccion(String calle, String ciudad) {
        try {
            int id = dirTabla.create(calle, ciudad);
            if (id != -1) {
                return Optional.of(String.format("Dirección creada con ID %d", id));
            }
            return Optional.of("No se pudo crear la dirección.");
        } catch (SQLException e) {
            return Optional.of("No se pudo completar esta acción.");
        }
    }

    public Optional<String> asignarDireccion(int direccionId, int personaId, String tipo) {
        try {
            if (dirTabla.assignToPerson(direccionId, personaId, tipo)) {
                return Optional.of(String.format("Dirección asignada correctamente a la persona con ID %d", personaId));
            }
            return Optional.of("No se pudo asignar la dirección.");
        } catch (SQLException e) {
            return Optional.of("No se pudo completar esta acción.");
        }
    }

    public Optional<String> desasignarDireccion(int direccionId, int personaId) {
        try {
            if (dirTabla.unassignFromPerson(direccionId, personaId)) {
                return Optional.of(String.format("Se ha desasignado correctamente la dirección para la persona con ID %d", personaId));
            }
            return Optional.of("No se pudo desasignar la dirección.");
        } catch (SQLException e) {
            return Optional.of("No se pudo completar esta acción.");
        }
    }

    public Optional<String> modificarDireccion(int id, String calle, String ciudad) {
        try {
            if (dirTabla.update(id, calle, ciudad)) {
                return Optional.of(String.format("Se ha actualizado la dirección con el ID %d", id));
            }
            return Optional.of("No se ha encontrado el ID.");
        } catch (SQLException e) {
            return Optional.of("No se pudo completar esta acción.");
        }
    }

    public Optional<String> bajaDireccion(int id) {
        try {
            if (dirTabla.delete(id)) {
                return Optional.of(String.format("La dirección de ID: %d ha sido dado de baja exitosamente.", id));
            }
            return Optional.of("No se ha encontrado el ID.");
        } catch (SQLException e) {
            return Optional.of("No se pudo completar esta acción.");
        }
    }

    public boolean existePersona(int id) {
        try {
            return perTabla.exists(id);
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean existeDireccion(int id) {
        try {
            return dirTabla.exists(id);
        } catch (SQLException e) {
            return false;
        }
    }
}
