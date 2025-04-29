package tn.eduskool.controllers;

import javafx.scene.control.ListCell;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import tn.eduskool.entities.Commentaire;

public class CommentaireListCell extends ListCell<Commentaire> {
    private VBox content;
    private Label commentText;
    private Label rating;

    public CommentaireListCell() {
        commentText = new Label();
        rating = new Label();
        content = new VBox(5); // 5 pixels spacing
        content.getChildren().addAll(commentText, rating);
    }

    @Override
    protected void updateItem(Commentaire comment, boolean empty) {
        super.updateItem(comment, empty);

        if (empty || comment == null) {
            setGraphic(null);
        } else {
            commentText.setText(comment.getContenu());
            rating.setText("Note: " + "★".repeat(comment.getNote()));
            setGraphic(content);
        }
    }
}