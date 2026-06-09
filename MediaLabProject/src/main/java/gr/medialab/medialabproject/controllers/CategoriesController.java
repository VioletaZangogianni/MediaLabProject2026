package gr.medialab.medialabproject.controllers;

import gr.medialab.medialabproject.model.*;
import gr.medialab.medialabproject.service.DataService;
import gr.medialab.medialabproject.service.JsonDataType;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.List;
import java.util.stream.Collectors;

public class CategoriesController {

    @FXML private ListView<String> categoryList;
    @FXML private TextField newCategoryField;
    @FXML private TextField renameField;

    @FXML
    public void initialize() {
        refreshList();
    }

    private void refreshList() {
        List<String> cats = DataService.getList(JsonDataType.CATEGORIES)
                .stream().map(c -> ((Category)c).getName()).collect(Collectors.toList());
        categoryList.setItems(FXCollections.observableArrayList(cats));
        MainController.instance.refreshStats();
    }

    @FXML
    private void handleAdd() {
        String name = newCategoryField.getText().trim();
        if (name.isEmpty()) {
            showAlert("Enter a category name."); return;
        }
        if (DataService.getCategory(name) != null) {
            showAlert("A category with that name already exists."); return;
        }
        DataService.createCategory(new Category(name));
        newCategoryField.clear();
        refreshList();
    }

    @FXML
    private void handleRename() {
        String selected = categoryList.getSelectionModel().getSelectedItem();
        String newName  = renameField.getText().trim();
        if (selected == null) { showAlert("Select a category to rename."); return; }
        if (newName.isEmpty()) { showAlert("Enter the new name."); return; }
        int result = DataService.renameCategory(selected, newName);
        if (result == -1) { showAlert("That name is already taken."); return; }
        renameField.clear();
        refreshList();
    }

    @FXML
    private void handleDelete() {
        String selected = categoryList.getSelectionModel().getSelectedItem();
        if (selected == null) { showAlert("Select a category to delete."); return; }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete '" + selected + "' and ALL its documents?",
                ButtonType.YES, ButtonType.NO);
        confirm.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.YES) {
                DataService.deleteCategory(selected);
                refreshList();
            }
        });
    }

    private void showAlert(String msg) {
        new Alert(Alert.AlertType.WARNING, msg, ButtonType.OK).showAndWait();
    }
}
