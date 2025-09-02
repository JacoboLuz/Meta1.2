import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.Optional;

public class Main extends Application {
    //Para Personas
    private TextField idPersona = new TextField();
    private TextField nomPersona = new TextField();
    private TextArea listaPersona = new TextArea();

    //Para Teléfonos
    private TextField idTelefono = new TextField();
    private TextField idPerTelefono = new TextField();
    private TextField telTelefono = new TextField();
    private TextArea listaTelefono = new TextArea();

    //Para Direcciones
    private TextField idDireccion = new TextField();
    private TextField calleDireccion = new TextField();
    private TextField ciudadDireccion = new TextField();
    private ComboBox<String> tipoDireccion = new ComboBox<>();
    private TextArea listaDireccion = new TextArea();

    //Para la agenda
    private ServiciosAgenda serviciosAgenda;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        DatabaseConnection dbConnection = new DatabaseConnection();
        serviciosAgenda = new ServiciosAgenda(dbConnection);

        TabPane tabPane = new TabPane();

        Tab perTab = new Tab("Personas", perPanel());
        perTab.setClosable(false);
        Tab telTab = new Tab("Telefonos", telPanel());
        telTab.setClosable(false);
        Tab dirTab = new Tab("Direcciones", dirPanel());
        dirTab.setClosable(false);
        tabPane.getTabs().addAll(perTab, telTab, dirTab);
        Scene scene = new Scene(tabPane, 500, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox perPanel() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));

        //Campos de texto
        grid.add(new Label("ID: "), 0, 0);
        grid.add(idPersona, 1, 0);
        idPersona.setDisable(true);
        grid.add(new Label("Nombre: "), 0, 1);
        grid.add(nomPersona, 1, 1);

        //Botones
        HBox hb = new HBox(10);
        Button alta = new Button("Alta");
        Button baja = new Button("Baja");
        Button modificar = new Button("Modificar");
        Button mostrar = new Button("Mostrar");
        Button cambiar = new Button("Cambiar campos");

        hb.getChildren().addAll(alta, baja, modificar, mostrar, cambiar);
        baja.setDisable(true);
        modificar.setDisable(true);

        //Acciones para los botones
        alta.setOnAction(e -> altaPer());
        baja.setOnAction(e -> bajaPer());
        modificar.setOnAction(e -> modificarPer());
        mostrar.setOnAction(e -> mostrarInf());

        //Cambiar el estado de los botones
        cambiar.setOnAction(e -> {
            if (!alta.isDisable()) {
                alta.setDisable(true);
                baja.setDisable(false);
                idPersona.setDisable(false);
                nomPersona.setDisable(true);
            } else if (!baja.isDisable()) {
                baja.setDisable(true);
                modificar.setDisable(false);
                nomPersona.setDisable(false);
            } else {
                modificar.setDisable(true);
                alta.setDisable(false);
                idPersona.setDisable(true);
            }
        });

        //Panel para mostrar la información
        listaPersona.setEditable(false);
        listaPersona.setPrefHeight(300);
        listaPersona.setWrapText(true);

        return new VBox(grid, hb, listaPersona);
    }

    private VBox telPanel() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));

        //Campos de texto
        grid.add(new Label("ID: "), 0, 0);
        grid.add(idTelefono, 1, 0);
        idTelefono.setDisable(true);
        grid.add(new Label("ID de la persona:"), 0, 1);
        grid.add(idPerTelefono, 1, 1);
        grid.add(new Label("Teléfono: "), 0, 2);
        grid.add(telTelefono, 1, 2);

        //Botones
        HBox hb = new HBox(10);
        Button alta = new Button("Alta");
        Button baja = new Button("Baja");
        Button modificar = new Button("Modificar");
        Button mostrar = new Button("Mostrar");
        Button cambiar = new Button("Cambiar campos");

        hb.getChildren().addAll(alta, baja, modificar, mostrar, cambiar);
        baja.setDisable(true);
        modificar.setDisable(true);

        //Acciones para los botones
        alta.setOnAction(e -> altaTel());
        baja.setOnAction(e -> bajaTel());
        modificar.setOnAction(e -> modificarTel());
        mostrar.setOnAction(e -> mostrarTelefonos());

        //Cambiar el estado de los botones
        cambiar.setOnAction(e -> {
            if (!alta.isDisable()) {
                alta.setDisable(true);
                baja.setDisable(false);
                idTelefono.setDisable(false);
                idPerTelefono.setDisable(true);
                telTelefono.setDisable(true);
            } else if (!baja.isDisable()) {
                baja.setDisable(true);
                modificar.setDisable(false);
                idPerTelefono.setDisable(true);
                telTelefono.setDisable(false);
            } else {
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

        return new VBox(grid, hb, listaTelefono);
    }

    private VBox dirPanel() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));

        //Campos de texto
        grid.add(new Label("ID Dirección: "), 0, 0);
        grid.add(idDireccion, 1, 0);
        idDireccion.setDisable(true);
        grid.add(new Label("Calle: "), 0, 1);
        grid.add(calleDireccion, 1, 1);
        grid.add(new Label("Ciudad: "), 0, 2);
        grid.add(ciudadDireccion, 1, 2);

        grid.add(new Label("Estado: "), 0, 3);
        tipoDireccion.getItems().addAll("Casa", "Trabajo", "Otra");
        tipoDireccion.setValue("Casa");
        grid.add(tipoDireccion, 1, 3);

        //Botones
        HBox hb = new HBox(10);
        Button alta = new Button("Alta");
        Button asignar = new Button("Asignar");
        Button desasignar = new Button("Desasignar");
        Button mostrar = new Button("Mostrar");
        Button bajaBtn = new Button("Baja");
        Button modificar = new Button("Modificar");
        Button cambiar = new Button("Cambiar campos");

        hb.getChildren().addAll(alta, asignar, desasignar, bajaBtn, modificar, mostrar, cambiar);
        asignar.setDisable(true);
        desasignar.setDisable(true);
        modificar.setDisable(true);
        bajaBtn.setDisable(true);
        tipoDireccion.setDisable(true);

        //Acciones para los botones
        alta.setOnAction(e -> altaDir());
        asignar.setOnAction(e -> asignarDir());
        desasignar.setOnAction(e -> desasignarDir());
        bajaBtn.setOnAction(e -> bajaDir());
        mostrar.setOnAction(e -> mostrarDir());
        modificar.setOnAction(e -> modificarDir());

        //Cambiar el estado de los botones
        cambiar.setOnAction(e -> {
            if (!alta.isDisable()) {
                alta.setDisable(true);
                asignar.setDisable(false);
                desasignar.setDisable(false);
                bajaBtn.setDisable(false);
                modificar.setDisable(true);
                calleDireccion.setDisable(true);
                ciudadDireccion.setDisable(true);
                tipoDireccion.setDisable(false);
                idDireccion.setDisable(false);
            } else if (!asignar.isDisable()) {
                alta.setDisable(true);
                asignar.setDisable(true);
                desasignar.setDisable(true);
                bajaBtn.setDisable(true);
                modificar.setDisable(false);
                calleDireccion.setDisable(false);
                ciudadDireccion.setDisable(false);
                tipoDireccion.setDisable(true);
                idDireccion.setDisable(false);
            } else {
                alta.setDisable(false);
                asignar.setDisable(true);
                desasignar.setDisable(true);
                bajaBtn.setDisable(true);
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

        return new VBox(grid, hb, listaDireccion);
    }

    //////////////////Para Persona//////////////////
    private void altaPer() {
        if (nomPersona.getText().trim().isEmpty()) {
            Alerta.showError("Los campos no pueden estar vacíos.");
            return;
        }

        Optional<String> resultado = serviciosAgenda.altaPersona(nomPersona.getText());
        resultado.ifPresent(Alerta::showInformation);
    }

    private void bajaPer() {
        if (idPersona.getText().trim().isEmpty()) {
            Alerta.showError("Los campos no pueden estar vacíos.");
            return;
        }

        try {
            int id = Integer.parseInt(idPersona.getText());
            Optional<String> resultado = serviciosAgenda.bajaPersona(id);
            resultado.ifPresent(Alerta::showInformation);
        } catch (NumberFormatException e) {
            Alerta.showError("El ID debe ser un número válido.");
        }
    }

    private void modificarPer() {
        if (idPersona.getText().trim().isEmpty() || nomPersona.getText().trim().isEmpty()) {
            Alerta.showError("Los campos no pueden estar vacíos.");
            return;
        }

        try {
            int id = Integer.parseInt(idPersona.getText());
            Optional<String> resultado = serviciosAgenda.modificarPersona(id, nomPersona.getText());
            resultado.ifPresent(Alerta::showInformation);
        } catch (NumberFormatException e) {
            Alerta.showError("El ID debe ser un número válido.");
        }
    }

    //////////////////Para Telefono//////////////////
    private void altaTel() {
        if (idPerTelefono.getText().trim().isEmpty() || telTelefono.getText().trim().isEmpty()) {
            Alerta.showError("Los campos no pueden estar vacíos.");
            return;
        }

        try {
            int personaId = Integer.parseInt(idPerTelefono.getText());
            Optional<String> resultado = serviciosAgenda.altaTelefono(personaId, telTelefono.getText());
            resultado.ifPresent(Alerta::showInformation);
        } catch (NumberFormatException e) {
            Alerta.showError("El ID de persona debe ser un número válido.");
        }
    }

    private void bajaTel() {
        if (idTelefono.getText().trim().isEmpty()) {
            Alerta.showError("Los campos no pueden estar vacíos.");
            return;
        }

        try {
            int id = Integer.parseInt(idTelefono.getText());
            Optional<String> resultado = serviciosAgenda.bajaTelefono(id);
            resultado.ifPresent(Alerta::showInformation);
        } catch (NumberFormatException e) {
            Alerta.showError("El ID debe ser un número válido.");
        }
    }

    private void modificarTel() {
        if (idTelefono.getText().trim().isEmpty() || telTelefono.getText().trim().isEmpty()) {
            Alerta.showError("Los campos no pueden estar vacíos.");
            return;
        }

        try {
            int id = Integer.parseInt(idTelefono.getText());
            Optional<String> resultado = serviciosAgenda.modificarTelefono(id, telTelefono.getText());
            resultado.ifPresent(Alerta::showInformation);
        } catch (NumberFormatException e) {
            Alerta.showError("El ID debe ser un número válido.");
        }
    }

    //////////////////Para Direcciones//////////////////
    private void altaDir() {
        if (calleDireccion.getText().trim().isEmpty() || ciudadDireccion.getText().trim().isEmpty()) {
            Alerta.showError("Los campos no pueden estar vacíos.");
            return;
        }

        Optional<String> resultado = serviciosAgenda.altaDireccion(calleDireccion.getText(), ciudadDireccion.getText());
        resultado.ifPresent(Alerta::showInformation);
    }

    private void asignarDir() {
        if (idDireccion.getText().trim().isEmpty()) {
            Alerta.showError("Los campos no pueden estar vacíos.");
            return;
        }

        TextInputDialog dialog = new TextInputDialog();
        dialog.setContentText("Ingrese el ID de la persona a la que desea asignar esta dirección:");
        Optional<String> result = dialog.showAndWait();

        result.ifPresent(personaIdStr -> {
            try {
                int personaId = Integer.parseInt(personaIdStr);
                int direccionId = Integer.parseInt(idDireccion.getText().trim());
                if (!serviciosAgenda.existePersona(personaId) || !serviciosAgenda.existeDireccion(direccionId)) {
                    Alerta.showError("Revise la información ingresada.");
                    return;
                }

                Optional<String> resultado = serviciosAgenda.asignarDireccion(direccionId, personaId, tipoDireccion.getValue());
                resultado.ifPresent(Alerta::showInformation);
            } catch (NumberFormatException e) {
                Alerta.showError("Los IDs deben ser números válidos.");
            }
        });
    }

    private void desasignarDir() {
        if (idDireccion.getText().trim().isEmpty()) {
            Alerta.showError("Los campos no pueden estar vacíos.");
            return;
        }

        TextInputDialog dialog = new TextInputDialog();
        dialog.setContentText("Ingrese el ID de la persona a la que desea desasignar esta dirección:");
        Optional<String> result = dialog.showAndWait();

        result.ifPresent(personaIdStr -> {
            try {
                int personaId = Integer.parseInt(personaIdStr);
                int direccionId = Integer.parseInt(idDireccion.getText().trim());
                if (!serviciosAgenda.existePersona(personaId) || !serviciosAgenda.existeDireccion(direccionId)) {
                    Alerta.showError("Revise la información ingresada.");
                    return;
                }

                Optional<String> resultado = serviciosAgenda.desasignarDireccion(direccionId, personaId);
                resultado.ifPresent(Alerta::showInformation);
            } catch (NumberFormatException e) {
                Alerta.showError("Los IDs deben ser números válidos.");
            }
        });
    }

    private void modificarDir() {
        if (idDireccion.getText().trim().isEmpty() || calleDireccion.getText().trim().isEmpty() || ciudadDireccion.getText().trim().isEmpty()) {
            Alerta.showError("Los campos no pueden estar vacíos.");
            return;
        }

        try {
            int id = Integer.parseInt(idDireccion.getText());
            Optional<String> resultado = serviciosAgenda.modificarDireccion(id, calleDireccion.getText(), ciudadDireccion.getText());
            resultado.ifPresent(Alerta::showInformation);
        } catch (NumberFormatException e) {
            Alerta.showError("El ID debe ser un número válido.");
        }
    }

    private void bajaDir() {
        if (idDireccion.getText().trim().isEmpty()) {
            Alerta.showError("Los campos no pueden estar vacíos.");
            return;
        }

        try {
            int id = Integer.parseInt(idDireccion.getText());
            Optional<String> resultado = serviciosAgenda.bajaDireccion(id);
            resultado.ifPresent(Alerta::showInformation);
        } catch (NumberFormatException e) {
            Alerta.showError("El ID debe ser un número válido.");
        }
    }

    //////////////////Para mostrar en los campos de texto//////////////////
    private void mostrarInf() {
        String infoPersonas = serviciosAgenda.mostrarInformacionCompleta();
        String infoTelefonos = serviciosAgenda.mostrarTelefonos();

        listaPersona.setText(infoPersonas);
        listaTelefono.setText(infoTelefonos);
    }
    private void mostrarTelefonos() {
        String infoTelefonos = serviciosAgenda.mostrarTelefonos();
        listaTelefono.setText(infoTelefonos);
    }

    private void mostrarDir() {
        String info = serviciosAgenda.mostrarDirecciones();
        listaDireccion.setText(info);
    }
}