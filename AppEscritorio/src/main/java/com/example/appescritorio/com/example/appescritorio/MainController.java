package com.example.appescritorio.com.example.appescritorio;

import com.example.appescritorio.com.example.appescritorio.clases.Mesa;
import com.example.appescritorio.com.example.appescritorio.services.MesaService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.util.ArrayList;
import java.util.List;

public class MainController {
    @FXML
    Button botonServir1, botonServir2,botonServir3,botonServir4,botonServir5;

    @FXML
    Label estado1, estado2, estado3,  estado4, estado5;

    @FXML
    Button botonRefrescar;

    private ArrayList<Button> botonesServir;
    private ArrayList<Label> estados;

    @FXML
    public void initialize() {
        botonesServir = new ArrayList<>();
        botonesServir.add(botonServir1);
        botonesServir.add(botonServir2);
        botonesServir.add(botonServir3);
        botonesServir.add(botonServir4);
        botonesServir.add(botonServir5);

        estados = new ArrayList<>();
        estados.add(estado1);
        estados.add(estado2);
        estados.add(estado3);
        estados.add(estado4);
        estados.add(estado5);

        cambiarColorEstado();
        cambiarColorServir();

        botonServir1.setOnAction(e -> accionServir(0));
        botonServir2.setOnAction(e -> accionServir(1));
        botonServir3.setOnAction(e -> accionServir(2));
        botonServir4.setOnAction(e -> accionServir(3));
        botonServir5.setOnAction(e -> accionServir(4));
        botonRefrescar.setOnAction(e -> refrescar());

    }

    public void cambiarColorEstado(){
        new Thread(() -> { // Se inicia en otro hilo para poder usar las peticiones HTTP del backend

            try {
                List<Mesa> mesas = MesaService.getMesas(); // Listo las mesas de la BD

                int contador = 0; // El contador es para saber en que posicion estan los Labels ya que es la misma que las mesas
                for (Mesa m : mesas) { // Recorro las mesas y veo si esta pillada, dependiendo de eso se pone de un color u otro
                    final int index = contador; // No me dejaba hacer un contador normal porque estaba dentro de una lambda y no era una copia final
                    if (m.isEstadoMesa()){
                        Platform.runLater(() -> { // Uso platform.runLater para poder cambiar el UI desde otro hilo diferente al principal
                            estados.get(index).setStyle("-fx-background-color: #83E291; -fx-background-radius: 100; -fx-border-color: black; -fx-border-radius: 100;");
                        });
                    }
                    else{
                        Platform.runLater(() -> {
                            estados.get(index).setStyle( "-fx-background-color: #EF4D4D; -fx-background-radius: 100; -fx-border-color: black; -fx-border-radius: 100;");
                        });
                    }
                    contador++;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }).start();
    }

    public void cambiarColorServir(){ // Igual que en la funcion de arriba pero con los botones de servir
        new Thread(() -> {

            try {
                List<Mesa> mesas = MesaService.getMesas();

                int contador = 0;
                for (Mesa m : mesas) {
                    final int index = contador;
                    if (m.isEstadoPedido()){
                        Platform.runLater(() -> {
                            botonesServir.get(index).setStyle("-fx-background-color: #99E9FF;");
                        });
                    }

                    else {
                        Platform.runLater(() -> {
                            botonesServir.get(index).setStyle("-fx-background-color: #dddddd;");
                        });
                    }
                    contador++;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }).start();
    }

    public void accionServir (int posicionMesa){
        new Thread(() -> {

            try {
                List<Mesa> mesas = MesaService.getMesas(); // Se listan las mesas

                Mesa mesa = mesas.get(posicionMesa); // Se escoje la mesa correspondiente al boton
                if (mesa.isEstadoPedido()){ // Se ve el estado del pedido
                    mesa.setEstadoPedido(false); // Cambiamos el estado del pedido

                    MesaService.updateMesa(mesa.getId(), mesa); // Actualizamos la mesa para que se cambie el estado del pedido
                    Platform.runLater(() -> mostrarAlerta("Pedido servido correctamente")); // Se muestra una alerta

                    Platform.runLater(() -> {
                        botonesServir.get(posicionMesa).setStyle("-fx-background-color: #dddddd;"); // Se pone el color del boton como antes
                    });

                }

                else {
                    Platform.runLater(() -> mostrarAlerta("No hay ningun pedido de la mesa seleccionada para servir")); // Alerta
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }).start();
    }

    public void refrescar(){ // Para el boton de refrescar se usan las dos funciones que cambian los colores
        cambiarColorServir();
        cambiarColorEstado();
    }

    public void mostrarAlerta(String contenido) { // Funcion para crear alertas
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Información");
        alerta.setContentText(contenido);

        alerta.showAndWait();
    }
}
