import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.sql.*;
import java.util.Optional;

public class Main extends Application {
    //Personas
    private TextField idPersona=new TextField();
    private TextField nomPersona=new TextField();
    //private TextField dirPersona=new TextField();
    private TextArea listaPersona=new TextArea();

    //Teléfonos
    private TextField idTelefono=new TextField();
    private TextField idPerTelefono=new TextField();
    private TextField telTelefono=new TextField();
    private TextArea listaTelefono=new TextArea();

    //Direcciones
    private TextField idDireccion=new TextField();
    private TextField calleDireccion=new TextField();
    private TextField ciudadDireccion=new TextField();
    private ComboBox<String> tipoDireccion=new ComboBox<>();
    private TextArea listaDireccion=new TextArea();


    //Para la base de datos
    private static final String URL = "jdbc:mariadb://localhost:3306/agenda";
    private static final String USER = "usuario1";
    private static final String PASSWORD = "superpassword";


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        TabPane tabPane = new TabPane();

        //Panel para Personas
        Tab perTab=new Tab("Personas",perPanel());
        perTab.setClosable(false);

        //Panel para Telefonos
        Tab telTab=new Tab("Telefonos",telPanel());
        telTab.setClosable(false);

        //Panel para Direcciones
        Tab dirTab=new Tab("Direcciones",dirPanel());
        dirTab.setClosable(false);

        tabPane.getTabs().addAll(perTab, telTab,dirTab);
        Scene scene = new Scene(tabPane,500,500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    //Obtenemos la conexión
    private Connection getConnection() throws SQLException{
        return DriverManager.getConnection(URL,USER,PASSWORD);
    }

    private VBox perPanel(){
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10,10,10,10));

        //Campos de texto
        grid.add(new Label("ID: "),0,0);
        grid.add(idPersona,1,0);
        idPersona.setDisable(true);
        grid.add(new Label("Nombre: "),0,1);
        grid.add(nomPersona,1,1);
        //grid.add(new Label("Dirección: "),0,2);
        //grid.add(dirPersona,1,2);

        //Botones
        HBox hb=new HBox(10);
        Button alta=new Button("Alta");
        Button baja=new Button("Baja");
        Button modificar=new Button("Modificar");
        Button mostrar=new Button("Mostrar");
        Button cambiar=new Button("Cambiar campos");


        hb.getChildren().addAll(alta,baja,modificar,mostrar,cambiar);
        baja.setDisable(true);
        modificar.setDisable(true);

        //Acciones para los botones
        alta.setOnAction(e->altaPer());
        baja.setOnAction(e->bajaPer());
        modificar.setOnAction(e->modificarPer());
        mostrar.setOnAction(e->mostrarInf());

        //Cambiar el estado de los botones
        cambiar.setOnAction(e->{
            if(!alta.isDisable()){
                alta.setDisable(true);
                baja.setDisable(false);

                idPersona.setDisable(false);
                nomPersona.setDisable(true);
                //dirPersona.setDisable(true);

            }else if(!baja.isDisable()){
                baja.setDisable(true);
                modificar.setDisable(false);

                nomPersona.setDisable(false);
                //dirPersona.setDisable(false);
            }else{
                modificar.setDisable(true);
                alta.setDisable(false);

                idPersona.setDisable(true);
            }
        });

        //Panel para mostrar la información
        listaPersona.setEditable(false);
        listaPersona.setPrefHeight(300);
        listaPersona.setWrapText(true);

        VBox box = new VBox(grid,hb,listaPersona);
        return box;
    }

    private VBox telPanel(){
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10,10,10,10));

        //Campos de texto
        grid.add(new Label("ID: "),0,0);
        grid.add(idTelefono,1,0);
        idTelefono.setDisable(true);
        grid.add(new Label("ID de la persona:" ),0,1);
        grid.add(idPerTelefono,1,1);
        grid.add(new Label("Teléfono: "),0,2);
        grid.add(telTelefono,1,2);

        //Botones
        HBox hb=new HBox(10);
        Button alta=new Button("Alta");
        Button baja=new Button("Baja");
        Button modificar=new Button("Modificar");
        Button mostrar=new Button("Mostrar");
        Button cambiar=new Button("Cambiar campos");

        hb.getChildren().addAll(alta,baja,modificar,mostrar,cambiar);
        baja.setDisable(true);
        modificar.setDisable(true);

        //Acciones para los botones
        alta.setOnAction(e->altaTel());
        baja.setOnAction(e->bajaTel());
        modificar.setOnAction(e->modificarTel());
        mostrar.setOnAction(e->mostrarInf());

        //Cambiar el estado de los botones
        cambiar.setOnAction(e->{
            if(!alta.isDisable()){
                alta.setDisable(true);
                baja.setDisable(false);

                idTelefono.setDisable(false);
                idPerTelefono.setDisable(true);
                telTelefono.setDisable(true);

            }else if(!baja.isDisable()){
                baja.setDisable(true);
                modificar.setDisable(false);

                idPerTelefono.setDisable(true);
                telTelefono.setDisable(false);
            }else{
                modificar.setDisable(true);
                alta.setDisable(false);

                idTelefono.setDisable(true);
                idPerTelefono.setDisable(false);
            }
        });

        //Panel para mostrar la información
        listaTelefono.setEditable(false);
        listaTelefono.setPrefHeight(300);
        listaTelefono.setWrapText(true);

        VBox box = new VBox(grid,hb,listaTelefono);
        return box;
    }

    private VBox dirPanel(){
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10,10,10,10));

        //Campos de texto
        grid.add(new Label("ID Dirección: "),0,0);
        grid.add(idDireccion,1,0);
        idDireccion.setDisable(true);
        grid.add(new Label("Calle: " ),0,1);
        grid.add(calleDireccion,1,1);
        grid.add(new Label("Ciudad: "),0,2);
        grid.add(ciudadDireccion,1,2);

        grid.add(new Label("Estado: "),0,3);
        tipoDireccion.getItems().addAll("Casa","Trabajo","Otra");
        tipoDireccion.setValue("Casa");
        grid.add(tipoDireccion,1,3);

        //Botones
        HBox hb=new HBox(10);
        Button alta=new Button("Alta");
        Button asignar=new Button("Asignar");
        Button desasignar=new Button("Desasignar");
        Button mostrar=new Button("Mostrar");
        Button baja=new Button("Baja");
        Button modificar=new Button("Modificar");
        Button cambiar=new Button("Cambiar campos");

        hb.getChildren().addAll(alta,asignar,desasignar,baja,modificar,mostrar,cambiar);
        asignar.setDisable(true);
        desasignar.setDisable(true);
        modificar.setDisable(true);
        baja.setDisable(true);
        tipoDireccion.setDisable(true);

        //Acciones para los botones
        alta.setOnAction(e->altaDir());
        asignar.setOnAction(e->asignarDir());
        desasignar.setOnAction(e->desasignarDir());
        baja.setOnAction(e-> bajaDir());
        mostrar.setOnAction(e->mostrarDir());
        modificar.setOnAction(e->modificarDir());

        //Cambiar el estado de los botones
        cambiar.setOnAction(e->{
            if(!alta.isDisable()){
                alta.setDisable(true);
                asignar.setDisable(false);
                desasignar.setDisable(false);
                baja.setDisable(false);
                modificar.setDisable(true);

                calleDireccion.setDisable(true);
                ciudadDireccion.setDisable(true);
                tipoDireccion.setDisable(false);
                idDireccion.setDisable(false);
            }else if(!asignar.isDisable()){
                alta.setDisable(true);
                asignar.setDisable(true);
                desasignar.setDisable(true);
                baja.setDisable(true);
                modificar.setDisable(false);

                calleDireccion.setDisable(false);
                ciudadDireccion.setDisable(false);
                tipoDireccion.setDisable(true);
                idDireccion.setDisable(false);

            }else{
                alta.setDisable(false);
                asignar.setDisable(true);
                desasignar.setDisable(true);
                baja.setDisable(true);
                modificar.setDisable(true);

                calleDireccion.setDisable(false);
                ciudadDireccion.setDisable(false);
                tipoDireccion.setDisable(true);
                idDireccion.setDisable(true);

            }
        });



        //Panel para mostrar la información
        listaDireccion.setEditable(false);
        listaDireccion.setPrefHeight(300);
        listaDireccion.setWrapText(true);

        VBox box = new VBox(grid,hb,listaDireccion);
        return box;
    }



    //Función para mostrar en el area de texto la información
    private void mostrarInf() {
        StringBuilder sbPersonas = new StringBuilder();
        StringBuilder sbTelefonos = new StringBuilder();

        try (Connection conn = getConnection();
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT * FROM Personas")) {

            sbPersonas.append("=== LISTADO DE PERSONAS ===\n\n");
            while (rs.next()) {
                int personaId = rs.getInt("id");
                sbPersonas.append(String.format("ID: %d\nNombre: %s\n",
                        personaId, rs.getString("nombre")));

                //Conseguir direcciones
                try (PreparedStatement psDir = conn.prepareStatement(
                        "SELECT d.id, d.calle, d.ciudad, pd.tipo " +
                                "FROM Direcciones d " +
                                "JOIN Persona_Direccion pd ON d.id = pd.direccionId " +
                                "WHERE pd.personaId = ?")) {
                    psDir.setInt(1, personaId);
                    ResultSet rsDir = psDir.executeQuery();

                    sbPersonas.append("Direcciones:\n");
                    while (rsDir.next()) {
                        sbPersonas.append(String.format("  - %s, %s (%s)\n",
                                rsDir.getString("calle"),
                                rsDir.getString("ciudad"),
                                rsDir.getString("tipo")));
                    }
                }

                //Conseguir teléfonos
                try (Statement stmtTelefonos = conn.createStatement();
                     ResultSet rsTelefonos = stmtTelefonos.executeQuery(
                             "SELECT id, telefono FROM Telefonos WHERE personaId = " + personaId)) {

                    sbPersonas.append("Teléfonos:\n");
                    while (rsTelefonos.next()) {
                        sbPersonas.append(String.format("  - ID: %d, Número: %s\n",
                                rsTelefonos.getInt("id"),
                                rsTelefonos.getString("telefono")));

                        sbTelefonos.append(String.format("ID: %d | Persona ID: %d | Teléfono: %s\n",
                                rsTelefonos.getInt("id"),
                                personaId,
                                rsTelefonos.getString("telefono")));
                    }
                }
                sbPersonas.append("\n");
            }

            Platform.runLater(() -> {
                listaPersona.setText(sbPersonas.toString());
                listaTelefono.setText("=== LISTADO DE TELÉFONOS ===\n\n" + sbTelefonos.toString());
            });

        } catch (SQLException e) {
            Platform.runLater(() -> {
                error("Error al mostrar información.");
            });
        }
    }



    //////////////////Para Persona//////////////////
    private void altaPer(){
        String inst="INSERT INTO Personas(nombre) VALUES(?,?)";

        //comprobar si existen campos vacíos
        if (nomPersona.getText().trim().isEmpty()) {
            error("Los campos no pueden estar vacíos.");
            return;
        }

        try(Connection conn=getConnection();
            PreparedStatement ps=conn.prepareStatement(inst,Statement.RETURN_GENERATED_KEYS)){
            ps.setString(1,nomPersona.getText());
            ps.executeUpdate();

            try(ResultSet rs=ps.getGeneratedKeys()) {
                if(rs.next()){
                    int id=rs.getInt(1);
                    Platform.runLater(()->{
                        alertas(String.format("El usuario %s se ha" +
                                " almacenado en el ID %d.",nomPersona.getText(),id));
                    });

                }
            }
        }catch(SQLException e){
            Platform.runLater(()->{
                error(String.format("Los datos no pudieron ser guardados."));
            });

        }
    }


    private void bajaPer(){
        String inst="DELETE FROM Personas WHERE id=?";

        //comprobar si existen campos vacíos
        if (idPersona.getText().trim().isEmpty()) {
            error("Los campos no pueden estar vacíos.");
            return;
        }

        try(Connection conn=getConnection();
            PreparedStatement ps=conn.prepareStatement(inst,Statement.RETURN_GENERATED_KEYS)){
            ps.setInt(1,Integer.parseInt(idPersona.getText()));
            int cambios=ps.executeUpdate();

            if(cambios>0){
                Platform.runLater(()->{
                    alertas(String.format("La persona de ID: %s " +
                            "ha sido dado de baja exitosamente.",idPersona.getText()));
                });

            }else{
                Platform.runLater(()->{
                    alertas(String.format("No se ha encontrado el ID."));
                });
            }
        }catch(SQLException e){
            Platform.runLater(()->{
                error(String.format("Los datos no pudieron ser eliminados."));
            });

        }
    }


    private void modificarPer(){
        String inst="UPDATE Personas SET nombre=? WHERE id=?";

        //comprobar si existen campos vacíos
        if (idPersona.getText().trim().isEmpty()|| nomPersona.getText().trim().isEmpty()) {
            error("Los campos no pueden estar vacíos.");
            return;
        }

        try(Connection conn=getConnection();
            PreparedStatement ps=conn.prepareStatement(inst)){
            ps.setString(1,nomPersona.getText());
            ps.setInt(2,Integer.parseInt(idPersona.getText()));
            int cambios=ps.executeUpdate();

            if(cambios>0){
                Platform.runLater(()->{
                    alertas(String.format("Se han actualizado correctamente los" +
                            " datos de la persona %s (ID: %s)",nomPersona.getText(),idPersona.getText()));
                });

            }else{
                Platform.runLater(()->{
                    alertas(String.format("No se ha encontrado el ID."));
                });
            }
        }catch(SQLException e){
            Platform.runLater(()->{
                error(String.format("Los datos no pudieron ser modificados."));
            });

        }
    }

    //////////////////Para Telefono//////////////////
    private void altaTel(){
        String inst="INSERT INTO Telefonos(personaId,telefono) VALUES(?,?)";

        //comprobar si existen campos vacíos
        if (idPerTelefono.getText().trim().isEmpty() || telTelefono.getText().trim().isEmpty()) {
            error("Los campos no pueden estar vacíos.");
            return;
        }
        try {
            Integer.parseInt(idPerTelefono.getText().trim());
        } catch (NumberFormatException e) {
            error("El ID de persona debe ser un número válido");
            return;
        }

        try(Connection conn=getConnection();
            PreparedStatement ps=conn.prepareStatement(inst,Statement.RETURN_GENERATED_KEYS)){
            ps.setInt(1,Integer.parseInt(idPerTelefono.getText()));
            ps.setString(2,telTelefono.getText());
            int cambios=ps.executeUpdate();

            if(cambios>0){
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int id=generatedKeys.getInt(1);
                        Platform.runLater(() -> {
                            alertas(String.format("El teléfono %s(ID:%s) ha sido" +
                                    " almacenado en el ID %d.", telTelefono.getText(), idPerTelefono.getText(),id));
                        });
                    }
                }
            }
        }catch(SQLException e){
            Platform.runLater(()->{
                error(String.format(e.getMessage()));
            });

        }
    }


    private void bajaTel(){
        String inst="DELETE FROM Telefonos WHERE id=?";

        //comprobar si existen campos vacíos
        if (idTelefono.getText().trim().isEmpty()) {
            error("Los campos no pueden estar vacíos.");
            return;
        }

        try(Connection conn=getConnection();
            PreparedStatement ps=conn.prepareStatement(inst,Statement.RETURN_GENERATED_KEYS)){
            ps.setInt(1,Integer.parseInt(idTelefono.getText()));
            int cambios=ps.executeUpdate();

            if(cambios>0){
                Platform.runLater(()->{
                    alertas(String.format("El telefono de ID: %s " +
                            "ha sido dado de baja exitosamente.",idTelefono.getText()));
                });

            }else{
                Platform.runLater(()->{
                    alertas(String.format("No se ha encontrado el ID."));
                });
            }
        }catch(SQLException e){
            Platform.runLater(()->{
                error(String.format("Los datos no pudieron ser eliminados."));
            });

        }

    }
    private void modificarTel(){
        String inst="UPDATE Telefonos SET telefono=? WHERE id=?";

        //comprobar si existen campos vacíos
        if (idTelefono.getText().trim().isEmpty() || telTelefono.getText().trim().isEmpty()) {
            error("Los campos no pueden estar vacíos.");
            return;
        }

        try(Connection conn=getConnection();
            PreparedStatement ps=conn.prepareStatement(inst)){
            ps.setString(1,telTelefono.getText());
            ps.setInt(2,Integer.parseInt(idTelefono.getText()));
            int cambios=ps.executeUpdate();

            if(cambios>0){
                Platform.runLater(()->{
                    alertas(String.format("Se ha actualizado el número telefónico" +
                            " %s (ID: %s)",telTelefono.getText(),idTelefono.getText()));
                });

            }else{
                Platform.runLater(()->{
                    alertas(String.format("No se ha encontrado el ID."));
                });
            }
        }catch(SQLException e){
            Platform.runLater(()->{
                error(String.format("Los datos no pudieron ser modificados."));
            });

        }
    }


    //////////////////Para Direcciones//////////////////
    private void altaDir() {
        String inst="INSERT INTO Direcciones(calle,ciudad) VALUES(?,?)";

        if(calleDireccion.getText().trim().isEmpty() || ciudadDireccion.getText().trim().isEmpty()) {
            error("Los campos no pueden estar vacios.");
            return;
        }

        try(Connection conn=getConnection();
            PreparedStatement ps=conn.prepareStatement(inst,
                    Statement.RETURN_GENERATED_KEYS)){
            ps.setString(1,calleDireccion.getText());
            ps.setString(2,ciudadDireccion.getText());
            ps.executeUpdate();
            try (ResultSet rs=ps.getGeneratedKeys()){
                if(rs.next()){
                    int id = rs.getInt(1);
                    Platform.runLater(() -> {
                        alertas(String.format("Dirección creada con ID %d", id));
                    });

                }
            }
        } catch (SQLException e){
            Platform.runLater(()->{
                error(String.format("Los datos no pudieron ser modificados."));
            });
        }
    }

    private void asignarDir() {
        String inst = "INSERT INTO Persona_Direccion(personaId, direccionId, tipo) VALUES(?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE tipo=?";

        if(idDireccion.getText().trim().isEmpty()) {
            error("Los campos no pueden estar vacios.");
            return;
        }

        TextInputDialog dialog = new TextInputDialog();
        dialog.setContentText("Ingrese el ID de la persona a la que desea asignar esta dirección:");
        Optional<String> result = dialog.showAndWait();

        result.ifPresent(personaIdStr ->{
            try {
                int personaId = Integer.parseInt(personaIdStr);
                int direccionId = Integer.parseInt(idDireccion.getText().trim());

                // Verificar que la persona existe
                if (!existePersona(personaId) || !existeDireccion(direccionId)) {
                    error("Revise la información ingresada.");
                    return;
                }

                try (Connection conn = getConnection();
                     PreparedStatement ps = conn.prepareStatement(inst)) {
                    ps.setInt(1, personaId);
                    ps.setInt(2, direccionId);
                    ps.setString(3, tipoDireccion.getValue());
                    ps.setString(4, tipoDireccion.getValue());
                    int cambios = ps.executeUpdate();

                    if (cambios > 0) {
                        alertas("Dirección asignada correctamente a la persona con ID " + personaId);
                    }
                } catch (SQLException e) {
                    error("No se pudo completar esta acción.");
                }

            } catch (NumberFormatException e) {
                error("No se pudo completar esta acción.");
            }

        });
    }

    private void desasignarDir() {
        String inst = "DELETE FROM Persona_Direccion WHERE personaId = ? AND direccionId = ?";

        if(idDireccion.getText().trim().isEmpty()) {
            error("Los campos no pueden estar vacios.");
            return;
        }

        TextInputDialog dialog = new TextInputDialog();
        dialog.setContentText("Ingrese el ID de la persona a la que desea desasignar esta dirección: ");
        Optional<String> result = dialog.showAndWait();

        result.ifPresent(personaIdStr -> {
            try {
                int personaId = Integer.parseInt(personaIdStr);
                int direccionId = Integer.parseInt(idDireccion.getText().trim());

                // Verificar que la persona existe
                if (!existePersona(personaId) || !existeDireccion(direccionId)) {
                    error("Revise la información ingresada.");
                    return;
                }
                try (Connection conn = getConnection();
                     PreparedStatement ps = conn.prepareStatement(inst)) {
                    ps.setInt(1, personaId);
                    ps.setInt(2, direccionId);
                    int cambios = ps.executeUpdate();

                    if (cambios > 0) {
                        alertas("Se ha desasignado correctamente la dirección para la persona con ID " + personaId);
                    } else {
                        alertas("No se pudo completar esta acción. Revise los datos ingresados.");
                    }
                } catch (SQLException e) {
                    error("No se pudo completar esta acción.");
                }

            } catch (NumberFormatException e) {
                error("No se pudo completar esta acción.");
            }
        });

    }

    private void modificarDir(){
        String inst="UPDATE Direcciones SET calle=?,ciudad=? WHERE id=?";

        //comprobar si existen campos vacíos
        if (idDireccion.getText().trim().isEmpty() || calleDireccion.getText().trim().isEmpty() || ciudadDireccion.getText().trim().isEmpty()) {
            error("Los campos no pueden estar vacíos.");
            return;
        }

        try(Connection conn=getConnection();
            PreparedStatement ps=conn.prepareStatement(inst)){
            ps.setString(1,calleDireccion.getText());
            ps.setString(2,ciudadDireccion.getText());
            ps.setInt(3,Integer.parseInt(idDireccion.getText()));
            int cambios=ps.executeUpdate();

            if(cambios>0){
                Platform.runLater(()->{
                    alertas(String.format("Se ha actualizado la dirección con el ID ",idDireccion.getText()));
                });
            }else{
                Platform.runLater(()->{
                    alertas(String.format("No se ha encontrado el ID."));
                });
            }
        }catch(SQLException e){
            Platform.runLater(()->{
                error(String.format("Los datos no pudieron ser modificados."+e.getMessage()));
            });

        }

    }

    private void bajaDir(){
        String inst="DELETE FROM Direcciones WHERE id=?";

        //comprobar si existen campos vacíos
        if (idDireccion.getText().trim().isEmpty()) {
            error("Los campos no pueden estar vacíos.");
            return;
        }

        try(Connection conn=getConnection();
            PreparedStatement ps=conn.prepareStatement(inst,Statement.RETURN_GENERATED_KEYS)){
            ps.setInt(1,Integer.parseInt(idDireccion.getText()));
            int cambios=ps.executeUpdate();

            if(cambios>0){
                Platform.runLater(()->{
                    alertas(String.format("La dirección de ID: %s " +
                            "ha sido dado de baja exitosamente.",idDireccion.getText()));
                });

            }else{
                Platform.runLater(()->{
                    alertas(String.format("No se ha encontrado el ID."));
                });
            }
        }catch(SQLException e){
            Platform.runLater(()->{
                error(String.format("Los datos no pudieron ser eliminados."));
            });

        }

    }
    private void mostrarDir(){
        mostrarInf();
        StringBuilder sb = new StringBuilder();
        sb.append("=== LISTADO DE DIRECCIONES ===\n\n");

        try (Connection conn = getConnection();
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT * FROM Direcciones")) {

            while (rs.next()) {
                sb.append(String.format("ID: %d\nCalle: %s\nCiudad: %s\n",
                        rs.getInt("id"),
                        rs.getString("calle"),
                        rs.getString("ciudad")));

                // Obtener personas asociadas a esta dirección
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

            Platform.runLater(() -> {
                listaDireccion.setText(sb.toString());
            });

        } catch (SQLException e) {
            Platform.runLater(() -> {
                error("Error al mostrar información.");
            });
        }
    }




    

    //////////////////Para las alertas//////////////////
    public static void alertas(String msg){
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setContentText(msg);
        alert.showAndWait();
    }
    public static void error(String msg){
        Alert alert = new Alert(AlertType.ERROR);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    //////////////////Para las ventanas de dialogo//////////////////
    private Optional<String> mostrarDialogoIdPersona(String titulo, String mensaje) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(titulo);
        dialog.setHeaderText(null);
        dialog.setContentText(mensaje);

        return dialog.showAndWait();
    }

    //////////////////Para las verificaciones//////////////////
    private boolean existePersona(int personaid){
        String inst= "SELECT COUNT(*) FROM Personas WHERE id=?";
        try(Connection conn=getConnection();
            PreparedStatement ps=conn.prepareStatement(inst)){
            ps.setInt(1,personaid);
            ResultSet rs=ps.executeQuery();
            if(rs.next()){
                return rs.getInt(1)>0;
            }
        }catch(SQLException e){
            error("Error al buscar persona.");

        }
        return false;
    }
    private boolean existeDireccion(int direccionid){
        String inst= "SELECT COUNT(*) FROM Direcciones WHERE id=?";
        try(Connection conn=getConnection();
            PreparedStatement ps=conn.prepareStatement(inst)){
            ps.setInt(1,direccionid);
            ResultSet rs=ps.executeQuery();
            if(rs.next()){
                return rs.getInt(1)>0;
            }
        }catch(SQLException e){
            error("Error al buscar dirección.");

        }
        return false;
    }

}