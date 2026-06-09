package gr.medialab.medialabproject.controllers;

import gr.medialab.medialabproject.model.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.List;

public class FollowedController {

    @FXML private TableView<Document> followedTable;
    @FXML private TableColumn<Document, String> fTitleCol;
    @FXML private TableColumn<Document, Integer> fVersionCol;
    @FXML private TableView<Document> allDocsTable;
    @FXML private TableColumn<Document, String> aTitleCol;
    @FXML private TableColumn<Document, String> aAuthorCol;
    @FXML private TableColumn<Document, String> aCategoryCol;

    private User currentUser;

    @FXML
    public void initialize() {
        currentUser = MainController.instance.getCurrentUser();

        fTitleCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        fVersionCol.setCellValueFactory(new PropertyValueFactory<>("version"));
        aTitleCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        aAuthorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        aCategoryCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getCategory().getName()));
        List<String> deleted=currentUser.deletedFollowingDocs();
        List<String> updated=currentUser.updatedFollowingDocs();
        if (!deleted.isEmpty() || !updated.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Updates of Following Documents");

            String content = "Updated Documents:\n" + updated + "\nDeleted Documents:\n" + deleted + "\n";
            alert.setContentText(content);

            alert.getDialogPane().setMinWidth(600);
            alert.showAndWait();
        }
        refreshTables();
    }

    private void refreshTables() {

        List<Document> allDocs = currentUser.getDocuments();
        allDocsTable.setItems(FXCollections.observableArrayList(allDocs));

        List<Document> followedDocs = currentUser.getFollowingDocs();
        followedTable.setItems(FXCollections.observableArrayList(followedDocs));
        MainController.instance.refreshStats();
    }

    @FXML
    private void addFollow() {
        Document selected = allDocsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            new Alert(Alert.AlertType.WARNING,
                    "Select a document to follow.", ButtonType.OK).showAndWait();
            return;
        } else{
            int ret=currentUser.followDoc(selected);
            if(ret==-1){
                new Alert(Alert.AlertType.WARNING,
                    "Document already followed", ButtonType.OK).showAndWait();
            };
        }

        refreshTables();
    }

    @FXML
    private void removeFollow() {
        Document selected = followedTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            new Alert(Alert.AlertType.WARNING,
                    "Select a followed document to remove.", ButtonType.OK).showAndWait();
            return;
        }else{
            currentUser.removeFollowDoc(selected);
        }
        refreshTables();
    }
}
