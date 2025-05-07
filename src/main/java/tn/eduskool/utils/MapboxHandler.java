package tn.eduskool.utils;

import javafx.application.Platform;
import java.util.function.Consumer;

public class MapboxHandler {
    private final Consumer<String> onAddressSelected;

    public MapboxHandler(Consumer<String> onAddressSelected) {
        this.onAddressSelected = onAddressSelected;
    }

    public void handleAddressSelected(String address) {
        System.out.println("Adresse reçue: " + address);
        Platform.runLater(() -> onAddressSelected.accept(address));
    }

    public void logMessage(String message) {
        System.out.println("Mapbox: " + message);
    }
}
