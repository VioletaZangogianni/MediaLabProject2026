package gr.medialab.medialabproject.controllers;

import gr.medialab.medialabproject.model.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.List;
import java.util.stream.Collectors;

public class DocumentsController {

    @FXML private ComboBox<String> categoryFilter;
    @FXML private TableView<Document> documentsTable;
    @FXML private TableColumn<Document, String> titleCol;
    @FXML private TableColumn<Document, String> authorCol;
    @FXML private TableColumn<Document, String> categoryCol;
    @FXML private TableColumn<Document, String> dateCol;
    @FXML private TableColumn<Document, Integer> versionCol;
    @FXML private Button editBtn;
    @FXML private Button deleteBtn;
    @FXML private Button addBtn;

    private User currentUser;
    private boolean canEdit;

    @FXML
    public void initialize() {
        currentUser = MainController.instance.getCurrentUser();

        titleCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        categoryCol.setCellValueFactory(
                data -> new SimpleStringProperty(data.getValue().getCategory().getName()));
        dateCol.setCellValueFactory(
                data -> new SimpleStringProperty(data.getValue().getDate().toString()));
        versionCol.setCellValueFactory(new PropertyValueFactory<>("version"));

        canEdit = currentUser.getRole() != Roles.SIMPLEUSER;
        editBtn.setVisible(canEdit);
        editBtn.setManaged(canEdit);
        deleteBtn.setVisible(canEdit);
        deleteBtn.setManaged(canEdit);
        addBtn.setVisible(canEdit);
        addBtn.setManaged(canEdit);

        List<String> catNames = currentUser.getCategories()
                .stream()
                .map(Category::getName)
                .collect(Collectors.toList());
        catNames.add(0, "All Categories");
        categoryFilter.setItems(FXCollections.observableArrayList(catNames));
        categoryFilter.setValue("All Categories");

        loadDocuments(null);
    }

    @FXML
    private void filterByCategory() {
        String selected = categoryFilter.getValue();
        loadDocuments("All Categories".equals(selected) ? null : selected);
    }

    private void loadDocuments(String categoryName) {
        List<Document> docs = currentUser.getDocuments()
                .stream()
                .filter(d -> categoryName == null || d.getCategory().getName().equals(categoryName))
                .collect(Collectors.toList());
        documentsTable.setItems(FXCollections.observableArrayList(docs));
    }

    @FXML
    private void viewDocument() {
        Document selected = documentsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Please select a document to view.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(selected.getName());
        alert.setHeaderText(
                "Author: " + selected.getAuthor() +
                        "  |  Category: " + selected.getCategory().getName() +
                        "  |  Version: " + selected.getVersion() +
                        "  |  Date: " + selected.getDate()
        );

        String content = selected.open(canEdit);
        TextArea textArea = new TextArea(content);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);

        alert.getDialogPane().setExpandableContent(textArea);
        alert.showAndWait();
    }

    @FXML
    private void editDocument() {
        Document selected = documentsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Please select a document to edit.");
            return;
        }
        openDocumentDialog(selected);

        loadDocuments(null);
    }

    @FXML
    private void deleteDocument() {
        Document selected = documentsTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Please select a document to delete.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete \"" + selected.getName() + "\" (version " + selected.getVersion() + ")?\n" +
                        "This will remove ALL versions of this document.",
                ButtonType.YES, ButtonType.NO);
        confirm.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.YES) {
                if(currentUser instanceof Author a) {
                    a.deleteDocument(selected.getName());

                    loadDocuments(null);
                    //MainController.instance.refreshStats();
                }
            }
        });
    }

    @FXML
    private void addDocument() {
        openDocumentDialog(null);

        loadDocuments(null);
    }

    private void openDocumentDialog(Document docToEdit) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/gr/medialab/medialabproject/addDocument.fxml"));
            Parent root = loader.load();

            AddDocumentController ctrl = loader.getController();
            ctrl.init(docToEdit, currentUser, this);

            Stage dialog = new Stage();
            dialog.setTitle(docToEdit == null ? "Add Document" : "Edit Document");
            dialog.setScene(new javafx.scene.Scene(root, 520, 420));
            dialog.initOwner(addBtn.getScene().getWindow());
            dialog.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void refreshTable() {
        loadDocuments(null);
    }

    private void showAlert(Alert.AlertType type, String message) {
        new Alert(type, message, ButtonType.OK).showAndWait();
    }
}
