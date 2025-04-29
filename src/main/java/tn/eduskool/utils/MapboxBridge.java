package tn.eduskool.utils;

import javafx.application.Platform;
import java.util.function.Consumer;

public class MapboxBridge {
    private final Consumer<String> onAddressSelected;

    public MapboxBridge(Consumer<String> onAddressSelected) {
        this.onAddressSelected = onAddressSelected;
    }

    public void setAddress(String address) {
        if (address != null && !address.isEmpty()) {
            Platform.runLater(() -> onAddressSelected.accept(address));
        }
    }
}
