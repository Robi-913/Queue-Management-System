package org.example.pt2024_30225_ardelean_robert_assignment_2;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class InterfaceController {

    @FXML
    private Button startSimulationButton;
    @FXML
    private TextField nrOfClients;
    @FXML
    private TextField nrOfQueues;
    @FXML
    private TextField simulationInterval;
    @FXML
    private TextField serviceTime;
    @FXML
    private TextField arrivalTime;



    @FXML
    protected void onStartSimulation() {
        startSimulationButton.setDisable(true);

        try {
            int numberOfClients = Integer.parseInt(nrOfClients.getText());
            int numberOfQueues = Integer.parseInt(nrOfQueues.getText());
            int simulationTimeMax = Integer.parseInt(simulationInterval.getText());
            int[] arrivalTimeBounds = parseRange(arrivalTime.getText());
            int[] serviceTimeBounds = parseRange(serviceTime.getText());


            if (arrivalTimeBounds == null || serviceTimeBounds == null) {
                showNotification("Invalid range format. Please use the correct format.", "ERROR");
                startSimulationButton.setDisable(false);
                return;
            }

            SimulationManager simulationManager = new SimulationManager(
                    simulationTimeMax,
                    numberOfClients,
                    numberOfQueues,
                    arrivalTimeBounds[0],
                    arrivalTimeBounds[1],
                    serviceTimeBounds[0],
                    serviceTimeBounds[1],
                    message -> Platform.runLater(() -> {
                        showNotification(message, "SUCCESS");
                        startSimulationButton.setDisable(false);
                    })
            );

            new Thread(simulationManager).start();

        } catch (Exception e) {
            showNotification("An error occurred: " + e.getMessage(), "ERROR");
            Platform.runLater(() -> startSimulationButton.setDisable(false));
        }
    }

    private int[] parseRange(String text) {
        String normalized = text.trim().replaceAll("[\\[\\](){}]", "");
        Pattern pattern = Pattern.compile("\\s*(\\d+)\\s*,\\s*(\\d+)\\s*");
        Matcher matcher = pattern.matcher(normalized);

        if (matcher.matches()) {
            int min = Integer.parseInt(matcher.group(1));
            int max = Integer.parseInt(matcher.group(2));
            return new int[]{min, max};
        } else {
            return null;
        }
    }

    public void showNotification(String message, String alertType) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(alertType);
        alert.setHeaderText(null);
        alert.setContentText(message);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(Objects.requireNonNull(InterfaceController.class.getResource("/org/example/pt2024_30225_ardelean_robert_assignment_2/CSSFiles/ErrorPopup.css")).toExternalForm());
        dialogPane.getStyleClass().add("error-panel");

        dialogPane.setGraphic(null);

        dialogPane.setHeader(null);

        DropShadow dropShadow = new DropShadow();
        ButtonBar buttonBar = (ButtonBar) dialogPane.lookup(".button-bar");
        if (buttonBar != null) {
            buttonBar.getButtons().forEach(node -> {
                if (node instanceof Button button) {
                    button.setEffect(dropShadow);
                }
            });
        }

        alert.showAndWait();
    }
}
