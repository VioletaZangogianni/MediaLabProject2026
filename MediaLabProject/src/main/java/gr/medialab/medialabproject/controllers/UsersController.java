package gr.medialab.medialabproject.controllers;

import gr.medialab.medialabproject.model.*;
import gr.medialab.medialabproject.service.DataService;
import gr.medialab.medialabproject.service.JsonDataType;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class UsersController {

    @FXML private TableView<User> usersTable;
    @FXML private TableColumn<User, String> unCol;
    @FXML private TableColumn<User, String> nameCol;
    @FXML private TableColumn<User, String> surnameCol;
    @FXML private TableColumn<User, String> roleCol;

    @FXML private TextField unField;
    @FXML private PasswordField pwField;
    @FXML private TextField nameField;
    @FXML private TextField surnameField;
    @FXML private ComboBox<String> roleCombo;
    @FXML private ListView<String> catsListView;

    @FXML
    public void initialize() {
        unCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("realName"));
        surnameCol.setCellValueFactory(new PropertyValueFactory<>("surname"));
        roleCol.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getRole().toString()));

        roleCombo.setItems(FXCollections.observableArrayList("SIMPLEUSER", "AUTHOR", "ADMIN"));
        roleCombo.setValue("SIMPLEUSER");
        catsListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        ObservableList<String> cats = FXCollections.observableArrayList(
                DataService.getList(JsonDataType.CATEGORIES)
                        .stream()
                        .map(c -> ((Category) c).getName())
                        .collect(Collectors.toList())
        );
        catsListView.setItems(cats);

        catsListView.setPlaceholder(new Label("No categories available"));

        refreshTable();
    }

    private void refreshTable() {
        List<User> users = DataService.getList(JsonDataType.USERS)
                .stream()
                .filter(u -> u instanceof User)
                .map(u -> (User) u)
                .collect(Collectors.toList());
        usersTable.setItems(FXCollections.observableArrayList(users));
    }

    @FXML
    private void handleAddUser() {
        String un   = unField.getText().trim();
        String pw   = pwField.getText();
        String name = nameField.getText().trim();
        String sur  = surnameField.getText().trim();
        String role = roleCombo.getValue();

        ObservableList<String> selectedCats = catsListView.getSelectionModel().getSelectedItems();

        if (un.isEmpty() || pw.isEmpty() || name.isEmpty() || sur.isEmpty()) {
            showAlert("All fields (username, password, name, surname) are required.");
            return;
        }
        if (selectedCats.isEmpty() && (!Objects.equals(role, "ADMIN"))) {
            showAlert("Please select at least one category for this user.\n" +
                    "Tip: hold Ctrl and click to select multiple categories.");
            return;
        }
        if (DataService.getUser(un) != null) {
            showAlert("A user with that username already exists.");
            return;
        }

        List<Category> categories = selectedCats.stream()
                .map(DataService::getCategory)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        User currentUser = MainController.instance.getCurrentUser();
        switch (role) {
            case "ADMIN":
                if(currentUser instanceof Admin a) {
                    a.createUser(un, pw, name, sur, categories, Roles.ADMIN);
                }
                break;
            case "AUTHOR":
                if(currentUser instanceof Admin a) {
                    a.createUser(un, pw, name, sur, categories, Roles.AUTHOR);
                }
                break;
            default:
                if(currentUser instanceof Admin a) {
                    a.createUser(un, pw, name, sur, categories, Roles.SIMPLEUSER);
                }
                break;
        }

        clearForm();
        refreshTable();
    }

    @FXML
    private void handleDeleteUser() {
        User currentUser = MainController.instance.getCurrentUser();
        User selected = usersTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Select a user from the table to delete.");
            return;
        }
        if ("medialab".equals(selected.getName())) {
            showAlert("The default admin account cannot be deleted.");
            return;
        }
        if (currentUser.getName().equals(selected.getName())) {
            showAlert("You cannot delete yourself.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete user \"" + selected.getName() + "\"?",
                ButtonType.YES, ButtonType.NO);
        confirm.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.YES) {
                if(currentUser instanceof Admin a) {
                    a.deleteUser(selected.getName());
                }

                refreshTable();
            }
        });
    }

    private void clearForm() {
        unField.clear();
        pwField.clear();
        nameField.clear();
        surnameField.clear();
        roleCombo.setValue("SIMPLEUSER");
        catsListView.getSelectionModel().clearSelection();
    }

    private void showAlert(String message) {
        new Alert(Alert.AlertType.WARNING, message, ButtonType.OK).showAndWait();
    }
}