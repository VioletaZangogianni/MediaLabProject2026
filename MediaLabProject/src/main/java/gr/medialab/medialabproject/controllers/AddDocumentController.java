package gr.medialab.medialabproject.controllers;

import gr.medialab.medialabproject.model.*;
import gr.medialab.medialabproject.service.DataService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.List;
import java.util.stream.Collectors;

public class AddDocumentController {

    @FXML private TextField titleField;
    @FXML private ComboBox<String> categoryCombo;
    @FXML private TextArea contentArea;

    private Document documentToEdit;
    private User currentUser;
    private DocumentsController parentController;

    public void init(Document doc, User user, DocumentsController parent) {
        this.documentToEdit = doc;
        this.currentUser = user;
        this.parentController = parent;

        List<String> cats = currentUser.getCategories()
                .stream()
                .map(Category::getName)
                .collect(Collectors.toList());
        categoryCombo.setItems(FXCollections.observableArrayList(cats));

        if (doc != null) {
            // EDIT MODE
            titleField.setText(doc.getName());
            titleField.setDisable(true);
            categoryCombo.setValue(doc.getCategory().getName());
            categoryCombo.setDisable(true);
            contentArea.setText(doc.open(false));
        } else {
            // ADD MODE
            if (!cats.isEmpty()) categoryCombo.setValue(cats.get(0));
        }
    }

    @FXML
    private void handleSave() {
        String title       = titleField.getText().trim();
        String catName     = categoryCombo.getValue();
        String textContent = contentArea.getText();

        if (title.isEmpty() || catName == null || catName.isEmpty()) {
            new Alert(Alert.AlertType.WARNING,
                    "Title and Category are required.", ButtonType.OK).showAndWait();
            return;
        }


        if (documentToEdit == null) {
            if (DataService.getDocument(title) != null) {
                new Alert(Alert.AlertType.WARNING,
                        "A document with that title already exists.", ButtonType.OK).showAndWait();
                return;
            }

            Category category = DataService.getCategory(catName);
            ((Author) currentUser).createDocument(title, category, textContent);

        } else {
            ((Author) currentUser).editDocument(documentToEdit, textContent);
        }

        parentController.refreshTable();
        MainController.instance.refreshStats();
        closeDialog();
    }

    @FXML
    private void handleCancel() { closeDialog(); }

    private void closeDialog() {
        ((Stage) titleField.getScene().getWindow()).close();
    }
}