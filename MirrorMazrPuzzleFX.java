package com.example.juego;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MirrorMazePuzzleFX extends Application {
    private String[] correctPattern = {"izquierda", "derecha", "centro"};
    private String[] playerPattern = new String[3];
    private int step = 0;
    private ComboBox<String> directionBox;
    private Label resultLabel;

    @Override
    public void start(Stage primaryStage) {
        Label instructionLabel = new Label("Coloca los espejos en la dirección correcta.");
        directionBox = new ComboBox<>();
        directionBox.getItems().addAll("izquierda", "derecha", "centro");

        Button submitButton = new Button("Colocar Espejo");
        resultLabel = new Label();

        submitButton.setOnAction(e -> placeMirror());

        VBox layout = new VBox(10, instructionLabel, directionBox, submitButton, resultLabel);
        Scene scene = new Scene(layout, 400, 200);

        primaryStage.setTitle("Laberinto de los Espejos");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void placeMirror() {
        if (step < 3) {
            playerPattern[step] = directionBox.getValue();
            step++;
        }

        if (step == 3) {
            checkSolution();
        }
    }

    private void checkSolution() {
        boolean correct = true;
        for (int i = 0; i < 3; i++) {
            if (!playerPattern[i].equals(correctPattern[i])) {
                correct = false;
                break;
            }
        }

        if (correct) {
            resultLabel.setText("¡Correcto! El portal se activa.");
        } else {
            step = 0;
            resultLabel.setText("Patrón incorrecto. Inténtalo de nuevo.");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

